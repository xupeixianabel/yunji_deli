package com.yunji.deliveryman;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import com.yunji.sdk.YunjiApiFactory;
import com.yunji.sdk.api.YunjiApiDeliInterface;


public class DeliApplication extends Application {
    private static DeliApplication mApplication;
    public static DeliApplication getmApplication() {
        return mApplication;
    }
    public static YunjiApiDeliInterface yunjiApiDeli;
    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        Logger.addLogAdapter(new AndroidLogAdapter());
        CrashReport.initCrashReport(getApplicationContext(), "fc57333323", false);
        yunjiApiDeli= YunjiApiFactory.getYunjiApiDeli(mApplication);

        SpeechUtility.createUtility(mApplication, SpeechConstant.APPID +"=5b49768b");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
