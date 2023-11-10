package com.example.chatlxt.Base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.chatlxt.Base.MainApplication
import com.example.chatlxt.R

abstract class BaseFragment:Fragment() {

    private var TAG:String = javaClass.getName()
    lateinit var application: MainApplication;
    lateinit var my_context:Context;
    lateinit var title: TextView;
    lateinit var back: ImageView;
    lateinit var more: ImageView;
    lateinit var delete: ImageView;
    lateinit var layoutView:View;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)  // 开启菜单项
        beforeSetLayout()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        application = MainApplication.getInstance()
        my_context = requireContext()
        layoutView = if(setLayout() is Int) inflater.inflate((setLayout() as Int),container,false) else (setLayout() as View)  // 使用 R.layout 或 ViewBinding

        title = layoutView.findViewById(R.id.title)
        back = layoutView.findViewById(R.id.back)
        more = layoutView.findViewById(R.id.more)
        delete = layoutView.findViewById(R.id.delete)
        delete.visibility = View.GONE

        initView(layoutView)  // 初始化控件事件
        // 设置 TAG
        if (getTAG() != null) {
            TAG = getTAG()
        } else {
            TAG = "unknow_fragment"
        }
        return layoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try{

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    abstract fun beforeSetLayout()
    abstract fun setLayout():Any
    abstract fun initView(view: View)
    abstract fun getTAG():String

    public fun loge(log:String){
        Log.e(TAG, log )
    }

    fun setTitle(title:String){
        this.title?.let {
            it.visibility = View.VISIBLE
            it.text = title
        }
    }

}