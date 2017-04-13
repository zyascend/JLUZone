package com.zyascend.JLUZone.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zyascend.JLUZone.base.BaseApplication;

/**
 *
 * Created by Administrator on 2017/3/22.
 */

public class NetStateUtil {

    public static boolean isNetworkAvailable() {
        Context context = BaseApplication.context;
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}