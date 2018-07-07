package com.yunji.deliveryman.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.yunji.deliveryman.R;
import com.yunji.deliveryman.adapter.EntryGridAdapter;
import com.yunji.deliveryman.common.Constants;
import com.yunji.deliveryman.db.TablePoint;
import com.yunji.deliveryman.db.TablePointDao;
import com.yunji.deliveryman.other.log.FL;
import com.yunji.deliveryman.utils.DipPxUtils;
import com.yunji.deliveryman.utils.MyLogcat;
import com.yunji.deliveryman.utils.MyToast;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.OnClickListener;


public class EntryTablePointActivity extends Activity implements OnClickListener {


    GridView gv_layout;

    private List<TablePoint> mTablePointList = new ArrayList<TablePoint>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

               keyBoardCancle();

        }
    };
    private EntryGridAdapter entryGridAdapter;
    private EditText et_tableName;
    private EditText et_tablePoint;
    private Button bt_add;
    private TablePointDao tablePointDao;

    private RadioGroup radioGroup;
    private RadioButton rb1,rb2,rb3;
    private Dialog updateInfoDialog;
    private Dialog deleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_entry);

        initView();
        setListener();
        reloadData();

    }

    private void initView() {
        tablePointDao = new TablePointDao(this);
        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        gv_layout = (GridView) findViewById(R.id.gv_layout);
        et_tableName = (EditText) findViewById(R.id.et_tableName);
        et_tablePoint = (EditText) findViewById(R.id.et_tablePoint);

        radioGroup = (RadioGroup)findViewById(R.id.rg_layout);
        rb1 = (RadioButton)findViewById(R.id.rb1);
        rb2 = (RadioButton)findViewById(R.id.rb2);
        rb3 = (RadioButton)findViewById(R.id.rb3);

        bt_add = (Button) findViewById(R.id.bt_add);
        tv_back.setOnClickListener(this);
        bt_add.setOnClickListener(this);

    }

    private void setAdapter() {
        //单选框
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

            }
        });

        if (entryGridAdapter == null) {
            entryGridAdapter = new EntryGridAdapter(this, mTablePointList);
            gv_layout.setAdapter(entryGridAdapter);
        } else {
            entryGridAdapter.notifyDataSetChanged();
        }



    }

    private void setListener() {
        gv_layout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showDeleteDialog(mTablePointList.get(position));
            }
        });
        gv_layout.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showUpdateInfoDialog(mTablePointList.get(position),null);
                return true;
            }
        });
    }

    //强制隐藏软键盘
    private void keyBoardCancle() {

        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(EntryTablePointActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void reloadData() {
        mTablePointList.clear();
        List<TablePoint> mList =tablePointDao.queryAllPoint();
        if(mList!=null && mList.size() >0){
            mTablePointList.addAll(mList);
        }
        setAdapter();
    }

    @Override
    public void onClick(View v) {

        keyBoardCancle();
        String tabelName = et_tableName.getText().toString();
        String tablePoint = et_tablePoint.getText().toString();
        switch (v.getId()) {

            case R.id.tv_back:
                finish();
                break;
            case R.id.bt_add:
                if(TextUtils.isEmpty(tabelName)){
                    MyToast.showToast(getApplicationContext(),"餐桌名不能为空！");
                    return;
                }
                if(TextUtils.isEmpty(tablePoint)){
                     MyToast.showToast(getApplicationContext(),"点位不能为空！");
                     return;
                 }
                List<TablePoint> mList = tablePointDao.queryByTablePointPoint(tablePoint);
                TablePoint mTablePoint = null;
                if(rb1.isChecked()){
                    mTablePoint = new TablePoint(tabelName ,tablePoint,0);
                }else if(rb2.isChecked()){
                    mTablePoint = new TablePoint(tabelName ,tablePoint,1);
                }else if(rb3.isChecked()){
                    mTablePoint = new TablePoint(tabelName ,tablePoint,2);
                }
                rb1.setChecked(true);
                et_tableName.setText("");
                et_tablePoint.setText("");
                if(mList!=null && mList.size() <= 0){
                    mTablePointList.add(0,mTablePoint);
                    tablePointDao.add(mTablePoint);
                    MyToast.showToast(getApplicationContext(),"加入成功");
                }else{
                    MyLogcat.showLog("mOldTablePoint="+ mList.get(0).toString());
                    MyLogcat.showLog("currentTablePoint"+mTablePoint.toString());
                    FL.e(Constants.show_log, "mOldTablePoint="+ mList.get(0).toString());
                    FL.e(Constants.show_log, "currentTablePoint"+mTablePoint.toString());
                    Boolean showUpdate = true;
                    Boolean needCheck = true;
                    TablePoint mOldTablePoint =null;
                    for (int i=0;i<mList.size();i++){
                        if(mList.get(i).pointType==mTablePoint.pointType){
                            mOldTablePoint = mList.get(i);
                        }
                        if(mList.get(i).tableName.equals(tabelName) && mList.get(i).tablePoint.equals(tablePoint) && mList.get(i).pointType== mTablePoint.pointType ) {
                            MyToast.showToast(getApplicationContext(), "已存在");
                            showUpdate = false;
                            needCheck = false;
                            break;
                        }
                    }
                    if(showUpdate && needCheck){

                        for (int m=0;m<mList.size();m++){
                            if(mTablePoint.pointType ==mList.get(m).pointType ){//同一类型
                                needCheck = false;
                                break;

                            }
                        }
                    }
                    if(showUpdate && needCheck){

                        for (int m=0;m<mList.size();m++){
                             if(mTablePoint.pointType==1 && mList.get(m).pointType==2 || mTablePoint.pointType==2 && mList.get(m).pointType==1){//允许充电桩与厨房为同一个点

                                    mTablePointList.add(0,mTablePoint);
                                    tablePointDao.add(mTablePoint);
                                    MyToast.showToast(getApplicationContext(),"加入成功");
                                    showUpdate = false;
                                    needCheck = false;
                                    break;

                            }
                        }
                    }
                    if(showUpdate){

                        if(mOldTablePoint !=null){
                            showUpdateInfoDialog(mOldTablePoint,mTablePoint);
                        }else{
                            showUpdateInfoDialog(mList.get(0),mTablePoint);
                        }

                    }
                }

                reloadData();
                break;
        }

    }
    public void showDeleteDialog(final TablePoint mTablePoint){
        if(deleteDialog==null){
            deleteDialog = new Dialog(this, R.style.dialog_style);
        }
        int width = DipPxUtils.getScreenWidth(this)*2/3;
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(this, R.layout.dialog_no_net, null);
        deleteDialog.setContentView(view,lp);


        TextView tv_title = (TextView) deleteDialog.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) deleteDialog.findViewById(R.id.tv_content);
        tv_title.setText("删除点位");
        tv_content.setText("确定要删除么？"+"\n"+"当前点位："+mTablePoint.tablePoint+"      餐桌: "+mTablePoint.tableName);
        Button btn_cancel = (Button) deleteDialog.findViewById(R.id.btn_net_cancel);
        Button btn_setting = (Button) deleteDialog.findViewById(R.id.btn_net_setting);
        btn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyBoardCancle();
                deleteDialog.dismiss();
            }
        });
        btn_setting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                keyBoardCancle();
                tablePointDao.deleteByTablePoint(mTablePoint.id);
                reloadData();

                MyToast.showToast(getApplicationContext(),"已删除");
                deleteDialog.dismiss();
                keyBoardCancle();
            }
        });
        if(!deleteDialog.isShowing()){
            deleteDialog.show();
        }
    }
    public void showUpdateInfoDialog(final TablePoint mOldTablePoint,final TablePoint mTablePoint){

        if(updateInfoDialog==null){
            updateInfoDialog = new Dialog(this, R.style.dialog_style);
        }

        int width = DipPxUtils.getScreenWidth(this)*7/12;
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(this, R.layout.dialog_update_point, null);
        updateInfoDialog.setContentView(view,lp);


        TextView tv_title = (TextView) updateInfoDialog.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) updateInfoDialog.findViewById(R.id.tv_content);
        final  EditText et_tableName = (EditText) updateInfoDialog.findViewById(R.id.et_tableName);
        final EditText et_tablePoint = (EditText) updateInfoDialog.findViewById(R.id.et_tablePoint);
        et_tablePoint.setVisibility(View.GONE);

        tv_title.setText("当前点位已存在");
        String typeStr =null;
        String oldTypeStr= typeStr(mOldTablePoint.pointType);
        if(mTablePoint!=null){
            typeStr = typeStr(mTablePoint.pointType);
        }else{
            typeStr= oldTypeStr;
        }


        if(mTablePoint!=null){
            et_tableName.setText(mTablePoint.tableName);
            et_tablePoint.setText(mTablePoint.tablePoint);

            tv_content.setText("已存在的点位："+mOldTablePoint.tablePoint+"，类型:"+oldTypeStr+  "，名称："+ mOldTablePoint.tableName +"\n"+"\n"+
                    "重新设定的点位："+mTablePoint.tablePoint+"，类型:"+oldTypeStr+  "，名称："+mTablePoint.tableName +"\n" );
        }else{
            et_tableName.setText(mOldTablePoint.tableName);
            et_tablePoint.setText(mOldTablePoint.tablePoint);
            tv_content.setText("当前点位："+mOldTablePoint.tablePoint+"，类型"+typeStr+  "，名称："+ mOldTablePoint.tableName +"\n");
        }

        Button btn_cancel = (Button) updateInfoDialog.findViewById(R.id.btn_net_cancel);
        Button btn_setting = (Button) updateInfoDialog.findViewById(R.id.btn_net_setting);
        btn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                updateInfoDialog.dismiss();

            }
        });
        btn_setting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                mOldTablePoint.tableName = et_tableName.getText().toString();
                tablePointDao.update(mOldTablePoint);
                if(mTablePoint!=null){
                    MyToast.showToast(getApplicationContext(),"已覆盖");
                }else{
                    MyToast.showToast(getApplicationContext(),"已更新");
                }
                reloadData();
                updateInfoDialog.dismiss();


            }
        });
        if(!updateInfoDialog.isShowing()){
            updateInfoDialog.show();
        }
        updateInfoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mHandler.sendEmptyMessageDelayed(1,500);
            }
        });
    }
    private String typeStr(int pointType){
        String typeStr = "餐桌";
        switch (pointType){
            case 0:
                typeStr = "餐桌";
                break;
            case 1:
                typeStr = "厨房";
                break;
            case 2:
                typeStr = "充电桩";
                break;
        }
        return  typeStr;
    }

}
