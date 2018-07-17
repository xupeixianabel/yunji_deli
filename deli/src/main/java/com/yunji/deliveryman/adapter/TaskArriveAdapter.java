package com.yunji.deliveryman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunji.deliveryman.R;
import com.yunji.deliveryman.base.SimpleRecAdapter;
import com.yunji.deliveryman.bean.MainLayerBean;
import com.yunji.deliveryman.util.KnifeKit;

import butterknife.BindView;

public class TaskArriveAdapter extends SimpleRecAdapter<MainLayerBean, TaskArriveAdapter.ViewHolder> {
    private int checkedIndex;
    private String[] districts;
    TranslateAnimation animation = new TranslateAnimation(0, -5, 0, 0);
    private int[] drawable_take_meal_checked = {R.drawable.take_meal_f1_checked, R.drawable.take_meal_f2_checked
            , R.drawable.take_meal_f3_checked, R.drawable.take_meal_f4_checked};
    private int[] drawable_take_meal_unchecked = {R.drawable.take_meal_f1_unchecked, R.drawable.take_meal_f2_unchecked
            , R.drawable.take_meal_f3_unchecked, R.drawable.take_meal_f4_unchecked};
    public void setChoosedPosition(int position) {
        this.checkedIndex = position;
    }

    public TaskArriveAdapter(Context context) {
        super(context);
        districts = context.getResources().getStringArray(R.array.district);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.task_item_qucan;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_layer.setText(data.get(position).getLayer());

        if (data.get(position).isHasChoosed()) {
            int disPosition = data.get(position).getDistrictPosition();
            if (disPosition >= 0 && disPosition < districts.length) {
                holder.tv_district.setText(districts[disPosition]);
            }

        }else {
            holder.tv_district.setText(context.getResources().getString(R.string.no_data));
        }

        if (checkedIndex==position){
            holder.tv_state.setText(context.getResources().getString(R.string.please_take_meal));
            holder.iv_gig.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(R.drawable.loading_new)
                    .into(holder.iv_gig);
            holder.ll_root.setBackgroundResource(R.drawable.take_meal_checked);
            holder.ll_layer.setBackgroundResource(drawable_take_meal_checked[position]);

            holder.tv_layer.setTextSize(context.getResources().getDimension(R.dimen.sp_10));
            holder.tv_district.setTextSize(context.getResources().getDimension(R.dimen.sp_10));
            holder.tv_state.setTextSize(context.getResources().getDimension(R.dimen.sp_10));

            holder.tv_layer.setTextColor(context.getResources().getColor(R.color.theme_green));
            holder.tv_district.setTextColor(context.getResources().getColor(R.color.theme_green));
            holder.tv_state.setTextColor(context.getResources().getColor(R.color.theme_green));
            startAnimation(holder.ll_root);

        }else {
            holder.iv_gig.setImageResource(0);
            holder.ll_root.setBackgroundResource(0);
            holder.tv_layer.setTextSize(context.getResources().getDimension(R.dimen.sp_6));
            holder.tv_district.setTextSize(context.getResources().getDimension(R.dimen.sp_6));
            holder.tv_state.setTextSize(context.getResources().getDimension(R.dimen.sp_6));

            holder.iv_gig.setVisibility(View.INVISIBLE);
            if (data.get(position).isHasChoosed()){
                if (position<checkedIndex){
                    holder.tv_state.setText(context.getResources().getString(R.string.state_complete));
                }else {
                    holder.tv_state.setText(context.getResources().getString(R.string.state_not_arrive));
                }

                holder.tv_layer.setTextColor(context.getResources().getColor(R.color.theme_green));
                holder.tv_district.setTextColor(context.getResources().getColor(R.color.theme_green));
                holder.tv_state.setTextColor(context.getResources().getColor(R.color.theme_green));

                holder.ll_layer.setBackgroundResource(drawable_take_meal_checked[position]);
            }else {
                holder.tv_state.setText(context.getResources().getString(R.string.state_not_pull));
                holder.tv_layer.setTextColor(context.getResources().getColor(R.color.gray_99));
                holder.tv_district.setTextColor(context.getResources().getColor(R.color.gray_99));
                holder.tv_state.setTextColor(context.getResources().getColor(R.color.gray_99));

                holder.ll_layer.setBackgroundResource(drawable_take_meal_unchecked[position]);
            }
        }
    }

    private void startAnimation(LinearLayout ll_root) {
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(100);
        animation.setRepeatCount(Integer.MAX_VALUE);
        animation.setRepeatMode(Animation.REVERSE);
        ll_root.startAnimation(animation);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_root)
        LinearLayout ll_root;
        @BindView(R.id.ll_layer)
        LinearLayout ll_layer;


        @BindView(R.id.tv_layer)
        TextView tv_layer;
        @BindView(R.id.tv_district)
        TextView tv_district;
        @BindView(R.id.tv_state)
        TextView tv_state;
        @BindView(R.id.iv_gig)
        ImageView iv_gig;

        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }
    }
}
