package kr.co.icense;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;

public class MainActivity extends FlutterActivity {
  private static final String CHANNEL = "kr.co.icense";
  public static MethodChannel methodChannel; // 기존 MethodChannel
  private EventChannel eventChannel; // EventChannel 선언
  private static EventChannel.EventSink progressEventSink; // 이벤트 수신 객체

  private NfcAdapter nfcAdapter;

  @Override
  public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
    super.configureFlutterEngine(flutterEngine);
    Log.d("DEBUG_2", "configureFlutterEngine: Initializing NFC adapter");
    nfcAdapter = NfcAdapter.getDefaultAdapter(this);

    methodChannel = new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL);
    methodChannel.setMethodCallHandler((call, result) -> {
      Log.d("DEBUG_2", "MethodCall received: " + call.method);
      if ("startNFCProcess".equals(call.method)) {
        startNFCProcess(call, result);
      } else {
        Log.d("DEBUG_2", "Method not implemented: " + call.method);
        result.notImplemented();
      }
    });
    Log.d("DEBUG_2", "configureFlutterEngine: MethodChannel set up complete");

    // EventChannel 설정 (채널 이름 "nfc_progress")
    eventChannel = new EventChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), "nfc_progress");
    eventChannel.setStreamHandler(new EventChannel.StreamHandler() {
      @Override
      public void onListen(Object arguments, EventChannel.EventSink events) {
        progressEventSink = events;
        Log.d("DEBUG_2", "EventChannel: onListen - progressEventSink set");
      }

      @Override
      public void onCancel(Object arguments) {
        progressEventSink = null;
        Log.d("DEBUG_2", "EventChannel: onCancel - progressEventSink cleared");
      }
    });
  }

  private void startNFCProcess(MethodCall call, MethodChannel.Result result) {
    byte[] imageData = call.argument("imageData");
    Integer displaySize = call.argument("displaySize");

    Log.d("DEBUG_2", "startNFCProcess called with displaySize: " + displaySize);
    if (imageData == null || displaySize == null) {
      Log.e("DEBUG_2", "Invalid arguments: imageData or displaySize is null");
      result.error("INVALID_ARGUMENT", "Image data or displaySize is null", null);
      return;
    }

    if (!isNFCEnabled()) {
      Log.e("DEBUG_2", "NFC is disabled. Attempting to enable...");
      showNFCSettingsDialog(this); // 수정된 부분: this 대신 activity 매개변수 사용
      result.error("NFC_DISABLED", "NFC is disabled. Please enable it in settings.", null);
      return;
    }

    int epdInch = displaySize / 10;
    int epdColor = displaySize % 10;
    Log.d("DEBUG_2", "Parsed epdInch: " + epdInch + ", epdColor: " + epdColor);

    if (epdInch == 420 && epdColor == 2) {
      Log.d("DEBUG_2", "Calling nfc_eink_other.startProcess() for 420x2");
      nfc_eink_other.startProcess(this, imageData, 3, result);
    } else {
      nfc_eink_gooddisplay.startProcess(this, imageData, epdColor, epdInch, result);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.d("DEBUG_2", "onResume: Enabling NFC foreground dispatch");
    enableNFCForegroundDispatch();
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.d("DEBUG_2", "onPause: Disabling NFC foreground dispatch");
    disableNFCForegroundDispatch();
  }

  private void enableNFCForegroundDispatch() {
    if (nfcAdapter != null) {
      Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
      PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
      IntentFilter[] filters = new IntentFilter[]{ new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED) };
      String[][] techList = new String[][]{ new String[]{ NfcA.class.getName() } };
      nfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, techList);
      Log.d("DEBUG_2", "enableNFCForegroundDispatch: Foreground Dispatch Enabled");
    } else {
      Log.e("DEBUG_2", "enableNFCForegroundDispatch: NFC adapter is null");
    }
  }

  private void disableNFCForegroundDispatch() {
    if (nfcAdapter != null) {
      nfcAdapter.disableForegroundDispatch(this);
      Log.d("DEBUG_2", "disableNFCForegroundDispatch: Foreground Dispatch Disabled");
    } else {
      Log.e("DEBUG_2", "disableNFCForegroundDispatch: NFC adapter is null");
    }
  }

  private boolean isNFCEnabled() {
    return nfcAdapter != null && nfcAdapter.isEnabled();
  }

  // 수정된 부분: static 메서드로 변경하고, Activity 매개변수를 사용
  public static void showNFCSettingsDialog(MainActivity activity) {
    new AlertDialog.Builder(activity)
            .setTitle("NFC Disabled")
            .setMessage("NFC is disabled. Please enable NFC in settings to continue.")
            .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.provider.Settings.ACTION_NFC_SETTINGS);
                activity.startActivity(intent);
              }
            })
            .setNegativeButton("Cancel", null)
            .show();
  }

  // MainActivity에서 이벤트 전송을 위한 메서드
  public static void sendProgressUpdate(int progress) {
    if (progressEventSink != null) {
      progressEventSink.success(progress);
      Log.d("DEBUG_2", "sendProgressUpdate: progress sent: " + progress + "%");
    } else {
      Log.e("DEBUG_2", "sendProgressUpdate: progressEventSink is null");
    }
  }
}
