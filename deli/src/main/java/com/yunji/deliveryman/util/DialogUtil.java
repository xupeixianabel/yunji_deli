package com.yunji.deliveryman.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.ViewGroup;

import com.yunji.deliveryman.R;

public class DialogUtil {
    public static Dialog estopDialog(Activity context) {
        if (context == null) {
            return null;
        }
        if (context.isFinishing()) {
            return null;
        }
        Dialog dialog = new Dialog(context, R.style.MyDialog);
        dialog.setContentView(R.layout.dialog_estop);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        dialog. getWindow().getDecorView().setPadding(0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }
}
