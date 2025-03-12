import 'package:flutter/material.dart';
import 'dart:developer' as developer;
import 'package:url_launcher/url_launcher.dart';
import 'package:icense_project/app_clone/shoes_store/shoes_store_page.dart';
import 'package:icense_project/app_clone/shoes_store/shoes_store_detail_page.dart';
import 'package:icense_project/app_clone/android_messages/android_messages_page.dart';
import 'package:icense_project/app_clone/movies_concept/movies_concept_page.dart';
import 'package:icense_project/app_clone/credit_cards_concept/credit_cards_concept_page.dart';
import 'package:icense_project/app_clone/travel_concept/travel_concept_page.dart';

const double marginSide = 14.0;
const List<String> brands = [
  'e-명함',
  'e-카드 만들기',
  'e-쉐어카드',
  '사진',
  '정보추가',
  '나의페이지'
];

/// 공통 헤더 위젯
class CommonHeader extends StatelessWidget {
  const CommonHeader({Key? key}) : super(key: key);

  /// 주어진 URL을 여는 전용 함수
  Future<void> _launchUrl(String url) async {
    developer.log("openUrl: 시도 중 - URL: $url", name: "DEBUG_2");
    if (await canLaunch(url)) {
      developer.log("openUrl: canLaunch 성공 - URL: $url", name: "DEBUG_2");
      await launch(url);
      developer.log("openUrl: launch 성공 - URL: $url", name: "DEBUG_2");
    } else {
      developer.log("openUrl: URL을 열 수 없음 - URL: $url", name: "DEBUG_2", level: 900);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(marginSide),
      child: Column(
        mainAxisSize: MainAxisSize.min, // 내용에 맞는 최소 높이
        children: [
          // 상단 행: "ICEnse" 텍스트와 아이콘 버튼들
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              GestureDetector(
                onTap: () async {
                  const url = "http://192.168.0.136:5000/";
                  developer.log("ICEnse 탭 - URL 열기 시도", name: "DEBUG_2");
                  await _launchUrl(url);
                },
                child: const Text(
                  "ICEnse",
                  style: TextStyle(fontSize: 26, fontWeight: FontWeight.w700),
                ),
              ),
              Row(
                children: [
                  IconButton(
                    icon: const Icon(Icons.search),
                    onPressed: () {
                      developer.log("검색 아이콘 탭", name: "DEBUG_2");
                    },
                  ),
                  IconButton(
                    icon: const Icon(Icons.login), // 🔄 로그인 아이콘으로 변경
                    onPressed: () async {
                      const url = "http://192.168.0.136:5000/auth/login";
                      developer.log("로그인 아이콘 탭 - URL 열기 시도", name: "DEBUG_2");
                      await _launchUrl(url);
                      },
                  ),

                ],
              ),
            ],
          ),
          const SizedBox(height: 10),
          // 하단 행: 브랜드 리스트 (가로 스크롤)
          SizedBox(
            height: 40,
            child: ListView.builder(
              scrollDirection: Axis.horizontal,
              itemCount: brands.length,
              itemBuilder: (_, index) => Padding(
                padding: const EdgeInsets.symmetric(horizontal: 16.0),
                child: GestureDetector(
                  onTap: () {
                    developer.log("브랜드 '${brands[index]}' 탭", name: "DEBUG_2");
                    // index에 따라 다른 페이지로 이동
                    if (index == 0) {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => ShoesStorePage(),
                        ),
                      );
                    } else if (index == 1) {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => CreditCardConceptPage(),
                        ),
                      );
                    } else if (index == 2) {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => MoviesConceptPage(),
                        ),
                      );
                    } else if (index == 3) {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => AndroidMessagesPage(),
                        ),
                      );
                    } else if (index == 4) {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => TravelConceptPage(),
                        ),
                      );
                    }else if (index == 5) {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => TravelConceptPage(),
                        ),
                      );
                    }
                  },
                  child: Text(
                    brands[index],
                    style: TextStyle(
                      fontWeight: FontWeight.w700,
                      color: index == 0 ? Colors.black : Colors.grey[400],
                    ),
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
