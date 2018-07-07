package com.yunji.deliveryman.net;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.yunji.deliveryman.bean.UpdateInfo;
import com.yunji.deliveryman.common.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * json解析类
 */
public final class WParseJson {
    private static SimpleDateFormat sdfTimeUpload = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

    private WParseJson() {
        throw new Error("异常，止步");
    }

    /**
     * 返回 备忘云端code
     */
    public static int parseMemoCloudCode(String json) {
        int ret = 0;
        try {
            JSONObject jo = new JSONObject(json);
            ret = jo.getInt("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 返回获取版本
     */
    public static UpdateInfo parseGetVersion(Handler mHandler, String json) {
        UpdateInfo updateInfo = null;
        String msgFail = "";
        Message msg = Message.obtain(mHandler);
        if (!TextUtils.isEmpty(json)) {//有json数据
            try {
                JSONObject jo = new JSONObject(json);
                try {
                    JSONObject subJsonObject = jo.getJSONObject("data");
                    String versionCode = subJsonObject.getString("version");
                    String description = subJsonObject.getString("d");
                    String apkurl = subJsonObject.getString("url");
                    updateInfo = new UpdateInfo(versionCode, description, apkurl);
                    msg.what = Constants.MSG_GET_VERSION_SUCCESS;
                } catch (Exception e) {
                    e.printStackTrace();
                    msgFail = "解析数据异常";
                    msg.what = Constants.MSG_GET_VERSION_FAIL;
                    Bundle data = new Bundle();
                    data.putString("msg", msgFail);
                    msg.setData(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                msg.what = Constants.MSG_SERVER_ERROR;
            }
        } else {//json数据为空
            msg.what = Constants.MSG_GET_VERSION_SUCCESS;
        }
        mHandler.sendMessage(msg);
        return updateInfo;
    }

    //方法  返回true为包含中文；false不包含
    public static boolean isContainsChinese(String str) {
        Pattern pat = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }

    /**
     * 解析数据
     */
    /*public static void parseRecoverGoodsData(Context context, final Handler handler, String jsonStr, int type) {
        List<String> contentList = null;
        GoodsDao mGoodsDao = new GoodsDao(context);
        Goods mGoods;
        List<Goods> mGoodsListOld;

        try {
            MyLogcat.showLog("恢复数据收到jsonStr===" + jsonStr);
            JSONObject jsonObject = new JSONObject(jsonStr);
            int code = jsonObject.getInt("code");
            if (code == 1) {
                JSONArray josnArray = jsonObject.getJSONArray("Goods");
                if (josnArray != null) {
                    if (josnArray.length() > 0) {
                        contentList = new ArrayList<String>();
                        for (int i = 0; i < josnArray.length(); i++) {
                            JSONObject json = josnArray.getJSONObject(i);

                            mGoods = new Goods();
                            if (json.has("goodsName"))
                                mGoods.goodsName = json.getString("goodsName");
                            if (json.has("goodsId")) {
                                mGoods.goodsId = json.getString("goodsId");
                            }
                            if (json.has("goodsRFidNum"))
                                mGoods.goodsRFidNum = json.getString("goodsRFidNum");
                            if (json.has("goodsPrice")) {
                                mGoods.goodsPrice = json.getInt("goodsPrice");
                            }


                            if (json.has("isPaySuccess")) {
                                mGoods.isPaySuccess = json.getBoolean("isPaySuccess");
                            }
                            if (json.has("isExpired")) {
                                mGoods.isExpired = json.getBoolean("isExpired");
                            }


                            if (json.has("goodsCreatTime")) {
                                if (!"null".equals(json.getString("goodsCreatTime")) && !TextUtils.isEmpty(json.getString("goodsCreatTime"))) {
                                    mGoods.goodsCreatTime = Long.parseLong(json.getString("goodsCreatTime"));
                                }
                            }
                            if (json.has("goodsUpdateTime")) {
                                if (!"null".equals(json.getString("goodsUpdateTime")) && !TextUtils.isEmpty(json.getString("goodsUpdateTime"))) {
                                    mGoods.goodsUpdateTime = Long.parseLong(json.getString("goodsUpdateTime"));
                                }
                            }
                            if (json.has("goodsExpiredTime")) {
                                if (!"null".equals(json.getString("goodsExpiredTime")) && !TextUtils.isEmpty(json.getString("goodsExpiredTime"))) {
                                    mGoods.goodsCreatTime = Long.parseLong(json.getString("goodsExpiredTime"));
                                }
                            }


                            mGoodsListOld = mGoodsDao.queryByGoodsNum(mGoods.goodsId);

                            MyLogcat.showLog("返回mGoods=" + mGoods.toString());
                            if (mGoodsListOld != null && mGoodsListOld.size() > 0) {
                                mGoodsDao.deleteByGoodsId(mGoodsListOld.get(0).id);
                            }

                        }

                    }

                }
                Message msg = Message.obtain();
                switch (type) {
                    case Constants.TYPE_GOODS_SYNC:
                        msg.what = Constants.TYPE_GOODS_SYNC_SUCCESS;
                        msg.arg1 = Constants.TYPE_GOODS_SYNC;
                        break;

                }

                msg.obj = contentList;
                handler.sendMessage(msg);
            } else {
                Message msg = Message.obtain();

                switch (type) {

                    case Constants.TYPE_GOODS_SYNC:
                        msg.what = Constants.TYPE_GOODS_SYNC_FAIL;
                        msg.arg1 = Constants.TYPE_GOODS_SYNC;
                        break;

                }
                handler.sendMessage(msg);
            }


        } catch (Exception e) {
            e.printStackTrace();
            Message msg = Message.obtain();
            switch (type) {
                case Constants.TYPE_GOODS_SYNC:
                    msg.what = Constants.TYPE_GOODS_SYNC_FAIL;
                    msg.arg1 = Constants.TYPE_GOODS_SYNC;
                    break;

            }
            handler.sendMessage(msg);
        }

    }*/

    /**
     * 返回修改密码结果
     */
    /*public static void parseChangePwd(Handler mHandler, String json) {
        String msgFail = "";
        try {
            JSONObject jo = new JSONObject(json);
            String ret = String.valueOf(jo.getInt("code"));

            if ("1".equals(ret)) {
                Message msg = Message.obtain(mHandler);
                msg.what = Constants.MSG_CHANGE_PWD_SUCCESS;
                mHandler.sendMessage(msg);
            } else {
                msgFail = jo.getString("message");
                Message msg = Message.obtain(mHandler);
                msg.what = Constants.MSG_CHANGE_PWD_FAIL;
                Bundle data = new Bundle();
                data.putString("msg", msgFail);
                msg.setData(data);
                mHandler.sendMessage(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Message msg = Message.obtain(mHandler);
            msg.what = Constants.MSG_SERVER_ERROR;
            mHandler.sendMessage(msg);
        }
    }*/
}
