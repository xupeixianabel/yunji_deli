package com.yunji.deliveryman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.base.SimpleRecAdapter;
import com.yunji.deliveryman.bean.MainDistrictBean;
import com.yunji.deliveryman.bean.SettingBean;
import com.yunji.deliveryman.util.KnifeKit;

import butterknife.BindView;

public class SettingAdapter extends SimpleRecAdapter<SettingBean, SettingAdapter.ViewHolder> {
    public SettingAdapter(Context context) {
        super(context);
    }
    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.setting_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.iv_icon.setImageResource(data.get(position).getImageId());
        holder.tv_name.setText(data.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecItemClick().onItemClick(position, data.get(position), 0, null);
            }
        });

     /*   holder.tv_city.setText(data.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRecItemClick() != null) {
                    getRecItemClick().onItemClick(position, data.get(position), TAG_VIEW, null);
                }
            }
        });*/
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_root)
        LinearLayout ll_root;
        @BindView(R.id.iv_icon)
        ImageView iv_icon;
        @BindView(R.id.tv_name)
        TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }
    }
}
