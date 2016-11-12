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
import com.zyascend.JLUZone.job.JobPresenter;
import com.zyascend.JLUZone.login.LoginPresenter;
import com.zyascend.JLUZone.main.MainPresenter;
import com.zyascend.JLUZone.schedule.SchedulePresenter;
import com.zyascend.JLUZone.score.ScorePresenter;

import java.util.List;

/**
 * Created by Administrator on 2016/7/6.
 */
public interface HttpManagerListener {

    void cancel();

    void login(LoginPresenter loginPresenter, boolean isLoginOutside, String user, String passWord, LoginCallBack myCallBack);
    void getCurrentInfo();
    void getSemester(SchedulePresenter schedulePresenter, TermCallBack myCallBack);
    void getSchedule(SchedulePresenter schedulePresenter, int termId, ScheduleCallBack callBack);

    void getNewScore(ScorePresenter scorePresenter, int row, ScoreCallBack httpCallBack);
    void getAllScore(ScorePresenter back, ScoreCallBack callBack);
    void getYearScore(ScorePresenter scorePresenter, int year, ScoreCallBack callBack);
    void getAvgScore(ScorePresenter scorePresenter,AvgScoreCallback callback);
    void getScoreDetail(ScorePresenter scorePresenter, String asId, ScoreDetailCallback callback);

    void getXiaoZhaoList(JobPresenter presenter, int page, JobListCallback callback);
    void getShixiList(JobPresenter presenter, int page, JobListCallback callback);
    void getJobContent(JobPresenter jobPresenter, int id, JobContentCallback callback);
    void getWeather(MainPresenter mainPresenter, WeatherCallback callback);

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
