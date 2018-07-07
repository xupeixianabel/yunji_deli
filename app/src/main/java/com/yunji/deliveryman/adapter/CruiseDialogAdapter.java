package com.yunji.deliveryman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.base.SimpleRecAdapter;


public class CruiseDialogAdapter extends SimpleRecAdapter<String,CruiseDialogAdapter.ViewHolderNormal> {
    private int type;
    public int checkedPosition0;
    public int checkedPosition1;
    public void setType(int type) {
        this.type = type;
    }

    public String getChoosedStr(){
        if (type==0){
            return data.get(checkedPosition0);
        }else  {
            return data.get(checkedPosition1);
        }
    }


    public CruiseDialogAdapter(Context context) {
        super(context);
    }
    @Override
    public int getLayoutId() {
        return R.layout.item_cruise_dialog;
    }

    @Override
    public void onBindViewHolder(ViewHolderNormal holder,final int position) {
        if (type==0){
            if (position==checkedPosition0 ){
                holder.iv_checked.setVisibility(View.VISIBLE);
            }else {
                holder.iv_checked.setVisibility(View.INVISIBLE);
            }
        }else {
            if (position==checkedPosition1 ){
                holder.iv_checked.setVisibility(View.VISIBLE);
            }else {
                holder.iv_checked.setVisibility(View.INVISIBLE);
            }
        }

        holder.tv_room_voice.setText(data.get(position));
        holder.ll_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type==0){
                    checkedPosition0=position;
                }else {
                    checkedPosition1=position;
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public CruiseDialogAdapter.ViewHolderNormal newViewHolder(View itemView) {
        return new ViewHolderNormal(itemView);
    }

    public static class ViewHolderNormal extends RecyclerView.ViewHolder{
        TextView tv_room_voice;
        LinearLayout ll_root;
        ImageView iv_checked;
        ViewHolderNormal(View view){
            super(view);
            tv_room_voice=view.findViewById(R.id.tv_room_voice);
            ll_root=view.findViewById(R.id.ll_root);
            iv_checked=view.findViewById(R.id.iv_checked);
        }
    }

}
