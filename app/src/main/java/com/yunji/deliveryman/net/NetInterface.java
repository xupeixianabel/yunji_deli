package com.yunji.deliveryman.net;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yunji.deliveryman.common.Constants;
import com.yunji.deliveryman.other.log.FL;
import com.yunji.deliveryman.utils.MyLogcat;

import org.apache.http.entity.StringEntity;

/**
 * 网络交互 调用接口
 */
public class NetInterface {
    /**
     * 获取考勤列表</br>
     */
    public static void getAttendanceListPost(final Handler handler, String url, String json, String apiName) {
        HttpUtils hu = new HttpUtils();
        RequestParams params = new RequestParams();
//        params.addBodyParameter("userId", userId);
//        params.addBodyParameter("date", time);

        // 接收参数json列表
        //JSONObject jsonParam = new JSONObject();
        try {
//            jsonParam.put("method", "getAttendanceListGet");
//            jsonParam.put("userId", userId);
//            jsonParam.put("date", time);
            StringEntity entity = new StringEntity(json.toString(), "utf-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            params.setBodyEntity(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        hu.send(HttpMethod.POST, url, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        //MyLogcat.showLog("请求失败post-->" + arg0.result);
                        Message msg = Message.obtain();
                        msg.what = Constants.MSG_POST_SUCCESS;
                        msg.obj = arg0.result;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
//                        MyLogcat.showLog("请求失败post-->" + arg0.getMessage());
                        MyLogcat.showLog("getAttendanceListPost  onFailure 请求失败post = " + arg0.getMessage());
//                        FL.e(Constants.show_log, "请求失败post-->" + arg0.getMessage());
                        FL.e(Constants.show_log, "getAttendanceListPost  onFailure 请求失败post = " + arg0.getMessage());
                        Message msg = Message.obtain();
                        msg.what = Constants.MSG_POST_FAILED;
                        handler.sendMessage(msg);

                    }
                }
        );
    }

    public static void getAttendanceListGet(final Handler handler, String url, final String apiName) {
        HttpUtils hu = new HttpUtils(10000);
        // 设置缓存1秒,1秒内直接返回上次成功请求的结果。
        hu.configCurrentHttpCacheExpiry(0);
        hu.send(HttpMethod.GET, url, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        if (apiName.equals("taskstatus")) {
//                            MyLogcat.showLogStatus("请求成功-->" + arg0.result + "  ");
//                            FL.e(Constants.show_log_status, "请求成功-->" + arg0.result + "  ");
                        }
                        else {
                            Log.e("yong", "--------- 请求失败 = " + arg0.result);
                        }
                        Message msg = Message.obtain();
                        msg.what = Constants.MSG_GET_SUCCESS;
                        msg.obj = arg0.result;
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.URL_API_NAME, apiName);
                        msg.setData(bundle);
                        if (handler != null) {
                            handler.sendMessage(msg);
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        MyLogcat.showLog("请求失败-->" + arg0.getMessage());
                        FL.e(Constants.show_log, "请求失败-->" + arg0.getMessage());
                        Message msg = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.URL_API_NAME, apiName);
                        msg.setData(bundle);
                        msg.what = Constants.MSG_GET_FAILED;
                        if (handler != null) {
                            handler.sendMessage(msg);
                        }
                    }
                }
        );
    }

    /**
     * 获取版本信息
     */
    public static void getVersionInfo(final Handler handler, String appHostUrl) {
        MyLogcat.showLog("getVersionInfo() appHostUrl = " + appHostUrl);
        FL.e(Constants.show_log, "getVersionInfo() appHostUrl = " + appHostUrl);
        HttpUtils hu = new HttpUtils();
        RequestParams params = new RequestParams();
        hu.send(HttpMethod.GET, appHostUrl, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        MyLogcat.showLog("getVersionInfo()请求成功 arg0 = " + arg0.toString());
                        FL.e(Constants.show_log,"getVersionInfo()请求成功 arg0 = " + arg0.toString() );
                        Message msg = Message.obtain();
                        msg.what = Constants.MSG_GET_VERSION;
                        msg.obj = arg0.result;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        MyLogcat.showLog("getVersionInfo()请求失败 HttpException = " + arg0.getMessage());
                        FL.e(Constants.show_log, "getVersionInfo()请求失败 HttpException = " + arg0.getMessage());
                        Message msg = Message.obtain();
                        msg.what = Constants.MSG_GET_VERSION_FAIL;
                        handler.sendMessage(msg);
                    }
                }
        );
    }
}
