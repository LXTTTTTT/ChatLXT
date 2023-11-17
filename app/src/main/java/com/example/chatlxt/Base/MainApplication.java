package com.example.chatlxt.Base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.chatlxt.R;
import com.example.chatlxt.View.CustomDialog;
import com.example.chatlxt.View.WarnDialog;
import com.example.chatlxt.ViewModel.DataViewModel;
import com.example.chatlxt.Global.Variable;
import com.example.chatlxt.HTTP.GPTInterface;
import com.example.chatlxt.Utils.DaoUtil;
import com.example.chatlxt.Utils.RequestUtil;
import com.example.chatlxt.Utils.SharedPreferencesUtil;
import com.example.lxt.Utils.RetrofitUtil;
import com.google.android.material.snackbar.Snackbar;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainApplication extends Application {

// Variable ------------------------------
    public String TAG = "MainApplication";
    public BaseActivity now_activity;  // 程序当前所在的 activity
    private Toast my_toast;  // Toast
    private Snackbar my_snackbar;  //

// Control --------------------------------
    private AlertDialog alertDialog;  // 自定义图像的系统警告框 AlertDialog
    public PopupWindow popupWindow;
    public PopupWindow popupWindow2;
    private CustomDialog customDialog;  // 角色扮演弹窗
    private WarnDialog warnDialog;  // 警告 dialog

// ViewModel ------------------------------
//    public TextViewModel textViewModel;
    public DataViewModel dataViewModel;

// Other ----------------------------------
    public Vibrator vibrator;  // 振动
    public AudioManager audioManager;  // 音频
    public NotificationManager notificationManager;  // 通知
    private TextToSpeech textToSpeech;  // 文字转语音工具
    public ExecutorService threadPool;  // 线程池
    public GPTInterface gptInterface;  // gpt请求工具
    public LayoutInflater layoutInflater;  // 布局填充器
    public InputMethodManager inputMethodManager;  // 软键盘管理

// Instance -------------------------
    private static MainApplication mainApplication;
    public static MainApplication getInstance() {
        return mainApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mainApplication = this;
        // 初始化 内存监测参数
        
        // 初始化 数据库 工具
        DaoUtil.getInstance().init(this);
        // 初始化 SharedPreferences 工具
        SharedPreferencesUtil.getInstance().initContext(this);
        // 设置这个程序中的每个 activity 的生命周期事件（暂不操作）
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
                Log.d(TAG, "onActivityCreated: 打开页面 "+activity.getLocalClassName());
            }
            @Override public void onActivityStarted(@NonNull Activity activity) {}
            @Override public void onActivityResumed(@NonNull Activity activity) {}
            @Override public void onActivityPaused(@NonNull Activity activity) {}
            @Override public void onActivityStopped(@NonNull Activity activity) {}
            @Override public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {}
            @Override public void onActivityDestroyed(@NonNull Activity activity) {
                Log.d(TAG, "onActivityDestroyed: 关闭页面 "+activity.getLocalClassName());
            }
        });
        // 初始化全局使用 ViewModel
        init_ViewModel();
        // 初始化请求工具
        init_request();

        // 初始化线程池
        threadPool = Executors.newCachedThreadPool();  // 执行大量不耗时的任务用这个
//        threadPool = Executors.newFixedThreadPool(10);  // 一般用这个就够了
//        threadPool = Executors.newSingleThreadExecutor();  // 任务量不大的话用这个
//        threadPool = Executors.newScheduledThreadPool(10);  // 有定时任务用这个
        threadPool.execute(new Runnable() {@Override public void run() {Log.e(TAG, "初始化线程池" );}});  // 使用线程池

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void init_request(){
        // 初始化 Retrofit 网络请求工具
        RetrofitUtil.INSTANCE.init();
        gptInterface = RetrofitUtil.INSTANCE.getRetrofit().create(GPTInterface.class);
        RequestUtil.getInstance().init(gptInterface);
    }

// 控件操作 ---------------------------------------------------------------------

    // 全局唯一 Toast 方法：0 - 短 ， 1 - 长
    public void showToast(String msg,int length) {
        if(now_activity==null){return;}
        now_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(my_toast!=null){
                    my_toast.cancel();
                    my_toast = null;
                }
                if(length == 0){
                    my_toast = Toast.makeText(now_activity,msg,Toast.LENGTH_SHORT);
                }else {
                    my_toast = Toast.makeText(now_activity,msg,Toast.LENGTH_LONG);
                }
                my_toast.show();
                Log.e(TAG, "showToast：" + msg);
            }
        });
    }


    public void showPopupMenu(View view, View.OnClickListener... onClickListeners){
        popupWindow = null;
        View customView = layoutInflater.inflate(R.layout.layout_popupmenu, null);
        if(onClickListeners.length>0 && onClickListeners[0]!=null){customView.findViewById(R.id.clean).setOnClickListener(onClickListeners[0]);}
        if(onClickListeners.length>1 && onClickListeners[1]!=null){customView.findViewById(R.id.change).setOnClickListener(onClickListeners[1]);}
        if(onClickListeners.length>2 && onClickListeners[2]!=null){customView.findViewById(R.id.other).setOnClickListener(onClickListeners[2]);}

        popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.PopupMenuAnimation);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        popupWindow.showAtLocation(view, Gravity.START | Gravity.TOP, 0, 0);  // 相对于整个页面显示
        // 关闭软键盘
        hideKeyboard();
        popupWindow.showAsDropDown(view,0,0);
    }

    public void hidePopupMenu(){
        if(popupWindow!=null){popupWindow.dismiss();}
    }

    public void showPopupMenu2(View view, View.OnClickListener... onClickListeners){
        popupWindow2 = null;
        View customView = layoutInflater.inflate(R.layout.layout_popupmenu_2, null);
        if(onClickListeners.length>0 && onClickListeners[0]!=null){customView.findViewById(R.id.clean).setOnClickListener(onClickListeners[0]);}
        if(onClickListeners.length>1 && onClickListeners[1]!=null){customView.findViewById(R.id.add).setOnClickListener(onClickListeners[1]);}

        popupWindow2 = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow2.setAnimationStyle(R.style.PopupMenuAnimation);
        popupWindow2.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 关闭软键盘
        hideKeyboard();
        popupWindow2.showAsDropDown(view,0,0);
    }

    public void hidePopupMenu2(){
        if(popupWindow2!=null){popupWindow2.dismiss();}
    }

    public void showSnackBar(View view,String msg,int length){
        now_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(my_snackbar!=null){
                    my_snackbar.dismiss();
                    my_snackbar = null;
                }
                if(length==0){
                    my_snackbar = Snackbar.make(view,msg,Snackbar.LENGTH_SHORT);
                }else {
                    my_snackbar = Snackbar.make(view,msg,Snackbar.LENGTH_LONG);
                }
                my_snackbar.show();
                Log.e(TAG, "showSnackBar：" + msg);
            }
        });
    }


    // 全局唯一 展示 AlertDialog 方法： target_activity 留空 - 显示在当前 activity
    public void showAlertDialog(Activity target_activity, View view) {
        if (alertDialog == null) {

            if(target_activity != null){
                alertDialog = new AlertDialog.Builder(target_activity)
                        .setView(view)
                        .setTitle(null)
                        .setMessage(null)
                        .setPositiveButton(null,null)
                        .setNegativeButton(null, null)
                        .create();
            }else {
                alertDialog = new AlertDialog.Builder(now_activity)
                        .setView(view)
                        .setTitle(null)
                        .setMessage(null)
                        .setPositiveButton(null,null)
                        .setNegativeButton(null, null)
                        .create();;
            }
        }
        if(alertDialog.isShowing()){
            return;
        }
        alertDialog.show();
    }

    // 隐藏 AlertDialog 方法
    public void hideAlertDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    // 全局唯一 展示 自定义 dialog 方法： target_activity 留空 - 显示在当前 activity，列表用到的数据，保存列表拿到的数据
    public void showCustomDialog(Activity target_activity, String title, String cancel, CustomDialog.OnDialogWork onDialogWork) {
        if (customDialog == null) {
            customDialog = new CustomDialog();
        }
        Log.e(TAG, "弹出 CustomDialog");
        if (customDialog.isAdded()) return;
        customDialog.setData(title,cancel,onDialogWork);
        if(target_activity != null){
            customDialog.show(target_activity.getFragmentManager(), "");
        }else {
            customDialog.show(now_activity.getFragmentManager(), "");
        }
    }
    // 隐藏 警告 dialog 方法
    public void hideCustomDialog() {
        if (customDialog != null) {
            customDialog.dismiss();
        }
    }

    // 全局唯一 展示 警告 dialog 方法： target_activity 留空 - 显示在当前 activity
    public void showWarnDialog(Activity target_activity, String str, View.OnClickListener onYesClickListener) {
        if (warnDialog == null) {
            warnDialog = new WarnDialog();
        }
        if (warnDialog.isAdded()) return;
        Log.e(TAG, "弹出 WarnDialog");
        // 设置警告的 内容 和 点击确定事件
        if(target_activity != null){
            warnDialog.setData(target_activity, str, onYesClickListener);
            warnDialog.show(target_activity.getFragmentManager(), "");
        } else {
            warnDialog.setData(now_activity, str, onYesClickListener);
            warnDialog.show(now_activity.getFragmentManager(), "");
        }
    }

    // 隐藏 警告 dialog 方法
    public void hideWarnDialog() {
        if (warnDialog != null) {
            warnDialog.dismiss();
        }
    }


// 其它操作 -------------------------------------------------------------------

    // 播放消息提示音
    public void ringBell(){
        // 检查设备是否支持振动
        if (vibrator!=null && vibrator.hasVibrator()) {
            // 震动一段时间（以毫秒为单位）
            long[] pattern = {0, 1000, 500, 1000, 500}; // 静止，振动1秒，静止0.5秒，振动1秒，静止0.5秒
            vibrator.vibrate(pattern, -1); // 第二个参数是重复的索引，-1表示不重复
        }
        // 检查当前音频模式是否为正常模式
        if (audioManager!=null && (audioManager.getRingerMode()== AudioManager.RINGER_MODE_NORMAL)) {
            // 播放铃声
            // 你可以在这里添加播放铃声的逻辑
            // 获取默认的消息提示音
            Uri defaultNotificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            // 创建消息提示音的实例
            Ringtone defaultNotificationRingtone = RingtoneManager.getRingtone(getApplicationContext(), defaultNotificationUri);
            // 播放消息提示音
            if (defaultNotificationRingtone != null) {
                defaultNotificationRingtone.play();
            }
        }

    }

    // 获取 gradle 的 versionName
    public float getVersionName(){
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return Float.parseFloat(packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    // 获取 gradle 的 versionName
    public int getVersionCode(){
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 初始化 ViewModel
    public void init_ViewModel(){
//        textViewModel = new ViewModelProvider.AndroidViewModelFactory(this).create(TextViewModel.class);
        dataViewModel = new ViewModelProvider.AndroidViewModelFactory(this).create(DataViewModel.class);
    }

    // 销毁线程池
    public void destroyThreadPool(){
        if(threadPool != null && !threadPool.isShutdown()) {
            threadPool.shutdown();
            threadPool = null;
        }
    }

    // 倒计时x秒后干活，避免内存泄露
    public void doSomethingAfter(int seconds){
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "干活" );
            }
        }, seconds*60*1000L);
    }

    // 显示键盘
    public void showKeyboard(final View view) {
        view.postDelayed(() -> {
            if (null != inputMethodManager) {
                view.requestFocus();
                inputMethodManager.showSoftInput(view, 0);
            }
        }, 50);
    }

    // 隐藏键盘
    public void hideKeyboard(){
        if (now_activity.window.getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (now_activity.getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(now_activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

// 定时执行任务 ------------------------------

    public Timer timer;  // 计时器
    public boolean is_reporting  = false;  // 是否正在上报的状态
    public long countDown = 0;  // 这个是第一次发送的延时，追踪页面的倒计时显示也用

    // 发送极限追踪消息
    public void doSomething() {
        Log.e(TAG, "run: 执行发送SOS" );
        countDown = Variable.frequency;  // 设置第一次上报位置到第二次的等待间隔（和频度一样）
    }

    public void startRecurringTask() {
        is_reporting = true;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // 判断是否开启了 上报
                if (is_reporting) {
                    doSomething();
                }else {
                    // 如果中间被断开的话就关闭定时器
                    timer.cancel();
                    timer = null;
                }
            }
        };
        //从现在起过delay毫秒以后，每隔period毫秒执行一次。
        if (timer == null) {
            timer = new Timer();
            timer.schedule(task, countDown * 1000, Variable.frequency * 1000);
        } else {
            // 如果在上报的过程中再次调用的开始指令就执行这个
            timer.cancel();
            timer = null;
            timer = new Timer();
            Log.e(TAG, "修改上报中的频度: " + countDown + "//"+Variable.frequency);
            timer.schedule(task, countDown * 1000, Variable.frequency * 1000);
        }
    }

    public void stopRecurringTask(){
        if(timer != null){

            is_reporting = false;
            countDown = 0;

            timer.cancel();
            timer = null;
        }
    }




// 队列 -----------------------------------------------

// 生命周期 --------------------------------------------

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
