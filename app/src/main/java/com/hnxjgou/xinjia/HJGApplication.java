package com.hnxjgou.xinjia;

import android.app.Application;

import com.hnxjgou.xinjia.utils.AMapLocationUtil;

/**
 * Created by apple on 2018/6/7.
 */

public class HJGApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AMapLocationUtil.getInstance().initLocationClient(this);
    }

}
