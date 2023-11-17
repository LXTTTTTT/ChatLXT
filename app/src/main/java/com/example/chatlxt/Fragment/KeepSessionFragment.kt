package com.example.chatlxt.Fragment

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatlxt.Adapter.ChatListAdapter
import com.example.chatlxt.Adapter.SingleMessageAdapter
import com.example.chatlxt.Base.BaseFragment
import com.example.chatlxt.ChatActivity
import com.example.chatlxt.Dao.ChatDao
import com.example.chatlxt.Dao.MessageDao
import com.example.chatlxt.Entity.DaoBean.Chat
import com.example.chatlxt.Entity.DaoBean.Message
import com.example.chatlxt.Global.Variable
import com.example.chatlxt.Utils.DaoUtil
import com.example.chatlxt.View.CustomDialog
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
        init_adapter()
        init_control()
    }

    override fun onResume() {
        super.onResume()
        init_chat()
        viewBinding.chatList.closeMenu()
        loge("onResume")
    }

    fun init_adapter() {
        adapter = ChatListAdapter(my_context,chatList)
        // 列表事件
        adapter.setAdapterListener(object : ChatListAdapter.AdapterListener{
            override fun onChoice(id: Long) {
                loge("点击：$id")
                val intent = Intent(my_context,ChatActivity::class.java)
                intent.putExtra(ChatActivity.CHAT_ID,id)
                startActivity(intent)
                viewBinding.chatList.closeMenu()
            }

            override fun onDelete(id: Long) {
                loge("删除：$id")
                application.showWarnDialog(null,"聊天删除后不可恢复\n确定要删除吗？",object :
                    View.OnClickListener {
                    override fun onClick(p0: View?) {
                        DaoUtil.getMessageBuilder().where(MessageDao.Properties.Belong.eq(id)).buildDelete().executeDeleteWithoutDetachingEntities()  // 删除消息数据
                        DaoUtil.getChatBuilder().where(ChatDao.Properties.Id.eq(id)).buildDelete().executeDeleteWithoutDetachingEntities()  // 删除聊天数据
                        init_chat()
                        application.hideWarnDialog()
                        viewBinding.chatList.closeMenu()
                    }
                })
            }
        })
        viewBinding.chatList.adapter = adapter
        val layoutManager = LinearLayoutManager(my_context, LinearLayoutManager.VERTICAL,false)
        viewBinding.chatList.layoutManager = layoutManager
    }

    fun init_chat() {
        // 获取消息列表
        chatList = (DaoUtil.getChatBuilder().where(ChatDao.Properties.Id.notEq(0)).orderDesc(ChatDao.Properties.Id).list() as List<Chat>).toMutableList()

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
        setTitle("聊天列表")  // 设置标题
        back.visibility = View.GONE  // 隐藏返回
        // 更多
        more.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                application.showPopupMenu2(p0,object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        if(chatList.isEmpty()) {
                            application.showToast("当前暂无聊天记录",0)
                            return
                        }
                        // 清除记录
                        application.hidePopupMenu2()
                        application.showWarnDialog(null,"聊天清除后不可恢复\n确定要清除吗？",object :
                            View.OnClickListener {
                            override fun onClick(p0: View?) {
                                DaoUtil.getMessageBuilder().where(MessageDao.Properties.Belong.notEq(0)).buildDelete().executeDeleteWithoutDetachingEntities()  // 删除消息数据
                                DaoUtil.getChatBuilder().where(ChatDao.Properties.Id.notEq(0)).buildDelete().executeDeleteWithoutDetachingEntities()  // 删除聊天数据
//                                DaoUtil.getInstance().daoSession.chatDao.deleteAll()  // 删除聊天数据
//                                chatList.clear()
//                                adapter.update(chatList)  // 清空列表
                                init_chat()
                                application.hideWarnDialog()
                            }
                        })
                    }
                },object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        // 角色扮演
                        application.hidePopupMenu2()
                        // 新建聊天
                        application.showCustomDialog(activity,"选择角色","取消",object : CustomDialog.OnDialogWork{
                            override fun onChoice(character: String?, prologue: String?) {
                                application.hideCustomDialog()
                                // 页面跳转
                                val intent = Intent(my_context,ChatActivity::class.java)
                                intent.putExtra(ChatActivity.IS_NEW_CHAT,true)
                                intent.putExtra(ChatActivity.CHARACTER,character)
                                intent.putExtra(ChatActivity.PROLOGUE,prologue)
                                startActivity(intent)
                            }
                        })
                    }
                })
            }
        })
        viewBinding.noChatText.setOnClickListener {
            loge("添加聊天")
            val intent = Intent(my_context,ChatActivity::class.java)
            intent.putExtra(ChatActivity.IS_NEW_CHAT,true)
            intent.putExtra(ChatActivity.CHARACTER,"ChatLXT")
            intent.putExtra(ChatActivity.PROLOGUE,"")
            startActivity(intent)
        }
    }
}