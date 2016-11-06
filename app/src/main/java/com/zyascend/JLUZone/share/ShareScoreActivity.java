package com.zyascend.JLUZone.share;

import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zyascend.JLUZone.R;
import com.zyascend.JLUZone.base.BaseActivity;
import com.zyascend.JLUZone.entity.Score;
import com.zyascend.JLUZone.entity.ScoreDetail;
import com.zyascend.JLUZone.utils.ActivityUtils;
import com.zyascend.JLUZone.utils.PieChart;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.zyascend.JLUZone.score.ScoreDetailActivity.EXTRA_DETAIL;
import static com.zyascend.JLUZone.score.ScoreDetailActivity.EXTRA_SCORE;

/**
 * Created by Administrator on 2016/11/4.
 */

public class ShareScoreActivity extends BaseActivity<ShareContract.View, SharePresenter>
        implements ShareContract.View {
    @Bind(R.id.tags)
    ImageView tags;
    @Bind(R.id.tv_type)
    TextView tvType;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_score)
    TextView tvScore;
    @Bind(R.id.tv_gpoint)
    TextView tvGpoint;
    @Bind(R.id.fenbu)
    TextView fenbu;
    @Bind(R.id.pie)
    PieChart pie;
    @Bind(R.id.createrBy)
    TextView createrBy;
    @Bind(R.id.tv_stuName)
    TextView tvStuName;
    @Bind(R.id.score)
    TextView score;
    @Bind(R.id.gpoint)
    TextView gpoint;
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    @Override
    protected void doOnCreate() {

    }

    @Override
    protected void initView() {
        setToolbarTitle("分享成绩");
        tvType.setText("单科成绩");
        Score mScore = getIntent().getParcelableExtra(EXTRA_SCORE);
        if (mScore != null) {
            tvName.setText(mScore.getName());
            tvGpoint.setText(mScore.getGpoint());
            tvScore.setText(mScore.getScore());
            tvStuName.setText(mScore.getStuName());
        }

        ArrayList<ScoreDetail.ItemsBean> mItems = getIntent().getParcelableArrayListExtra(EXTRA_DETAIL);
        if (mItems != null && !mItems.isEmpty()) {
            double[] data = new double[5];
            String[] titles = new String[5];

            for (int i = 0; i < mItems.size(); i++) {
                data[i] = mItems.get(i).getPercent();
                String s = getTitles(mItems.get(i).getLabel());
                titles[i] = s + "  " + (int) data[i] + "%";
            }

            pie.setDatas(data);
            pie.setTitles(titles);

            if (ActivityUtils.isNumeric(mScore.getScore())) {
                pie.setMainCount(Integer.parseInt(mScore.getScore()));
            }
        }

    }

    private String getTitles(String s) {
        while (true) {
            int i1 = s.indexOf("(");
            if (i1 == -1)
                break;
            int i2 = s.indexOf(")");
            if (i2 == -1)
                break;
            s = s.replace(s.substring(i1, i2 + 1), "");
        }
        return s;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_score_share;
    }

    @Override
    protected SharePresenter getPresenter() {
        return new SharePresenter(this);
    }

    @Override
    protected void loadFragment() {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.score_share,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_share){
            mPresenter.shareContent(scrollView);
        }
        return super.onOptionsItemSelected(item);
    }
}
