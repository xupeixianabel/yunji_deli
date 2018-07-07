package com.yunji.deliveryman.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.adapter.CruiseAdapter;
import com.yunji.deliveryman.adapter.CruiseDialogAdapter;
import com.yunji.deliveryman.base.RecyclerItemCallback;
import com.yunji.deliveryman.bean.CruiseJsonPost;
import com.yunji.deliveryman.utils.JsonUtils;
import com.yunji.deliveryman.utils.MyToast;
import com.yunji.deliveryman.utils.SharedPrefsUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.view.View.OnClickListener;


public class CruiseActivity extends Activity {
    ArrayList<CruiseJsonPost.DataBean.TargetsBean> list;
    RecyclerView rv_cruise;
    Button bt_start;

    String choosedVoice;
    String choosedPoint;

    List<String> voicesList;
    List<String> pointsList;
    private Dialog dialog;
    CruiseAdapter cruiseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cruise);
        try {
            initView();
            getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        rv_cruise = findViewById(R.id.rv_cruise);
        bt_start = findViewById(R.id.bt_start);
        bt_start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cruiseAdapter!=null && cruiseAdapter.getDataSource()!=null && cruiseAdapter.getDataSource().size() < 2) {
                    MyToast.showToast(CruiseActivity.this, "请先添加巡游点");
                } else {
                    dialogSettingShow();
                }
            }
        });
    }


    private void getData() {
        try {
            String voices = getIntent().getStringExtra("voices");
            String points = getIntent().getStringExtra("points");

//            MyToast.showToast(CruiseActivity.this, voices + "\n\n" + points);
//            Gson gson = new Gson();
//            voicesList = gson.fromJson(voices, new TypeToken<ArrayList<String>>() {
//            }.getType()); //将json字符串转换成List集合
//            pointsList = gson.fromJson(points, new TypeToken<ArrayList<String>>() {
//            }.getType()); //将json字符串转换成List集合


            voicesList=new ArrayList<String>();
            JSONArray jsonArray=new JSONArray(voices);
            for(int i=0;i<jsonArray.length();i++){
                String voiceName=(String)  (jsonArray.get(i));
                if (voiceName.contains(".")){
                    voiceName=voiceName.substring(0,voiceName.lastIndexOf("."));
                }
                voicesList.add (voiceName);
            }

            pointsList=new ArrayList<String>();
            JSONObject poinObject=new JSONObject(points);
            for (Iterator<String> it = poinObject.keys(); it.hasNext(); ) {
                String key = it.next();
                pointsList.add(key);
            }


            /*voicesList = new ArrayList<String>();
            voicesList.add("cruise_1");
            voicesList.add("cruise_2");
            voicesList.add("cruise_3");

            pointsList = new ArrayList<String>();
            pointsList.add("point_1");
            pointsList.add("point_2");
            pointsList.add("point_3");*/


            setAdatpter();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setAdatpter() {
        list = SharedPrefsUtil.getPointList(CruiseActivity.this);
        cruiseAdapter = new CruiseAdapter(CruiseActivity.this);
        if (list != null && list.size() > 0) {
            for (int i=list.size()-1;i>=0;i--){
                if (!pointsList.contains(list.get(i).getMarker())){
                    list.remove(i);
                }
            }
        } else {
            list = new ArrayList<CruiseJsonPost.DataBean.TargetsBean>();
        }

        list.add(new CruiseJsonPost.DataBean.TargetsBean());
        cruiseAdapter.setData(list);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CruiseActivity.this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(CruiseActivity.this, 4);


        cruiseAdapter.setRecItemClick(new RecyclerItemCallback<CruiseJsonPost.DataBean.TargetsBean, RecyclerView.ViewHolder>() {
            @Override
            public void onItemClick(int position, CruiseJsonPost.DataBean.TargetsBean model, int tag, RecyclerView.ViewHolder holder) {
                super.onItemClick(position, model, tag, holder);
                //添加一个新的点：diagshow
                if (tag==0){
//                    list.remove(position);
                }else {
                    dialogChoosePointShow();

                }

                /*if (position==list.size()){
                    dialogChoosePointShow();
                }else if (position<list.size()){
                    list.remove(position);
                }*/
            }
        });
        rv_cruise.setLayoutManager(gridLayoutManager);
        rv_cruise.setAdapter(cruiseAdapter);
    }


    int timesOrtime = 1;
    int timeStay = 10;

    private void dialogSettingShow() {
        dialog = new Dialog(this, R.style.myDialog);
        dialog.setContentView(R.layout.dialog_cruise_set);


        View in_mode_time = dialog.findViewById(R.id.in_mode_time);
        final TextView tv_mode_time = in_mode_time.findViewById(R.id.tv_content);
        in_mode_time.findViewById(R.id.iv_up).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timesOrtime <= 100) {
                    timesOrtime += 1;
                    tv_mode_time.setText(timesOrtime + "");
                }
            }
        });
        in_mode_time.findViewById(R.id.iv_down).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timesOrtime > 1) {
                    timesOrtime -= 1;
                    tv_mode_time.setText(timesOrtime + "");
                }
            }
        });

        final TextView tv_mode_unit = dialog.findViewById(R.id.tv_mode_unit);
        final RadioGroup rg_mode = dialog.findViewById(R.id.rg_time_mode);
        final RadioGroup rg_voice_mode = dialog.findViewById(R.id.rg_voice_mode);
        rg_mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_top) {
                    tv_mode_unit.setText("分钟");
                } else {
                    tv_mode_unit.setText("次");
                }
            }
        });
        View in_stay_time = dialog.findViewById(R.id.in_stay_time);
        final TextView tv_mode_stay = in_stay_time.findViewById(R.id.tv_content);
        tv_mode_stay.setText(timeStay + "");
        in_stay_time.findViewById(R.id.iv_up).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeStay <= 1000) {
                    timeStay += 10;
                    tv_mode_stay.setText(timeStay + "");
                }
            }
        });
        in_stay_time.findViewById(R.id.iv_down).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeStay > 10) {
                    timeStay -= 10;
                    tv_mode_stay.setText(timeStay + "");
                }
            }
        });


        dialog.findViewById(R.id.tv_cacel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_confirm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                   /* JSONObject jsonOuter = new JSONObject();
                    if (rg_mode.getCheckedRadioButtonId() == R.id.rb_top) {
                        jsonOuter.put("type", "time");
                        jsonOuter.put("time", timesOrtime);
                    } else {
                        jsonOuter.put("type", "times");
                        jsonOuter.put("times", timesOrtime);
                    }
                    JSONObject data = new JSONObject();
                    data.put("name", "default");

                    if (rg_voice_mode.getCheckedRadioButtonId() == R.id.rb_speak_alltime) {
                        data.put("sound_type", "async");
                    } else {
                        data.put("sound_type", "sync");
                    }
                    JSONArray targets = new JSONArray();
                    for (int i = 0; i < list.size() - 1; i++) {
                        PointCruise pointCruise = list.get(i);
                        if (pointCruise.getMarker() != null) {
                            JSONObject maker = new JSONObject();
                            maker.put("marker", pointCruise.getMarker());
                            maker.put("sound", pointCruise.getSound());
                        }
                    }
                    data.put("targets", targets);
                    jsonOuter.put("data", data);
                    String postJson = JsonUtils.toJson(jsonOuter);
                    result(postJson);*/


                    CruiseJsonPost cruiseJsonPost=new CruiseJsonPost();
                    if (rg_mode.getCheckedRadioButtonId() == R.id.rb_top) {
                        cruiseJsonPost.setType("time");
                        cruiseJsonPost.setTime(timesOrtime);
                    } else {
                        cruiseJsonPost.setType("times");
                        cruiseJsonPost.setTimes(timesOrtime);
                    }

                    cruiseJsonPost.setDwell_time(Integer.parseInt(tv_mode_stay.getText().toString().trim()));
                    CruiseJsonPost.DataBean dataBean=new CruiseJsonPost.DataBean();
                    dataBean.setName("default");
                    if (rg_voice_mode.getCheckedRadioButtonId() == R.id.rb_speak_alltime) {
                        dataBean.setSound_type("async");
                    } else {
                        dataBean.setSound_type("sync");
                    }

                   /* ArrayList<CruiseJsonPost.DataBean.TargetsBean> arrayList=new ArrayList<CruiseJsonPost.DataBean.TargetsBean>();
                    for (int i = 0; i < list.size() - 1; i++) {
                        CruiseJsonPost.DataBean.TargetsBean targetsBean=new CruiseJsonPost.DataBean.TargetsBean();
                        CruiseJsonPost.DataBean.TargetsBean pointCruise=list.get(i);
                        if (pointCruise.getMarker() != null) {
                            targetsBean.setMarker( pointCruise.getMarker());
                            targetsBean.setSound( pointCruise.getSound());
                            arrayList.add(targetsBean);
                        }
                    }*/

                    List<CruiseJsonPost.DataBean.TargetsBean> arrayList = cruiseAdapter.getDataSource();
                    arrayList.remove(arrayList.size()-1);

                    SharedPrefsUtil.savePointList(CruiseActivity.this,arrayList);
                    dataBean.setTargets(arrayList);
                    cruiseJsonPost.setData(dataBean);
                    String result = JsonUtils.toJson(cruiseJsonPost);
                    result(result);
                } catch (Exception e) {

                }
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void result(String postJson) {
        Intent intent = new Intent();
        intent.putExtra("postJson", postJson);
        setResult(200, intent);
        finish();
    }


    int dialogPosition;

    private void dialogChoosePointShow() {
        if (isFinishing()) return;
        dialogPosition = 0;
        dialog = new Dialog(this, R.style.myDialog);
        dialog.setContentView(R.layout.dialog_cruise);

        final TextView tv_left = dialog.findViewById(R.id.tv_left);
        final TextView tv_mid = dialog.findViewById(R.id.tv_mid);
        final TextView tv_right = dialog.findViewById(R.id.tv_right);
//        EditText et_search = dialog.findViewById(R.id.et_search);
        RecyclerView rv_choose = dialog.findViewById(R.id.rv_choose);

        final CruiseDialogAdapter adapter = new CruiseDialogAdapter(CruiseActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CruiseActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_choose.setLayoutManager(linearLayoutManager);
        rv_choose.setAdapter(adapter);

        adapter.setData(pointsList);
        tv_left.setVisibility(View.INVISIBLE);
        tv_mid.setText("请选择点位");
        tv_right.setText("下一步");

        tv_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogPosition == 0) {
                    return;
                } else {
                    dialogPosition = 0;
                    adapter.setData(pointsList);
                    tv_left.setVisibility(View.INVISIBLE);
                    tv_mid.setText("请选择点位");
                    tv_right.setText("下一步");
                    adapter.setType(0);
                }
            }
        });


        tv_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogPosition == 0) {
                    dialogPosition = 1;
                    choosedPoint = adapter.getChoosedStr();
                    adapter.setData(voicesList);
                    adapter.notifyDataSetChanged();
                    tv_left.setVisibility(View.VISIBLE);
                    tv_mid.setText("请选择语音");
                    tv_right.setText("完成");
                    adapter.setType(1);

                } else if (dialogPosition == 1) {
                    String tempName = adapter.getChoosedStr();
                    if (tempName.contains(".")) {
                        choosedVoice = tempName.substring(0, tempName.lastIndexOf("."));
                    } else {
                        choosedVoice = tempName;
                    }
                    dialog.dismiss();
                    addCruiseList();
                }
            }
        });


        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void addCruiseList() {
//        List<CruiseJsonPost.DataBean.TargetsBean> adapterList = cruiseAdapter.getDataSource();
//        adapterList.add(adapterList.size()-1,new CruiseJsonPost.DataBean.TargetsBean(choosedPoint, choosedVoice));
//        cruiseAdapter.notifyDataSetChanged();
        cruiseAdapter.addElement(cruiseAdapter.getItemCount()-1,new CruiseJsonPost.DataBean.TargetsBean(choosedPoint, choosedVoice));
    }


    @Override
    protected void onDestroy() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        super.onDestroy();
    }

    //强制隐藏软键盘
    private void keyBoardCancle() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(CruiseActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
