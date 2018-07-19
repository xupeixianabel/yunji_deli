package com.yunji.deliveryman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        TaskBean bean = data.get(position);
        holder.cb_task.setSelected(bean.isChecked());
        if (bean.getDistrict().equals("E")){
            String task_go_kichen=context.getResources().getString(R.string.task_go_kichen);
            holder.cb_task.setText(task_go_kichen);
        }else {
            String task_delivery=String.format(context.getResources().getString(R.string.task_delivery),bean.getDistrict());
            holder.cb_task.setText(task_delivery);
        }

        holder.cb_task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                data.get(position).setChecked(b);
            }
        });


       /* holder.ll_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.get(position).setChecked(!data.get(position).isChecked());
                holder.tv_check.setSelected(!data.get(position).isChecked());
//                notifyDataSetChanged();
            }
        });*/


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
   /*     @BindView(R.id.tv_check)
        TextView tv_check;
        @BindView(R.id.tv_task_name)
        TextView tv_task_name;
        @BindView(R.id.tv_task_desc)
        TextView tv_task_desc;*/

     /*   @BindView(R.id.tv_task_desc)
        TextView tv_task_desc;*/


        @BindView(R.id.cb_task)
        CheckBox cb_task;


        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }
    }
}
