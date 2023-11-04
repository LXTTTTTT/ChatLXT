package com.example.chatlxt.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.chatlxt.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CharacterListAdapter extends RecyclerView.Adapter<CharacterListAdapter.MyViewHolder> {

    private final String TAG = "CharacterListAdapter";
    Context my_context;
    List<String[]> characters = new ArrayList<>();  // 用到的操作名称

    public CharacterListAdapter(Context context){
        my_context = context;
        // 初始化 json 文件里面的数据
        try {
            // 打开 JSON 文件
            InputStream inputStream = my_context.getAssets().open("cosplay.json");
            // 使用 InputStreamReader 读取 JSON 文件内容
            InputStreamReader reader = new InputStreamReader(inputStream);
            // 使用 BufferedReader 逐行读取文件内容
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            // JSON 数据现在存储在 stringBuilder 中
            String jsonData = stringBuilder.toString();
            // 现在你可以将 jsonData 解析成 JSON 对象
            JSONArray jsonArray = new JSONArray (jsonData);
            // 进一步处理 jsonObject
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i); // 获取数组中的每个JSON对象
                // 从JSON对象中提取数据
                String act = jsonObject.getString("act");
                String prompt = jsonObject.getString("prompt");
                // 在这里，你可以对提取的数据进行任何需要的操作
                Log.i(TAG, "Act: " + act + " / Prompt: " + prompt);
                String[] character = new String[2];
                character[0] = act;
                character[1] = prompt;
                characters.add(character);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(my_context).inflate(R.layout.adapter_character_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String[] character = characters.get(position);
        holder.title.setText(character[0]);
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClickListener(character[0],character[1]);
                }
            }
        });
        if(position==characters.size()-1){
//            holder.bottom_line.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private View bottom_line;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item);
            bottom_line = itemView.findViewById(R.id.bottom_line);
        }
    }

// 接口 ----------------------------------------------------------
    public interface onItemClickListener{
        void onItemClickListener(String title,String character);
    }
    public onItemClickListener onItemClickListener;
    public void setOnItemClickListener(onItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

}
