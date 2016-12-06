package com.zyascend.JLUZone.main;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.zyascend.JLUZone.R;
import com.zyascend.JLUZone.base.BaseFragmentActivity;
import com.zyascend.JLUZone.explore.ExploreFragment;
import com.zyascend.JLUZone.user.UserFragment;
import com.zyascend.JLUZone.utils.ActivityUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.zyascend.JLUZone.R.id.toolbar;


/**
 *
 * Created by zyascend on 2016/8/2.
 */
public class MainActivity extends BaseFragmentActivity implements OnTabReselectListener, OnTabSelectListener {

    private static final String TAG = "TAG";
    @Bind(R.id.bottomBar)
    BottomBar bottomBar;
    private MainFragment mainFragment;
    private UserFragment userFragment;
    private ExploreFragment exploreFragment;

    @Override
    protected void doOnCreate() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        AppBarLayout.LayoutParams param = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
        param.setMargins(0 , getStatusBarHeight() , 0 , 0);
        mToolbar.setLayoutParams(param);
        setToolbarTitle("知吉");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        if (saveState == null){
            //初次加载时
            mainFragment = MainFragment.newInstance();
            userFragment = new UserFragment();
            exploreFragment = new ExploreFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame,mainFragment)
                    .add(R.id.contentFrame,userFragment)
                    .add(R.id.contentFrame,exploreFragment)
//                    .hide(userFragment)
//                    .hide(exploreFragment)
                    .commit();

        }

        bottomBar.setOnTabSelectListener(this);
        bottomBar.setOnTabReselectListener(this);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        Log.d(TAG, "getStatusBarHeight: result = " + result);
        return result;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void onTabReSelected(@IdRes int tabId) {

    }

    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId){
            case R.id.tab_main:
                setToolbarTitle("知吉");
                getSupportFragmentManager().beginTransaction()
                        .show(mainFragment)
                        .hide(userFragment)
                        .hide(exploreFragment)
                        .commit();
                break;
            case R.id.tab_explore:
                setToolbarTitle("发现");
//                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),new ExploreFragment());

                getSupportFragmentManager().beginTransaction()
                        .show(exploreFragment)
                        .hide(userFragment)
                        .hide(mainFragment)
                        .commit();
                break;
            case R.id.tab_user:
                setToolbarTitle("设置");
//                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),new UserFragment());
                getSupportFragmentManager().beginTransaction()
                        .show(userFragment)
                        .hide(mainFragment)
                        .hide(exploreFragment)
                        .commit();
                break;
        }
    }

}
