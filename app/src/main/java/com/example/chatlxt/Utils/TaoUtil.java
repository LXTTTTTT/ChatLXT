package com.example.chatlxt.Utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

// 我的工具
public class TaoUtil {

    private static String TAG = "TaoUtil";

    // 将 dp 转化为 px
    public static int convertDpToPixel(Context context, int dp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return Math.round(px);
    }

    // 将 dp 转化为 px(float)
    public static float convertDpToFloatPixel(Context context, float dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    // dp转px
    public static int dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5F);
    }

    // px转dp
    public static int px2dp(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5F);
    }

    // 四舍五入：v - 需要处理的数 ，scale - 保留几位
    public static double round(double v,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    // 判断点击区域是否在这个控件范围内
    public static boolean eventInView(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        Log.d(TAG, "eventInView: location[" + location[0] + ", " + location[1] + "]");
        int left = location[0];
        int top = location[1];
        int right = location[0] + view.getWidth();
        int bottom = location[1] + view.getHeight();
        float x = event.getRawX();
        float y = event.getRawY();
        Log.d(TAG, "eventInView: [" + left + ", " + top + ", " + right + ", " + bottom + "], [" + x + ", " + y + "]");
        return left < right && top < bottom  // check for empty first
                && x >= left && x < right && y >= top && y < bottom;
    }

    // 直接按16进制异或
    public static String var_code_hex(String content) {
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

    // 按 ascii 码异或
    public String var_code(String str){
        byte[] bytes = new byte[0];
        try {
            bytes = str.getBytes("gbk");
        } catch (UnsupportedEncodingException var10) {
            var10.printStackTrace();
        }
        byte ret = CheckByte(bytes, bytes.length);
        String var = castByteToHexString(ret);

        return var;
    }

    public static final byte CheckByte(byte[] parambyte, int length) {
        if (length <= 0) {
            return (Byte)null;
        } else {
            byte bytetemp = parambyte[0];

            for(int i = 1; i < length; ++i) {
                bytetemp ^= parambyte[i];
            }

            return bytetemp;
        }
    }


    public static final String castByteToHexString(byte paramArrayOfByte) {
        String str = null;
        StringBuffer result = new StringBuffer();
        str = Integer.toHexString(paramArrayOfByte & 255 | -256).substring(6);
        result.append(str);
        return result.toString().toUpperCase();
    }

    // MD5加密
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


    // 通过 context 获取 xxx.class
    public static Class<? extends Activity> getClassByContext(Context context){
        if (context instanceof Activity) {
            Class<? extends Activity> activityClass = (Class<? extends Activity>) context.getClass();
            // 现在你可以使用 activityClass 进行其他操作，如反射等
            return activityClass;
        }
        return null;
    }

// 获取文件大小 ------------------------------------
    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值

    // 获取文件指定文件的指定单位的大小 filePath 文件路径 sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"获取文件大小失败!");
        }
        return FormetFileSize(blockSize, sizeType);
    }

    // 调用此方法自动计算指定文件或指定文件夹的大小
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"获取文件大小失败!");
        }
        return FormetFileSize(blockSize);
    }

    // 获取指定文件大小
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e(TAG,"获取文件大小不存在!");
        }
        return size;
    }

    // 获取指定文件夹
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    // 转换文件大小
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    // 转换文件大小,指定转换的类型
    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }


    // 是否点击过快/太快
    private static long lastClickTime;
    public static boolean isFastClick(int MIN_CLICK_DELAY_TIME) {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) <= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    // 四舍五入：round - true四舍五入，false直接截取
    public static double roundDouble(double value, int places, boolean round) {
        if (places < 0) {
            throw new IllegalArgumentException("小数位数不能小于0");
        }

        double scale = Math.pow(10, places);
        if(round){return Math.round(value * scale) / scale;}
        else {return Math.floor(value * scale) / scale;}

    }

    // 获取手机卡信息
    public static String getIMSI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            return telephonyManager.getSubscriberId();
        }
        return null;
    }

    /**
     * 度分秒转经纬度
     * @param dms 116°25'7.85"
     * @return 116.418847
     */
    public static double changeToDu(String dms) {
        if (dms == null) return 0;
        try {
            dms = dms.replace(" ", "");
            String[] str2 = dms.split("°");
            if (str2.length < 2) return 0;
            int d = Integer.parseInt(str2[0]);
            String[] str3 = str2[1].split("\\′");
            if (str3.length < 2) return 0;
            int f = Integer.parseInt(str3[0]);
            String str4 = str3[1].substring(0, str3[1].length() - 1);
            double m = Double.parseDouble(str4);

            double fen = f + (m / 60);
            double du = (fen / 60) + Math.abs(d);
            if (d < 0) du = -du;
            return du;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 将经纬度转换为度分秒格式
     * @param du 116.418847
     * @return 116°25'7.85"
     */
    public static String changeToDFM(double du) {
        int du1 = (int) du;
        double tp = (du - du1) * 60;
        int fen = (int) tp;
        String miao = String.format("%.2f", Math.abs(((tp - fen) * 60)));
        return du1 + "°" + Math.abs(fen) + "'" + miao + "\"";
    }


    // 以动画效果 显示/隐藏 控件
    public static void animateView(View view) {
        int visibility;
        if (view.getVisibility() == View.VISIBLE) {visibility = View.GONE;}
        else {visibility = View.VISIBLE;}
        if (visibility == View.VISIBLE) {
            view.setAlpha(0f); // 初始化透明度为0，将视图设为不可见
            view.setVisibility(View.VISIBLE);
        }
        view.animate()
                .translationY(visibility == View.VISIBLE ? 0 : view.getHeight()) // 设置Y轴平移
                .alpha(visibility == View.VISIBLE ? 1f : 0f) // 设置透明度：出现/消失时的渐变效果
                .setDuration(400) // 动画持续时间
                .setInterpolator(new AccelerateInterpolator()) // 设置插值器
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(visibility);
                    }
                })
                .start();
    }

    // AES加密
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";

    public static String encrypt(String key, String data) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
    }

    // 高位补0
    public String padWithZeros(String input, int desiredLength) {
        int inputLength = input.length();
        if (inputLength >= desiredLength) {
            return input; // 如果输入长度大于等于指定长度，直接返回输入字符串
        }
        int zerosToAdd = desiredLength - inputLength; // 需要补充的0的个数
        StringBuilder paddedString = new StringBuilder();
        // 在前面添加补充的0
        for (int i = 0; i < zerosToAdd; i++) {
            paddedString.append('0');
        }
        paddedString.append(input); // 添加原始字符串
        return paddedString.toString();
    }

    // 校验输入的字符是否ip地址
    public static boolean isValidIP(String ip) {
        String regex = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    // 计算SHA-256哈希值
    public static String calculateSHA256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes());

            // 将哈希值转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 反射测试私有方法
    private void testReflect(){
        Log.e(TAG, "反射私有方法");
    }

}
