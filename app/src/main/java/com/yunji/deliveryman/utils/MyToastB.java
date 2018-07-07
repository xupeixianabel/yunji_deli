package com.yunji.deliveryman.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yunji.deliveryman.R;

public class MyToastB {

    private static String oldMsg;
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;
    private static TextView tv_toast;

    public static void showToast(Context context, String s) {

        if (toast == null) {
            toast = new Toast(context);
            View layout = View.inflate(context, R.layout.toast_view_b, null);
            tv_toast = (TextView) layout.findViewById(R.id.toast_textview);
            tv_toast.setText(s);
            toast.setView(layout);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            if (tv_toast != null) {
                tv_toast.setText(s);
            }
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.show();
            }
        }
        oneTime = twoTime;
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }

}