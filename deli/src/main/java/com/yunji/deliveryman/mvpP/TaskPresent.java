package com.yunji.deliveryman.mvpP;

import android.app.Dialog;
import android.os.Message;
import android.view.View;

import com.yunji.deliveryman.DeliApplication;
import com.yunji.deliveryman.MyConst;
import com.yunji.deliveryman.R;
import com.yunji.deliveryman.bean.TaskBean;
import com.yunji.deliveryman.core.RobotStateService;
import com.yunji.deliveryman.mvpI.XPresent;
import com.yunji.deliveryman.mvpV.TaskActivity;
import com.yunji.deliveryman.util.DialogUtil;
import com.yunji.deliveryman.util.SpeechUtil;
import com.yunji.sdk.api.YunjiCallBack;
import com.yunji.sdk.bean.deli.YJDeliTaskNoback;
import com.yunji.sdk.bean.deli.YJDeliTaskStateBean;

public class TaskPresent extends XPresent<TaskActivity> implements RobotStateService.RobotStateCallBack {
    private int taskState;
    Dialog dialogTaskEnd;
    public String speakStr;

    @Override
    public void robotState(YJDeliTaskStateBean stateBean) {
        switch (stateBean.getData().getState()) {
            case "DELIVERY_START"://任务开始
                taskState = MyConst.DELIVERY_START;
                break;
            case "DELIVERY_END"://任务结束
                taskState = MyConst.DELIVERY_END;
                DeliApplication.yunjiApiDeli.cancelTask(null);
                showDialogTaskEnd(stateBean);
                break;
            case "ACTION_CANCELED"://任务取消
                taskState = MyConst.ACTION_CANCELED;
                break;
            case "ACTION_ABNORMAL"://任务失败
                taskState = MyConst.ACTION_ABNORMAL;
                break;
            case "BATTERY_CHARGING"://充电中
                taskState = MyConst.BATTERY_CHARGING;
                break;


        }
    }


    private void showDialogTaskEnd(YJDeliTaskStateBean stateBean) {
        if (stateBean.getData().getTarget().equals("E")) {
            SpeechUtil.getInstance().speaking(getV().getResources().getString(R.string.arrvive_kitcken));
            getV().finish();
            return;
        }


        if (dialogTaskEnd == null || !dialogTaskEnd.isShowing()) {
            if (MyConst.tasks != null && MyConst.tasks.size() > 0) {
                TaskBean task = MyConst.tasks.get(0);
                speak(task);
              /*  int position=0;
                String[] districts=getV().getResources().getStringArray(R.array.Points);
                String target=  stateBean.getData().getTarget();
                for (int i=0;i<getV().layerBeans.size();i++){
                    int j=getV().layerBeans.get(i).getDistrictPosition();
                    if (j>=0 && j<districts.length){
                        if (target.equals(districts[j])){
                            position=i;
                            break;
                        }
                    }
                }*/
                int position = 0;
                String layer = task.getLayer();
                if (layer.equals("F1")) {
                    position = 0;
                } else if (layer.equals("F2")) {
                    position = 1;
                } else if (layer.equals("F3")) {
                    position = 2;
                } else if (layer.equals("F4")) {
                    position = 3;
                }

                dialogTaskEnd = DialogUtil.quCanDialog(getV(), getV().layerBeans, position, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyConst.tasks.remove(0);
                        startTask(false);
                        dialogTaskEnd.dismiss();
                        dialogTaskEnd = null;
                    }
                });
            }
        }
    }

    public void speak(TaskBean task) {
        String str1 = task.getDistrict();
        String str2 = task.getLayer();
        if (str2.equals("F1")) {
            str2 = "最上层";
        } else if (str2.equals("F2")) {
            str2 = "第二层";
        } else if (str2.equals("F3")) {
            str2 = "第三层";
        } else if (str2.equals("F4")) {
            str2 = "最下层";
        }
        speakStr = String.format(getV().getResources().getString(R.string.arrvive_marker), str1, str2);
        startSpeak();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            SpeechUtil.getInstance().speaking(speakStr);
            getV().handler.postDelayed(this, 6000);
        }
    };

    private void startSpeak() {
        getV().handler.postDelayed(runnable, 60000);

    }

    public void startTask(boolean first) {
        if (MyConst.tasks != null && MyConst.tasks.size() > 0  &&getV()!=null) {
            getV().changeDistrictImage(MyConst.tasks.get(0).getDistrictPosition());
            getV().handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    taskNoback(MyConst.tasks.get(0).getDistrict());
                }
            }, 2000);
            String spkStr = "";
            if (first) {
                spkStr = String.format(getV().getResources().getString(R.string.go_delivery_first), MyConst.tasks.get(0).getDistrict());
            } else {
                spkStr = String.format(getV().getResources().getString(R.string.go_delivery), MyConst.tasks.get(0).getDistrict());
            }
            SpeechUtil.getInstance().speaking(spkStr);
        }


    }

    private void taskNoback(String marker) {
        DeliApplication.yunjiApiDeli.taskNoBack(new YunjiCallBack<YJDeliTaskNoback>() {
            @Override
            public void onError(String s) {
                getV().handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startTask(false);
                    }
                }, 2000);
            }

            @Override
            public void onSuccess(YJDeliTaskNoback yjDeliTaskNoback) {
//                MyConf.tasks.remove(0);
            }
        }, marker);
    }

    @Override
    public void detachV() {
        if (dialogTaskEnd != null) {
            dialogTaskEnd.dismiss();
            dialogTaskEnd = null;
        }
        getV().handler.removeCallbacks(runnable);
        super.detachV();

    }
}
