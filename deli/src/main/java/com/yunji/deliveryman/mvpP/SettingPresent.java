package com.yunji.deliveryman.mvpP;

import android.app.Dialog;
import android.os.Handler;
import android.view.View;

import com.google.gson.Gson;
import com.yunji.deliveryman.DeliApplication;
import com.yunji.deliveryman.R;
import com.yunji.deliveryman.bean.SettingBean;
import com.yunji.deliveryman.core.RobotStateService;
import com.yunji.deliveryman.mvpI.XPresent;
import com.yunji.deliveryman.mvpM.SettingModel;
import com.yunji.deliveryman.mvpV.SettingActivity;
import com.yunji.deliveryman.util.ActivityUtil;
import com.yunji.deliveryman.util.DialogUtil;
import com.yunji.sdk.api.YunjiCallBack;
import com.yunji.sdk.bean.deli.YJDeliTaskNoback;
import com.yunji.sdk.bean.deli.YJDeliTaskStateBean;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SettingPresent extends XPresent<SettingActivity> implements RobotStateService.RobotStateCallBack {
    SettingModel mainModel;
    private boolean showNavibar;
    private Dialog dialog;

    public SettingPresent() {
        super();
        mainModel = new SettingModel();
        mainModel.setPresent(this);
    }

    @Override
    public void detachV() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        mainModel.detachP();
        mainModel = null;
        super.detachV();
    }

    int basePowerPercent;
    int animatePower;

    public List<SettingBean> getSettingData() {
        return mainModel.getSettingData(getV());
    }

    @Override
    public void robotState(YJDeliTaskStateBean stateBean) {
        if (stateBean != null && stateBean.getData() != null) {
            if (getV().stateVisible) {
                getV().setStateText(new Gson().toJson(stateBean));
            }
            basePowerPercent = (int) (stateBean.getData().getPower_percent());


            if (stateBean.getData().getCharge_state() == 1) {
                getV().iv_charging.setVisibility(View.VISIBLE);
                getV().tv_electricity.setText("充电中 " + basePowerPercent + "%");

                if (animatePower < basePowerPercent || animatePower >= 100) {
                    animatePower = basePowerPercent;
                }
                animatePower += 5;
                if (animatePower >= 100) {
                    animatePower = 100;
                }
                getV().horizontalBattery.setPower(animatePower);


            } else {
                if (basePowerPercent + 5 > 100) {
                    getV().horizontalBattery.setPower(100);
                } else {
                    getV().horizontalBattery.setPower(basePowerPercent += 5);
                }

                getV().iv_charging.setVisibility(View.GONE);
                getV().tv_electricity.setText(basePowerPercent + "%");
            }
        }
    }


    public void gotoCharge() {
        DeliApplication.yunjiApiDeli.forceCharge(new YunjiCallBack<String>() {
            @Override
            public void onError(String s) {
            }

            @Override
            public void onSuccess(String s) {
            }
        });
    }

    public void gotokichen() {
        DeliApplication.yunjiApiDeli.taskNoBack("E", new YunjiCallBack<YJDeliTaskNoback>() {
            @Override
            public void onError(String s) {

            }

            @Override
            public void onSuccess(YJDeliTaskNoback yjDeliTaskNoback) {

            }
        });
    }

    public void clearTask() {
        DeliApplication.yunjiApiDeli.cancelTask(new YunjiCallBack<String>() {
            @Override
            public void onError(String s) {

            }

            @Override
            public void onSuccess(String s) {

            }
        });
    }

    public void showHideNavigationBar() {
        showNavibar = !showNavibar;
        ActivityUtil.showDorbar(getV(), showNavibar);
        String hint = "";
        if (showNavibar) {
            hint = getV().getResources().getString(R.string.hide_navi_bar);
        } else {
            hint = getV().getResources().getString(R.string.show_navi_bar);
        }
        getV().settingAdapter.getDataSource().get(3).setName(hint);
        getV().showTs(hint);

    }

    public void voiceSetting() {
        dialog = DialogUtil.voiceSettingDialog(getV());
    }

    public void inputPoint() {

    }

    public void changePwd() {
    }

    public void update() {
    }
}
