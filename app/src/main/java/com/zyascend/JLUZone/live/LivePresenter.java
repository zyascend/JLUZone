package com.zyascend.JLUZone.live;

import com.zyascend.JLUZone.base.BasePresenter;
import com.zyascend.JLUZone.entity.LiveChannel;
import com.zyascend.JLUZone.model.net.HttpManager;
import com.zyascend.JLUZone.model.net.HttpManagerListener;

import java.util.List;

/**
 *
 * Created by Administrator on 2017/3/22.
 */

public class LivePresenter extends BasePresenter<LiveContract.View>implements LiveContract.Presenter {

    private HttpManager manager;
    public LivePresenter(){
        manager = HttpManager.getInstance();
    }

    @Override
    public void fetchChannel() {
        manager.getChannel(new HttpManagerListener.ChannelCallback() {
            @Override
            public void onSuccess(List<LiveChannel> channels) {
                mViewListener.onGetChannelList(channels);
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
        manager.cancel();
    }
}
