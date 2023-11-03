package com.example.chatlxt.Entity.GsonBean.Receiving;

import java.util.List;

public class GPTResponse {
    public String id;
    public String object;
    public long created;
    public String model;
    public List<Choice> choices;
    public Usage usage;

    public class Choice {
        public int index;
        public Message message;
        public String finish_reason;
    }

    public class Usage {
        public int prompt_tokens;
        public int completion_tokens;
        public int total_tokens;
    }
    
}
