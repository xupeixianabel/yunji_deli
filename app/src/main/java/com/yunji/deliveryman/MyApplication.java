package com.yunji.deliveryman;


import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.tencent.bugly.crashreport.CrashReport;
import com.yunji.deliveryman.common.CommonConstant;
import com.yunji.deliveryman.common.Constants;
import com.yunji.deliveryman.core.HttpService;
import com.yunji.deliveryman.other.exceptions.CrashHandler;
import com.yunji.deliveryman.other.log.FL;
import com.yunji.deliveryman.other.log.FLConfig;
import com.yunji.deliveryman.other.log.FLConst;
import com.yunji.deliveryman.utils.MyLogcat;
import com.yunji.deliveryman.utils.MyToast;
import com.yunji.deliveryman.utils.PublicUtils;
import com.yunji.deliveryman.utils.RegistryUtil;

import java.io.File;

public class MyApplication extends Application {

    public HttpService.HttpHandler httpHandler;
    public static MyApplication myApp;
    public HttpService.HttpBinder myBinder;
    private RegistryUtil mRegistryUtil;

    public static MyApplication getInstance() {

        return myApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //保存日志到文件
        FL.init(new FLConfig.Builder(this)
                .logToFile(true)
                .dir(new File(Environment.getExternalStorageDirectory(), getPackageName()))
                .retentionPolicy(FLConst.RetentionPolicy.FILE_COUNT)
                .build());
        FL.setEnabled(true);

        //异常保存文件
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        FL.e(Constants.show_log, "Application-->onCreate()");

        myApp = this;
//        CrashReport.initCrashReport(this, "3674ca082d", false);

        mRegistryUtil = RegistryUtil.init(this);
        /**
         * 刷主主板，机器人id
         */
        /***************   一定不要打开               ***********/
    /*    MyToast.showToast(getApplicationContext(),"测试出厂中");
        mRegistryUtil.setProp(CommonConstant.PropertyKey.productId,"YH01SH011818000033");
        MyToast.showToast(getApplicationContext(),"测试中");*/
        /***************   一定不要打开               ***********/



     /*   Constants.robotId = mRegistryUtil.getProp(CommonConstant.PropertyKey.productId);
        if (TextUtils.isEmpty(Constants.robotId)) {
            Toast.makeText(getApplicationContext(), "机器人id为空", Toast.LENGTH_LONG).show();
            return;
        }
        MyLogcat.showLog("Application-->Constants.robotId= " + Constants.robotId);
        FL.e(Constants.show_log, "Application-->Constants.robotId= " + Constants.robotId);

        Constants.topicNotification = PublicUtils.getNewTopic(Constants.topicNotification, Constants.robotId);
        Constants.topicCommand = PublicUtils.getNewTopic(Constants.topicCommand, Constants.robotId);
        Constants.topicBarricade = PublicUtils.getNewTopic(Constants.topicBarricade, Constants.robotId);*/
    }
}
