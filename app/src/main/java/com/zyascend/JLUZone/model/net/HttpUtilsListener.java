package com.zyascend.JLUZone.model.net;

import com.zyascend.JLUZone.base.BaseCallBack;
import com.zyascend.JLUZone.entity.AvgScore;
import com.zyascend.JLUZone.entity.Course;
import com.zyascend.JLUZone.entity.Job;
import com.zyascend.JLUZone.entity.JobContent;
import com.zyascend.JLUZone.entity.Score;
import com.zyascend.JLUZone.entity.ScoreDetail;
import com.zyascend.JLUZone.entity.StuInfo;
import com.zyascend.JLUZone.entity.Term;
import com.zyascend.JLUZone.entity.Weather;

import java.util.List;

/**
 * Created by Administrator on 2016/7/6.
 */
public interface HttpUtilsListener {

    void cancel();

    void login(boolean isLoginOutside ,String user,String passWord,LoginCallBack myCallBack);
    void getCurrentInfo();
    void getSemester(TermCallBack myCallBack);
    void getSchedule(int termId, ScheduleCallBack callBack);

    void getNewScore(int row,ScoreCallBack httpCallBack);
    void getAllScore(ScoreCallBack callBack);
    void getYearScore(int year,ScoreCallBack callBack);
    void getAvgScore(AvgScoreCallback callback);
    void getScoreDetail(String asId,ScoreDetailCallback callback);

    void getXiaoZhaoList(int page,JobListCallback callback);
    void getShixiList(int page,JobListCallback callback);
    void getJobContent(int id,JobContentCallback callback);
    void getWeather(WeatherCallback callback);

    interface ScoreDetailCallback extends BaseCallBack<ScoreDetail>{}
    interface AvgScoreCallback extends BaseCallBack<AvgScore>{}
    interface LoginCallBack extends BaseCallBack<StuInfo>{
    }
    interface ScoreCallBack extends BaseCallBack<List<Score>>{
    }
    interface TermCallBack extends BaseCallBack<List<Term>>{}
    interface ScheduleCallBack extends BaseCallBack<List<Course>>{}
    interface JobListCallback extends BaseCallBack<List<Job>>{}
    interface JobContentCallback extends BaseCallBack<JobContent> {}

    interface WeatherCallback extends BaseCallBack<Weather>{}
}
