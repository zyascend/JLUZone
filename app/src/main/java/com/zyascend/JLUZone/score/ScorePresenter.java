package com.zyascend.JLUZone.score;

import android.content.Context;
import android.util.Log;

import com.zyascend.JLUZone.base.BasePresenter;
import com.zyascend.JLUZone.entity.ConstValue;
import com.zyascend.JLUZone.entity.Score;
import com.zyascend.JLUZone.entity.ScoreDetail;
import com.zyascend.JLUZone.http.OkHttpUtils;
import com.zyascend.JLUZone.model.data.DataListener;
import com.zyascend.JLUZone.model.data.DataUtils;
import com.zyascend.JLUZone.model.net.HttpManager;
import com.zyascend.JLUZone.model.net.HttpManagerListener;
import com.zyascend.JLUZone.utils.ActivityUtils;
import com.zyascend.JLUZone.utils.ScoreSortUtils;

import java.util.List;



/**
 *
 * Created by Administrator on 2016/8/3.
 */
public class ScorePresenter extends BasePresenter<ScoreContract.View>implements ScoreContract.Presenter, HttpManagerListener.ScoreCallBack {

    private DataListener mDataListener;
    private HttpManagerListener mHttpUtils;
    private int mType;

    private static final String TAG = "TAG_ScorePresenter";


    public ScorePresenter(Context context){
        mDataListener = DataUtils.getInstance(context.getApplicationContext());
        mHttpUtils = HttpManager.getInstance();
    }



    @Override
    public void getScore(int params, int type) {
        mType = type;
        Log.d(TAG, "getScore: "+params);
        if (type == ConstValue.SCORE_TYPE_NEW){
            mHttpUtils.getNewScore(this,params,this);
        }else{
            List<Score> scores = mDataListener.getScoresByType(type, params);
            if (ActivityUtils.NotNullOrEmpty(scores)){
                mViewListener.loadScore(scores);
            }else {
                if (type == ConstValue.SCORE_TYPE_YEAR){
                    mHttpUtils.getYearScore(this,params,this);
                }else if (type == ConstValue.SCORE_TYPE_ALL){
                    mHttpUtils.getAllScore(this,this);
                }
            }
        }
    }

    @Override
    public void getScoreDetail(String asId) {
        mHttpUtils.getScoreDetail(this,asId, new HttpManagerListener.ScoreDetailCallback() {
            @Override
            public void onSuccess(ScoreDetail detail) {
                Log.d(TAG, "onSuccess: ");
                mViewListener.loadScoreDetail(detail);
            }

            @Override
            public void onFailure(Exception e) {
                mViewListener.showFailure();
            }
        });
    }

    @Override
    public void sort(int type, List<Score> scores) {
        if (ActivityUtils.NotNullOrEmpty(scores)){
            List<Score> list = ScoreSortUtils.getInstance().sort(type,scores);
            mViewListener.onSorted(list);
        }
    }


    @Override
    public void detachView() {
        super.detachView();
        mHttpUtils.cancel();
        mHttpUtils = null;
        mDataListener = null;
        OkHttpUtils.getInstance().cancelTag(this);
    }

    @Override
    public void onSuccess(List<Score> scores) {
        mViewListener.loadScore(scores);
        Log.d(TAG, "onLoadedList: "+scores.size());
        // TODO: 2016/10/16 数据库的操作
//        for (Score score : scores) {
//            mDataListener.saveScore(score);
//        }
    }

    @Override
    public void onFailure(Exception e) {
        mViewListener.showFailure();
    }
}
