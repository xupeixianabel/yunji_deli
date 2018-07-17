package com.yunji.deliveryman.mvpV;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.base.BaseMvpLifeActivity;
import com.yunji.deliveryman.core.RobotStateService;
import com.yunji.deliveryman.mvpP.EstopPresent;
import com.yunji.deliveryman.mvpP.TaskPresent;
import com.yunji.deliveryman.widget.WaveView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class EstopActivity extends BaseMvpLifeActivity<EstopPresent> {

    @Override
    protected void onResume() {
        super.onResume();
        RobotStateService.getInstance().setRobotStateCallBack(getP());
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        getP().ifHasTask();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_estop;
    }

    @Override
    public EstopPresent newP() {
        return new EstopPresent();
    }






}
