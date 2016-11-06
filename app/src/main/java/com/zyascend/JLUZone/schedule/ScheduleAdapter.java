package com.zyascend.JLUZone.schedule;


import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zyascend.JLUZone.R;
import com.zyascend.JLUZone.base.BaseReAdapter;
import com.zyascend.JLUZone.entity.Course;
import com.zyascend.JLUZone.entity.Score;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/14.
 */

public class ScheduleAdapter extends BaseReAdapter {
    private static final String TAG = "TAG_SCADapter";
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.place)
    TextView place;
    @Bind(R.id.teacher)
    TextView teacher;
    private List<Course> mList = new ArrayList<>();
    private List<String> mNameList;

    public ScheduleAdapter() {
        mNameList = new ArrayList<>();
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        super.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        Course course = getCourseByPosition(position);
        if (course != null) {
            String time = course.getTime();
            if (time.contains("双") || time.contains("单")){
                viewHolder.tvAlarm.setVisibility(View.VISIBLE);
            }
            viewHolder.name.setText(course.getName());
            String placeString = course.getPlace();
            if (!placeString.contains("-")){
                viewHolder.place.setText(placeString);
            }else {
                String[] p = course.getPlace().split("-");
                viewHolder.place.setText(p[1]);
            }
            String teacherAndWeek = course.getTeacher()+" "+course.getBeginWeek()+"~"+course.getEndWeek()+"周";
            viewHolder.teacher.setText(teacherAndWeek);
            viewHolder.itemView.setBackgroundResource(getColorByName(course.getName()));
        } else {
            viewHolder.itemView.setBackgroundResource(android.R.color.white);
        }

    }

    private int getColorByName(String name) {
        int color = R.color.colorAccent;
        for (int i = 0; i < mNameList.size(); i++) {
            if (TextUtils.equals(name, mNameList.get(i))) {
                color = getColor(i);
            }
        }
        if (color == R.color.colorAccent) {
            color = getColor(mNameList.size());
            mNameList.add(name);
            Log.d(TAG, "getColorByName: first get");
        }
        return color;
    }

    private int getColor(int i) {

        int[] color = {R.color.material_blue, R.color.material_deep_orange, R.color.material_green
                , R.color.material_deep_purple, R.color.material_red};
        int flags = i >= 5 ? i % 5 : i;

        return color[flags];
    }

    private Course getCourseByPosition(int position) {


        if (mList != null && !mList.isEmpty()) {
            for (Course course : mList) {
                String time = course.getTime();
                int start = 0;
                if (time != null){

                    start = Integer.parseInt(time.substring(3, 4));

                }
                int day = course.getDayOfWeek();

                if (day == 7) {
                    day = 0;
                }

                // TODO: 2016/10/16 处理有晚自习的情况
                Log.d(TAG, "getCourseByPosition: before = " + course.getName());
                if ((position + 8) % 7 == day  && position / 7 == (start+1) / 2 - 1) {
                    Log.d(TAG, "getCourseByPosition: after = " + course.getName());
                    return course;
                }
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 35;
    }

    public void setData(List<Course> courses, int mCurrentWeek) {
        Log.d(TAG, "setData: " + courses.size());
        this.mList = courses;
        int mWeek = mCurrentWeek;

        notifyDataSetChanged();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.place)
        TextView place;
        @Bind(R.id.teacher)
        TextView teacher;
        @Bind(R.id.tv_alarm)
        TextView tvAlarm;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
