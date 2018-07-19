package com.yunji.deliveryman.base;


import android.content.Context;
import android.media.AudioManager;
import android.text.TextUtils;
import android.view.KeyEvent;
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


    /**
     * 屏蔽系统自带的调节音量控件
     *屏蔽返回键
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0);
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0);
        }
        return true;
    }
}
