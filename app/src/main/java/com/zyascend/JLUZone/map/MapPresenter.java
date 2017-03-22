package com.zyascend.JLUZone.map;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.zyascend.JLUZone.base.BasePresenter;

import java.util.List;

/**
 *
 * Created by Administrator on 2016/12/4.
 */

public class MapPresenter extends BasePresenter<MapContract.View> implements MapContract.Presenter {

    @Override
    public void locateMySelf(double lat, double lon) {
        if (mViewListener!= null){
            mViewListener.onLocated(lat,lon);
        }
    }


}
