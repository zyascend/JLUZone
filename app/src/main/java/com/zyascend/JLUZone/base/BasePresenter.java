package com.zyascend.JLUZone.base;

import com.zyascend.JLUZone.http.OkHttpUtils;

/**
 *
 * Created by Administrator on 2016/7/6.
 */
public abstract class BasePresenter<T> {
    protected T mViewListener;

    public void attachView(T view){
        mViewListener = view;

    }

    protected T getView(){
        return mViewListener;
    }

    public boolean isViewAttached(){
        return mViewListener != null;
    }

    public void detachView(){
        if (mViewListener != null){
            mViewListener = null;
        }
    }

}
