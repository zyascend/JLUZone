package com.zyascend.JLUZone.job;

import android.content.Context;
import android.text.TextUtils;

import com.zyascend.JLUZone.base.BasePresenter;
import com.zyascend.JLUZone.entity.ConstValue;
import com.zyascend.JLUZone.entity.Job;
import com.zyascend.JLUZone.entity.JobContent;
import com.zyascend.JLUZone.model.data.DataUtils;
import com.zyascend.JLUZone.model.net.HttpUtils;
import com.zyascend.JLUZone.model.net.HttpUtilsListener;
import com.zyascend.JLUZone.utils.ActivityUtils;

import java.util.List;

/**
 *
 * Created by Administrator on 2016/10/20.
 */

public class JobPresenter extends BasePresenter<JobContract.View> implements JobContract.Presenter
        , HttpUtilsListener.JobListCallback
        {
    private HttpUtils httpUtils;
    private DataUtils dataUtils;
    private String mTag;

    public JobPresenter(Context context){
        httpUtils = HttpUtils.getInstance();
        dataUtils = DataUtils.getInstance(context.getApplicationContext());
    }

    @Override
    public void getJobList(String tag, int page) {
        mTag = tag;
        if (TextUtils.equals(tag, ConstValue.TAG_XIAOZHAO)){
            httpUtils.getXiaoZhaoList(page,this);
        }else {
            httpUtils.getShixiList(page,this);
        }

    }

    @Override
    public void getJobContent(int id) {
        mTag = null;
        httpUtils.getJobContent(id, new HttpUtilsListener.JobContentCallback() {
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
    }
}
