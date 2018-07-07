package com.yunji.deliveryman.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.common.Constants;
import com.yunji.deliveryman.db.TablePoint;
import com.yunji.deliveryman.db.TablePointDao;
import com.yunji.deliveryman.db.TaskSend;
import com.yunji.deliveryman.other.log.FL;
import com.yunji.deliveryman.utils.MyLogcat;
import com.yunji.deliveryman.utils.SharedPrefsUtil;
import com.yunji.deliveryman.view.GifView;

import java.util.List;

public class TaskArraivedListAdapter extends BaseAdapter {

    private Context mContext;

    private LayoutInflater mInflater;

    private List<TaskSend> mTaskSendList;
    private TablePointDao tablePointDao;
    TaskSend currentTaskSend;
    private int[] drawable_take_meal_checked = {R.drawable.take_meal_f1_checked, R.drawable.take_meal_f2_checked
            , R.drawable.take_meal_f3_checked, R.drawable.take_meal_f4_checked};
    private int[] drawable_take_meal_unchecked = {R.drawable.take_meal_f1_unchecked, R.drawable.take_meal_f2_unchecked
            , R.drawable.take_meal_f3_unchecked, R.drawable.take_meal_f4_unchecked};

    public TaskArraivedListAdapter(Context context, List<TaskSend> mTaskSendList, TaskSend currentTaskSend) {
        mContext = context;
        this.mTaskSendList = mTaskSendList;
        this.currentTaskSend = currentTaskSend;
        mInflater = LayoutInflater.from(mContext);
        tablePointDao = new TablePointDao(context);
    }

    @Override
    public int getCount() {
        return (mTaskSendList != null ? mTaskSendList.size() : 0);
    }

    @Override
    public Object getItem(int position) {
        return (mTaskSendList != null ? mTaskSendList.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.listview_item_arrvied, null);

            holder = new ViewHolder();

            holder.rl_layout = (RelativeLayout) convertView.findViewById(R.id.rl_item);

            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);

            holder.tv_selectPoint = (TextView) convertView.findViewById(R.id.tv_selectPoint);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);

            holder.rl_gif = (RelativeLayout) convertView.findViewById(R.id.rl_gif);
            holder.work_gif = (GifView) convertView.findViewById(R.id.work_gif);
            holder.text_layout = (RelativeLayout) convertView.findViewById(R.id.text_layout);

            holder.animation = new TranslateAnimation(0, -5, 0, 0);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String tableName = SharedPrefsUtil.get(mContext, position + Constants.TASK_TABLE_NAME, "");
//        String tableName0 = SharedPrefsUtil.get(mContext, 0 + Constants.TASK_TABLE_NAME, "");
//        String tableName1 = SharedPrefsUtil.get(mContext, 1 + Constants.TASK_TABLE_NAME, "");
//        String tableName2 = SharedPrefsUtil.get(mContext, 2 + Constants.TASK_TABLE_NAME, "");
        TaskSend mTaskSend = mTaskSendList.get(position);
        List<TablePoint> mList = null;
        mList = tablePointDao.queryByTablePointId(mTaskSend.tablePointId);
        if (mList != null && mList.size() > 0) {
            MyLogcat.showLog("已到达查找点位：" + mList.get(0).tablePoint);
            FL.e(Constants.show_log, "已到达查找点位：" + mList.get(0).tablePoint);

        }
        switch (mTaskSendList.get(position).taskLayer) {
            case 0:

                holder.tv_name.setText("F1");
                break;

            case 1:

                holder.tv_name.setText("F2");
                break;
            case 2:

                holder.tv_name.setText("F3");
                break;
            case 3:

                holder.tv_name.setText("F4");
                break;
        }

        if (mTaskSend.taskState == 1 && currentTaskSend.tablePointId == mTaskSend.tablePointId) {
            setBigItem(mContext, position, true, tableName, mTaskSend.taskState, holder.text_layout, holder.rl_layout, holder.tv_name, holder.tv_selectPoint, holder.tv_state, holder.rl_gif, holder.work_gif, holder.animation);
            MyLogcat.showLog("大position==" + position);
            FL.e(Constants.show_log, "大position==" + position);

        } else {
            setBigItem(mContext, position, false, tableName, mTaskSend.taskState, holder.text_layout, holder.rl_layout, holder.tv_name, holder.tv_selectPoint, holder.tv_state, holder.rl_gif, holder.work_gif, holder.animation);

        }

        return convertView;
    }

    class ViewHolder {
        RelativeLayout rl_layout;
        RelativeLayout text_layout;
        TextView tv_name;
        TextView tv_selectPoint;

        TextView tv_state;
        RelativeLayout rl_gif;
        GifView work_gif;
        TranslateAnimation animation;
    }


    private void setBigItem(Context context, int position, boolean isBig, String tableName, int state, RelativeLayout textLayout, RelativeLayout rl_layout, TextView tv_name, TextView tv_selectPoint, TextView tv_state, RelativeLayout rl_gif, GifView work_gif, TranslateAnimation animation) {
        String stateStr = "未到达";
        switch (state) {
            case -1:
                stateStr = "已取消";
                break;
            case 0:
                stateStr = "未到达";
                break;
            case 1:
                stateStr = "请取餐";
                break;
            case 2:
                stateStr = "已完成";
                break;
        }
        if (TextUtils.isEmpty(tableName)) {
            tableName = "空";
            stateStr = "未放置";
        }

        RelativeLayout.LayoutParams rl_layoutLayoutParams = (RelativeLayout.LayoutParams) rl_layout.getLayoutParams();
        rl_layoutLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        RelativeLayout.LayoutParams textParams = (RelativeLayout.LayoutParams) textLayout.getLayoutParams();
        textParams.addRule(RelativeLayout.CENTER_VERTICAL);

        if (isBig) {
            rl_layoutLayoutParams.height = 204;
            rl_layoutLayoutParams.width = 1287;
            rl_layout.setLayoutParams(rl_layoutLayoutParams);
            textParams.leftMargin = 450;
            textLayout.setLayoutParams(textParams);
            rl_layout.setBackgroundResource(R.drawable.take_meal_checked);
            tv_name.setTextSize(48);
            tv_name.setVisibility(View.VISIBLE);
            tv_selectPoint.setTextSize(43);
            tv_selectPoint.setVisibility(View.VISIBLE);
            tv_state.setTextSize(43);
            tv_selectPoint.setPadding(0, 0, 0, 15);
            tv_state.setPadding(0, 0, 0, 15);
            rl_gif.setVisibility(View.VISIBLE);
            tv_state.setTextColor(Color.parseColor("#78d8f0"));
            tv_selectPoint.setTextColor(Color.parseColor("#78d8f0"));
            startTrans(rl_layout, animation);
            work_gif.startAnimation();
        } else {
            textParams.leftMargin = 350;
            textLayout.setLayoutParams(textParams);
            rl_layout.clearAnimation();
            rl_layoutLayoutParams.height = 105;
            rl_layoutLayoutParams.width = 998;
            rl_layout.setLayoutParams(rl_layoutLayoutParams);
            rl_layout.setBackgroundResource(drawable_take_meal_checked[position]);
            tv_name.setVisibility(View.INVISIBLE);
            tv_selectPoint.setVisibility(View.VISIBLE);
            tv_selectPoint.setTextSize(32);
            tv_state.setTextSize(32);
            rl_gif.setVisibility(View.GONE);
            work_gif.stopAnimation();

            if (state != 1) {

                textParams.leftMargin = 380;
                textLayout.setLayoutParams(textParams);
                tv_state.setTextColor(Color.parseColor("#477F8A"));
                tv_selectPoint.setTextColor(Color.parseColor("#477F8A"));
            }
            if (tableName.equals("空")) {
                textParams.leftMargin = 380;
                textLayout.setLayoutParams(textParams);
                tv_selectPoint.setTextColor(Color.parseColor("#804B515A"));
                tv_state.setTextColor(Color.parseColor("#804B515A"));
                rl_layout.setBackgroundResource(drawable_take_meal_unchecked[position]);
            }
            //设置文字居中显示
            tv_selectPoint.setPadding(0, 0, 0, 0);
            tv_state.setPadding(0, 0, 0, 0);
        }
        tv_state.setText(stateStr);
        tv_selectPoint.setText(tableName);

    }

    private void startTrans(RelativeLayout rl_layout, TranslateAnimation animation) {

        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(100);
        animation.setRepeatCount(Integer.MAX_VALUE);
        animation.setRepeatMode(Animation.REVERSE);
        rl_layout.startAnimation(animation);

    }

}
