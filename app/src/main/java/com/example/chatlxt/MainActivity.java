package com.example.chatlxt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.chatlxt.Base.BaseActivity;
import com.example.chatlxt.Dao.ChatDao;
import com.example.chatlxt.Entity.DaoBean.Chat;
import com.example.chatlxt.Fragment.KeepSessionFragment;
import com.example.chatlxt.Fragment.SingleSessionFragment;
import com.example.chatlxt.Global.Variable;
import com.example.chatlxt.Utils.DaoUtil;
import com.example.chatlxt.View.BottomBar;
import com.example.chatlxt.databinding.ActivityMainBinding;
import com.example.chatlxt.databinding.FragmentSingleBinding;

import java.util.List;

public class MainActivity extends BaseActivity {

    ActivityMainBinding viewBinding;
    SingleSessionFragment singleSessionFragment;
    KeepSessionFragment keepSessionFragment;

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
        // 如果没有单次聊天数据库就创建
        List<Chat> chats = DaoUtil.getChatBuilder().where(ChatDao.Properties.Id.eq(0)).list();
        if(chats==null || chats.isEmpty()){
            Chat singleChat = new Chat();
            singleChat.setId(0L);
            DaoUtil.getInstance().getDaoSession().insertOrReplace(singleChat);
            Variable.nowChat = singleChat;
        }else {
            Variable.nowChat = chats.get(0);
            loge("单次会话 id:"+chats.get(0).getId());
        }
        init_fragment();
    }

    @Override protected void initData() {}

    private void init_fragment(){
        singleSessionFragment = new SingleSessionFragment();
        keepSessionFragment = new KeepSessionFragment();
        viewBinding.bottomBar.setContainer(R.id.fragment)  // 设置容器控件
                .setOrientation(BottomBar.HORIZONTAL)  // 设置方向
                .setFirstChecked(0)  // 设置首选项
                .setTitleBeforeAndAfterColor("#7f7f7f", "#00BFFF")  // 设置标题选中和未选中的颜色
                .addItem(singleSessionFragment,"单次会话",R.mipmap.single_icon1, R.mipmap.single_icon2)  // 添加页面: fragment对象,标题名称,选中前图标,选中后图标
                .addItem(keepSessionFragment,"持续会话",R.mipmap.keep_icon1, R.mipmap.keep_icon2)  // 添加页面
                .buildWithEntity();  // 构建

        viewBinding.bottomBar.setOnSwitchListener(new BottomBar.OnSwitchListener() {
            @Override
            public void result(@Nullable Fragment currentFragment) {
                application.hideKeyboard();
            }
        });
    }

    private long backPressedTime = 0;
    private long backPressInterval = 2000;
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - backPressedTime < backPressInterval){
            super.onBackPressed();
        }else {
            application.showToast("再次点击返回按键返回桌面",0);
            backPressedTime = currentTime;
        }
    }
}