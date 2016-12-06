package com.zyascend.JLUZone.map;

/**
 * Created by Administrator on 2016/12/4.
 */

public interface MapContract {
    interface View{
        void onLocated(double lat, double lon);
    }
    interface Presenter{
        void locateMySelf(double lat, double lon);
    }
}
