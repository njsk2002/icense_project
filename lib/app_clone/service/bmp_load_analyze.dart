import 'dart:io';
import 'dart:typed_data';
import 'package:path_provider/path_provider.dart';
import 'package:http/http.dart' as http;
import 'dart:developer' as developer;
import 'package:image/image.dart' as img;

class BMPFileLoader {
  /// icesense 폴더 경로를 가져오고, 폴더가 없으면 생성합니다.
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

  /// BMP 파일을 로드한 후, 파일 구성 분석과 분류 작업을 수행합니다.
  /// 1. bmpFileUrl을 통해 BMP 파일 데이터를 확보하고,
  /// 2. _analyzeBmp 함수로 해상도 및 색상 정보를 추출한 후,
  /// 3. _bmpClassify 함수를 통해 mapping table에 따른 displaySize와 uniqueColorCount, bmpData를 함께 반환합니다.
  static Future<Map<String, dynamic>?> loadAndAnalyzeBmp(String bmpFileUrl) async {
    try {
      final icesensePath = await _getIcesenseFolderPath();
      final fileName = bmpFileUrl.split('/').last;
      final filePath = '$icesensePath/$fileName';
      final file = File(filePath);
      Uint8List? bmpData;

      if (await file.exists()) {
        developer.log("🔍 BMP 파일이 이미 존재: $filePath", name: "DEBUG_2");
        bmpData = await file.readAsBytes();
      } else {
        developer.log("📢 BMP 파일 요청: $bmpFileUrl", name: "DEBUG_2");
        final response = await http.get(Uri.parse(bmpFileUrl));
        if (response.statusCode == 200) {
          bmpData = response.bodyBytes;
          await file.writeAsBytes(bmpData);
          developer.log("✅ BMP 파일 저장 완료: $filePath", name: "DEBUG_2");
        } else {
          developer.log("❌ BMP 파일 로드 실패 (HTTP ${response.statusCode})", name: "DEBUG_2");
          return null;
        }
      }

      // 이미지 구성 분석
      final bmpAnalyzeData = _analyzeBmp(bmpData);
      // 분류 함수 호출하여 uniqueColorCount, displaySize, bmpData를 함께 반환
      return _bmpClassify(bmpAnalyzeData, bmpData);
    } catch (e) {
      developer.log("❌ BMP 파일 로드 중 오류: $e", name: "DEBUG_2");
      return null;
    }
  }

  /// BMP 데이터를 분석하여 해상도, 고유 색상 수, 색상 목록, 이미지 유형을 반환합니다.
  /// 반환 예시: { 'width': 400, 'height': 300, 'uniqueColorCount': 3, 'colors': {0x0, 0xFFFFFF, 0xFF0000}, 'type': '3-color' }
  static Map<String, dynamic> _analyzeBmp(Uint8List bmpData) {
    final image = img.decodeBmp(bmpData);
    if (image == null) {
      throw Exception('BMP 이미지를 디코딩할 수 없습니다.');
    }
    int width = image.width;
    int height = image.height;

    // 모든 픽셀을 순회하여 고유 색상(RGB)을 수집합니다.
    Set<int> uniqueColors = {};
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        final pixel = image.getPixel(x, y); // Pixel 객체 가져오기
        int r = pixel.r.toInt(); // `num` → `int` 변환
        int g = pixel.g.toInt();
        int b = pixel.b.toInt();

        int rgb = (r << 16) | (g << 8) | b; // RGB 값을 정수로 변환
        uniqueColors.add(rgb);
      }
    }



    return {
      'width': width,
      'height': height,
      'uniqueColorCount': uniqueColors.length,
      'colors': uniqueColors,
      // type은 추후 필요에 따라 추가할 수 있습니다.
    };
  }

  /// bmpAnalyzeData와 bmpData를 기반으로 displaySize를 매핑하여 최종 결과를 반환합니다.
  /// mapping table 예시:
  /// width: 250, height: 122
  ///   uniqueColorCount = 2 → displaySize = 2132
  ///   uniqueColorCount = 3 → displaySize = 2133
  ///   uniqueColorCount = 4 → displaySize = 2134
  ///
  /// width: 296, height: 128
  ///   uniqueColorCount = 2 → displaySize = 2902
  ///   uniqueColorCount = 3 → displaySize = 2903
  ///   uniqueColorCount = 4 → displaySize = 2904
  ///
  /// width: 416, height: 240
  ///   uniqueColorCount = 2 → displaySize = 3702
  ///   uniqueColorCount = 3 → displaySize = 3703
  ///   uniqueColorCount = 4 → displaySize = 3704
  ///
  /// width: 400, height: 300
  ///   uniqueColorCount = 2 → displaySize = 4202
  ///   uniqueColorCount = 3 → displaySize = 4203
  ///   uniqueColorCount = 4 → displaySize = 4204
  static Map<String, dynamic> _bmpClassify(
      Map<String, dynamic> bmpAnalyzeData, Uint8List bmpData) {
    int width = bmpAnalyzeData['width'];
    int height = bmpAnalyzeData['height'];
    int uniqueColorCount = bmpAnalyzeData['uniqueColorCount'];
    int displaySize = 0;

    if (width == 250 && height == 122) {
      if (uniqueColorCount == 2) {
        displaySize = 2132;
      } else if (uniqueColorCount == 3) {
        displaySize = 2133;
      } else if (uniqueColorCount == 4) {
        displaySize = 2134;
      }
    } else if (width == 296 && height == 128) {
      if (uniqueColorCount == 2) {
        displaySize = 2902;
      } else if (uniqueColorCount == 3) {
        displaySize = 2903;
      } else if (uniqueColorCount == 4) {
        displaySize = 2904;
      }
    } else if (width == 416 && height == 240) {
      if (uniqueColorCount == 2) {
        displaySize = 3702;
      } else if (uniqueColorCount == 3) {
        displaySize = 3703;
      } else if (uniqueColorCount == 4) {
        displaySize = 3704;
      }
    } else if (width == 400 && height == 300) {
      if (uniqueColorCount == 2) {
        displaySize = 4202;
      } else if (uniqueColorCount == 3) {
        displaySize = 4203;
      } else if (uniqueColorCount == 4) {
        displaySize = 4204;
      }
    }

    return {
      'uniqueColorCount': uniqueColorCount,
      'displaySize': displaySize,
      'bmpData': bmpData,
    };
  }
}
