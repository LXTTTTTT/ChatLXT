package com.example.chatlxt;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chatlxt.Adapter.SingleMessageAdapter;
import com.example.chatlxt.Base.BaseActivity;
import com.example.chatlxt.Dao.ChatDao;
import com.example.chatlxt.Dao.MessageDao;
import com.example.chatlxt.Entity.DaoBean.Chat;
import com.example.chatlxt.Entity.DaoBean.Message;
import com.example.chatlxt.Global.Constant;
import com.example.chatlxt.Global.Variable;
import com.example.chatlxt.Utils.DaoUtil;
import com.example.chatlxt.Utils.NotificationCenter;
import com.example.chatlxt.Utils.RequestUtil;
import com.example.chatlxt.databinding.ActivityChatBinding;
import com.example.lxt.Utils.RetrofitUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChatActivity extends BaseActivity implements NotificationCenter.NotificationCenterDelegate{

    private ActivityChatBinding viewBinding;
    private List<Message> messageList = new ArrayList<>();
    private SingleMessageAdapter adapter;
    private com.example.chatlxt.Entity.GsonBean.Receiving.Message msg;  // 最后一条消息的请求对象
    private Chat chat_info;
    private Long chatID = 0l;  // 当前聊天id
    private String character = "角色";  // 角色
    private String prologue = "";  // 序言
    private boolean newChat = false;  // 是否新页面
    public static String IS_NEW_CHAT = "IS_NEW_CHAT";
    public static String CHAT_ID = "CHAT_ID";
    public static String CHARACTER = "CHARACTER";
    public static String PROLOGUE = "PROLOGUE";

    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if(adapter.getItemCount()>1){viewBinding.messageList.smoothScrollToPosition(adapter.getItemCount() - 1);}
                    break;
            }
        }
    };
    @Override
    protected Object setLayout() {
        viewBinding = ActivityChatBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }

    @Override protected void beforeSetLayout() {}

    @Override
    protected void initView(Bundle savedInstanceState) {
//        initKeyboardListener();
        newChat = getIntent().getBooleanExtra(IS_NEW_CHAT,false);
        if(newChat){
            chat_info = new Chat();
            DaoUtil.getInstance().getDaoSession().insertOrReplace(chat_info);
            character = getIntent().getStringExtra(CHARACTER);
            prologue = getIntent().getStringExtra(PROLOGUE);
            chat_info.character = character;
            chat_info.prologue = prologue;
            chatID = chat_info.getId();
            loge("创建聊天:"+chat_info.toString());
        }else {
            chatID = getIntent().getLongExtra(CHAT_ID,0L);
            loge("进入聊天 id:"+chatID);
            List<Chat> chats = DaoUtil.getChatBuilder().where(ChatDao.Properties.Id.eq(chatID)).list();
            if(chats!=null && !chats.isEmpty()){
                chat_info = chats.get(0);
                character = chat_info.character;
                prologue = chat_info.prologue;
                loge("角色是："+character);
            }else {
                loge("没有对应的记录");
            }
        }
        Variable.nowChat = chat_info;
        NotificationCenter.standard().addveObserver(this, Constant.RECEIVE_MESSAGE);
        init_adapter();
        if(!newChat){init_message();}
        init_control();
    }

    @Override
    protected void initData() {

    }

    private void init_adapter() {
        prologue = prologue.replaceAll("\n","");
        boolean showPrologue = prologue.equals("")? false:true;
        adapter = new SingleMessageAdapter(my_context,messageList,prologue,showPrologue);
        adapter.setAdapterListener(new SingleMessageAdapter.AdapterListener() {
            @Override
            public void onReSend(@NonNull Message message) {
                application.showToast("重发消息",0);
                if(Variable.isAsking){ application.showToast("脑子过载了",0);return; }
                // 根据id找到发送的问题
                List<Message> messages = DaoUtil.getMessageBuilder().where(MessageDao.Properties.Answer.eq(message.getId())).list();
                Message question = messages.size()>0? messages.get(0) : null;
                loge("答案id：" + message.getId() + " / 问题数量：" + messages.size() + " / 问题id：" + (question != null ? question.getId() : "null") + " / 问题内容：" + (question != null ? question.getContent() : "null"));
                if(question!=null){
                    Variable.lastestReceive = message;
                    Variable.lastestSend = question;
                    askQuestion(question.getId());
                }
            }
        });

        viewBinding.messageList.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(my_context,LinearLayoutManager.VERTICAL,false);
//        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);  // 让 recyclerview 跟随键盘弹起
        GridLayoutManager gridLayoutManager = new GridLayoutManager(my_context,1);
        viewBinding.messageList.setLayoutManager(gridLayoutManager);
    }

    private void init_message() {
        // 获取消息列表
        messageList = DaoUtil.getMessageBuilder().where(MessageDao.Properties.Belong.eq(chatID)).orderDesc(MessageDao.Properties.Id).limit(20).list();
        Collections.reverse(messageList); // 反转术式
        loge("消息数量："+messageList.size());
        adapter.update(messageList);
        // 滚动到底部
        handler.sendEmptyMessage(0);

    }

    private void init_control() {
        setTitle(character);  // 设置标题
        more.setVisibility(View.GONE);
        delete.setVisibility(View.VISIBLE);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(messageList.isEmpty()) {
                    application.showToast("当前暂无消息",0);
                    return;
                }
                application.showWarnDialog(null, "记录清除后不可恢复\n确定要清除吗？", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DaoUtil.getMessageBuilder().where(MessageDao.Properties.Belong.eq(chatID)).buildDelete().executeDeleteWithoutDetachingEntities();  // 删除数据
                        messageList.clear();
                        adapter.update(messageList);  // 清空列表
                        application.hideWarnDialog();
                    }
                });
            }
        });

        // 发送
        viewBinding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewBinding.content.getText().toString().isEmpty()) {return;}
                if(Variable.isAsking){ application.showToast("脑子过载了",0);return; }
                loge("发送消息");
                String content = viewBinding.content.getText().toString();
                // 添加数据库
                Message message = new Message();
                message.content = content;
                message.role = Constant.GPT_USER;
                message.type = Constant.MESSAGE_QUESTION;
                message.createTime = System.currentTimeMillis();
                message.belong = chatID;
                Variable.lastestSend = message;
                DaoUtil.getInstance().getDaoSession().insertOrReplace(Variable.lastestSend);
                // 刷新列表
                init_message();
                askQuestion(null);
                viewBinding.content.setText("");  // 清空输入栏
            }
        });
        // 解决键盘弹起时遮挡 recyclerview 问题
        viewBinding.content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                loge("请求焦点："+b);
                if(b){
//                    application.showKeyboard(viewBinding.content);
                    handler.sendEmptyMessageDelayed(0,250);
                }
            }
        });
    }

    // 发送请求
    private void askQuestion(Long toID){
        if(!messageList.isEmpty()){
            List<com.example.chatlxt.Entity.GsonBean.Receiving.Message> msgs = new ArrayList<>();
            for (int i = 0; i < messageList.size(); i++) {
                Message message = messageList.get(i);
                String conversation;
                if(message.type==Constant.MESSAGE_QUESTION){
                    if(i==0){conversation = prologue + message.content;}else {conversation = message.content;}
                    msg = new com.example.chatlxt.Entity.GsonBean.Receiving.Message(Constant.GPT_USER,conversation);
                    msgs.add(msg);
                }else {
                    if(message.content!=null && !message.content.equals("")){
                        conversation = message.content;
                    }else {
                        conversation = "";
                    }
                    msg = new com.example.chatlxt.Entity.GsonBean.Receiving.Message(Constant.GPT_ASSISTANT,conversation);
                    msgs.add(msg);
                }
                if(toID!=null && message.getId()==toID){break;}
            }
            RequestUtil.getInstance().chat(msgs);
        }
    }

    @Override
    protected void onPause() {
        if(!messageList.isEmpty()){
            if(chat_info.firstQuestion==null || chat_info.firstQuestion.equals("")){
                List<Message> questions = DaoUtil.getMessageBuilder().where(MessageDao.Properties.Belong.eq(chatID),MessageDao.Properties.Type.eq(Constant.MESSAGE_QUESTION)).orderAsc(MessageDao.Properties.Id).limit(5).list();
                if(questions!=null || !questions.isEmpty()){
                    chat_info.firstQuestion = questions.get(0).content;
                } else {chat_info.firstQuestion = "没有问题";}

                List<Message> answers = DaoUtil.getMessageBuilder().where(MessageDao.Properties.Belong.eq(chatID),MessageDao.Properties.Type.eq(Constant.MESSAGE_ANSWER)).orderAsc(MessageDao.Properties.Id).limit(5).list();
                if(answers!=null || !answers.isEmpty()){
                    chat_info.firstAnswer = answers.get(0).content==null? "答案就是没有答案":answers.get(0).content;
                } else {
                    chat_info.firstAnswer = "答案就是没有答案";
                }
            }else {
                if(chat_info.firstAnswer.equals("答案就是没有答案")){
                    List<Message> answers = DaoUtil.getMessageBuilder().where(MessageDao.Properties.Belong.eq(chatID),MessageDao.Properties.Type.eq(Constant.MESSAGE_ANSWER)).orderAsc(MessageDao.Properties.Id).limit(5).list();
                    chat_info.firstAnswer = answers.get(0).content==null? "答案就是没有答案":answers.get(0).content;
                }
            }
            DaoUtil.getInstance().getDaoSession().update(chat_info);
        }else {
            loge("没有消息，删除该聊天");
            if(chat_info!=null) DaoUtil.getInstance().getDaoSession().delete(chat_info);
        }
        super.onPause();
    }



    @Override
    protected void onDestroy() {
        if(Variable.lastestSend!=null && Variable.lastestSend.belong == chatID){RequestUtil.getInstance().cancelRequest();} // 取消请求
        super.onDestroy();
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        if(id==Constant.RECEIVE_MESSAGE){
            init_message();
        }
    }
}
