package com.zyasend.customvideoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

/**
 *
 * Created by Administrator on 2017/4/6.
 */

public class PlayActivity extends AppCompatActivity {

    public static final String INTENT_PLAY_LIST = "PLAY_LIST";
    VideoPlayer player;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_palyer);
        Config config = getIntent().getParcelableExtra("config");
        if (config == null || TextUtils.isEmpty(config.url)){
            Toast.makeText(this, "数据为空", Toast.LENGTH_SHORT).show();
        }else {
            player = new VideoPlayer(this)
                    .isLive(config.isLive)
                    .setRetryTime(config.defaultRetryTime)
                    .setTitle(config.title)
                    .url(config.url);
            player.play();
        }


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(player!=null)player.onBackPressed();
    }

    public static Config init(Activity activity){
        return new Config(activity);
    }

    public static class Config implements Parcelable {

        private Context context;
        private boolean isLive;
        private boolean fullScreenOnly;
        private long defaultRetryTime = 5 * 1000;
        private String title;
        private String url;

        public Config isLive(boolean live) {
            isLive = live;
            return this;
        }

        public Config setFullScreenOnly(boolean fullScreenOnly) {
            this.fullScreenOnly = fullScreenOnly;
            return this;
        }

        public Config setDefaultRetryTime(long defaultRetryTime) {
            this.defaultRetryTime = defaultRetryTime;
            return this;

        }

        public Config setTitle(String title) {
            this.title = title;
            return this;

        }

        public Config setUrl(String url) {
            this.url = url;
            return this;
        }


        public void play(){
            Intent intent = new Intent(context,PlayActivity.class);
            intent.putExtra("config",this);
            context.startActivity(intent);
        }


        public Config(Context context){
            this.context = context;
        }



        protected Config(Parcel in) {
            isLive = in.readByte() != 0;
            fullScreenOnly = in.readByte() != 0;
            defaultRetryTime = in.readLong();
            title = in.readString();
            url = in.readString();
        }

        public static final Creator<Config> CREATOR = new Creator<Config>() {
            @Override
            public Config createFromParcel(Parcel in) {
                return new Config(in);
            }

            @Override
            public Config[] newArray(int size) {
                return new Config[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeByte((byte) (isLive ? 1 : 0));
            parcel.writeByte((byte) (fullScreenOnly ? 1 : 0));
            parcel.writeLong(defaultRetryTime);
            parcel.writeString(title);
            parcel.writeString(url);
        }
    }

}
