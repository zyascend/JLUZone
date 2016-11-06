package com.zyascend.JLUZone.login;

import android.content.Context;
import android.util.Log;

import com.zyascend.JLUZone.base.BasePresenter;
import com.zyascend.JLUZone.entity.StuInfo;
import com.zyascend.JLUZone.model.data.DataListener;
import com.zyascend.JLUZone.model.data.DataUtils;
import com.zyascend.JLUZone.model.net.HttpUtils;
import com.zyascend.JLUZone.model.net.HttpUtilsListener;


/**
 *
 * Created by Administrator on 2016/7/6.
 */
public class LoginPresenter extends BasePresenter<LoginContract.View>implements LoginContract.Presenter {
    private static final String TAG = "TAG_LoginPresenter";
    private DataListener mDataListener;
    private HttpUtilsListener mHttpUtils;
    private StuInfo mStuinfo;

    public LoginPresenter (Context context){
        mDataListener = DataUtils.getInstance(context.getApplicationContext());
        mHttpUtils = HttpUtils.getInstance();
    }


    @Override
    public void saveStuInfo(StuInfo stuInfo) {
        mDataListener.saveStuInfo(stuInfo);
    }

    @Override
    public void getStuInfo() {
        mDataListener.getStuInfo(new HttpUtilsListener.LoginCallBack() {
            @Override
            public void onSuccess(StuInfo stuInfo) {
                mViewListener.loadStuInfo(stuInfo);
            }

            @Override
            public void onFailure(Exception e) {
            }
        });
    }

    @Override
    public void login(StuInfo stuInfo) {
        mStuinfo = stuInfo;
        mViewListener.showLoginProgress();
        mHttpUtils.login(stuInfo.getIsLoginOutside(), stuInfo.getAccount()
                , stuInfo.getPassWord(), new HttpUtilsListener.LoginCallBack() {
            @Override
            public void onSuccess(StuInfo stuInfo) {
                Log.d(TAG, "onSuccess: ");
                mStuinfo.setStuId(stuInfo.stuId);
                mStuinfo.setName(stuInfo.name);
                mStuinfo.setCurrentTerm(stuInfo.currentTerm);
                saveStuInfo(mStuinfo);
                mViewListener.showLoginSuccess();

            }

            @Override
            public void onFailure(Exception e) {
                mViewListener.dismissLoginProgress();
                mViewListener.showLoginFailure();
            }

        });
    }

    @Override
    public void cancel() {
        mHttpUtils.cancel();
    }

    @Override
    public void detachView() {
        super.detachView();
//        mHttpUtils.cancel();
        mViewListener = null;
        mHttpUtils = null;
    }
}
