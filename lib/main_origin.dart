import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
// import 'package:icense_project/animations/main_animations.dart';
// import 'package:icense_project/app_clone/main_apps_clone.dart';
// import 'package:icense_project/appbar_sliverappbar/main_appbar_sliverappbar.dart';
// import 'package:icense_project/collapsing_toolbar/main_collapsing_toolbar.dart';
// import 'package:icense_project/communication_widgets/main_communication_widgets.dart';
// import 'package:icense_project/fetch_data/main_fetch_data.dart';
// import 'package:icense_project/hero_animations/main_hero_animations.dart';
// import 'package:icense_project/menu_navigations/main_menu_navigations.dart';
// import 'package:icense_project/persistent_tabbar/main_persistent_tabbar.dart';
// import 'package:icense_project/scroll_controller/main_scroll_controller.dart';
// import 'package:icense_project/size_and_position/main_size_and_position.dart';
// import 'package:icense_project/split_image/main_split_image.dart';

import 'package:icense_project/app_clone/album_flow/album_flow_page.dart';
import 'package:icense_project/app_clone/android_messages/android_messages_page.dart';
import 'package:icense_project/app_clone/credit_cards_concept/credit_cards_concept_page.dart';
import 'package:icense_project/app_clone/movies_concept/movies_concept_page.dart';
import 'package:icense_project/app_clone/photo_concept/photo_concept_page.dart';
import 'package:icense_project/app_clone/shoes_store/shoes_store_page.dart';
import 'package:icense_project/app_clone/sports_store/sports_store_page.dart';
import 'package:icense_project/app_clone/travel_concept/travel_concept_page.dart';
import 'package:icense_project/app_clone/twitter_profile/twitter_profile_page.dart';

void main() => runApp(MaterialApp(
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      // home: MyApp(),
      home: ShoesStorePage(),
    ));

class MyApp extends StatefulWidget {
  @override
  MyAppState createState() {
    return MyAppState();
  }
}

class MyAppState extends State<MyApp> {
  onButtonTap(Widget page) {
    Navigator.push(
        context, MaterialPageRoute(builder: (BuildContext context) => page));
  }

//   @override
//   Widget build(BuildContext context) {
//     SystemChrome.setEnabledSystemUIMode(SystemUiMode.manual, overlays: [SystemUiOverlay.bottom]);
//     return Scaffold(
//       appBar: AppBar(
//         title: Text("Flutter Samples"),
//       ),
//       body: Padding(
//         padding: EdgeInsets.all(15.0),
//         child: ListView(
//           children: <Widget>[
//             MyMenuButton(
//               title: "Fetch Data JSON",
//               actionTap: () {
//                 onButtonTap(
//                   MainFetchData(),
//                 );
//               },
//             ),
//             MyMenuButton(
//                 title: "Persistent Tab Bar",
//                 actionTap: () {
//                   onButtonTap(
//                     MainPersistentTabBar(),
//                   );
//                 }),
//             MyMenuButton(
//               title: "Collapsing Toolbar",
//               actionTap: () {
//                 onButtonTap(
//                   MainCollapsingToolbar(),
//                 );
//               },
//             ),
//             MyMenuButton(
//               title: "Hero Animations",
//               actionTap: () {
//                 onButtonTap(
//                   MainHeroAnimationsPage(),
//                 );
//               },
//             ),
//             MyMenuButton(
//               title: "Size and Positions",
//               actionTap: () {
//                 onButtonTap(
//                   MainSizeAndPosition(),
//                 );
//               },
//             ),
//             MyMenuButton(
//               title: "ScrollController and ScrollNotification",
//               actionTap: () {
//                 onButtonTap(
//                   MainScrollController(),
//                 );
//               },
//             ),
//             MyMenuButton(
//               title: "Apps Clone",
//               actionTap: () {
//                 onButtonTap(
//                   MainAppsClone(),
//                 );
//               },
//             ),
//             MyMenuButton(
//               title: "Animations",
//               actionTap: () {
//                 onButtonTap(
//                   MainAnimations(),
//                 );
//               },
//             ),
//             MyMenuButton(
//               title: "Communication Widgets",
//               actionTap: () {
//                 onButtonTap(
//                   MainCommunicationWidgets(),
//                 );
//               },
//             ),
//             MyMenuButton(
//               title: "Split Image",
//               actionTap: () {
//                 onButtonTap(MainSplitImage());
//               },
//             ),
//             MyMenuButton(
//               title: "Custom AppBar & SliverAppBar",
//               actionTap: () {
//                 onButtonTap(MainAppBarSliverAppBar());
//               },
//             ),
//             MyMenuButton(
//               title: "Menu Navigations",
//               actionTap: () {
//                 onButtonTap(MainMenuNavigations());
//               },
//             ),
//           ],
//         ),
//       ),
//     );
//   }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Flutter Apps Clone"),
      ),
      body: Padding(
        padding: EdgeInsets.all(15.0),
        child: ListView(
          children: <Widget>[
            MyMenuButton(
              title: "Android Messages Page",
              actionTap: () {
                onButtonTap(AndroidMessagesPage());
              },
            ),
            MyMenuButton(
              title: "Twitter Profile Page",
              actionTap: () {
                onButtonTap(TwitterProfilePage());
              },
            ),
            MyMenuButton(
              title: "Movies Concept",
              actionTap: () {
                onButtonTap(MoviesConceptPage());
              },
            ),
            MyMenuButton(
              title: "Photo Concept",
              actionTap: () {
                onButtonTap(PhotoConceptPage());
              },
            ),
            MyMenuButton(
              title: "Sports Store",
              actionTap: () {
                onButtonTap(SportsStorePage());
              },
            ),
            MyMenuButton(
              title: "Shoes Store",
              actionTap: () {
                onButtonTap(ShoesStorePage());
              },
            ),
            MyMenuButton(
              title: "Album Flow",
              actionTap: () {
                onButtonTap(AlbumFlowPage());
              },
            ),
            MyMenuButton(
              title: "Credit Cards Concept",
              actionTap: () {
                onButtonTap(CreditCardConceptPage());
              },
            ),
            MyMenuButton(
              title: "Travel Concept",
              actionTap: () {
                onButtonTap(TravelConceptPage());
              },
            ),
          ],
        ),
      ),
    );
  }

 }

class MyMenuButton extends StatelessWidget {
  final String? title;
  final VoidCallback? actionTap;

  MyMenuButton({this.title, this.actionTap});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(15.0),
      child: MaterialButton(
        height: 50.0,
        color: Theme.of(context).primaryColor,
        textColor: Colors.white,
        child: new Text(title!),
        onPressed: actionTap,
      ),
    );
  }
}
