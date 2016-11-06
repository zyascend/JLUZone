package com.zyascend.JLUZone.model.data;

import com.zyascend.JLUZone.entity.Course;
import com.zyascend.JLUZone.entity.Editor;
import com.zyascend.JLUZone.entity.Job;
import com.zyascend.JLUZone.entity.MainImage;
import com.zyascend.JLUZone.entity.News;
import com.zyascend.JLUZone.entity.StuInfo;
import com.zyascend.JLUZone.entity.Score;
import com.zyascend.JLUZone.entity.Term;
import com.zyascend.JLUZone.entity.Todo;
import com.zyascend.JLUZone.login.LoginActivity;
import com.zyascend.JLUZone.model.net.HttpUtilsListener;

import java.util.List;

/**
 *
 * Created by Administrator on 2016/7/6.
 */
public interface DataListener {
    void saveStuInfo(StuInfo stuInfo);

    void getStuInfo(HttpUtilsListener.LoginCallBack callBack);

    void saveSchedule(Course course);

    List<Course> getAllCoursesByTerm(int termId);

    Course getCourse(int id);

    void upDateCourse(List<Course> courses, int termId);

    List<Score> getScoresByType(int type, int id);

    void saveScore(Score score);

    void saveNews(News news);
    News getNewsByUrl(String url);
    List<News> getNewsByEditor(String editor);
    List<News> getAllNews();

    void saveJobs(Job job);
    List<Job> getAllJobsByType(String type);

    void saveEditor(List<String> list,boolean type);
    List<String> getEditor(boolean type);

    List<MainImage> getImages();
    List<Todo> getTodos();
    List<Course> getCourseByDayAndTerm(int day,int termId);
    void saveTodos(List<Todo> todoList);

    List<Term> getTerms();
    void saveTerms(List<Term> terms);
}