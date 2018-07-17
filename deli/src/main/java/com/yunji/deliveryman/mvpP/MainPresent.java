package com.yunji.deliveryman.mvpP;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;

import com.yunji.deliveryman.DeliApplication;
import com.yunji.deliveryman.MyConst;
import com.yunji.deliveryman.R;
import com.yunji.deliveryman.bean.MainDistrictBean;
import com.yunji.deliveryman.bean.MainLayerBean;
import com.yunji.deliveryman.bean.TaskBean;
import com.yunji.deliveryman.core.RobotStateService;
import com.yunji.deliveryman.mvpI.XPresent;
import com.yunji.deliveryman.mvpM.MainModel;
import com.yunji.deliveryman.mvpV.MainActivity;
import com.yunji.deliveryman.mvpV.TaskActivity;
import com.yunji.deliveryman.util.Kits;
import com.yunji.deliveryman.util.SpeechUtil;
import com.yunji.sdk.api.YunjiCallBack;
import com.yunji.sdk.bean.deli.YJDeliTaskStateBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class MainPresent extends XPresent<MainActivity> implements RobotStateService.RobotStateCallBack {
    //    public SpeechUtil speechUtil;
    MainModel mainModel;

    public MainPresent() {
        super();
        mainModel = new MainModel();
        mainModel.setMainPresent(this);
    }

    public void initPermission() {
        getV().getRxPermissions().request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECEIVE_BOOT_COMPLETED)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            //TODO 许可

                        } else {
                            //TODO 未许可

                        }
                    }
                });
    }

    public List<MainLayerBean> getLayerData(int size) {
        return mainModel.getLayerData(getV(), size);
    }

    public String[] getDistrictList() {
        return mainModel.getDistrictList(getV());
    }

    public List<MainDistrictBean> getDistrictData() {
        return mainModel.getDistrictData(getV());
    }


    @Override
    public void attachV(MainActivity view) {
        super.attachV(view);
        RobotStateService.getInstance().startTimer();
        RobotStateService.getInstance().setRobotStateCallBack(this);
//        speechUtil = new SpeechUtil(getV());
    }

    @Override
    public void detachV() {
        mainModel.detachP();
        mainModel = null;
        RobotStateService.getInstance().cancelTimer();
        RobotStateService.getInstance().removeStateCallBack(this);
        super.detachV();
    }

    @Override
    public void robotState(YJDeliTaskStateBean status) {
      /*  if (status != null && status.getCode() == 0) {
            if (status.getData() != null) {
                if (status.getData().isEstop()) {
                    getV().showEstopDialog();
                } else {
                    getV().hideEstopDialog();
                }
            }
        }*/

    }

    public void checkTable(ArrayList<MainLayerBean> dataSource) {
        ArrayList<TaskBean> tasks = new ArrayList<TaskBean>();
        String[] points = getV().getResources().getStringArray(R.array.Points);
        for (MainLayerBean bean : dataSource) {
            if (bean.isHasChoosed()) {
                if (bean.getDistrictPosition() >= 0 && bean.getDistrictPosition() < points.length) {
                    TaskBean taskBean=new TaskBean();
                    taskBean.setLayer(bean.getLayer());
                    taskBean.setDistrict(points[bean.getDistrictPosition()]);
                    taskBean.setDistrictPosition(bean.getDistrictPosition());
                    tasks.add(taskBean);
//                    tasks.add(points[bean.getDistrictPosition()]);
                }
            }
        }

        if (tasks.size() == 0) {
            try {
                SpeechUtil.getInstance().speaking(getV().getResources().getString(R.string.choose_district));
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }
        tasks.add(new TaskBean("F0","E",4));//厨房点位

        if (Kits.NetWork.checkNetworkIfAvailable(getV())) {
            DeliApplication.yunjiApiDeli.cancelTask(null);
            MyConst.tasks = tasks;
            Intent intent = new Intent(getV(), TaskActivity.class);
            intent.putExtra("data",dataSource);
            getV().startActivity(intent);
            //        getPoint(tasks);
        } else {
            getV().showTs("please check net connection!");
        }


    }

    //巡游前获取点位信息。
    private void getPoint(final ArrayList<String> tasks) {
        DeliApplication.yunjiApiDeli.queryMarkerList(new YunjiCallBack<String>() {
            @Override
            public void onError(String s) {
                getV().showTs(s);
            }

            @Override
            public void onSuccess(String markers) {
                checkPointExist(tasks, markers);
            }
        });
    }

    private void checkPointExist(ArrayList<String> tasks, String markers) {
        try {
            JSONObject data = new JSONObject(markers);
            if (data.getBoolean("success")) {
                JSONObject message = data.getJSONObject("message");
//                Iterator<String> ss = message.keys();
                for (String marker : tasks) {
                    if (TextUtils.isEmpty(message.getString(marker))) {
                        getV().showTs("Point " + marker + "  is not existed");
                        return;
                    }
                }
                Intent intent = new Intent(getV(), TaskActivity.class);
                intent.putExtra("data", tasks);
                getV().startActivity(intent);
            }
        } catch (JSONException e) {
            getV().showTs("Point " + "  is not existed");
            e.printStackTrace();
        }
    }
}
