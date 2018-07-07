package com.yunji.deliveryman.mvpP;

import android.Manifest;

import com.yunji.deliveryman.bean.MainDistrictBean;
import com.yunji.deliveryman.bean.MainLayerBean;
import com.yunji.deliveryman.mvpM.MainModel;
import com.yunji.deliveryman.mvpI.XPresent;
import com.yunji.deliveryman.mvpV.MainActivity;

import java.util.List;

import io.reactivex.functions.Consumer;

public class MainPresent extends XPresent<MainActivity> {
    MainModel mainModel;
    public MainPresent(){
        super();
        mainModel=new MainModel();
        mainModel.setMainPresent(this);
    }
    @Override
    public void detachV() {
        super.detachV();
        mainModel.detachP();
        mainModel=null;
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
    public List<MainLayerBean> getLayerData() {
        return mainModel.getLayerData(getV());
    }
    public String[] getDistrictList(){
        return mainModel.getDistrictList(getV());
    }
    public List<MainDistrictBean> getDistrictData() {
        return mainModel.getDistrictData(getV());
    }






}
