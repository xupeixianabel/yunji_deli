package com.yunji.deliveryman.bean;

import java.io.Serializable;
import java.util.List;


public class CruiseJsonPost implements Serializable
{


    /**
     * type : time
     * time : 10
     * times : 5
     * data : {"name":"default1","sound_type":"sync","targets":[{"marker":"002","sound":"cruise_测试1"}]}
     */

    private String type;
    private int time;
    private int times;
    private int dwell_time;
    private DataBean data;

    public int getDwell_time() {
        return dwell_time;
    }

    public void setDwell_time(int dwell_time) {
        this.dwell_time = dwell_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * name : default1
         * sound_type : sync
         * targets : [{"marker":"002","sound":"cruise_测试1"}]
         */

        private String name;
        private String sound_type;
        private List<TargetsBean> targets;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSound_type() {
            return sound_type;
        }

        public void setSound_type(String sound_type) {
            this.sound_type = sound_type;
        }

        public List<TargetsBean> getTargets() {
            return targets;
        }

        public void setTargets(List<TargetsBean> targets) {
            this.targets = targets;
        }

        public static class TargetsBean {
            public TargetsBean(String marker, String sound) {
                this.marker = marker;
                this.sound = sound;
            }

            public TargetsBean() {
            }

            /**
             * marker : 002
             * sound : cruise_测试1
             */

            private String marker;
            private String sound;

            public String getMarker() {
                return marker;
            }

            public void setMarker(String marker) {
                this.marker = marker;
            }

            public String getSound() {
                return sound;
            }

            public void setSound(String sound) {
                this.sound = sound;
            }
        }
    }
}

