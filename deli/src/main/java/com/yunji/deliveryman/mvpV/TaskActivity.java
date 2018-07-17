package com.yunji.deliveryman.mvpV;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunji.deliveryman.DeliApplication;
import com.yunji.deliveryman.MyConst;
import com.yunji.deliveryman.R;
import com.yunji.deliveryman.adapter.SettingAdapter;
import com.yunji.deliveryman.base.BaseMvpLifeActivity;
import com.yunji.deliveryman.base.RecyclerItemCallback;
import com.yunji.deliveryman.bean.MainLayerBean;
import com.yunji.deliveryman.bean.SettingBean;
import com.yunji.deliveryman.core.RobotStateService;
import com.yunji.deliveryman.mvpP.SettingPresent;
import com.yunji.deliveryman.mvpP.TaskPresent;
import com.yunji.deliveryman.util.Kits;
import com.yunji.deliveryman.util.SpeechUtil;
import com.yunji.deliveryman.widget.BatteryView;
import com.yunji.deliveryman.widget.WaveView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class TaskActivity extends BaseMvpLifeActivity<TaskPresent> {
    @BindView(R.id.wave)
    WaveView wave;
    @BindView(R.id.iv_target)
    ImageView iv_target;
    @BindView(R.id.iv_smile)
    ImageView iv_smile;

    public ArrayList<MainLayerBean> layerBeans;
    private int[] imgIds=new int[]{R.drawable.ic_tasking_a,R.drawable.ic_tasking_b,R.drawable.ic_tasking_c,R.drawable.ic_tasking_d,R.drawable.ic_tasking_kitchen};
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        RobotStateService.getInstance().setRobotStateCallBack(getP());
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        try {
            layerBeans = (ArrayList<MainLayerBean>) getIntent().getSerializableExtra("data");
            getP().startTask(true);
            wave.addWave();
        } catch (Exception e) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        wave.stop();
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_task;
    }

    @Override
    public TaskPresent newP() {
        return new TaskPresent();
    }


    @OnClick(R.id.rl_wave)
    public void onViewClicked(View view) {
//        finish();

    }

    public void changeDistrictImage(int districtPosition) {
        iv_target.setImageResource(imgIds[districtPosition]);
    }
}
