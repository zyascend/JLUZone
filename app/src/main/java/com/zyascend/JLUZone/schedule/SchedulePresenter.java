package com.zyascend.JLUZone.schedule;

import android.content.Context;
import android.util.Log;
import android.widget.HorizontalScrollView;

import com.zyascend.JLUZone.base.BasePresenter;
import com.zyascend.JLUZone.entity.Course;
import com.zyascend.JLUZone.entity.Term;
import com.zyascend.JLUZone.http.OkHttpUtils;
import com.zyascend.JLUZone.model.data.DataListener;
import com.zyascend.JLUZone.model.data.DataUtils;
import com.zyascend.JLUZone.model.net.HttpManager;
import com.zyascend.JLUZone.model.net.HttpManagerListener;
import com.zyascend.JLUZone.utils.ActivityUtils;
import com.zyascend.JLUZone.utils.ShareUtils;

import java.util.List;

/**
 *
 * Created by Administrator on 2016/10/14.
 */

public class SchedulePresenter extends BasePresenter<ScheduleConstract.View>
implements ScheduleConstract.Presenter{
    private static final String TAG = "TAG_SchedulePresenter";
    private final Context mContext;
    private DataListener mDataUtils;
    private HttpManagerListener mHttpUtils;

    public SchedulePresenter(Context context) {
        mHttpUtils = HttpManager.getInstance();
        mDataUtils = DataUtils.getInstance(context.getApplicationContext());
        mContext = context;
    }


    @Override
    public void loadTermList() {
        //先从网络中获取
        mHttpUtils.getSemester(this,new HttpManagerListener.TermCallBack() {
            @Override
            public void onSuccess(List<Term> terms) {
                if (!isViewAttached()){
                    return;
                }

                if (ActivityUtils.NotNullOrEmpty(terms)){
                     mViewListener.showLoadTermSuccess(terms);
                    //获取成功，存入数据库
                    mDataUtils.saveTerms(terms);
                    Log.d(TAG, "onSuccess: "+terms.size());
                }else {
                    //返回为空时也从数据库中查找
                    getTermsFromData();
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (!isViewAttached()){
                    return;
                }
                getTermsFromData();
            }
        });

    }

    private void getTermsFromData() {
        //获取失败，从数据库找
        List<Term> terms = mDataUtils.getTerms();
        if (ActivityUtils.NotNullOrEmpty(terms)){
            mViewListener.showLoadTermSuccess(terms);
        }else {
            //数据库中也无，显示失败
            mViewListener.showLoadFail(null);
        }
    }

    @Override
    public void loadSchedule(int termId,boolean refresh) {
        // TODO: 2016/10/15 判断：在数据库否存在对应termId的数据
        //若有，则读取
        //若无，则再次加载

        if (!refresh){
            List<Course> courses = mDataUtils.getAllCoursesByTerm(termId);
            if (ActivityUtils.NotNullOrEmpty(courses)){
                mViewListener.showLoadCourseSuccess(courses);
                Log.d(TAG, "loadSchedule: from data size = "+courses.size());
            }else {
                loadScheduleFromNet(termId);
            }
        }else {
            loadScheduleFromNet(termId);
        }
    }

    @Override
    public void shareSchedule(HorizontalScrollView scrollView) {
        ShareUtils.getInstance(new ShareUtils.ShareCallback() {
            @Override
            public void onSuccess() {
                mViewListener.shareOK();
            }

            @Override
            public void onFail() {
                mViewListener.shareFail();
            }
        }).shareSchedule(scrollView,mContext);
    }

    private void loadScheduleFromNet(final int termId) {
        mHttpUtils.getSchedule(this,termId, new HttpManagerListener.ScheduleCallBack() {
            @Override
            public void onSuccess(List<Course> courses) {

                mViewListener.showLoadCourseSuccess(courses);
                if (ActivityUtils.NotNullOrEmpty(courses)){
                    for (Course course : courses) {
                        mDataUtils.saveSchedule(course);
                    }
                    Log.d(TAG, "onLoadedList: 已添加");
                }

//                if (!mRefresh){
//
//                }else {
//                    mDataUtils.upDateCourse(courses,termId);
//                    Log.d(TAG, "onLoadedList: 已更新");
//                }
            }

            @Override
            public void onFailure(Exception e) {
                mViewListener.showLoadFail(e);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        mDataUtils = null;
        mHttpUtils = null;
        OkHttpUtils.getInstance().cancelTag(this);

    }
}
