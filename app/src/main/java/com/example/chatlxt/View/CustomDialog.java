package com.example.chatlxt.View;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.chatlxt.Adapter.CharacterListAdapter;
import com.example.chatlxt.R;
import com.example.chatlxt.databinding.DialogCustomBinding;

import java.util.List;


// 自定义内容 dialog
public class CustomDialog extends DialogFragment {

    private String TAG = "CustomDialog";
    DialogCustomBinding viewBinding;
    CharacterListAdapter adapter;
    String title = "标题";
    String cancel = "取消";

    public CustomDialog() {}

    public void setData(String title,String cancel, OnDialogWork onDialogWork){
        this.title = title;
        this.cancel = cancel;
        this.onDialogWork = onDialogWork;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null){
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.corner_fill_white_1);
        }

        // 设置他的宽度为 屏幕的 0.9 倍，不用的话就注释
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.5), ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.5), (int) (dm.heightPixels * 0.5));
        }
//        setCancelable(false);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // 去掉dialog默认标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 弹窗每次出现调用

        viewBinding = DialogCustomBinding.inflate(inflater);

        adapter = new CharacterListAdapter(getContext());
        adapter.setOnItemClickListener(new CharacterListAdapter.onItemClickListener() {
            @Override
            public void onItemClickListener(String character, String prologue) {
                if(onDialogWork!=null){onDialogWork.onChoice(character,prologue);}
            }
        });
        viewBinding.characterList.setAdapter(adapter);
        viewBinding.characterList.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        viewBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        viewBinding.title.setText(title);
        viewBinding.cancel.setText(cancel);
        return viewBinding.getRoot();
    }


    public OnDialogWork onDialogWork;
    public interface OnDialogWork{
        void onChoice(String title,String character);
//        void onCancel();
    }
    public void setOnDialogWork(OnDialogWork onDialogWork){
        this.onDialogWork = onDialogWork;
    }


}
