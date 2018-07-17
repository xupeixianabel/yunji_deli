package com.yunji.deliveryman.mvpP;

import com.yunji.deliveryman.bean.SettingBean;
import com.yunji.deliveryman.core.RobotStateService;
import com.yunji.deliveryman.mvpI.XPresent;
import com.yunji.deliveryman.mvpM.SettingModel;
import com.yunji.deliveryman.mvpV.MainActivity;
import com.yunji.sdk.bean.deli.YJDeliTaskStateBean;

import java.util.List;

public class SettingPresent extends XPresent<MainActivity> implements RobotStateService.RobotStateCallBack{
    SettingModel mainModel;
    public SettingPresent(){
        super();
        mainModel=new SettingModel();
        mainModel.setPresent(this);
    }
    @Override
    public void detachV() {
        super.detachV();
        mainModel.detachP();
        mainModel=null;
    }


    public List<SettingBean> getSettingData() {
        return  mainModel.getSettingData(getV());
    }

    @Override
    public void robotState(YJDeliTaskStateBean stateBean) {

    }
}
