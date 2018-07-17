package com.yunji.deliveryman.mvpP;

import com.google.gson.Gson;
import com.yunji.deliveryman.DeliApplication;
import com.yunji.deliveryman.R;
import com.yunji.deliveryman.bean.SettingBean;
import com.yunji.deliveryman.core.RobotStateService;
import com.yunji.deliveryman.mvpI.XPresent;
import com.yunji.deliveryman.mvpM.SettingModel;
import com.yunji.deliveryman.mvpV.MainActivity;
import com.yunji.deliveryman.mvpV.SettingActivity;
import com.yunji.deliveryman.util.ActivityUtil;
import com.yunji.sdk.api.YunjiCallBack;
import com.yunji.sdk.bean.deli.YJDeliTaskNoback;
import com.yunji.sdk.bean.deli.YJDeliTaskStateBean;

import java.util.List;

public class SettingPresent extends XPresent<SettingActivity> implements RobotStateService.RobotStateCallBack {
    SettingModel mainModel;
    private boolean showNavibar;
    public SettingPresent() {
        super();
        mainModel = new SettingModel();
        mainModel.setPresent(this);
    }

    @Override
    public void detachV() {
        super.detachV();
        mainModel.detachP();
        mainModel = null;
    }


    public List<SettingBean> getSettingData() {
        return mainModel.getSettingData(getV());
    }

    @Override
    public void robotState(YJDeliTaskStateBean stateBean) {
        if (getV().stateVisible){
            getV().setStateText(new Gson().toJson(stateBean));
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
        DeliApplication.yunjiApiDeli.taskNoBack(new YunjiCallBack<YJDeliTaskNoback>() {
            @Override
            public void onError(String s) {

            }
            @Override
            public void onSuccess(YJDeliTaskNoback yjDeliTaskNoback) {

            }
        },"E");
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
        showNavibar=!showNavibar;
        ActivityUtil.showDorbar(getV(),showNavibar);
        String hint="";
        if (showNavibar){
            hint=getV().getResources().getString(R.string.hide_navi_bar);
        }else {
            hint=getV().getResources().getString(R.string.show_navi_bar);
        }
        getV().settingAdapter.getDataSource().get(3).setName(hint);
        getV().showTs(hint);

    }

    public void voiceSetting() {
    }

    public void inputPoint() {
    }

    public void changePwd() {
    }

    public void update() {
    }
}
