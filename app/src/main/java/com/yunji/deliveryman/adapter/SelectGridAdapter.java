package com.yunji.deliveryman.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.yunji.deliveryman.R;
import com.yunji.deliveryman.common.Constants;
import com.yunji.deliveryman.db.TablePoint;
import com.yunji.deliveryman.utils.SharedPrefsUtil;


public class SelectGridAdapter extends BaseAdapter{
	
	 
	private Context activity = null;
	List<TablePoint> itemList;
	private int mLayerIndex ;

	/**
	 * 自定义构造方法
	 * 
	 * @param activity
	 * @param //list
	 */
	public SelectGridAdapter(Context activity, List<TablePoint> itemList ) {
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
	 public void  setLayerIndex(int mLayerIndex){
		 this.mLayerIndex = mLayerIndex;

	 }
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		 
		ViewHolder holder = null;
		if (convertView== null) {
			holder = new ViewHolder();
			// 下拉项布局
			LayoutInflater inflater =  (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			convertView = inflater.inflate(R.layout.gridview_item_select, null);
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);			
		} else {
			holder = (ViewHolder) convertView.getTag();			
		}
		int colorRed = activity.getResources().getColor(R.color.theme_green);
		int colorBla = activity.getResources().getColor(R.color.theme_green);

		String mTableName = SharedPrefsUtil.get(activity,mLayerIndex+Constants.TASK_TABLE_NAME,"");
		if(!TextUtils.isEmpty(mTableName) && mTableName.equals(itemList.get(position).tableName)){
			holder.iv_icon.setVisibility(View.VISIBLE);
			holder.tv_name.setTextColor(colorRed);
		}else{
			holder.tv_name.setTextColor(colorBla);
			holder.iv_icon.setVisibility(View.GONE);
		}
		holder.tv_name.setText(itemList.get(position).tableName);

		return convertView;
	}
	class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
	}
}

