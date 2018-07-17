package com.yunji.deliveryman.mvpP;

import com.yunji.deliveryman.core.RobotStateService;
import com.yunji.deliveryman.mvpI.XPresent;
import com.yunji.deliveryman.mvpV.EstopActivity;
import com.yunji.sdk.bean.deli.YJDeliTaskStateBean;

public class EstopPresent extends XPresent<EstopActivity> implements RobotStateService.RobotStateCallBack{


    @Override
    public void robotState(YJDeliTaskStateBean stateBean) {

    }
}
