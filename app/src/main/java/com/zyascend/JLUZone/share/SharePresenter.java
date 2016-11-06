package com.zyascend.JLUZone.share;

import android.content.Context;
import android.widget.ScrollView;

import com.zyascend.JLUZone.base.BasePresenter;
import com.zyascend.JLUZone.utils.ShareUtils;

/**
 *
 * Created by Administrator on 2016/10/22.
 */

public class SharePresenter extends BasePresenter<ShareContract.View>implements ShareContract.Presenter, ShareUtils.ShareCallback {

    private final Context mContext;
    private final ShareUtils mShareUtils;

    public SharePresenter(Context context) {
        mContext = context;
        mShareUtils = ShareUtils.getInstance(this);
    }

    @Override
    public void saveContent(ScrollView scrollView) {
        mShareUtils.saveContent(scrollView);
    }

    @Override
    public void shareContent(ScrollView scrollView) {
        mShareUtils.shareContent(mContext,scrollView);
    }

    @Override
    public void onSuccess() {
        mViewListener.onSuccess();
    }

    @Override
    public void onFail() {
        mViewListener.onFailed();
    }
}
