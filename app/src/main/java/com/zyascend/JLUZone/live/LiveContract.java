package com.zyascend.JLUZone.live;

import com.zyascend.JLUZone.base.BaseView;
import com.zyascend.JLUZone.entity.LiveChannel;

import java.util.List;

/**
 * Created by Administrator on 2017/3/22.
 */

public interface LiveContract {
    interface View{
        void onGetChannelList(List<LiveChannel> channels);
        void onFail();
    }
    interface Presenter{
        void fetchChannel();
    }

}
