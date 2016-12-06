package com.zyascend.JLUZone.evaluate;

import com.zyascend.JLUZone.base.BaseCallBack;
import com.zyascend.JLUZone.base.BasePresenter;
import com.zyascend.JLUZone.entity.EvaluateItem;
import com.zyascend.JLUZone.model.net.HttpManager;
import com.zyascend.JLUZone.model.net.HttpManagerListener;

import java.util.List;

/**
 * Created by Administrator on 2016/12/5.
 */

public class EvaluatePresenter extends BasePresenter<EvaluateContract.View> implements EvaluateContract.Presenter {

    private HttpManagerListener mHttp;

    public EvaluatePresenter() {
        mHttp = HttpManager.getInstance();
    }

    @Override
    public void loadEvaluateList() {
        mHttp.loadEvaluateList(new BaseCallBack<List<EvaluateItem>>() {
            @Override
            public void onSuccess(List<EvaluateItem> list) {
                mViewListener.onLoadList(list);
            }
            @Override
            public void onFailure(Exception e) {
                mViewListener.onFail();
            }
        });
    }

    @Override
    public void evaluate(String itemId, int type, String advice) {
        mHttp.evaluate(itemId, type, new BaseCallBack() {
            @Override
            public void onSuccess(Object o) {
                mViewListener.onEvaluated();
            }
            @Override
            public void onFailure(Exception e) {
                mViewListener.onFail();
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        mHttp.cancel();
        mHttp = null;
    }
}
