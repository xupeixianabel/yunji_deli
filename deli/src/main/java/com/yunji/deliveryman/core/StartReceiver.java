package com.yunji.deliveryman.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yunji.deliveryman.mvpV.MainActivity;

public class StartReceiver extends BroadcastReceiver {
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
