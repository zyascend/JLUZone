package com.zyascend.JLUZone.job;

import android.content.Context;
import android.text.TextUtils;

import com.zyascend.JLUZone.base.BasePresenter;
import com.zyascend.JLUZone.entity.ConstValue;
import com.zyascend.JLUZone.entity.Job;
import com.zyascend.JLUZone.entity.JobContent;
import com.zyascend.JLUZone.http.OkHttpUtils;
import com.zyascend.JLUZone.model.data.DataUtils;
import com.zyascend.JLUZone.model.net.HttpManager;
import com.zyascend.JLUZone.model.net.HttpManagerListener;
import com.zyascend.JLUZone.utils.ActivityUtils;

import java.util.List;

/**
 *
 * Created by Administrator on 2016/10/20.
 */

public class JobPresenter extends BasePresenter<JobContract.View> implements JobContract.Presenter
        , HttpManagerListener.JobListCallback
        {

    private HttpManager httpUtils;
    private DataUtils dataUtils;
    private String mTag;

    public JobPresenter(Context context){
        httpUtils = HttpManager.getInstance();
        dataUtils = DataUtils.getInstance(context.getApplicationContext());
    }

    @Override
    public void getJobList(String tag, int page) {
        mTag = tag;
        if (TextUtils.equals(tag, ConstValue.TAG_XIAOZHAO)){
            httpUtils.getXiaoZhaoList(this,page,this);
        }else {
            httpUtils.getShixiList(this,page,this);
        }

    }

    @Override
    public void getJobContent(int id) {
        mTag = null;
        httpUtils.getJobContent(this,id, new HttpManagerListener.JobContentCallback() {
            @Override
            public void onSuccess(JobContent content) {
                mViewListener.onLoadedContent(content);
            }

            @Override
            public void onFailure(Exception e) {
                mViewListener.onFailed(e);
            }
        });
    }

    @Override
    public void onSuccess(List<Job> jobs) {
//        if (ActivityUtils.NotNullOrEmpty(jobs)){
//            for (Job job : jobs) {
//                dataUtils.saveJobs(job);
//            }
//        }else {
//            jobs = dataUtils.getAllJobsByType(mTag);
//        }
        mViewListener.onLoadedList(jobs);
    }



    @Override
    public void onFailure(Exception e) {

        if (mTag != null){
            //加载list的情况
            List<Job> jobs = dataUtils.getAllJobsByType(mTag);
            if (!ActivityUtils.NotNullOrEmpty(jobs)){
                mViewListener.onFailed(e);
            }else {
                mViewListener.onLoadedList(jobs);
            }
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        httpUtils = null;
        dataUtils = null;
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
