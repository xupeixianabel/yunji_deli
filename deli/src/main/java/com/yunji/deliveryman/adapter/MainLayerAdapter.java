package com.yunji.deliveryman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.base.SimpleRecAdapter;
import com.yunji.deliveryman.bean.MainLayerBean;
import com.yunji.deliveryman.util.KnifeKit;

import butterknife.BindView;

public class MainLayerAdapter extends SimpleRecAdapter<MainLayerBean, MainLayerAdapter.ViewHolder> {
    public final static int TAG_VIEW = 1;
    private int checkedIndex;
    String[] districtData;

    public void setDistrictData(String[] districts) {
        this.districtData = districts;
    }


    public void setDistrict(int position) {
        data.get(checkedIndex).setHasChoosed(true);
        data.get(checkedIndex).setDistrictPosition(position);
        notifyDataSetChanged();
    }

    public void unSetDistrict() {
        data.get(checkedIndex).setHasChoosed(false);
        data.get(checkedIndex).setDistrictPosition(-1);
        notifyDataSetChanged();
    }

    public void setDistrictAll() {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setHasChoosed(true);
            data.get(i).setDistrictPosition(i);
        }
        notifyDataSetChanged();
    }

    public void unSetDistrictAll() {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setHasChoosed(false);
            data.get(i).setDistrictPosition(-1);
        }
        notifyDataSetChanged();
    }

    public MainLayerAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.main_item_layer;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_layer.setText(data.get(position).getLayer());
        holder.tv_plate.setText(data.get(position).getPlateLayer());

        if (data.get(position).isHasChoosed()) {
            if (districtData != null && districtData.length > position) {
                holder.tv_choosed.setText(districtData[data.get(position).getDistrictPosition()]);
            }
        } else {
            if (checkedIndex == position) {
                holder.tv_choosed.setText(context.getResources().getString(R.string.main_add_zone));
            } else {
                holder.tv_choosed.setText(context.getResources().getString(R.string.main_click_choose));
            }
        }

        if (checkedIndex == position) {
            holder.ll_root.setSelected(true);
            holder.ll_root.setAlpha(1.0f);
            holder.tv_plate.setTextColor(context.getResources().getColor(R.color.frame));

        } else {
            holder.ll_root.setSelected(false);
            holder.ll_root.setAlpha(0.8f);
            holder.tv_plate.setTextColor(context.getResources().getColor(R.color.gray_66));

        }

        if (data.get(position).isHasChoosed()) {
            holder.tv_choosed.setTextSize(30);
        } else {
            holder.tv_choosed.setTextSize(20);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedIndex == position) return;
                checkedIndex = position;
                notifyDataSetChanged();
                if (getRecItemClick() != null) {
                    getRecItemClick().onItemClick(position, data.get(position), TAG_VIEW, null);
                }
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
        @BindView(R.id.tv_layer)
        TextView tv_layer;
        @BindView(R.id.tv_plate)
        TextView tv_plate;
        @BindView(R.id.tv_choosed)
        TextView tv_choosed;
        @BindView(R.id.ll_root)
        LinearLayout ll_root;

        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }
    }
}
