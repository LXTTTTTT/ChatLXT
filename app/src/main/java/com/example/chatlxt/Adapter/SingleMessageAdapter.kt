package com.example.chatlxt.Adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.chatlxt.Entity.DaoBean.Message
import com.example.chatlxt.Global.Constant
import com.example.chatlxt.R
import com.example.chatlxt.Utils.DataUtil
import com.example.chatlxt.databinding.AdapterPrologueBinding
import com.example.chatlxt.databinding.AdapterReceiveMessageBinding
import com.example.chatlxt.databinding.AdapterSendMessageBinding

class SingleMessageAdapter(context: Context,list: List<Message>,prologue: String?="",showPrologue:Boolean): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "SingleMessageAdapter"
    private val my_context = context;
    private val layoutinflater = LayoutInflater.from(context)
    private var messageList = list;  // 消息列表
    private var prologue = prologue;  // 序言
    private var showPrologue = showPrologue  // 是否展示开场白
    private val USER = 1;
    private val ASSISTANT = 2;
    private val PROLOGUE = 0;
    private lateinit var sendViewBinding:AdapterSendMessageBinding
    private lateinit var receiveViewBinding:AdapterReceiveMessageBinding
    private lateinit var prologueViewBinding:AdapterPrologueBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        Log.e(TAG, "onCreateViewHolder: $viewType")
        if(viewType==PROLOGUE){
            prologueViewBinding = AdapterPrologueBinding.inflate(layoutinflater,parent,false)
            return PrologueHolder(prologueViewBinding)
        }
        else if(viewType==USER){
            sendViewBinding = AdapterSendMessageBinding.inflate(layoutinflater,parent,false)
            return SendMessageHolder(sendViewBinding)
        }else{
            receiveViewBinding = AdapterReceiveMessageBinding.inflate(layoutinflater,parent,false)
            return ReceiveMessageHolder(receiveViewBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // 序言
        if(getItemViewType(position)==PROLOGUE){
            try {
                val prologueHolder = holder as PrologueHolder
                prologueHolder.viewBinding.prologue.text = prologue
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        // 消息
        else{
            val messagePosition = if(showPrologue) position-1 else position
//            Log.e(TAG, "消息类型: ${getItemViewType(position)}" )
            val message = messageList[messagePosition]
//            Log.e(TAG, "onBindViewHolder: "+message.role )
//            Log.e(TAG, "onBindViewHolder: $position/$itemCount")
            var showTime = true;  // 是否显示时间（与上一条间隔大于3分钟）
            if(messagePosition!=0 && position<itemCount){
                val message_pre = messageList[messagePosition-1]
                val interval = message.createTime - message_pre.createTime
                showTime = interval>=180000L
            }
            if(message.role.equals(Constant.GPT_USER)){
                try {
                    val sendMessageHolder = holder as SendMessageHolder
                    sendMessageHolder.viewBinding.content.text = message.content
                    sendMessageHolder.viewBinding.content.setTextColor(ContextCompat.getColor(my_context, R.color.black))
                    sendMessageHolder.viewBinding.time.text = DataUtil.stamp2Date(message.createTime)
                    sendMessageHolder.viewBinding.time.visibility = if(showTime) View.VISIBLE else View.GONE
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }else{
                try {
                    val receiveMessageHolder = holder as ReceiveMessageHolder
                    receiveMessageHolder.viewBinding.content.text = message.content
                    receiveMessageHolder.viewBinding.content.setTextColor(ContextCompat.getColor(my_context, R.color.black))
                    receiveMessageHolder.viewBinding.time.text = DataUtil.stamp2Date(message.createTime)
                    receiveMessageHolder.viewBinding.time.visibility = if(showTime) View.VISIBLE else View.GONE
                    when(message.status){
                        Constant.MESSAGE_SEND -> {
                            receiveMessageHolder.viewBinding.content.text = Constant.THINKING
                            receiveMessageHolder.viewBinding.content.setTextColor(ContextCompat.getColor(my_context, R.color.blue_1))
                            receiveMessageHolder.viewBinding.loading.visibility = View.VISIBLE
                            receiveMessageHolder.viewBinding.fail.visibility = View.GONE
                        }
                        Constant.MESSAGE_FAIL -> {
                            receiveMessageHolder.viewBinding.content.text = Constant.FAIL
                            receiveMessageHolder.viewBinding.content.setTextColor(ContextCompat.getColor(my_context, R.color.blue_1))
                            receiveMessageHolder.viewBinding.loading.visibility = View.GONE
                            receiveMessageHolder.viewBinding.fail.visibility = View.VISIBLE
                            receiveMessageHolder.viewBinding.fail.setOnClickListener {
                                listener?.onReSend(message)  // 把它传出去
                            }
                        }
                        else -> {
                            receiveMessageHolder.viewBinding.loading.visibility = View.GONE
                            receiveMessageHolder.viewBinding.fail.visibility = View.GONE
                        }
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if(showPrologue) messageList.size + 1 else messageList.size  // 序言+1
    }

    override fun getItemViewType(position: Int): Int {
        if(showPrologue){
            if(position==0){
                return PROLOGUE;
            }else{
                return if(messageList[position-1].role.equals(Constant.GPT_USER)) USER else ASSISTANT
            }
        }else{
            return if(messageList[position].role.equals(Constant.GPT_USER)) USER else ASSISTANT
        }
    }

    // 序言
    inner class PrologueHolder : RecyclerView.ViewHolder {
        lateinit var viewBinding:AdapterPrologueBinding
        constructor(prologueViewBinding:AdapterPrologueBinding) : super(prologueViewBinding.root) {

            this.viewBinding = prologueViewBinding
        }
    }
    // 发送
    inner class SendMessageHolder : RecyclerView.ViewHolder {
        lateinit var viewBinding:AdapterSendMessageBinding
        constructor(sendViewBinding:AdapterSendMessageBinding) : super(sendViewBinding.root) {

            this.viewBinding = sendViewBinding
        }
    }
    // 接收
    inner class ReceiveMessageHolder : RecyclerView.ViewHolder {
        lateinit var viewBinding:AdapterReceiveMessageBinding
        constructor(receiveViewBinding:AdapterReceiveMessageBinding) : super(receiveViewBinding.root) {

            this.viewBinding = receiveViewBinding
        }
    }

    fun update(list: MutableList<Message>) {
        messageList = list
        notifyDataSetChanged()
    }

// 接口 -------------------------------------------------------------
    var listener: AdapterListener? = null
    fun setAdapterListener(listener:AdapterListener) {
        this.listener=listener
    }
    interface AdapterListener {
        fun onReSend(message:Message)
    }

}