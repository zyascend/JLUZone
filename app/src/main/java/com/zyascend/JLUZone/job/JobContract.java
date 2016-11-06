package com.zyascend.JLUZone.job;

import com.zyascend.JLUZone.entity.Job;
import com.zyascend.JLUZone.entity.JobContent;

import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */

public interface JobContract {
    interface Presenter{
        void getJobList(String tag,int page);
        void getJobContent(int id);


    }
    interface View{
        void onLoadedList(List<Job> jobs);
        void onFailed(Exception e);
        void onLoadedContent(JobContent content);

    }
}
