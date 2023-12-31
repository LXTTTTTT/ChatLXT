package com.example.chatlxt.Utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.chatlxt.Base.MainApplication;
import com.example.chatlxt.Entity.GsonBean.Receiving.GPTResponse;
import com.example.chatlxt.Entity.GsonBean.Receiving.Message;
import com.example.chatlxt.Entity.GsonBean.Sending.GPTRequest;
import com.example.chatlxt.Global.Constant;
import com.example.chatlxt.Global.Variable;
import com.example.chatlxt.HTTP.GPTInterface;
import com.example.lxt.Utils.RetrofitUtil;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

// 请求工具
public class RequestUtil {

    String TAG = "RequestUtil";
    GPTInterface request;
    Disposable disposable;  // 当前正在发送的请求
    com.example.chatlxt.Entity.DaoBean.Message result;  // 需要添加的答案消息

// 单例 ----------------------------------------------------
    private static RequestUtil requestUtil;
    public static RequestUtil getInstance() {
        if(requestUtil == null){
            requestUtil = new RequestUtil();
        }
        return requestUtil;
    }

    public void init(GPTInterface request){
        this.request = request;
//        this.dataViewModel = dataViewModel;
    }

    // 取消发送请求
    public void cancelRequest(){
        try{
            if (disposable != null && !disposable.isDisposed()) {
                // 取消发送请求
                disposable.dispose();
                Variable.isAsking = false;
                if(result.status==Constant.MESSAGE_SEND){
                    result.status = Constant.MESSAGE_CANCEL;
                    DaoUtil.getInstance().getDaoSession().update(result);
                    Variable.lastestReceive=null;  // 初始化
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getxxx(){
        try{
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void chat(List<Message> chatList){
        try{
            String key = Variable.getGPTKey();
            if(key.isEmpty()){
                MainApplication.getInstance().showToast("请先设置key",0);
                return;
            }
            GPTRequest gptRequest = new GPTRequest();
            gptRequest.messages = chatList;
            request.chat("Bearer "+ key ,gptRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.Observer<GPTResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
//                            Log.e(TAG, "onSubscribe: " );
                            disposable = d;
                            Variable.isAsking = true;
                            // 添加一个空状态消息
                            result = Variable.lastestReceive==null? new com.example.chatlxt.Entity.DaoBean.Message():Variable.lastestReceive;
                            result.role = Constant.GPT_ASSISTANT;
                            result.type = Constant.MESSAGE_ANSWER;
                            result.createTime = System.currentTimeMillis();
                            result.status = Constant.MESSAGE_SEND;
                            result.belong = Variable.nowChat.getId();
                            DaoUtil.getInstance().getDaoSession().insertOrReplace(result);
                            Log.e(TAG, "onSubscribe: "+result.getId()+"/"+result.answer);
                            // 如果最后发那条消息还没有答案的话表示这条消息就是他的答案
                            if(Variable.lastestSend!=null && Variable.lastestSend.answer==null){
                                Variable.lastestSend.answer=result.getId();
                                DaoUtil.getInstance().getDaoSession().update(Variable.lastestSend);
                            }
                            NotificationCenter.standard().postNotification(Constant.RECEIVE_MESSAGE);
                        }

                        @Override
                        public void onNext(GPTResponse answer) {
                            String result_str = answer.choices.get(0).message.content;
                            Log.e(TAG, "聊天答复："+result_str);
                            result.status = Constant.MESSAGE_SUCCESS;
                            result.content = result_str;
                            DaoUtil.getInstance().getDaoSession().update(result);
                            NotificationCenter.standard().postNotification(Constant.RECEIVE_MESSAGE);
                            Variable.lastestReceive=null;  // 初始化
                            Variable.isAsking = false;
                        }

                        @Override
                        public void onError(Throwable e) {
//                            Log.e(TAG, "onError: ");
                            MainApplication.getInstance().showToast("你梯子挂了?",0);
                            result.status = Constant.MESSAGE_FAIL;
                            DaoUtil.getInstance().getDaoSession().update(result);
                            NotificationCenter.standard().postNotification(Constant.RECEIVE_MESSAGE);
                            Variable.lastestReceive=null;  // 初始化
                            Variable.isAsking = false;
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {
                            Log.e(TAG, "onComplete: " );
                        }

                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
