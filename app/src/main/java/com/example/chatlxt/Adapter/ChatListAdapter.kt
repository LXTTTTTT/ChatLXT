package com.example.chatlxt.Adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.chatlxt.Entity.DaoBean.Chat
import com.example.chatlxt.Entity.DaoBean.Message
import com.example.chatlxt.Global.Constant
import com.example.chatlxt.R
import com.example.chatlxt.Utils.DataUtil
import com.example.chatlxt.databinding.AdapterChatListBinding
import com.example.chatlxt.databinding.AdapterReceiveMessageBinding
import com.example.chatlxt.databinding.AdapterSendMessageBinding

class ChatListAdapter(context: Context, list: List<Chat>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "ChatListAdapter"
    private val my_context = context;
    private val layoutinflater = LayoutInflater.from(context)
    private var chatList = list;

    private lateinit var chatViewBinding:AdapterChatListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        chatViewBinding = AdapterChatListBinding.inflate(layoutinflater,parent,false)
        return ChatMessageHolder(chatViewBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = chatList[position]
        try {
//            Log.e(TAG, "onBindViewHolder: " + chat.firstAnswer)
            val viewHolder = holder as ChatMessageHolder
            chat.firstQuestion?.let { viewHolder.viewBinding.question.text = it }
            chat.firstAnswer?.let { viewHolder.viewBinding.answer.text = it }
            viewHolder.viewBinding.item.setOnClickListener {
                listener?.let { it.onChoice(chat.id) }
                Log.e(TAG, "点击的聊天是: ${chat.toString()}")
            }
            viewHolder.viewBinding.delete.setOnClickListener(object :View.OnClickListener{
                override fun onClick(p0: View?) {
                    listener?.let { it.onDelete(chat.id) }
                }
            })
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }


    inner class ChatMessageHolder : RecyclerView.ViewHolder {
        lateinit var viewBinding:AdapterChatListBinding
        constructor(viewBinding:AdapterChatListBinding) : super(viewBinding.root) {
            this.viewBinding = viewBinding
        }
    }

    fun update(list: MutableList<Chat>) {
        chatList = list
        notifyDataSetChanged()
    }

// 接口 -------------------------------------------------------------
    var listener: AdapterListener? = null
    fun setAdapterListener(listener:AdapterListener) {
        this.listener=listener
    }
    interface AdapterListener {
        fun onChoice(id:Long)
        fun onDelete(id:Long)
    }

}