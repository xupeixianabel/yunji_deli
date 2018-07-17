package com.yunji.deliveryman.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yunji.deliveryman.MyApplication;
import com.yunji.deliveryman.R;
import com.yunji.deliveryman.adapter.GridAdapter;
import com.yunji.deliveryman.adapter.ListAdapter;
import com.yunji.deliveryman.adapter.SettingsGridAdapter;
import com.yunji.deliveryman.adapter.TaskAdapter;
import com.yunji.deliveryman.adapter.TaskArraivedListAdapter;
import com.yunji.deliveryman.bean.FoodTable;
import com.yunji.deliveryman.bean.HttpMessage;
import com.yunji.deliveryman.bean.Mode;
import com.yunji.deliveryman.bean.RobotStatusResponse;
import com.yunji.deliveryman.common.Constants;
import com.yunji.deliveryman.core.HttpService;
import com.yunji.deliveryman.core.getStatusService;
import com.yunji.deliveryman.db.TablePoint;
import com.yunji.deliveryman.db.TablePointDao;
import com.yunji.deliveryman.db.TaskSend;
import com.yunji.deliveryman.db.TaskSendDao;
import com.yunji.deliveryman.mqtt.MqttManager;
import com.yunji.deliveryman.other.log.FL;
import com.yunji.deliveryman.services.UpdateManager;
import com.yunji.deliveryman.utils.DialogUtil;
import com.yunji.deliveryman.utils.DipPxUtils;
import com.yunji.deliveryman.utils.FullScreenDialog;
import com.yunji.deliveryman.utils.MyLogcat;
import com.yunji.deliveryman.utils.MyToast;
import com.yunji.deliveryman.utils.MyToastB;
import com.yunji.deliveryman.utils.NumKeyBoardUtil;
import com.yunji.deliveryman.utils.PublicUtils;
import com.yunji.deliveryman.utils.SharedPrefsUtil;
import com.yunji.deliveryman.utils.Util;
import com.yunji.deliveryman.view.BatteryView;
import com.yunji.deliveryman.view.GifViewTasking;
import com.yunji.deliveryman.view.PasswordView;
import com.yunji.deliveryman.view.ViewHolder;
import com.yunji.deliveryman.view.WaveView;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;


public class MainActivity extends Activity implements HttpService.IHttpServiceInterface, TextToSpeech.OnInitListener {

    HttpService.HttpBinder myBinder;
    public static final int NOT_REPEAT_SPEAK = 100;
    public static final int IS_FIRST_TASKSENDZ = 101;
    public static final int MSG_WHAT_UPDATE = 200;
    public static final int MSG_WHAT_SPEAK = 201;
    public static final int MSG_WHAT_DO_START_TASK = 202;
    public static final int CHECK_ITEM_SELECTED = 205;
    public static final int DISMISS_ARRAIVED_DIALOG = 206;
    public static final int DISMISS_TASKING_DIALOG = 207;
    public static final int MSG_WHAT_SETTINGS_CONNECT_SOCKET = 209;
    public static final int MSG_CHARGING_STATE = 210;
    public static final int MSG_CHARGING = 211;
    public static final int MSG_GO_CHARGING = 212;
    public static final int MSG_GO_KITCHEN = 213;
    public static final int MSG_GO_MARKET = 214;
    public static final int MSG_SHOW_TASKING1 = 215;
    public static final int MSG_SHOW_TASKING2 = 216;
    public static final int MSG_SPEAK_OVER = 217;

    int MY_DATA_CHECK_CODE = 30;
    int CRUISE_RESULT_CODE = 31;
    TextToSpeech mTts;
    String speakStr = "";

    public MyHandler myHandler;
    private String tableName;
    private TaskAdapter mTaskAdapter;
    private Dialog exceptionDialog;
    public static long delayTime = 100000;//测试40000
    private List<TaskSend> mTaskSendList;

    private boolean isTaskException;//异常
    private boolean isTaskExceptionChanged;//异常状态改变
    private boolean isFirstCharging;
    private boolean isFirstFinishCharging;
    private boolean isTaskArraived;//点位到达
    private boolean isTaskStart;//点位开始移动
    private boolean isHardEstopChanged;//急停状态改变
    private boolean isHardEstop;
    private boolean startCancel;
    private boolean noSpeakCancelHardEstop;

    private int currentTaskSendState = -1;
    public boolean settingsShowBar;

    private VolumeReceiver volumeReceiver;
    private SeekBar seekBar;
    private AudioManager am;
    private BatteryView mBatteryView;
    private TextView tv_electricity;
    private ImageView iv_charging;
    private static int power;
    private TablePoint mArraivedTablePoint = null;
    private String mOldTablePoint = null;

    @BindView(R.id.setting)
    ImageView setting;
    @BindView(R.id.recycler_list)
    RecyclerView mRecyclerViewList;
    @BindView(R.id.recycler_grid)
    RecyclerView mRecyclerViewGrid;
    @BindView(R.id.tv_start_task)
    AppCompatButton tv_start_task;

    private ListAdapter mAdapterList;
    private GridAdapter mAdapterGrid;

    private String[] selectNameList = {"A区", "B区", "C区", "D区"};
    private int[] drawableId_tasking = {R.drawable.ic_tasking_a, R.drawable.ic_tasking_b, R.drawable.ic_tasking_c
            , R.drawable.ic_tasking_d};
    private int layerIndex;
    private WaveView wave;
    private GifViewTasking task_gif;
    private RelativeLayout rl_wave;
    private RelativeLayout rl_gif;
    private boolean isSpeaking;
    private NumKeyBoardUtil keyboardUtil;
    private String clientId = "";
    private String userName = "";
    private String password = "";

    private MqttManager mqttManager;
    private FullScreenDialog mModifyPasswordDialog;
    private NumKeyBoardUtil keyboardUtilNew;
    private TextView mTxtVersionName;
    //    private String topicBarricade = "robot/yunhai/HOTQY00SZ201708100603070/topic/notification";//订阅路障话题
//    private boolean netConnectFlag;

    @Override
    public void sendMessage(final HttpMessage message) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (myBinder != null) {
                    if (Util.isNetworkAvailable(getApplicationContext())) {
                        myBinder.sendMessage(message);
                    } else {
                        MyLogcat.showLog("当前网络已经断开！");
                        FL.e(Constants.show_log, "当前网络已经断开！");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showToast(getApplicationContext(), "当前网络已经断开!");
                            }
                        });
                    }
                }
            }
        }).start();
    }

    @Override
    public void updateMessageListStatus(RobotStatusResponse robotResponse, String apiName) {
        Message data = Message.obtain();
        data.what = MSG_WHAT_UPDATE;
        data.obj = robotResponse;
        Bundle bundle = new Bundle();
        bundle.putString("API_NAME", apiName);
        data.setData(bundle);
        myHandler.sendMessage(data);
    }

    @Override
    public void updateMessageList(String apiName) {
        Message data = Message.obtain();
        data.what = MSG_WHAT_UPDATE;
        Bundle bundle = new Bundle();
        bundle.putString("API_NAME", apiName);
        data.setData(bundle);
        myHandler.sendMessage(data);
    }


    //    private List<ImageView> mImageList = new ArrayList<>();

    /**
     * 创建静态内部类
     */
    private static class MyHandler extends Handler {
        //持有弱引用MainActivity,GC回收时会被回收掉.
        private final WeakReference<MainActivity> mActivty;

        public MyHandler(MainActivity activity) {
            mActivty = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivty.get();
            if (activity == null) {
                super.handleMessage(msg);
                return;
            }
            super.handleMessage(msg);
            if (activity != null) {
                //执行业务逻辑
                switch (msg.what) {
                    case MSG_CHARGING_STATE:
                        if (activity.mSettingsDialog != null && activity.mSettingsDialog.isShowing()) {
                            if (power + 5 > 100) {
                                activity.mBatteryView.setPower(100);
                            } else {
                                activity.mBatteryView.setPower(power += 5);
                            }

                            if (power == 100) {
                                power = 0;
                            }
                            Message ms = Message.obtain(this);
                            ms.what = MSG_CHARGING_STATE;
                            sendMessageDelayed(ms, 800);
                        } else {
                            removeMessages(MSG_CHARGING_STATE);
                        }
                        break;

                    case MSG_SPEAK_OVER:
                        activity.isSpeaking = false;
                        break;
                    case MSG_SHOW_TASKING1:
                        if (activity.cancelTaskDialog != null && activity.cancelTaskDialog.isShowing()
                                || activity.exceptionDialog != null && activity.exceptionDialog.isShowing()) {

                        } else {
                            activity.showTaskingAnima(activity, true, 6000);
                        }
                        break;
                    case MSG_SHOW_TASKING2:
                        if (activity.cancelTaskDialog != null && activity.cancelTaskDialog.isShowing()
                                || activity.exceptionDialog != null && activity.exceptionDialog.isShowing()) {

                        } else {
                            if (!activity.isSpeaking) {
                                activity.showTaskingAnima(activity, false, 6000);
                            } else {
                                Message msg1 = Message.obtain(this);
                                msg1.what = MSG_SHOW_TASKING2;
                                this.sendMessageDelayed(msg1, 2000);
                            }
                        }
                        break;
                    case MSG_GO_CHARGING:
//                        if (!activity.netConnectFlag) {
//                            MyToast.showToast(activity, "网络连接失败");
//                            return;
//                        }
                        HttpMessage message = new HttpMessage();
                        message.requestType = "GET";
                        message.apiName = "forcecharge";
                        message.message = "";
                        activity.sendMessage(message);
                        break;
                    case MSG_GO_KITCHEN:
//                        if (!activity.netConnectFlag) {
//                            MyToast.showToast(activity, "网络连接失败");
//                            return;
//                        }
                        String tablePoint2 = (String) msg.obj;
                        HttpMessage httpMessage = new HttpMessage();
                        httpMessage.requestType = "GET";
                        httpMessage.apiName = "settarget_noback";
                        httpMessage.message = tablePoint2;
                        activity.sendMessage(httpMessage);
                        break;
                    case MSG_GO_MARKET:
//                        if (!activity.netConnectFlag) {
//                            MyToast.showToast(activity, "网络连接失败");
//                            return;
//                        }
                        String tablePoint3 = (String) msg.obj;
                        HttpMessage httpMessage2 = new HttpMessage();
                        httpMessage2.requestType = "GET";
                        httpMessage2.apiName = "settarget_noback";
                        httpMessage2.message = tablePoint3;
                        activity.sendMessage(httpMessage2);

                        MyLogcat.showLog("@@@@@@@@@@收到执行命令去点位：" + tablePoint3);
                        FL.e(Constants.show_log, "@@@@@@@@@@收到执行命令去点位：" + tablePoint3);

                        break;

                    case MSG_WHAT_UPDATE://跟新UI界面的消息列表
                        String apiName = msg.getData().getString("API_NAME");
                        MyLogcat.showLog("我得到的方法是 = " + apiName);
                        FL.e(Constants.show_log, "我得到的方法是 = " + apiName);
                        if (apiName.equals("taskstatus")) {
                            if (msg != null) {
                                RobotStatusResponse robotResponse = (RobotStatusResponse) msg.obj;
                                if (robotResponse != null) {
//                                    if (!activity.netConnectFlag) {
//                                        MyLogcat.showLog("MSG_WHAT_UPDATE 网络连接失败！");
//                                        return;
//                                    }
                                    MyLogcat.showLogStatusState("我得到的对象数据status = 999999999999" + robotResponse.toString());
                                    FL.e(Constants.show_log_status_state, "我得到的对象数据status = 999999999999" + robotResponse.toString());
                                    if (activity.mSettingsDialog != null && activity.tv_receive_message != null) {
                                        activity.tv_receive_message.setText(robotResponse.toString());
                                    }

                                    MyLogcat.showLogStatusState("@@@@@@@@@@@@@@当前急停状态=" + robotResponse.estop);
                                    FL.e(Constants.show_log_status_state, "@@@@@@@@@@@@@@当前急停状态=" + robotResponse.estop);
                                    if (robotResponse.estop) {
                                        activity.showEstopDialog();

                                        activity.isHardEstop = true;
                                        if (activity.mSettingsDialog != null && activity.mSettingsDialog.isShowing()) {

                                        } else {
                                            activity.noSpeakCancelHardEstop = false;
                                        }

                                    } else {
                                        activity.stopShowEstopDialog();


                                        activity.isHardEstop = false;
                                    }
//
                                    power = robotResponse.power_percent;

                                    if (robotResponse.charge_state == 1) {
                                        this.removeMessages(MSG_WHAT_SPEAK);
                                        //充电中
                                        MyLogcat.showLogStatusState("充电中");
                                        FL.e(Constants.show_log_status_state, "充电中");
                                        if (activity.mSettingsDialog != null && activity.mSettingsDialog.isShowing()) {
                                            if (activity.tv_electricity != null) {
                                                activity.tv_electricity.setText("充电中 " + power + "%");
                                            }
                                            activity.iv_charging.setVisibility(VISIBLE);
                                            Message ms = Message.obtain(this);
                                            ms.what = MSG_CHARGING_STATE;
                                            sendEmptyMessage(MSG_CHARGING_STATE);
                                        }
                                        if (!activity.isFirstCharging) {
                                            activity.isFirstCharging = true;
                                            activity.clearData();
//                                            activity.startSpeak(false,0,"已充上电");
                                        }

                                    } else {
                                        activity.isFirstCharging = false;
                                        MyLogcat.showLogStatusState("充电完成");

                                        if (!activity.isFirstFinishCharging) {
                                            activity.isFirstFinishCharging = true;

//                                            activity.startSpeak(false,0,"充电完成");
                                        }


                                        FL.e(Constants.show_log_status_state, "充电完成");
                                        if (activity.mSettingsDialog != null && activity.mSettingsDialog.isShowing()) {

                                            if (activity.tv_electricity != null) {
                                                activity.tv_electricity.setText(power + "%");
                                            }
                                            activity.iv_charging.setVisibility(View.GONE);
                                            removeMessages(MSG_CHARGING_STATE);
                                            activity.mBatteryView.setPower(power);
                                        }


                                    }
                                    if ("ACTION_CANCELED".equals(robotResponse.state)) {
                                        MyLogcat.showLog("#####——————任务已取消");
                                        FL.e(Constants.show_log, "#####——————任务已取消");

                                    }

                                    MyLogcat.showLogStatusState("Status_State——————" + robotResponse.state);
                                    FL.e(Constants.show_log_status_state, "Status_State——————" + robotResponse.state);

                                    MyLogcat.showLogStatusState("上海 switch外 robotResponse.state = " + robotResponse.state);
                                    switch (robotResponse.state) {
                                        case "ACTION_CANCELED":
                                            MyLogcat.showLog("#####——————任务已取消");
                                            FL.e(Constants.show_log, "#####——————任务已取消");
                                            MyLogcat.showLogStatusState("上海，进入ACTION_CANCELED activity.startCancel = " + activity.startCancel);
                                            //MyToast.showToast(activity.getApplicationContext(), "取消任务成功");

                                            if (activity.startCancel) {
                                                activity.startCancel = false;
                                                MyLogcat.showLogStatusState("上海，进入if (activity.startCancel)  activity.startCancel = " + activity.startCancel);
                                                activity.isTaskStart = false;
                                                activity.isTaskArraived = false;
                                                if (activity.moveNewTask) {
                                                    MyLogcat.showLogStatusState("上海 if (activity.moveNewTask)   activity.moveNewTask = " + activity.moveNewTask);
                                                    activity.moveNewTask = false;
                                                    Message msg2 = new Message();
                                                    msg2.what = MSG_WHAT_DO_START_TASK;
                                                    activity.myHandler.sendMessage(msg2);
                                                }
                                            }
                                            break;
                                        case "DELIVERY_END":
                                            MyLogcat.showLog("#####——————任务结束等待新任务");
                                            MyLogcat.showLog("#####——————任务到达????");
                                            FL.e(Constants.show_log, "#####——————任务结束等待新任务");
                                            FL.e(Constants.show_log, "#####——————任务到达????");
                                            activity.isTaskArraived = true;

                                            break;
                                        case "BATTERY_CHARGING":
                                            MyLogcat.showLog("#####——————充电中");
                                            FL.e(Constants.show_log, "#####——————充电中");
                                            break;
                                        case "DELIVERY_START":

                                            activity.isTaskStart = true;
                                            activity.isTaskArraived = false;
                                            MyLogcat.showLog("#####——————任务开始");
                                            FL.e(Constants.show_log, "#####——————任务开始");
                                            break;
                                        case "ACTION_ABNORMAL":
                                            MyLogcat.showLog("#####——————任务失败");
                                            FL.e(Constants.show_log, "#####——————任务失败");
                                            activity.isTaskException = true;

                                            break;
                                        case "ITEMS_SERVED_ON":
                                            //activity.isTaskArraived = true;//必须不能判断
                                            MyLogcat.showLog("#####——————任务到达");
                                            FL.e(Constants.show_log, "#####——————任务到达");
                                            break;
                                    }
                                } else {
                                    MyLogcat.showLog("网络连接成功，robotResponse没有数据");
                                    FL.e(Constants.show_log, "网络连接成功，robotResponse没有数据");
                                }

                            }
                        }
                        if (activity.isTaskExceptionChanged != activity.isTaskException && activity.isTaskException) {
                            activity.showExceptionDialog();
                        }
                        activity.isTaskExceptionChanged = activity.isTaskException;
//                        MyLogcat.showLog("#################################99999任务到达,isTaskStart=" + activity.isTaskStart + ",activity.isTaskArraived=" + activity.isTaskArraived);
//                        FL.e(Constants.show_log, "#################################99999任务到达,isTaskStart=" + activity.isTaskStart + ",activity.isTaskArraived=" + activity.isTaskArraived);
                        if (activity.isTaskStart && activity.isTaskArraived) {
                            activity.isTaskStart = false;
                            activity.showTaskArraivedDialog();
                        }

                        if (activity.isHardEstopChanged != activity.isHardEstop) {
                            if (activity.isHardEstop) {
                                activity.isHardEstop = true;
                                activity.noSpeakCancelHardEstop = false;
                                if (activity.exceptionDialog != null && activity.exceptionDialog.isShowing()) {

                                } else {
                                    activity.showCancelTaskDialog();
                                }
                            } else {
                                activity.isHardEstop = false;
                                if (activity.cancelTaskDialog != null && activity.cancelTaskDialog.isShowing()) {
                                    activity.cancelTaskDialog.dismiss();
                                }

                                activity.reStartSpeak();
                            }
                        }
                        activity.isHardEstopChanged = activity.isHardEstop;

                        break;
                    case MSG_WHAT_SPEAK:

                        Boolean repeatSpeak = true;
                        if (msg.arg2 == NOT_REPEAT_SPEAK) {
                            repeatSpeak = false;
                        }
                        activity.startSpeak(repeatSpeak, delayTime, activity.speakStr);

                        break;

                    case MSG_CHARGING:
//                        if (!activity.netConnectFlag) {
//                            MyToast.showToast(activity, "网络连接失败");
//                            return;
//                        }
                        if (activity.mSettingsDialog != null && activity.mSettingsDialog.isShowing()) {
                            if (activity.isServiceConnect(false)) {
                                HttpMessage message1 = new HttpMessage();
                                message1.requestType = "GET";
                                message1.apiName = "taskstatus";
                                message1.message = "";
//                                if (!activity.netConnectFlag) {
//                                    MyToast.showToast(activity, "网络连接失败");
//                                    return;
//                                }
                                activity.sendMessage(message1);
                            }
                            Message msg2 = Message.obtain(this);
                            msg2.what = MSG_CHARGING;
                            this.sendMessageDelayed(msg2, 1000);
                        }
                        break;
                    case MSG_WHAT_DO_START_TASK:
                        if (msg.arg1 == IS_FIRST_TASKSENDZ) {
                            activity.doStartTask(true);
                        } else {
                            activity.doStartTask(false);
                        }
                        break;
                    case DISMISS_ARRAIVED_DIALOG:
                        if (activity.taskArraivedDialog != null) {
                            activity.taskArraivedDialog.dismiss();
                        }
                        break;
                    case DISMISS_TASKING_DIALOG:
                        if (activity.taskingDialog != null) {
                            activity.taskingDialog.dismiss();
                        }
                        break;
                    case CHECK_ITEM_SELECTED:
                        break;
                }

            }
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            if (iBinder != null) {

                //接收从Service传回的对象，强制转换类型
                myBinder = (HttpService.HttpBinder) iBinder;
                MyApplication.getInstance().myBinder = myBinder;
                myBinder.addHttpServiceListener(MainActivity.this);
//                netConnectFlag = myBinder.getNetConnectFlag();
//                statusBinder = (getStatusService.getServiceBinder) iBinder;
//                MyApplication.getInstance().statusBinder = statusBinder;
//                statusBinder.addHttpServiceListener(MainActivity.this);

            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            myBinder = null;
        }
    };


    private TablePointDao tablePointDao;

    private FullScreenDialog taskArraivedDialog;
    private FullScreenDialog taskingDialog;

    private FullScreenDialog cancelTaskDialog;
    private FullScreenDialog mSettingsDialog;
    private FullScreenDialog mPlatePliesDialog;
    private FullScreenDialog mPasswordDialog, mSettingPasswordDialog;
    private TextView tv_receive_message;
    private TaskSendDao mTaskSendDao;
    private int showSettingsCount;
    private Boolean moveNewTask = false;
    List<FoodTable> mFoodTableList = new ArrayList<FoodTable>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
        setClickListener();
    }

    private void initViews() {
//        mqttSubscribeBarricade();
        getrobotId();
        myHandler = new MyHandler(this);
        /**
         * 绑定服务
         */
        Intent intent = new Intent(this, HttpService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        Intent intentStatus = new Intent(this, getStatusService.class);
        startService(intentStatus);//如果测试升级的话，此行代码会避免阻塞引起阻塞，故需要注释代码才能测试成功


        //音量
        volumeReceiver = new VolumeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(volumeReceiver, filter);

        Util.showDorbar(this, false);
        mTaskSendDao = new TaskSendDao(this);
        tablePointDao = new TablePointDao(this);

        initLayerListAdapter();


        if (mAdapterGrid == null) {
            mAdapterGrid = new GridAdapter(this);
            mRecyclerViewGrid.setAdapter(mAdapterGrid);
            mRecyclerViewGrid.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            mAdapterGrid.notifyDataSetChanged();
        }

        doClickListItem(0);

        EventBus.getDefault().register(this);
    }

    private void initLayerListAdapter() {
        int layerNum = SharedPrefsUtil.get(getApplicationContext(), Constants.LAYER_NUM, 4);

        //初始化数据
        mFoodTableList.clear();
        FoodTable mFoodTable = new FoodTable("F1", "餐盘(上)", "");
        mFoodTableList.add(mFoodTable);
        mFoodTable = new FoodTable("F2", "餐盘(中)", "");
        mFoodTableList.add(mFoodTable);
        mFoodTable = new FoodTable("F3", "餐盘(中)", "");
        mFoodTableList.add(mFoodTable);
        mFoodTable = new FoodTable("F4", "餐盘(下)", "");
        mFoodTableList.add(mFoodTable);
        switch (layerNum) {
            case 1:
                mFoodTableList.remove(mFoodTableList.size() - 1);
                mFoodTableList.remove(mFoodTableList.size() - 1);
                mFoodTableList.remove(mFoodTableList.size() - 1);
                break;
            case 2:
                mFoodTableList.remove(mFoodTableList.size() - 1);
                mFoodTableList.remove(mFoodTableList.size() - 1);
                break;
            case 3:
                mFoodTableList.remove(mFoodTableList.size() - 1);
                break;
            case 4:

                break;
        }
        mRecyclerViewList.setLayoutManager(new LinearLayoutManager(this));
        if (mAdapterList == null) {
            mAdapterList = new ListAdapter(this, mFoodTableList);
            mRecyclerViewList.setAdapter(mAdapterList);
        } else {
            mAdapterList.setFoodList(mFoodTableList);
            mAdapterList.notifyDataSetChanged();
        }
        clearSharedPrefs();
    }

    private void doClickListItem(int position) {
        mAdapterList.setItemPos(position);
        mAdapterList.notifyDataSetChanged();
        layerIndex = position;
        String layerName = SharedPrefsUtil.get(getApplicationContext(), layerIndex + Constants.TASK_TABLE_NAME, "");

        int GridPos = -1;
        for (int i = 0; i < selectNameList.length; i++) {
            if (layerName.equals(selectNameList[i])) {
                GridPos = i;
                break;
            }
        }

        mAdapterGrid.setItemPos(GridPos);
        mAdapterGrid.notifyDataSetChanged();

        if (!isServiceConnect(false)) {
            MyLogcat.showLog("服务未连接");
            FL.e(Constants.show_log, "服务未连接");
        }
    }

    //设置按钮
    private void setClickListener() {
        findViewById(R.id.iv_cruise).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startGetVoice();
//                startGetPoints();


//                Intent intent=new Intent(MainActivity.this,CruiseActivity.class);
//                startActivityForResult(intent,CRUISE_RESULT_CODE);
            }
        });

        findViewById(R.id.iv_add).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                addVoice();
//                startPlay("cruise_1");

            }
        });
        findViewById(R.id.iv_stop).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                stopPlay();
                String str = "{\"action\":\"leave\",\"marker\":\"A\",\"productId\":\"fysfdsfsfd\",\"sound\":\"cruise_1\",\"ts\":1500000000}";
                try {
                    MqttManager.getInstance().publish(Constants.topicCruise, 1, str.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        setting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                showPasswordDialog();
                showSettingsDialog(true);
            }
        });

        mAdapterList.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, TextView mTextLetter, LinearLayout mListBg, int position) {

                doClickListItem(position);
            }
        });

        mAdapterGrid.setOnItemClickListener(new GridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ImageView imageView, int position) {
                doClickGridItem(position);

            }
        });
        tv_start_task.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Util.isNetworkAvailable(getApplicationContext())) {
                    MyToast.showToast(getApplicationContext(), "当前网络已经断开！");
                } else {
                    mTaskSendDao.deleteAllTaskSend();
                    isServiceConnect(false);//当Socket未连接时去连接Socket
                    if (isServiceConnect(true)) {
                        HttpMessage httpMessage = new HttpMessage();
                        httpMessage.requestType = "GET";
                        httpMessage.apiName = "taskstatus";
                        httpMessage.message = "";
                        sendMessage(httpMessage);
                        startTask(true);
                    } else {
                        MyToast.showToast(MainActivity.this, "当前网络无连接");
                    }
                }
            }
        });
    }

   /* private void addVoice() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                String uri1 = "android.resource://" + getPackageName() + "/" + R.raw.cruise_1;
//       String uri1 = "android.resource://" + getPackageName() + "/" + R.raw.cruise_2;
//       String uri1 = "android.resource://" + getPackageName() + "/" + R.raw.cruise_3;

//                Uri uri = Uri.parse(uri1);
//                File file=new File(uri.toString());
//                String path = file.getAbsolutePath();
                Map<String,String> data=new HashMap<String,String>();
                data.put("taskType","transport");
                data.put("lang","zh");
//                data.put("filename","cruise_1");
//                data.put("file","cruise_1");


                UploadFileUtil.uploadFile(MainActivity.this,"http://192.168.0.157:8809/api/upload_custom_sound", new String[]{uri1}, data);

            }
        }.start();

    }*/

    private void doClickGridItem(int gridViewPos) {
        String tableName = SharedPrefsUtil.get(getApplicationContext(), layerIndex + Constants.TASK_TABLE_NAME, "");
        switch (gridViewPos) {
            case 0:
            case 1:
            case 2:
            case 3:
                String newTableName = selectNameList[gridViewPos];

                if (!TextUtils.isEmpty(tableName) && newTableName.equals(tableName)) {
                    SharedPrefsUtil.put(getApplicationContext(), layerIndex + Constants.TASK_TABLE_NAME, "");
                    mAdapterGrid.setItemPos(-1);
                } else {
                    SharedPrefsUtil.put(getApplicationContext(), layerIndex + Constants.TASK_TABLE_NAME, selectNameList[gridViewPos]);
                    mAdapterGrid.setItemPos(gridViewPos);
                }
                break;
            case -1:
            case 4:
                for (int i = 0; i < mFoodTableList.size(); i++) {

                    if (mAdapterGrid.selectAll || gridViewPos == -1) {
                        SharedPrefsUtil.put(getApplicationContext(), i + Constants.TASK_TABLE_NAME, "");
                    } else {
                        SharedPrefsUtil.put(getApplicationContext(), i + Constants.TASK_TABLE_NAME, selectNameList[i]);
                    }
                }
                if (mAdapterGrid.selectAll || gridViewPos == -1) {
                    mAdapterGrid.setItemPos(-1);
                } else {
                    mAdapterGrid.setItemPos(4);
                }
                break;
        }
        mAdapterList.notifyDataSetChanged();
        mAdapterGrid.notifyDataSetChanged();
    }


    private boolean isServiceConnect(boolean isShowToast) {
        boolean result = false;
        result = true;//测试
     /*   try {
            if (myBinder != null && myBinder.isSocketServerClose()) {

//                   if(!Util.isServiceRunning(getApplicationContext(),"ai.yunji.mirage.ipforward.forwarder.IpForwardService")){
//
//                       //开启USB共享网络
                NetworkHelper.setUsbTethering(MainActivity.this, true);

                if (isShowToast) {
                    MyToast.showToast(getApplicationContext(), "请稍候重试");
                }

//                       final Intent intent = new Intent();
//                       //开启IP转发服务
//                       intent.setComponent(new ComponentName("ai.yunji.mirage.ipforward", "ai.yunji.mirage.ipforward.forwarder.IpForwardService"));
//                       startService(intent);
//
//                       MyLogcat.showLog("重启ipforword");
//                   }else{
                Message msg = Message.obtain(myHandler);
                msg.what = MSG_WHAT_SETTINGS_CONNECT_SOCKET;
                myHandler.sendMessage(msg);
                MyLogcat.showLog("myBinder.connectSocket");
//                   }


            } else {
                result = true;
                MyLogcat.showLog("isSocketServer connected");
            }

        } catch (Exception e) {
            e.printStackTrace();
            MyLogcat.showLog("isServiceConnect 异常");
            result = false;
        }
        */
        return result;
    }

    private void startTask(boolean isFirst) {

        List<TablePoint> mTableList = tablePointDao.queryAllPointByPointType(0);
        if (mTableList != null && mTableList.size() <= 0) {
            MyToast.showToast(getApplicationContext(), "请先在后台添加餐桌点位");
            return;
        }

        List<TablePoint> mKitchenList = tablePointDao.queryAllPointByPointType(1);
        if (mKitchenList != null && mKitchenList.size() <= 0) {
            MyToast.showToast(getApplicationContext(), "请先在后台添加厨房点位");
            return;
        }

        String tableName0 = SharedPrefsUtil.get(getApplicationContext(), 0 + Constants.TASK_TABLE_NAME, "");
        String tableName1 = SharedPrefsUtil.get(getApplicationContext(), 1 + Constants.TASK_TABLE_NAME, "");
        String tableName2 = SharedPrefsUtil.get(getApplicationContext(), 2 + Constants.TASK_TABLE_NAME, "");
        String tableName3 = SharedPrefsUtil.get(getApplicationContext(), 3 + Constants.TASK_TABLE_NAME, "");
        List<String> tableNameList = new ArrayList<String>();
        tableNameList.add(tableName0);
        tableNameList.add(tableName1);
        tableNameList.add(tableName2);
        tableNameList.add(tableName3);

        for (int i = 0; i < tableNameList.size(); i++) {
            if (!TextUtils.isEmpty(tableNameList.get(i))) {

                List<TablePoint> mList = tablePointDao.queryByTablePointName(tableNameList.get(i));
                if (mList != null && mList.size() > 0) {
                    MyLogcat.showLog("添加TablePoint =：" + mList.get(0).tablePoint);
                    TaskSend mTaskSend = new TaskSend(i, mList.get(0).id, 0, false, true);
                    mTaskSendDao.add(mTaskSend);
                }

            } else {
                MyLogcat.showLog("添加null TablePoint =：");
                TaskSend mTaskSend = new TaskSend(i, -1, 2, false, true);
                mTaskSendDao.add(mTaskSend);
            }
        }
        List<TablePoint> mList = tablePointDao.queryAllPointByPointType(1);
        if (mList != null && mList.size() > 0) {
            MyLogcat.showLog("添加TablePoint =：" + mList.get(0).tablePoint);
            TaskSend mTaskSend = new TaskSend(4, mList.get(0).id, 0, false, false);
            mTaskSendDao.add(mTaskSend);
        }

        if (TextUtils.isEmpty(tableName0) && TextUtils.isEmpty(tableName1) && TextUtils.isEmpty(tableName2) && TextUtils.isEmpty(tableName3)) {


            MyToastB.showToast(getApplicationContext(), "请先选择送餐目的餐桌");

            speakStr = "请先选择送餐目的餐桌";
            startSpeak(false, 0, speakStr);

            return;
        } else {

//                if(!Util.isConnected(MainActivity.this)){//不是100%准确，当前连接正常后
//                    NoNetDialog noNetDialog = new NoNetDialog(MainActivity.this);
//                    return;
//                }
            if (!isServiceConnect(true)) {
                MyLogcat.showLog("服务未连接");
                return;
            }
            Message msg = Message.obtain(myHandler);
            msg.what = MSG_WHAT_DO_START_TASK;
            boolean result = false;
            if (isFirst) {
                msg.arg1 = IS_FIRST_TASKSENDZ;
                for (int i = 0; i < tableNameList.size(); i++) {
                    if (!TextUtils.isEmpty(tableNameList.get(i))) {
                        MyLogcat.showLog("查找tablename =：" + tableNameList.get(i));
                        mList = tablePointDao.queryByTablePointName(tableNameList.get(i));
                        if (mList != null && mList.size() > 0) {
                            MyLogcat.showLog("查找到TablePoint =：" + mList.get(0).tablePoint);
                        } else {
                            MyLogcat.showLog("——————未查找到TablePoint =：" + tableNameList.get(i));
                            MyToast.showToast(getApplicationContext(), "餐桌不存在：" + tableNameList.get(i));
                            MyLogcat.showLog("餐桌不存在：" + tableNameList.get(i));
                            result = true;
                            break;
                        }
                    }
                }
                if (result) {
                    MyLogcat.showLog("终止执行命令");
                    return;
                }
                MyLogcat.showLog("没有终止");

                myHandler.sendMessage(msg);
            } else {
                myHandler.sendMessage(msg);
            }
        }
    }

    private List<TaskSend> getTaskListByTaskState(int taskState) {
        List<TaskSend> tmpTaskSendList = null;
        if (taskState == 99) {//全部
            tmpTaskSendList = mTaskSendDao.queryAllTaskSend();
        } else {
            tmpTaskSendList = mTaskSendDao.queryTaskSendByTaskSate(taskState);
        }
        //正序，未到达
        if (tmpTaskSendList != null && tmpTaskSendList.size() > 1) {
            Collections.sort(tmpTaskSendList, new Comparator<TaskSend>() {
                @Override
                public int compare(TaskSend mTaskSend1, TaskSend mTaskSend2) {
                    int id1 = mTaskSend1.id;
                    int id2 = mTaskSend2.id;
                    Integer praiseId1 = Integer.valueOf(id1);
                    Integer praiseId2 = Integer.valueOf(id2);
                    return praiseId1.compareTo(praiseId2);
                }
            });
        }

        return tmpTaskSendList;
    }

    private TaskSend updateTaskSendsState(int oldTaskState, int newTaskState) {

        List<TaskSend> mTaskSendList = getTaskListByTaskState(oldTaskState);
        TaskSend tmpTaskSend = null;
        if (mTaskSendList != null && mTaskSendList.size() > 0) {
            for (int n = 0; n < mTaskSendList.size(); n++) {
                MyLogcat.showLog("遍历=" + mTaskSendList.get(n).toString());
                if (tmpTaskSend == null && mTaskSendList.get(n).taskState == oldTaskState) {
                    tmpTaskSend = mTaskSendList.get(n);
                    tmpTaskSend.taskState = newTaskState;
                    mTaskSendDao.update(tmpTaskSend);
                } else {
                    if (tmpTaskSend != null && tmpTaskSend.tablePointId == mTaskSendList.get(n).tablePointId) {
                        tmpTaskSend.taskState = newTaskState;
                        mTaskSendDao.update(tmpTaskSend);
                    }
                }
            }

        }
        return tmpTaskSend;
    }


    private void doStartTask(Boolean isFirst) {

        List<TaskSend> mTaskSendList = getTaskListByTaskState(0);

        if (mTaskSendList.size() > 0) {
            for (int n = 0; n < mTaskSendList.size(); n++) {
                MyLogcat.showLog("遍历=" + mTaskSendList.get(n).toString());
                if (mTaskSendList.get(n).taskState == 0) {
                    List<TablePoint> mList = tablePointDao.queryByTablePointId(mTaskSendList.get(n).tablePointId);
                    if (mList != null && mList.size() > 0) {

                        Message msg = Message.obtain(myHandler);
                        msg.what = MSG_GO_MARKET;
                        msg.obj = mList.get(0).tablePoint;
                        myHandler.sendMessageDelayed(msg, 3000);
                        MyLogcat.showLog("@@@@@@@@@@延迟3S执行命令去点位：" + mList.get(0).tablePoint);
                        showTaskingDialog(isFirst, mTaskSendList.get(n), mList.get(0));
                        break;
                    }
                }
            }

        }

        if (mTaskSendList.size() == 1) {
            Message msg = Message.obtain(myHandler);
            msg.what = MSG_WHAT_SPEAK;
            msg.arg2 = NOT_REPEAT_SPEAK;//不重复
            speakStr = "我现在要回厨房了";
            myHandler.sendMessage(msg);

        }
        if (mTaskSendList.size() == 0) {
            clearData();
        }

    }

    @Override
    protected void onResume() {

        super.onResume();
        if (mTaskSendDao == null) {
            mTaskSendDao = new TaskSendDao(this);
        }

        HttpMessage httpMessage = new HttpMessage();
        httpMessage.requestType = "GET";
        httpMessage.apiName = "taskstatus";
        httpMessage.message = "";
        sendMessage(httpMessage);

    }

    private class MySelectedItemListener implements TaskAdapter.OnSelectedItemChanged {

        @Override
        public void selectedItemChange(int count) {

        }

    }

    private void showExceptionDialog() {
        showSettingsCount = 0;

        if (exceptionDialog == null) {
            exceptionDialog = new FullScreenDialog(this);
        }

        if (exceptionDialog != null && !exceptionDialog.isShowing()) {
            exceptionDialog.show();
        }
        LayoutInflater inflater = getLayoutInflater();
        View contentView = inflater.inflate(R.layout.dialog_exception, null);

        View tv_reset_task = contentView.findViewById(R.id.tv_reset_task);
        tv_reset_task.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServiceConnect(true)) {
                    reCheckTaskSend();
                    exceptionDialog.dismiss();
                }
            }
        });
        View v_settings = contentView.findViewById(R.id.v_settings);
        v_settings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingsCount += 1;
                if (showSettingsCount == 10) {
                    showSettingsCount = 0;
                    showSettingsDialog(false);
                }
            }
        });

        exceptionDialog.setContentView(contentView);
        exceptionDialog.setCancelable(false);
        exceptionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (taskingDialog != null && taskingDialog.isShowing()) {

                    Message msg = Message.obtain(myHandler);
                    msg.what = MSG_SHOW_TASKING1;
                    myHandler.sendMessage(msg);
                }
            }
        });
        cancelTaskAnima();
        startSpeak(false, 130000, "我现在遇到了点小麻烦，请工作人员帮帮我");
    }

    private void reCheckTaskSend() {
        if (currentTaskSendState == 0) {
            doStartTask(false);
        } else if (currentTaskSendState == 1) {
            showTaskArraivedDialog();
        }
    }

    private void showCancelTaskDialog() {
        mTaskSendList = getTaskListByTaskState(0);
        List<TablePoint> mTablePointIdList = new ArrayList<TablePoint>();
        if (mTaskSendList.size() > 0) {
            for (int n = 0; n < mTaskSendList.size(); n++) {

                List<TablePoint> mList = tablePointDao.queryByTablePointId(mTaskSendList.get(n).tablePointId);
                if (mList != null && mList.size() > 0) {
                    mTablePointIdList.add(mList.get(0));
                    MyLogcat.showLog("找到去点位：" + mList.get(0).tablePoint);
                }
            }
            MyLogcat.showLog(mTaskSendList.size() + "");
        }

        if (mTaskSendList.size() <= 0 || mTablePointIdList.size() <= 0) {
            return;
        }

        stopSpeak(false);//暂停

        if (cancelTaskDialog == null) {
            cancelTaskDialog = new FullScreenDialog(this);
        }


        LayoutInflater inflater = getLayoutInflater();

        View contentView = inflater.inflate(R.layout.dialog_cancel_task, null);
        int width = DipPxUtils.getScreenWidth(MainActivity.this) * 2 / 5;
        int height = width * 28 / 32;
        cancelTaskDialog.setWidthAndHeight(width, height);
        if (cancelTaskDialog != null && !cancelTaskDialog.isShowing()) {
            cancelTaskDialog.show();
        }

        cancelTaskDialog.setContentView(contentView);
        final TextView tv_content = ((TextView) contentView.findViewById(R.id.tv_content));
        final ListView lv_list = ((ListView) contentView.findViewById(R.id.lv_list));
        lv_list.setVisibility(View.GONE);

        final List<TaskSend> mCancelList = new ArrayList<TaskSend>();
        final Map<Integer, Boolean> tmpCheckMap = new HashMap<Integer, Boolean>();

        mTaskAdapter = new TaskAdapter(MainActivity.this, mTaskSendList, mTablePointIdList, myHandler, new MySelectedItemListener() {
            @Override
            public void selectedItemChange(int count) {
                super.selectedItemChange(count);
                mCancelList.clear();
                tmpCheckMap.clear();
                tmpCheckMap.putAll(TaskAdapter.isCheckMap);
                for (Map.Entry<Integer, Boolean> entry : tmpCheckMap.entrySet()) {
                    MyLogcat.showLog("key= " + entry.getKey() + " and value= "
                            + entry.getValue());
                    if (entry.getValue()) {
                        mCancelList.add(mTaskSendList.get(entry.getKey()));
                    }
                }
            }
        });

        lv_list.setAdapter(mTaskAdapter);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long arg3) {
                ViewHolder holder = (ViewHolder) view.getTag();
                boolean isKitchen = TaskAdapter.isCheckMap.get(TaskAdapter.isCheckMap.size() - 1);
                if (isKitchen && position != TaskAdapter.isCheckMap.size() - 1) {
//                    MyToast.showToast(MainActivity.this,"不可点击");
                } else {
                    holder.checkbox.toggle();
                    TaskAdapter.isCheckMap.put(position, holder.checkbox.isChecked());
                }
            }
        });
        cancelTaskDialog.setCanceledOnTouchOutside(false);
        Button btn_cancel = ((Button) contentView.findViewById(R.id.btn_cancel));
        Button btn_confirm = ((Button) contentView.findViewById(R.id.btn_confirm));
        cancelTaskAnima();
        cancelTaskDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (taskingDialog != null && taskingDialog.isShowing()) {
                    Message msg = Message.obtain(myHandler);
                    msg.what = MSG_SHOW_TASKING1;
                    myHandler.sendMessage(msg);
                }
            }
        });

        btn_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Message msg = Message.obtain(myHandler);
                msg.arg2 = NOT_REPEAT_SPEAK;
                msg.what = MSG_WHAT_SPEAK;
                myHandler.sendMessageDelayed(msg, 500);
                noSpeakCancelHardEstop = false;
                cancelTaskDialog.dismiss();
            }
        });

        btn_confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!Util.isNetworkAvailable(getApplicationContext())) {
                    MyToast.showToast(getApplicationContext(), "当前网络已经断开");
                } else {
                    if (tv_content.getVisibility() == VISIBLE) {
                        tv_content.setVisibility(View.GONE);
                        lv_list.setVisibility(VISIBLE);
                    } else {
                        if (mCancelList.size() > 0) {
                            updateTaskSendsState(1, 2);
                            moveNewTask = true;
                            TaskSend tmpTaskSend = null;
                            for (int i = 0; i < mCancelList.size(); i++) {
                                tmpTaskSend = mCancelList.get(i);
                                tmpTaskSend.taskState = -1;
                                mTaskSendDao.update(tmpTaskSend);
                            }

                            mTaskSendList = getTaskListByTaskState(0);
                            if (mTaskSendList.size() == 0) {
                                clearData();
                            }

                            if (taskArraivedDialog != null && taskArraivedDialog.isShowing()) {

                            } else {
                                sendMessageCancel();
                            }
                        } else {
                            MyToast.showToast(getApplicationContext(), "请选择要取消的任务");
                            return;
                        }

                        noSpeakCancelHardEstop = false;
                        Message msg = Message.obtain(myHandler);
                        msg.arg2 = NOT_REPEAT_SPEAK;
                        msg.what = MSG_WHAT_SPEAK;
                        myHandler.sendMessageDelayed(msg, 500);
                        cancelTaskDialog.dismiss();
                    }
                }
            }
        });
    }

    private void sendMessageCancel() {
        MyLogcat.showLog("999999999 cancel");
        startCancel = true;
        HttpMessage httpMessage = new HttpMessage();
        httpMessage.requestType = "GET";
        httpMessage.apiName = "cancel";
        httpMessage.message = "";
        sendMessage(httpMessage);
    }

    /*密码输入框*/
    private void showPasswordDialog() {
        if (mPasswordDialog == null) {
            mPasswordDialog = new FullScreenDialog(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        View contentView = inflater.inflate(R.layout.dialog_password, null);
        final TextView mPasswordTitle = (TextView) contentView.findViewById(R.id.password_title);

        final PasswordView passwordView = (PasswordView) contentView.findViewById(R.id.password_view);
        passwordView.setInputType(InputType.TYPE_NULL);

        keyboardUtil = new NumKeyBoardUtil(contentView, this, passwordView);
        passwordView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyboardUtil.showKeyboard();
                return false;
            }
        });

        int width = DipPxUtils.getScreenWidth(this) * 3 / 8;
        int height = width * 4 / 5;
        mPasswordDialog.setWidthAndHeight(width, height);
        if (mPasswordDialog != null && !mPasswordDialog.isShowing()) {
            mPasswordDialog.show();
        }

        /*输入密码自动弹出键盘*/
        if (keyboardUtil != null) {
            keyboardUtil.showKeyboard();
        }

        mPasswordDialog.setContentView(contentView);
        passwordView.setOnFinishListener(new PasswordView.OnFinishListener() {
            @Override
            public void setOnPasswordFinished(String text) {
                String password = SharedPrefsUtil.get(getApplicationContext(), "password", "000000");
                if (!TextUtils.isEmpty(text)) {
                    if (text.length() == 6) {
                        if (text.equals(password) || text.equals("789789")) {
                            showSettingsDialog(true);
                            mPasswordDialog.dismiss();
                        } else {//密码错误
                            mPasswordTitle.setText("密码错误");
                            mPasswordTitle.setTextColor(Color.parseColor("#dc4c2d"));
                        }
                    } else {
                        mPasswordTitle.setText("请输入6位密码");
                        mPasswordTitle.setTextColor(Color.parseColor("#000000"));
                    }
                }
            }
        });

        mPasswordDialog.setOnTouchListener(new FullScreenDialog.onTouchListener() {
            @Override
            public void onDialogTouch(MotionEvent event) {
                keyboardUtil.showKeyboard();
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
//                        if (keyboardUtil != null) {
//                            keyboardUtil.hideKeyboard();
//                        }
//                    }
//                }
            }
        });
        mPasswordDialog.setCanceledOnTouchOutside(true);
    }

    /*修改密码弹窗*/
    private void showModifyPasswordDialog() {
        if (mModifyPasswordDialog == null) {
            mModifyPasswordDialog = new FullScreenDialog(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        View contentView = inflater.inflate(R.layout.dialog_modify_password, null);
        int width = DipPxUtils.getScreenWidth(this) * 3 / 8;
        int height = DipPxUtils.getScreenWidth(this) * 2 / 5;
        mModifyPasswordDialog.setWidthAndHeight(width, height);
        final EditText mEtPasswordNew = (EditText) contentView.findViewById(R.id.et_password_new);
        final EditText mEtPasswordConfirm = (EditText) contentView.findViewById(R.id.et_password_confirm);
        Button mButtonConfirm = (Button) contentView.findViewById(R.id.button_confirm);
        Button mButtonCancel = (Button) contentView.findViewById(R.id.button_cancel);
        if (mModifyPasswordDialog != null && !mModifyPasswordDialog.isShowing()) {
            mModifyPasswordDialog.show();
        }
        /*键盘*/
        keyboardUtilNew = new NumKeyBoardUtil(contentView, MainActivity.this, mEtPasswordNew, "");
        keyboardUtilNew.showKeyboard();
        mModifyPasswordDialog.setContentView(contentView);
        mModifyPasswordDialog.setCanceledOnTouchOutside(true);
        mButtonConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mEtPasswordNew.getText().toString()) || TextUtils.isEmpty(mEtPasswordConfirm.getText().toString())) {
                    MyToast.showToast(getApplicationContext(), "密码不能为空");
                    return;
                }
                int lengthPasswordNew = mEtPasswordNew.getText().length();
                int lengthPasswordConfirm = mEtPasswordConfirm.getText().length();
                if (lengthPasswordNew != 6 || lengthPasswordConfirm != 6) {
                    MyToast.showToast(getApplicationContext(), "请输入6位密码");
                    return;
                }
                if (!mEtPasswordNew.getText().toString().equals(mEtPasswordConfirm.getText().toString())) {
                    MyToast.showToast(getApplicationContext(), "密码不相同");
                    return;
                }
                SharedPrefsUtil.put(getApplicationContext(), "password", mEtPasswordConfirm.getText().toString());
                MyToast.showToast(getApplicationContext(), "密码修改成功！");
                if (mModifyPasswordDialog != null && mModifyPasswordDialog.isShowing()) {
                    mModifyPasswordDialog.dismiss();
                }
            }
        });

        mButtonCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mModifyPasswordDialog != null && mModifyPasswordDialog.isShowing()) {
                    mModifyPasswordDialog.dismiss();
                }
            }
        });

        /*输入密码*/
        mEtPasswordNew.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyboardUtilNew.showKeyboard();
                keyboardUtilNew.setEditText(mEtPasswordNew);
                mEtPasswordNew.requestFocus();
                return false;
            }
        });

        /*确认密码*/
        mEtPasswordConfirm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyboardUtilNew.showKeyboard();
                keyboardUtilNew.setEditText(mEtPasswordConfirm);
                mEtPasswordConfirm.requestFocus();
                return false;
            }
        });
    }

    private void settingPasswordDialog() {
        if (mSettingPasswordDialog == null) {
            mSettingPasswordDialog = new FullScreenDialog(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        View contentView = inflater.inflate(R.layout.dialog_password_setting, null);
        final PasswordView passwordView = (PasswordView) contentView.findViewById(R.id.password_view);
        passwordView.setInputType(InputType.TYPE_NULL);
        int width = DipPxUtils.getScreenWidth(MainActivity.this) * 1 / 2;
        int height = width * 4 / 5;
        mSettingPasswordDialog.setWidthAndHeight(width, height);
        if (mSettingPasswordDialog != null && !mSettingPasswordDialog.isShowing()) {
            mSettingPasswordDialog.show();
        }
        mSettingPasswordDialog.setContentView(contentView);
        mSettingPasswordDialog.setCanceledOnTouchOutside(true);
    }


    private void showPlatePliesDialog() {
        if (mPlatePliesDialog == null) {
            mPlatePliesDialog = new FullScreenDialog(this);
        }

        LayoutInflater inflater = getLayoutInflater();

        View contentView = inflater.inflate(R.layout.dialog_plate_plies, null);
        RadioGroup radioGroup = (RadioGroup) contentView.findViewById(R.id.choose_plies);
        RadioButton four_plies = (RadioButton) contentView.findViewById(R.id.four_plies);
        RadioButton three_plies = (RadioButton) contentView.findViewById(R.id.three_plies);
        RadioButton two_plies = (RadioButton) contentView.findViewById(R.id.two_plies);
        RadioButton one_plies = (RadioButton) contentView.findViewById(R.id.one_plies);

        int layerNum = SharedPrefsUtil.get(getApplicationContext(), Constants.LAYER_NUM, 4);
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
                SharedPrefsUtil.put(getApplicationContext(), Constants.LAYER_NUM, 4);
                break;
        }
        int width = DipPxUtils.getScreenWidth(MainActivity.this) * 2 / 5;
        int height = width * 27 / 32;
        mPlatePliesDialog.setWidthAndHeight(width, height);
        if (mPlatePliesDialog != null && !mPlatePliesDialog.isShowing()) {
            mPlatePliesDialog.show();
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.four_plies:
                        SharedPrefsUtil.put(getApplicationContext(), Constants.LAYER_NUM, 4);

                        break;
                    case R.id.three_plies:
                        SharedPrefsUtil.put(getApplicationContext(), Constants.LAYER_NUM, 3);

                        break;
                    case R.id.two_plies:
                        SharedPrefsUtil.put(getApplicationContext(), Constants.LAYER_NUM, 2);

                        break;
                    case R.id.one_plies:
                        SharedPrefsUtil.put(getApplicationContext(), Constants.LAYER_NUM, 1);

                        break;
                }
                initLayerListAdapter();
            }
        });

        mPlatePliesDialog.setContentView(contentView);

        mPlatePliesDialog.setCanceledOnTouchOutside(true);
    }

    private void reStartSpeak() {
        List<TaskSend> mTaskSendList = getTaskListByTaskState(0);
        if (mTaskSendList != null && mTaskSendList.size() > 0) {

            Message msg = Message.obtain(myHandler);
            msg.what = MSG_WHAT_SPEAK;
            if (mTaskSendList.size() == 1) {
                msg.arg2 = NOT_REPEAT_SPEAK;
            }
            myHandler.sendMessage(msg);

        }
    }

    private void showTaskingDialog(boolean isFirst, final TaskSend mTaskSend, TablePoint mTablePoint) {
        showSettingsCount = 0;
        currentTaskSendState = 0;


        if (taskingDialog == null) {
            taskingDialog = new FullScreenDialog(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        View contentView = inflater.inflate(R.layout.view_tasking, null);

        View v_settings = contentView.findViewById(R.id.v_settings);
        v_settings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingsCount += 1;
                if (showSettingsCount == 10) {
                    showSettingsCount = 0;
                    showSettingsDialog(false);
                }
            }
        });

        rl_wave = (RelativeLayout) contentView.findViewById(R.id.rl_wave);
        wave = (WaveView) contentView.findViewById(R.id.wave);
        ImageView head = (ImageView) contentView.findViewById(R.id.head);

        rl_gif = (RelativeLayout) contentView.findViewById(R.id.rl_gif);
        task_gif = (GifViewTasking) contentView.findViewById(R.id.task_gif);
        wave.addWave();
        rl_wave.setVisibility(VISIBLE);
        rl_gif.setVisibility(View.GONE);


        tableName = Util.updateTableNameStr(mTablePoint.tableName);
        Message msg = Message.obtain(myHandler);
        msg.what = MSG_WHAT_SPEAK;
        if (mTaskSend.isShow) {
            for (int i = 0; i < selectNameList.length; i++) {
                if (mTablePoint.tableName.equals(selectNameList[i])) {
                    head.setBackgroundResource(drawableId_tasking[i]);
                }
            }

            tableName = "我正在前往 " + tableName + " 送餐";
        } else {
            head.setBackgroundResource(R.drawable.ic_tasking_kitchen);
            msg.arg2 = NOT_REPEAT_SPEAK;//不重复
            tableName = "我现在要回厨房了，祝您用餐愉快";
        }

        if (isFirst) {
            speakStr = "我要出发送餐、、、" + tableName;
            myHandler.sendMessage(msg);

        } else {
            if (mTaskSend.isShow) {
                speakStr = tableName + " ,请您给我让让路";

            } else {

                speakStr = tableName;
            }

            myHandler.sendMessage(msg);

        }

        //取消水波纹动画
        head.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskingDialog != null) {

//                    showTaskArraivedDialog();//测试

                }
            }
        });
        taskingDialog.show();
        taskingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (dialog != null) {
                    cancelTaskAnima();
                }
            }
        });
        taskingDialog.setCancelable(false);
        taskingDialog.setContentView(contentView);

        Message msg1 = Message.obtain(myHandler);
        msg1.what = MSG_SHOW_TASKING1;
        myHandler.sendMessage(msg1);

        Message msg2 = Message.obtain(myHandler);
        msg2.what = DISMISS_ARRAIVED_DIALOG;
        myHandler.sendMessageDelayed(msg2, 500);

    }

    private void showTaskArraivedDialog() {

        showSettingsCount = 0;
        currentTaskSendState = 1;

        sendMessageCancel();
        stopSpeak(false);

        final List<TaskSend> mTaskSendList = getTaskListByTaskState(0);

        List<TablePoint> mTablePointList = new ArrayList<TablePoint>();
        for (int i = 0; i < mTaskSendList.size() - 1; i++) {

            List<TablePoint> mList = tablePointDao.queryByTablePointId(mTaskSendList.get(i).tablePointId);
            if (mList != null && mList.size() > 0) {
                mTablePointList.add(mList.get(0));
                MyLogcat.showLog("找到点位：" + mList.get(0).toString());
                MyLogcat.showLogArrivedStatus("找到点位：" + mList.get(0).toString());
                FL.e(Constants.show_log, "找到点位：" + mList.get(0).toString());
                FL.e(Constants.show_log_arrived_status, "找到点位：" + mList.get(0).toString());
            } else {
                mTablePointList.add(null);
            }
        }

        TaskSend mTaskSend = null;//只是到达任务中的其中一个
        boolean isFirst = true;
        for (int n = 0; n < mTaskSendList.size() - 1; n++) {
            if (mTaskSendList.get(n).taskState == 0) {
                if (isFirst) {
                    isFirst = false;
                    if (mArraivedTablePoint != null) {
                        MyLogcat.showLog("任务改动前，防止socket多次发送到达消息，过滤");
                        FL.e(Constants.show_log, "任务改动前，防止socket多次发送到达消息，过滤");
                        MyLogcat.showLogArrivedStatus("任务改动前，防止socket多次发送到达消息，过滤");
                        FL.e(Constants.show_log_arrived_status, "任务改动前，防止socket多次发送到达消息，过滤");
                        return;
                    }
                    mArraivedTablePoint = mTablePointList.get(n);

                    mTaskSend = mTaskSendList.get(n);
                    MyLogcat.showLog("到达点位=" + mArraivedTablePoint.toString());
                    MyLogcat.showLog("任务改动前=" + mTaskSend.toString());
                    FL.e(Constants.show_log, "到达点位=" + mArraivedTablePoint.toString());
                    FL.e(Constants.show_log, "任务改动前=" + mTaskSend.toString());

                    MyLogcat.showLogArrivedStatus("到达点位=" + mArraivedTablePoint.toString());
                    MyLogcat.showLogArrivedStatus("任务改动前=" + mTaskSend.toString());
                    FL.e(Constants.show_log_arrived_status, "到达点位=" + mArraivedTablePoint.toString());
                    FL.e(Constants.show_log_arrived_status, "任务改动前=" + mTaskSend.toString());
                    if (mTaskSend.taskState == 0) {
                        mTaskSend.taskState = 1;
                    }
                    mTaskSend = mTaskSendList.get(n);
                    mTaskSendDao.update(mTaskSend);
                    List<TaskSend> mList = mTaskSendDao.queryByTaskLayer(mTaskSend.taskLayer);
                    if (mList != null && mList.size() > 0) {
                        MyLogcat.showLog("任务改动后=" + mTaskSend.toString());
                        MyLogcat.showLogArrivedStatus("任务改动后=" + mTaskSend.toString());
                        FL.e(Constants.show_log, "任务改动后=" + mTaskSend.toString());
                        FL.e(Constants.show_log_arrived_status, "任务改动后=" + mTaskSend.toString());
                    }
                } else if (mTaskSend != null && mTaskSend.tablePointId == mTaskSendList.get(n).tablePointId) {
                    TaskSend tmpTaskSend = mTaskSendList.get(n);

                    if (tmpTaskSend.taskState == 0) {
                        tmpTaskSend.taskState = 1;
                    }
                    mTaskSendDao.update(tmpTaskSend);
                    List<TaskSend> mList = mTaskSendDao.queryByTaskLayer(tmpTaskSend.taskLayer);
                    if (mList != null && mList.size() > 0) {
                        MyLogcat.showLog("任务改动后=" + mTaskSend.toString());
                        MyLogcat.showLogArrivedStatus("任务改动后=" + mTaskSend.toString());
                        FL.e(Constants.show_log, "任务改动后=" + mTaskSend.toString());
                        FL.e(Constants.show_log_arrived_status, "任务改动后=" + mTaskSend.toString());
                    }
                }

            }
        }
        if (mArraivedTablePoint != null) {
            MyLogcat.showLog("已到达的点：" + mArraivedTablePoint.toString());
            MyLogcat.showLogArrivedStatus("已到达的点：" + mArraivedTablePoint.toString());
            FL.e(Constants.show_log, "已到达的点：" + mArraivedTablePoint.toString());
            FL.e(Constants.show_log_arrived_status, "已到达的点：" + mArraivedTablePoint.toString());
        } else {
            MyLogcat.showLog("已到达的点：为空");
            MyLogcat.showLogArrivedStatus("已到达的点：为空");
            FL.e(Constants.show_log, "已到达的点：为空");
            FL.e(Constants.show_log_arrived_status, "已到达的点：为空");
        }

        MyLogcat.showLog("任务个数为" + mTaskSendList.size());
        MyLogcat.showLogArrivedStatus("任务个数为" + mTaskSendList.size());
        FL.e(Constants.show_log, "任务个数为" + mTaskSendList.size());
        FL.e(Constants.show_log_arrived_status, "任务个数为" + mTaskSendList.size());

        if (mArraivedTablePoint != null && (mArraivedTablePoint.tablePoint.equals(mOldTablePoint))) {
            MyLogcat.showLog("111防止socket多次发送到达消息，过滤");
            MyLogcat.showLogArrivedStatus("111防止socket多次发送到达消息，过滤");
            FL.e(Constants.show_log, "111防止socket多次发送到达消息，过滤");
            FL.e(Constants.show_log_arrived_status, "111防止socket多次发送到达消息，过滤");
            return;
        }
        if (mArraivedTablePoint != null) {
            mOldTablePoint = mArraivedTablePoint.tablePoint;
        }
//        if (mTaskSendList == null || mTaskSend == null || mTaskSendList.size() == 0 || mArraivedTablePoint == null) {
        if (mTaskSendList == null || mTaskSend == null || mTaskSendList.size() == 0 || mArraivedTablePoint == null) {

            MyLogcat.showLog("回厨房 任务个数=" + mTaskSendList.size() + ",mTablePoint=" + mArraivedTablePoint);
            MyLogcat.showLogArrivedStatus("回厨房 任务个数=" + mTaskSendList.size() + ",mTablePoint=" + mArraivedTablePoint);
            FL.e(Constants.show_log, "回厨房 任务个数=" + mTaskSendList.size() + ",mTablePoint=" + mArraivedTablePoint);
            FL.e(Constants.show_log_arrived_status, "回厨房 任务个数=" + mTaskSendList.size() + ",mTablePoint=" + mArraivedTablePoint);
            if (taskArraivedDialog != null) {
                taskArraivedDialog.dismiss();
            }
            clearData();
            speakStr = "已到达厨房";
            speakLine();
            return;
        }
        final List<TaskSend> newTaskSendList = getTaskListByTaskState(99);
        if (newTaskSendList.size() > 1) {
            newTaskSendList.remove(newTaskSendList.size() - 1);
        }

        if (taskArraivedDialog == null) {
            taskArraivedDialog = new FullScreenDialog(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        View contentView = inflater.inflate(R.layout.view_task_arraived, null);
        View v_settings = contentView.findViewById(R.id.v_settings);
        v_settings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingsCount += 1;
                if (showSettingsCount == 10) {
                    showSettingsCount = 0;
                    showSettingsDialog(false);
                }
            }
        });
        final ListView listView = (ListView) contentView.findViewById(R.id.list_finish);
        final TextView confirm_to_get = (TextView) contentView.findViewById(R.id.confirm_to_get);


        String layerName = "";

        for (int i = 0; i < mTaskSendList.size() - 1; i++) {

            if (mArraivedTablePoint != null && mArraivedTablePoint.id == mTaskSendList.get(i).tablePointId) {

                switch (mTaskSendList.get(i).taskLayer) {
                    case 0:
                        layerName += "最上层餐盘取餐,";
                        break;
                    case 1:
                        layerName += "第二层餐盘取餐,";//中1层
                        break;
                    case 2:
                        layerName += "第三层餐盘取餐,";//中2层
                        break;
                    case 3:
                        layerName += "最下层餐盘取餐,";
                        break;
                }
            }


        }
        String tableName = SharedPrefsUtil.get(getApplicationContext(), mTaskSend.taskLayer + Constants.TASK_TABLE_NAME, "");

        tableName = Util.updateTableNameStr(tableName);
        speakStr = tableName + "餐来了，请" + layerName + "  、小心烫";

        startSpeak(true, delayTime, speakStr);

        TaskArraivedListAdapter arraivedListAdapter = new TaskArraivedListAdapter(MainActivity.this, newTaskSendList, mTaskSend);
        listView.setAdapter(arraivedListAdapter);


        final TablePoint finalTablePoint = mArraivedTablePoint;

        confirm_to_get.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Util.isNetworkAvailable(getApplicationContext())) {
                    MyToast.showToast(getApplicationContext(), "当前网络已经断开！");
                } else {
                    TaskSend tmpTaskSend = null;
                    for (int m = 0; m < newTaskSendList.size(); m++) {
                        if (finalTablePoint.id == newTaskSendList.get(m).tablePointId) {
                            tmpTaskSend = newTaskSendList.get(m);
                            if (tmpTaskSend.taskState == 1) {
                                tmpTaskSend.taskState = 2;
                            }
                            MyLogcat.showLog("改动前=" + tmpTaskSend.toString());
                            mTaskSendDao.update(tmpTaskSend);
                            List<TaskSend> mList = mTaskSendDao.queryByTaskLayer(tmpTaskSend.taskLayer);
                            if (mList != null && mList.size() > 0) {
                                for (int n = 0; n < mList.size(); n++) {

                                    MyLogcat.showLog("改动后=" + mList.get(n).toString());
                                }

                            }
                        }
                    }
                    if (isServiceConnect(true)) {
                        doStartTask(false);
                    }
                }
            }
        });
        taskArraivedDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                mArraivedTablePoint = null;
                mOldTablePoint = null;
            }
        });

        taskArraivedDialog.show();

        Message msg2 = Message.obtain(myHandler);
        msg2.what = DISMISS_TASKING_DIALOG;
        myHandler.sendMessageDelayed(msg2, 500);

        taskArraivedDialog.setCancelable(false);
        taskArraivedDialog.setContentView(contentView);

    }

    private void showSettingsDialog(boolean isSettings) {
//        mqttSubscribeBarricade();
        getrobotId();

//        Message msg = Message.obtain(myHandler);
//        msg.what = MSG_CHARGING;
//        myHandler.sendMessage(msg);


        if (!isServiceConnect(false)) {
            MyLogcat.showLog("服务未连接");
        }

        if (mSettingsDialog == null) {
            mSettingsDialog = new FullScreenDialog(this);
        }
        int width = RelativeLayout.LayoutParams.MATCH_PARENT;
        int height = RelativeLayout.LayoutParams.MATCH_PARENT;
        if (isSettings) {
            width = RelativeLayout.LayoutParams.MATCH_PARENT;
            height = RelativeLayout.LayoutParams.MATCH_PARENT;
        } else {
            width = DipPxUtils.getScreenWidth(MainActivity.this) * 4 / 5;
            height = width * 3 / 4;

        }
        mSettingsDialog.setWidthAndHeight(width, height);
        if (!MainActivity.this.isFinishing() && !mSettingsDialog.isShowing()) {
            mSettingsDialog.show();
        }
        LayoutInflater inflater = getLayoutInflater();
        View contentView = inflater.inflate(R.layout.activity_setting, null);

        mSettingsDialog.setContentView(contentView);
        mSettingsDialog.setCancelable(false);
        RelativeLayout rl_back = (RelativeLayout) contentView.findViewById(R.id.rl_back);

        tv_electricity = (TextView) contentView.findViewById(R.id.tv_electricity);
        mBatteryView = (BatteryView) contentView.findViewById(R.id.horizontalBattery);

        iv_charging = (ImageView) contentView.findViewById(R.id.iv_charging);
        final RelativeLayout rl_setting_layout = (RelativeLayout) contentView.findViewById(R.id.rl_setting_layout);
        final RelativeLayout rl_volume_layout = (RelativeLayout) contentView.findViewById(R.id.rl_volume_layout);
        final ScrollView sl_layout = (ScrollView) contentView.findViewById(R.id.sl_layout);
        sl_layout.setVisibility(View.GONE);
        rl_setting_layout.setVisibility(VISIBLE);
        rl_volume_layout.setVisibility(View.GONE);

        GridView settingsGv_layout = (GridView) contentView.findViewById(R.id.gv_layout);

        Button mBtn_connect = (Button) contentView.findViewById(R.id.btn_connect);
        Button btn_resetIp = (Button) contentView.findViewById(R.id.btn_resetIp);

        Button mBtn_interrupt = (Button) contentView.findViewById(R.id.btn_interrupt);
        Button mBtn_send = (Button) contentView.findViewById(R.id.btn_send);
        EditText mEt_sendContent = (EditText) contentView.findViewById(R.id.et_sendContent);
        tv_receive_message = (TextView) contentView.findViewById(R.id.tv_receive_message);
        tv_receive_message.setText("");

        rl_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rl_volume_layout.getVisibility() == VISIBLE) {
                    rl_volume_layout.setVisibility(View.GONE);
                    rl_setting_layout.setVisibility(VISIBLE);
                } else {
                    mSettingsDialog.dismiss();
                }
            }
        });
        mBtn_connect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isServiceConnect(false)) {
                    MyLogcat.showLog("服务未连接");
                }
            }
        });
//=============================================================

        //初始化音量控制
        seekBar = (SeekBar) contentView.findViewById(R.id.seekBar);
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //获取系统最大音量
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

        //=========================================================
        final List<Mode> mModeList = new ArrayList<Mode>();
        mModeList.clear();

       /* mode = new Mode("移动", R.drawable.ic_move_point);
        mModeList.add(mode);
        mode = new Mode("取消移动", R.drawable.ic_cancel_task);
        mModeList.add(mode);
        mode = new Mode("急停", R.drawable.ic_stop);
        mModeList.add(mode);
        mode = new Mode("取消急停", R.drawable.ic_cancel_stop);
        mModeList.add(mode);*/
        Mode mode = new Mode("去充电", R.drawable.ic_charge);
        mModeList.add(mode);
        mode = new Mode("去厨房", R.drawable.ic_kitchen);
        mModeList.add(mode);
        mode = new Mode("清空任务", R.drawable.ic_clear);
        mModeList.add(mode);
        mode = new Mode("显示标题", R.drawable.ic_system_bar);
        mModeList.add(mode);
        mode = new Mode("获取状态", R.drawable.ic_status);
        mModeList.add(mode);
        mode = new Mode("声音设置", R.drawable.ic_volume);
        mModeList.add(mode);
        if (isSettings) {
            mode = new Mode("点位录入", R.drawable.ic_entry);
            mModeList.add(mode);
            mode = new Mode("设置层数", R.drawable.ic_floor);
            mModeList.add(mode);
        }
        mode = new Mode("修改密码", R.drawable.ic_password);
        mModeList.add(mode);
        mode = new Mode("升级版本", R.drawable.ic_update);
        mModeList.add(mode);


        final SettingsGridAdapter mAdapter = new SettingsGridAdapter(this, mModeList, myHandler);
        settingsGv_layout.setAdapter(mAdapter);
        settingsShowBar = true;
        settingsGv_layout.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mModeList.size() == 8 && position == mModeList.size() - 1) {
                    keyBoardCancle();
                    //录入点位
                    Intent intent = new Intent(MainActivity.this, EntryTablePointActivity.class);
                    startActivity(intent);
                } else if (mModeList.size() == 8 && position == mModeList.size() - 2) {
                    //设置层数
                    MyToast.showToast(getApplication(), "设置餐盘层数");
                    showPlatePliesDialog();
                } else {

                    MainActivity.this.fdoSettingsItemClick(mAdapter, position, rl_setting_layout, rl_volume_layout, sl_layout);
                }

            }
        });

        mTxtVersionName = (TextView) contentView.findViewById(R.id.txt_version_name);//版本号
        //显示版本号
        String versionName = Util.getVersionName(this);
        if (!TextUtils.isEmpty(versionName)) {
            mTxtVersionName.setText(versionName);
        }

        mSettingsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                myHandler.removeMessages(MSG_CHARGING);
                if (taskingDialog != null && taskingDialog.isShowing()) {
                    Message msg = Message.obtain(myHandler);
                    msg.what = MSG_SHOW_TASKING1;
                    myHandler.sendMessage(msg);

                }
            }
        });
        cancelTaskAnima();
    }

    private void cancelTaskAnima() {
        if (wave != null) {
            wave.stop();
        }
        if (task_gif != null) {
            task_gif.stopAnimation();
        }
        myHandler.removeMessages(MSG_SHOW_TASKING1);
        myHandler.removeMessages(MSG_SHOW_TASKING2);
    }

    //强制隐藏软键盘
    public void keyBoardCancle() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void fdoSettingsItemClick(SettingsGridAdapter mAdapter, int pos, RelativeLayout rl_setting_layout, RelativeLayout rl_volume_layout, ScrollView sl_layout) {

        keyBoardCancle();
        Intent intent = null;

        switch (pos) {
            case 0://去充电
                MyToast.showToast(getApplication(), "去充电");
                List<TablePoint> mList = tablePointDao.queryAllPointByPointType(2);
                if (mList != null && mList.size() > 0) {
                    MyToast.showToast(getApplication(), "去充电");
                    startSpeak(false, 0, "我要去充电");
                    Message msg = Message.obtain(myHandler);
                    msg.what = MSG_GO_CHARGING;
                    msg.obj = mList.get(0).tablePoint;
                    myHandler.sendMessageDelayed(msg, 2000);

                } else {
                    MyToast.showToast(getApplication(), "请先去后台录入充电桩");
                }
                if (mSettingsDialog != null) {
                    mSettingsDialog.dismiss();
                }
                break;
            case 1://去厨房
                MyToast.showToast(getApplication(), "去厨房");
                List<TablePoint> mList2 = tablePointDao.queryAllPointByPointType(1);
                MyLogcat.showLog("mList2==size=" + mList2.size());
                if (mList2 != null && mList2.size() > 0) {
                    MyToast.showToast(getApplication(), "去厨房");
                    startSpeak(false, 0, "我要回厨房");
                    MyLogcat.showLog("1111111111");
                    Message msg = Message.obtain(myHandler);
                    msg.what = MSG_GO_KITCHEN;
                    msg.obj = mList2.get(0).tablePoint;
                    myHandler.sendMessageDelayed(msg, 2000);


                } else {
                    MyToast.showToast(getApplication(), "请先录入厨房点位");
                }
                if (mSettingsDialog != null) {
                    mSettingsDialog.dismiss();
                }
                break;
            case 2://取消全部
                clearData();
                mSettingsDialog.dismiss();
                break;
            case 3://显示标题栏

                if (settingsShowBar) {
                    MyToast.showToast(getApplication(), "显示标题栏");
                    Util.showDorbar(getApplication(), true);
                } else {
                    MyToast.showToast(getApplication(), "隐藏标题栏");
                    Util.showDorbar(getApplication(), false);
                }
                mAdapter.setIsShowBar(settingsShowBar);
                mAdapter.notifyDataSetChanged();
                settingsShowBar = !settingsShowBar;
                break;

            case 4://获取状态
                sl_layout.setVisibility(VISIBLE);
                MyToast.showToast(getApplication(), "获取状态");
                HttpMessage httpMessage = new HttpMessage();
                httpMessage.requestType = "GET";
                httpMessage.apiName = "taskstatus";
                httpMessage.message = "";
                sendMessage(httpMessage);
                break;
            case 5://调节音量
                sl_layout.setVisibility(View.GONE);
                rl_setting_layout.setVisibility(View.GONE);
                rl_volume_layout.setVisibility(VISIBLE);
                break;
            case 6://录入点位
                intent = new Intent(MainActivity.this, EntryTablePointActivity.class);
                startActivity(intent);
                if (mSettingsDialog != null) {
                    mSettingsDialog.dismiss();
                }
                break;
            case 7://设置层数
                MyToast.showToast(getApplication(), "设置餐盘层数");
                showPlatePliesDialog();
                break;
            /*修改密码*/
            case 8:
//                HttpMessage httpMessage2 = new HttpMessage();
//                httpMessage2.requestType = "GET";
//                httpMessage2.apiName = "cancel";
//                httpMessage2.message = "";
//                sendMessage(httpMessage2);
                showModifyPasswordDialog();
                break;
            /*升级版本*/
            case 9:
                UpdateManager mUpdateManager = new UpdateManager(this);
                mUpdateManager.checkVersion(true);
                break;
            default:
                break;
        }
    }

    /**
     * 屏蔽系统自带的调节音量控件
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0);
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0);
        }
        return true;
    }

    private void startSpeak(boolean repeatSpeak, long delayMillis, String speakStr) {

        myHandler.removeMessages(MSG_WHAT_SPEAK);
        stopSpeak(false);

        Message msg = Message.obtain(myHandler);
        msg.what = MSG_WHAT_SPEAK;
        if (repeatSpeak || delayMillis == 120000) {
            if (delayMillis == 120000) {
                msg.arg2 = NOT_REPEAT_SPEAK;
                myHandler.sendMessageDelayed(msg, delayMillis);
                MyLogcat.showLog("延时发送异常");
            } else {
                if (!isHardEstop) {
                    myHandler.sendMessageDelayed(msg, delayMillis);
                }
            }
        }
        this.speakStr = speakStr;
        speakLine();

    }

    private void stopSpeak(Boolean clearStr) {

        myHandler.removeMessages(MSG_WHAT_SPEAK);

        try {

            if (mTts != null) {
                mTts.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearSharedPrefs() {

        SharedPrefsUtil.put(getApplicationContext(), 0 + Constants.TASK_TABLE_NAME, "");
        SharedPrefsUtil.put(getApplicationContext(), 1 + Constants.TASK_TABLE_NAME, "");
        SharedPrefsUtil.put(getApplicationContext(), 2 + Constants.TASK_TABLE_NAME, "");
        SharedPrefsUtil.put(getApplicationContext(), 3 + Constants.TASK_TABLE_NAME, "");
    }

    private void clearData() {
        mArraivedTablePoint = null;
        mOldTablePoint = null;//防止重复收到到达的消息

        currentTaskSendState = -1;
        if (exceptionDialog != null) {
            exceptionDialog.dismiss();
        }
        if (taskingDialog != null) {
            taskingDialog.dismiss();
        }
        if (taskArraivedDialog != null) {
            taskArraivedDialog.dismiss();
        }
        if (cancelTaskDialog != null) {
            cancelTaskDialog.dismiss();
        }

        myHandler.removeMessages(MSG_WHAT_SPEAK);
        if (mSettingsDialog != null && mSettingsDialog.isShowing()) {

        } else {
            myHandler.removeMessages(MSG_CHARGING);
            myHandler.removeMessages(MSG_CHARGING_STATE);
        }
        myHandler.removeMessages(MSG_SHOW_TASKING2);
        myHandler.removeMessages(MSG_SHOW_TASKING1);

        stopSpeak(true);

        clearSharedPrefs();
        cancelTaskAnima();
        doClickListItem(0);
        doClickGridItem(-1);

        HttpMessage httpMessage = new HttpMessage();
        httpMessage.requestType = "GET";
        httpMessage.apiName = "taskstatus";
        httpMessage.message = "";
        sendMessage(httpMessage);
        sendMessageCancel();
        mTaskSendDao.deleteAllTaskSend();
    }

    /**
     * 阻止程序在回退到最后一步被杀死，而是保持后台运行
     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(false);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
    private class VolumeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.media.VOLUME_CHANGED_ACTION")) {
                if (mSettingsDialog != null && mSettingsDialog.isShowing()) {
                    int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                    seekBar.setProgress(currentVolume);
                }
            } else if (action.equalsIgnoreCase("android.net.conn.CONNECTIVITY_CHANGE")) {
                MyLogcat.showLog("网络变化");
                FL.e(Constants.show_log, "网络变化");
                if (!PublicUtils.isConnected(context)) {

                    MyLogcat.showLog("网络变化Network is disconnected !");
                    FL.e(Constants.show_log, "网络变化Network is disconnected !");

                } else {
//                    mqttSubscribeBarricade();
                    getrobotId();
                    MyLogcat.showLog("网络变化Network is connected !");
                    FL.e(Constants.show_log, "网络变化Network is connected !");


                }


            }
        }
    }

    @Override
    protected void onDestroy() {
        MyLogcat.showLog("onDestroy解绑服务");
        //解绑服务
        unbindService(serviceConnection);

        if (volumeReceiver != null) {
            unregisterReceiver(volumeReceiver);
        }
        mTaskSendDao.deleteAllTaskSend();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void speakLine() {
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                mTts = new TextToSpeech(MainActivity.this, MainActivity.this);

            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        } else if (requestCode == CRUISE_RESULT_CODE && resultCode == 200) {
            if (data != null && data.getStringExtra("postJson") != null) {
                startCruise(data.getStringExtra("postJson"));
            }
        }
    }

    @Override
    public void onInit(int status) {
        speakStr();
    }

    private void showTaskingAnima(MainActivity activity, boolean showWave, long delayedTime) {
        if (taskingDialog != null && taskingDialog.isShowing()) {
            if (showWave) {
                rl_gif.setVisibility(View.GONE);
                task_gif.stopAnimation();

                activity.rl_wave.setVisibility(VISIBLE);
                activity.wave.stop();
                Message msg1 = Message.obtain(myHandler);
                msg1.what = MSG_SHOW_TASKING2;
                msg1.obj = false;
                myHandler.sendMessageDelayed(msg1, delayedTime);
            } else {
                activity.rl_gif.setVisibility(VISIBLE);
                activity.task_gif.startAnimation();

                activity.rl_wave.setVisibility(View.GONE);
                activity.wave.stop();
                Message msg1 = Message.obtain(myHandler);
                msg1.what = MSG_SHOW_TASKING1;
                myHandler.sendMessageDelayed(msg1, delayedTime);
            }
        }
    }

    private void speakStr() {
        isSpeaking = true;
        myHandler.removeMessages(MSG_SPEAK_OVER);
        myHandler.removeMessages(MSG_SHOW_TASKING2);
        Message msg = Message.obtain(myHandler);
        msg.what = MSG_SPEAK_OVER;
        myHandler.sendMessageDelayed(msg, 9000);
        showTaskingAnima(this, true, 10000);

        if (!TextUtils.isEmpty(speakStr)) {
            MyLogcat.showLog("###急停状态=" + isHardEstop + " ,noSpeakCancelHardEstop=" + noSpeakCancelHardEstop + ",语音=" + speakStr);
            if (cancelTaskDialog != null && cancelTaskDialog.isShowing()) {

            } else {

                if (isHardEstop) {
                    if (!noSpeakCancelHardEstop) {
                        noSpeakCancelHardEstop = true;
                        if (!speakStr.contains("拥挤")) {

                            mTts.speak("请取消急停按钮", TextToSpeech.QUEUE_FLUSH, null);
                            myHandler.removeMessages(MSG_WHAT_SPEAK);
                        }
                    }

                } else {

                    mTts.speak(speakStr, TextToSpeech.QUEUE_FLUSH, null);
                    if (speakStr.contains("、、、")) {
                        String[] speakStrArray = speakStr.split("、、、");
                        if (speakStrArray.length > 1) {
                            speakStr = speakStrArray[1];
                        }
                    }
                }
            }

            if (exceptionDialog != null && exceptionDialog.isShowing()) {
                mTts.speak("我现在遇到了点小麻烦，请工作人员帮帮我", TextToSpeech.QUEUE_FLUSH, null);
            }
        }

    }

    /**
     * 利用mqtt订阅路障topic发出语音提示
     */
    private void mqttSubscribeBarricade() {
        mqttManager = MqttManager.getInstance();
        clientId = clientId + "@@@" + System.currentTimeMillis();
        //MQTT连接
        final boolean b = mqttManager.creatConnect(Constants.URL, userName, password, clientId);
        MyLogcat.showLog("mqtt_isConnected: " + b);
        /*创建线程池，最大线程数为5，间隔1秒执行*/
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        scheduledThreadPool.schedule(new Runnable() {
            @Override
            public void run() {
                MyLogcat.showLog("mqtt_订阅结果: " + b);
                if (b) {
//                    mqttManager.subscribe(Constants.topicBarricade, 0);
//                    mqttManager.subscribe(Constants.topicCruise, 1);

                    MyLogcat.showLog("Subscribing to topic \"" + Constants.robotId);

                    mqttManager.subscribe(Constants.topicBarricade, 0);
                    mqttManager.subscribe(Constants.topicCruise, 1);
                    Log.e("-----mqttSubscribe-----",Constants.topicCruise);

//                    MqttManager.getInstance().publish(topicBarricade, 0, str.getBytes());
                } else {
                    MyLogcat.showLog("Mqtt连接失败！");
                }
            }
        }, 1, TimeUnit.SECONDS);



      /*  scheduledThreadPool.schedule(new Runnable() {
            @Override
            public void run() {
//                MyLogcat.showLog("mqtt_订阅结果: " + b);
                if (b) {
                    mqttManager.subscribe(Constants.topicCruise, 1);
//                    MqttManager.getInstance().publish(topicBarricade, 0, str.getBytes());
                } else {
                    MyLogcat.showLog("Mqtt连接失败！");
                }
            }
        }, 1, TimeUnit.SECONDS);*/

    }

    /**
     * 订阅接收到的消息
     * 这里的Event类型可以根据需要自定义, 这里只做基础的演示
     *
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MqttMessage message) {
        MyLogcat.showLog("收到+++++++++ ！" + message.toString());

        try {
            if (message != null && !TextUtils.isEmpty(message.toString())) {
                String mesStr = message.toString();
                if (mesStr.contains("action") && mesStr.contains("sound")) {
                    JSONObject jsonObject = new JSONObject(message.toString());
                    String action = jsonObject.getString("action");
                    if (action != null && action.equals("arrive")) {
                        String sound = jsonObject.getString("sound");
                        if (!TextUtils.isEmpty(sound)) {
                            startPlay(sound);
                        }
                    } else if (action != null && action.equals("leave")) {
                        stopPlay();
                    }
                } else {
                    JSONObject jsonObject = new JSONObject(message.toString());
                    String code = jsonObject.getString("code");
                    if ("01005".equals(code)) {
                        final String str = "当前道路有些拥挤，请给我让让路";
                        startSpeak(false, 0, str);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void httpResponse(final String response, final int type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (type == 0) {//语音列表获取成功
                        voiceList = response;
                        if (voiceList != null && !voiceList.equals("")) {
                            startGetPoints();
                        } else {
                            MyToast.showToast(MainActivity.this, "请先添加语音");
                        }
                    } else if (type == 1) {//点列表获取成功
                        String pointList = response;
                        if (pointList != null && !pointList.equals("")) {
                            Intent intent = new Intent(MainActivity.this, CruiseActivity.class);
                            intent.putExtra("voices", voiceList);
                            intent.putExtra("points", pointList);
                            startActivityForResult(intent, CRUISE_RESULT_CODE);
                        } else {
                            MyToast.showToast(MainActivity.this, "请先添加点位");
                        }
                    } else if (type == 2) {//巡游接口回调
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getInt("code") == 0) {
                            Constants.robotId = jsonObject.getJSONObject("data").getString("sn");
                            Constants.topicNotification = PublicUtils.getNewTopic(Constants.topicNotification, Constants.robotId);
                            Constants.topicCommand = PublicUtils.getNewTopic(Constants.topicCommand, Constants.robotId);
                            Constants.topicBarricade = PublicUtils.getNewTopic(Constants.topicBarricade, Constants.robotId);
                            Constants.topicCruise = PublicUtils.getNewTopic(Constants.topicCruise, Constants.robotId);

                            Log.d("Constants.topicCruise", Constants.topicCruise);

                        }

                    } else if (type == 3) {


                    } else if (type == -1) {
                        MyToast.showToast(MainActivity.this, response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }


    private String voiceList = "";

    public void startGetVoice() {
        String url = "http://" + HttpService.hostName + ":8809/api/" + "cruise_sound";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseStr = response.body().string();
                    Log.d("-----okhttp-------", "onResponse: " + responseStr);

                    voiceList = responseStr;
                    JSONArray jsonArray = new JSONArray(responseStr);
                    if (jsonArray.length() <= 0) {
                        MyToast.showToast(MainActivity.this, "请先添加巡游语音");
                    } else {
                        startGetPoints();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void startGetPoints() {
        //获取点位列表
     /*   HttpMessage httpMessage = new HttpMessage();
        httpMessage.requestType = "GET";
        httpMessage.apiName = "queryMarkerList";
        httpMessage.message = "";
        sendMessage(httpMessage);*/
        String url = "http://" + HttpService.hostName + ":8809/api/" + "queryMarkerList";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseStr = response.body().string();
                    Log.d("-----okhttp-------", "onResponse: " + responseStr);
                    JSONObject jsonObject = new JSONObject(responseStr);
                    String pointList = jsonObject.getString("message");
                    if (pointList != null && !pointList.equals("")) {
                        Intent intent = new Intent(MainActivity.this, CruiseActivity.class);
                        intent.putExtra("voices", voiceList);
                        intent.putExtra("points", pointList);
                        startActivityForResult(intent, CRUISE_RESULT_CODE);
                    } else {
                        MyToast.showToast(MainActivity.this, "请先添加点位");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void startCruise(String data) {
        try {
            SharedPrefsUtil.put(MainActivity.this, "cruise_data", data);
            Log.e("-activityResult-----", data);

           /* String url = "http://" + HttpService.hostName + ":8809/api/" + "start_cruise";
            MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
            Request request = new Request.Builder().url(url).post(RequestBody.create(mediaType, data)).build();
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("---response--cruise--", response.body().string());
                }
            });*/


            HttpMessage httpMessage = new HttpMessage();
            httpMessage.requestType = "POST";
            httpMessage.apiName = "start_cruise";
            httpMessage.message = data;
            sendMessage(httpMessage);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* private void startCruise(String data) {
        try {
            SharedPrefsUtil.put(MainActivity.this, "cruise_data", data);
//        MyToast.showToast(MainActivity.this, data);
            Log.e("-activityResult-----", data);
      *//*  HttpMessage httpMessage = new HttpMessage();
        httpMessage.requestType = "POST";
        httpMessage.apiName = "start_cruise";
        httpMessage.message = data;
        sendMessage(httpMessage);*//*

            String url = "http://" + HttpService.hostName + ":8809/api/" + "start_cruise";

            CruiseJsonPost cruiseJsonPost = JsonUtils.fromJson(data, CruiseJsonPost.class);
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("type", cruiseJsonPost.getType())
                    .add("time", cruiseJsonPost.getTime() + "")
                    .add("times", cruiseJsonPost.getTimes() + "")
                    .add("times", cruiseJsonPost.getTimes() + "")
                    .build();
            Request request = new Request.Builder().url(url).post(requestBody).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("---response--cruise--", response.body().string());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    private void addVoice() {
        String path = Environment.getExternalStorageDirectory() + "/Movies/cruise_food.wav";

//        Uri uri = Uri.parse("android.resource://com.yunji.deliveryman/" + R.raw.cruise_1);
        File file = new File(path);
        Log.e("------file-----", file.exists() ? "存在" : "不存在" + "   " + file.getAbsolutePath());
        OkHttpClient client = new OkHttpClient();
        MediaType MEDIA_TYPE_PNG = MediaType.parse("audio/x-wav");
        RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG, file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("filename", file.getName())
                .addFormDataPart("file", "head_image", fileBody)
                .addFormDataPart("taskType", "cruise")
                .addFormDataPart("lang", "zh")
                .build();
        Request request = new Request.Builder()
                .url("http://" + HttpService.hostName + ":8809/api/" + "upload_custom_sound")
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("-----okhttp-------", "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("-----okhttp-------", "onResponse: " + response.body().string());
            }
        });

    }


    MediaPlayer mediaPlayer;

    private void startPlay(final String voiceName) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    mediaPlayer = new MediaPlayer();

//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    //3 准备播放

                    //3.1 设置一个准备完成的监听
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            // 4 开始播放
                            mediaPlayer.start();
                        }
                    });
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            if (mediaPlayer != null) {
                                mediaPlayer.release();
                                mediaPlayer = null;
                            }
                        }
                    });
                    mediaPlayer.setDataSource("http://192.168.0.157:8809/customsound/cruise/zh/" + voiceName + ".wav");
                    mediaPlayer.prepareAsync();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

    private void stopPlay() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (Exception e) {

        }
    }


    private void getrobotId() {
        String url =   "http://" + HttpService.hostName + ":8809/api/"+"robot_no";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("-----okhttp------", "onFailure: ");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String robtId=response.body().string();
                Log.d("-----okhttp------", "onResponse: " + robtId);


                Constants.topicNotification = "robot/mirages/" + robtId + "/topic/notification";
                Constants.topicCommand = "robot/mirages/" + robtId + "/topic/command";
                Constants.topicBarricade = "robot/yunhai/" + robtId + "/topic/notification";//订阅路障话题
                Constants.topicCruise = "robot/yunhai/" + robtId + "/topic/robot_cruise_info";//订阅巡游话题
                mqttSubscribeBarricade();

            }
        });


    }


    Dialog estopDialog;
    private void showEstopDialog(){
        if (estopDialog==null || !estopDialog.isShowing()){
            estopDialog=  DialogUtil.estopDialog(MainActivity.this);
        }
    }

    private void stopShowEstopDialog(){
        if (estopDialog!=null ){
            estopDialog.cancel();
            estopDialog=null;
        }
    }


}
