package com.example.chatlxt.View;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.chatlxt.Base.MainApplication;
import com.example.chatlxt.R;


// 自定义输入 dialog
public class InputDialog extends DialogFragment {
    private String TAG = "InputDialog";


    public InputDialog() {

    }

    public void setData(onInputDialogWork onInputDialogWork) {
        this.onInputDialogWork = onInputDialogWork;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.6), (int) (dm.heightPixels * 0.2));
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.corner_fill_white_1);
        }
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 弹窗每次出现调用
//        Log.e(TAG, "onCreateView: " );
        View viewContent = inflater.inflate(R.layout.dialog_input, container, false);
        // 去掉dialog默认标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        EditText keyEt = viewContent.findViewById(R.id.key);
        MainApplication.getInstance().showKeyboard(keyEt);
        // 确定按钮
        TextView yesBtn = viewContent.findViewById(R.id.yes);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = keyEt.getText().toString();
                if(key.isEmpty()){
                    MainApplication.getInstance().showToast("请输入key",0);
                    return;
                }
                if(onInputDialogWork!=null){onInputDialogWork.onYes(key);}
                dismiss();
            }
        });
        // 取消按键
        TextView noBtn = viewContent.findViewById(R.id.no);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return viewContent;
    }

    public interface onInputDialogWork{
        void onYes(String key);
    }
    public onInputDialogWork onInputDialogWork;
    public void setOnInputDialogWork(onInputDialogWork onInputDialogWork){
        this.onInputDialogWork = onInputDialogWork;
    }

}
