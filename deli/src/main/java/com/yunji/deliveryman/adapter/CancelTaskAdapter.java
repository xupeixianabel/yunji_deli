package com.yunji.deliveryman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.base.SimpleRecAdapter;
import com.yunji.deliveryman.bean.SettingBean;
import com.yunji.deliveryman.bean.TaskBean;
import com.yunji.deliveryman.util.KnifeKit;

import butterknife.BindView;

public class CancelTaskAdapter extends SimpleRecAdapter<TaskBean, CancelTaskAdapter.ViewHolder> {
    public CancelTaskAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_item_cancel_task;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TaskBean bean = data.get(position);
        holder.iv_check.setSelected(bean.isChecked());
        if (bean.getDistrict().equals("E")){
            holder.tv_task_name.setText(context.getResources().getString(R.string.task_go_kichen));
        }else {
            holder.tv_task_name.setText(String.format(context.getResources().getString(R.string.task_delivery),bean.getDistrict()));
        }

        holder.ll_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.get(position).setChecked(!data.get(position).isChecked());
                notifyDataSetChanged();
            }
        });


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_check)
        ImageView iv_check;
        @BindView(R.id.tv_task_name)
        TextView tv_task_name;
        @BindView(R.id.tv_task_desc)
        TextView tv_task_desc;
        @BindView(R.id.ll_root)
        LinearLayout ll_root;


        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }
    }
}
