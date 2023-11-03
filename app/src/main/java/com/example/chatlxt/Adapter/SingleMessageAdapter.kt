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
import com.example.chatlxt.Entity.DaoBean.Message
import com.example.chatlxt.Global.Constant
import com.example.chatlxt.R
import com.example.chatlxt.Utils.DataUtil
import com.example.chatlxt.databinding.AdapterReceiveMessageBinding
import com.example.chatlxt.databinding.AdapterSendMessageBinding

class SingleMessageAdapter(context: Context,list: List<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "SingleMessageAdapter"
    private val my_context = context;
    private val layoutinflater = LayoutInflater.from(context)
    private var messageList = list;
    private val USER = 0;
    private val ASSISTANT = 1;
    private lateinit var sendViewBinding:AdapterSendMessageBinding
    private lateinit var receiveViewBinding:AdapterReceiveMessageBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        Log.e(TAG, "onCreateViewHolder: $viewType")
        if(viewType==USER){
            sendViewBinding = AdapterSendMessageBinding.inflate(layoutinflater,parent,false)
            return SendMessageHolder(sendViewBinding)
        }else{
            receiveViewBinding = AdapterReceiveMessageBinding.inflate(layoutinflater,parent,false)
            return ReceiveMessageHolder(receiveViewBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
//        Log.e(TAG, "onBindViewHolder: "+message.role )
//        Log.e(TAG, "onBindViewHolder: $position/$itemCount")
        var showTime = true;  // 是否显示时间（与上一条间隔大于3分钟）
        if(position!=0 && position<itemCount){
            val message_pre = messageList[position-1]
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

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
//        Log.e(TAG, "getItemViewType: " + messageList[position].role)
        return if(messageList[position].role.equals(Constant.GPT_USER)) USER else ASSISTANT
    }

    inner class SendMessageHolder : RecyclerView.ViewHolder {
        lateinit var viewBinding:AdapterSendMessageBinding
        constructor(sendViewBinding:AdapterSendMessageBinding) : super(sendViewBinding.root) {
            this.viewBinding = sendViewBinding
        }
    }

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