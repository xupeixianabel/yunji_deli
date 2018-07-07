package com.yunji.deliveryman.adapter;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.db.TablePoint;
import com.yunji.deliveryman.db.TaskSend;
import com.yunji.deliveryman.utils.Util;
import com.yunji.deliveryman.view.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class TaskAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Activity mActivity;
    public List<TaskSend> mTaskSendList;
    public List<TablePoint> mTablePointIdList;

    public Handler parentHandler;

    boolean isDisable;
    public static final int CHECK_ITEM_SELECTED = 205;

    /**
     * 当前选中的文件的集合
     */
    public static Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();
    private OnSelectedItemChanged listener;


    public TaskAdapter(Activity mActivity, List<TaskSend> mTaskSendList, List<TablePoint> mTablePointIdList, Handler parentHandler, OnSelectedItemChanged listener) {
        this.mActivity = mActivity;
        this.mTaskSendList = mTaskSendList;
        this.mTablePointIdList = mTablePointIdList;
        this.listener = listener;
        this.parentHandler = parentHandler;
        inflater = LayoutInflater.from(mActivity);
        initData();
    }

    public void initData() {
        isCheckMap.clear();
        for (int i = 0; i < mTaskSendList.size(); i++) {
            isCheckMap.put(i, false);
        }
    }

    private boolean init(int position) {
        if (isCheckMap.containsKey(Integer.valueOf(position))) {
            if (isCheckMap.get(position) == true) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int getCount() {
        return mTaskSendList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTaskSendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        int count = mTaskSendList != null ? mTaskSendList.size() : 1;
        return count;
    }

    public void setDiabled(boolean isDisable) {
        this.isDisable = isDisable;
    }

    public void set_datasource(List<TaskSend> mTaskSendList) {
        this.mTaskSendList = mTaskSendList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Resources resource = (Resources) mActivity.getResources();

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.listview_item_cancel_task, null);
            holder.rl_layout = (RelativeLayout) convertView.findViewById(R.id.rl_layout);
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);

            holder.tv_task_name = (TextView) convertView.findViewById(R.id.tv_task_name);
            holder.tv_task_desc = (TextView) convertView.findViewById(R.id.tv_task_desc);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final TaskSend mTaskSend = mTaskSendList.get(position);
        final TablePoint mTablePoint = mTablePointIdList.get(position);
        String newTableName = Util.updateTableNameStr(mTablePoint.tableName);
        switch (mTaskSend.taskLayer) {
            case 0:
                holder.tv_task_name.setText(newTableName + " 送餐任务（上层）");
                break;
            case 1:
                holder.tv_task_name.setText(newTableName + " 送餐任务（中层）");
                break;
            case 2:
                holder.tv_task_name.setText(newTableName + " 送餐任务（中层）");
                break;
            case 3:
                holder.tv_task_name.setText(newTableName + " 送餐任务（下层）");
                break;
            case 4:
                holder.tv_task_name.setText("回厨房任务");
                break;
        }

        if (isDisable && mTaskSend.taskLayer != 4) {
            holder.checkbox.setButtonDrawable(R.drawable.cbtn_check_on_disable);
            holder.tv_task_name.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.checkbox.setButtonDrawable(R.drawable.square_check_selector);
            holder.tv_task_name.getPaint().setFlags(0);
        }
        holder.tv_task_desc.setText(mTablePoint.tablePoint + "");

        holder.checkbox.setChecked(isCheckMap.get(position));
        final CheckBox cb = holder.checkbox;
        cb.setChecked(isCheckMap.get(position));// 设置选择状态
        holder.checkbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                TaskSend mTaskSend = mTaskSendList.get(mTaskSendList.size() - 1);
                boolean isKitchen = isCheckMap.get(isCheckMap.size() - 1);

                if (isKitchen && position != isCheckMap.size() - 1) {
//					MyToast.showToast(mActivity,"不可点击");

                    holder.checkbox.setChecked(true);
                } else {
                    isCheckMap.put(position, cb.isChecked());
                    TaskAdapter.this.listener.selectedItemChange(getSelectedCount(isCheckMap));
                }

            }
        });
        holder.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {
                    isCheckMap.put(position, true);
                    if (mTaskSend.taskLayer == 4) {
                        isDisable = true;
                        selectAll();
                    }
                    holder.tv_task_name.setTextColor(Color.parseColor("#60c030"));
                } else {
                    if (mTaskSend.taskLayer == 4) {
                        isDisable = false;
                        disSelectAll();
                    }
                    isCheckMap.put(position, false);
                    holder.tv_task_name.setTextColor(Color.parseColor("#5a5a5a"));
                }
                Message msg = new Message();
                msg.what = CHECK_ITEM_SELECTED;
                parentHandler.handleMessage(msg);
                TaskAdapter.this.listener.selectedItemChange(getSelectedCount(isCheckMap));
            }
        });
        return convertView;
    }

    /**
     * 获取选择的项的数目
     *
     * @param map
     * @return
     */
    public int getSelectedCount(Map<Integer, Boolean> map) {
        int i = 0;
        for (Entry<Integer, Boolean> entry : map.entrySet()) {
            if (entry.getValue()) {
                i++;
            }
        }
        return i;
    }

    /**
     * 向Activity暴露选择了多少项
     */
    public interface OnSelectedItemChanged {
        public void selectedItemChange(int count);
    }

    public void selectAll() { // 全选
        for (int i = 0; i < mTaskSendList.size(); i++) {
            isCheckMap.put(i, true);
        }
        notifyDataSetChanged();
    }

    public void disSelectAll() { // 全不选
        for (int i = 0; i < mTaskSendList.size(); i++) {
            isCheckMap.put(i, false);
        }
        notifyDataSetChanged();
    }

}
