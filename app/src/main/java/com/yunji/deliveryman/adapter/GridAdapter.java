package com.yunji.deliveryman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.bean.FoodTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YJCN503A0068 on 2018/3/16.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {

    private Context mContext;
    public OnItemClickListener mOnItemClickListener = null;
    public OnLongItemClickListener mOnLongItemClickListener = null;
    private int[] drawableId_unchecked = {R.drawable.a_unchecked, R.drawable.b_unchecked, R.drawable.c_unchecked
            , R.drawable.d_unchecked, R.drawable.all_unchecked};
    private int[] drawableId_checked = {R.drawable.a_checked, R.drawable.b_checked, R.drawable.c_checked
            , R.drawable.d_checked, R.drawable.all_checked};

    private List<ImageView> mFirstImage = new ArrayList<>();
    int index = -1;
    public Boolean selectAll = false;

    public GridAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_grid, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        if (index == position) {
            holder.mImageViewGrid.setImageResource(drawableId_checked[position]);
        } else {
            holder.mImageViewGrid.setImageResource(drawableId_unchecked[position]);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public void setItemPos(int index) {
        this.index = index;
        selectAll = index == 4;

    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageViewGrid;

        public GridViewHolder(View itemView) {
            super(itemView);
            mImageViewGrid = (ImageView) itemView.findViewById(R.id.imageGrid);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, mImageViewGrid, getAdapterPosition());
                    }
                }
            });
            mFirstImage.add(mImageViewGrid);
        }
    }

    public interface OnLongItemClickListener {
        void onLongItemClick(View view, int position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ImageView imageView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnLongItemClickListener(OnLongItemClickListener listener) {
        this.mOnLongItemClickListener = listener;
    }
}
