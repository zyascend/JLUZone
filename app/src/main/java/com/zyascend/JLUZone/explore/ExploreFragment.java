package com.zyascend.JLUZone.explore;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zyascend.JLUZone.R;
import com.zyascend.JLUZone.base.BaseFragment;
import com.zyascend.JLUZone.utils.ActivityUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * Created by Administrator on 2016/10/13.
 */

public class ExploreFragment extends BaseFragment<ExploreContract.View, ExplorePresenter> {


    @Bind(R.id.ll_schedule)
    LinearLayout llSchedule;
    @Bind(R.id.ll_score)
    LinearLayout llScore;
    @Bind(R.id.ll_news)
    LinearLayout llNews;
    @Bind(R.id.ll_job)
    LinearLayout llJob;
    @Bind(R.id.ll_lesson)
    LinearLayout llLesson;
    @Bind(R.id.ll_rate)
    LinearLayout llRate;
    @Bind(R.id.ll_more)
    LinearLayout llMore;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_explore;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected ExplorePresenter getPresenter() {
        return new ExplorePresenter(getActivity());
    }


    @OnClick({R.id.ll_schedule, R.id.ll_score, R.id.ll_news, R.id.ll_job, R.id.ll_lesson, R.id.ll_rate, R.id.ll_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_schedule:
                mPresenter.enterSchedule();
                break;
            case R.id.ll_score:
                mPresenter.enterScore();
                break;
            case R.id.ll_news:
                mPresenter.enterNews();
                break;
            case R.id.ll_job:
                mPresenter.enterJob();
                break;
            case R.id.ll_lesson:
                mPresenter.enterLesson();
                break;
            case R.id.ll_rate:
                mPresenter.enterRate();
                break;
            case R.id.ll_more:
                Toast.makeText(getActivity(), "更多功能，敬请期待", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
