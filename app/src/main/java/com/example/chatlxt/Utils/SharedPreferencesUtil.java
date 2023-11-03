package com.example.chatlxt.Utils;

import android.content.Context;
import android.content.SharedPreferences;


// SharedPreferencesUtil 应用配置文件（全局变量）工具
public class SharedPreferencesUtil {

    Context my_context;
    // 单例 -------------------------
    private static SharedPreferencesUtil sharedPreferencesUtil;
    public static SharedPreferencesUtil getInstance() {
        if(sharedPreferencesUtil == null){
            sharedPreferencesUtil = new SharedPreferencesUtil();
        }
        return sharedPreferencesUtil;
    }



    public void initContext(Context context){
        sharedPreferencesUtil.my_context = context;
    }


    // 从 sharedpreference 文件获取 boolean 类型数据方法
    public boolean getBoolean(String key,boolean defValue){
        SharedPreferences sp = my_context.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sp.getBoolean(key,defValue);
    }

    // 从 sharedpreference 文件设置 boolean 类型数据方法
    public void setBoolean(String key,boolean Value){
        SharedPreferences sp = my_context.getSharedPreferences("config",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,Value).commit();
    }

    // 从 sharedpreference 文件获取 string 类型数据方法
    public String getString(String key,String defValue){
        SharedPreferences sp = my_context.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sp.getString(key,defValue);
    }


    // 从 sharedpreference 文件设置 string 类型数据方法
    public void setString(String key,String Value){
        SharedPreferences sp = my_context.getSharedPreferences("config",Context.MODE_PRIVATE);
        sp.edit().putString(key,Value).commit();
    }


    // 从 sharedpreference 文件获取 int 类型数据方法
    public int getInt(String key,int defValue){
        SharedPreferences sp = my_context.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sp.getInt(key,defValue);
    }


    // 从 sharedpreference 文件设置 int 类型数据方法
    public void setInt(String key,int Value) {
        SharedPreferences sp = my_context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putInt(key, Value).commit();
    }


}
