package com.example.chatlxt.Global;

import com.example.chatlxt.Entity.DaoBean.Message;
import com.example.chatlxt.Utils.SharedPreferencesUtil;

// 项目使用的全局变量
public class Variable {

    public static boolean DebugMode = true;  // 调试模式

    public static Message lastestSend;
    public static Message lastestReceive;

    public static boolean isAsking = false;  // 是否正在提问













    public static String getGesturePassword(){
        return SharedPreferencesUtil.getInstance().getString(Constant.GESTURE_PASSWORD,Constant.NONE_PASSWORD);
    };
    public static boolean isConnectBluetooth = false;  // 是否连接 蓝牙
    public static boolean isConnectSerialPort = false;  // 是否连接 串口
    public static boolean isConnectUSBAccessory = false;  // 是否连接 USB附件
    public static boolean isConnectUSB = false;  // 是否连接 USB
    public static boolean isConnectTCPServer = false;  // 是否连接 TCP 服务器
    public static boolean isRecording = false;  // 是否正在录音

    public static String token;  // token
    public static int frequency = 60;  // 当前上报频度
    public static String sos_content = "请求救援";  // 默认的 SOS 内容
    public static String ok_content = "报平安";  // 默认的 OK 内容
    public static boolean need_phone = false;  // 是否需要 拨打电话


    // 当前设备的 经度、纬度、高度、速度 (未转换前)
    public static double gpsLongitude = 0;
    public static double gpsLatitude = 0;
    public static double gpsAltitude = 0;
    public static double speed = 0;

    public static String getAccount(){return SharedPreferencesUtil.getInstance().getString(Constant.ACCOUNT_S, Constant.DEFAULT_ACCOUNT);}
    public static void setAccount(String account){SharedPreferencesUtil.getInstance().setString(Constant.ACCOUNT_S, account);}
    public static String getPassword(){return SharedPreferencesUtil.getInstance().getString(Constant.PASSWORD_S, Constant.DEFAULT_PASSWORD);}
    public static void setPassword(String password){SharedPreferencesUtil.getInstance().setString(Constant.PASSWORD_S, password);}
    public static int systemNumber = SharedPreferencesUtil.getInstance().getInt("system_number",15950044);
    // 更新平台号码
    public static void updata_systemNumber(){
        systemNumber = SharedPreferencesUtil.getInstance().getInt("system_number",15950044);
    }

    public static int getReportFrequency(){
        return SharedPreferencesUtil.getInstance().getInt(Constant.GET_REPORT_FRE,Constant.default_frequency);
    }

    final public static String platformNumber = "110110110";
    final public static int platform_number = 15950044;  // 平台号码

}
