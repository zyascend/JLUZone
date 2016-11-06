package com.zyascend.JLUZone.schedule;

import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import com.zyascend.JLUZone.entity.Course;
import com.zyascend.JLUZone.entity.Term;

import java.util.List;

/**
 *
 * Created by Administrator on 2016/10/14.
 */

public interface ScheduleConstract {
    interface View{
        void showLoadTermSuccess(List<Term> terms);
        void showLoadFail(Exception e);
        void showLoadCourseSuccess(List<Course> courses);
        void shareOK();
        void shareFail();

    }
    interface Presenter{
        void loadTermList();
        void loadSchedule(int termId,boolean refresh);
        void shareSchedule(HorizontalScrollView scrollView);
    }
}
