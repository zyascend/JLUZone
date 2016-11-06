package com.zyascend.JLUZone.job;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.zyascend.JLUZone.R;
import com.zyascend.JLUZone.base.BaseFragmentActivity;
import com.zyascend.JLUZone.news.NewsFragment;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/10/20.
 */

public class JobActivity extends BaseFragmentActivity {

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void doOnCreate() {
        setToolbarTitle("就业资讯");
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return JobFragment.getInstance("xiaozhao");
                    case 1:
                        return JobFragment.getInstance("shixi");
                    default:
                        return JobFragment.getInstance("xiaozhao");
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return "校园招聘";
                    case 1:
                        return "实习招聘";
                    default:
                        return "校园招聘";
                }
            }
        });

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.tab_layout;
    }

}
