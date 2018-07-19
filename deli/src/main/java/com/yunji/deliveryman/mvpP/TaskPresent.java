package com.yunji.deliveryman.mvpP;

import android.app.Dialog;
import android.view.View;

import com.orhanobut.logger.Logger;
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
                dismissDialog();
                break;
            case "DELIVERY_END"://任务结束
                taskState = MyConst.DELIVERY_END;
                if (!ifFinish() && !getV().taskChanged) {
                    showDialogTaskEnd(stateBean);
                    cancelTask();
                }
                break;
            case "ACTION_CANCELED"://任务取消
                taskState = MyConst.ACTION_CANCELED;
                break;
            case "ACTION_ABNORMAL"://任务失败
                taskState = MyConst.ACTION_ABNORMAL;
                break;
            case "BATTERY_CHARGING"://充电中
                taskState = MyConst.BATTERY_CHARGING;
                ifFinish();
                break;


        }
    }

    public boolean ifFinish() {
        if (finish || getV().isFinishing()){
            return true;
        }else if (MyConst.tasks == null || MyConst.tasks.size() == 0) {
            cancelTask();
            getV().finish();
            return true;
        }
        return false;
    }


    private void showDialogTaskEnd(YJDeliTaskStateBean stateBean) {
        if (stateBean.getData().getTarget().equals("E")) {
            SpeechUtil.getInstance().speaking(getV().getResources().getString(R.string.arrvive_kitcken));
            cancelTask();
            MyConst.tasks.clear();
            getV().finish();
            return;
        }


        if (dialogTaskEnd == null || !dialogTaskEnd.isShowing()) {
            TaskBean task = MyConst.tasks.get(0);
            speak(task);
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

            final int finalPosition = position;
            dialogTaskEnd = DialogUtil.quCanDialog(getV(), getV().layerBeans, finalPosition, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        getV().layerBeans.get(finalPosition).setDeliveryState(2);
                        MyConst.tasks.remove(0);
                        startTask(false, true);
                        dismissDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!ifFinish()) {
                SpeechUtil.getInstance().speaking(speakStr);
                getV().handler.postDelayed(this, 30000);
            }
        }
    };

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
        SpeechUtil.getInstance().speaking(speakStr);
        getV().handler.postDelayed(runnable, 30000);
    }


    public void startTask(boolean first, boolean ifSpeak) {
        if (!ifFinish()) {
            getV().changeDistrictImage(MyConst.tasks.get(0).getDistrictPosition());
            getV().handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!ifFinish()) {
                        getV().taskChanged=false;
                        taskNoback(MyConst.tasks.get(0).getDistrict());
                    }
                }
            }, 2000);

            if (ifSpeak) {
                String spkStr = "";
                String district= MyConst.tasks.get(0).getDistrict();
                if("E".equals(district)){
                    spkStr =getV().getResources().getString(R.string.go_kitchen);
                }else   if (first) {
                    spkStr = String.format(getV().getResources().getString(R.string.go_delivery_first),district);
                } else {
                    spkStr = String.format(getV().getResources().getString(R.string.go_delivery),district);
                }
                SpeechUtil.getInstance().speaking(spkStr);
            }
        }


    }

    private void taskNoback(String marker) {
        DeliApplication.yunjiApiDeli.taskNoBack(marker, new YunjiCallBack<YJDeliTaskNoback>() {
            @Override
            public void onError(String s) {
                Logger.e("task noback fail");
                startTask(false, false);
            }

            @Override
            public void onSuccess(YJDeliTaskNoback yjDeliTaskNoback) {
                Logger.e("task noback success");
            }
        });
    }
    private void cancelTask(){
        DeliApplication.yunjiApiDeli.cancelTask(new YunjiCallBack<String>() {
            @Override
            public void onError(String s) {
                DeliApplication.yunjiApiDeli.cancelTask(null);
            }
            @Override
            public void onSuccess(String s) {
            }
        });
    }


    private boolean finish;
    @Override
    public void detachV() {
        finish=true;
        dismissDialog();
        super.detachV();

    }
    public void dismissDialog(){
        if (dialogTaskEnd != null) {
            dialogTaskEnd.dismiss();
            dialogTaskEnd = null;
            getV().handler.removeCallbacks(runnable);
        }
    }
}
