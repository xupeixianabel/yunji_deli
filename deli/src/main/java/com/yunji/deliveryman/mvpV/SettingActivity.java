package com.yunji.deliveryman.mvpV;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.adapter.MainDistrictAdapter;
import com.yunji.deliveryman.adapter.SettingAdapter;
import com.yunji.deliveryman.base.BaseMvpActivity;
import com.yunji.deliveryman.base.RecyclerItemCallback;
import com.yunji.deliveryman.bean.MainDistrictBean;
import com.yunji.deliveryman.bean.SettingBean;
import com.yunji.deliveryman.mvpP.SettingPresent;
import com.yunji.deliveryman.widget.BatteryView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseMvpActivity<SettingPresent> {
    SettingAdapter settingAdapter;
    @BindView(R.id.horizontalBattery)
    BatteryView horizontalBattery;
    @BindView(R.id.iv_charging)
    ImageView iv_charging;
    @BindView(R.id.tv_electricity)
    TextView tv_electricity;
    @BindView(R.id.rc_setting)
    RecyclerView rc_setting;

    @Override
    public void initData(Bundle savedInstanceState) {
        setAdapter();
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
            }
        });


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
    public void onViewClicked() {
        finish();

    }
}
