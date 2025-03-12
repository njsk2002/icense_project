import 'dart:io';
import 'dart:typed_data';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:http/http.dart' as http;
import 'package:path_provider/path_provider.dart';
import 'package:icense_project/app_clone/service/bmp_load_analyze.dart';
import 'dart:developer' as developer;

class NFCColorService {
  static const MethodChannel _platform = MethodChannel('kr.co.icense');
  static BuildContext? _context;
  static bool _isDialogShowing = false;
  static StateSetter? _dialogSetState;
  static String _dialogMessage = "🔄 NFC 초기화 중...";
  static bool _isCompleted = false; // NFC 완료 상태 관리

  /// **📌 icesense 폴더 경로 가져오기**
  static Future<String> _getIcesenseFolderPath() async {
    final directory = await getApplicationDocumentsDirectory();
    final icesensePath = '${directory.path}/icesense';

    final icesenseDir = Directory(icesensePath);
    if (!await icesenseDir.exists()) {
      await icesenseDir.create(recursive: true);
      developer.log("📁 'icesense' 폴더 생성됨: $icesensePath", name: "DEBUG_2");
    }

    return icesensePath;
  }

  /// **📌 NFC 프로세스 시작**
  ///
  /// bmpFileUrl를 통해 BMP 파일을 다운로드(또는 캐시에서 로드)하고, BMP 분석 결과(Map)
  /// { 'uniqueColorCount', 'displaySize', 'bmpData' }를 이용해 플랫폼 채널에 전달합니다.
  static Future<String> startNFCProcess(BuildContext context, String bmpFileUrl, int displaySizeParam) async {
    _context = context;
    _isCompleted = false; // NFC 완료 상태 초기화
    if (!_isDialogShowing) {
      _showProgressDialog();
    }

    try {
      // BMP 파일 다운로드 및 분석 → bmpResult에는 { uniqueColorCount, displaySize, bmpData }가 포함됨
      Map<String, dynamic>? bmpResult = await BMPFileLoader.loadAndAnalyzeBmp(bmpFileUrl);

      if (bmpResult == null ||
          bmpResult['bmpData'] == null ||
          (bmpResult['bmpData'] as Uint8List).isEmpty) {
        _closeProgressDialog("❌ BMP 파일을 불러오지 못했습니다.");
        return "❌ BMP 파일을 불러오지 못했습니다.";
      }
      // 분석된 displaySize를 사용 (필요 시 외부 파라미터와 비교 가능)
      int displaySize = bmpResult['displaySize'];
      Uint8List imageBytes = bmpResult['bmpData'];

      developer.log("📡 NFC 전송 시작 (파일 크기: ${imageBytes.length}, displaySize: $displaySize)", name: "DEBUG_2");
      _updateProgressDialog("📡 NFC 전송 시작...");

      final bool success = await _platform.invokeMethod('startNFCProcess', {
        "imageData": imageBytes,
        "displaySize": displaySize,
      });

      if (success) {
        _closeProgressDialog("✅ NFC 전송 완료");
        return "✅ NFC 전송 완료";
      } else {
        _closeProgressDialog("❌ NFC 전송 실패");
        return "❌ NFC 전송 실패";
      }
    } on PlatformException catch (e) {
      _closeProgressDialog("❌ NFC 오류: ${e.message}");
      return "❌ NFC 오류: ${e.message}";
    } catch (e) {
      _closeProgressDialog("❌ NFC 예외 발생: $e");
      return "❌ NFC 예외 발생: $e";
    }
  }

  /// **📌 NFC 진행률 업데이트 (Java에서 호출)**
  static void updateNFCProgress(String message) {
    if (_isCompleted || !_isDialogShowing) return; // NFC 완료 후 업데이트 방지
    _updateProgressDialog(message);
  }

  /// **📌 NFC 진행 상태 팝업 띄우기**
  static void _showProgressDialog() {
    if (_context == null || _isDialogShowing) return;
    _isDialogShowing = true;
    _dialogMessage = "🔄 NFC 초기화 중...";

    showDialog(
      context: _context!,
      barrierDismissible: false,
      builder: (BuildContext context) {
        return StatefulBuilder(
          builder: (BuildContext context, StateSetter setState) {
            _dialogSetState = setState;
            return AlertDialog(
              title: const Text("🔄 NFC 전송 상태"),
              content: Text(_dialogMessage),
              actions: [
                TextButton(
                  onPressed: () {
                    _isDialogShowing = false;
                    Navigator.of(context).pop();
                  },
                  child: const Text("닫기"),
                ),
              ],
            );
          },
        );
      },
    );
  }

  /// **📌 NFC 진행 상태 업데이트**
  static void _updateProgressDialog(String message) {
    _dialogMessage = message;
    if (_dialogSetState != null && _isDialogShowing) {
      WidgetsBinding.instance.addPostFrameCallback((_) {
        _dialogSetState!(() {});
      });
    }
  }

  /// **📌 NFC 완료 후 팝업 닫기**
  static void _closeProgressDialog(String message) {
    _isCompleted = true; // NFC 완료 상태 설정
    _dialogMessage = message;
    if (_dialogSetState != null && _isDialogShowing) {
      WidgetsBinding.instance.addPostFrameCallback((_) {
        _dialogSetState!(() {});
      });
    }

    Future.delayed(const Duration(milliseconds: 500), () {
      if (_isDialogShowing && _context != null) {
        _isDialogShowing = false;
        Navigator.of(_context!).pop();
      }
    });
  }
}
