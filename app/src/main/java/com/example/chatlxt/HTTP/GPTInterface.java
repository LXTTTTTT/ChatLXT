package com.example.chatlxt.HTTP;

import com.example.chatlxt.Entity.GsonBean.Receiving.GPTResponse;
import com.example.chatlxt.Entity.GsonBean.Sending.GPTRequest;
import com.example.chatlxt.Global.Constant;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GPTInterface {

    @POST(Constant.GPT_CHAT)
    Observable<GPTResponse> chat(@Header("Authorization") String token, @Body GPTRequest body);

}
