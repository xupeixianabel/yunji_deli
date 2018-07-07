package com.yunji.deliveryman.bean;


public class RobotStatusResponse {//status

    public int OpenCloseFlag;    //开关门状态：0->开门中，1->门已开，2->关门中, 3->已关门
    public int charge_state;     // 1->充电中,0->未充电
    public int door_close_count; //关门次数
    public int door_open_count;
    public Boolean estop;        //是否急停
    public String message;
    public int power_percent;    //电量百分比
    public String state;         //任务状态（before_transport：等待任务，in_transport：
    // 任务中，complete_transport：任务完成，back： 返回中，task_failed：任务失败或取消）
    public String status;        //running or others
    public int take_out;         //255 or ..
    public String target;        //当前任务目标
    public int tasks;            //任务总数
    public double total_distance;   //总程

    @Override
    public String toString() {
        return "RobotStatusResponse{" +
                "OpenCloseFlag='" + OpenCloseFlag + '\'' +
                ", charge_state='" + charge_state + '\'' +
                ", door_close_count='" + door_close_count + '\'' +
                ", door_open_count='" + door_open_count + '\'' +
                ", estop='" + estop + '\'' +
                ", message='" + message + '\'' +
                ", power_percent='" + power_percent + '\'' +
                ", state='" + state + '\'' +
                ", status='" + status + '\'' +
                ", take_out='" + take_out + '\'' +
                ", target='" + target + '\'' +
                ", tasks='" + tasks + '\'' +
                ", total_distance='" + total_distance + '\'' +
                '}';
    }
}

