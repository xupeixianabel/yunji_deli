package com.yunji.deliveryman.mvpV;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.adapter.MainDistrictAdapter;
import com.yunji.deliveryman.adapter.MainLayerAdapter;
import com.yunji.deliveryman.base.BaseMvpActivity;
import com.yunji.deliveryman.base.RecyclerItemCallback;
import com.yunji.deliveryman.bean.MainDistrictBean;
import com.yunji.deliveryman.bean.MainLayerBean;
import com.yunji.deliveryman.mvpP.MainPresent;
import com.yunji.deliveryman.util.ActivityUtil;
import com.yunji.deliveryman.util.DialogUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseMvpActivity<MainPresent> {
    @BindView(R.id.recycler_left)
    RecyclerView recycler_left;
    @BindView(R.id.recycler_right)
    RecyclerView recycler_right;
    @BindView(R.id.tv_start_task)
    AppCompatButton tv_start_task;

    MainLayerAdapter layerAdapter;
    MainDistrictAdapter districtAdapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        getP().initPermission();
        setAdapter();
    }

    private void setAdapter() {
        layerAdapter = new MainLayerAdapter(context);
        layerAdapter.setDistrictData(getP().getDistrictList());
        recycler_left.setLayoutManager(new LinearLayoutManager(context));
        recycler_left.setAdapter(layerAdapter);
        layerAdapter.setData(getP().getLayerData());
        layerAdapter.setRecItemClick(new RecyclerItemCallback<MainLayerBean, MainLayerAdapter.ViewHolder>() {
            @Override
            public void onItemClick(int position, MainLayerBean model, int tag, MainLayerAdapter.ViewHolder holder) {
                super.onItemClick(position, model, tag, holder);
                if (model.isHasChoosed()) {
                    districtAdapter.setCheckedIndex(model.getDistrictPosition());
                } else {
                    districtAdapter.setCheckedIndex(-1);
                }
            }
        });


        districtAdapter = new MainDistrictAdapter(context);
        recycler_right.setLayoutManager(new GridLayoutManager(context, 3));
        recycler_right.setAdapter(districtAdapter);
        districtAdapter.setData(getP().getDistrictData());
        districtAdapter.setRecItemClick(new RecyclerItemCallback<MainDistrictBean, MainDistrictAdapter.ViewHolder>() {
            @Override
            public void onItemClick(int position, MainDistrictBean model, int tag, MainDistrictAdapter.ViewHolder holder) {
                super.onItemClick(position, model, tag, holder);
                if (layerAdapter != null) {
                    if (tag == -1) {//取消选择区域
                        if (position == 4) {
                            layerAdapter.unSetDistrictAll();
                        } else {
                            layerAdapter.unSetDistrict();
                        }
                    } else {//选择了区域
                        if (position == 4) {
                            layerAdapter.setDistrictAll();
                        } else {
                            layerAdapter.setDistrict(position);
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainPresent newP() {
        return new MainPresent();
    }


    @OnClick({R.id.iv_setting, R.id.iv_cruise, R.id.tv_start_task})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                startActivity(new Intent(context,SettingActivity.class));
                break;
            case R.id.iv_cruise:
                break;
            case R.id.tv_start_task:
                break;
        }
    }


    private Dialog estopDialog;

    private void showEstopDialog() {
        if (estopDialog != null && estopDialog.isShowing()) return;
        estopDialog = DialogUtil.estopDialog(context);
    }

    private void hideEstopDialog() {
        if (estopDialog != null) {
            estopDialog.cancel();
            estopDialog = null;
        }
    }


    @Override
    protected void onDestroy() {
        hideEstopDialog();
        super.onDestroy();
    }
}
