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
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.chatlxt.R;


// 自定义警告 dialog
public class WarnDialog extends DialogFragment {
    private String TAG = "WarnDialog";
    private Activity mContext;
    private View.OnClickListener onYesClickListener;
    private String warnStr;


    public WarnDialog() {

    }

    public void setData(Activity context, String str, View.OnClickListener onYesClickListener) {
        this.mContext = context;
        this.warnStr = str;
        this.onYesClickListener = null;
        this.onYesClickListener = onYesClickListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.6), ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.corner_fill_white_1);
        }
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 弹窗每次出现调用
//        Log.e(TAG, "onCreateView: " );
        View viewContent = inflater.inflate(R.layout.dialog_warn, container, false);
        // 去掉dialog默认标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 警告标题
        TextView warnTv = viewContent.findViewById(R.id.warn_text);
        warnTv.setText(warnStr);

        // 确定按钮
        TextView yesBtn = viewContent.findViewById(R.id.yes);
        yesBtn.setOnClickListener(onYesClickListener);
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
}
