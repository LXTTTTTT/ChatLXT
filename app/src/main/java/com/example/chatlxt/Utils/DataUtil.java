package com.example.chatlxt.Utils;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据处理工具
 */
public class DataUtil {

    public static final String GB18030 = "GB18030";

    // bytes 转十六进制字符串
    public static String bytes2Hex(byte[] bytes){
        String hex = "";
        for (int i = 0; i < bytes.length; i++) {
            int value = bytes[i] & 0xff;
            String hexVaule = Integer.toHexString(value);
            if (hexVaule.length() < 2) {
                hexVaule = "0" + hexVaule;
            }
            hex += hexVaule;
        }
        return hex;
    }

    public static String byte2hex(byte b) {
        int value = b & 0xff;
        String hexVaule = Integer.toHexString(value);
        if (hexVaule.length() < 2) {
            hexVaule = "0" + hexVaule;
        }
        return hexVaule;
    }


    // 16进制 str 转 Ascii字符
    public static String hex2Ascii(String hexStr) {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    // str 转 MD5
    public static String string2MD5(String raw) {
        String md5Str = raw;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); // 创建一个MD5算法对象
            md.update(raw.getBytes()); // 给算法对象加载待加密的原始数据
            byte[] encryContext = md.digest(); // 调用digest方法完成哈希计算
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < encryContext.length; offset++) {
                i = encryContext[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i)); // 把字节数组逐位转换为十六进制数
            }
            md5Str = buf.toString(); // 拼装加密字符串
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5Str.toLowerCase(); // 输出小写写的加密串
    }


    // long 时间戳（毫秒）转 str 日期
    public static String stamp2Date(long s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // 要转的格式

        Date date = new Date(s);//(s + 28800000);
        res = simpleDateFormat.format(date);
        return res;
    }


    // 16进制字符串转int
    public static int hex2Int(String hex) {
        byte[] bytes = hex2bytes(hex);
        return bytes2int(bytes);
    }


    // 16进制转换成Long
    public static long hex2Long(String hex) {
        byte[] bytes = hex2bytes(hex);
        return bytes2long(bytes);
    }

    // 16进制转换成 int_str
    public static String hex2IntString(String hex) {
        byte[] bytes = hex2bytes(hex);
        return String.valueOf(bytes2int(bytes));
    }


    // 16进制转String
    public static String hex2String(String hex) {
        byte[] bytes = hex2bytes(hex);
        return bytes2string(bytes);
    }



    // string转16进制
    public static String string2Hex(String str) {
        String hex;
        try {
            byte[] bytes = string2bytes(str, GB18030);
            hex = bytes2Hex(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return hex;
    }

    public static final byte[] hex2byte(String strHexData) {
        if (strHexData == null) {
            return null;
        } else {
            byte[] bye = new byte[strHexData.length() / 2];
            int intLen = strHexData.length();
            int i = 0;

            for(int j = 0; i < intLen; ++j) {
                byte tmpByte1 = (byte)strHexData.charAt(i);
                byte bye1 = (byte)(char2HexByte((char)tmpByte1) << 4);
                byte tmpByte2 = (byte)strHexData.charAt(i + 1);
                byte bye2 = (byte)(char2HexByte((char)tmpByte2) & 15);
                bye[j] = (byte)(bye1 + bye2);
                i += 2;
            }

            return bye;
        }
    }



    /**
     * String转bytes
     *
     * @param str
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] string2bytes(String str, String charset) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        return str.getBytes(charset);
    }

    // 16进制转bytes
    public static byte[] hex2bytes(String hex) {
        if (hex == null || hex.length() < 1) {
            return null;
        }
        // 如果长度不是偶数，则前面补0
        if (hex.length() % 2 != 0) {
            hex = "0" + hex;
        }
        byte[] bytes = new byte[(hex.length() + 1) / 2];
        try {
            for (int i = 0, j = 0; i < hex.length(); i += 2) {
                byte hight = (byte) (Character.digit(hex.charAt(i), 16) & 0xff);
                byte low = (byte) (Character.digit(hex.charAt(i + 1), 16) & 0xff);
                bytes[j++] = (byte) (hight << 4 | low);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bytes;
    }

    // 16进制转bytes 2
    public static byte[] hexStringToBytes(@NonNull final String arg) {
        if (arg != null) {
            /* 1.先去除String中的' '，然后将String转换为char数组 */
            char[] NewArray = new char[1000];
            char[] array = arg.toCharArray();
            int length = 0;
            for (int i = 0; i < array.length; i++) {
                if (array[i] != ' ') {
                    NewArray[length] = array[i];
                    length++;
                }
            }
            /* 将char数组中的值转成一个实际的十进制数组 */
            int EvenLength = (length % 2 == 0) ? length : length + 1;
            if (EvenLength != 0) {
                int[] data = new int[EvenLength];
                data[EvenLength - 1] = 0;
                for (int i = 0; i < length; i++) {
                    if (NewArray[i] >= '0' && NewArray[i] <= '9') {
                        data[i] = NewArray[i] - '0';
                    } else if (NewArray[i] >= 'a' && NewArray[i] <= 'f') {
                        data[i] = NewArray[i] - 'a' + 10;
                    } else if (NewArray[i] >= 'A' && NewArray[i] <= 'F') {
                        data[i] = NewArray[i] - 'A' + 10;
                    }
                }
                /* 将 每个char的值每两个组成一个16进制数据 */
                byte[] byteArray = new byte[EvenLength / 2];
                for (int i = 0; i < EvenLength / 2; i++) {
                    byteArray[i] = (byte) (data[i * 2] * 16 + data[i * 2 + 1]);
                }
                return byteArray;
            }
        }
        return new byte[] {};
    }

    /**
     * bytes转int
     *
     * @param bytes
     * @return int
     */
    public static int bytes2int(byte[] bytes) {
        int intData = 0;
        int len = bytes.length <= 4 ? bytes.length : 4;
        for (int i = 0; i < len; i++) {
            intData <<= 8;
            intData |= (bytes[i] & 0xff);
        }
        return intData;
    }
    /**
     * bytes转long
     *
     * @param bytes 数组从左到右，依次是高位到低位
     * @return long
     */
    public static long bytes2long(byte[] bytes) {
        long longData = 0;
        int len = bytes.length <= 8 ? bytes.length : 8;
        for (int i = 0; i < len; i++) {
            longData <<= 8;
            longData |= (bytes[i] & 0xff);
        }
        return longData;
    }
    /**
     * bytes转String
     *
     * @param bytes
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String bytes2string(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        String newStr = null;
        try {
            newStr = new String(bytes, GB18030).trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return newStr;
    }

    /**
     * int转16进制
     *
     * @param i
     * @return
     */
    public static String int2Hex(int i) {
        String hex;
        byte[] bytes = int2bytes(i);
        hex = bytes2Hex(bytes);
        return hex;
    }

    /**
     * long转16进制
     *
     * @param l
     * @return
     */
    public static String long2Hex(long l) {
        String hex;
        byte[] bytes = long2bytes(l);
        hex = bytes2Hex(bytes);
        return hex;
    }

    /**
     * long转byte[],数组从左到右，数组从左到右，高位到低位
     *
     * @param longData
     * @return byte[]:byteData
     */
    public static byte[] long2bytes(long longData) {
        byte[] byteData = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = i * 8;
            byteData[i] = (byte) (longData >> offset);
        }
        reversalBytes(byteData);
        return byteData;
    }

    /**
     * int转byte[],数组从左到右，数组从左到右，高位到低位
     *
     * @param intData
     * @return byte[]:byteData
     */
    public static byte[] int2bytes(int intData) {
        byte[] byteData = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = i * 8;
            byteData[i] = (byte) (intData >> offset);
        }
        reversalBytes(byteData);
        return byteData;
    }

    /**
     * 翻转byte数组
     *
     * @param bytes
     */
    public static void reversalBytes(byte[] bytes) {
        byte b = 0;
        int length = bytes.length;
        int half = length / 2;
        for (int i = 0; i < half; i++) {
            b = bytes[i];
            bytes[i] = bytes[length - 1 - i];
            bytes[length - 1 - i] = b;
        }
    }
    /**
     * 将char转换为HexByte
     *
     * @param chr
     * @return
     */
    public static byte char2HexByte(char chr) {
        byte chrRet = -1;
        if (chr >= '0' && chr <= '9') {
            chrRet = (byte) chr;
        } else if (chr >= 'A' && chr <= 'F') {
            chrRet = (byte) (chr - 65 + 10);
        } else if (chr >= 'a' && chr <= 'f') {
            chrRet = (byte) (chr - 97 + 10);
        }
        return chrRet;
    }

    /**
     * 截取对应长度--不足前面补0
     * @param value
     * @param len
     * @return
     */
    public static String toLength(String value,int len){
        String str = value;
        if (str.length() > len){
            return str.substring(str.length()-len);
        }else {
            while (str.length() != len){
                str = "0"+str;
            }
        }
        return str;
    }


    /**
     * 加密算法
     *
     * @param password 密码
     * @return 加密密码
     */
    public static String encryption(String password) {
        //随机产生6个字节位加密因子
        Date d = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyMMddhhmmss");
        String jmiy = sd.format(d);//加密因子
        byte[] key = password.getBytes();//原密码字节
        byte[] A = DataUtil.hex2bytes(jmiy);//加密因子字节
        byte[] Xkey = new byte[6];
        //利用加密因子对key[]逐字节一一进行异或运算
        for (int i = 0; i < 6; i++) {
            Xkey[i] = (byte) (key[i] ^ A[i]);
        }
        //再将每个字节的高4位和低四位进行交换， 得到新密文Xkey[K6,K5,K4,K3,K2,K1].
        for (int i = 0; i < 6; i++) {
            //高四位
            int h4 = (Xkey[i] << 4) & 0xF0;
            //低四位
            int l4 = (Xkey[i] >> 4) & 0x0F;
            Xkey[i] = (byte) (l4 + h4);
        }
        //对加密因子A[]进行取反, 得到新的加密因子数组XA[A6,A5,A4,A3,A2,A1]。
        byte[] XA = new byte[6];
        for (int i = 0; i < 6; i++) {
            XA[i] = (byte) (~A[i]);
        }
        //将密码和加密因子按照如下规则排列,得到新的总体密文 SecretData[  K6,A1,  K5,A2,  K4,A3, K3,A4,  K2,A5  K1,A6 ]
        byte[] SecretData = new byte[12];
        for (int i = 0; i < 6; i++) {
            SecretData[i * 2] = Xkey[i];
            SecretData[i * 2 + 1] = XA[5 - i];
        }
        return DataUtil.bytes2Hex(SecretData);
    }

    /**
     * 4.0获取校验和
     * @param strProtocol
     * @return
     */
    public static final String getCheckCode(String strProtocol) {
        String strRet = "";
        strProtocol.replace((CharSequence) " ", (CharSequence) "");
        byte chrCheckCode = 0;
        for (int i = 0; i < strProtocol.length(); i += 2) {
            char chrTmp = '\u0000';
            chrTmp = strProtocol.charAt(i);
            if (chrTmp == ' ') continue;
            byte chTmp1 = (byte) (char2HexByte(chrTmp) << 4);
            chrTmp = strProtocol.charAt(i + 1);
            byte chTmp2 = (byte) (chTmp1 + (char2HexByte(chrTmp) & 15));
            chrCheckCode = i == 0 ? chTmp2 : (byte) (chrCheckCode ^ chTmp2);
        }
        String strHexCheckCode = String.format("%x", Byte.valueOf(chrCheckCode));
        if ((strHexCheckCode = strHexCheckCode.toUpperCase()).length() != 2) {
            if (strHexCheckCode.length() > 2) {
                strHexCheckCode = strHexCheckCode.substring(strHexCheckCode.length() - 2);
            } else if (strHexCheckCode.length() < 2 && strHexCheckCode.length() > 0) {
                strHexCheckCode = "0" + strHexCheckCode;
            }
        }
        strRet = strProtocol + strHexCheckCode;
        return strRet;
    }
    /**
     * 获取检验和
     *
     * @param strProtocol
     * @return
     */
    public static String getCheckCode0007(String strProtocol) {
        strProtocol.replace(" ",  "");
        byte chrCheckCode = 0;
        for (int i = 0; i < strProtocol.length(); i += 2) {
            char chrTmp ;
            chrTmp = strProtocol.charAt(i);
            if (chrTmp == ' ') continue;
            byte chTmp1 = (byte) (char2HexByte(chrTmp) << 4);
            chrTmp = strProtocol.charAt(i + 1);
            byte chTmp2 = (byte) (chTmp1 + (char2HexByte(chrTmp) & 15));
            chrCheckCode = i == 0 ? chTmp2 : (byte) (chrCheckCode ^ chTmp2);
        }
        String strHexCheckCode = String.format("%x", Byte.valueOf(chrCheckCode));
        if ((strHexCheckCode = strHexCheckCode.toUpperCase()).length() != 2) {
            if (strHexCheckCode.length() > 2) {
                strHexCheckCode = strHexCheckCode.substring(strHexCheckCode.length() - 2);
            } else if (strHexCheckCode.length() < 2 && strHexCheckCode.length() > 0) {
                strHexCheckCode = "0" + strHexCheckCode;
            }
        }
        return strHexCheckCode;
    }

    // 直接按16进制异或
    public static String var_code_hex(String content) {
//        Log.e("var_code_hex", "计算校验和的内容是: " + content);
        int a = 0;
        for (int i = 0; i < content.length()/2; i++) {
            a = a ^ Integer.parseInt(content.substring(i*2,(i*2)+2), 16);
        }
        String result = Integer.toHexString(a).toUpperCase();
        if (result.length()==1) {
            return "0"+result;
        }else{
            return result;
        }
    }

    /**
     * 解析时间 从2000年开始
     * @param hex 12位，每2位代表 年 月 日 时 分 秒
     * @return
     */
    public static String parsingDate(String hex){
        // yyyy-MM-dd HH:mm:ss
        int yy = DataUtil.hex2Int(hex.substring(0,2));
        int MM = DataUtil.hex2Int(hex.substring(2,4));
        int dd = DataUtil.hex2Int(hex.substring(4,6));
        int HH = DataUtil.hex2Int(hex.substring(6,8));
        int mm = DataUtil.hex2Int(hex.substring(8,10));
        int ss = DataUtil.hex2Int(hex.substring(10,12));
        String itmer = String.valueOf(2000+yy) + "-" + MM + "-" + dd + " " + HH +":"+mm+":"+ss;
        return itmer;
    }
    /**
     * 解析经/纬度
     * @param hex 8位，每2位代表 度 分 秒 小秒
     * @return
     */
    public static double parsingLnt(String hex){
        int d = DataUtil.hex2Int(hex.substring(0,2));
        int m = DataUtil.hex2Int(hex.substring(2,4));
        int s = DataUtil.hex2Int(hex.substring(4,6));
        int ds = DataUtil.hex2Int(hex.substring(6,8));
        String ss = String.format("%d.%d",s,ds);
        double lnt = d + (m/60.0)+(Double.valueOf(ss)/3600.0);
        return lnt;
    }


    /**
     * DP转换PX
     */

    public static int dp_to_px(Context ctx, float dp){
        // 获取屏幕的设备密度
        float density = ctx.getResources().getDisplayMetrics().density;
        // 利用公式将 dp 换算成 px，并且四舍五入
        int px = (int)(dp * density + 0.5f);
        return px;

    }

    public static float px_to_dp(Context ctx, int px){
        // 获取屏幕的设备密度
        float density = ctx.getResources().getDisplayMetrics().density;
        // 利用公式将 dp 换算成 px，并且四舍五入
        float dp = (float) px / density;
        return dp;

    }

    /**
     * 照片转byte二进制
     * @param imagepath 需要转byte的照片路径
     * @return 已经转成的byte
     * @throws Exception
     */
    public static byte[] readStream(String imagepath) throws Exception {
        FileInputStream fs = new FileInputStream(imagepath);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while (-1 != (len = fs.read(buffer))) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        fs.close();
        return outStream.toByteArray();
    }

    // 将字节数组按照指定的大小拆分成一个数组列表
    public static List<byte[]> splitPackage(byte[] src, int size) {
        List<byte[]> list = new ArrayList<>();
        int loop = (src.length + size - 1) / size;

        for (int i = 0; i < loop; i++) {
            int from = i * size;
            int to = Math.min(from + size, src.length);
            byte[] chunk = new byte[to - from];
            System.arraycopy(src, from, chunk, 0, to - from);
            list.add(chunk);
        }

        return list;
    }




}
