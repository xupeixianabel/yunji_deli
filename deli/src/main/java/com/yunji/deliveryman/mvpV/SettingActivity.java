package com.yunji.deliveryman.mvpV;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.adapter.MainDistrictAdapter;
import com.yunji.deliveryman.adapter.SettingAdapter;
import com.yunji.deliveryman.base.BaseMvpActivity;
import com.yunji.deliveryman.base.BaseMvpLifeActivity;
import com.yunji.deliveryman.base.RecyclerItemCallback;
import com.yunji.deliveryman.bean.MainDistrictBean;
import com.yunji.deliveryman.bean.SettingBean;
import com.yunji.deliveryman.core.RobotStateService;
import com.yunji.deliveryman.mvpP.SettingPresent;
import com.yunji.deliveryman.util.DialogUtil;
import com.yunji.deliveryman.util.Kits;
import com.yunji.deliveryman.widget.BatteryView;
import com.yunji.sdk.bean.deli.YJDeliTaskStateBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseMvpLifeActivity<SettingPresent> {
    public SettingAdapter settingAdapter;
    @BindView(R.id.horizontalBattery)
    public   BatteryView horizontalBattery;
    @BindView(R.id.iv_charging)
    public ImageView iv_charging;
    @BindView(R.id.tv_electricity)
    public TextView tv_electricity;
    @BindView(R.id.rc_setting)
    RecyclerView rc_setting;

    @BindView(R.id.tv_state)
    TextView tv_state;
    @BindView(R.id.tv_version)
    TextView tv_version;

    @Override
    protected void onResume() {
        super.onResume();
        RobotStateService.getInstance().setRobotStateCallBack(getP());
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        setAdapter();
        tv_version.setText(Kits.Package.getVersionName(context));
    }

    private void setAdapter() {
        settingAdapter = new SettingAdapter(context);
        rc_setting.setLayoutManager(new GridLayoutManager(context, 6));
        rc_setting.setAdapter(settingAdapter);
        settingAdapter.setData(getP().getSettingData());
        settingAdapter.setRecItemClick(new RecyclerItemCallback<SettingBean, SettingAdapter.ViewHolder>() {
            @Override
            public void onItemClick(int position, SettingBean model, int tag, SettingAdapter.ViewHolder holder) {
                super.onItemClick(position, model, tag, holder);
                switch (position) {
                    case 0://去充电
                        getP().gotoCharge();
                        break;
                    case 1://去厨房
                        getP().gotokichen();
                        break;
                    case 2://清空任务
                        getP().clearTask();
                        break;
                    case 3://显示标题
                        getP().showHideNavigationBar();
                        break;
                    case 4://获取状态
                        setStateVisible();
                        break;
                    case 5://声音设置
                        getP().voiceSetting();
                        break;
                    case 6://点位录入
                        getP().inputPoint();
                        break;
                    case 7://设置层数
                        DialogUtil.layerDialog(context);
                        break;
                /*    case 8://修改密码
                        getP().changePwd();
                        break;*/
                    case 8://升级版本
                        getP().update();
                        break;
                }

            }
        });
    }

    public boolean stateVisible;

    private void setStateVisible() {
        stateVisible=!stateVisible;
        if (stateVisible) {
            tv_state.setVisibility(View.VISIBLE);
        } else {
            tv_state.setVisibility(View.INVISIBLE);
        }
    }

    public void setStateText(String str) {
        if (stateVisible) {
            tv_state.setText(str);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public SettingPresent newP() {
        return new SettingPresent();
    }

    @OnClick(R.id.rl_back)
    public void onViewClicked(View view) {
        finish();
    }

}
