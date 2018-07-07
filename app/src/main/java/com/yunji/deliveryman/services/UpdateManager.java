package com.yunji.deliveryman.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.bean.UpdateInfo;
import com.yunji.deliveryman.common.Constants;
import com.yunji.deliveryman.net.NetInterface;
import com.yunji.deliveryman.net.WParseJson;
import com.yunji.deliveryman.other.log.FL;
import com.yunji.deliveryman.utils.DipPxUtils;
import com.yunji.deliveryman.utils.MyLogcat;
import com.yunji.deliveryman.utils.PublicUtils;

public class UpdateManager {
    private static final int SHOW_NEW_VERSION = 6;
    private Activity mContext;
    private UpdateInfo updateInfo;
    boolean isShowToast = false;
    //是否弹出升级弹窗
    private boolean updateNoticeFlag = false;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_NEW_VERSION:
                    UpdateManager.this.showNoticeDialog(updateNoticeFlag);
                    break;
                case Constants.MSG_GET_VERSION:
                    String json = (String) msg.obj;
                    MyLogcat.showLog("获取版本信息返回json-->" + json);
                    FL.e(Constants.show_log, "获取版本信息返回json-->" + json);
                    if (TextUtils.isEmpty(json)) {
                        MyLogcat.showLog("返回获取版本信息异常");
                        FL.e(Constants.show_log, "返回获取版本信息异常");
                    } else {
                        updateInfo = WParseJson.parseGetVersion(mHandler, json);
                    }
                    break;
                case Constants.MSG_GET_VERSION_SUCCESS:
                    MyLogcat.showLog("MSG_GET_VERSION_SUCCESS 返回获取版本信息成功");
                    FL.e(Constants.show_log, "MSG_GET_VERSION_SUCCESS 返回获取版本信息成功");
                    String serviceVersionCode = "0";
                    if (updateInfo != null && !"null".equals(updateInfo.versionCode) && !TextUtils.isEmpty(updateInfo.versionCode)) {
                        serviceVersionCode = updateInfo.versionCode;
                        if (updateInfo != null && Double.parseDouble(serviceVersionCode) > PublicUtils.getVersionCode(mContext.getApplicationContext())) {
                            MyLogcat.showLog("MSG_GET_VERSION_SUCCESS 服务器版本 updateInfo = " + updateInfo.toString());
                            FL.e(Constants.show_log, "MSG_GET_VERSION_SUCCESS 服务器版本 updateInfo = " + updateInfo.toString());
                            updateNoticeFlag = true;
                            Message msg2 = Message.obtain();
                            msg2.what = SHOW_NEW_VERSION;
                            UpdateManager.this.mHandler.sendMessage(msg2);
                        } else {
                            updateNoticeFlag = false;
                            Toast.makeText(mContext, "已是最新版本", Toast.LENGTH_SHORT).show();
                            MyLogcat.showLog("已是最新版本");
                            FL.e(Constants.show_log, "已是最新版本");
//                            if (isShowToast) {
//                              MyLogcat.showLog("MSG_GET_VERSION_SUCCESS 已是最新版本 isShowToast = " + isShowToast);
//                            }
                        }
                    } else {
                        MyLogcat.showLog("参数错误");
                        FL.e(Constants.show_log, "参数错误");
                    }
                    break;
                case Constants.MSG_GET_VERSION_FAIL:
                    String failMsg = "";
                    Bundle bundle = msg.getData();
                    if (bundle != null) {
                        failMsg = bundle.getString("msg");
                    }
                    if (TextUtils.isEmpty(failMsg)) {
                        failMsg = "获取版本信息失败";
                    }
                    MyLogcat.showLog(failMsg);
                    FL.e(Constants.show_log, failMsg);
                    break;
                case Constants.MSG_SERVER_ERROR:
                    MyLogcat.showLog("服务器签到异常");
                    FL.e(Constants.show_log, "服务器签到异常");
                    break;
                default:
                    break;
            }
        }
    };

    public UpdateManager(Activity paramContext) {
        this.mContext = paramContext;
    }

    /**
     * 发现新版本
     */
    private void showNoticeDialog(boolean updateNoticeFlag) {
        if (updateNoticeFlag) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.mContext);
            final Dialog noticeDialog = localBuilder.create();
            if (mContext != null && !mContext.isFinishing() && !noticeDialog.isShowing()) {
                noticeDialog.show();
            }
            Window window = noticeDialog.getWindow();
            window.setContentView(R.layout.dialog_update_version);
            int width = DipPxUtils.getScreenWidth(mContext) * 2 / 3;
            int height = width * 3 / 4;

            window.setLayout(width, height);

            TextView tv_update_info = ((TextView) window.findViewById(R.id.tv_update_info));
            Button btn_update_cancel = ((Button) window.findViewById(R.id.btn_update_cancel));
            Button btn_update_yes = ((Button) window.findViewById(R.id.btn_update_submit));

            String info = this.updateInfo.description;
            info = info.replaceAll(" ", "");
            String[] infos = info.split("\\=");

            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < infos.length; i++) {
                if (i != infos.length - 1) {
                    stringBuffer.append(infos[i] + ";" + "\n");
                } else {
                    stringBuffer.append(infos[i] + ";");
                }
            }
            tv_update_info.setText(stringBuffer.toString());
            tv_update_info.setMovementMethod(ScrollingMovementMethod.getInstance());
            btn_update_cancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    noticeDialog.dismiss();

                }
            });
            btn_update_yes.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    noticeDialog.dismiss();
                    downloadApk();
                }
            });
        } else {
            Toast.makeText(mContext, "已是最新版本", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkVersion(final boolean isShowToast) {
        if (PublicUtils.isConnected(mContext)) {
            this.isShowToast = isShowToast;
            NetInterface.getVersionInfo(mHandler, Constants.HTTP_APK_URL);
        } else {
            Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 下载APK
     */
    private void downloadApk() {
        if (updateInfo == null || TextUtils.isEmpty(updateInfo.apkurl)) {
            return;
        }
        String apkUrl = updateInfo.apkurl;
        MyLogcat.showLog("下载地址=" + apkUrl);
        FL.e(Constants.show_log, "下载地址=" + apkUrl);

        try {
            Intent intent = new Intent(mContext, DownLoadService.class);
            intent.putExtra("download_url", apkUrl);
            mContext.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

 