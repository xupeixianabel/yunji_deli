package com.yunji.deliveryman;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import com.yunji.handler.YunJiApiFactory;
import com.yunji.handler.api.IYunJiAPIManger;
import com.yunji.handler.log.CommonLog;

/**
 * @author lixuanting
 * @date 2018/3/15 上午9:54
 */

public class DeliApplication extends Application {
    private static DeliApplication mApplication;

    public static DeliApplication getmApplication() {
        return mApplication;
    }

    public static IYunJiAPIManger apiManger;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        Logger.addLogAdapter(new AndroidLogAdapter());
        CrashReport.initCrashReport(getApplicationContext(), "fc57333323", false);
//        initDeliApi();
    }





    private void initDeliApi() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Logger.d("Init api manager");
                try {
                    apiManger = YunJiApiFactory.createYunJiApi(mApplication);
                    Logger.d(apiManger.toString());
                } catch (Exception e) {
                    Logger.d(e.getMessage());
                }
            }
        }.start();
    }

}
