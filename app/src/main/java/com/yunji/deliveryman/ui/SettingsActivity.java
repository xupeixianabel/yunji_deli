/*
package com.yunji.deliveryman.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


import com.yunji.deliveryman.MyApplication;
import com.yunji.deliveryman.R;
import com.yunji.deliveryman.adapter.SettingsGridAdapter;
import com.yunji.deliveryman.bean.Mode2;
import com.yunji.deliveryman.bean.SocketMessage;
import com.yunji.deliveryman.common.Constants;
import com.yunji.deliveryman.core.SocketService;
import com.yunji.deliveryman.db.TablePoint;
import com.yunji.deliveryman.db.TablePointDao;
import com.yunji.deliveryman.utils.MyLogcat;
import com.yunji.deliveryman.utils.SharedPrefsUtil;
import com.yunji.deliveryman.utils.ToastUtil;
import com.yunji.deliveryman.utils.Util;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.OnClickListener;


public class SettingsActivity extends Activity implements OnClickListener{


    private TextView mTvShake;
    private TextView mTvAngle;
    private TextView mTvLevel;
    private TextView mTvModel;
    private Button mBtn_send, mBtn_connect, mBtn_interrupt;
    GridView gv_layout;
    private EditText mEt_sendContent;
    TextView tv_receive_message;

    private List<Mode2> mModeList = new ArrayList<Mode2>();
    private SettingsGridAdapter mAdapter;


    private final int MSG_WHAT_CLICK_ITEM = 201;
    private double currentAngle;
    private int currentLevel;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case MSG_WHAT_CLICK_ITEM://点击Item
                    String pointStr = (String) msg.obj;
                    int postion = msg.arg1;
                    doItemClick(postion, pointStr);
                    break;

                case 1:
                    break;
            }
        }
    };

    private Button btn_resetIp;
    private TablePointDao tablePointDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_setting);


        */
/**
         * 绑定服务
         *//*


        initView();
        setListener();
    }

    private void initView() {
        tablePointDao = new TablePointDao(this);
        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        gv_layout = (GridView) findViewById(R.id.gv_layout);
        mBtn_connect = (Button) findViewById(R.id.btn_connect);
        btn_resetIp = (Button) findViewById(R.id.btn_resetIp);

        mBtn_interrupt = (Button) findViewById(R.id.btn_interrupt);
        mBtn_send = (Button) findViewById(R.id.btn_send);
        mEt_sendContent = (EditText) findViewById(R.id.et_sendContent);
        tv_receive_message = (TextView) findViewById(R.id.tv_receive_message);
        tv_receive_message.setText("");
        tv_back.setOnClickListener(this);
        mBtn_connect.setOnClickListener(this);
        mBtn_interrupt.setOnClickListener(this);
        mBtn_send.setOnClickListener(this);
        btn_resetIp.setOnClickListener(this);

        initData();
        mAdapter = new SettingsGridAdapter(this, mModeList, mHandler);
        gv_layout.setAdapter(mAdapter);

    }

    private void doItemClick(int pos, String pointStr) {

        keyBoardCancle();
        Intent intent = null;
        switch (pos) {

            case 0://获取状态
                ToastUtil.showToast(getApplication(), "获取状态");

                sendMessage("/api/robot_status");
                break;
            case 1://移动
                ToastUtil.showToast(getApplication(), "移动");

                sendMessage("/api/joy_control?angular_velocity=0.1&linear_velocity=0.2");
                break;
            case 2://取消移动
                ToastUtil.showToast(getApplication(), "取消移动");

                sendMessage("/api/move/cancel");
                break;
            case 3://急停
                ToastUtil.showToast(getApplication(), "急停");

                sendMessage("/api/estop?flag=true");
                break;
            case 4://取消急停
                ToastUtil.showToast(getApplication(), "取消急停急停");

                sendMessage("/api/estop?flag=false");
                break;

            case 5://去充电
                List<TablePoint> mList = tablePointDao.queryAllPointBySate(2);
                if (mList != null && mList.size() > 0) {
                    ToastUtil.showToast(getApplication(), "去充电桩");
                    sendMessage("/api/move?marker=" + mList.get(0).tablePoint);
                } else {
                    ToastUtil.showToast(getApplication(), "请先录入充电点位");
                }
                break;
            case 6://去厨房

                List<TablePoint> mList2 = tablePointDao.queryAllPointBySate(1);
                if (mList2 != null && mList2.size() > 0) {
                    ToastUtil.showToast(getApplication(), "去厨房");
                    sendMessage("/api/move?marker=" + mList2.get(0).tablePoint);
                } else {
                    ToastUtil.showToast(getApplication(), "请先录入厨房点位");
                }

                break;
            case 7://显示标题栏
                ToastUtil.showToast(getApplication(), "显示标题栏");
                Util.showDorbar(getApplicationContext(), true);
                break;
            case 8://隐藏标题栏
                ToastUtil.showToast(getApplication(), "隐藏标题栏");
                Util.showDorbar(getApplicationContext(), false);
                break;
            case 9://录入点位
                intent = new Intent(SettingsActivity.this, EntryTablePointActivity.class);
                startActivity(intent);

                break;
            default:
                break;
        }
    }

    private void setListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        Util.showDorbar(getApplicationContext(), true);
    }

    //强制隐藏软键盘
    public void keyBoardCancle() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void initData() {
        mModeList.clear();
        Mode mode = new Mode("获取状态", R.drawable.ic_state);
        mModeList.add(mode);
        mode = new Mode("移动", R.drawable.ic_stop_task);
        mModeList.add(mode);
        mode = new Mode("取消移动", R.drawable.ic_stop_task);
        mModeList.add(mode);
        mode = new Mode("急停", R.drawable.ic_move_point);
        mModeList.add(mode);
        mode = new Mode("取消急停", R.drawable.ic_move_point);
        mModeList.add(mode);
        mode = new Mode("去充电桩", R.drawable.ic_move_point);
        mModeList.add(mode);
        mode = new Mode("去厨房", R.drawable.ic_moveto);
        mModeList.add(mode);
        mode = new Mode("显示标题栏", R.drawable.ic_moveto);
        mModeList.add(mode);
        mode = new Mode("隐藏标题栏", R.drawable.ic_moveto);
        mModeList.add(mode);
        mode = new Mode("点位录入", R.drawable.ic_moveto);
        mModeList.add(mode);


    }

    @Override
    public void onClick(View v) {


            switch (v.getId()) {

                case R.id.tv_back:
//                     showDorbar(false);
                    finish();
                    break;
                case R.id.btn_connect://连接服务器

                    break;
                case R.id.btn_resetIp:
                    startActivity(new Intent(SettingsActivity.this, IpSettingActivity.class));
                    finish();
                    break;
                case R.id.btn_interrupt://断开Socket连接

                    break;
                case R.id.btn_send://向服务器发送消息
                    String content = mEt_sendContent.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        Toast.makeText(this, "消息为空", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mEt_sendContent.setText("");
                    break;
            }

    }

    @Override
    protected void onDestroy() {

        //解绑服务
//        unbindService(serviceConnection);
        super.onDestroy();
    }


}
*/
