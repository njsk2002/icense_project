import 'package:flutter/material.dart';
import 'shoes_store_page.dart';
import 'package:icense_project/app_clone/service/nfc_communication.dart';

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

  @override
  void initState() {
    super.initState();
    bmpImageUrl = "http://192.168.0.136:5000/naverapi/bmp_files/${widget.shoe!.bmpFile}"; // BMP 이미지 경로
  }

  /// **📌 BMP 보기 토글**
  void toggleBmpOverlay() {
    setState(() {
      isBmpView = !isBmpView;
    });
  }

  /// **📌 NFC 전송 버튼 클릭 시 호출**
  Future<void> _sendToNFC() async {
    if (widget.shoe?.bmpFile == null || widget.shoe!.bmpFile!.isEmpty) {
      _showStatusDialog("❌ BMP 파일이 없습니다.");
      return;
    }

    setState(() => isSending = true); // ✅ 로딩 상태 활성화

    String result = await NFCService.startNFCProcess(context, bmpImageUrl, 3); // ✅ BMP 파일 URL을 전달

    setState(() => isSending = false); // ✅ 로딩 상태 해제

    _showStatusDialog(result); // ✅ 결과 메시지 다이얼로그
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
                      top: 40,
                      right: 20,
                      child: IconButton(
                        icon: const Icon(Icons.close, size: 30, color: Colors.black),
                        onPressed: toggleBmpOverlay, // ✅ X 버튼 클릭 시 BMP 오버레이 닫기
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
              left: size.width * 0.3,
              right: size.width * 0.3,
              child: ElevatedButton(
                onPressed: toggleBmpOverlay,
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.black.withOpacity(0.7), // ✅ 반투명 배경
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                  padding: const EdgeInsets.symmetric(vertical: 14),
                ),
                child: const Text(
                  "BMP 보기",
                  style: TextStyle(fontSize: 18, color: Colors.white),
                ),
              ),
            ),
        ],
      ),
    );
  }
}
