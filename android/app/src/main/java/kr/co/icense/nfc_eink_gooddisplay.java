package kr.co.icense;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import java.io.IOException;

import io.flutter.plugin.common.MethodChannel;

public class nfc_eink_gooddisplay {
    // 상수 및 버퍼 크기
    private static final int TIMEOUT = 50000;
    private static final int BUFFER_SIZE = 100000;
    private static final byte ScreenIndex_BW = 0;
    private static final byte ScreenIndex_R = 1;

    // 이미지 및 NFC 관련 변수 (정적 변수로 관리)
    private static Bitmap bitmap0;
    private static int width0, height0;
    private static byte[] image_buffer = new byte[BUFFER_SIZE];
    private static int epdInch, epdColor, epdIC;
    private static byte[] cmd, response = new byte[2];
    private static int dataNum; // 데이터 전송 진행 상태

    private static NfcAdapter mNfcAdapter;
    private static IsoDep isodep;
    private static Tag detectedTag;
    private static PendingIntent mNfcPendingIntent;
    private static IntentFilter[] mWriteTagFilters;

    /**
     * NFC 전송을 시작하는 정적 메서드.
     * MainActivity, 이미지 데이터, epdColor, epdInch, 그리고 Flutter의 MethodChannel.Result를 전달받아 NFC 전송을 수행합니다.
     */
    public static void startProcess(final MainActivity activity, final byte[] imageData,
                                    final int epdColor, final int epdInch, final MethodChannel.Result result) {
        if (activity == null) {
            result.error("ACTIVITY_NULL", "MainActivity가 null입니다.", null);
            return;
        }
        nfc_eink_gooddisplay.epdColor = epdColor;
        nfc_eink_gooddisplay.epdInch = epdInch;
        // 필요한 경우 epdIC 등 추가 초기화 (여기서는 임의로 2로 설정)
        nfc_eink_gooddisplay.epdIC = 2;

        // Bitmap 디코딩
        bitmap0 = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        if (bitmap0 == null) {
            result.error("BITMAP_ERROR", "Bitmap 디코딩 실패", null);
            return;
        }
        Log.d("DEBUG_2", "nfc_eink_gooddisplay.startProcess: Bitmap 디코딩 성공, 크기="
                + bitmap0.getWidth() + "x" + bitmap0.getHeight());

        // NFC 어댑터 확인
        mNfcAdapter = NfcAdapter.getDefaultAdapter(activity);
        if (mNfcAdapter == null || !mNfcAdapter.isEnabled()) {
            result.error("NFC_DISABLED", "NFC가 비활성화되어 있습니다.", null);
            return;
        }
        Log.d("DEBUG_2", "nfc_eink_gooddisplay.startProcess: NFC 리더 모드 활성화");

        // NFC 리더모드 활성화 (태그가 발견되면 handleTag() 호출)
        mNfcAdapter.enableReaderMode(activity, new NfcAdapter.ReaderCallback() {
            @Override
            public void onTagDiscovered(Tag tag) {
                handleTag(activity, tag, result);
            }
        }, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null);
    }

    /**
     * 태그가 발견되었을 때 IsoDep를 이용해 NFC 데이터를 전송하는 메서드.
     * onResume()에 있던 순차적 로직을 각 단계를 별도의 헬퍼 메서드로 분리하여 호출합니다.
     */
    private static void handleTag(final MainActivity activity, final Tag tag, final MethodChannel.Result result) {
        String[] tech = tag.getTechList();
        if (!tech[0].equals("android.nfc.tech.IsoDep")) {
            result.error("NFC_ERROR", "IsoDep를 지원하지 않는 태그입니다.", null);
            return;
        }
        isodep = IsoDep.get(tag);
        try {
            isodep.setTimeout(TIMEOUT);
            if (!isodep.isConnected()) {
                isodep.connect();
            }
            if (isodep.isConnected()) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // "NFC is connect" -> 코드 0 (진행률 0%)
                        activity.sendProgressUpdate(0);
                    }
                });

                // 1. IC DIY DB instruction
                sendICDIYCommand();
                // 2. 전자종이 파라미터 설정(초기화 및 스크린 컷팅)
                sendEpdInitAndScreenCut();
                // 3. 초기화 결과에 따라 UI 업데이트
                updateInitResult(activity);
                // 4. 이미지 전송 (회전 후 각 색상 데이터 전송)
                sendImageTransmission(activity);
                // 5. Refresh 명령 전송
                sendRefreshCommand(activity);
                // 6. 이미지 원래 상태 복구 (270도 회전)
                bitmap0 = rotateBitmap(bitmap0, 270);
            }
        } catch (IOException e) {
            result.error("NFC_ERROR", "IOException: " + e.getMessage(), null);
        } finally {
            if (mNfcAdapter != null) {
                try {
                    isodep.close();
                } catch (IOException e) {
                    // 필요시 예외 처리
                }
            }
        }
    }

    //************************ 헬퍼 메서드 ************************//

    /**
     * 1. IC DIY DB instruction 전송
     */
    private static void sendICDIYCommand() throws IOException {
        cmd = HexString2Btyes("F0DB020000");
        response = isodep.transceive(cmd);
        Log.d("DEBUG_2", "IC DIY 응답: " + HexToString(response));
    }

    /**
     * 2. 전자종이 파라미터 설정 및 스크린 컷팅 명령 전송
     * data.setEpdInit(epdColor, epdInch)는 외부에서 전자종이 초기화 파라미터를 제공한다고 가정합니다.
     */
    private static void sendEpdInitAndScreenCut() throws IOException {
        // 전자종이 초기화 파라미터 설정
        cmd = HexString2Btyes(data.setEpdInit(epdColor, epdInch)[0]);
        response = isodep.transceive(cmd);
        // 스크린 컷팅 명령 전송
        cmd = HexString2Btyes(data.setEpdInit(epdColor, epdInch)[1]);
        response = isodep.transceive(cmd);
    }

    /**
     * 3. 초기화 결과에 따라 UI 업데이트
     */
    private static void updateInitResult(final MainActivity activity) {
        if (response[0] == (byte) 0x90) {
            // 초기화 성공 -> 90% 진행률
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.sendProgressUpdate(90);
                }
            });
        } else {
            // 초기화 실패 -> -1 (오류 코드)
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.sendProgressUpdate(-1);
                }
            });
        }
    }

    /**
     * 4. 이미지 전송 (회전 후 각 색상별 이미지 데이터 전송)
     */
    private static void sendImageTransmission(final MainActivity activity) throws IOException {
        // 이미지를 90도 회전 (세로 스캔 및 데이터 처리 편의를 위함)
        bitmap0 = rotateBitmap(bitmap0, 90);
        if (epdIC == 2) { // SSD 시리즈 동작
            if (epdColor == 2) {  // 흑백 이미지 전송
                GetPictureData_SSD(0);
                int datas = width0 * height0 / 8;
                for (int i = 0; i < datas / 250; i++) {
                    cmd = new byte[250 + 5];
                    cmd[0] = (byte) 0xF0;
                    cmd[1] = (byte) 0xD2;
                    cmd[2] = ScreenIndex_BW;
                    cmd[3] = (byte) i;
                    cmd[4] = (byte) 0xFA;
                    for (int j = 0; j < 250; j++) {
                        cmd[j + 5] = image_buffer[j + 250 * i];
                    }
                    response = isodep.transceive(cmd);
                    // 데이터 꼬리 판단
                    if (i == datas / 250 - 1 && datas % 250 != 0) {
                        cmd = new byte[250 + 5];
                        cmd[0] = (byte) 0xF0;
                        cmd[1] = (byte) 0xD2;
                        cmd[2] = ScreenIndex_BW;
                        cmd[3] = (byte) (i + 1);
                        cmd[4] = (byte) 0xFA;
                        for (int j = 0; j < 250; j++) {
                            cmd[j + 5] = image_buffer[j + 250 * (datas / 250)];
                        }
                        response = isodep.transceive(cmd);
                    }
                    dataNum = i;
                    final int progress = (int)(((float) (250 * dataNum) / datas) * 100);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.sendProgressUpdate(progress);
                        }
                    });
                }
            } else if (epdColor == 3) {  // 흑백 및 적색 이미지 전송
                // 흑백 데이터 전송
                GetPictureData_SSD(0);
                int datas = width0 * height0 / 8;
                for (int i = 0; i < datas / 250; i++) {
                    cmd = new byte[250 + 5];
                    cmd[0] = (byte) 0xF0;
                    cmd[1] = (byte) 0xD2;
                    cmd[2] = ScreenIndex_BW;
                    cmd[3] = (byte) i;
                    cmd[4] = (byte) 0xFA;
                    for (int j = 0; j < 250; j++) {
                        cmd[j + 5] = image_buffer[j + 250 * i];
                    }
                    response = isodep.transceive(cmd);
                    if (i == datas / 250 - 1 && datas % 250 != 0) {
                        cmd = new byte[250 + 5];
                        cmd[0] = (byte) 0xF0;
                        cmd[1] = (byte) 0xD2;
                        cmd[2] = ScreenIndex_BW;
                        cmd[3] = (byte) (i + 1);
                        cmd[4] = (byte) 0xFA;
                        for (int j = 0; j < 250; j++) {
                            cmd[j + 5] = image_buffer[j + 250 * (datas / 250)];
                        }
                        response = isodep.transceive(cmd);
                    }
                    dataNum = i;
                    final int progress = (int)(((float) (250 * dataNum) / datas * 2) * 100);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.sendProgressUpdate(progress);
                        }
                    });
                }
                // 적색 데이터 전송 (적색은 반전 처리 필요)
                GetPictureData_SSD(1);
                for (int i = 0; i < datas / 250; i++) {
                    cmd = new byte[250 + 5];
                    cmd[0] = (byte) 0xF0;
                    cmd[1] = (byte) 0xD2;
                    cmd[2] = ScreenIndex_R;
                    cmd[3] = (byte) i;
                    cmd[4] = (byte) 0xFA;
                    for (int j = 0; j < 250; j++) {
                        cmd[j + 5] = (byte) (0xFF - image_buffer[j + 250 * i]);
                    }
                    response = isodep.transceive(cmd);
                    if (i == datas / 250 - 1 && datas % 250 != 0) {
                        cmd = new byte[250 + 5];
                        cmd[0] = (byte) 0xF0;
                        cmd[1] = (byte) 0xD2;
                        cmd[2] = ScreenIndex_R;
                        cmd[3] = (byte) (i + 1);
                        cmd[4] = (byte) 0xFA;
                        for (int j = 0; j < 250; j++) {
                            cmd[j + 5] = (byte) (0xFF - image_buffer[j + 250 * (datas / 250)]);
                        }
                        response = isodep.transceive(cmd);
                    }
                    dataNum = i + dataNum;
                    final int progress = (int)(((float) (250 * dataNum) / datas * 2) * 100);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.sendProgressUpdate(progress);
                        }
                    });
                }
            } else if (epdColor == 4) { // 흑백, 적색, 노란색 이미지 전송 (4G)
                GetPictureData_4G();
                int datas = width0 * height0 / 4;
                for (int i = 0; i < datas / 250; i++) {
                    cmd = new byte[250 + 5];
                    cmd[0] = (byte) 0xF0;
                    cmd[1] = (byte) 0xD2;
                    cmd[2] = ScreenIndex_BW;
                    cmd[3] = (byte) i;
                    cmd[4] = (byte) 0xFA;
                    for (int j = 0; j < 250; j++) {
                        cmd[j + 5] = image_buffer[j + 250 * i];
                    }
                    response = isodep.transceive(cmd);
                    if (i == datas / 250 - 1 && datas % 250 != 0) {
                        cmd = new byte[250 + 5];
                        cmd[0] = (byte) 0xF0;
                        cmd[1] = (byte) 0xD2;
                        cmd[2] = ScreenIndex_BW;
                        cmd[3] = (byte) (i + 1);
                        cmd[4] = (byte) 0xFA;
                        for (int j = 0; j < 250; j++) {
                            cmd[j + 5] = image_buffer[j + 250 * (datas / 250)];
                        }
                        response = isodep.transceive(cmd);
                    }
                    dataNum = i;
                    final int progress = (int)(((float) (250 * dataNum) / datas) * 100);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.sendProgressUpdate(progress);
                        }
                    });
                }
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.sendProgressUpdate(100);
                }
            });
        }
    }

    /**
     * 5. Refresh 명령 전송
     */
    private static void sendRefreshCommand(final MainActivity activity) throws IOException {
        byte[] refreshcmd = new byte[5];
        if (epdColor == 4) {
            refreshcmd[0] = (byte) 0xF0;
            refreshcmd[1] = (byte) 0xD4;
            refreshcmd[2] = (byte) 0x85;
            refreshcmd[3] = (byte) 0x80;
            refreshcmd[4] = (byte) 0x00;
        } else {
            refreshcmd[0] = (byte) 0xF0;
            refreshcmd[1] = (byte) 0xD4;
            refreshcmd[2] = (byte) 0x05;
            refreshcmd[3] = (byte) 0x80;
            refreshcmd[4] = (byte) 0x00;
        }
        response = isodep.transceive(refreshcmd);
        if (response[0] == (byte) 0x90) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.sendProgressUpdate(90);
                }
            });
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.sendProgressUpdate(-2);
                }
            });
        }
    }

    //************************ 기존 함수 (유지) ************************//

    public static Bitmap rotateBitmap(Bitmap bitmap, float angle) {
        Log.d("DEBUG_2", "nfc_eink_gooddisplay: Rotating bitmap by " + angle + " degrees");
        if (bitmap == null) {
            Log.e("DEBUG_2", "nfc_eink_gooddisplay: Bitmap is null");
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        if (newBitmap.sameAs(bitmap)) {
            Log.d("DEBUG_2", "nfc_eink_gooddisplay: New bitmap is identical to original");
            return newBitmap;
        }
        bitmap.recycle();
        Log.d("DEBUG_2", "nfc_eink_gooddisplay: Original bitmap recycled, returning new bitmap");
        return newBitmap;
    }

    private static String HexToString(byte[] data) {
        String ReData = "";
        for (int i = 0; i < data.length; i++) {
            ReData += NumToString(data[i] / 16) + NumToString(data[i] % 16);
        }
        Log.d("DEBUG_2", "nfc_eink_gooddisplay: HexToString: " + ReData);
        return ReData;
    }

    private static String NumToString(int c) {
        String data = "";
        if (c >= 10) {
            switch (c) {
                case 10:
                    data = "A";
                    break;
                case 11:
                    data = "B";
                    break;
                case 12:
                    data = "C";
                    break;
                case 13:
                    data = "D";
                    break;
                case 14:
                    data = "E";
                    break;
                case 15:
                    data = "F";
                    break;
            }
        } else {
            switch (c) {
                case 0:
                    data = "0";
                    break;
                case 1:
                    data = "1";
                    break;
                case 2:
                    data = "2";
                    break;
                case 3:
                    data = "3";
                    break;
                case 4:
                    data = "4";
                    break;
                case 5:
                    data = "5";
                    break;
                case 6:
                    data = "6";
                    break;
                case 7:
                    data = "7";
                    break;
                case 8:
                    data = "8";
                    break;
                case 9:
                    data = "9";
                    break;
            }
        }
        Log.d("DEBUG_2", "nfc_eink_gooddisplay: NumToString: " + data);
        return data;
    }

    private static int parse(char c) {
        if (c >= 'a')
            return (c - 'a' + 10) & 0x0f;
        if (c >= 'A')
            return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }

    public static byte[] HexString2Btyes(String hexstr) {
        char[] charArray = hexstr.toCharArray();
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = charArray[j++];
            char c1 = charArray[j++];
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        Log.d("DEBUG_2", "nfc_eink_gooddisplay: HexString2Btyes: " + hexstr);
        return b;
    }

    // 흑백/적색 이미지 데이터 생성 함수 (SSD 방식)
    public static byte[] GetPictureData_SSD(int model) {
        Log.d("DEBUG_2", "nfc_eink_gooddisplay.GetPictureData_SSD: 입력된 model=" + model);
        int index = 0;
        byte temp = 0;
        if (bitmap0 == null) {
            Log.e("DEBUG_2", "nfc_eink_gooddisplay.GetPictureData_SSD: bitmap0 is null");
            return null;
        }
        width0 = bitmap0.getWidth();
        height0 = bitmap0.getHeight();
        Log.d("DEBUG_2", "nfc_eink_gooddisplay.GetPictureData_SSD: bitmap size=" + width0 + "x" + height0);
        for (int i = width0 - 1; i >= 0; i--) {
            temp = 0;
            for (int j = 0; j <= height0 / 8 - 1; j++) {
                for (int k = 0; k < 8; k++) {
                    temp = (byte) (temp * 2);
                    int pixel = bitmap0.getPixel(i, (j * 8) + k);
                    int r = (pixel & 0xff0000) >> 16;
                    int g = (pixel & 0xff00) >> 8;
                    int b = (pixel & 0xff);
                    Log.d("DEBUG_2", "nfc_eink_gooddisplay.GetPictureData_SSD: 현재 model=" + model);
                    if (model == 0) {  // 흑백(BW) 모드
                        temp = (r <= 100 && g <= 100 && b <= 100) ? (byte) (temp + 0) : (byte) (temp + 1);
                    } else if (model == 1) {  // RW 모드
                        temp = (r >= 100 && g <= 100 && b <= 100) ? (byte) (temp + 0) : (byte) (temp + 1);
                    } else {
                        Log.e("DEBUG_2", "nfc_eink_gooddisplay.GetPictureData_SSD: 잘못된 model 값: " + model);
                    }
                }
                image_buffer[index] = temp;
                index++;
            }
        }
        Log.d("DEBUG_2", "nfc_eink_gooddisplay.GetPictureData_SSD: Completed, data length=" + index);
        return image_buffer;
    }

    // 4G 이미지 데이터 생성 함수
    public static byte[] GetPictureData_4G() {
        Log.d("DEBUG_2", "nfc_eink_gooddisplay.GetPictureData_4G: Start");
        int index = 0;
        byte temp = 0;
        if (bitmap0 == null) {
            Log.e("DEBUG_2", "nfc_eink_gooddisplay.GetPictureData_4G: bitmap0 is null");
            return null;
        }
        width0 = bitmap0.getWidth();
        height0 = bitmap0.getHeight();
        Log.d("DEBUG_2", "nfc_eink_gooddisplay.GetPictureData_4G: bitmap size=" + width0 + "x" + height0);
        for (int i = width0 - 1; i >= 0; i--) {
            temp = 0;
            for (int j = 0; j <= height0 / 4 - 1; j++) {
                for (int k = 0; k < 4; k++) {
                    temp = (byte)(temp * 4);
                    int pixel = bitmap0.getPixel(i, (j * 4) + k);
                    int r = (pixel & 0xff0000) >> 16;
                    int g = (pixel & 0xff00) >> 8;
                    int b = (pixel & 0xff);
                    if (r <= 100 && g <= 100 && b <= 100)
                        temp = (byte)(temp + 0x00);
                    else if (r >= 200 && g >= 200 && b >= 200)
                        temp = (byte)(temp + 0x01);
                    else {
                        int num = (r + g + b) / 3;
                        temp = (num <= 127) ? (byte)(temp + 0x03) : (byte)(temp + 0x02);
                    }
                }
                image_buffer[index] = temp;
                index++;
            }
        }
        Log.d("DEBUG_2", "nfc_eink_gooddisplay.GetPictureData_4G: Completed, data length=" + index);
        return image_buffer;
    }
}
