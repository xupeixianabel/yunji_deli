package com.yunji.deliveryman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.base.RecyclerAdapter;
import com.yunji.deliveryman.bean.CruiseJsonPost;


public class CruiseAdapter extends RecyclerAdapter<CruiseJsonPost.DataBean.TargetsBean,RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_NOMAL = 1;
    public static final int VIEW_TYPE_LAST = 2;
    public CruiseAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == VIEW_TYPE_NOMAL) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cruise, parent, false);
            return new ViewHolderNormal(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cruise_add, parent, false);
            return new ViewHolderAdd(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,final int position) {
        if (holder instanceof ViewHolderNormal){
            ViewHolderNormal viewHolderNormal=(ViewHolderNormal)holder;
            CruiseJsonPost.DataBean.TargetsBean pointCruise=data.get(position);
            viewHolderNormal.tv_point_name.setText(pointCruise.getMarker());
            viewHolderNormal.tv_voice_name.setText(pointCruise.getSound());
            viewHolderNormal.iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getRecItemClick() != null){
                        getRecItemClick().onItemClick(position,null,0,holder);
                    }
                   data.remove(position);
                   notifyDataSetChanged();

                }
            });

        }else if (holder instanceof ViewHolderAdd){
            ViewHolderAdd viewHolderAdd=(ViewHolderAdd)holder;
            viewHolderAdd.ll_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getRecItemClick() != null){
                        getRecItemClick().onItemClick(position,null,1,holder);
                    }
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        return getDataSource().size()==position+1?VIEW_TYPE_LAST:VIEW_TYPE_NOMAL;
    }

    public static class ViewHolderNormal extends RecyclerView.ViewHolder{
        TextView tv_point_name;
        LinearLayout ll_root;
        LinearLayout ll_bottom;
        TextView tv_voice_name;
        ImageView iv_close;
        ViewHolderNormal(View view){
            super(view);
            tv_point_name=view.findViewById(R.id.tv_point_name);
            ll_root=view.findViewById(R.id.ll_root);
            ll_bottom=view.findViewById(R.id.ll_bottom);
            tv_voice_name=view.findViewById(R.id.tv_voice_name);
            iv_close=view.findViewById(R.id.iv_close);

        }
    }

    public static class ViewHolderAdd extends RecyclerView.ViewHolder{
        LinearLayout ll_add;
        ViewHolderAdd(View view){
            super(view);
            ll_add=view.findViewById(R.id.ll_add);
        }
    }
}
