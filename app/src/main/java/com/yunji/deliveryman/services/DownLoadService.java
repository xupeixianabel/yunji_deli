package com.yunji.deliveryman.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.common.Constants;
import com.yunji.deliveryman.other.log.FL;
import com.yunji.deliveryman.utils.MyLogcat;
import com.yunji.deliveryman.utils.NotifyUtil;
import com.yunji.deliveryman.utils.PublicUtils;

import java.io.File;

public class DownLoadService extends Service {
    private String download_url;
    private int requestCode = (int) SystemClock.uptimeMillis();
    private File mFile;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        download_url = intent.getStringExtra("download_url");

        int lastIndexOf = download_url.lastIndexOf("/");
        String apkName = download_url.substring(lastIndexOf + 1, download_url.length());
        MyLogcat.showLog("onStartCommand  download_url = " + download_url);
        MyLogcat.showLog("onStartCommand  lastIndexOf = " + lastIndexOf);
        MyLogcat.showLog("onStartCommand  apkName = " + apkName);
        FL.e(Constants.show_log, "onStartCommand  download_url = " + download_url);
        FL.e(Constants.show_log, "onStartCommand  lastIndexOf = " + lastIndexOf);
        FL.e(Constants.show_log, "onStartCommand  apkName = " + apkName);

        String savePath = Constants.LOCAL_APK_PATH + apkName;
        mFile = new File(savePath);
        PublicUtils.setFileEnable(savePath);//修改文件读写权限

        MyLogcat.showLog("DownLoadService  onStartCommand = 执行");
        FL.e(Constants.show_log, "DownLoadService  onStartCommand = 执行");
        ////设置想要展示的数据内容
        Intent intent_noti = new Intent();
        intent_noti.setAction(Intent.ACTION_VIEW);
        PendingIntent rightPendIntent = PendingIntent.getActivity(this,
                requestCode, intent_noti, PendingIntent.FLAG_UPDATE_CURRENT);
        int smallIcon = R.drawable.ic_launcher_background;
        String ticker = "正在更新软件";

        //实例化工具类，并且调用接口
        NotifyUtil notify7 = new NotifyUtil(this, 7);
        notify7.notify_progress(rightPendIntent, smallIcon, ticker, "在线升级程序", "下载中..",
                false, false, false, download_url, savePath, new NotifyUtil.DownLoadListener() {
                    @Override
                    public void OnSuccess(File file) {
                        MyLogcat.showLog("notify_progress  OnSuccess");
                        FL.e(Constants.show_log, "notify_progress  OnSuccess");
                        mFile = file;
                        DownLoadService.this.stopSelf();
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        MyLogcat.showLog("notify_progress  onFailure");
                        FL.e(Constants.show_log, "notify_progress  onFailure");
                    }
                });
        return super.onStartCommand(intent, flags, startId);
    }
}
