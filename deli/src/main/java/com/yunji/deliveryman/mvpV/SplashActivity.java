package com.yunji.deliveryman.mvpV;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yunji.deliveryman.R;
import com.yunji.deliveryman.base.BaseMvpActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

public class SplashActivity extends BaseMvpActivity {
    @BindView(R.id.iv_gif)
    ImageView ivGif;

    private Timer timer;
    private TimerTask timerTask;
    @Override
    public void initData(Bundle savedInstanceState) {
        initGif();
        timer();

    }

    private void timer() {
        timer=new Timer();
        timerTask=new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(context,MainActivity.class));
                finish();
            }
        };
        timer.schedule(timerTask,3000);
    }

    private void initGif() {
        Glide.with(context)
                .load(R.drawable.smile)
                .into(ivGif);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public Object newP() {
        return null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer!=null){
            timer.cancel();
            timer=null;
        }
        if (timerTask!=null){
            timerTask.cancel();
            timerTask=null;
        }
    }
}
