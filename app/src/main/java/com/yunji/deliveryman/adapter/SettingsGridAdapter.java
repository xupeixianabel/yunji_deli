package com.yunji.deliveryman.adapter;

import java.util.List;
import android.os.Handler;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yunji.deliveryman.R;
import com.yunji.deliveryman.bean.Mode;
import com.yunji.deliveryman.utils.MyLogcat;

public class SettingsGridAdapter extends BaseAdapter{

	private String  pointStr;
	private Context activity = null;
	List<Mode> itemList;
	int index;
	private Handler mHandle;
	private boolean isShowBar;

	/**
	 * 自定义构造方法
	 * 
	 * @param activity
	 * @param //List<Mode>
	 */
	public SettingsGridAdapter(Context activity, List<Mode> itemList, Handler mHandle) {
		this.activity = activity;
		this.itemList = itemList;
		this.mHandle =mHandle;
	}
	public void setIsShowBar(boolean isShowBar){
		this.isShowBar =isShowBar;
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
			convertView = inflater.inflate(R.layout.settings_gridview_item, null);
			holder.ll_item1 = (LinearLayout) convertView.findViewById(R.id.ll_item1);

			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();			
		}
		if(position==-1){

			holder.ll_item1.setVisibility(View.GONE);

		}else{

			holder.ll_item1.setVisibility(View.VISIBLE);
		}
		holder.iv_icon.setBackgroundResource(itemList.get(position).imgId);
		holder.tv_name.setText(itemList.get(position).name);
		if(position==3){
		 
			if(isShowBar){
				holder.tv_name.setText("隐藏标题");
				holder.iv_icon.setBackgroundResource(itemList.get(position).imgId);
			}else{
				holder.tv_name.setText("显示标题");
				holder.iv_icon.setBackgroundResource(itemList.get(position).imgId);
			}
		}


		/*holder.ll_item1.setOnClickListener(new View.OnClickListener() {


			@Override
			public void onClick(View v) {

				Message msg = new Message();
				msg .what =  Constants.MSG_WHAT_SETTINGS_CLICK_ITEM;
				msg.obj = SettingsGridAdapter.this.pointStr;
				msg.arg1 = position;
				SettingsGridAdapter.this.mHandle.sendMessage(msg);
			}
		});*/


		return convertView;
	}
	class ViewHolder {
		LinearLayout ll_item1;
		ImageView iv_icon;
		TextView tv_name;

	}
}

