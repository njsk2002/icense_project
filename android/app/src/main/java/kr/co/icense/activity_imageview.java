//package kr.co.icense;
//
//import android.app.PendingIntent;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.graphics.Matrix;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.nfc.NfcAdapter;
//import android.nfc.Tag;
//import android.nfc.tech.IsoDep;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import android.util.Log;
//
//
//
////NFC
//import android.provider.Settings;
//
//import java.io.IOException;
//
//public class activity_imageview extends AppCompatActivity {
//    private TextView textView1;
//    private Button button_image,button_text;
//    Bitmap bitmap0;
//    int[] epdArray =new int[100];//Define the size of the image array (the array size needs to be defined, otherwise assigning values to the array elements separately will cause the APP to crash)// Save electronic paper resolution
//    //Electronic paper width and height
//    public int width0;//Electronic paper width
//    public int height0;///Electronic paper height
//    byte[] image_buffer = new byte[100000];//Define the size of the image array
//    int epdInch;//Electronic paper size
//    int epdColor;//Electronic paper color
//    int epdIC;//Electronic paper IC
//    int dataNum; //Data transmission progress
//    //NFC
//    // NFC I/O operation class IsoDep
//    IntentFilter[] mWriteTagFilters;
//    PendingIntent mNfcPendingIntent;
//    NfcAdapter mNfcAdapter;
//    IsoDep isodep;
//    Tag detectedTag;
//    byte[] cmd;
//    byte[] response = new byte[2]; //TAG feedback data
//    byte ScreenIndex_BW = 0;  //The screen to be written currently
//    byte ScreenIndex_R = 1; //The screen to be written currently
//    private TextView resultTextView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_imageview);
//        ImageView imageView = findViewById(R.id.imageView);
//        resultTextView = findViewById(R.id.result_text_view);
//
//        //Display the default image of the current electronic paper
//        epdArray = data.getEpdArray(); //Global storage electronic paper resolution
//        epdInch = epdArray[6];//Electronic paper size
//        epdColor = epdArray[4];//Electronic paper color
//        epdIC = epdArray[5];//Electronic paper IC model
//
//        if (epdColor == 2) //Monochrome electronic paper
//        {
//            switch (epdInch) //Electronic paper size
//            {
//                case 213:
//                    imageView.setImageResource(R.drawable.bw213);
//                    break;
//
//                case 290:
//                    imageView.setImageResource(R.drawable.bw290);
//                    break;
//                case 370:
//                    imageView.setImageResource(R.drawable.bw370);
//                    break;
//                default:
//                    break;
//            }
//
//            bitmap0 = getBitmapFromImageView(imageView);
//
//        }
//        if (epdColor == 3) //Tri color electronic paper
//        {
//            switch (epdInch) //Electronic paper size
//            {
//                case 213:
//                    imageView.setImageResource(R.drawable.r213);
//                    break;
//                case 290:
//                    imageView.setImageResource(R.drawable.r290);
//                    break;
//                case 370:
//                    imageView.setImageResource(R.drawable.r370);
//                    break;
//                default:
//                    break;
//            }
//
//            bitmap0 = getBitmapFromImageView(imageView);
//        }
//        if (epdColor == 4) //Four color electronic paper
//        {
//            switch (epdInch) //Electronic paper size
//            {
//                case 213:
//                    imageView.setImageResource(R.drawable.f213);
//                    break;
//                case 290:
//                    imageView.setImageResource(R.drawable.f290);
//                    break;
//                case 370:
//                    imageView.setImageResource(R.drawable.f370);
//                    break;
//                default:
//                    break;
//            }
//
//            bitmap0 = getBitmapFromImageView(imageView);
//        }
//
//    }
////imageView转bitmap
//
//private Bitmap getBitmapFromImageView(ImageView imageView) {
//    Drawable drawable = imageView.getDrawable();
//    if (drawable == null) {
//        return null;
//    }
//    if (drawable instanceof BitmapDrawable) {
//        return ((BitmapDrawable) drawable).getBitmap();
//    }
//    return null;
//}
//
//
//public byte[] GetPictureData_SSD(int mode)  //mode=0 BW  mode=1 RW
//{
//    int index = 0;
//    byte temp = 0;
//    Bitmap bitmap = bitmap0;
//    width0 = bitmap.getWidth();
//    height0 = bitmap.getHeight();
//    for (int i = width0 - 1; i >= 0; i--)
//    {
//        temp = 0;
//        for (int j = 0; j <= height0 / 8 - 1; j++)
//        {
//            for (int k = 0; k < 8; k++)
//            {
//                temp = (byte)(temp * 2);
//                int pixel = bitmap.getPixel(i, (j * 8) + k);
//                int r = (pixel & 0xff0000) >> 16;
//                int g = (pixel & 0xff00) >> 8;
//                int b = (pixel & 0xff);
//                if (mode == 0)  //黑白数据
//                {  //BW模式
//                    if (r <= 100 && g <= 100 && b <= 100)
//                        temp = (byte)(temp + 0);
//                    else
//                        temp = (byte)(temp + 1);
//                }
//                if (mode == 1) //红白数据
//                { //RW模式
//                    if (r >= 100 && g <= 100 && b <= 100)
//                        temp = (byte)(temp + 0);
//                    else
//                        temp = (byte)(temp + 1);
//                }
//            }
//            image_buffer[index] = temp;
//            index++;
//        }
//    }
//    return image_buffer;
//}
//
//    public byte[] GetPictureData_4G()
//    {
//        int index = 0;
//        byte temp = 0;
//        Bitmap bitmap = bitmap0;
//        width0 = bitmap.getWidth();
//        height0 = bitmap.getHeight();
//        for (int i = width0 - 1; i >= 0; i--)
//        {
//            temp = 0;
//            for (int j = 0; j <= height0 / 4 - 1; j++)
//            {
//                for (int k = 0; k < 4; k++)
//                {
//                    temp = (byte)(temp * 4);
//                    int pixel = bitmap.getPixel(i, (j * 4) + k);
//                    int r = (pixel & 0xff0000) >> 16;
//                    int g = (pixel & 0xff00) >> 8;
//                    int b = (pixel & 0xff);
//
//                    //BW模式
//                    if (r <= 100 && g <= 100 && b <= 100)
//                        temp = (byte)(temp + 0x00);
//                    else if (r >= 200 && g >= 200 && b >= 200)
//                        temp = (byte)(temp + 0x01);
//                    else
//                    {
//                        int num = (r + g + b) / 3;
//                        if (num <= 127)
//                            temp = (byte)(temp + 0x03);
//                        else
//                            temp = (byte)(temp + 0x02);
//                    }
//                }
//                image_buffer[index] = temp;
//                index++;
//            }
//        }
//        return image_buffer;
//    }
//
//    //********************************NFC************************************************************//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        if (mNfcAdapter == null) {
//            //The device does not support NFC
//            resultTextView.setText("NFC is not supported!");
//            //finish();
//            return;
//        }
//        if (!mNfcAdapter.isEnabled()) {
//            //NFC not enabled, guide the user to enable NFC in the settings
//            Intent setNfc = new Intent(Settings.ACTION_NFC_SETTINGS);
//            startActivity(setNfc);
//        };
//        resultTextView.setText("NFC is ready!");
//        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
//        mWriteTagFilters = new IntentFilter[]{tagDetected};
//        //When an NFC tag is detected, PendingIntent is used to return this activity
//        mNfcPendingIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE); //PendingIntent.FLAG_IMMUTABLE 这个是必须的，否则APP闪退
//        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mNfcAdapter.disableForegroundDispatch(this);
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        // Tag writing mode
//        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
//            detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//            Thread t2 = new Thread() {
//                @Override
//                public void run() {
//                    writeTag(detectedTag);
//                }
//            };
//            t2.start();
//
//        }
//    }
//
//
//    protected void writeTag(Tag tag) {
//        String[] tech = tag.getTechList();
//        if (tech[0].equals("android.nfc.tech.IsoDep")) {
//            IsoDep isodep = IsoDep.get(tag);
//            try {
//                isodep.setTimeout(50000);
//                if (!isodep.isConnected()) {
//                    isodep.connect();
//                }
//                if (isodep.isConnected())
//                {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            resultTextView.setText("NFC is connect");
//                        }
//                    });
//
//                    //1. IC DIY DB instruction
//                    cmd = HexString2Btyes("F0DB020000");    //IC_DIY = "F0DB020000";
//                    response = isodep.transceive(cmd);
//                    //2. Electronic paper parameter writing
//                    cmd = HexString2Btyes(data.setEpdInit(epdColor,epdInch)[0]);    //Electronic paper initialization parameter setting operation
//                    response = isodep.transceive(cmd);
//                    //3. Screen cutting
//                    cmd = HexString2Btyes(data.setEpdInit(epdColor,epdInch)[1]);    //3. Screen cutting
//                    response = isodep.transceive(cmd);
//
//                    if (response[0] == (byte) 0x90)
//                    {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                resultTextView.setText("90");
//                            }
//                        });
//                    }
//                    else
//                    {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                resultTextView.setText("Init NG");
//                            }
//                        });
//                    }
//
//                    //4. Image transmission
////The image must be rotated 90 degrees - otherwise there will be garbled display in the image
//                    bitmap0 = rotateBitmap(bitmap0, 90); //Rotate 90 degrees for convenient vertical scanning and data processing
//                    if (epdIC == 2) //SSD series operation
//                    {
//                        if (epdColor == 2)  //Black and white image transfer
//                        {
//                            GetPictureData_SSD(0);//Obtain black and white image data
//                            int datas = width0 * height0 / 8; //Total amount of electronic paper data
//                            int i;
//                            for (i = 0; i < datas / 250; i++) {
//                                cmd = new byte[250 + 5];
//                                cmd[0] = (byte) 0xF0;
//                                cmd[1] = (byte) 0xD2;
//                                cmd[2] = (byte) ScreenIndex_BW;//Image data writing area
//                                cmd[3] = (byte) i;// 索引号
//                                cmd[4] = (byte) 0xFA;
//                                for (int j = 0; j < 250; j++) {
//                                    cmd[j + 5] = image_buffer[j + 250 * i];
//                                }
//                                response = isodep.transceive(cmd);//Send black and white data
//
//                                //Data tail judgment
//                                if (i == datas / 250 - 1 && datas % 250 != 0) //Send data tail, fill in the missing data
//                                {
//                                    cmd = new byte[250 + 5];
//                                    cmd[0] = (byte) 0xF0;
//                                    cmd[1] = (byte) 0xD2;
//                                    cmd[2] = (byte) ScreenIndex_BW;  //Image data writing area
//                                    cmd[3] = (byte) (i + 1);  //Index number
//                                    cmd[4] = (byte) 0xFA;
//                                    for (int j = 0; j < 250; j++) {
//                                        cmd[j + 5] = image_buffer[j + 250 * (datas / 250)];
//                                    }
//                                    response = isodep.transceive(cmd);
//                                }
//                                dataNum=i;
//
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        float aa = (float) (250 * dataNum) / (float) datas;//Data transmission progress
//                                        int bb = (int) (aa * 100);//Enlarge data by 100x
//                                        String str = String.valueOf(bb);
//                                        resultTextView.setText("Data complete:" + str + "%");
//                                    }
//                                });
//
//
//                            }
//                        }
//                        if (epdColor == 3)  //Black, white, and red image transfer
//                        {
//                            //传递黑白数据
//                            GetPictureData_SSD(0);//Obtain black and white image data
//                            int datas = width0 * height0 / 8; //Total amount of electronic paper data
//                            int i;
//                            for (i = 0; i < datas / 250; i++) {
//                                cmd = new byte[250 + 5];
//                                cmd[0] = (byte) 0xF0;
//                                cmd[1] = (byte) 0xD2;
//                                cmd[2] = (byte) ScreenIndex_BW;//Image data writing area
//                                cmd[3] = (byte) i;// 索引号
//                                cmd[4] = (byte) 0xFA;
//                                for (int j = 0; j < 250; j++) {
//                                    cmd[j + 5] = image_buffer[j + 250 * i];
//                                }
//                                response = isodep.transceive(cmd);
//
//                                //数据尾数判断
//                                if (i == datas / 250 - 1 && datas % 250 != 0) //Send data tail, fill in the missing data
//                                {
//                                    cmd = new byte[250 + 5];
//                                    cmd[0] = (byte) 0xF0;
//                                    cmd[1] = (byte) 0xD2;
//                                    cmd[2] = (byte) ScreenIndex_BW;  //Image data writing area
//                                    cmd[3] = (byte) (i + 1);  // 索引号
//                                    cmd[4] = (byte) 0xFA;
//                                    for (int j = 0; j < 250; j++) {
//                                        cmd[j + 5] = image_buffer[j + 250 * (datas / 250)];
//                                    }
//                                    response = isodep.transceive(cmd);//Send black and white data
//                                }
//                                dataNum=i;
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        float aa = (float) (250 * dataNum) / (float)datas*2;//Data transmission progress
//                                        int bb = (int) (aa * 100); //Enlarge data by 100x
//                                        String str = String.valueOf(bb);
//                                        resultTextView.setText("Data complete:" + str + "%");
//                                    }
//                                });
//                            }
//
//                            //Transmitting red and white data
//                            GetPictureData_SSD(1);//Obtain red and white image data
//                            for (i = 0; i < datas / 250; i++) {
//                                cmd = new byte[250 + 5];
//                                cmd[0] = (byte) 0xF0;
//                                cmd[1] = (byte) 0xD2;
//                                cmd[2] = (byte) ScreenIndex_R;//Write image data into the red area
//                                cmd[3] = (byte) i;// 索引号
//                                cmd[4] = (byte) 0xFA;
//                                for (int j = 0; j < 250; j++) {
//                                    cmd[j + 5] = (byte)(0xFF - image_buffer[j + 250 * i]); //Red needs to be reversed
//                                }
//                                response = isodep.transceive(cmd);
//
//                                //数据尾数判断
//                                if (i == datas / 250 - 1 && datas % 250 != 0) //Send data tail, fill in the missing data
//                                {
//                                    cmd = new byte[250 + 5];
//                                    cmd[0] = (byte) 0xF0;
//                                    cmd[1] = (byte) 0xD2;
//                                    cmd[2] = (byte) ScreenIndex_R;  //Write image data into the red area
//                                    cmd[3] = (byte) (i + 1);  //Index number
//                                    cmd[4] = (byte) 0xFA;
//                                    for (int j = 0; j < 250; j++) {
//                                        cmd[j + 5] = (byte)(0xFF - image_buffer[j + 250 * (datas / 250)]);//Red needs to be reversed
//                                    }
//                                    response = isodep.transceive(cmd);
//                                }
//                                dataNum=i+dataNum;
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        float aa = (float) (250 * dataNum) / (float)datas*2;//Data transmission progress
//                                        int bb = (int) (aa * 100); //Enlarge data by 100x
//                                        String str = String.valueOf(bb);
//                                        resultTextView.setText("Data complete:" + str + "%");
//                                    }
//                                });
//                            }
//                        }
//                        if (epdColor == 4) //Black, white, red, and yellow data transmission
//                        {
//                            GetPictureData_4G();//Obtain image data
//                            int datas = width0 * height0 / 4; //Total amount of electronic paper data
//                            int i;
//                            for (i = 0; i < datas / 250; i++) {
//                                cmd = new byte[250 + 5];
//                                cmd[0] = (byte) 0xF0;
//                                cmd[1] = (byte) 0xD2;
//                                cmd[2] = (byte) ScreenIndex_BW;//Image data writing area
//                                cmd[3] = (byte) i;// 索引号
//                                cmd[4] = (byte) 0xFA;
//                                for (int j = 0; j < 250; j++) {
//                                    cmd[j + 5] = image_buffer[j + 250 * i];
//                                }
//                                response = isodep.transceive(cmd);
//
//                                //Data tail judgment
//                                if (i == datas / 250 - 1 && datas % 250 != 0) //Send data tail, fill in the missing data
//                                {
//                                    cmd = new byte[250 + 5];
//                                    cmd[0] = (byte) 0xF0;
//                                    cmd[1] = (byte) 0xD2;
//                                    cmd[2] = (byte) ScreenIndex_BW;  //Image data writing area,
//                                    cmd[3] = (byte) (i + 1);  //Index number
//                                    cmd[4] = (byte) 0xFA;
//                                    for (int j = 0; j < 250; j++) {
//                                        cmd[j + 5] = image_buffer[j + 250 * (datas / 250)];
//                                    }
//                                    response = isodep.transceive(cmd);//Send black and white data
//                                }
//                                dataNum=i;
//
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        float aa = (float) (250 * dataNum) / (float) datas;//Data transmission progress
//                                        int bb = (int) (aa * 100); //Enlarge data by 100x
//                                        String str = String.valueOf(bb);
//                                        resultTextView.setText("Data complete:" + str + "%");
//                                    }
//                                });
//
//
//                            }
//                        }
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                resultTextView.setText("Data complete:" + "100" + "%");
//                            }
//                        });
//                    }
//
//
//                    byte[] refreshcmd = new byte[5];
//                    if (epdColor== 4)////Four color swipe command
//                    {
//                        //epd  refresh
//                        //Support image refresh command D4
//                        refreshcmd[0] = (byte)0xF0;
//                        refreshcmd[1] = (byte)0xD4;
//                        refreshcmd[2] = (byte)0x85;
//                        refreshcmd[3] = (byte)0x80; //Directly return+image data writing area
//                        refreshcmd[4] = (byte)0x00;
//                        response = isodep.transceive(refreshcmd);
//                    }
//                    else  //Monochrome and Tricolor Brushing Commands
//                    {
//                        //epd  refresh
//                        //Support image refresh command D4
//                        refreshcmd[0] = (byte)0xF0;
//                        refreshcmd[1] = (byte)0xD4;
//                        refreshcmd[2] = (byte)0x05;
//                        refreshcmd[3] = (byte)0x80; //Directly return+image data writing area
//                        refreshcmd[4] = (byte)0x00;
//                        response = isodep.transceive(refreshcmd);
//                    }
//
//
//                    if (response[0] == (byte) 0x90)
//                    {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                resultTextView.setText("90");
//                            }
//                        });
//                    }
//                    else
//                    {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                resultTextView.setText("update NG");
//                            }
//                        });
//                    }
//
//                    bitmap0 = rotateBitmap(bitmap0, 270); //Rotate 270 degrees, reset
//
//                }
//
//            }
//            catch (IOException e)
//            {
//                //throw new RuntimeException(e);
//            }
//            finally
//            {
//                if (mNfcAdapter != null)
//                {
//                    try
//                    {
//                        isodep.close();
//
//                    }
//                    catch (IOException e)
//                    {
//
//                    }
//                }
//
//            }
//
//        }
//    }
//
//    public static Bitmap rotateBitmap(Bitmap bitmap, float angle) {
//        if (bitmap == null) {
//            return null;
//        }
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//        Matrix matrix = new Matrix();
//        matrix.postRotate(angle);
//        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
//        if (newBitmap.sameAs(bitmap)) {
//            return newBitmap;
//        }
//        bitmap.recycle();
//        return newBitmap;
//    }
//
//
//    private static String HexToString(byte[] data)
//    {
//        String ReData = "";
//        for (int i = 0; i < data.length; i++)
//        {
//            ReData += NumToString(data[i] / 16) + NumToString(data[i] % 16);
//        }
//        return ReData;
//    }
//    private static String NumToString(int c)
//    {
//
//        String data = "";
//        if (c >= 10)
//        {
//            switch (c)
//            {
//                case 10:
//                    data = "A";
//                    break;
//                case 11:
//                    data = "B";
//                    break;
//                case 12:
//                    data = "C";
//                    break;
//                case 13:
//                    data = "D";
//                    break;
//                case 14:
//                    data = "E";
//                    break;
//                case 15:
//                    data = "F";
//                    break;
//            }
//        }
//        else
//        {
//            switch (c)
//            {
//                case 0:
//                    data = "0";
//                    break;
//                case 1:
//                    data = "1";
//                    break;
//                case 2:
//                    data = "2";
//                    break;
//                case 3:
//                    data = "3";
//                    break;
//                case 4:
//                    data = "4";
//                    break;
//                case 5:
//                    data = "5";
//                    break;
//                case 6:
//                    data = "6";
//                    break;
//                case 7:
//                    data = "7";
//                    break;
//                case 8:
//                    data = "8";
//                    break;
//                case 9:
//                    data = "9";
//                    break;
//
//            }
//
//        }
//        return data;
//    }
//    private static int parse(char c)
//    {
//        if (c >= 'a')
//            return (c - 'a' + 10) & 0x0f;
//        if (c >= 'A')
//            return (c - 'A' + 10) & 0x0f;
//        return (c - '0') & 0x0f;
//    }
//
//    public static byte[] HexString2Btyes(String hexstr)
//    {
//
//        char[] charArray = hexstr.toCharArray();
//        byte[] b = new byte[hexstr.length() / 2];
//        int j = 0;
//        for (int i = 0; i < b.length; i++)
//        {
//            char c0 = charArray[j++];
//            char c1 = charArray[j++];
//            b[i] = (byte)((parse(c0) << 4) | parse(c1));
//
//        }
//        return b;
//    }
//
//
//
//
//}
