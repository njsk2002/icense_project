package kr.co.icense;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import io.flutter.plugin.common.MethodChannel;
import kr.co.icense.MainActivity;

public class nfc_eink_other {
    private static final int MAX_TAG_RETRIES = 3;
    private static final int MAX_TRANSMISSION_RETRIES = 2;
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    // 결과 중복 제출 방지를 위한 플래그
    private static boolean _resultSubmitted = false;

    public static void startProcess(MainActivity activity, byte[] imageData, int displaySize, MethodChannel.Result result) {
        _resultSubmitted = false; // 호출 시마다 초기화
        if (activity == null) {
            result.error("ACTIVITY_NULL", "MainActivity is null", null);
            return;
        }
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(activity);
        if (adapter == null || !adapter.isEnabled()) {
            MainActivity.showNFCSettingsDialog(activity);
            result.error("NFC_DISABLED", "NFC is disabled", null);
            return;
        }
        Log.d("DEBUG_2", "✅ NFC Reader Mode Enabled in nfc_eink_other");
        adapter.enableReaderMode(activity,
                tag -> handleTag(activity, tag, imageData, displaySize, result, 0),
                NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                null);
    }

    private static void handleTag(MainActivity activity, Tag tag, byte[] imageData, int displaySize, MethodChannel.Result result, int retryCount) {
        if (tag == null) {
            Log.e("DEBUG_2", "❌ No NFC tag detected in nfc_eink_other, attempt " + retryCount);
            if (retryCount < MAX_TAG_RETRIES) {
                mainHandler.postDelayed(() -> startProcess(activity, imageData, displaySize, result), 1000);
            } else {
                if (!_resultSubmitted) {
                    _resultSubmitted = true;
                    mainHandler.post(() -> result.error("NFC_ERROR", "No NFC tag detected after retries", null));
                }
            }
            return;
        }

        Log.d("DEBUG_2", "✅ NFC Tag Detected in nfc_eink_other: " + Arrays.toString(tag.getTechList()));
        NfcA nfcA = NfcA.get(tag);
        if (nfcA == null) {
            Log.e("DEBUG_2", "❌ Tag does not support NfcA in nfc_eink_other");
            if (!_resultSubmitted) {
                _resultSubmitted = true;
                mainHandler.post(() -> result.error("NFC_ERROR", "Tag does not support NfcA", null));
            }
            return;
        }

        try {
            // waveshare 라이브러리 초기화
            waveshare.feng.nfctag.activity.a nfcLibrary = new waveshare.feng.nfctag.activity.a();
            closeOtherTechnologies(tag);
            int initResponse = nfcLibrary.a(nfcA);
            if (initResponse != 1) {
                Log.e("DEBUG_2", "❌ NFC Initialization Failed in nfc_eink_other");
                if (!_resultSubmitted) {
                    _resultSubmitted = true;
                    mainHandler.post(() -> result.error("NFC_ERROR", "NFC initialization failed", null));
                }
                return;
            }
            Log.d("DEBUG_2", "✅ NFC Initialized Successfully in nfc_eink_other");

            Bitmap bitmap = convertToEInkCompatible(BitmapFactory.decodeStream(new ByteArrayInputStream(imageData)));
            if (bitmap == null) {
                Log.e("DEBUG_2", "❌ Failed to convert image to E-Ink format in nfc_eink_other");
                if (!_resultSubmitted) {
                    _resultSubmitted = true;
                    mainHandler.post(() -> result.error("BITMAP_ERROR", "Failed to convert image", null));
                }
                return;
            }
            Log.d("DEBUG_2", "📤 Starting data transmission in nfc_eink_other...");

            // 진행률 모니터링 스레드 시작 (메인 스레드에서 이벤트 전송)
            new Thread(() -> monitorProgress(nfcLibrary, result)).start();

            sendData(nfcLibrary, tag, displaySize, bitmap, result, 0);
        } catch (Exception e) {
            Log.e("DEBUG_2", "❌ NFC Error in nfc_eink_other: " + e.getMessage());
            if (!_resultSubmitted) {
                _resultSubmitted = true;
                mainHandler.post(() -> result.error("NFC_ERROR", "Error: " + e.getMessage(), null));
            }
        }
    }

    private static void sendData(waveshare.feng.nfctag.activity.a nfcLibrary, Tag tag, int displaySize, Bitmap bitmap, MethodChannel.Result result, int retryCount) {
        int sendResponse = nfcLibrary.a(displaySize, bitmap);
        if (sendResponse == 1) {
            Log.d("DEBUG_2", "✅ NFC Transmission Completed in nfc_eink_other");
            if (!_resultSubmitted) {
                _resultSubmitted = true;
                mainHandler.post(() -> result.success(true));
            }
        } else {
            Log.e("DEBUG_2", "❌ NFC Transmission Failed in nfc_eink_other, attempt " + retryCount);
            if (retryCount < MAX_TRANSMISSION_RETRIES) {
                mainHandler.postDelayed(() -> sendData(nfcLibrary, tag, displaySize, bitmap, result, retryCount + 1), 1000);
            } else {
                if (!_resultSubmitted) {
                    _resultSubmitted = true;
                    mainHandler.post(() -> result.error("TRANSMISSION_ERROR", "Data transmission failed after retries", null));
                }
            }
        }
    }

    private static void monitorProgress(waveshare.feng.nfctag.activity.a nfcLibrary, MethodChannel.Result result) {
        try {
            boolean isCompleted = false;
            while (!isCompleted) {
                int progress = nfcLibrary.a();
                Log.d("DEBUG_2", "📊 전송 중... " + progress + "%");

                // 메인 스레드에서 진행률 업데이트
                mainHandler.post(() -> MainActivity.sendProgressUpdate(progress));

                if (progress >= 100) {
                    Log.d("DEBUG_2", "✅ NFC Data Transmission Completed in nfc_eink_other");
                    if (!_resultSubmitted) {
                        _resultSubmitted = true;
                        mainHandler.post(() -> result.success(true));
                    }
                    isCompleted = true;
                } else if (progress < 0) {
                    Log.e("DEBUG_2", "❌ Transmission Error Occurred in nfc_eink_other");
                    if (!_resultSubmitted) {
                        _resultSubmitted = true;
                        mainHandler.post(() -> result.error("TRANSMISSION_ERROR", "Error during progress monitoring", null));
                    }
                    isCompleted = true;
                }
                Thread.sleep(500);
            }
        } catch (Exception e) {
            Log.e("DEBUG_2", "❌ NFC Monitoring Error in nfc_eink_other: " + e.getMessage());
            if (!_resultSubmitted) {
                _resultSubmitted = true;
                mainHandler.post(() -> result.error("MONITORING_ERROR", "Error during progress monitoring", null));
            }
        }
    }

    private static void closeOtherTechnologies(Tag tag) {
        String[] techList = tag.getTechList();
        for (String tech : techList) {
            try {
                switch (tech) {
                    case "android.nfc.tech.Ndef":
                        android.nfc.tech.Ndef.get(tag).close();
                        break;
                    case "android.nfc.tech.NfcA":
                        android.nfc.tech.NfcA.get(tag).close();
                        break;
                }
            } catch (Exception e) {
                Log.e("DEBUG_2", "Failed to close NFC tech: " + tech, e);
            }
        }
    }

    private static Bitmap convertToEInkCompatible(Bitmap original) {
        Bitmap converted = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                int color = original.getPixel(x, y);
                int gray = (int) (0.299 * ((color >> 16) & 0xff)
                        + 0.587 * ((color >> 8) & 0xff)
                        + 0.114 * (color & 0xff));
                converted.setPixel(x, y, gray > 128 ? Color.WHITE : Color.BLACK);
            }
        }
        return converted;
    }
}
