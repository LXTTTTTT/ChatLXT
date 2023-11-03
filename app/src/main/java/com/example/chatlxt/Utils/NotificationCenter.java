package com.example.chatlxt.Utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * - @Description:  观察者模式NotificationCenter
 * - @Author:  Ken
 */
public class NotificationCenter {
    // 观察者管理对象
    final private HashMap<Integer, ArrayList<Object>> observers = new HashMap<>();
    // 删除缓冲区，避免通知管理对象在线程锁中使用的时候无法删除操作
    final private HashMap<Integer, Object> removeAfterBroadcast = new HashMap<>();
    // 添加缓冲区，避免通知管理对象在线程锁中使用的时候无法添加操作
    final private HashMap<Integer, Object> addAfterBroadcast = new HashMap<>();
    // 用于判断这个observers 对象是否在线程锁中 正在使用
    private boolean broadcastion = false;
    // 如果么有输入id，证明需要删除所有关于某个对象的监听通知
    final private static Integer nullId = -999999;
    private static volatile NotificationCenter instance = null;
    /**
     * 单例
     * @return
     */
    public static NotificationCenter standard() {
        if (instance == null) {
            synchronized (NotificationCenter.class) {
                if (instance == null) {
                    instance = new NotificationCenter();
                }
            }
        }
        return instance;
    }
    /**
     * 发送通知
     */
    public void postNotification(final int id, final Object... args) {
        synchronized (observers){
            broadcastion = true ;

            ArrayList<Object> objects = observers.get(id);
            if (objects != null) {
                for (final Object obj : objects) {
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            //已在主线程中，可以更新UI
                            ((NotificationCenterDelegate)obj).didReceivedNotification(id,args);
                        }
                    });
                }
            }
            broadcastion = false;

            if (!removeAfterBroadcast.isEmpty()) {
                for (HashMap.Entry<Integer, Object> entry: removeAfterBroadcast.entrySet()){
                    if (entry.getKey() == nullId) {
                        removeObserver(entry.getValue());
                    } else {
                        removeObserver(entry.getValue(), entry.getKey());
                    }
                }
                removeAfterBroadcast.clear();
            }

            if (!addAfterBroadcast.isEmpty()){
                for (HashMap.Entry<Integer, Object> entry: addAfterBroadcast.entrySet()){
                    addveObserver((NotificationCenterDelegate)entry.getValue(), entry.getKey());
                }
                addAfterBroadcast.clear();
            }
        }
    }
    /**
     * 添加观察者
     **/
    public void addveObserver(NotificationCenterDelegate observer, Integer id) {
        synchronized (observers) {   // HashMap<Integer, ArrayList<Object>>
            if (broadcastion) {
                addAfterBroadcast.put(id, observer);
                return;
            }

            ArrayList<Object> objects = observers.get(id);
            if (objects == null) {
                observers.put(id, (objects = new ArrayList<>()));
            }
            if (!objects.contains(observer)){
                objects.add(observer);
            }
        }
    }

    /**
     * 删除某个观察者事件
     * @param observer 监听对象
     * @param id  监听的key
     */
    public void removeObserver(Object observer, Integer id) {
        synchronized (observers) {
            if (broadcastion){
                removeAfterBroadcast.put(id, observer);
                return;
            }
            ArrayList<Object> objects = observers.get(id);
            if (objects != null){
                objects.remove(observer);
                if (objects.size() == 0){
                    observers.remove(id);
                }
            }
        }
    }

    /**
     * 删除观察者
     * @param observer 监听对象
     */
    public void removeObserver(Object observer) {
        synchronized (observers) {
            if (broadcastion){
                removeAfterBroadcast.put(nullId, observer);
                return;
            }
            for (Iterator<Map.Entry<Integer, ArrayList<Object>>> it = observers.entrySet().iterator(); it.hasNext();) {
                Map.Entry<Integer, ArrayList<Object>> items = it.next();
                ArrayList<Object> objects =  items.getValue();
                for (int i= 0 ;i<objects.size();i++){
                    Object obj = objects.get(i);
                    if (obj == null){
                        Log.e("removeObserver obj","null obj");
                    }
                    if (obj == observer){
                        objects.remove(observer);
                        if (objects.size() == 0){
                            it.remove();
                        }
                    }
                }
            }
        }
    }


    public interface NotificationCenterDelegate {
        void didReceivedNotification(int id, Object... args);
    }
}
