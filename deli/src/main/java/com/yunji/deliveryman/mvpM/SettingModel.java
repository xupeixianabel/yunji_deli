package com.yunji.deliveryman.mvpM;

import android.content.Context;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.bean.SettingBean;
import com.yunji.deliveryman.mvpP.SettingPresent;

import java.util.ArrayList;
import java.util.List;

public class SettingModel {
    private SettingPresent mainPresent;
    public void setPresent(SettingPresent mainPresent) {
        this.mainPresent = mainPresent;
    }
    public void detachP(){
        mainPresent=null;
    }

    public List<SettingBean> getSettingData(Context context) {
        List<SettingBean> list=new ArrayList<SettingBean>();
        String[] names=context.getResources().getStringArray(R.array.setting);
        int[] images=new int[]{ R.drawable.ic_charge,R.drawable.ic_kitchen, R.drawable.ic_clear,R.drawable.ic_system_bar, R.drawable.ic_status,R.drawable.ic_volume,
                R.drawable.ic_entry,R.drawable.ic_floor,
//                R.drawable.ic_password,
                R.drawable.ic_update};

        for (int i=0;i<names.length;i++) {
            list.add(new SettingBean(images[i], names[i]));
        }
        return list;
    }
}
