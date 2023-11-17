package com.example.chatlxt.Global;


import android.media.AudioFormat;

// 项目使用的全局常量
public class Constant {

// 全局常量 ---------------------------------------------------------
    public static final String NONE_PASSWORD = "000";  // 未设置密码标识
    public static final String DEFAULT_ACCOUNT = "admin";  // 默认账号
    public static final String DEFAULT_PASSWORD = "test12346";  // 默认密码

    public static int default_frequency = 60;
    public static final int SEND_MSG_SUCCESS = 1;  // 发送成功
    public static final int SEND_MSG_FAIL = 2;  // 发送失败
    public static final int SEND_MSG_PROCESS= 3;  // 发送中
    public static final int SEND_MSG_WAIT_LOAD= 4;  // 等待发送
    public static final int SEND_MSG_CANCEL= 5;  // 已取消发送

    public static final int SEND_MSG_TEXT = 1;
    public static final int SEND_MSG_VOICE = 2;
    public static final int SEND_MSG_PIC = 3;
    public static final int SEND_MSG_SOS = 4;
    public static final int SEND_MSG_SAFETY = 5;
    public static final int RECEIVE_MSG_TEXT = 6;
    public static final int RECEIVE_MSG_VOICE = 7;
    public static final int RECEIVE_MSG_PIC = 8;
    public static final int RECEIVE_MSG_SOS = 9;
    public static final int RECEIVE_MSG_SAFETY = 10;

    public static final int TEXT = 1;  // 文本消息
    public static final int VOICE = 2;  // 语音消息
    public static final int PICTURE = 3;  // 语音消息

// NotificationCenter 常量 -----------------------------------
    public static final int UPDATA_NOTE = 6660;  // 更新笔记数据库
    public static final int CONNECT_SERIAL_PORT = 6661;  // 连接串口
    public static final int DISCONNECT_SERIAL_PORT = 6662;  // 断开连接串口
    public static final int CONNECT_USB_ACCESSORY = 6663;  // 连接USB附件
    public static final int DISCONNECT_USB_ACCESSORY = 6664;  // 连接USB附件
    public static final int CONNECT_USB = 6665;  // 连接USB
    public static final int DISCONNECT_USB = 6666;  // 连接USB
    public static final int RECEIVE_MESSAGE = 6667;  // 接收消息

// SharedPreferences 名称 ----------------------------------
    public static final String GESTURE_PASSWORD = "gesture_password";  // 手势密码
    public static final String LOGIN_TOKEN = "login_token";  // token
    public static final String SERVICE_OPERATION = "service_operation";  // 服务指令标识
    public static final String INIT_COMMAND = "init_command";  // 下发初始化指令
    public static final String SEND_COMMAND = "send_command";  // 下发消息指令
    public static final String DISCONNECT_COMMAND = "disconnect_command";  // 断开连接指令
    public static final String SEND_DATA = "send_data";  // 下发的消息包裹标识
    public static final String CONNECT_BLUETOOTH = "connect_bluetooth";  // 连接蓝牙标识
    public static final String DISCONNECT_BLUETOOTH = "disconnect_bluetooth";  // 断开蓝牙标识
    public static final String BLUETOOTH_DEVICE = "bluetooth_device";  // 下发的消息包裹标识
    public static final String ACCOUNT_S = "default_account";  // 最后设置的SN码
    public static final String PASSWORD_S = "default_password";  // 最后设置的SN码
    public static final String GET_REPORT_FRE = "get_report_fre";
    public static final String DISCONNECT_WEBSOCKET = "disconnect_websocket";  // 断开websocket

// 权限请求码 -----------------------------------
    public static final int REQUEST_RECORD_AUDIO = 700;  // 请求录音权限
    public static final int REQUEST_CAMERA = 701;  // 请求相机权限

// 页面响应码 -----------------------------------
    public static final int REQUEST_SCAN = 153;  // 扫描页面返回码

// 通知的通道 -----------------------------------
    public static final String MSG_CHANNEL_ID = "001";  // 消息
    public static final String WS_CHANNEL_ID = "002";  // websocket

// 录音工具参数 ----------------------------------
    public static final int SAMPLE_RATE = 8000;  // 44100 MediaRecoder 的采样率通常是8000Hz AAC的通常是44100Hz。 设置采样率为44100，目前为常用的采样率，官方文档表示这个值可以兼容所有的设置
    // 声道   CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
    public static final int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    public static final int channelCount = channelConfig == AudioFormat.CHANNEL_IN_STEREO ? 2:1;
    // 比特率
    public static final int BIT_RATE = 64;
    // 编码格式
    public static final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    // 读取数据的最大字节数
    public static final int KEY_MAX_INPUT_SIZE =20 * 1024;

// 请求用到的 URL ----------------------------------------------------
    public static final String WEBSOCKET_ADDR = "wss://back.monitor.beidou.online/ws";
//    public static final String BASE_URL = Variable.DebugMode? "http://back.tdwtv2.pg8.ink":"https://back.monitor.beidou.online";
    public static final String BASE_URL = "https://api.openai.com";
    public static final String GPT_CHAT = "/v1/chat/completions";

// ChatGPT ---------------------------------------------------------------
    public static final String GPT_KEY = "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";
    public static final String GPT_USER = "user";
    public static final String GPT_ASSISTANT = "assistant";
    public static final String GPT_SYSTEM = "system";
    public static final String THINKING = "正在思考中... ";
    public static final String FAIL = "发送失败，点击小图标重新发送";
    public static final String CANCEL = "已取消发送，点击小图标重新发送";

    public static final int MESSAGE_SEND = 0;
    public static final int MESSAGE_SUCCESS = 1;
    public static final int MESSAGE_FAIL = 2;
    public static final int MESSAGE_CANCEL = 3;

    public static final int MESSAGE_QUESTION = 0;
    public static final int MESSAGE_ANSWER = 1;
}
