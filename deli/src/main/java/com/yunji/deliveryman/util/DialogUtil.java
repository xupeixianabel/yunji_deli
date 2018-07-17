package com.yunji.deliveryman.util;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.adapter.TaskArriveAdapter;
import com.yunji.deliveryman.bean.MainLayerBean;

import java.util.ArrayList;

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
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }



    public static Dialog quCanDialog(Activity context, ArrayList<MainLayerBean> layerBeans,int position, View.OnClickListener listener) {
        if (context == null) {
            return null;
        }
        if (context.isFinishing()) {
            return null;
        }
        Dialog dialog = new Dialog(context, R.style.MyDialog);
        dialog.setContentView(R.layout.dialog_task_arrive);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TextView confirm=dialog.findViewById(R.id.confirm_to_get);
        confirm.setOnClickListener(listener);
        RecyclerView rv_qucan=dialog.findViewById(R.id.rv_qucan);

        TaskArriveAdapter adapter=new TaskArriveAdapter(context);
        adapter.setChoosedPosition(position);
        adapter.setData(layerBeans);
        rv_qucan.setLayoutManager(new LinearLayoutManager(context));
        rv_qucan.setAdapter(adapter);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }



}
