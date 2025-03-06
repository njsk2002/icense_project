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
