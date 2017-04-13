package com.zyascend.JLUZone.score;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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


    private Handler mHandler;
    private static final String TAG = "ScoreActivity";
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


    public void setHandler(Handler handler) {
        Log.d(TAG, "setHandler: ");
        this.mHandler = handler;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_score,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_statics){
            Message msg = mHandler.obtainMessage();
            msg.arg1 = -1;
            mHandler.sendMessage(msg);
        }
        return super.onOptionsItemSelected(item);
    }
}
