import 'dart:convert';
import 'dart:ui';
import 'dart:developer' as developer;
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:icense_project/app_clone/shoes_store/shoes_store_detail_page.dart';
import 'package:vector_math/vector_math.dart' as vector;

const bottomBackgroundColor = Color(0xFFF1F2F7);
const brands = ['e-명함', 'e-카드 만들기', 'e-쉐어카드', '사진', '나의페이지'];
const marginSide = 14.0;

class Shoe {
  final String name, image, bmp_42_mono,bmp_42_3color, bmp_37_4color, bmp_29_4color;
  final double price;
  final Color color;

  Shoe({
    required this.name,
    required this.image,
    required this.price,
    required this.color,
    required this.bmp_42_mono,
    required this.bmp_42_3color,
    required this.bmp_37_4color,
    required this.bmp_29_4color});

  factory Shoe.fromJson(Map<String, dynamic> json) {
    return Shoe(
      name: json['key_word'] ?? 'Unknown',  // ✅ 이름 (기본값: 'Unknown')
      image: json['url'] ?? '',  // ✅ URL을 이미지로 사용
      bmp_42_mono: json['bmp_42_mono'] ?? '',  // ✅ URL을 이미지로 사용
      bmp_42_3color: json['bmp_42_3color'] ?? '',  // ✅ URL을 이미지로 사용
      bmp_37_4color: json['bmp_37_4color'] ?? '',  // ✅ URL을 이미지로 사용
      bmp_29_4color: json['bmp_29_4color'] ?? '',  // ✅ URL을 이미지로 사용
      price: 0.0,  // ✅ 가격이 없으므로 기본값 0.0 사용
      color: Color(0xFF5574b9),  // ✅ JSON에 "color" 값이 없으므로 기본값 지정
    );
  }
}

class ShoesStorePage extends StatefulWidget {
  @override
  _ShoesStorePageState createState() => _ShoesStorePageState();
}

class _ShoesStorePageState extends State<ShoesStorePage> {
  final _pageController = PageController(viewportFraction: 0.78);
  List<Shoe> shoes = [], shoesBottom = [];
  int currentPage = 1;
  bool isLoading = false;

  @override
  void initState() {
    super.initState();
    developer.log("🔥 initState 실행됨", name: "DEBUG_2"); // ✅ initState 확인용 로그
    _fetchShoes();
    _pageController.addListener(() {
      if (_pageController.page! >= shoes.length - 5) _fetchShoes();
    });
  }

  Future<void> _fetchShoes() async {
    if (isLoading) return;
    isLoading = true; // ✅ setState() 전에 isLoading 변경
    developer.log("📢 API 요청: $currentPage 페이지 요청 중", name: "DEBUG_2"); // ✅ 로그 먼저 출력

    //final url = Uri.parse("http://192.168.0.136:5000/naverapi/admin_image?page=$currentPage&per_page=10");
    final url = Uri.parse("http://192.168.219.106:5000/naverapi/admin_image?page=$currentPage&per_page=10");
    //final url = Uri.parse("http://192.168.0.136:5000/naverapi/admin_image");

    try {
      final response = await http.get(url);
      developer.log("📢 응답 코드: ${response.statusCode}", name: "DEBUG_2");
      developer.log("📢 응답 본문: ${response.body}", name: "DEBUG_2");

      if (response.statusCode == 200) {
        final data = json.decode(response.body)['data'] as List;
        setState(() {
          shoes.addAll(data.map((json) => Shoe.fromJson(json)));
          shoesBottom = shoes.take(2).toList();
          currentPage++;
          isLoading = false;
        });
      } else {
        developer.log("❌ 서버 오류: ${response.statusCode}", name: "DEBUG_2", level: 900);
        setState(() => isLoading = false);
      }
    } catch (e) {
      developer.log("❌ 네트워크 오류: $e", name: "DEBUG_2", level: 1000);
      setState(() => isLoading = false);
    }
  }


  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    return Scaffold(
      backgroundColor: Colors.white,
      bottomNavigationBar: _buildBottomNavigationBar(),
      body: Column(
        children: [
          _buildHeader(),
          Expanded(
            child: Stack(
              children: [
                _buildSideNavigation(),
                Positioned(left: 0, bottom: 0, right: 0, child: _buildBottom()),
                Positioned(
                  left: 0,
                  right: 0,
                  top: -10,
                  height: size.height * 0.50,
                  child: PageView.builder(
                    controller: _pageController,
                    itemCount: shoes.length,
                    itemBuilder: (_, index) => _buildShoeCard(shoes[index], size),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildHeader() => Padding(
    padding: const EdgeInsets.all(marginSide),
    child: Column(
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text("ICEnse", style: TextStyle(fontSize: 26, fontWeight: FontWeight.w700)),
            Row(
              children: [
                IconButton(icon: Icon(Icons.search), onPressed: () {}),
                IconButton(icon: Icon(Icons.notifications_none), onPressed: () {}),
              ],
            ),
          ],
        ),
        SizedBox(height: 10),
        SizedBox(
          height: 40,
          child: ListView.builder(
            scrollDirection: Axis.horizontal,
            itemCount: brands.length,
            itemBuilder: (_, index) => Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16.0),
              child: Text(
                brands[index],
                style: TextStyle(fontWeight: FontWeight.w700, color: index == 0 ? Colors.black : Colors.grey[400]),
              ),
            ),
          ),
        ),
      ],
    ),
  );

  Widget _buildShoeCard(Shoe shoe, Size size) => Padding(
    padding: const EdgeInsets.symmetric(vertical: 10.0, horizontal: 5.0),
    child: InkWell(
      onTap: () => Navigator.of(context).push(
        MaterialPageRoute(builder: (_) => ShoesStoreDetailPage(shoe: shoe)),
      ),
      child: Stack(
        children: [
          // ✅ 카드 배경 (색상 & 그림자 적용)
          Card(
            elevation: 6,
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
            color: shoe.color,
            child: SizedBox(height: size.width / 1.8), // ✅ 이미지 크기 조정
          ),

          // ✅ 네트워크 이미지 (Stack 아래 배치)
          ClipRRect(
            borderRadius: BorderRadius.circular(20),
            child: Image.network(
              shoe.image,
              width: double.infinity,
              height: size.width,
              fit: BoxFit.cover, // ✅ 이미지가 카드를 꽉 채우도록 설정
            ),
          ),

          // ✅ 텍스트를 최상단에 배치
          Positioned(
            top: 12,
            left: 12,
            right: 12,
            child: Container(
              padding: EdgeInsets.all(8),
              decoration: BoxDecoration(
                //color: Colors.black.withOpacity(0.5), // ✅ 반투명 배경 추가
                borderRadius: BorderRadius.circular(8),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    shoe.name,
                    style: TextStyle(
                      color: Colors.white,
                      fontSize: 18,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    "\$${shoe.price}",
                    style: TextStyle(
                      color: Colors.white,
                      fontSize: 14,
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    ),
  );



  Widget _buildBottom() => Container(
    color: bottomBackgroundColor,
    height: 140,
    child: Padding(
      padding: const EdgeInsets.symmetric(horizontal: marginSide),
      child: Row(
        children: shoesBottom.map((shoe) => Expanded(child: _buildShoeCard(shoe, MediaQuery.of(context).size))).toList(),
      ),
    ),

  );

  Widget _buildSideNavigation() => Positioned(
    left: 0,
    top: 0,
    child: Padding(
      padding: const EdgeInsets.symmetric(horizontal: 12.0),
      child: RotatedBox(
        quarterTurns: 3,
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: ["New", "Featured", "Upcoming"].map((e) {
            return Padding(
              padding: const EdgeInsets.symmetric(horizontal: 15),
              child: Text(e, style: TextStyle(fontWeight: FontWeight.w700, color: e == "Featured" ? Colors.black : Colors.grey[400])),
            );
          }).toList(),
        ),
      ),
    ),
  );

  Widget _buildBottomNavigationBar() => BottomNavigationBar(
    selectedItemColor: Colors.red,
    backgroundColor: bottomBackgroundColor,
    unselectedItemColor: Colors.grey[400],
    elevation: 4,
    type: BottomNavigationBarType.fixed,
    items: List.generate(5, (index) {
      final icons = [Icons.home, Icons.favorite_border, Icons.location_city, Icons.shopping_cart, Icons.person_outline];
      return BottomNavigationBarItem(label: '', icon: Icon(icons[index]));
    }),
  );
}
