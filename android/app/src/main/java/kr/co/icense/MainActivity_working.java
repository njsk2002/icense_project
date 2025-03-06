//package kr.co.icense;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.nfc.NfcAdapter;
//import android.nfc.Tag;
//import android.nfc.tech.NfcA;
//import android.util.Log;
//import android.app.PendingIntent;
//import android.content.IntentFilter;
//
//import androidx.appcompat.app.AlertDialog;
//import io.flutter.embedding.android.FlutterActivity;
//import io.flutter.embedding.engine.FlutterEngine;
//import io.flutter.plugin.common.MethodChannel;
//import java.io.ByteArrayInputStream;
//import java.util.Arrays;
//
//public class MainActivity extends FlutterActivity {
//  private static final String CHANNEL = "kr.co.icense";
//  private NfcAdapter nfcAdapter;
//
//  @Override
//  public void configureFlutterEngine(FlutterEngine flutterEngine) {
//    super.configureFlutterEngine(flutterEngine);
//    nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//
//    new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
//            .setMethodCallHandler((call, result) -> {
//              if ("startNFCProcess".equals(call.method)) {
//                byte[] imageData = call.argument("imageData");
//                int displaySize = call.argument("displaySize");
//
//                if (imageData == null) {
//                  Log.e("DEBUG_2", "Image data is null");
//                  result.error("INVALID_ARGUMENT", "Image data is null", null);
//                  return;
//                }
//
//                if (displaySize < 0 || displaySize > 9) {
//                  Log.e("DEBUG_2", "Invalid display size");
//                  result.error("INVALID_ARGUMENT", "Invalid display size", null);
//                  return;
//                }
//
//                if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
//                  showNFCSettingsDialog();
//                  result.error("NFC_DISABLED", "NFC is disabled", null);
//                  return;
//                }
//
//                Log.d("DEBUG_2", "NFC Reader Mode Enabled");
//                nfcAdapter.enableReaderMode(this, tag -> handleTag(tag, imageData, displaySize, result),
//                        NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null);
//              } else {
//                result.notImplemented();
//              }
//            });
//  }
//
//  private void handleTag(Tag tag, byte[] imageData, int displaySize, MethodChannel.Result result) {
//    if (tag == null) {
//      Log.e("DEBUG_2", "❌ No NFC tag detected");
//      result.error("NFC_ERROR", "No NFC tag detected", null);
//      return;
//    }
//
//    Log.d("DEBUG_2", "✅ NFC Tag Detected: " + Arrays.toString(tag.getTechList()));
//    NfcA nfcA = NfcA.get(tag);
//
//    if (nfcA == null) {
//      Log.e("DEBUG_2", "❌ Tag does not support NfcA");
//      result.error("NFC_ERROR", "Tag does not support NfcA", null);
//      return;
//    }
//
//    try {
//      waveshare.feng.nfctag.activity.a nfcLibrary = new waveshare.feng.nfctag.activity.a();
//      closeOtherTechnologies(tag);
//
//      int initResponse = nfcLibrary.a(nfcA);
//      if (initResponse != 1) {
//        Log.e("DEBUG_2", "❌ NFC Initialization Failed");
//        result.error("NFC_ERROR", "NFC initialization failed", null);
//        return;
//      }
//
//      Log.d("DEBUG_2", "✅ NFC Initialized Successfully");
//
//      // ✅ BMP 이미지를 흑백 변환하여 E-Ink 전송
//      Bitmap bitmap = convertToEInkCompatible(BitmapFactory.decodeStream(new ByteArrayInputStream(imageData)));
//      if (bitmap == null) {
//        Log.e("DEBUG_2", "❌ Failed to convert image to E-Ink format");
//        result.error("BITMAP_ERROR", "Failed to convert image", null);
//        return;
//      }
//
//      Log.d("DEBUG_2", "📤 Starting data transmission...");
//
//      // ✅ 모니터링을 먼저 실행한 후 전송을 진행
//      Thread monitorThread = new Thread(() -> monitorProgress(nfcLibrary, tag, result));
//      monitorThread.start(); // 진행률 추적 시작
//
//      int sendResponse = nfcLibrary.a(displaySize, bitmap); // NFC 전송 실행
//
//      if (sendResponse == 1) {
//        Log.d("DEBUG_2", "✅ NFC Transmission Completed");
//      } else {
//        Log.e("DEBUG_2", "❌ NFC Transmission Failed");
//        result.error("TRANSMISSION_ERROR", "Data transmission failed", null);
//        closeOtherTechnologies(tag);
//        disableNFCReaderMode();
//      }
//    } catch (Exception e) {
//      Log.e("DEBUG_2", "❌ NFC Error: " + e.getMessage());
//      result.error("NFC_ERROR", "Error: " + e.getMessage(), null);
//    } finally {
//      disableNFCReaderMode();
//    }
//  }
//
//
//  private void closeOtherTechnologies(Tag tag) {
//    String[] techList = tag.getTechList();
//    for (String tech : techList) {
//      try {
//        switch (tech) {
//          case "android.nfc.tech.Ndef":
//            android.nfc.tech.Ndef.get(tag).close();
//            break;
//          case "android.nfc.tech.NdefFormatable":
//            android.nfc.tech.NdefFormatable.get(tag).close();
//            break;
//          case "android.nfc.tech.MifareClassic":
//            android.nfc.tech.MifareClassic.get(tag).close();
//            break;
//          case "android.nfc.tech.MifareUltralight":
//            android.nfc.tech.MifareUltralight.get(tag).close();
//            break;
//          case "android.nfc.tech.NfcA":
//            android.nfc.tech.NfcA.get(tag).close();
//            break;
//        }
//      } catch (Exception e) {
//        Log.e("DEBUG_2", "Failed to close technology: " + tech, e);
//      }
//    }
//  }
//
//  private void showNFCSettingsDialog() {
//    new AlertDialog.Builder(this)
//            .setTitle("NFC Disabled")
//            .setMessage("NFC is disabled. Please enable NFC in settings to continue.")
//            .setPositiveButton("Open Settings", (DialogInterface dialog, int which) -> {
//              Intent intent = new Intent(android.provider.Settings.ACTION_NFC_SETTINGS);
//              startActivity(intent);
//            })
//            .setNegativeButton("Cancel", null)
//            .show();
//  }
//
//  private void monitorProgress(waveshare.feng.nfctag.activity.a nfcLibrary, Tag tag, MethodChannel.Result result) {
//    try {
//      boolean isCompleted = false;
//
//      while (!isCompleted) {
//        int progress = nfcLibrary.a(); // ✅ 진행률 가져오기
//        Log.d("NFC_PROGRESS", "📊 NFC 전송 진행률: " + progress + "%");
//
//        if (progress >= 100) { // ✅ 완료 체크
//          Log.d("DEBUG_2", "✅ NFC Data Transmission Completed");
//          result.success(true);
//          isCompleted = true;
//          closeOtherTechnologies(tag);
//          disableNFCReaderMode();
//        } else if (progress < 0) { // ❌ 오류 발생 시
//          Log.e("DEBUG_2", "❌ Transmission Error Occurred");
//          result.error("TRANSMISSION_ERROR", "Error occurred while monitoring progress", null);
//          isCompleted = true;
//        }
//
//        Thread.sleep(500); // ✅ 0.5초마다 체크
//      }
//    } catch (Exception e) {
//      Log.e("DEBUG_2", "❌ NFC Monitoring Error: " + e.getMessage());
//      result.error("MONITORING_ERROR", "Error during progress monitoring", null);
//    } finally {
//      disableNFCReaderMode();
//    }
//  }
//
//
//  @Override
//  protected void onResume() {
//    super.onResume();
//    enableNFCForegroundDispatch();
//  }
//
//  @Override
//  protected void onPause() {
//    super.onPause();
//    disableNFCForegroundDispatch();
//  }
//
//  private void enableNFCForegroundDispatch() {
//    if (nfcAdapter != null) {
//      Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//      PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
//      IntentFilter[] filters = new IntentFilter[] { new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED) };
//      String[][] techList = new String[][] { new String[] { NfcA.class.getName() } };
//      nfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, techList);
//      Log.d("DEBUG_2", "Foreground Dispatch Enabled");
//    }
//  }
//
//  private void disableNFCForegroundDispatch() {
//    if (nfcAdapter != null) {
//      nfcAdapter.disableForegroundDispatch(this);
//      Log.d("DEBUG_2", "Foreground Dispatch Disabled");
//    }
//  }
//
//
//
//
//
//  private void disableNFCReaderMode() {
//    if (nfcAdapter != null) {
//      nfcAdapter.disableReaderMode(this);
//      Log.d("DEBUG_2", "NFC Reader Mode Disabled");
//    }
//  }
//
//  private Bitmap convertToEInkCompatible(Bitmap original) {
//    Bitmap converted = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
//    for (int y = 0; y < original.getHeight(); y++) {
//      for (int x = 0; x < original.getWidth(); x++) {
//        int color = original.getPixel(x, y);
//        int gray = (int) (0.299 * ((color >> 16) & 0xff) + 0.587 * ((color >> 8) & 0xff) + 0.114 * (color & 0xff));
//        converted.setPixel(x, y, gray > 128 ? Color.WHITE : Color.BLACK);
//      }
//    }
//    return converted;
//  }
//}
