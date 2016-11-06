package com.zyascend.JLUZone.share;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zyascend.JLUZone.R;
import com.zyascend.JLUZone.base.BaseActivity;
import com.zzhoujay.richtext.RichText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/22.
 */

public class ShareContentActivity extends BaseActivity<ShareContract.View, SharePresenter>
        implements ShareContract.View {


    public static final String KEY_SHARE_TYPE = "key_share_type";
    public static final String SHARE_TYPE_JOB = "type_job";
    public static final String SHARE_TYPE_NEWS = "type_job";
    public static final String KEY_SHARE_TITLE = "key_share_title";
    public static final String KEY_SHARE_CONTENT = "key_share_content";

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.btn_save)
    Button btnSave;
    @Bind(R.id.btn_share)
    Button btnShare;
    @Bind(R.id.tv_type)
    TextView tvType;

    @Override
    protected void doOnCreate() {

    }

    @Override
    protected void initView() {
        String type = getIntent().getStringExtra(KEY_SHARE_TYPE);
        tvType.setText(type);
        if (TextUtils.equals(type, "校园招聘") || TextUtils.equals(type, "实习招聘")) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(getIntent().getStringExtra(KEY_SHARE_TITLE));
        } else {
            tvTitle.setVisibility(View.GONE);
        }

        RichText.fromHtml(getIntent().getStringExtra(KEY_SHARE_CONTENT)).into(tvContent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share;
    }

    @Override
    protected SharePresenter getPresenter() {
        return new SharePresenter(this);
    }


    @OnClick({R.id.btn_save, R.id.btn_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                } else {
                    mPresenter.saveContent(scrollView);
                    showLoading();
                }
                break;
            case R.id.btn_share:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                } else {
                    mPresenter.shareContent(scrollView);
                    showLoading();
                }
                break;
        }
    }


    private void showLoading() {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
