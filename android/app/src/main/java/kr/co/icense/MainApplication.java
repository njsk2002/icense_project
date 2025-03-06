package kr.co.icense;

import android.app.Application;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;

public class MainApplication extends Application {
  private FlutterEngine flutterEngine;

  @Override
  public void onCreate() {
    super.onCreate();

    // Flutter 엔진 초기화
    flutterEngine = new FlutterEngine(this);
    flutterEngine.getDartExecutor().executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
    );

    // FlutterEngine을 캐시에 저장 (필요한 경우)
    FlutterEngineCache.getInstance().put("my_engine", flutterEngine);
  }

  public FlutterEngine getFlutterEngine() {
    return flutterEngine;
  }
}
