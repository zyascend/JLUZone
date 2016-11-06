package com.zyascend.JLUZone.schedule;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.zyascend.JLUZone.R;
import com.zyascend.JLUZone.base.BaseActivity;
import com.zyascend.JLUZone.entity.Course;
import com.zyascend.JLUZone.entity.Term;
import com.zyascend.JLUZone.model.data.DataUtils;
import com.zyascend.JLUZone.utils.ActivityUtils;
import com.zyascend.JLUZone.utils.ShareUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 *
 * Created by Administrator on 2016/10/14.
 */

public class ScheduleActivity extends BaseActivity<ScheduleConstract.View, SchedulePresenter>
        implements ScheduleConstract.View, AdapterView.OnItemSelectedListener {
    private static final String TAG = "ScheduleActivity";
    @Bind(R.id.sp_term)
    AppCompatSpinner spTerm;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.scroll_horizontal)
    HorizontalScrollView scrollHorizontal;
    @Bind(R.id.scroll_vertical)
    ScrollView scrollVertical;
    @Bind(R.id.layout_fail)
    LinearLayout failLayout;

    private ScheduleAdapter adapter;
    private List<Term> mTermList;
    private Term mCurrentTerm;
    private int mCurrentWeek;
    private boolean isRefresh = false;


    @Override
    protected void doOnCreate() {
        mPresenter.loadTermList();
    }

    @Override
    protected void initView() {
        setToolbarTitle("课表");
        recyclerView.setLayoutManager(new GridLayoutManager(this,7));
        adapter = new ScheduleAdapter();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollVertical.smoothScrollBy(0,dy);
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled: ");
            }
        });
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_schedule;
    }

    @Override
    protected SchedulePresenter getPresenter() {
        return new SchedulePresenter(this);
    }

    @Override
    protected void loadFragment() {

    }

    @Override
    public void showLoadTermSuccess(List<Term> terms) {
        Log.d(TAG, "showLoadTermSuccess: "+ terms.size());
        if(!ActivityUtils.NotNullOrEmpty(terms) || terms.size() < 2){
            failLayout.setVisibility(View.VISIBLE);
            scrollVertical.setVisibility(View.INVISIBLE);
            scrollHorizontal.setVisibility(View.INVISIBLE);
            return;
        }
        mTermList = terms;
        List<String> termList = new ArrayList<>();
        for (int i = 0; i < terms.size(); i++) {
            termList.add(terms.get(i).getTermName());
        }
        ArrayAdapter<String> termAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,termList);
        termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTerm.setAdapter(termAdapter);
        spTerm.setSelection(1);
        mCurrentTerm = terms.get(1);
        spTerm.setOnItemSelectedListener(this);
        generateSchedule();
    }

    @Override
    public void showLoadFail(Exception e) {
        failLayout.setVisibility(View.VISIBLE);
        scrollVertical.setVisibility(View.INVISIBLE);
        scrollHorizontal.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoadCourseSuccess(List<Course> courses) {
        Log.d(TAG, "showLoadCourseSuccess: "+courses.size());
        adapter.setData(courses,mCurrentWeek);
    }

    @Override
    public void shareOK() {

    }

    @Override
    public void shareFail() {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mCurrentTerm = mTermList.get(position);
        spTerm.setSelection(position);
        generateSchedule();
        Log.d(TAG, "onItemSelected: spinner clicked !!!!");

//        generateTerm();

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void generateSchedule() {
        mPresenter.loadSchedule(Integer.parseInt(mCurrentTerm.getTermId()), isRefresh);
        isRefresh = true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.schedule,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:
                shareSchedule();
                break;
            case R.id.action_info:
                showInfo();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showInfo() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("单双周说明")
                .setMessage("带有“单双周不同”的课程，单双周的课并不一样，由于技术原因，暂时无法显示，请登录uims.jlu.edu.cn查看详情")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    private void shareSchedule() {
        //分享课表
        mPresenter.shareSchedule(scrollHorizontal);

    }
}
