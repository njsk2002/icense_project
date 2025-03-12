// import 'dart:convert';
// import 'dart:ui';
// import 'dart:developer' as developer;
// import 'package:flutter/material.dart';
// import 'package:http/http.dart' as http;
// import 'package:icense_project/app_clone/shoes_store/shoes_store_detail_page.dart';
// import 'package:icense_project/app_clone/android_messages/android_messages_page.dart';
// import 'package:icense_project/app_clone/movies_concept/movies_concept_page.dart';
// import 'package:icense_project/app_clone/credit_cards_concept/credit_cards_concept_page.dart';
// import 'package:icense_project/app_clone/travel_concept/travel_concept_page.dart';
// import 'package:vector_math/vector_math.dart' as vector;
//
// import 'package:url_launcher/url_launcher.dart'; // 외부 URL 열기 위해 추가
//
// const bottomBackgroundColor = Color(0xFFF1F2F7);
// const brands = ['e-명함', 'e-카드 만들기', 'e-쉐어카드', '사진', '나의페이지'];
// const marginSide = 14.0;
//
// /// Shoe 모델 클래스
// class Shoe {
//   final String name, image, bmp_42_mono, bmp_42_3color, bmp_37_4color, bmp_29_4color;
//   final double price;
//   final Color color;
//
//   Shoe({
//     required this.name,
//     required this.image,
//     required this.price,
//     required this.color,
//     required this.bmp_42_mono,
//     required this.bmp_42_3color,
//     required this.bmp_37_4color,
//     required this.bmp_29_4color,
//   });
//
//   factory Shoe.fromJson(Map<String, dynamic> json) {
//     return Shoe(
//       name: json['key_word'] ?? 'Unknown',
//       image: json['url'] ?? '',
//       bmp_42_mono: json['bmp_42_mono'] ?? '',
//       bmp_42_3color: json['bmp_42_3color'] ?? '',
//       bmp_37_4color: json['bmp_37_4color'] ?? '',
//       bmp_29_4color: json['bmp_29_4color'] ?? '',
//       price: 0.0,
//       color: const Color(0xFF5574b9),
//     );
//   }
// }
//
// /// 메인 ShoesStorePage 화면
// class ShoesStorePage extends StatefulWidget {
//   @override
//   _ShoesStorePageState createState() => _ShoesStorePageState();
// }
//
// class _ShoesStorePageState extends State<ShoesStorePage> {
//   final _pageController = PageController(viewportFraction: 0.78);
//   List<Shoe> shoes = [];
//   List<Shoe> shoesBottom = [];
//   int currentPage = 1;
//   bool _isLoading = false; // 로딩 상태
//   String selectedShoe = ""; // 선택된 신발 이름
//   // fetchedShoes는 호출 결과를 임시 저장(디버깅용)
//   List<Shoe> fetchedShoes = [];
//
//   @override
//   void initState() {
//     super.initState();
//     developer.log("🔥 initState 실행됨", name: "DEBUG_2");
//     // 초기 데이터 로드
//     _fetchShoes().then((_) {
//       developer.log("✅ 초기 _fetchShoes 완료", name: "DEBUG_2");
//     });
//     _pageController.addListener(() {
//       // 현재 페이지 인덱스 (실수형이므로 int로 변환)
//       int currentIndex = _pageController.page!.round();
//       // shoes 리스트에 currentIndex가 존재하는지 확인 후 진행
//       if (currentIndex < shoes.length &&
//           _pageController.page! >= shoes.length - 5) {
//         String currentShoeName = shoes[currentIndex].name;
//         _fetchShoes(keyword: currentShoeName);
//       }
//     });
//
//   }
//
//   /// 서버에서 데이터를 가져와 shoes와 shoesBottom을 업데이트한다.
//   /// keyword가 지정되면 해당 키워드에 따른 데이터를 가져온다.
//   Future<List<Shoe>> _fetchShoes({String? keyword}) async {
//     developer.log("🔎 _fetchShoes 시작, keyword: $keyword", name: "DEBUG_2");
//
//     String baseUrl = "http://192.168.0.136:5000/naverapi/admin_image?page=$currentPage&per_page=10";
//     String keyWordParam = (keyword != null && keyword.isNotEmpty)
//         ? "&key_word=${Uri.encodeComponent(keyword)}"
//         : "";
//     final url = Uri.parse("$baseUrl$keyWordParam");
//     developer.log("📢 API 요청 URL: $url", name: "DEBUG_2");
//
//     try {
//       final response = await http.get(url).timeout(const Duration(seconds: 10));
//       developer.log("📢 응답 코드: ${response.statusCode}", name: "DEBUG_2");
//       developer.log("📢 응답 본문: ${response.body}", name: "DEBUG_2");
//
//       if (response.statusCode == 200) {
//         final data = json.decode(response.body)['data'] as List;
//         final userkey = json.decode(response.body)['unique_key'] as List;
//
//         List<Shoe> newShoes = data.map((json) => Shoe.fromJson(json)).toList();
//         List<Shoe> newShoesBottom = userkey.map((json) => Shoe(
//           name: json['key_word'] ?? 'Unknown',
//           image: json['url'] ?? '',
//           bmp_42_mono: json.containsKey('bmp_42_mono') ? json['bmp_42_mono'] : '',
//           bmp_42_3color: json.containsKey('bmp_42_3color') ? json['bmp_42_3color'] : '',
//           bmp_37_4color: json.containsKey('bmp_37_4color') ? json['bmp_37_4color'] : '',
//           bmp_29_4color: json.containsKey('bmp_29_4color') ? json['bmp_29_4color'] : '',
//           price: json.containsKey('price') ? (json['price'] as num).toDouble() : 0.0,
//           color: const Color(0xFF5574b9),
//         )).toList();
//
//         developer.log("✅ 새 데이터 로드 성공, newShoes 개수: ${newShoes.length}", name: "DEBUG_2");
//         setState(() {
//           // 상단과 하단 모두 새 데이터로 업데이트
//           shoes
//             ..clear()
//             ..addAll(newShoes);
//           shoesBottom
//             ..clear()
//             ..addAll(newShoesBottom);
//           currentPage++;
//           // _isLoading은 onTap에서 관리됨.
//         });
//         return newShoes;
//       } else {
//         developer.log("❌ 서버 오류: ${response.statusCode}", name: "DEBUG_2", level: 900);
//         return [];
//       }
//     } catch (e) {
//       developer.log("❌ 네트워크 오류 또는 타임아웃 발생: $e", name: "DEBUG_2", level: 1000);
//       return [];
//     }
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     final size = MediaQuery.of(context).size;
//     return Scaffold(
//       backgroundColor: Colors.white,
//       bottomNavigationBar: _buildBottomNavigationBar(),
//       body: Column(
//         children: [
//           _buildHeader(),
//           Expanded(
//             child: Stack(
//               children: [
//                 _buildSideNavigation(),
//                 // 하단 바 (인덱스) 위치
//                 Positioned(left: 0, bottom: 0, right: 0, child: _buildBottom()),
//                 // 상단 PageView: shoes 리스트를 _buildShoeCard로 표시
//                 Positioned(
//                   left: 0,
//                   right: 0,
//                   top: -10,
//                   height: size.height * 0.50,
//                   child: PageView.builder(
//                     controller: _pageController,
//                     itemCount: shoes.length,
//                     itemBuilder: (_, index) => _buildShoeCard(shoes[index], size),
//                   ),
//                 ),
//               ],
//             ),
//           ),
//         ],
//       ),
//     );
//   }
//
//   /// 헤더 위젯
//   /// - "ICEnse"를 터치하면 외부 URL (http://192.168.0.136:5000/)로 이동
//   /// - 브랜드 리스트 각 항목 터치 시 해당 페이지로 이동
//   Widget _buildHeader() => Padding(
//     padding: const EdgeInsets.all(marginSide),
//     child: Column(
//       children: [
//         Row(
//           mainAxisAlignment: MainAxisAlignment.spaceBetween,
//           children: [
//             // "ICEnse" 텍스트에 터치 이벤트 추가
//             GestureDetector(
//               onTap: () async {
//                 const url = "http://192.168.0.136:5000/";
//                 if (await canLaunch(url)) {
//                   await launch(url);
//                 } else {
//                   developer.log("URL을 열 수 없습니다: $url", name: "DEBUG_2");
//                 }
//               },
//               child: const Text(
//                 "ICEnse",
//                 style: TextStyle(fontSize: 26, fontWeight: FontWeight.w700),
//               ),
//             ),
//             Row(
//               children: [
//                 IconButton(
//                     icon: const Icon(Icons.search), onPressed: () {}),
//                 IconButton(
//                     icon: const Icon(Icons.notifications_none),
//                     onPressed: () {}),
//               ],
//             ),
//           ],
//         ),
//         const SizedBox(height: 10),
//         // 브랜드 리스트 (가로 스크롤)
//         SizedBox(
//           height: 40,
//           child: ListView.builder(
//             scrollDirection: Axis.horizontal,
//             itemCount: brands.length,
//             itemBuilder: (_, index) => Padding(
//               padding: const EdgeInsets.symmetric(horizontal: 16.0),
//               child: GestureDetector(
//                 onTap: () {
//                   // index에 따라 다른 페이지로 이동
//                   if (index == 0) {
//                     Navigator.push(
//                       context,
//                       MaterialPageRoute(
//                           builder: (_) => ShoesStorePage()),
//                     );
//                   } else if (index == 1) {
//                     Navigator.push(
//                       context,
//                       MaterialPageRoute(
//                           builder: (_) => CreditCardConceptPage()),
//                     );
//                   } else if (index == 2) {
//                     Navigator.push(
//                       context,
//                       MaterialPageRoute(
//                           builder: (_) => MoviesConceptPage()),
//                     );
//                   } else if (index == 3) {
//                     Navigator.push(
//                       context,
//                       MaterialPageRoute(
//                           builder: (_) => AndroidMessagesPage()),
//                     );
//                   } else if (index == 4) {
//                     Navigator.push(
//                       context,
//                       MaterialPageRoute(
//                           builder: (_) => TravelConceptPage()),
//                     );
//                   }
//                 },
//                 child: Text(
//                   brands[index],
//                   style: TextStyle(
//                     fontWeight: FontWeight.w700,
//                     color: index == 0 ? Colors.black : Colors.grey[400],
//                   ),
//                 ),
//               ),
//             ),
//           ),
//         ),
//       ],
//     ),
//   );
//
//
//   /// 상단 PageView에 표시될 신발 카드 (상세 페이지로 이동)
//   Widget _buildShoeCard(Shoe shoe, Size size, {bool disableInkWell = false}) =>
//       Padding(
//         padding: const EdgeInsets.symmetric(vertical: 10.0, horizontal: 5.0),
//         child: InkWell(
//           onTap: disableInkWell
//               ? null
//               : () => Navigator.of(context).push(
//             MaterialPageRoute(
//                 builder: (_) => ShoesStoreDetailPage(shoe: shoe)),
//           ),
//           child: Stack(
//             children: [
//               Card(
//                 elevation: 6,
//                 shape: RoundedRectangleBorder(
//                     borderRadius: BorderRadius.circular(20)),
//                 color: shoe.color,
//                 child: SizedBox(height: size.width / 1.8),
//               ),
//               ClipRRect(
//                 borderRadius: BorderRadius.circular(20),
//                 child: Image.network(
//                   shoe.image,
//                   width: double.infinity,
//                   height: size.width,
//                   fit: BoxFit.cover,
//                 ),
//               ),
//               Positioned(
//                 top: 12,
//                 left: 12,
//                 right: 12,
//                 child: Container(
//                   padding: const EdgeInsets.all(8),
//                   child: Column(
//                     crossAxisAlignment: CrossAxisAlignment.start,
//                     children: [
//                       Text(
//                         shoe.name,
//                         style: const TextStyle(
//                             color: Colors.white,
//                             fontSize: 18,
//                             fontWeight: FontWeight.w600),
//                       ),
//                       const SizedBox(height: 4),
//                       Text(
//                         "\$${shoe.price}",
//                         style: const TextStyle(
//                             color: Colors.white, fontSize: 14),
//                       ),
//                     ],
//                   ),
//                 ),
//               ),
//             ],
//           ),
//         ),
//       );
//
//   /// 하단 바에 표시될 인덱스 카드 (클릭 시 새 데이터 로드)
//   Widget _buildBottom() => Container(
//     color: bottomBackgroundColor,
//     height: 140,
//     child: Padding(
//       padding: const EdgeInsets.symmetric(horizontal: marginSide),
//       child: Row(
//         children: shoesBottom.map((shoe) => Expanded(
//           child: GestureDetector(
//             onTap: () async {
//               developer.log("버튼 클릭 직전, _isLoading: $_isLoading",
//                   name: "DEBUG_2");
//               if (_isLoading) {
//                 developer.log("이미 로딩 중이므로 onTap 종료",
//                     name: "DEBUG_2");
//                 return;
//               }
//               developer.log("🖱️ Clicked on Bottom Index: ${shoe.name}",
//                   name: "DEBUG_2");
//
//               setState(() {
//                 _isLoading = true;
//                 selectedShoe = shoe.name;
//                 currentPage = 1; // 검색 시 페이지 초기화
//               });
//               developer.log("🛠️ _isLoading true, selectedShoe: $selectedShoe",
//                   name: "DEBUG_2");
//
//               // 새 데이터 로드 (상단과 하단 모두 업데이트)
//               fetchedShoes = await _fetchShoes(keyword: selectedShoe);
//               developer.log("✅ _fetchShoes 완료, fetchedShoes 개수: ${fetchedShoes.length}",
//                   name: "DEBUG_2");
//
//               setState(() {
//                 _isLoading = false;
//               });
//               developer.log("🔄 _isLoading false, UI 업데이트 완료",
//                   name: "DEBUG_2");
//             },
//             // inner InkWell의 onTap은 비활성화하여 외부 GestureDetector가 처리하도록 함
//             child: _buildShoeIndex(shoe, MediaQuery.of(context).size,
//                 disableInkWell: true),
//           ),
//         )).toList(),
//       ),
//     ),
//   );
//
//   /// 하단 인덱스 카드를 만드는 위젯 (내부 InkWell onTap은 디버깅용)
//   Widget _buildShoeIndex(Shoe shoe, Size size, {bool disableInkWell = false}) =>
//       Padding(
//         padding: const EdgeInsets.symmetric(vertical: 10.0, horizontal: 5.0),
//         child: InkWell(
//           onTap: disableInkWell
//               ? null
//               : () {
//             developer.log("🖱️ Clicked on Shoe Index (Inner): ${shoe.name}",
//                 name: "DEBUG_2");
//           },
//           child: Stack(
//             children: [
//               Card(
//                 elevation: 6,
//                 shape: RoundedRectangleBorder(
//                     borderRadius: BorderRadius.circular(20)),
//                 color: shoe.color,
//                 child: SizedBox(height: size.width / 1.8),
//               ),
//               ClipRRect(
//                 borderRadius: BorderRadius.circular(20),
//                 child: Image.network(
//                   shoe.image,
//                   width: double.infinity,
//                   height: size.width,
//                   fit: BoxFit.cover,
//                 ),
//               ),
//               Positioned(
//                 top: 12,
//                 left: 12,
//                 right: 12,
//                 child: Container(
//                   padding: const EdgeInsets.all(8),
//                   child: Column(
//                     crossAxisAlignment: CrossAxisAlignment.start,
//                     children: [
//                       Text(
//                         shoe.name,
//                         style: const TextStyle(
//                             color: Colors.white,
//                             fontSize: 16, // 조정된 폰트 크기
//                             fontWeight: FontWeight.w600),
//                       ),
//                       const SizedBox(height: 4),
//                       Text(
//                         "\$${shoe.price}",
//                         style: const TextStyle(
//                             color: Colors.white, fontSize: 8), // 조정된 폰트 크기
//                       ),
//                     ],
//                   ),
//                 ),
//               ),
//             ],
//           ),
//         ),
//       );
//
//   /// 좌측 사이드 내비게이션
//   Widget _buildSideNavigation() => Positioned(
//     left: 0,
//     top: 0,
//     child: Padding(
//       padding: const EdgeInsets.symmetric(horizontal: 12.0),
//       child: RotatedBox(
//         quarterTurns: 3,
//         child: Row(
//           mainAxisAlignment: MainAxisAlignment.center,
//           children: ["New", "Featured", "Upcoming"].map((e) {
//             return Padding(
//               padding: const EdgeInsets.symmetric(horizontal: 15),
//               child: Text(e,
//                   style: TextStyle(
//                       fontWeight: FontWeight.w700,
//                       color: e == "Featured" ? Colors.black : Colors.grey[400])),
//             );
//           }).toList(),
//         ),
//       ),
//     ),
//   );
//
//   /// 하단 내비게이션 바
//   Widget _buildBottomNavigationBar() => BottomNavigationBar(
//     selectedItemColor: Colors.red,
//     backgroundColor: bottomBackgroundColor,
//     unselectedItemColor: Colors.grey[400],
//     elevation: 4,
//     type: BottomNavigationBarType.fixed,
//     items: List.generate(5, (index) {
//       final icons = [
//         Icons.home,
//         Icons.favorite_border,
//         Icons.location_city,
//         Icons.shopping_cart,
//         Icons.person_outline
//       ];
//       return BottomNavigationBarItem(
//           label: '', icon: Icon(icons[index]));
//     }),
//   );
// }
