package com.zyascend.JLUZone.score;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.zyascend.JLUZone.R;
import com.zyascend.JLUZone.base.BaseFragmentActivity;
import com.zyascend.JLUZone.utils.ActivityUtils;

/**
 *
 * Created by Administrator on 2016/8/3.
 */
public class ScoreActivity extends BaseFragmentActivity {


    @Override
    protected void doOnCreate() {
        loadFragment();
        setToolbarTitle("成绩");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_score;
    }

    private void loadFragment() {
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),new ScoreFragment());
    }


}
