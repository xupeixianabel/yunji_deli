package com.yunji.deliveryman.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.yunji.deliveryman.R;

/**
 * 无网弹窗
 */
public class NoNetDialog{

	private Context context;
	private TextView tv_content;
	private Dialog dialog;
	
	public NoNetDialog(Context context) {
		this.context = context;
		init(null,null);
	}
	public NoNetDialog(Context context,String title,String content) {
		this.context = context;
		init(title,content);
	}
	 
	public void init(String title,String content){
		dialog = new Dialog(context, R.style.dialog_style);
		int width = DipPxUtils.getScreenWidth(context)*2/3;
		LayoutParams lp = new LayoutParams(width, LayoutParams.WRAP_CONTENT);
		View view = View.inflate(context, R.layout.dialog_no_net, null);
		dialog.setContentView(view,lp);
		dialog.setCancelable(false);
		 
		TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
		tv_content = (TextView) dialog.findViewById(R.id.tv_content);
		if(!TextUtils.isEmpty(title)){
			tv_title.setText(title);
		}
		if(!TextUtils.isEmpty(content)){
			tv_content.setText(content);
		}
		Button btn_cancel = (Button) dialog.findViewById(R.id.btn_net_cancel);
		Button btn_setting = (Button) dialog.findViewById(R.id.btn_net_setting);
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		btn_setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 跳转到系统的网络设置界面  
                Intent intent = new Intent();
				context.startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));


                dialog.dismiss();
			}
		});
		dialog.show();
	}

	 
	
}
