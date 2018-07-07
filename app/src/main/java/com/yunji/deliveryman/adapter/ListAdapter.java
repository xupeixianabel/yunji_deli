package com.yunji.deliveryman.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.bean.FoodTable;
import com.yunji.deliveryman.common.Constants;
import com.yunji.deliveryman.utils.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YJCN503A0068 on 2018/3/14.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private Context mContext;
    public OnItemClickListener mOnItemClickListener = null;
    public OnLongItemClickListener mOnLongItemClickListener = null;

    private List<FoodTable> mTableList;
    private List<TextView> mFirstText = new ArrayList<>();
    private List<LinearLayout> mFirstBG = new ArrayList<>();
    private int index;


    public ListAdapter(Context context, List<FoodTable> mTableList) {
        this.mContext = context;
        this.mTableList = mTableList;
    }

    public void setItemPos(int index) {
        this.index = index;
    }

    public void setFoodList(List<FoodTable> mTableList) {
        this.mTableList = mTableList;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        holder.mTextF.setText("F" + (position + 1));
        holder.mTextCan.setText(mTableList.get(position).layerTableName);

        if (index == position) {
            holder.mTextLetter.setTextSize(42);
            holder.mTextF.setTextColor(Color.parseColor("#60c030"));
            holder.mTextCan.setTextColor(Color.parseColor("#60c030"));
            holder.mTextLetter.setTextColor(Color.parseColor("#60c030"));
            String mTableName = SharedPrefsUtil.get(mContext, position + Constants.TASK_TABLE_NAME, "");
            if (!TextUtils.isEmpty(mTableName)) {
                holder.mTextLetter.setText(mTableName);
            } else {
                holder.mTextLetter.setTextSize(24);
                holder.mTextLetter.setText("添加区域");
            }
            holder.mListBg.setBackgroundResource(R.drawable.list_checked);
        } else {
            String mTableName = SharedPrefsUtil.get(mContext, position + Constants.TASK_TABLE_NAME, "");
            if (!TextUtils.isEmpty(mTableName)) {
                holder.mTextLetter.setTextSize(42);
                holder.mTextLetter.setText(mTableName);
                holder.mTextF.setTextColor(Color.parseColor("#60c030"));
                holder.mTextCan.setTextColor(Color.parseColor("#80414141"));
                holder.mTextLetter.setTextColor(Color.parseColor("#60c030"));
            } else {
                holder.mTextLetter.setTextSize(24);
                holder.mTextLetter.setText("点击选择");
                holder.mTextF.setTextColor(Color.parseColor("#60c030"));
                holder.mTextCan.setTextColor(Color.parseColor("#80414141"));
                holder.mTextLetter.setTextColor(Color.parseColor("#60c030"));
            }
            holder.mListBg.setBackgroundResource(R.drawable.list_unchecked);
        }
        String mTableName = SharedPrefsUtil.get(mContext, position + Constants.TASK_TABLE_NAME, "");

    }

    public List<TextView> getAllText() {
        return mFirstText;
    }

    public List<LinearLayout> getAllBG() {
        return mFirstBG;
    }

    @Override
    public int getItemCount() {
        return mTableList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextF, mTextCan, mTextLetter;
        private LinearLayout mListBg;

        public ListViewHolder(View itemView) {
            super(itemView);
            mTextF = (TextView) itemView.findViewById(R.id.textF);
            mTextCan = (TextView) itemView.findViewById(R.id.textList);
            mTextLetter = (TextView) itemView.findViewById(R.id.letterList);
            mListBg = (LinearLayout) itemView.findViewById(R.id.list_bg);
            mFirstText.add(mTextLetter);
            mFirstBG.add(mListBg);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, mTextLetter, mListBg, getAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnLongItemClickListener != null) {
                        mOnLongItemClickListener.onLongItemClick(v, getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }

    public interface OnLongItemClickListener {
        void onLongItemClick(View view, int position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, TextView mTextLetter, LinearLayout listBg, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnLongItemClickListener(OnLongItemClickListener listener) {
        this.mOnLongItemClickListener = listener;
    }
}
