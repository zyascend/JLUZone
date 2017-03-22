package com.zyascend.JLUZone.base;

import android.app.Application;
import android.content.Context;


import com.baidu.mapapi.SDKInitializer;
import com.zyascend.JLUZone.http.OkHttpUtils;

import cn.jpush.android.api.JPushInterface;

/**
 *
 * Created by Administrator on 2016/11/2.
 */

public class BaseApplication extends Application {

    private boolean isNight;
    public static Context context;
//    public RefWatcher refWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
//        refWatcher = LeakCanary.install(this);
        JPushInterface.init(this);
        OkHttpUtils.init(this);
        OkHttpUtils.getInstance()
                .build();
        SDKInitializer.initialize(this);
    }




//    public static RefWatcher getRefWatcher(Context context) {
//        BaseApplication application = (BaseApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }

}
