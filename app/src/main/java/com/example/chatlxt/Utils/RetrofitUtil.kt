package com.example.lxt.Utils
import android.util.Log
import com.example.chatlxt.Global.Constant
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Retrofit 网络请求工具类，kotlin 的 object 自带单例
object RetrofitUtil {

    private val TAG:String = "RetrofitUtil"
    var builder:OkHttpClient.Builder?=null

    fun init(){
        builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor { msg -> Log.e(TAG+" 返回数据", msg) }  // 不重写,部分手机平板需要打开日志
//       if (Variable.DebugMode) {
//            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//        } else {
//            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE  // 不是调试模式不打印
//        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY  // 定义打印的数据是请求体

        builder!!.addInterceptor(loggingInterceptor)
            .connectTimeout(20, TimeUnit.SECONDS)  // 设置连接超时
            .readTimeout(20, TimeUnit.SECONDS)  // 设置读取超时
            .writeTimeout(20, TimeUnit.SECONDS)  // 设置写入超时
    }

    // 请求默认的 url
    fun getRetrofit(): Retrofit? {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  // 在这里把 Retrofit 和 rxjava 关联起来，让请求接口返回的数据转化为 rxjava 的 Observable 对象
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))  // 让它把返回的数据转化为 json 对象
            .baseUrl(Constant.BASE_URL)  // 绑定基本 url
            .client(builder!!.build())  // 配置请求器 okhttp
            .build()
    }

    // 请求指定的 url
    fun getRetrofit(url:String): Retrofit? {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .baseUrl(url)
            .client(builder!!.build())
            .build()
    }

}