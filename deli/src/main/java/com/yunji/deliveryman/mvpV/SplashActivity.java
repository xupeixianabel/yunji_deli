package com.yunji.deliveryman.mvpV;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yunji.deliveryman.R;
import com.yunji.deliveryman.base.BaseMvpLifeActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

public class SplashActivity extends BaseMvpLifeActivity {
        @BindView(R.id.iv_gif)
    ImageView ivGif;
//    @BindView(R.id.task_gif)
//    GifViewTasking task_gif;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    public void initData(Bundle savedInstanceState) {
        initGif();
        timer();

    }

    private void timer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(context, MainActivity.class));
                finish();
            }
        };
        timer.schedule(timerTask, 6000);
    }

    private void initGif() {
//        task_gif.startAnimation();
        Glide.with(context)
                .load(R.drawable.smile)
                .apply(new RequestOptions().override(960,540))
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
//        task_gif.stopAnimation();
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }
}
