package com.yunji.deliveryman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yunji.deliveryman.R;
import com.yunji.deliveryman.base.SimpleRecAdapter;
import com.yunji.deliveryman.bean.MainDistrictBean;
import com.yunji.deliveryman.util.KnifeKit;

import butterknife.BindView;

public class MainDistrictAdapter extends SimpleRecAdapter<MainDistrictBean, MainDistrictAdapter.ViewHolder> {
    private int checkedIndex=-1;
    public void setCheckedIndex(int index){
        checkedIndex=index;
        notifyDataSetChanged();
    }

    public MainDistrictAdapter(Context context) {
        super(context);
    }
    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.main_item_district;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final   int position) {
        if (position==checkedIndex){
            holder.iv_district.setImageResource(data.get(position).getCheckedId());
        }else {
            holder.iv_district.setImageResource(data.get(position).getUnCheckedId());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position==checkedIndex){
                    checkedIndex=-1;
                }else {
                    checkedIndex=position;
                }
                notifyDataSetChanged();
                getRecItemClick().onItemClick(position, data.get(position), checkedIndex, null);
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
        @BindView(R.id.iv_district)
        ImageView iv_district;
        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }
    }
}
