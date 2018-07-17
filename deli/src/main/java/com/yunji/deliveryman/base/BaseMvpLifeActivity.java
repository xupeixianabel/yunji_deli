package com.yunji.deliveryman.base;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yunji.deliveryman.mvpI.IPresent;

public abstract class BaseMvpLifeActivity<P extends IPresent> extends BaseMvpActivity<P> {
    public int lifeState;
    public static final int STATE_ONCREATE=1;
    public static final int STATE_ONSTART=2;
    public static final int STATE_ONRESUME=3;
    public static final int STATE_ONPAUSE=4;
    public static final int STATE_ONSTOP=5;
    public static final int STATE_ONDESTROY=6;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifeState=STATE_ONCREATE;
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifeState=STATE_ONSTART;
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifeState=STATE_ONRESUME;
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifeState=STATE_ONPAUSE;
    }

    @Override
    protected void onStop() {
        super.onStop();
        lifeState=STATE_ONSTOP;

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifeState=STATE_ONDESTROY;
    }
}
