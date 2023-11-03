package com.example.chatlxt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.chatlxt.Base.BaseActivity;
import com.example.chatlxt.Fragment.SingleSessionFragment;
import com.example.chatlxt.View.BottomBar;
import com.example.chatlxt.databinding.ActivityMainBinding;
import com.example.chatlxt.databinding.FragmentSingleBinding;

public class MainActivity extends BaseActivity {

    ActivityMainBinding viewBinding;
    SingleSessionFragment singleSessionFragment;

    @Override
    protected Object setLayout() {
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }

    @Override
    protected void beforeSetLayout() {
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        singleSessionFragment = new SingleSessionFragment();
        viewBinding.bottomBar.setContainer(R.id.fragment)  // 设置容器控件
                .setOrientation(BottomBar.HORIZONTAL)  // 设置方向
                .setFirstChecked(0)  // 设置首选项
                .setTitleBeforeAndAfterColor("#7f7f7f", "#00BFFF")  // 设置标题选中和未选中的颜色
                .addItem(singleSessionFragment,"单次会话",R.mipmap.single_icon1, R.mipmap.single_icon2)  // 添加页面: fragment对象,标题名称,选中前图标,选中后图标
                .addItem(singleSessionFragment,"持续会话",R.mipmap.keep_icon1, R.mipmap.keep_icon2)  // 添加页面
                .buildWithEntity();  // 构建
    }

    @Override
    protected void initData() {

    }

    private long backPressedTime = 0;
    private long backPressInterval = 2000;
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - backPressedTime < backPressInterval){
            super.onBackPressed();
            //返回键返回桌面
//            Intent home = new Intent(Intent.ACTION_MAIN);
//            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            home.addCategory(Intent.CATEGORY_HOME);
//            startActivity(home);
        }else {
            application.showToast("再次点击返回按键返回桌面",0);
            backPressedTime = currentTime;
        }
    }
}