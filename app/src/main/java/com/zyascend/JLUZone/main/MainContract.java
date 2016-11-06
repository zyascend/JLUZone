package com.zyascend.JLUZone.main;

import android.support.v7.widget.Toolbar;

import com.zyascend.JLUZone.entity.Course;
import com.zyascend.JLUZone.entity.MainImage;
import com.zyascend.JLUZone.entity.Todo;
import com.zyascend.JLUZone.entity.Weather;

import java.util.List;

/**
 * Created by Administrator on 2016/10/13.
 */

public interface MainContract {
    interface View{
        void onLoadImagesSuccess(List<MainImage> imageList);
        void onLoadWeather(Weather weather);
        void onLoadSchedule(List<Course> courses);
        void onLoadTodo(List<Todo> todoList);
        void failed();
    }
    interface Presenter{
        void loadImages();
        void loadWeather();
        void loadSchedule(int day);
        void loadTodo();
        void saveTodo(List<Todo> todoList);
    }
}
