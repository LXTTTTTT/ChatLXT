package com.example.chatlxt.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.chatlxt.Base.BaseActivity;
import com.example.chatlxt.Base.MainApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 权限管理工具
public class PermissionUtil {

    private static final String TAG = "PermissionUtil";
    public static final Map<Integer, String> permissionDisplayNameMap = new HashMap<>();  // 权限提示列表
    public static final Map<Integer, String> permissionsMap = new HashMap<>();  // 权限列表
    public static final int PERMISSION_REQUEST_ALL = 999;  // 请求所有权限

// 要新增权限的位置：这里、下面的 static、最后的 isPermissionGranted ---------------------------------------------
    public static final int PERMISSION_REQUEST_CODE_CAMERA = 0x001;  // 相机
    public static final int PERMISSION_REQUEST_CODE_RECORD_AUDIO = 0x003;  // 录音
    public static final int PERMISSION_REQUEST_CODE_READ_PHONE_STATE = 0x004;  // 读取设备状态
    public static final int PERMISSION_REQUEST_CODE_MANAGE_EXTERNAL_STORAGE = 0x005;  // 存储权限
    public static final int PERMISSION_REQUEST_CODE_CALL_PHONE = 0x009;  // 电话
    // 位置权限
    public static final int PERMISSION_REQUEST_CODE_ACCESS_COARSE_LOCATION = 0x006;  // 定位
    public static final int PERMISSION_REQUEST_CODE_ACCESS_FINE_LOCATION = 0x007;  // 定位
    public static final int PERMISSION_REQUEST_CODE_ACCESS_BACKGROUND_LOCATION = 0x008;  // 后台定位
    // 文件权限
    public static final int PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE = 0x011;  // 读取文件
    public static final int PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 0x012;  // 写入文件
    // 联系人权限
    public static final int PERMISSION_REQUEST_CODE_READ_CONTACTS = 0x013;  // 读取文件
    public static final int PERMISSION_REQUEST_CODE_WRITE_CONTACTS = 0x014;  // 写入文件
    // 蓝牙权限
    public static final int PERMISSION_REQUEST_CODE_BLUETOOTH = 0x015;
    public static final int PERMISSION_REQUEST_CODE_BLUETOOTH_CONNECT = 0x016;
    public static final int PERMISSION_REQUEST_CODE_BLUETOOTH_SCAN = 0x017;
    public static final int PERMISSION_REQUEST_CODE_BLUETOOTH_ADMIN = 0x018;
    public static final int PERMISSION_REQUEST_CODE_BLUETOOTH_PRIVILEGED = 0x019;

    public static final int PERMISSION_REQUEST_CODE_FOREGROUND_SERVICE = 0x020;  // 前台服务：保持后台活跃
    public static final int PERMISSION_REQUEST_CODE_SET_ALARM = 0x021;  // 设置闹钟
    public static final int PERMISSION_REQUEST_CODE_VIBRATE = 0x022;  // 振动
    public static final int PERMISSION_REQUEST_CODE_INSTALL = 0x023;  // 安装程序

    static {
        Resources resources = MainApplication.getInstance().getResources();
        permissionDisplayNameMap.put(0, "请给予相关权限，否则APP将不能正常运行");
        permissionDisplayNameMap.put(PERMISSION_REQUEST_CODE_CAMERA, "相机会在您上传图片的时候用到");
        permissionDisplayNameMap.put(PERMISSION_REQUEST_CODE_MANAGE_EXTERNAL_STORAGE, "存储权限是为了保存您的登录信息");
        permissionDisplayNameMap.put(PERMISSION_REQUEST_CODE_READ_PHONE_STATE, "读取设备权限是为了更好的适配您的手机");
        permissionDisplayNameMap.put(PERMISSION_REQUEST_CODE_ACCESS_COARSE_LOCATION, "读取位置信息是为了让您有更好的体验");
        permissionDisplayNameMap.put(PERMISSION_REQUEST_CODE_ACCESS_FINE_LOCATION, "读取位置信息是为了让您有更好的体验");
        permissionDisplayNameMap.put(PERMISSION_REQUEST_CODE_ACCESS_BACKGROUND_LOCATION, "读取位置信息是为了让您有更好的体验");
        permissionDisplayNameMap.put(PERMISSION_REQUEST_CODE_CALL_PHONE, "打电话权限是为了更好的体验");
        permissionDisplayNameMap.put(PERMISSION_REQUEST_CODE_RECORD_AUDIO, "录音权限是为了更好的体验");
        permissionDisplayNameMap.put(PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE, "读取存储权限是为了更好的体验");
        permissionDisplayNameMap.put(PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE, "写入存储是为了更好的体验");
        permissionDisplayNameMap.put(PERMISSION_REQUEST_CODE_READ_CONTACTS, "-");
        permissionDisplayNameMap.put(PERMISSION_REQUEST_CODE_WRITE_CONTACTS, "-");
        permissionDisplayNameMap.put(PERMISSION_REQUEST_CODE_BLUETOOTH, "-");
        permissionDisplayNameMap.put(PERMISSION_REQUEST_CODE_BLUETOOTH_CONNECT, "-");
        permissionDisplayNameMap.put(PERMISSION_REQUEST_CODE_BLUETOOTH_SCAN, "-");
        permissionDisplayNameMap.put(PERMISSION_REQUEST_CODE_BLUETOOTH_ADMIN, "-");
        permissionDisplayNameMap.put(PERMISSION_REQUEST_CODE_BLUETOOTH_PRIVILEGED, "-");
        permissionDisplayNameMap.put(PERMISSION_REQUEST_CODE_FOREGROUND_SERVICE, "-");
        permissionDisplayNameMap.put(PERMISSION_REQUEST_CODE_SET_ALARM, "-");
        permissionDisplayNameMap.put(PERMISSION_REQUEST_CODE_VIBRATE, "-");

        permissionsMap.put(PERMISSION_REQUEST_CODE_CAMERA, Manifest.permission.CAMERA);  // 相机
        permissionsMap.put(PERMISSION_REQUEST_CODE_READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE);  // 读取设备状态
        permissionsMap.put(PERMISSION_REQUEST_CODE_MANAGE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE);  // 文件存储
        permissionsMap.put(PERMISSION_REQUEST_CODE_ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);  // 定位
        permissionsMap.put(PERMISSION_REQUEST_CODE_ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);  // 定位
        permissionsMap.put(PERMISSION_REQUEST_CODE_ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION);  // 定位
        permissionsMap.put(PERMISSION_REQUEST_CODE_CALL_PHONE, Manifest.permission.CALL_PHONE);  // 打电话
        permissionsMap.put(PERMISSION_REQUEST_CODE_RECORD_AUDIO, Manifest.permission.RECORD_AUDIO);  // 录音
        permissionsMap.put(PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);  // 读取文件
        permissionsMap.put(PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);  // 写入文件
        permissionsMap.put(PERMISSION_REQUEST_CODE_READ_CONTACTS, Manifest.permission.READ_CONTACTS);  // 联系人
        permissionsMap.put(PERMISSION_REQUEST_CODE_WRITE_CONTACTS, Manifest.permission.WRITE_CONTACTS);
        permissionsMap.put(PERMISSION_REQUEST_CODE_BLUETOOTH, Manifest.permission.BLUETOOTH);  // 打电话
        permissionsMap.put(PERMISSION_REQUEST_CODE_BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_CONNECT);
        permissionsMap.put(PERMISSION_REQUEST_CODE_BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_SCAN);
        permissionsMap.put(PERMISSION_REQUEST_CODE_BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_ADMIN);
        permissionsMap.put(PERMISSION_REQUEST_CODE_BLUETOOTH_PRIVILEGED, Manifest.permission.BLUETOOTH_PRIVILEGED);
        permissionsMap.put(PERMISSION_REQUEST_CODE_FOREGROUND_SERVICE, Manifest.permission.FOREGROUND_SERVICE);
        permissionsMap.put(PERMISSION_REQUEST_CODE_SET_ALARM, Manifest.permission.SET_ALARM);
        permissionsMap.put(PERMISSION_REQUEST_CODE_VIBRATE, Manifest.permission.VIBRATE);

    }


    // 使用requestCode码查看权限获取状态
    public static boolean isPermissionGranted(Activity activity, int requestCode) {
        // 高于 23 版本
        if (Build.VERSION.SDK_INT >= 23) {
            boolean ans = false;
            switch (requestCode) {
                case PERMISSION_REQUEST_CODE_CAMERA:
                    // 判断是否拥有对应的权限，返回 true / false
                    ans = (activity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
                    return ans;
                case PERMISSION_REQUEST_CODE_READ_PHONE_STATE:
                    ans = (activity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);
                    return ans;
                case PERMISSION_REQUEST_CODE_MANAGE_EXTERNAL_STORAGE:
                    ans = (activity.checkSelfPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                    return ans;
                case PERMISSION_REQUEST_CODE_ACCESS_COARSE_LOCATION:
                    ans = (activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
                    return ans;
                case PERMISSION_REQUEST_CODE_ACCESS_FINE_LOCATION:
                    ans = (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
                    return ans;
                case PERMISSION_REQUEST_CODE_ACCESS_BACKGROUND_LOCATION:
                    ans = (activity.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED);
                    return ans;
                case PERMISSION_REQUEST_CODE_CALL_PHONE:
                    ans = (activity.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED);
                    return ans;
                case PERMISSION_REQUEST_CODE_RECORD_AUDIO:
                    ans = (activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);
                    return ans;
                case PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE:
                    ans = (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                    return ans;
                case PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE:
                    ans = (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                    return ans;
                case PERMISSION_REQUEST_CODE_READ_CONTACTS:
                    ans = (activity.checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED);
                    return ans;
                case PERMISSION_REQUEST_CODE_WRITE_CONTACTS:
                    ans = (activity.checkSelfPermission(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED);
                    return ans;
                case PERMISSION_REQUEST_CODE_BLUETOOTH:
                    ans = (activity.checkSelfPermission(Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED);
                    return ans;
                case PERMISSION_REQUEST_CODE_BLUETOOTH_CONNECT:
                    ans = (activity.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED);
                    return ans;
                case PERMISSION_REQUEST_CODE_BLUETOOTH_SCAN:
                    ans = (activity.checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED);
                    return ans;
                case PERMISSION_REQUEST_CODE_BLUETOOTH_ADMIN:
                    ans = (activity.checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED);
                    return ans;
                case PERMISSION_REQUEST_CODE_BLUETOOTH_PRIVILEGED:
                    ans = (activity.checkSelfPermission(Manifest.permission.BLUETOOTH_PRIVILEGED) == PackageManager.PERMISSION_GRANTED);
                    return ans;
                case PERMISSION_REQUEST_CODE_FOREGROUND_SERVICE:
                    ans = (activity.checkSelfPermission(Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED);
                    return ans;
                case PERMISSION_REQUEST_CODE_SET_ALARM:
                    ans = (activity.checkSelfPermission(Manifest.permission.SET_ALARM) == PackageManager.PERMISSION_GRANTED);
                    return ans;
                case PERMISSION_REQUEST_CODE_VIBRATE:
                    ans = (activity.checkSelfPermission(Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED);
                    return ans;

                default:
                    return false;
            }
        } else {
            return true;
        }
    }

    // =======================================================================
    // ************************ 批量权限授予 ***********************************
    // =======================================================================

    // 批量申请权限
    public static void requestPermissionsByRequestCodes(BaseActivity activity, int[] requestCodes, int requestCode) {
        if (null == requestCodes || requestCodes.length < 1) {
            return;
        }
        List<String> requestPermissions = new ArrayList<>();

        for (int i = 0; i < requestCodes.length; i++) {
            if (permissionsMap.containsKey(requestCodes[i]) && !isPermissionGranted(activity, requestCodes[i])) {
                requestPermissions.add(permissionsMap.get(requestCodes[i]));
            }
        }

        if (requestPermissions.size() < 1) {
            return;
        }

        String[] permissions = new String[requestPermissions.size()];
        for (int i = 0; i < requestPermissions.size(); i++) {
            permissions[i] = requestPermissions.get(i);
        }
        Log.e(TAG, "请求多个权限："+ Arrays.toString(permissions));
        // 申请多个权限
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 查看某权限组是否被拒绝过
     *
     * @param activity
     * @param requestCode
     * @return
     */
    public static boolean isPermissionRefused(BaseActivity activity, int requestCode) {
        if (permissionsMap.containsKey(requestCode)) {
            return ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionsMap.get(requestCode));
        }
        return false;
    }


    // ===================================================================
    // ********************* 单组权限授予 **********************************
    // ===================================================================


// 获取手机状态权限
    public static void requestPhoneStatePermission(BaseActivity activity) {
        if (!isPermissionGranted(activity, PERMISSION_REQUEST_CODE_READ_PHONE_STATE)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_PHONE_STATE)) {
                // 这个权限被用户拒绝过
                Toast.makeText(activity,"请给予设备信息权限APP的部分功能才可以正常使用",Toast.LENGTH_SHORT);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE_READ_PHONE_STATE);
            }
        }
    }

    // 检查应用是否具有发送通知的权限，传入对应的id
    public static boolean hasNotificationPermission(String channel_id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) MainApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = notificationManager.getNotificationChannel(channel_id); // 替换成你的通知渠道ID
            return channel != null;
        }
        return true; // 对于低于 Android 8.0 的版本，假设有通知权限
    }



}
