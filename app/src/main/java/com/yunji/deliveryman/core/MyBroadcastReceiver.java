package com.yunji.deliveryman.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.yunji.deliveryman.ui.MainActivity;
import com.yunji.deliveryman.utils.CommandExe;


public class MyBroadcastReceiver extends BroadcastReceiver {

	private static final String tag = "MyBroadcastReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action=intent.getAction();
		if(action.equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) //Boot Completed
		{

	        Intent i = new Intent(context, MainActivity.class);
	        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(i);

		}

	}

}
