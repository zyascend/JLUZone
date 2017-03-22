package com.zyascend.JLUZone.http;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/11/11.
 */

public class OkHttpUtils {


    private Handler mHandler;
    private OkHttpClient.Builder okHttpBuilder;
    private static OkHttpUtils INSTANCE;
    private static Application context;                         //全局上下文
    private OkHttpClient okHttpClient;


    public static void init(Application app){
        context = app;
    }

    private OkHttpUtils(){
        okHttpBuilder = new OkHttpClient.Builder();
        okHttpBuilder.connectTimeout(20000, TimeUnit.MILLISECONDS);
        okHttpBuilder.readTimeout(20000, TimeUnit.MILLISECONDS);
        okHttpBuilder.cookieJar(new CookiesManager());
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpUtils getInstance(){
        if (INSTANCE == null){
            synchronized (OkHttpUtils.class){
                if (INSTANCE == null){
                    INSTANCE = new OkHttpUtils();
                }
            }
        }
        return INSTANCE;
    }


    public void build(){
        okHttpClient = okHttpBuilder.build();
    }

    public OkHttpClient getHttpClient(){
        return okHttpClient;
    }

    public OkHttpUtils setConnectTimeOut(long timeOut){
        okHttpBuilder.connectTimeout(timeOut, TimeUnit.MILLISECONDS);
        return this;
    }

    public OkHttpUtils setReadTimeOut(long timeOut){
        okHttpBuilder.readTimeout(timeOut, TimeUnit.MILLISECONDS);
        return this;
    }

    public Handler getHandler() {
        return mHandler;
    }


    public static PostCall post(){
        return new PostCall();
    }

    public void cancelTag(Object tag){

        for (Call call : getHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public void cancelAll(){
        getHttpClient().dispatcher().cancelAll();
    }


    private class CookiesManager implements CookieJar {

        private final PersistentCookieStore cookieStore = new PersistentCookieStore(context);
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (cookies != null && cookies.size() > 0) {
                for (Cookie item : cookies) {
                    cookieStore.add(url, item);
                }
            }
        }
        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url);
            return cookies;
        }
    }


}
