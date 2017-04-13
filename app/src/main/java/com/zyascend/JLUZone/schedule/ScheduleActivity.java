package com.zyascend.JLUZone.schedule;

import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zyascend.JLUZone.R;
import com.zyascend.JLUZone.base.BaseActivity;
import com.zyascend.JLUZone.entity.Course;
import com.zyascend.JLUZone.entity.Term;
import com.zyascend.JLUZone.model.data.DataUtils;
import com.zyascend.JLUZone.utils.ActivityUtils;
import com.zyascend.JLUZone.utils.ShareUtils;
import com.zyascend.JLUZone.utils.view.CustomMessageDialog;
import com.zyascend.JLUZone.utils.view.RecyclerDivider;

import org.greenrobot.greendao.annotation.Id;

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

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.scroll_horizontal)
    HorizontalScrollView scrollHorizontal;
    @Bind(R.id.scroll_vertical)
    ScrollView scrollVertical;
    @Bind(R.id.layout_fail)
    LinearLayout failLayout;
    @Bind(R.id.spinner)
    AppCompatSpinner spinner;

    private ScheduleAdapter adapter;
    private List<Term> mTermList;
    private Term mCurrentTerm;
    private int mCurrentWeek;
    private boolean isRefresh = false;
    private int mCurrentTermId;
    private boolean spinnerShowed = false;


    @Override
    protected void doOnCreate() {
        mPresenter.loadCurrentTerm();
    }

    @Override
    protected void initView() {
        setToolbarTitle("我的课表");
        recyclerView.setLayoutManager(new GridLayoutManager(this,7));
        adapter = new ScheduleAdapter(this);
        recyclerView.addItemDecoration(new RecyclerDivider(ContextCompat.getDrawable(this,R.drawable.divider)));
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

        spinner.setVisibility(View.VISIBLE);
        List<String> termList = new ArrayList<>();
        for (int i = 0; i < terms.size(); i++) {
            termList.add(terms.get(i).getTermName());
        }

        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,termList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner .setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                mCurrentTermId = Integer.parseInt(mTermList.get(pos).getTermId());
                generateSchedule();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

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
    public void showLoadCurrentTerm(int id) {
        mCurrentTermId = id;
        generateSchedule();
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
        generateSchedule();
        Log.d(TAG, "onItemSelected: spinner clicked !!!!");

//        generateTerm();

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void generateSchedule() {
        mPresenter.loadSchedule(mCurrentTermId, isRefresh);
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
                break;
            case R.id.action_change_year:
                if (spinnerShowed)spinner.setVisibility(View.GONE);
                else {
                    mPresenter.loadTermList();
                    spinnerShowed = true;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showInfo() {
        TextView textView = new TextView(this);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
        textView.setText(getString(R.string.week_info));
        final CustomMessageDialog dialog = new CustomMessageDialog.Builder(this)
                .setStyle(R.style.CustomDialog)
                .setTitle("注意：")
                .setContentView(textView)
                .removeCancelButton(true)
                .onPositiveClicked("知道了", new CustomMessageDialog.CustomDialogListener() {
                    @Override
                    public void onClick(View v, CustomMessageDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build();
        dialog.show();
    }

    private void shareSchedule() {
        //分享课表
        mPresenter.shareSchedule(scrollHorizontal);
    }
}
