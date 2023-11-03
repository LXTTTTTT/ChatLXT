package com.example.chatlxt.Entity.GsonBean.Receiving;

public class Message {
    public String role;
    public String content;
    public Message(String role,String content){
        this.role = role;
        this.content = content;
    }
}
