package com.example.chatlxt.Fragment

import android.os.Handler
import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatlxt.Adapter.SingleMessageAdapter
import com.example.chatlxt.Base.BaseFragment
import com.example.chatlxt.Dao.MessageDao
import com.example.chatlxt.Entity.DaoBean.Message
import com.example.chatlxt.Global.Constant
import com.example.chatlxt.Global.Variable
import com.example.chatlxt.Utils.DaoUtil
import com.example.chatlxt.Utils.NotificationCenter
import com.example.chatlxt.Utils.RequestUtil
import com.example.chatlxt.Utils.SharedPreferencesUtil
import com.example.chatlxt.View.CustomDialog
import com.example.chatlxt.View.InputDialog
import com.example.chatlxt.databinding.FragmentSingleBinding

class SingleSessionFragment: BaseFragment(),NotificationCenter.NotificationCenterDelegate {

    lateinit var viewBinding:FragmentSingleBinding;
    var messageList:MutableList<Message> = arrayListOf()
    lateinit var adapter:SingleMessageAdapter
    lateinit var msg:com.example.chatlxt.Entity.GsonBean.Receiving.Message  // 最后一条消息内容
    var prologue:String = ""  // 序言
    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> if (adapter.itemCount > 1) {
                    viewBinding.messageList.smoothScrollToPosition(adapter.itemCount - 1)
                }
            }
        }
    }

    override fun beforeSetLayout() {}
    override fun getTAG(): String { return "SingleSessionFragment"; }

    override fun setLayout(): Any {
        viewBinding = FragmentSingleBinding.inflate(layoutInflater)
        return viewBinding.root
    }


    override fun initView(view: View) {
        NotificationCenter.standard().addveObserver(this,Constant.RECEIVE_MESSAGE)
        init_adapter()
        init_message()
        init_control()
    }

    fun init_adapter() {
        adapter = SingleMessageAdapter(my_context,messageList,null,false)
        // 列表事件
        adapter.setAdapterListener(object : SingleMessageAdapter.AdapterListener{
            // 重发
            override fun onReSend(message : Message) {
                application.showToast("重发消息",0)
                if(Variable.isAsking){ application.showToast("脑子过载了",0);return }
                // 根据id找到发送的问题
                val messages = DaoUtil.getMessageBuilder().where(MessageDao.Properties.Answer.eq(message.id)).list()
                val question:Message? = if(messages.size>0) messages[0] as Message? else null
                loge("答案id：${message.id} / 问题数量：${messages.size} / 问题id：${question?.let { it.id }} / 问题内容：${question?.let { it.content }}")
                question?.let {
                    Variable.lastestReceive = message
                    askQuestion(it.content)
                }
            }
        })
        viewBinding.messageList.adapter = adapter
//        val layoutManager = LinearLayoutManager(my_context,LinearLayoutManager.VERTICAL,false)
//        layoutManager.stackFromEnd = true  // 让 recyclerview 跟随键盘弹起
        val layoutManager = GridLayoutManager(my_context,1)
        viewBinding.messageList.layoutManager = layoutManager
    }

    fun init_message() {
        // 获取消息列表
        messageList = (DaoUtil.getMessageBuilder().where(MessageDao.Properties.Belong.eq(0)).orderDesc(MessageDao.Properties.Id).limit(20).list() as List<Message>).toMutableList()
        messageList.reverse()  // 反转术式
        loge("消息数量：${messageList.size}")
        adapter.update(messageList)
        handler.sendEmptyMessage(0)
    }

    fun init_control() {
        setTitle("ChatLXT")  // 设置标题
        back.visibility = View.GONE  // 隐藏返回
        // 更多
        more.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                application.showPopupMenu(p0,object :OnClickListener{
                    override fun onClick(p0: View?) {
                        if(messageList.isEmpty()) {
                            application.showToast("当前暂无消息",0)
                            return
                        }
                        // 清除记录
                        application.hidePopupMenu()
                        application.showWarnDialog(null,"记录清除后不可恢复\n确定要清除吗？",object : OnClickListener{
                            override fun onClick(p0: View?) {
                                DaoUtil.getMessageBuilder().where(MessageDao.Properties.Belong.eq(0)).buildDelete().executeDeleteWithoutDetachingEntities()  // 删除数据
                                messageList.clear()
                                adapter.update(messageList)  // 清空列表
                                application.hideWarnDialog()
                            }
                        })
                    }
                },object :OnClickListener{
                    override fun onClick(p0: View?) {
                        // 角色扮演
                        application.hidePopupMenu()
                        application.showCustomDialog(activity,"变身!!","告辞",object : CustomDialog.OnDialogWork{
                            override fun onChoice(character: String?, prologue: String?) {
                                loge("$character : ${prologue?.length}")
                                character?.let { setTitle(it) }
                                prologue?.let { this@SingleSessionFragment.prologue = it }
                                application.hideCustomDialog()
                            }
                        })
                    }
                },object :OnClickListener{
                    override fun onClick(p0: View?) {
                        // 设置 key
                        application.hidePopupMenu()
                        application.showInputDialog(activity,object :InputDialog.onInputDialogWork{
                            override fun onYes(key: String?) {
                                key?.let {
                                    SharedPreferencesUtil.getInstance().setString(Constant.GPT_KEY,key)
                                    application.showToast("设置成功",0)
                                }
                            }
                        })
                    }
                })
            }
        })
        // 发送
        viewBinding.send.setOnClickListener{
            if(viewBinding.content.text.toString().isEmpty()) {return@setOnClickListener}
            if(Variable.isAsking){ application.showToast("脑子过载了",0);return@setOnClickListener }
            loge("发送消息")
            val content = viewBinding.content.text.toString()
            // 添加数据库
            val message = Message()
            message.content = content
            message.role = Constant.GPT_USER
            message.type = Constant.MESSAGE_QUESTION;
            message.createTime = System.currentTimeMillis();
            message.belong = 0L
            Variable.lastestSend = message
            DaoUtil.getInstance().daoSession.insertOrReplace(Variable.lastestSend)
            // 刷新列表
            init_message()
            askQuestion(content)
            viewBinding.content.text.clear()  // 清空输入栏
        }
        // 解决键盘弹起时遮挡 recyclerview 问题
        viewBinding.content.setOnFocusChangeListener { view, b ->
            if (b) handler.sendEmptyMessageDelayed(0,250)
        }
        viewBinding.content.setOnClickListener {
            handler.sendEmptyMessageDelayed(0,250)
        }

    }

    fun askQuestion(question:String){
        // 发送请求
        val character_question = prologue + question
        msg = com.example.chatlxt.Entity.GsonBean.Receiving.Message(Constant.GPT_USER,character_question)
        var msgs = arrayListOf<com.example.chatlxt.Entity.GsonBean.Receiving.Message>()
        msgs.add(msg)
        RequestUtil.getInstance().chat(msgs)
    }

    override fun didReceivedNotification(id: Int, vararg args: Any?) {
        when (id) {
            Constant.RECEIVE_MESSAGE -> {
                init_message()
            }
            else -> return
        }
    }
}