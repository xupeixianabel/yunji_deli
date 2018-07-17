package com.yunji.deliveryman.mvpP;

import com.yunji.deliveryman.MyConst;
import com.yunji.deliveryman.core.RobotStateService;
import com.yunji.deliveryman.mvpI.XPresent;
import com.yunji.deliveryman.mvpV.EstopActivity;
import com.yunji.deliveryman.util.DialogUtil;
import com.yunji.sdk.bean.deli.YJDeliTaskStateBean;

public class EstopPresent extends XPresent<EstopActivity> implements RobotStateService.RobotStateCallBack {


    @Override
    public void robotState(YJDeliTaskStateBean status) {
        if (status != null && status.getCode() == 0) {
            if (status.getData() != null) {
                if (!status.getData().isEstop()) {
                    getV().finish();
                }

            }
        }
    }
    public void ifHasTask() {
        if (MyConst.tasks!=null && MyConst.tasks.size()>0){
            DialogUtil.cancelTaskDialog(getV());
        }
    }
}
