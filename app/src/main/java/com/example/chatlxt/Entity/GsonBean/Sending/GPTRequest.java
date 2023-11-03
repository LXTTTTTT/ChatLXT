package com.example.chatlxt.Entity.GsonBean.Sending;


import com.example.chatlxt.Entity.GsonBean.Receiving.Message;

import java.util.ArrayList;
import java.util.List;

public class GPTRequest {
    public String model = "gpt-3.5-turbo";
    public float temperature = 0.7f;
    public List<Message> messages = new ArrayList<>();
}
