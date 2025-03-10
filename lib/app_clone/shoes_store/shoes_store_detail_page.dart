import 'package:flutter/material.dart';
import 'shoes_store_page.dart';
import 'package:icense_project/app_clone/service/nfc_communication.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;

class ShoesStoreDetailPage extends StatefulWidget {
  final Shoe? shoe;

  const ShoesStoreDetailPage({Key? key, this.shoe}) : super(key: key);

  @override
  _ShoesStoreDetailPageState createState() => _ShoesStoreDetailPageState();
}

class _ShoesStoreDetailPageState extends State<ShoesStoreDetailPage> {
  bool isBmpView = false; // BMP 오버레이 활성화 상태
  late String bmpImageUrl;
  bool isSending = false; // NFC 전송 중 상태 관리

  late final String url42Mono;
  late final String url42Color;
  late final String url37Color;

  String? selectedBmpUrl; // 사용자가 선택한 BMP URL


  @override
  void initState() {
    super.initState();
    // 기본 bmpImageUrl (필요 시 사용)
    bmpImageUrl = "http://192.168.0.136:5000/naverapi/get_bmp";
    // 각각의 버튼에 해당하는 BMP 이미지 경로
    // 하나의 공통 엔드포인트로 사용 (POST 방식으로 style과 bmp_file을 전달할 예정)
    url42Mono  = "http://192.168.0.136:5000/naverapi/get_bmp";
    url42Color = "http://192.168.0.136:5000/naverapi/get_bmp";
    url37Color = "http://192.168.0.136:5000/naverapi/get_bmp";

  }

  /// **📌 BMP 보기 토글**
  /// newUrl 파라미터가 전달되면 해당 URL을 bmpImageUrl에 할당하고 오버레이를 켭니다.
  /// 파라미터 없이 호출되면 현재 상태를 토글합니다.
  void toggleBmpOverlay({String? bmpurl}) async {
    if (bmpurl != null) {
      setState(() {
        bmpImageUrl =
        "http://192.168.219.106:5000/naverapi/get_bmp?key_word=${Uri.encodeComponent(widget.shoe!.name)}&bmp_file=${Uri.encodeComponent(bmpurl)}&t=${DateTime.now().millisecondsSinceEpoch}";
        isBmpView = true;
        selectedBmpUrl = bmpurl;
      });
    } else {
      setState(() {
        isBmpView = false;
        selectedBmpUrl = null;
      });
    }
  }



  // void toggleBmpOverlay({String? bmpurl}) async {
  //   if (bmpurl != null) {
  //     final url = Uri.parse("http://192.168.219.106:5000/naverapi/get_bmp");
  //     final body = json.encode({
  //       "key_word": widget.shoe!.name,
  //       "bmp_file": bmpurl,
  //     });
  //
  //     try {
  //       final response = await http.post(
  //         url,
  //         headers: {"Content-Type": "application/json"},
  //         body: body,
  //       );
  //
  //       if (response.statusCode == 200) {
  //         // ✅ JSON 디코딩 제거 (서버가 직접 이미지 바이너리를 보내줌)
  //         setState(() {
  //           bmpImageUrl = url.toString(); // 요청한 URL 자체가 이미지 소스가 됨
  //           isBmpView = true;
  //           selectedBmpUrl = bmpurl; // 선택한 BMP 저장
  //         });
  //       } else {
  //         _showStatusDialog("BMP 파일을 가져오지 못했습니다 (Status: ${response.statusCode}).");
  //       }
  //     } catch (e) {
  //       _showStatusDialog("네트워크 오류 발생: $e");
  //     }
  //   } else {
  //     setState(() {
  //       isBmpView = false;
  //       selectedBmpUrl = null;
  //     });
  //   }
  // }






  /// **📌 NFC 전송 버튼 클릭 시 호출**
  Future<void> _sendToNFC() async {
    if (selectedBmpUrl == null || selectedBmpUrl!.isEmpty) {
      _showStatusDialog("❌ 선택된 BMP 파일이 없습니다.");
      return;
    }

    setState(() => isSending = true);

    String result = await NFCService.startNFCProcess(context, bmpImageUrl, 3);

    setState(() => isSending = false);

    _showStatusDialog(result);
  }


  /// **📌 상태 메시지 다이얼로그**
  void _showStatusDialog(String message) {
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text("NFC 전송 결과"),
          content: Text(message),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(context).pop(),
              child: const Text("확인"),
            ),
          ],
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;

    return Scaffold(
      body: Stack(
        fit: StackFit.expand,
        children: [
          // 🔹 기존 이미지 (배경)
          Positioned.fill(
            child: ColorFiltered(
              colorFilter: isBmpView
                  ? ColorFilter.mode(Colors.white.withOpacity(0.5), BlendMode.srcATop) // ✅ 연하게 만들기
                  : ColorFilter.mode(Colors.transparent, BlendMode.dst),
              child: Image.network(
                widget.shoe!.image,
                fit: BoxFit.cover, // ✅ 전체 화면 채우기
              ),
            ),
          ),

          // 🔹 BMP 이미지 오버레이 (isBmpView가 true일 때 표시)
          if (isBmpView)
            AnimatedOpacity(
              duration: const Duration(milliseconds: 300),
              opacity: isBmpView ? 1.0 : 0.0,
              child: Container(
                color: Colors.white.withOpacity(0.5), // ✅ 반투명 흰색 배경
                child: Stack(
                  children: [
                    // 🔹 BMP 이미지 (중앙 정렬)
                    Center(
                      child: Container(
                        padding: const EdgeInsets.all(16),
                        child: Image.network(
                          bmpImageUrl,
                          fit: BoxFit.contain, // ✅ BMP 원본 비율 유지
                        ),
                      ),
                    ),


                    // 🔹 X 버튼 (오버레이 닫기)
                    Positioned(
                      top: 200,
                      right: 20,
                      child: IconButton(
                        icon: const Icon(Icons.close, size: 50, color: Colors.blueGrey),
                        onPressed: () => toggleBmpOverlay(), // 파라미터 없이 호출 → 오버레이 닫기
                      ),
                    ),


                    // 🔹 NFC 전송 버튼 (하단 중앙)
                    Positioned(
                      bottom: 30,
                      left: size.width * 0.25,
                      right: size.width * 0.25,
                      child: ElevatedButton.icon(
                        onPressed: isSending ? null : _sendToNFC, // ✅ NFC 전송 함수 호출 (전송 중이면 비활성화)
                        style: ElevatedButton.styleFrom(
                          backgroundColor: isSending ? Colors.grey : Colors.blueAccent,
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(12),
                          ),
                          padding: const EdgeInsets.symmetric(vertical: 14),
                        ),
                        icon: isSending
                            ? const SizedBox(
                          width: 24,
                          height: 24,
                          child: CircularProgressIndicator(
                            color: Colors.white, // ✅ 로딩 인디케이터 추가
                            strokeWidth: 2.5,
                          ),
                        )
                            : const Icon(Icons.nfc, color: Colors.white),
                        label: Text(
                          isSending ? "전송 중..." : "📡 NFC 전송",
                          style: const TextStyle(fontSize: 18, color: Colors.white),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),

          // 🔹 상단 AppBar (뒤로 가기 + 좋아요 버튼)
          Align(
            alignment: Alignment.topCenter,
            child: SizedBox(
              height: kToolbarHeight + 20,
              child: AppBar(
                backgroundColor: Colors.transparent,
                elevation: 0,
                title: Text(
                  widget.shoe!.name,
                  style: const TextStyle(
                    color: Colors.white,
                    fontSize: 25,
                    fontWeight: FontWeight.w700,
                  ),
                ),
                actions: [
                  Padding(
                    padding: const EdgeInsets.only(right: 14.0),
                    child: Material(
                      elevation: 10,
                      shape: CircleBorder(
                        side: BorderSide(color: Colors.white),
                      ),
                      color: Colors.transparent,
                      child: const Padding(
                        padding: EdgeInsets.all(5.0),
                        child: Icon(Icons.favorite_border, color: Colors.white),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),

          // 🔹 하단에 신발 정보 표시
          Positioned(
            bottom: 90,
            left: 20,
            right: 20,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  widget.shoe!.name,
                  style: const TextStyle(
                    fontSize: 32,
                    fontWeight: FontWeight.bold,
                    color: Colors.white,
                  ),
                ),
                const SizedBox(height: 8),
                Text(
                  "\$${widget.shoe!.price}",
                  style: const TextStyle(
                    fontSize: 26,
                    fontWeight: FontWeight.w600,
                    color: Colors.white,
                  ),
                ),
              ],
            ),
          ),

          // 🔹 BMP 버튼 (이미지 오버레이 토글, isBmpView가 false일 때만 표시)
    if (!isBmpView)
      Positioned(
        bottom: 30,
        left: 20,
        right: 20,
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            // "4.2 Mono" 버튼 (단색: 검정)
            Expanded(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 4.0),
                child: ElevatedButton(
                  onPressed: () => toggleBmpOverlay(bmpurl: widget.shoe!.bmp_42_mono),
                  style: ElevatedButton.styleFrom(
                    minimumSize: const Size(0, 60), // 유연한 너비, 최소 높이 60
                    backgroundColor: Colors.black.withOpacity(0.7),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                    padding: const EdgeInsets.symmetric(vertical: 14),
                  ),
                  child: const Text(
                    "4.2 Mono",
                    style: TextStyle(fontSize: 18, color: Colors.white),
                  ),
                ),
              ),
            ),
            // "4.2 Color" 버튼 (반반 사선 분할: 좌상=검정, 우하=레드)
            Expanded(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 4.0),
                child: Container(
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(12),
                    // LinearGradient의 hard stop을 이용해 색상이 섞이지 않도록 함
                    gradient: LinearGradient(
                      begin: Alignment.topLeft,
                      end: Alignment.bottomRight,
                      colors: [
                        Colors.black.withOpacity(0.7),
                        Colors.black.withOpacity(0.7),
                        Colors.red.withOpacity(0.7),
                        Colors.red.withOpacity(0.7),
                      ],
                      stops: const [0.0, 0.5, 0.5, 1.0],
                    ),
                  ),
                  child: ElevatedButton(
                    onPressed: () => toggleBmpOverlay(bmpurl: widget.shoe!.bmp_42_3color),
                    style: ElevatedButton.styleFrom(
                      minimumSize: const Size(0, 60),
                      backgroundColor: Colors.transparent,
                      shadowColor: Colors.transparent,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                      padding: const EdgeInsets.symmetric(vertical: 14),
                    ),
                    child: const Text(
                      "4.2 Color",
                      style: TextStyle(fontSize: 18, color: Colors.white),
                    ),
                  ),
                ),
              ),
            ),
            // "3.7 Color" 버튼 (1/3씩 사선 분할: 좌상=검정, 중간=레드, 우하=노랑)
            Expanded(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 4.0),
                child: Container(
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(12),
                    gradient: LinearGradient(
                      begin: Alignment.topLeft,
                      end: Alignment.bottomRight,
                      // hard stop을 위해 각 색상을 2회씩 반복
                      colors: [
                        Colors.black.withOpacity(0.7),
                        Colors.black.withOpacity(0.7),
                        Colors.red.withOpacity(0.7),
                        Colors.red.withOpacity(0.7),
                        Colors.yellow.withOpacity(0.7),
                        Colors.yellow.withOpacity(0.7),
                      ],
                      // 각 색상이 차지하는 구간을 명확하게 지정 (예: 0~33%, 33~66%, 66~100%)
                      stops: const [0.0, 0.33, 0.33, 0.66, 0.66, 1.0],
                    ),
                  ),
                  child: ElevatedButton(
                    onPressed: () => toggleBmpOverlay(bmpurl: widget.shoe!.bmp_37_4color),
                    style: ElevatedButton.styleFrom(
                      minimumSize: const Size(0, 60),
                      backgroundColor: Colors.transparent,
                      shadowColor: Colors.transparent,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                      padding: const EdgeInsets.symmetric(vertical: 14),
                    ),
                    child: const Text(
                      "3.7 Color",
                      style: TextStyle(fontSize: 18, color: Colors.white),
                    ),
                  ),
                ),
              ),
            ),
            // "2.9 Color" 버튼 (1/3씩 사선 분할: 좌상=검정, 중간=레드, 우하=노랑)
            Expanded(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 4.0),
                child: Container(
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(12),
                    gradient: LinearGradient(
                      begin: Alignment.topLeft,
                      end: Alignment.bottomRight,
                      // hard stop을 위해 각 색상을 2회씩 반복
                      colors: [
                        Colors.black.withOpacity(0.7),
                        Colors.black.withOpacity(0.7),
                        Colors.red.withOpacity(0.7),
                        Colors.red.withOpacity(0.7),
                        Colors.yellow.withOpacity(0.7),
                        Colors.yellow.withOpacity(0.7),
                      ],
                      // 각 색상이 차지하는 구간을 명확하게 지정 (예: 0~33%, 33~66%, 66~100%)
                      stops: const [0.0, 0.33, 0.33, 0.66, 0.66, 1.0],
                    ),
                  ),
                  child: ElevatedButton(
                    onPressed: () => toggleBmpOverlay(bmpurl: widget.shoe!.bmp_29_4color),
                    style: ElevatedButton.styleFrom(
                      minimumSize: const Size(0, 60),
                      backgroundColor: Colors.transparent,
                      shadowColor: Colors.transparent,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                      padding: const EdgeInsets.symmetric(vertical: 14),
                    ),
                    child: const Text(
                      "2.9 Color",
                      style: TextStyle(fontSize: 18, color: Colors.white),
                    ),
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
        ],
      ),
    );
  }
}
