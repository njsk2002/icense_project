import 'dart:io';
import 'dart:typed_data';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:http/http.dart' as http;
import 'package:path_provider/path_provider.dart';
import 'dart:developer' as developer;

class NFCService {
  static const MethodChannel _platform = MethodChannel('kr.co.icense');
  static BuildContext? _context;
  static bool _isDialogShowing = false;
  static StateSetter? _dialogSetState;
  static String _dialogMessage = "🔄 NFC 초기화 중...";
  static bool _isCompleted = false; // ✅ NFC 완료 상태 관리

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

  /// **📌 BMP 파일을 Uint8List로 변환 (저장 후 로드)**
  static Future<Uint8List?> _loadBmpFile(String bmpFileUrl) async {
    try {
      final icesensePath = await _getIcesenseFolderPath();
      final fileName = bmpFileUrl.split('/').last;
      final filePath = '$icesensePath/$fileName';
      final file = File(filePath);

      if (await file.exists()) {
        developer.log("🔍 BMP 파일이 이미 존재: $filePath", name: "DEBUG_2");
        return await file.readAsBytes();
      }

      developer.log("📢 BMP 파일 요청: $bmpFileUrl", name: "DEBUG_2");
      final response = await http.get(Uri.parse(bmpFileUrl));

      if (response.statusCode == 200) {
        await file.writeAsBytes(response.bodyBytes);
        developer.log("✅ BMP 파일 저장 완료: $filePath", name: "DEBUG_2");
        return response.bodyBytes;
      } else {
        developer.log("❌ BMP 파일 로드 실패 (HTTP ${response.statusCode})", name: "DEBUG_2");
        return null;
      }
    } catch (e) {
      developer.log("❌ BMP 파일 로드 중 오류: $e", name: "DEBUG_2");
      return null;
    }
  }

  /// **📌 NFC 프로세스 시작**
  static Future<String> startNFCProcess(BuildContext context, String bmpFileUrl, int displaySize) async {
    _context = context;
    _isCompleted = false; // ✅ NFC 완료 상태 초기화
    if (!_isDialogShowing) {
      _showProgressDialog();
    }

    try {
      Uint8List? imageBytes = await _loadBmpFile(bmpFileUrl);

      if (imageBytes == null || imageBytes.isEmpty) {
        _closeProgressDialog("❌ BMP 파일을 불러오지 못했습니다.");
        return "❌ BMP 파일을 불러오지 못했습니다.";
      }
      if (displaySize == 0) {
        _closeProgressDialog("❌ 디스플레이 크기를 선택하세요.");
        return "❌ 디스플레이 크기를 선택하세요.";
      }

      developer.log("📡 NFC 전송 시작 (파일 크기: ${imageBytes.length})", name: "DEBUG_2");
      _updateProgressDialog("📡 NFC 전송 시작...");

      final bool result = await _platform.invokeMethod('startNFCProcess', {
        "imageData": imageBytes,
        "displaySize": displaySize,
      });

      if (result) {
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
    if (_isCompleted || !_isDialogShowing) return; // ✅ NFC 완료 후 업데이트 방지
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
    _isCompleted = true; // ✅ NFC 완료 상태 설정
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
