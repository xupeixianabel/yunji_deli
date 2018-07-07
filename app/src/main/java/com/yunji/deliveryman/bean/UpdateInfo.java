package com.yunji.deliveryman.bean;

/**
 * 升级信息
 */
public class UpdateInfo {
    public String versionCode;
    public String description;
    public String apkurl;

    public UpdateInfo(String versionCode, String description, String apkurl) {
        super();
        this.versionCode = versionCode;
        this.description = description;
        this.apkurl = apkurl;
    }

    public String getVersionCode() {
        return versionCode;
    }
}
