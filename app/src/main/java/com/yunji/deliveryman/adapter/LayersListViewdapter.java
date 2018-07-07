package com.yunji.deliveryman.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yunji.deliveryman.R;
import com.yunji.deliveryman.bean.Layer;
import com.yunji.deliveryman.common.Constants;
import com.yunji.deliveryman.utils.SharedPrefsUtil;
import com.yunji.deliveryman.utils.Util;
import java.util.List;

public class LayersListViewdapter extends BaseAdapter{

	private Activity activity = null;
	List<Layer> itemList;
	Handler mHandler;
	int index;
	/**
	 * 自定义构造方法
	 *
	 * @param activity
	 * @param //List<Layer>
	 */
	public LayersListViewdapter(Activity activity, List<Layer> itemList,Handler mHandler) {
		this.activity = activity;
		this.itemList = itemList;
		this.mHandler = mHandler;
	}
	 
	public void setTabButton(int index){
		this.index =index;
	}
	@Override
	public int getCount() {
		return itemList.size();
	}
	
	@Override
	public Object getItem(int position) {
		return itemList.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		 
		ViewHolder holder = null;
		if (convertView== null) {
			holder = new ViewHolder();
			// 下拉项布局
			LayoutInflater inflater =  (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			convertView = inflater.inflate(R.layout.listview_item_layer, null);
			holder.rl_item_bg = (RelativeLayout) convertView.findViewById(R.id.rl_item_bg);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_name2 = (TextView) convertView.findViewById(R.id.tv_name2);
			holder.tv_unselectPoint = (TextView) convertView.findViewById(R.id.tv_unselectPoint);
			holder.tv_selectPoint = (TextView) convertView.findViewById(R.id.tv_selectPoint);
			holder.v_line1 =convertView.findViewById(R.id.v_line1);


			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();			
		}
		holder.tv_name.setText(itemList.get(position).name);
		holder.tv_name2.setText(itemList.get(position).name2);
		int color1 = activity.getResources().getColor(R.color.theme_green);
		int color2 = activity.getResources().getColor(R.color.theme_green);

		int colorWhite = activity.getResources().getColor(R.color.white);
		 if(position==index){
			 holder.rl_item_bg.setBackgroundResource(R.drawable.ic_main_item);
			 holder.tv_name.setTextColor(color1);
			 holder.tv_selectPoint.setTextColor(color1);
			 holder.tv_unselectPoint.setTextColor(color1);
			 holder.v_line1.setVisibility(View.INVISIBLE);
		 }else{
			 holder.rl_item_bg.setBackgroundResource(R.color.translucent);
			 holder.tv_name.setTextColor(color2);
			 holder.tv_selectPoint.setTextColor(colorWhite);
			 holder.tv_unselectPoint.setTextColor(colorWhite);
			 holder.v_line1.setVisibility(View.VISIBLE);
		 }

		String selectName = SharedPrefsUtil.get(activity,position+ Constants.TASK_TABLE_NAME,"");
		if(!TextUtils.isEmpty(selectName)){
			holder.tv_selectPoint.setText(selectName);
			holder.tv_unselectPoint.setVisibility(View.GONE);
			holder.tv_selectPoint.setVisibility(View.VISIBLE);

		}else{
			selectName = "输入桌号";
			holder.tv_selectPoint.setText(selectName);
			holder.tv_unselectPoint.setText(selectName);
			holder.tv_unselectPoint.setVisibility(View.VISIBLE);
			holder.tv_selectPoint.setVisibility(View.GONE);
		}
		boolean result =	Util.isContainsChinese(selectName);
		if(result){
			holder.tv_selectPoint.setTextSize(32);
			holder.tv_unselectPoint.setTextSize(32);
		}else{
			holder.tv_selectPoint.setTextSize(40);
			holder.tv_unselectPoint.setTextSize(40);
		}

		return convertView;
	}

	class ViewHolder {
		RelativeLayout rl_item_bg;

		TextView tv_name;
		TextView tv_name2;
		TextView tv_unselectPoint;
		TextView tv_selectPoint;
		View v_line1;
	}
}

