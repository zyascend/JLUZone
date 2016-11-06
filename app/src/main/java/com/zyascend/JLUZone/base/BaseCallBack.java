package com.zyascend.JLUZone.base;

/**
 * Created by Administrator on 2016/8/4.
 */
public interface BaseCallBack<T> {
     void onSuccess(T t);
     void onFailure(Exception e);
}
