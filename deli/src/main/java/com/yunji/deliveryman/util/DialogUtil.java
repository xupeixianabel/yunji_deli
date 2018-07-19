package com.yunji.deliveryman.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yunji.deliveryman.DeliApplication;
import com.yunji.deliveryman.MyConst;
import com.yunji.deliveryman.R;
import com.yunji.deliveryman.adapter.CancelTaskAdapter;
import com.yunji.deliveryman.adapter.TaskArriveAdapter;
import com.yunji.deliveryman.base.BaseMvpActivity;
import com.yunji.deliveryman.bean.BusTaskChangeEvent;
import com.yunji.deliveryman.bean.MainLayerBean;
import com.yunji.deliveryman.bean.TaskBean;
import com.yunji.deliveryman.bus.BusProvider;
import com.yunji.deliveryman.mvpV.MainActivity;
import com.yunji.sdk.YunjiConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DialogUtil {
    public static Dialog estopDialog(Activity context) {
        if (context == null) {
            return null;
        }
        if (context.isFinishing()) {
            return null;
        }
        Dialog dialog = new Dialog(context, R.style.fullrSceenMyDialog);
        dialog.setContentView(R.layout.dialog_estop);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        dialog. getWindow().getDecorView().setPadding(0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }


    public static Dialog quCanDialog(Activity context, ArrayList<MainLayerBean> layerBeans, int position, View.OnClickListener listener) {
        if (context == null) {
            return null;
        }
        if (context.isFinishing()) {
            return null;
        }
        Dialog dialog = new Dialog(context, R.style.fullrSceenMyDialog);
        dialog.setContentView(R.layout.dialog_task_arrive);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TextView confirm = dialog.findViewById(R.id.confirm_to_get);
        confirm.setOnClickListener(listener);
        RecyclerView rv_qucan = dialog.findViewById(R.id.rv_qucan);

        TaskArriveAdapter adapter = new TaskArriveAdapter(context);
        adapter.setChoosedPosition(position);
        adapter.setData(layerBeans);
        rv_qucan.setLayoutManager(new LinearLayoutManager(context));
        rv_qucan.setAdapter(adapter);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    public static Dialog layerDialog(Activity context) {
        if (context == null) {
            return null;
        }
        if (context.isFinishing()) {
            return null;
        }
        final Dialog dialog = new Dialog(context, R.style.nomalDialog);
        dialog.setContentView(R.layout.dialog_plate_plies);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        RadioGroup radioGroup = dialog.findViewById(R.id.choose_plies);
        RadioButton four_plies = dialog.findViewById(R.id.four_plies);
        RadioButton three_plies = dialog.findViewById(R.id.three_plies);
        RadioButton two_plies = dialog.findViewById(R.id.two_plies);
        RadioButton one_plies = dialog.findViewById(R.id.one_plies);

        int layerNum = SharedPrefsUtil.get(MyConst.PLAT_LAYER, 4);
        switch (layerNum) {
            case 1:
                one_plies.setChecked(true);
                break;
            case 2:
                two_plies.setChecked(true);
                break;
            case 3:
                three_plies.setChecked(true);
                break;
            case 4:
                four_plies.setChecked(true);
                SharedPrefsUtil.put(MyConst.PLAT_LAYER, 4);
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.four_plies:
                        SharedPrefsUtil.put(MyConst.PLAT_LAYER, 4);
                        break;
                    case R.id.three_plies:
                        SharedPrefsUtil.put(MyConst.PLAT_LAYER, 3);
                        break;
                    case R.id.two_plies:
                        SharedPrefsUtil.put(MyConst.PLAT_LAYER, 2);
                        break;
                    case R.id.one_plies:
                        SharedPrefsUtil.put(MyConst.PLAT_LAYER, 1);
                        break;
                }
            }
        });
        dialog.findViewById(R.id.bt_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

//        int width = ActivityUtil.getScreenWidthMetrics(context) * 2 / 5;
//        int height = ActivityUtil.getScreenHeightMetrics(context) * 5 / 6;
//        LinearLayout ll = dialog.findViewById(R.id.ll_root);
//        ll.setLayoutParams(new LinearLayout.LayoutParams(width, height));


        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;


    }


    public static Dialog cancelTaskDialog(final Activity context) {
        if (context == null) {
            return null;
        }
        if (context.isFinishing()) {
            return null;
        }
        final Dialog dialog = new Dialog(context, R.style.nomalDialog);
        dialog.setContentView(R.layout.dialog_cancel_task);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        RecyclerView rv_list = dialog.findViewById(R.id.rv_list);
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        Button btn_confirm = dialog.findViewById(R.id.btn_confirm);

        final CancelTaskAdapter adapter = new CancelTaskAdapter(context);
        adapter.setData(MyConst.tasks);
        rv_list.setLayoutManager(new LinearLayoutManager(context));
        rv_list.setAdapter(adapter);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedNum = 0;
                List<TaskBean> data = adapter.getDataSource();
                for (int i = data.size() - 1; i >= 0; i--) {
                    if (data.get(i).isChecked()) {
                        selectedNum++;
                        if (i == 0) {
                            DeliApplication.yunjiApiDeli.cancelTask(null);
                            DeliApplication.yunjiApiDeli.cancelTask(null);
                        }
                        TaskBean district = MyConst.tasks.remove(i);
                        BusProvider.getBus().post(new BusTaskChangeEvent(district.getLayer()));
                    }
                }

                if (selectedNum > 0) {
                    dialog.dismiss();
                } else {
                    if (context instanceof BaseMvpActivity) {
                        ((BaseMvpActivity) context).showTs(context.getResources().getString(R.string.please_choose_task));
                    }
                }
            }
        });


        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;


    }


    public static Dialog voiceSettingDialog(final Activity context) {
        if (context == null) {
            return null;
        }
        if (context.isFinishing()) {
            return null;
        }
        final Dialog dialog = new Dialog(context, R.style.nomalDialog);
        dialog.setContentView(R.layout.dialog_voice_setting);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        SeekBar seekBar = dialog.findViewById(R.id.seekBar);
        Button btn_confirm = dialog.findViewById(R.id.btn_confirm);

        final AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        seekBar.setMax(maxVolume);

        //获取当前音量
        int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBar.setProgress(currentVolume);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    //设置系统音量
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                    seekBar.setProgress(currentVolume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;


    }


  /*  public static Dialog chargeCancelTaskDialog(final Activity context, View.OnClickListener listener) {
        if (context == null) {
            return null;
        }
        if (context.isFinishing()) {
            return null;
        }
        final Dialog dialog = new Dialog(context, R.style.nomalDialog);
        dialog.setContentView(R.layout.dialog_charge_cancel_task);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        final Button btn_confirm = dialog.findViewById(R.id.btn_confirm);
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_confirm.setOnClickListener(listener);
        btn_cancel.setOnClickListener(listener);

        final String excuteNow = context.getResources().getString(R.string.excute_now);
        CountDownTimer timer1 = new CountDownTimer(10 * 1000, 1000) {
            @Override
            public void onTick(long timeLeft) {
                btn_confirm.setText(excuteNow + "(" + (timeLeft / 1000) + "s)");
            }

            @Override
            public void onFinish() {
                btn_confirm.performClick();
            }
        };
        timer1.start();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;


    }*/
}
