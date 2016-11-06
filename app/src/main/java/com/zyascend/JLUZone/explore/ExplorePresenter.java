package com.zyascend.JLUZone.explore;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.zyascend.JLUZone.base.BasePresenter;
import com.zyascend.JLUZone.job.JobActivity;
import com.zyascend.JLUZone.news.NewsActivity;
import com.zyascend.JLUZone.schedule.ScheduleActivity;
import com.zyascend.JLUZone.score.ScoreActivity;
import com.zyascend.JLUZone.utils.ActivityUtils;

/**
 *
 * Created by Administrator on 2016/10/13.
 */

public class ExplorePresenter extends BasePresenter<ExploreContract.View> implements
            ExploreContract.Presenter{


    private Context mContext;

    public ExplorePresenter(Context context) {
        mContext = context;
    }

    @Override
    public void enterSchedule() {
        ActivityUtils.enterActivity(mContext, ScheduleActivity.class);
    }

    @Override
    public void enterScore() {
        ActivityUtils.enterActivity(mContext,ScoreActivity.class);
    }

    @Override
    public void enterNews() {
        ActivityUtils.enterActivity(mContext, NewsActivity.class);
    }

    @Override
    public void enterLesson() {
        Toast.makeText(mContext, "现在不是选课时间哟", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void enterJob() {
        ActivityUtils.enterActivity(mContext, JobActivity.class);
    }

    @Override
    public void enterPic() {

    }

    @Override
    public void enterRate() {
        Toast.makeText(mContext, "现在不是评教时间哦", Toast.LENGTH_SHORT).show();
    }
}
