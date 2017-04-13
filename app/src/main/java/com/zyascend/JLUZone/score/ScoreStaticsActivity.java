package com.zyascend.JLUZone.score;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zyascend.JLUZone.R;
import com.zyascend.JLUZone.base.BaseFragmentActivity;
import com.zyascend.JLUZone.entity.ScoreStatics;
import com.zyascend.JLUZone.utils.ActivityUtils;
import com.zyascend.JLUZone.utils.ShareUtils;
import com.zyascend.JLUZone.utils.view.NumAnim;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/3/30.
 */

public class ScoreStaticsActivity extends BaseFragmentActivity implements Animator.AnimatorListener {


    public static final String INTENT_SCORE = "intent_score";
    private static final String TAG = "ScoreStaticsActivity";
    @Bind(R.id.firstGp_text)
    TextView firstGpText;
    @Bind(R.id.firstGp_view)
    RelativeLayout firstGpView;
    @Bind(R.id.firstScore_text)
    TextView firstScoreText;
    @Bind(R.id.firstScore_view)
    RelativeLayout firstScoreView;
    @Bind(R.id.bestGp_text)
    TextView bestGpText;
    @Bind(R.id.bestGp_view)
    RelativeLayout bestGpView;
    @Bind(R.id.bestScore_text)
    TextView bestScoreText;
    @Bind(R.id.bestScore_view)
    RelativeLayout bestScoreView;
    @Bind(R.id.deadLine)
    TextView deadLineText;
    @Bind(R.id.btn_share)
    ImageView btnShare;
    @Bind(R.id.btn_close)
    ImageView btnClose;

    private ScoreStatics scoreStatics;
    private int currentAnimation = -1;

    @Override
    protected void doOnCreate() {

        firstGpView.setAlpha(0);
        firstScoreView.setAlpha(0);
        bestGpView.setAlpha(0);
        bestScoreView.setAlpha(0);

        deadLineText.setText("(截至" + ActivityUtils.getCurrentDate()+")");

        scoreStatics = getIntent().getParcelableExtra(INTENT_SCORE);
        if (scoreStatics != null) showScore();
        else showError();

    }

    private void showScore() {


        ObjectAnimator animator1 = ObjectAnimator.ofFloat(firstGpView, "alpha", 0f, 1f);
        animator1.addListener(this);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(firstScoreView, "alpha", 0f, 1f);
        animator2.addListener(this);
        animator2.setStartDelay(1500);

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(bestGpView, "alpha", 0f, 1f);
        animator3.addListener(this);
        animator3.setStartDelay(3000);

        ObjectAnimator animator4 = ObjectAnimator.ofFloat(bestScoreView, "alpha", 0f, 1f);
        animator4.addListener(this);
        animator4.setStartDelay(4500);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator1, animator2, animator3, animator4);
        set.setDuration(500);
        set.start();

    }

    private void showError() {
        Toast.makeText(this, "加载数据出错...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_statistis_page1;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        currentAnimation++;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        startTextViewAnim();
    }

    private void startTextViewAnim() {
        Log.d(TAG, "startTextViewAnim: num = " + currentAnimation);
        switch (currentAnimation) {
            case 0:
                Log.d(TAG, "startTextViewAnim:fir " + scoreStatics.getGpaFirst());
                NumAnim.startAnim(firstGpText, formatDoubleToFloat(scoreStatics.getGpaFirst()), 500);
                break;
            case 1:
                NumAnim.startAnim(firstScoreText, formatDoubleToFloat(scoreStatics.getAvgScoreFirst()), 500);
                break;
            case 2:
                NumAnim.startAnim(bestGpText, formatDoubleToFloat(scoreStatics.getGpaBest()), 500);
                break;
            case 3:
                NumAnim.startAnim(bestScoreText, formatDoubleToFloat(scoreStatics.getAvgScoreBest()), 500);
                break;
        }
    }

    private float formatDoubleToFloat(double num) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return Float.parseFloat(df.format(num));
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }


    @OnClick({R.id.btn_share, R.id.btn_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_share:
                ShareUtils.getInstance(new ShareUtils.ShareCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail() {
                        Toast.makeText(ScoreStaticsActivity.this, "分享失败...", Toast.LENGTH_SHORT).show();

                    }
                }).shareView(this,getCurrentView());
                btnClose.setVisibility(View.VISIBLE);
                btnShare.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_close:
                onBackPressed();
                break;
        }
    }

    private View getCurrentView() {
        btnClose.setVisibility(View.VISIBLE);
        btnShare.setVisibility(View.VISIBLE);
        return findViewById(android.R.id.content);
    }
}
