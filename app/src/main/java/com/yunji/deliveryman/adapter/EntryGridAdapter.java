package com.yunji.deliveryman.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yunji.deliveryman.R;
import com.yunji.deliveryman.db.TablePoint;
import java.util.List;


public class EntryGridAdapter extends BaseAdapter{


	private Context activity = null;
	List<TablePoint> itemList;

	/**
	 * 自定义构造方法
	 *
	 * @param activity
	 * @param //list
	 */
	public EntryGridAdapter(Context activity, List<TablePoint> itemList ) {
		this.activity = activity;
		this.itemList = itemList;

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
			convertView = inflater.inflate(R.layout.gridview_item_entry, null);
			holder.rl_layout = (RelativeLayout) convertView.findViewById(R.id.rl_layout);

			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.tv_pointSate = (TextView) convertView.findViewById(R.id.tv_pointSate);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_point = (TextView) convertView.findViewById(R.id.tv_point);
			convertView.setTag(holder);			
		} else {
			holder = (ViewHolder) convertView.getTag();			
		}
		int colorBlue = activity.getResources().getColor(R.color.blue);
		int colorGreen = activity.getResources().getColor(R.color.green);
		int pointType =itemList.get(position).pointType;
		switch (pointType){
			case 0:
				holder.tv_pointSate.setText("餐桌");
				holder.rl_layout.setBackgroundColor(colorGreen);
			break;
			case 1:
				holder.tv_pointSate.setText("厨房");
				holder.rl_layout.setBackgroundColor(colorBlue);
				break;
			case 2:
				holder.tv_pointSate.setText("充电桩");
				holder.rl_layout.setBackgroundColor(colorBlue);
				break;
		}
		holder.tv_name.setText(itemList.get(position).tableName);
		holder.tv_point.setText(itemList.get(position).tablePoint);

		return convertView;
	}
	class ViewHolder {
		RelativeLayout rl_layout;
		ImageView iv_icon;
		TextView tv_pointSate;
		TextView tv_name;
		TextView tv_point;
	}
}

