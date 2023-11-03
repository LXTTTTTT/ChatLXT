package com.example.chatlxt.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatlxt.Entity.DaoBean.Message;

import java.util.ArrayList;
import java.util.List;

// 数据视图模型
public class DataViewModel extends ViewModel {

    private String TAG = "DataViewModel";

    private MutableLiveData<Message> lastest_send = new MutableLiveData<>();  // 账号信息
    private MutableLiveData<Message> lastest_receive = new MutableLiveData<>();  // 账号信息
    private MutableLiveData<String> account_info = new MutableLiveData<>();  // 账号信息
    private MutableLiveData<List<String>> chat_list = new MutableLiveData<>();  // 聊天列表

    public DataViewModel(){
        initData();
    }
    public void initData(){
        lastest_send.postValue(new Message());
        lastest_receive.postValue(new Message());
        account_info.postValue("");
        chat_list.postValue(new ArrayList<>());
    }

    public LiveData<Message> getLastest_send() {return lastest_send;}
    public void setLastest_send(Message lastest_send){this.lastest_send.postValue(lastest_send);}

    public LiveData<Message> getLastest_receive() {return lastest_receive;}
    public void setLastest_receive(Message lastest_receive){this.lastest_receive.postValue(lastest_receive);}

    public LiveData<String> getAccount_info() {return account_info;}
    public void setAccount_info(String account_info){this.account_info.postValue(account_info);}

    public LiveData<List<String>> getChat_list() {return chat_list;}
    public void setChat_list(List<String> chat_list){
        this.chat_list.postValue(chat_list);
    }


}
