package com.zyasend.customvideoplayer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Administrator on 2017/4/4.
 */


public class PlayBean implements Parcelable{

    public String title;
    public String url;

    protected PlayBean(Parcel in) {
        title = in.readString();
        url = in.readString();
    }

    public static final Creator<PlayBean> CREATOR = new Creator<PlayBean>() {
        @Override
        public PlayBean createFromParcel(Parcel in) {
            return new PlayBean(in);
        }

        @Override
        public PlayBean[] newArray(int size) {
            return new PlayBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(url);
    }
}
