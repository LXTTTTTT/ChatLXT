package com.example.chatlxt.Fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatlxt.Adapter.ChatListAdapter
import com.example.chatlxt.Adapter.SingleMessageAdapter
import com.example.chatlxt.Base.BaseFragment
import com.example.chatlxt.Dao.ChatDao
import com.example.chatlxt.Dao.MessageDao
import com.example.chatlxt.Entity.DaoBean.Chat
import com.example.chatlxt.Entity.DaoBean.Message
import com.example.chatlxt.Global.Variable
import com.example.chatlxt.Utils.DaoUtil
import com.example.chatlxt.databinding.FragmentKeepBinding

class KeepSessionFragment: BaseFragment() {

    lateinit var viewBinding:FragmentKeepBinding
    lateinit var adapter:ChatListAdapter
    var chatList:MutableList<Chat> = arrayListOf()

    override fun beforeSetLayout() {}
    override fun getTAG(): String { return "KeepSessionFragment"; }

    override fun setLayout(): Any {
        viewBinding = FragmentKeepBinding.inflate(layoutInflater)
        return viewBinding.root
    }


    override fun initView(view: View) {
        setTitle("聊天列表")  // 设置标题
        back.visibility = View.GONE  // 隐藏返回
        init_adapter()
        init_chat()
        init_control()
    }

    fun init_adapter() {
        adapter = ChatListAdapter(my_context,chatList)
        // 列表事件
        adapter.setAdapterListener(object : ChatListAdapter.AdapterListener{
            override fun onChoice(id: Long) {
                loge("点击：$id")
            }
        })
        viewBinding.chatList.adapter = adapter
        val layoutManager = LinearLayoutManager(my_context, LinearLayoutManager.VERTICAL,false)
        viewBinding.chatList.layoutManager = layoutManager
    }

    fun init_chat() {
        // 获取消息列表
        chatList = (DaoUtil.getChatBuilder().orderDesc(ChatDao.Properties.Id).list() as List<Chat>).toMutableList()
        val chat = Chat()
        val question = Message()
        val answer = Message()
        question.content = "问题一一一一一"
        answer.content = "答案1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111"
        chat.firstQuestion = question
        chat.firstAnswer = answer
        chat.id = 1L
        chatList.add(chat)
        chatList.add(chat)
        chatList.add(chat)
        chatList.add(chat)
        chatList.add(chat)
        chatList.add(chat)
        chatList.add(chat)
        chatList.add(chat)
        chatList.add(chat)
        chatList.add(chat)
        chatList.add(chat)
        chatList.add(chat)
        chatList.add(chat)
        chatList.add(chat)
        chatList.add(chat)

        loge("聊天数量：${chatList.size}")
        if(chatList.size>0){
            viewBinding.chatListGroup.visibility = View.VISIBLE
            viewBinding.noChatGroup.visibility = View.GONE
            adapter.update(chatList)
        }else{
            viewBinding.chatListGroup.visibility = View.GONE
            viewBinding.noChatGroup.visibility = View.VISIBLE
        }

//        if(adapter.itemCount>1){
//            viewBinding.chatList.smoothScrollToPosition(adapter.itemCount - 1)  // 使用这个方法
//        }

    }

    fun init_control() {
        viewBinding.noChatText.setOnClickListener {
            loge("添加聊天")
        }
    }
}