package com.yunji.deliveryman.core;

import android.content.Intent;

import com.orhanobut.logger.Logger;
import com.yunji.deliveryman.DeliApplication;
import com.yunji.deliveryman.mvpP.EstopPresent;
import com.yunji.deliveryman.mvpV.EstopActivity;
import com.yunji.deliveryman.util.Kits;
import com.yunji.sdk.api.YunjiCallBack;
import com.yunji.sdk.bean.deli.YJDeliTaskStateBean;

import java.util.Timer;
import java.util.TimerTask;

public class RobotStateService {

    static RobotStateService robotStateService;

    RobotStateCallBack robotStateCallBack;
    private Timer timer;
    private TimerTask timerTask;

    private RobotStateService() {
    }

    public static RobotStateService getInstance() {
        if (robotStateService == null) {
            robotStateService = new RobotStateService();
        }
        return robotStateService;
    }


    public void setRobotStateCallBack(RobotStateCallBack robotStateCallBack) {
        this.robotStateCallBack = robotStateCallBack;
    }

    public void removeStateCallBack(RobotStateCallBack robotStateCallBack){
        if (this.robotStateCallBack==robotStateCallBack){
            this.robotStateCallBack=null;
        }
    }


    public void startTimer() {
        if (timer!=null){
            Logger.e("重复初始化Timer");
            return;
        }
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (Kits.NetWork.checkNetworkIfAvailable(DeliApplication.getmApplication())) {
                    DeliApplication.yunjiApiDeli.getTaskStatus(new YunjiCallBack<YJDeliTaskStateBean>() {
                        @Override
                        public void onError(String s) {
//                            Logger.e(s);
                        }

                        @Override
                        public void onSuccess(YJDeliTaskStateBean status) {
//                            Logger.e(status.getData().getState());
                            if (robotStateCallBack != null) {
                                robotStateCallBack.robotState(status);
                            }
                            if (status.getData().isEstop() && robotStateCallBack!=null && !(robotStateCallBack instanceof EstopPresent)){
                                Intent intent=new Intent(DeliApplication.getmApplication(),EstopActivity.class);
                                DeliApplication.getmApplication().startActivity(intent);
                            }
                        }
                    });
                }
            }
        };
        timer.schedule(timerTask, 6000, 1500);
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }




    public interface RobotStateCallBack {
        void robotState(YJDeliTaskStateBean stateBean);
    }

}
