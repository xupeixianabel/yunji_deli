package com.yunji.deliveryman.mvpM;

import android.content.Context;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.bean.MainDistrictBean;
import com.yunji.deliveryman.bean.MainLayerBean;
import com.yunji.deliveryman.mvpP.MainPresent;

import java.util.ArrayList;
import java.util.List;

public class MainModel  {
    private MainPresent mainPresent;
    public void setMainPresent(MainPresent mainPresent) {
        this.mainPresent = mainPresent;
    }
    public void detachP(){
        mainPresent=null;
    }

    public List<MainLayerBean> getLayerData(Context context,int length) {
        if (length<=0)length=1;
        if (length>4)length=4;
        String[] layers = context.getResources().getStringArray(R.array.layer);
        String[] plate = context.getResources().getStringArray(R.array.plates);
        List<MainLayerBean> list = new ArrayList<MainLayerBean>();
        if (length==4) {
            for (int i = 0; i < length; i++) {
                list.add(new MainLayerBean(layers[i], plate[i], -1, 0));
            }
        }else if (length==3){
            list.add(new MainLayerBean(layers[0], plate[0], -1, 0));
            list.add(new MainLayerBean(layers[1], plate[1], -1, 0));
            list.add(new MainLayerBean(layers[2], plate[3], -1, 0));
        }else if (length==2){
            list.add(new MainLayerBean(layers[0], plate[0], -1, 0));
            list.add(new MainLayerBean(layers[2], plate[3], -1, 0));
        }else {
            list.add(new MainLayerBean(layers[0], plate[0], -1, 0));
        }
        return list;
    }

    public String[] getDistrictList(Context context){
        return context.getResources().getStringArray(R.array.district);
    }
    public List<MainDistrictBean> getDistrictData(Context context) {
        String[] districts = getDistrictList(context);
        List<MainDistrictBean> list = new ArrayList<MainDistrictBean>();
        list.add(new MainDistrictBean(R.drawable.a_checked, R.drawable.a_unchecked,districts[0]));
        list.add(new MainDistrictBean(R.drawable.b_checked, R.drawable.b_unchecked,districts[1]));
        list.add(new MainDistrictBean(R.drawable.c_checked, R.drawable.c_unchecked,districts[2]));
        list.add(new MainDistrictBean(R.drawable.d_checked, R.drawable.d_unchecked,districts[3]));
        list.add(new MainDistrictBean(R.drawable.all_checked, R.drawable.all_unchecked,""));
        return list;
    }


}
