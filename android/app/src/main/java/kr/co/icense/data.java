package kr.co.icense;

import android.graphics.Bitmap;

public class data {
    public static int x;
    public static int y;
    public static int w;
    public static int h;
    public static int[] epdArray = new int[10]; //Save electronic paper resolution
    public static float scaling; //Scale ratio
    public static String epdmodel = "Please Select"; //Save the electronic paper model string
    private static Bitmap globalBitmap;
    //E-paper 参数
//***************EPD parameters*********************************************************************//
//Set electronic paper model
//Custom driver ->Cut screen ->Transfer image data ->Perform display updates
    public static String data_DB;
    public static String start;
    public static String RST;
    public static String set_wf;
    public static String set_power;
    public static String set_resolution;
    public static String set_border;
    public static String write_BW;
    public static String write_BWR;
    public static String update;
    public static String sleep;


    public static String data_DB1;//10    The last digit is half of the sum of all subsequent data.
    public static String start1;//
    public static String RST1 = "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40102"; //48
    public static String soft_RST1 = "A10112" + "A40102";//14
    public static String set_control1;//12    //01 27 01
    public static String set_mode1 = "A1021101";//8
    public static String set_RAM1;//24    //44 45
    public static String set_border1 = "A1023C05";//8
    public static String set_RAM_Shift1 = "A103210080";//10
    public static String set_temperature1 = "A1021880";//8
    public static String set_RAM_counter1;//18     //4E 4F
    public static String write_BW1 = "A30124"; //6
    public static String write_BWR1 = "A3022426"; //8
    public static String update1 = "A20222F7" + "A20120" + "A40102";//20
    public static String sleep1 = "A2021001" + "A502000A"; //16
//四色电子纸参数
    public static String set_0x4D = "A1024D78";//8   //4D 78
    public static String set_0x00 = "A103000F29";//10    //00 0F 29
    public static String set_0x01 = "A103010700";//10    //01 07 00
    public static String set_0x03 = "A10403105444";//12   //03 10 54 44
    public static String set_0x06 = "A1080605003F0A25121A";//20  //06 05 00 3F 0A 25 12 1A
    public static String set_0x50 = "A1025037";//8   //50 37
    public static String set_0x60 = "A103600202";//10    //60 02 02
    public static String set_0x61 = "A1056100800128";//14    //61 00 80 01 28
    public static String set_0xE7 = "A102E71C";//8   //E7 1C
    public static String set_0xE3 = "A102E322";//8   //E3 22
    public static String set_0xB4 = "A102B4D0";//8   //B4 D0
    public static String set_0xB5 = "A102B503";//8   //B5 03
    public static String set_0xE9 = "A102E901";//8   //E9 01
    public static String set_0x30 = "A1023008";//8   //30 08

    ////////////// 추가 20250311 ////////////////////////////////////////
    //0.97寸
    public static String set_0xB6 = "A102E71C";//8   //B6 6F
    //3.7寸
    public static String set_0x41= "A1024100";//8   //41 00
    public static String set_0x65 = "A1056500000000";//14   //65 00 00 00 00
    public static String set_0xFF1 = "A102FFA5";//8   //FF A5
    public static String set_0xEF = "A107EF011E0A1B0B17";//18   //EF011E0A1B0B17
    public static String set_0xC3 = "A108C3FDDC01DD08DE41";//20   //C3FDDC01DD08DE41
    public static String set_0xFD = "A10301E803";//10   //01E803
    public static String set_0xDA = "A102DA07";//8   //DA07
    public static String set_0xC9 = "A102C900";//8   //C900
    public static String set_0xA8 = "A102A80F";//8   //A80F
    public static String set_0xFF2 = "A102FFE3";//8   //FF E3
    //4.2寸
    public static String  set_0xAE = "A102AECF";//8   //AECF
    public static String  set_0xB0 = "A102B013";//8   //B013
    public static String  set_0xBD = "A102BD07";//8   //BD07
    public static String  set_0xBE = "A102BEFE";//8   //BEFE




    public static Bitmap getBitmap() {
        return globalBitmap;
    }

    public static void setBitmap(Bitmap bitmap) {
        data.globalBitmap = bitmap;
    }

    //x值存储
    public static int getX() {
        return x;
    }

    public static void setX(int x) {
        data.x = x;
    }

    //y值存储
    public static int getY() {
        return y;
    }

    public static void setY(int y) {
        data.y = y;
    }

    //W and H storage
//W value storage
    public static int getW() {
        return w;
    }

    public static void setW(int w) {
        data.w = w;
    }

    //h值存储
    public static int getH() {
        return h;
    }

    public static void setH(int h) {
        data.h = h;
    }


    //W and H storage
//EPD_W value storage
    public static int[] getEpdArray() {
        return epdArray;
    }

    public static void setEpdArray(int[] EpdArray) {
        data.epdArray = EpdArray;
    }

    //x值存储
    public static float getS() {
        return scaling;
    }

    public static void setS(float s) {
        data.scaling = s;
    }

    //Save electronic paper model
    public static String getEpdString() {
        return epdmodel;
    }

    public static void setEpdString(String EpdArray) {
        data.epdmodel = EpdArray;
    }


    public static String[] setEpdInit(int epdColor, int epdInch) {
        String[] epd_init = new String[10];
        ;
        if (epdColor == 2) {
            switch (epdInch) {
                /****************************************************UC系列单色屏**************************************************************/
                //屏幕尺寸分类：0.97-0111 1.54-0010   2.13-0000   2.66-0110  2.7-0100 2.9-0001 3.1-0101  3.7-0101  4.2- 0011
                case 97:
                    //0.97 88x184  SSD1680
                    data_DB1 = "F0DB000067";//10    尾数是后面所有数据和的一半。
                    start1 = "A0060720005800B8";//16   0.97寸单色88x184
                    RST1 = "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40102"; //48
                    soft_RST1 = "A10112" + "A40102";//14
                    set_control1 = "A10401270101";//12    //01 27 01
                    set_mode1 = "A1021101";//8
                    set_RAM1 = "A10344000A" + "A1054527010000";//24    //44 45
                    set_border1 = "A1023C05";//8
                    set_RAM_Shift1 = "A103210080";//10
                    set_temperature1 = "A1021880";//8
                    set_RAM_counter1 = "A1024E00" + "A1034F2701";//18     //4E 4F
                    write_BW1 = "A30124"; //6
                    write_BWR1 = "A3022426"; //8
                    update1 = "A20222F7" + "A20120" + "A40102";//20
                    sleep1 = "A2021001" + "A502000A"; //16
                    epd_init[0] = data_DB1 + start1 + RST1 + soft_RST1 + set_control1 + set_mode1 + set_RAM1 + set_border1 + set_RAM_Shift1 + set_temperature1 + set_RAM_counter1 + write_BW1 + update1 + sleep1;
                    //切屏
                    epd_init[1] = "F0DA000003F00720";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20   （屏幕分辨率和颜色和A0命令一致）
                    break;
                case 154: //2.9
                    data_DB1 = "F0DB000067";//10    尾数是后面所有数据和的一半。
                    start1 = "A006012000800128";//16   2.9寸单色128x296
                    RST1 = "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40102"; //48
                    soft_RST1 = "A10112" + "A40102";//14
                    set_control1 = "A10401270101";//12    //01 27 01
                    set_mode1 = "A1021101";//8
                    set_RAM1 = "A10344000F" + "A1054527010000";//24    //44 45
                    set_border1 = "A1023C05";//8
                    set_RAM_Shift1 = "A103210080";//10
                    set_temperature1 = "A1021880";//8
                    set_RAM_counter1 = "A1024E00" + "A1034F2701";//18     //4E 4F
                    write_BW1 = "A30124"; //6
                    write_BWR1 = "A3022426"; //8
                    update1 = "A20222F7" + "A20120" + "A40102";//20
                    sleep1 = "A2021001" + "A502000A"; //16
                    epd_init[0] = data_DB1 + start1 + RST1 + soft_RST1 + set_control1 + set_mode1 + set_RAM1 + set_border1 + set_RAM_Shift1 + set_temperature1 + set_RAM_counter1 + write_BW1 + update1 + sleep1;
                    //切屏
                    epd_init[1] = "F0DA000003F00120";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20   （屏幕分辨率和颜色和A0命令一致）
                    break;
                case 213:
                    //2.13 122x250  SSD1680
                    data_DB1 = "F0DB000067";//10    尾数是后面所有数据和的一半。
                    start1 = "A0060020007A00FA";//16   2.13寸单色128x250
                    RST1 = "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40102"; //48
                    soft_RST1 = "A10112" + "A40102";//14
                    set_control1 = "A10401270101";//12    //01 27 01
                    set_mode1 = "A1021101";//8
                    set_RAM1 = "A10344000F" + "A1054527010000";//24    //44 45
                    set_border1 = "A1023C05";//8
                    set_RAM_Shift1 = "A103210080";//10
                    set_temperature1 = "A1021880";//8
                    set_RAM_counter1 = "A1024E00" + "A1034F2701";//18     //4E 4F
                    write_BW1 = "A30124"; //6
                    write_BWR1 = "A3022426"; //8
                    update1 = "A20222F7" + "A20120" + "A40102";//20
                    sleep1 = "A2021001" + "A502000A"; //16
                    epd_init[0] = data_DB1 + start1 + RST1 + soft_RST1 + set_control1 + set_mode1 + set_RAM1 + set_border1 + set_RAM_Shift1 + set_temperature1 + set_RAM_counter1 + write_BW1 + update1 + sleep1;
                    //切屏
                    epd_init[1] = "F0DA000003F00020";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20   （屏幕分辨率和颜色和A0命令一致）
                    break;
                case 266:
                    data_DB1 = "F0DB000067";//10    尾数是后面所有数据和的一半。
                    start1 = "A006062000980128";//16   2.66寸单色152x296
                    RST1 = "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40102"; //48
                    soft_RST1 = "A10112" + "A40102";//14
                    set_control1 = "A10401270101";//12    //01 27 01
                    set_mode1 = "A1021101";//8
                    set_RAM1 = "A103440012" + "A1054527010000";//24    //44 45
                    set_border1 = "A1023C05";//8
                    set_RAM_Shift1 = "A103210080";//10
                    set_temperature1 = "A1021880";//8
                    set_RAM_counter1 = "A1024E00" + "A1034F2701";//18     //4E 4F
                    write_BW1 = "A30124"; //6
                    write_BWR1 = "A3022426"; //8
                    update1 = "A20222F7" + "A20120" + "A40102";//20
                    sleep1 = "A2021001" + "A502000A"; //16
                    epd_init[0] = data_DB1 + start1 + RST1 + soft_RST1 + set_control1 + set_mode1 + set_RAM1 + set_border1 + set_RAM_Shift1 + set_temperature1 + set_RAM_counter1 + write_BW1 + update1 + sleep1;
                    //切屏
                    epd_init[1] = "F0DA000003F00620";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20   （屏幕分辨率和颜色和A0命令一致）
                    break;
                case 270:
                    data_DB1 = "F0DB000062";//10    尾数是后面所有数据和的一半。
                    start1 = "A006042000B00108";//16   2.7寸单色176x264
                    RST1 = "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40102"; //48
                    soft_RST1 = "A10112" + "A40102";//14
                    set_control1 = "A10401270101";//12    //01 27 01
                    set_mode1 = "A1021101";//8
                    set_RAM1 = "A103440015" + "A1054527010000";//24    //44 45
                    set_border1 = "A1023C05";//8
                    //set_RAM_Shift1 = "A103210080";//10
                    set_temperature1 = "A1021880";//8
                    set_RAM_counter1 = "A1024E00" + "A1034F2701";//18     //4E 4F
                    write_BW1 = "A30124"; //6
                    write_BWR1 = "A3022426"; //8
                    update1 = "A20222F7" + "A20120" + "A40102";//20
                    sleep1 = "A2021001" + "A502000A"; //16
                    epd_init[0] = data_DB1 + start1 + RST1 + soft_RST1 + set_control1 + set_mode1 + set_RAM1 + set_border1 + set_temperature1 + set_RAM_counter1 + write_BW1 + update1 + sleep1;
                    //切屏
                    epd_init[1] = "F0DA000003F00420";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20   （屏幕分辨率和颜色和A0命令一致）
                    break;
                case 290: //2.9
                    data_DB1 = "F0DB000067";//10    尾数是后面所有数据和的一半。
                    start1 = "A006012000800128";//16   2.9寸单色128x296
                    RST1 = "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40102"; //48
                    soft_RST1 = "A10112" + "A40102";//14
                    set_control1 = "A10401270101";//12    //01 27 01
                    set_mode1 = "A1021101";//8
                    set_RAM1 = "A10344000F" + "A1054527010000";//24    //44 45
                    set_border1 = "A1023C05";//8
                    set_RAM_Shift1 = "A103210080";//10
                    set_temperature1 = "A1021880";//8
                    set_RAM_counter1 = "A1024E00" + "A1034F2701";//18     //4E 4F
                    write_BW1 = "A30124"; //6
                    write_BWR1 = "A3022426"; //8
                    update1 = "A20222F7" + "A20120" + "A40102";//20
                    sleep1 = "A2021001" + "A502000A"; //16
                    epd_init[0] = data_DB1 + start1 + RST1 + soft_RST1 + set_control1 + set_mode1 + set_RAM1 + set_border1 + set_RAM_Shift1 + set_temperature1 + set_RAM_counter1 + write_BW1 + update1 + sleep1;
                    //切屏
                    epd_init[1] = "F0DA000003F00120";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20   （屏幕分辨率和颜色和A0命令一致）
                    break;
                case 420:  //需要传两次数据，把它放入三色模式
                    data_DB1 = "F0DB000063";//10    尾数是后面所有数据和的一半。
                    start1 = "A00603300190012C";//16   4.2寸三色400x300
                    RST1 = "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40102"; //48
                    soft_RST1 = "A10112" + "A40102";//14
                    set_control1 = "A104012B0101";//12    //01 2B 01
                    set_mode1 = "A1021101";//8
                    set_RAM1 = "A103440031" + "A105452B010000";//24    //44 45
                    set_border1 = "A1023C01";//8
                    set_temperature1 = "A1021880";//8
                    set_RAM_counter1 = "A1024E00" + "A1034F2B01";//18     //4E 4F
                    write_BW1 = "A30124"; //6
                    write_BWR1 = "A3022426"; //8
                    update1 = "A20222F7" + "A20120" + "A40102";//20
                    sleep1 = "A2021001" + "A502000A"; //16
                    epd_init[0] = data_DB1 + start1 + RST1 + soft_RST1 + set_control1 + set_mode1 + set_RAM1 + set_border1 + set_temperature1 + set_RAM_counter1 + write_BWR1 + update1 + sleep1;
                    //切屏
                    epd_init[1] = "F0DA000003F00330";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20   （屏幕分辨率和颜色和A0命令一致）
                    break;
                case 370:
                    //3.71 240x416  UC8171
                    data_DB = "F0DB00005E";//10    尾数是后面所有数据和的一半。
                    start = "A006512000F001A0";//16  3.71寸单色240x416
                    RST = "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40103"; // 48 +56   复位三次
                    set_wf = "A102001F";//8  //0x00  0x1F
                    set_resolution = "A10461F001A0";//12   //0x61 f0 01 a0
                    set_power = "A10104" + "A40103";//12  //0x04  busy
                    write_BW = "A3021013"; //8    //0x10
                    update = "A20112" + "A502000A" + "A40103";//20  //0x12 delay  busy
                    sleep = "A20102" + "A40103" + "A20207A5"; //20 //0x02 busy 07 A5
                    epd_init[0] = data_DB + start + RST + set_wf + set_power + write_BW + update + sleep;   //16+104+12+8+8+20+20=188   188/2=94=0x5E
                    //切屏
                    epd_init[1] = "F0DA000003F05120";//10   屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20   （屏幕分辨率和颜色和A0命令一致）
                    break;
            }
        }
        if (epdColor == 3) {
            switch (epdInch) {
                /****************************************************UC系列单色屏**************************************************************/
                //屏幕尺寸分类：0.97-0111 1.54-0010   2.13-0000   2.66-0110  2.7-0100 2.9-0001 3.1-0101  3.7-0101  4.2- 0011
                case 97:
                    //0.97 88x184  SSD1680
                    data_DB1 = "F0DB000068";//10    尾数是后面所有数据和的一半。
                    start1 = "A0060730005800B8";//16   0.97寸三色88x184
                    RST1 = "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40102"; //48
                    soft_RST1 = "A10112" + "A40102";//14
                    set_control1 = "A10401270101";//12    //01 27 01
                    set_mode1 = "A1021101";//8
                    set_RAM1 = "A10344000A" + "A1054527010000";//24    //44 45
                    set_border1 = "A1023C05";//8
                    set_RAM_Shift1 = "A103210080";//10
                    set_temperature1 = "A1021880";//8
                    set_RAM_counter1 = "A1024E00" + "A1034F2701";//18     //4E 4F
                    write_BW1 = "A30124"; //6
                    write_BWR1 = "A3022426"; //8
                    update1 = "A20222F7" + "A20120" + "A40102";//20
                    sleep1 = "A2021001" + "A502000A"; //16
                    epd_init[0] = data_DB1 + start1 + RST1 + soft_RST1 + set_control1 + set_mode1 + set_RAM1 + set_border1 + set_RAM_Shift1 + set_temperature1 + set_RAM_counter1 + write_BWR1 + update1 + sleep1;
                    //切屏
                    epd_init[1] = "F0DA000003F00730";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20   （屏幕分辨率和颜色和A0命令一致）
                    break;
                case 154:
                    //1.54 200x200  SSD1681
                    data_DB1 = "F0DB000063";//10
                    start1 = "A006123000C800C8";//16    1.54寸三色200x200
                    RST1 = "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40102"; //48
                    soft_RST1 = "A10112" + "A40102";//14
                    set_control1 = "A10401C70001";//12
                    set_mode1 = "A1021101";//8
                    set_RAM1 = "A103440018" + "A10545C7000000";//24
                    set_border1 = "A1023C05";//8
                    set_temperature1 = "A1021880";//8
                    set_RAM_counter1 = "A1024E00" + "A1034FC700";//18
                    write_BW1 = "A30124"; //6
                    write_BWR1 = "A3022426"; //8
                    update1 = "A20222F7" + "A20120" + "A40102";//20
                    sleep1 = "A2021001" + "A502000A"; //16
                    epd_init[0] = data_DB1 + start1 + RST1 + soft_RST1 + set_control1 + set_mode1 + set_RAM1 + set_border1 + set_temperature1 + set_RAM_counter1 + write_BWR1 + update1 + sleep1;
                    //切屏
                    epd_init[1] = "F0DA000003F01230";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20   （屏幕分辨率和颜色和A0命令一致）
                    break;
                case 213:
                    //2.13 122x250  SSD1680
                    data_DB1 = "F0DB000068";//10    尾数是后面所有数据和的一半。
                    start1 = "A0060030007A00FA";//16   2.13寸三色128x250
                    RST1 = "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40102"; //48
                    soft_RST1 = "A10112" + "A40102";//14
                    set_control1 = "A10401270101";//12    //01 27 01
                    set_mode1 = "A1021101";//8
                    set_RAM1 = "A10344000F" + "A1054527010000";//24    //44 45
                    set_border1 = "A1023C05";//8
                    set_RAM_Shift1 = "A103210080";//10
                    set_temperature1 = "A1021880";//8
                    set_RAM_counter1 = "A1024E00" + "A1034F2701";//18     //4E 4F
                    write_BW1 = "A30124"; //6
                    write_BWR1 = "A3022426"; //8
                    update1 = "A20222F7" + "A20120" + "A40102";//20
                    sleep1 = "A2021001" + "A502000A"; //16
                    epd_init[0] = data_DB1 + start1 + RST1 + soft_RST1 + set_control1 + set_mode1 + set_RAM1 + set_border1 + set_RAM_Shift1 + set_temperature1 + set_RAM_counter1 + write_BWR1 + update1 + sleep1;
                    //切屏
                    epd_init[1] = "F0DA000003F00030";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20   （屏幕分辨率和颜色和A0命令一致）
                    break;
                case 266:
                    data_DB1 = "F0DB000068";//10    尾数是后面所有数据和的一半。
                    start1 = "A006063000980128";//16   2.66寸三色152x296
                    RST1 = "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40102"; //48
                    soft_RST1 = "A10112" + "A40102";//14
                    set_control1 = "A10401270101";//12    //01 27 01
                    set_mode1 = "A1021101";//8
                    set_RAM1 = "A103440012" + "A1054527010000";//24    //44 45
                    set_border1 = "A1023C05";//8
                    set_RAM_Shift1 = "A103210080";//10
                    set_temperature1 = "A1021880";//8
                    set_RAM_counter1 = "A1024E00" + "A1034F2701";//18     //4E 4F
                    write_BW1 = "A30124"; //6
                    write_BWR1 = "A3022426"; //8
                    update1 = "A20222F7" + "A20120" + "A40102";//20
                    sleep1 = "A2021001" + "A502000A"; //16
                    epd_init[0] = data_DB1 + start1 + RST1 + soft_RST1 + set_control1 + set_mode1 + set_RAM1 + set_border1 + set_RAM_Shift1 + set_temperature1 + set_RAM_counter1 + write_BWR1 + update1 + sleep1;
                    //切屏
                    epd_init[1] = "F0DA000003F00630";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20   （屏幕分辨率和颜色和A0命令一致）
                    break;
                case 270:
                    data_DB1 = "F0DB000063";//10    尾数是后面所有数据和的一半。
                    start1 = "A006043000B00108";//16   2.7寸三色176x264
                    RST1 = "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40102"; //48
                    soft_RST1 = "A10112" + "A40102";//14
                    set_control1 = "A10401270101";//12    //01 27 01
                    set_mode1 = "A1021101";//8
                    set_RAM1 = "A103440015" + "A1054527010000";//24    //44 45
                    set_border1 = "A1023C05";//8
                    //set_RAM_Shift1 = "A103210080";//10
                    set_temperature1 = "A1021880";//8
                    set_RAM_counter1 = "A1024E00" + "A1034F2701";//18     //4E 4F
                    write_BW1 = "A30124"; //6
                    write_BWR1 = "A3022426"; //8
                    update1 = "A20222F7" + "A20120" + "A40102";//20
                    sleep1 = "A2021001" + "A502000A"; //16
                    epd_init[0] = data_DB1 + start1 + RST1 + soft_RST1 + set_control1 + set_mode1 + set_RAM1 + set_border1 + set_temperature1 + set_RAM_counter1 + write_BWR1 + update1 + sleep1;
                    //切屏
                    epd_init[1] = "F0DA000003F00430";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20   （屏幕分辨率和颜色和A0命令一致）
                    break;
                case 290: //2.9
                    data_DB1 = "F0DB000068";//10    尾数是后面所有数据和的一半。
                    start1 = "A006013000800128";//16   2.9寸三色128x296
                    RST1 = "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40102"; //48
                    soft_RST1 = "A10112" + "A40102";//14
                    set_control1 = "A10401270101";//12    //01 27 01
                    set_mode1 = "A1021101";//8
                    set_RAM1 = "A10344000F" + "A1054527010000";//24    //44 45
                    set_border1 = "A1023C05";//8
                    set_RAM_Shift1 = "A103210080";//10
                    set_temperature1 = "A1021880";//8
                    set_RAM_counter1 = "A1024E00" + "A1034F2701";//18     //4E 4F
                    write_BW1 = "A30124"; //6
                    write_BWR1 = "A3022426"; //8
                    update1 = "A20222F7" + "A20120" + "A40102";//20
                    sleep1 = "A2021001" + "A502000A"; //16
                    epd_init[0] = data_DB1 + start1 + RST1 + soft_RST1 + set_control1 + set_mode1 + set_RAM1 + set_border1 + set_RAM_Shift1 + set_temperature1 + set_RAM_counter1 + write_BWR1 + update1 + sleep1;
                    //切屏
                    epd_init[1] = "F0DA000003F00130";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20   （屏幕分辨率和颜色和A0命令一致）
                    break;
                case 420:
                    data_DB1 = "F0DB000063";//10    尾数是后面所有数据和的一半。
                    start1 = "A00603300190012C";//16   4.2寸三色400x300
                    RST1 = "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40102"; //48
                    soft_RST1 = "A10112" + "A40102";//14
                    set_control1 = "A104012B0101";//12    //01 2B 01
                    set_mode1 = "A1021101";//8
                    set_RAM1 = "A103440031" + "A105452B010000";//24    //44 45
                    set_border1 = "A1023C01";//8
                    set_temperature1 = "A1021880";//8
                    set_RAM_counter1 = "A1024E00" + "A1034F2B01";//18     //4E 4F
                    write_BW1 = "A30124"; //6
                    write_BWR1 = "A3022426"; //8
                    update1 = "A20222F7" + "A20120" + "A40102";//20
                    sleep1 = "A2021001" + "A502000A"; //16
                    epd_init[0] = data_DB1 + start1 + RST1 + soft_RST1 + set_control1 + set_mode1 + set_RAM1 + set_border1 + set_temperature1 + set_RAM_counter1 + write_BWR1 + update1 + sleep1;
                    //切屏
                    epd_init[1] = "F0DA000003F00330";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20   （屏幕分辨率和颜色和A0命令一致）
                    break;
                case 370:
                    //3.71 240x416  UC8171
                    data_DB = "F0DB00005E";//10    尾数是后面所有数据和的一半。
                    start = "A006513000F001A0";//16  3.71寸单色240x416
                    RST = "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40103"; // 48 +56   复位三次
                    set_wf = "A102000F";//8  //0x00  0x0F
                    set_resolution = "A10461F001A0";//12   //0x61 f0 01 a0
                    set_power = "A10104" + "A40103";//12  //0x04  busy
                    write_BW = "A3021013"; //8    //0x10
                    update = "A20112" + "A502000A" + "A40103";//20  //0x12 delay  busy
                    sleep = "A20102" + "A40103" + "A20207A5"; //20 //0x02 busy 07 A5
                    epd_init[0] = data_DB + start + RST + set_wf + set_power + write_BW + update + sleep;   //16+104+12+8+8+20+20=188   188/2=94=0x5E
                    //切屏
                    epd_init[1] = "F0DA000003F05130";//10   屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20   （屏幕分辨率和颜色和A0命令一致）
                    break;
            }
        }
        if (epdColor == 4) {
            switch (epdInch) {
                /****************************************************UC系列单色屏**************************************************************/
                //屏幕尺寸分类：0.97-0111 1.54-0010   2.13-0000   2.66-0110  2.7-0100 2.9-0001 3.1-0101  3.7-0101  4.2- 0011
                case 97:
                    data_DB1 = "F0DB000076";//10    尾数是后面所有数据和的一半。
                    start1 = "A006072000B000B8";//16   0.97寸四色88x184--176x184分辨率放大一倍
                    RST1 = "A40108" + "A5020028" + "A4010C" + "A5020028" + "A40103"; //34  //RST0 40ms RST1 40ms BUSY
                    set_0x4D = "A1024D78";//8   //4D 78
                    set_0x00 = "A103000F29";//10    //00 0F 29
                    set_0x01 = "A103010700";//10    //01 07 00
                    set_0x03 = "A10403105444";//12   //03 10 54 44
                    set_0x06 = "A1080605003F0A25121A";//20  //06 05 00 3F 0A 25 12 1A
                    set_0x50 = "A1025037";//8   //50 37
                    set_0x60 = "A103600202";//10    //60 02 02
                    set_0x61 = "A10561005800B8";//14    //61 00 58 00 B8
                    set_0xE7 = "";//8   //E7 1C
                    set_0xE3 = "A102E322";//8   //E3 22
                    set_0xB6 = "A102E71C";//8   //B6 6F
                    set_0xB4 = "A102B4D0";//8   //B4 D0
                    set_0xB5 = "";//8   //B5 03
                    set_0xE9 = "A102E901";//8   //E9 01
                    set_0x30 = "A1023008";//8   //30 08
                    set_power = "A10104" + "A40103";//12  //04  busy
                    write_BW = "A30110"; //6    //0x10
                    update = "A2021200" + "A40103";//14  //12 00  busy
                    sleep = "A2020200" + "A40103" + "A20207A5"; //22 //0x02 00 busy 07 A5
                    epd_init[0] = data_DB1 + start1 + RST1 + set_0x4D + set_0x00 + set_0x01 + set_0x03 + set_0x06 + set_0x50 + set_0x60 + set_0x61 +
                            set_0xB6 + set_0xE3 + set_0xB4 + set_0xE9 + set_0x30 + set_power + write_BW + update + sleep; //236 /2=118=0x76
                    //切屏
                    epd_init[1] = "F0DA000003F00720";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20 -四色  （屏幕分辨率和颜色和A0命令一致）
                    break;
                case 213:
                    data_DB1 = "F0DB00007A";//10    尾数是后面所有数据和的一半。
                    start1 = "A0060020010000FA";//16   2.13寸四色128x250--256x250分辨率放大一倍
                    RST1 = "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40103"; //34  //RST0 10ms RST1 10ms BUSY
                    set_0x4D = "A1024D78";//8   //4D 78
                    set_0x00 = "A103000F29";//10    //00 0F 29
                    set_0x01 = "A103010700";//10    //01 07 00
                    set_0x03 = "A10403105444";//12   //03 10 54 44
                    set_0x06 = "A1080605003F0A25121A";//20  //06 05 00 3F 0A 25 12 1A
                    set_0x50 = "A1025037";//8   //50 37
                    set_0x60 = "A103600202";//10    //60 02 02
                    set_0x61 = "A10561008000FA";//14    //61 00 80 00 FA
                    set_0xE7 = "A102E71C";//8   //E7 1C
                    set_0xE3 = "A102E322";//8   //E3 22
                    set_0xB4 = "A102B4D0";//8   //B4 D0
                    set_0xB5 = "A102B503";//8   //B5 03
                    set_0xE9 = "A102E901";//8   //E9 01
                    set_0x30 = "A1023008";//8   //30 08
                    set_power = "A10104" + "A40103";//12  //04  busy
                    write_BW = "A30110"; //6    //0x10
                    update = "A2021200" + "A40103";//14  //12 00  busy
                    sleep = "A2020200" + "A40103" + "A20207A5"; //22 //0x02 00 busy 07 A5
                    epd_init[0] = data_DB1 + start1 + RST1 + set_0x4D + set_0x00 + set_0x01 + set_0x03 + set_0x06 + set_0x50 + set_0x60 + set_0x61 +
                            set_0xE7 + set_0xE3 + set_0xB4 + set_0xB5 + set_0xE9 + set_0x30 + set_power + write_BW + update + sleep; //244 /2=122=0x7A
                    //切屏
                    epd_init[1] = "F0DA000003F00020";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20 -四色  （屏幕分辨率和颜色和A0命令一致）
                    break;
               /* case 213: //新基板（测试的时候与手机反向一致测试，这样信号最强）
                    data_DB1 = "F0DB000038";//10    尾数是后面所有数据和的一半。
                    start1 = "A0060020010000FA";//16   2.13寸四色128x250--256x250分辨率放大一倍
                    RST1 = "A40108" + "A5020028" + "A4010C" + "A5020028" + "A40103"; //34  //RST0 40ms RST1 40ms BUSY
                    set_0xE9 = "A102E901";//8   //E9 01
                    set_power = "A10104" + "A40103";//12  //04  busy
                    write_BW = "A30110"; //6    //0x10
                    update = "A2021200" + "A40103";//14  //12 00  busy
                    sleep = "A2020200" + "A40103" + "A20207A5"; //22 //0x02 00 busy 07 A5
                    epd_init[0] = data_DB1 + start1 + RST1 + set_0xE9 + set_power + write_BW + update + sleep; //112 /2=56=0x38
                    //切屏
                    epd_init[1] = "F0DA000003F00020";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20 -四色  （屏幕分辨率和颜色和A0命令一致）
                    break;
                    */
                case 290:
                    data_DB1 = "F0DB00007A";//10    尾数是后面所有数据和的一半。
                    start1 = "A006012001000128";//16   2.9寸四色128x296--256x296分辨率放大一倍
                    RST1 = "A40108" + "A5020028" + "A4010C" + "A5020028" + "A40103"; //34  //RST0 40ms RST1 40ms BUSY
                    set_0x4D = "A1024D78";//8   //4D 78
                    set_0x00 = "A103000F29";//10    //00 0F 29
                    set_0x01 = "A103010700";//10    //01 07 00
                    set_0x03 = "A10403105444";//12   //03 10 54 44
                    set_0x06 = "A1080605003F0A25121A";//20  //06 05 00 3F 0A 25 12 1A
                    set_0x50 = "A1025037";//8   //50 37
                    set_0x60 = "A103600202";//10    //60 02 02
                    set_0x61 = "A1056100800128";//14    //61 00 80 01 28
                    set_0xE7 = "A102E71C";//8   //E7 1C
                    set_0xE3 = "A102E322";//8   //E3 22
                    set_0xB4 = "A102B4D0";//8   //B4 D0
                    set_0xB5 = "A102B503";//8   //B5 03
                    set_0xE9 = "A102E901";//8   //E9 01
                    set_0x30 = "A1023008";//8   //30 08
                    set_power = "A10104" + "A40103";//12  //04  busy
                    write_BW = "A30110"; //6    //0x10
                    update = "A2021200" + "A40103";//14  //12 00  busy
                    sleep = "A2020200" + "A40103" + "A20207A5"; //22 //0x02 00 busy 07 A5
                    epd_init[0] = data_DB1 + start1 + RST1 + set_0x4D + set_0x00 + set_0x01+ set_0x03+ set_0x06+ set_0x50+ set_0x60 + set_0x61+
                            set_0xE7 + set_0xE3 + set_0xB4 + set_0xB5 + set_0xE9 + set_0x30 + set_power + write_BW + update + sleep; //244 /2=122=0x7A
                    //切屏
                    epd_init[1] = "F0DA000003F00120";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20 -四色  （屏幕分辨率和颜色和A0命令一致）
                    break;
                case 291://2.9寸高分
                    data_DB1 = "F0DB00007A";//10    尾数是后面所有数据和的一半。
                    start1 = "A006012001500180";//16   2.9寸四色168x384--366x384分辨率放大一倍
                    RST1 = "A40108" + "A5020028" + "A4010C" + "A5020028" + "A40103"; //34  //RST0 40ms RST1 40ms BUSY
                    set_0x4D = "A1024D78";//8   //4D 78
                    set_0x00 = "A103000F29";//10    //00 0F 29
                    set_0x01 = "A103010700";//10    //01 07 00
                    set_0x03 = "A10403105444";//12   //03 10 54 44
                    set_0x06 = "A1080605003F0A25121A";//20  //06 05 00 3F 0A 25 12 1A
                    set_0x50 = "A1025037";//8   //50 37
                    set_0x60 = "A103600202";//10    //60 02 02
                    set_0x61 = "A1056100A80180";//14    //61 00 A8 01 80
                    set_0xE7 = "A102E71C";//8   //E7 1C
                    set_0xE3 = "A102E322";//8   //E3 22
                    set_0xB4 = "A102B4D0";//8   //B4 D0
                    set_0xB5 = "A102B503";//8   //B5 03
                    set_0xE9 = "A102E901";//8   //E9 01
                    set_0x30 = "A1023008";//8   //30 08
                    set_power = "A10104" + "A40103";//12  //04  busy
                    write_BW = "A30110"; //6    //0x10
                    update = "A2021200" + "A40103";//14  //12 00  busy
                    sleep = "A2020200" + "A40103" + "A20207A5"; //22 //0x02 00 busy 07 A5
                    epd_init[0] = data_DB1 + start1 + RST1 + set_0x4D + set_0x00 + set_0x01 + set_0x03 + set_0x06 + set_0x50 + set_0x60 + set_0x61 +
                            set_0xE7 + set_0xE3 + set_0xB4 + set_0xB5 + set_0xE9 + set_0x30 + set_power + write_BW + update + sleep; //244 /2=122=0x7A
                    //切屏
                    epd_init[1] = "F0DA000003F00120";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20 -四色  （屏幕分辨率和颜色和A0命令一致）
                    break;
                case 266:
                    data_DB1 = "F0DB00007A";//10    尾数是后面所有数据和的一半。
                    start1 = "A006062001300128";//16   2.66寸四色152x296--304x296分辨率放大一倍
                    RST1 = "A40108" + "A502000A" + "A4010C" + "A502000A" + "A40103"; //34  //RST0 10ms RST1 10ms BUSY
                    set_0x4D = "A1024D78";//8   //4D 78
                    set_0x00 = "A103000F29";//10    //00 0F 29
                    set_0x01 = "A103010700";//10    //01 07 00
                    set_0x03 = "A10403105444";//12   //03 10 54 44
                    set_0x06 = "A1080605003F0A25121A";//20  //06 05 00 3F 0A 25 12 1A
                    set_0x50 = "A1025037";//8   //50 37
                    set_0x60 = "A103600202";//10    //60 02 02
                    set_0x61 = "A1056100980128";//14    //61 00 98 01 28
                    set_0xE7 = "A102E71C";//8   //E7 1C
                    set_0xE3 = "A102E322";//8   //E3 22
                    set_0xB4 = "A102B4D0";//8   //B4 D0
                    set_0xB5 = "A102B503";//8   //B5 03
                    set_0xE9 = "A102E901";//8   //E9 01
                    set_0x30 = "A1023008";//8   //30 08
                    set_power = "A10104" + "A40103";//12  //04  busy
                    write_BW = "A30110"; //6    //0x10
                    update = "A2021200" + "A40103";//14  //12 00  busy
                    sleep = "A2020200" + "A40103" + "A20207A5"; //22 //0x02 00 busy 07 A5
                    epd_init[0] = data_DB1 + start1 + RST1 + set_0x4D + set_0x00 + set_0x01 + set_0x03 + set_0x06 + set_0x50 + set_0x60 + set_0x61 +
                            set_0xE7 + set_0xE3 + set_0xB4 + set_0xB5 + set_0xE9 + set_0x30 + set_power + write_BW + update + sleep; //244 /2=122=0x7A
                    //切屏
                    epd_init[1] = "F0DA000003F00620";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20 -四色  （屏幕分辨率和颜色和A0命令一致）
                    break;
                case 267: //2.66寸高分
                    data_DB1 = "F0DB00007A";//10    尾数是后面所有数据和的一半。
                    start1 = "A006062001700168";//16   2.66寸高分四色184x360--368x360分辨率放大一倍
                    RST1 = "A40108" + "A5020028" + "A4010C" + "A5020028" + "A40103"; //34  //RST0 40ms RST1 40ms BUSY
                    set_0x4D = "A1024D78";//8   //4D 78
                    set_0x00 = "A103000F29";//10    //00 0F 29
                    set_0x01 = "A103010700";//10    //01 07 00
                    set_0x03 = "A10403105444";//12   //03 10 54 44
                    set_0x06 = "A1080605003F0A25121A";//20  //06 05 00 3F 0A 25 12 1A
                    set_0x50 = "A1025037";//8   //50 37
                    set_0x60 = "A103600202";//10    //60 02 02
                    set_0x61 = "A1056100B80168";//14    //61 00 B8 01 68
                    set_0xE7 = "A102E71C";//8   //E7 1C
                    set_0xE3 = "A102E322";//8   //E3 22
                    set_0xB4 = "A102B4D0";//8   //B4 D0
                    set_0xB5 = "A102B503";//8   //B5 03
                    set_0xE9 = "A102E901";//8   //E9 01
                    set_0x30 = "A1023008";//8   //30 08
                    set_power = "A10104" + "A40103";//12  //04  busy
                    write_BW = "A30110"; //6    //0x10
                    update = "A2021200" + "A40103";//14  //12 00  busy
                    sleep = "A2020200" + "A40103" + "A20207A5"; //22 //0x02 00 busy 07 A5
                    epd_init[0] = data_DB1 + start1 + RST1 + set_0x4D + set_0x00 + set_0x01 + set_0x03 + set_0x06 + set_0x50 + set_0x60 + set_0x61 +
                            set_0xE7 + set_0xE3 + set_0xB4 + set_0xB5 + set_0xE9 + set_0x30 + set_power + write_BW + update + sleep; //244 /2=122=0x7A
                    //切屏
                    epd_init[1] = "F0DA000003F00620";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20 -四色  （屏幕分辨率和颜色和A0命令一致）
                    break;
                case 370:
                    data_DB1 = "F0DB0000A5";//10    尾数是后面所有数据和的一半。
                    start1 = "A006512001E001A0";//16   3.7寸四色240x416--480x416分辨率放大一倍
                    RST1 = "A40108" + "A5020028" + "A4010C" + "A5020028" + "A40103"; //34  //RST0 40ms RST1 40ms BUSY

                    set_0x00 = "A103000F29";//10    //00 0F 29
                    set_0x01 = "A10701070022780A22";//18    //01 07 00 22 78 0A 22
                    set_0x03 = "A10403105444";//12   //03 10 54 44
                    set_0x06 = "A10406C0C0C0";//12  //06 C0 C0 C0
                    set_0x30 = "A1023008";//8   //30 08
                    set_0x41= "A1024100";//8   //41 00
                    set_0x50 = "A1025037";//8   //50 37
                    set_0x60 = "A103600202";//10    //60 02 02
                    set_0x61 = "A1056100F001A0";//14    //61 00 F0 01 A0
                    set_0x65 = "A1056500000000";//14   //65 00 00 00 00
                    set_0xE7 = "A102E71C";//8   //E7 1C
                    set_0xE3 = "A102E322";//8   //E3 22
                    set_0xFF1 = "A102FFA5";//8   //FF A5
                    set_0xEF = "A107EF011E0A1B0B17";//18   //EF011E0A1B0B17
                    set_0xC3 = "A108C3FDDC01DD08DE41";//20   //C3FDDC01DD08DE41
                    set_0xFD = "A10301E803";//10   //01E803
                    set_0xDA = "A102DA07";//8   //DA07
                    set_0xC9 = "A102C900";//8   //C900
                    set_0xA8 = "A102A80F";//8   //A80F
                    set_0xFF2 = "A102FFE3";//8   //FF E3
                    set_0xE9 = "A102E901";//8   //E901

                    set_power = "A10104" + "A40103";//12  //04  busy
                    write_BW = "A30110"; //6    //0x10
                    update = "A2021200" + "A40103";//14  //12 00  busy
                    sleep = "A2020200" + "A40103" + "A20207A5"; //22 //0x02 00 busy 07 A5
                    epd_init[0] = data_DB1 + start1 + RST1  + set_0x00 + set_0x01 + set_0x03 + set_0x06 + set_0x30 + set_0x41 + set_0x50 +
                            set_0x60 + set_0x61 + set_0x65 + set_0xE7 + set_0xE3 + set_0xFF1 + set_0xEF + set_0xC3 + set_0xFD + set_0xDA + set_0xC9 + set_0xA8 + set_0xFF2 + set_0xE9 + set_power + write_BW + update + sleep; //330 /2=165=0xA5
                    //切屏
                    epd_init[1] = "F0DA000003F05120";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20 -四色  （屏幕分辨率和颜色和A0命令一致）
                    break;
                case 420:
                    data_DB1 = "F0DB00006A";//10    尾数是后面所有数据和的一半。
                    start1 = "A00603300190012C";//16   4.2寸四色400x300--400x300分辨率放大一倍
                    RST1 = "A40108" + "A5020028" + "A4010C" + "A5020028" + "A40103"; //34  //RST0 40ms RST1 40ms BUSY
                    set_0x4D = "A1024D78";//8   //4D 78
                    set_0x00 = "A103000F29";//10    //00 0F 29
                    set_0x06 = "A108060D122425122910";//20  //060D122425122910
                    set_0x30 = "A1023008";//8   //30 08
                    set_0x50 = "A1025037";//8   //50 37
                    set_0x61 = "A10561012C0190";//14    //61012C0190
                    set_0xAE = "A102AECF";//8   //AECF
                    set_0xB0 = "A102B013";//8   //B013
                    set_0xBD = "A102BD07";//8   //BD07
                    set_0xBE = "A102BEFE";//8   //BEFE
                    set_0xE9 = "A102E901";//8   //E9 01

                    set_power = "A10104" + "A40103";//12  //04  busy
                    write_BW = "A30110"; //6    //0x10
                    update = "A2021200" + "A40103";//14  //12 00  busy
                    sleep = "A2020200" + "A40103" + "A20207A5"; //22 //0x02 00 busy 07 A5
                    epd_init[0] = data_DB1 + start1 + RST1 + set_0x4D + set_0x00 + set_0x06 + set_0x30 + set_0x50 + set_0x61 +
                            set_0xAE + set_0xB0 + set_0xBD + set_0xBE + set_0xE9 + set_power + write_BW + update + sleep; //212 /2=106=0x6A
                    //切屏
                    epd_init[1] = "F0DA000003F00330";//10    屏幕参数0000  数据长度03   自定义屏幕F0    屏幕尺寸分辨率12   屏幕颜色20 -四色  （屏幕分辨率和颜色和A0命令一致）
                    break;

//                case 370: // 3.7-inch e-paper (4-color, 416x240)
//                    data_DB1 = "F0DB00007A"; // 10    The last digit is half of the sum of all subsequent data.
//                    start1 = "A006012001E001A0"; // 16   3.7-inch four-color 240x416 resolution, adjusted magnification
//                    RST1 = "A40108" + "A5020028" + "A4010C" + "A5020028" + "A40103"; // 34  //RST0 40ms RST1 40ms BUSY
//                    set_0x4D = "A1024D78"; // 8   // 4D 78
//                    set_0x00 = "A103000F29"; // 10    // 00 0F 29
//                    set_0x01 = "A103010700"; // 10    // 01 07 00
//                    set_0x03 = "A10403105444"; // 12   // 03 10 54 44
//                    set_0x06 = "A1080605003F0A25121A"; // 20  // 06 05 00 3F 0A 25 12 1A
//                    set_0x50 = "A1025037"; // 8   // 50 37
//                    set_0x60 = "A103600202"; // 10    // 60 02 02
//                    set_0x61 = "A1056100F00190"; // 14    // 61 00 F0 01 90 (416x240 해상도에 맞춤)
//                    set_0xE7 = "A102E71C"; // 8   // E7 1C
//                    set_0xE3 = "A102E322"; // 8   // E3 22
//                    set_0xB4 = "A102B4D0"; // 8   // B4 D0
//                    set_0xB5 = "A102B503"; // 8   // B5 03
//                    set_0xE9 = "A102E901"; // 8   // E9 01
//                    set_0x30 = "A1023008"; // 8   // 30 08
//                    set_power = "A10104" + "A40103"; // 12  // 04  busy
//                    write_BW = "A30110"; // 6    // 0x10
//                    update = "A2021200" + "A40103"; // 14  // 12 00  busy
//                    sleep = "A2020200" + "A40103" + "A20207A5"; // 22 // 0x02 00 busy 07 A5
//                    epd_init[0] = data_DB1 + start1 + RST1 + set_0x4D + set_0x00 + set_0x01 + set_0x03 + set_0x06 + set_0x50 + set_0x60 + set_0x61 +
//                            set_0xE7 + set_0xE3 + set_0xB4 + set_0xB5 + set_0xE9 + set_0x30 + set_power + write_BW + update + sleep; // 248 /2=124=0x7E
//                    //切屏
//                    epd_init[1] = "F0DA000003F00120"; // 10    Screen parameter 0000 Data length 03 Custom screen F0 Screen size Resolution 19 Screen color 20-Four color (Screen resolution and color are consistent with A0 command)
//                    break;



            }
        }
        return epd_init;
    }

}










