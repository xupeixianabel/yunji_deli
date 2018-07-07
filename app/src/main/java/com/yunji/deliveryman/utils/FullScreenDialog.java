package com.yunji.deliveryman.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

import com.yunji.deliveryman.R;


/**
 * 自定义弹框
 */
public class FullScreenDialog extends AlertDialog {
    Context mContext;
    onTouchListener onTouchListener;
    int width = LayoutParams.MATCH_PARENT;
    int height = LayoutParams.MATCH_PARENT;

    public FullScreenDialog(Context context) {
        super(context, R.style.MyDialog); // 自定义全屏style
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setWidthAndHeight(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = width;
        layoutParams.height = height;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (onTouchListener != null) {
            onTouchListener.onDialogTouch(event);
        }
        return super.onTouchEvent(event);
    }

    public interface onTouchListener {
        void onDialogTouch(MotionEvent event);
    }

    public void setOnTouchListener(onTouchListener listener) {
        this.onTouchListener = listener;
    }
}