package com.yunji.deliveryman.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.common.Constants;
import com.yunji.deliveryman.other.log.FL;
import com.yunji.deliveryman.utils.MyLogcat;
import com.yunji.deliveryman.utils.MyToast;
import com.yunji.deliveryman.utils.SharedPrefsUtil;
import com.yunji.deliveryman.view.IPEditText;


public class IpSettingActivity extends Activity implements OnClickListener {


	private IPEditText ipet;


	private Button bt_save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ipsetting);


		ipet = (IPEditText) findViewById(R.id.ipet);

		bt_save = (Button) findViewById(R.id.bt_save);
		bt_save.setOnClickListener(this);
	}


	/**
	 * Button点击
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		keyBoardCancle();
		switch (v.getId()) 
		{

			case R.id.bt_save:
				String ip0 = ipet.getText();


				 if(TextUtils.isEmpty(ip0)){
					 MyToast.showToast(getApplicationContext(),"请输入正确的ip");
					 return;
				 }
				 String ipStr= ip0;
				 MyLogcat.showLog("新的ip="+ipStr);
				FL.e(Constants.show_log, "新的ip="+ipStr);
				 SharedPrefsUtil.put(getApplicationContext(),"ip_setting",ipStr);
				MyToast.showToast(getApplicationContext(),"保存");

				 finish();
				break;

		}
	}
	//强制隐藏软键盘
	public void keyBoardCancle() {
		View view = getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
}
