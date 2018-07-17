package com.yunji.deliveryman.base;


import android.text.TextUtils;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yunji.deliveryman.MyConst;
import com.yunji.deliveryman.mvpI.IPresent;
import com.yunji.deliveryman.mvpI.XActivity;

public abstract class BaseMvpActivity<P extends IPresent> extends XActivity<P> {
    private Toast toast;
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 显示吐司
     *
     * @param msg
     */
    public void showTs(String msg) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        if (TextUtils.isEmpty(msg))return;
        toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }



    /**
     * 显示log
     *
     * @param msg
     */
    public void showLog(String msg) {
        if (MyConst.DEV) {
            Logger.d(msg);
        }
    }

    @Override
    protected void onDestroy() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        super.onDestroy();
    }



}
