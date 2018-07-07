package com.yunji.deliveryman.net;


import android.os.AsyncTask;
import android.os.Handler;

import com.yunji.deliveryman.bean.HttpMessage;
import com.yunji.deliveryman.common.Constants;
import com.yunji.deliveryman.other.log.FL;
import com.yunji.deliveryman.utils.MyLogcat;

import static com.yunji.deliveryman.core.HttpService.hostName;


public class HttpResponseTask extends AsyncTask<String, Integer, String> {

    private HttpMessage mHttpMessage;
    private Handler mHandler;


    public HttpResponseTask(Handler mHandler, HttpMessage mHttpMessage) {
        this.mHttpMessage = mHttpMessage;
        this.mHandler = mHandler;
    }

    @Override
    protected void onPreExecute() {
//        MyLogcat.showLog("onPreExecute: ");
//        FL.e(Constants.show_log, "onPreExecute: ");

    }

    @Override
    protected String doInBackground(String... params) {
//        MyLogcat.showLog("doInBackground: " + params);
//        FL.e(Constants.show_log, "doInBackground: " + params);
        if (mHttpMessage.requestType.equals("GET")) {
            String urlStrGet = null;
            if (mHttpMessage.message == "") {
                urlStrGet = "http://" + hostName + ":8809/api/"
                        + mHttpMessage.apiName;
            } else {
                urlStrGet = "http://" + hostName + ":8809/api/"
                        + mHttpMessage.apiName + "/" + mHttpMessage.message;
            }
//            MyLogcat.showLog("URLGET = " + urlStrGet);
//            FL.e(Constants.show_log, "URLGET = " + urlStrGet);

            NetInterface.getAttendanceListGet(mHandler, urlStrGet, mHttpMessage.apiName);

        } else if (mHttpMessage.requestType.equals("POST")) {

            String urlStrPost = "http://" + hostName + ":8809/api/"
                    + mHttpMessage.apiName;
//            MyLogcat.showLog("URLPOST = " + urlStrPost);
//            FL.e(Constants.show_log, "URLPOST = " + urlStrPost);

            NetInterface.getAttendanceListPost(mHandler
                    , urlStrPost, mHttpMessage.message, mHttpMessage.apiName);

        }

        return "";
    }


    @Override
    protected void onPostExecute(String result) {
//        MyLogcat.showLog("onPostExecute: " + result);
//        FL.e(Constants.show_log, "onPostExecute: " + result);
    }


}
