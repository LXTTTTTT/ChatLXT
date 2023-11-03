package com.example.chatlxt.Base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.chatlxt.Base.MainApplication;
import com.example.chatlxt.R;
import com.example.chatlxt.Utils.DataUtil;
import com.example.chatlxt.Utils.NotificationCenter;
import com.example.chatlxt.Utils.PermissionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

//    public String TAG = "unknow_activity";
    public String TAG = getClass().getName();
    public Window window;

    ImageView back;
    TextView title;
    public Context my_context;
    public MainApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        window = getWindow();
        application = MainApplication.getInstance();
        my_context = this;
        beforeSetLayout();  // 某些操作需要在 绑定layout 之前执行
//        if (getSupportActionBar() != null) {getSupportActionBar().hide();}  // 隐藏标题栏
        // 绑定 layout
        if(setLayout() instanceof Integer){
            setContentView((Integer) setLayout());  // 手动绑定 R.layout.id
        } else {
            setContentView((View) setLayout());  // 使用 ViewBinding
        }
        // 去掉状态栏
//        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setStatusBarColor();  // 设置状态栏的颜色

//        setOrientationPortrait();  // 锁定垂直方向

        // 设置返回按键的事件，放在初始化控件之前，防止某些页面要重写返回键的事件
//        try {
//            title = findViewById(R.id.title);
//            back = findViewById(R.id.back);
//            back.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    finish();
//                }
//            });
//        }catch (Exception e){
//            loge("这个页面没有 back 按键");
//        }
        initView(savedInstanceState);  // 初始化控件
        initData();  // 初始化数据
    }

    protected abstract Object setLayout();
    protected abstract void beforeSetLayout();
    protected abstract void initView(Bundle savedInstanceState);
    protected abstract void initData();

    // 设置页面标题
    public void setTitle(String title){this.title.setVisibility(View.VISIBLE);this.title.setText(title);}

    // 设置页面进入时的过渡动画
    public void setEnterTransition(int type){
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        switch (type){
            case 0:
                break;
            case 1:
                window.setEnterTransition(new Explode());
                break;
            case 2:
                window.setEnterTransition(new Slide());
                break;
            case 3:
                window.setEnterTransition(new Fade());
                break;
        }
    }

    // 设置页面消失时的过渡动画
    public void setExitTransition(int type){
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        switch (type){
            case 0:
                break;
            case 1:
                window.setExitTransition(new Explode());
                break;
            case 2:
                window.setExitTransition(new Slide());
                break;
            case 3:
                window.setExitTransition(new Fade());
                break;
        }
    }


    // 设置页面标签
    public void setTAG(String tag){
        TAG = getClass().getName();
//        TAG = tag;
    }

// 打印 loge
    public void loge(String tag){
        Log.e(TAG, tag);
    }

// 申请权限
    protected void initPermissions() {
        List<Integer> list = new ArrayList<>();

        // 定位
        if (!PermissionUtil.isPermissionGranted(this, PermissionUtil.PERMISSION_REQUEST_CODE_ACCESS_COARSE_LOCATION)) {
            list.add(PermissionUtil.PERMISSION_REQUEST_CODE_ACCESS_COARSE_LOCATION);
        }
        if (!PermissionUtil.isPermissionGranted(this, PermissionUtil.PERMISSION_REQUEST_CODE_ACCESS_FINE_LOCATION)) {
            list.add(PermissionUtil.PERMISSION_REQUEST_CODE_ACCESS_FINE_LOCATION);
        }
//        if (!PermissionUtil.isPermissionGranted(this, PermissionUtil.PERMISSION_REQUEST_CODE_ACCESS_BACKGROUND_LOCATION)) {
//            // 后台定位需要手动打开，不能在这里，不然会导致其他权限也不能请求
//            list.add(PermissionUtil.PERMISSION_REQUEST_CODE_ACCESS_BACKGROUND_LOCATION);
//        }
        // 读写手机存储
        if (!PermissionUtil.isPermissionGranted(this, PermissionUtil.PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE)) {
            list.add(PermissionUtil.PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE);
        }
        if (!PermissionUtil.isPermissionGranted(this, PermissionUtil.PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE)) {
            list.add(PermissionUtil.PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        }

        for (Integer integer : list) {
            Log.e(TAG, "没给权限: " + DataUtil.int2Hex(integer) );
        }

        if (list.size() < 1) {
            loge("所有权限都授予了");
        } else {
            int[] permissions = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                // 批量申请权限列表
                permissions[i] = list.get(i);
            }
            // 申请权限
            PermissionUtil.requestPermissionsByRequestCodes(this, permissions, PermissionUtil.PERMISSION_REQUEST_ALL);
        }

    }



// 显示键盘
    public void showKeyboard(final View view) {
        view.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != imm) {
                view.requestFocus();
                imm.showSoftInput(view, 0);
            }
        }, 100);
    }


// 隐藏键盘
    public void hideKeyboard() {
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

// 设置全屏
    protected void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.BLACK);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

// 设置状态栏透明
    public void setStatusBarColor(){
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.gray_3));
    }
// 设置页面垂直
    public void setOrientationPortrait(){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
// 页面跳转
    public void toActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
// 生命周期 -------------------------------------------


    @Override
    protected void onStart() {
        super.onStart();
//        loge("改变 activity：" + this);
        MainApplication.getInstance().now_activity = this;  // 加载时改变当前所在的 activity 对象
    }

    @Override
    protected void onDestroy() {
        try {
            NotificationCenter.standard().removeObserver(this);
        }catch (Exception e){
            loge("这个页面没有用到 NotificationCenter");
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        loge("请求权限码是："+requestCode+" / 权限列表："+ Arrays.toString(permissions) +" / 结果列表："+ Arrays.toString(grantResults));
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 在开启了自动旋转的情况下仍然锁定为垂直
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
