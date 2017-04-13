package com.zydemo.ijkPlayerDemo;

import android.app.Activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import tcking.github.com.giraffeplayer.IjkVideoView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 *
 * Created by zyascend on 2017/4/3.
 */

public class VideoPlayer {

    //播放器状态码
    private static final int STATUS_ERROR = -1;
    private static final int STATUS_PREPARED = 0;
    private static final int STATUS_COMPLETED = 2;
    private static final int STATUS_LOADING = 3;
    private static final int STATUS_PLAYING = 4;
    private static final int STATUS_PAUSE = 5;
    private static final int STATUS_IDLE = 1;

    private static final int ID_VIDEO_BACK = R.id.iv_video_back;
    private static final int ID_VIDEO_LIST = R.id.iv_video_playList;
    private static final int ID_VIDEO_LOCK_IV = R.id.im_video_lock;
    private static final int ID_VIDEO_PLAY = R.id.ll_video_play;
    private static final int ID_VIDEO_START = R.id.iv_video_play;
    private static final int ID_CONTROL_VIEW = R.id.control_view;
    private static final int ID_LOCK_VIEW = R.id.fr_video_lock;
    private static final int ID_PLAYLIST_VIEW = R.id.fr_video_playList;
    private static final int ID_VIDEO_TITLE = R.id.tv_video_title;
    private static final int ID_VIDEO_BRIGHTNESS = R.id.tv_video_brightness;
    private static final int ID_VIDEO_VOLUME = R.id.tv_video_volume;
    private static final int ID_VIDEO_FORWARD = R.id.tv_video_forward;
    private static final int ID_VIDEO_LOADING = R.id.pb_video_loading;
    private static final int ID_CENTER_VIEW = R.id.video_center_view;
    private static final int ID_VIDEO_CURRENTTIME = R.id.tv_video_currentTime;
    private static final int ID_VIDEO_ENDTIME = R.id.tv_video_endTime;
    private static final int ID_STATUS_VIEW = R.id.video_status_view;
    private static final int ID_VIDEO_ERROR = R.id.tv_video_error;

    private static final int MESSAGE_HIDE_ALL = 0;
    private static final int MESSAGE_SEEK_NEW_POSITION = 1;
    private static final int MESSAGE_SHOW_PROGRESS = 2;
    private static final int MESSAGE_RESTART_PLAY = 3;

    private static final String TAG = "VideoPlayer";

    private final AudioManager audioManager;
    private final int mMaxVolume;
    private final int initHeight;
    private final int initWidth;

    private AppCompatSeekBar mSeekBar;
    private ListView mPlayListView;


    private static final int ID_VIDEO_FULLSCREEN = R.id.iv_video_fullscreen;

    //显示视频的view
    private IjkVideoView videoView;
    private Activity mActivty;

    //ijk库是否可用
    private boolean playerSupport;
    private boolean isLocked;
    //控制view的管理类
    private ViewHelper mViewHelper;

    //是否为竖频
    private boolean isPortrait;
    private boolean fullScreenOnly;


    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case ID_VIDEO_PLAY:
                    toggleResumeAndPause(true);
                    break;
                case ID_VIDEO_START:
                    toggleResumeAndPause(false);
                    break;
                case ID_VIDEO_BACK:
                    doOnVideoBack();
                    break;
                case ID_LOCK_VIEW:
                    toggleLock();
                    break;
                case ID_VIDEO_FULLSCREEN:
                    toggleFullscreen();
                    break;
                case ID_VIDEO_LOCK_IV:
                    showPlayList();
                    break;
            }
        }


    };
    private int currentStatus;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_HIDE_ALL:
                    hideAll();
                    break;
                case MESSAGE_SEEK_NEW_POSITION:
                    if (!isLive && newPosition > 0)videoView.seekTo((int)newPosition);
                    newPosition = -1;
                    break;
                case MESSAGE_SHOW_PROGRESS:
                    setCurrentProgress();
                    //循环发送消息，更新当前进度,频率为1s
                    if (!isLive && isShowing){
                        sendMessageDelayed(obtainMessage(MESSAGE_SHOW_PROGRESS),1000);
                    }
                    break;
                case MESSAGE_RESTART_PLAY:
                    play();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void setCurrentProgress() {
        if (isDragging || mSeekBar == null)return;
        long position = videoView.getCurrentPosition();
        long duration = videoView.getDuration();

        if (duration > 0) {
            long pos = 1000L * position / duration;
            mSeekBar.setProgress((int) pos);
        }
        int percent = videoView.getBufferPercentage();
        mSeekBar.setSecondaryProgress(percent * 10);

        this.videoDuration = duration;
        mViewHelper.getView(ID_VIDEO_CURRENTTIME).text(generateTime(position));
        mViewHelper.getView(ID_VIDEO_ENDTIME).text(generateTime(this.videoDuration));

    }

    private boolean isShowing = false;

    public VideoPlayer isLive(boolean live) {
        isLive = live;
        return this;
    }

    private boolean isLive;
    private float screenWidthPixels;
    private long newPosition;
    private float brightness;
    private int volume;
    private String url;
    //暂停时的播放位置
    private int lastPosition;

    private void toggleResumeAndPause(boolean replay) {
        if (replay || currentStatus == STATUS_COMPLETED){
            videoView.seekTo(0);
            videoView.start();
        }else if (videoView.isPlaying()){
            toggleStatus(STATUS_PAUSE);
            videoView.pause();
        }else {
            videoView.start();
        }
        //更新图标
        updatePlayIcon();

        //隐藏ControlView
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_HIDE_ALL),3000);
    }

    private void updatePlayIcon() {
        if (videoView.isPlaying())mViewHelper.getView(ID_VIDEO_START).image(R.drawable.src_video_pause);
        else mViewHelper.getView(ID_VIDEO_START).image(R.drawable.src_video_start);
    }

    private void doOnVideoBack() {
        if (!fullScreenOnly && !isPortrait) {
            mActivty.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            mActivty.finish();
        }
    }

    private void toggleLock() {
        isLocked = ! isLocked;
        if (isLocked)mViewHelper.getView(ID_VIDEO_LOCK_IV).image(R.drawable.src_video_lock);
        else mViewHelper.getView(ID_VIDEO_LOCK_IV).image(R.drawable.src_video_unlock);
        hideAll();
        mViewHelper.getView(ID_LOCK_VIEW).show();
    }

    private void toggleFullscreen() {
        if (isPortrait){
            mActivty.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            mActivty.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        isPortrait = !isPortrait;
        toggleFullscreenImage();
    }


    private void toggleFullscreenImage() {
        if (isPortrait)mViewHelper.getView(ID_VIDEO_FULLSCREEN).image(R.drawable.src_video_fullscreen_por);
        else mViewHelper.getView(ID_VIDEO_FULLSCREEN).image(R.drawable.src_video_fullscreen_lan);
    }

    private void showPlayList() {

        if (playList == null || !playList.isEmpty())return;
        String[] strings = new String[playList.size()];
        for (int i = 0; i < playList.size(); i++) {
            strings[i] = playList.get(i).title;
        }
        //设置ListView
        mPlayListView = (ListView) mActivty.findViewById(R.id.listView);
        mPlayListView.setAdapter(new ArrayAdapter<>(mActivty,android.R.layout.simple_list_item_single_choice,strings));
        mPlayListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mPlayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                url = playList.get(i).url;
                setTitle(playList.get(i).title);
                play();
            }
        });
    }

    private boolean isDragging;
    private long videoDuration;

    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (!b)return;
            int newPosition = (int) ((videoDuration * i *1.0) / 1000);
            String time = generateTime(newPosition);
            //显示当前进度
            mViewHelper.getView(ID_VIDEO_CURRENTTIME).text(time);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //开始拖动进度条
            isDragging = true;
            //长时间（相对）显示controlView
            showAll(3000*3);
            //取消之前发送的更改进度的消息
            mHandler.removeMessages(MESSAGE_SHOW_PROGRESS);

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //拖动完毕
            isDragging = false;
            //3秒后隐藏ControlView
            showAll(3000);
            //防止停下后再拖动，一秒后再调整进度
            mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_PROGRESS,1000);
        }
    };

    private OrientationEventListener orientationEventListener;
    private long retryTime;
    private String title;
    private ArrayList<PlayBean> playList;

    public VideoPlayer(Activity activity) {
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            playerSupport=true;
        } catch (Throwable e) {
            Log.e("GiraffePlayer", "loadLibraries error", e);
        }

        this.mActivty = activity;

        //初始化VideoView
        initVideoView();

        //初始化ControlView
        initControlView();

        audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        final GestureDetector gestureDetector = new GestureDetector(activity, new PlayerGestureListener());
        View root = mActivty.findViewById(R.id.videoRootView);
        root.setClickable(true);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP)onGestureEnd();
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });

        isPortrait = getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        initHeight = root.getLayoutParams().height;
        initWidth = root.getLayoutParams().width;
        screenWidthPixels = mActivty.getResources().getDisplayMetrics().widthPixels;

        orientationEventListener = new OrientationEventListener(activity) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation >= 0 && orientation <= 30 || orientation >= 330 || (orientation >= 150 && orientation <= 210)) {
                    //竖屏
                    if (isPortrait) {
                        mActivty.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        orientationEventListener.disable();
                    }
                } else if ((orientation >= 90 && orientation <= 120) || (orientation >= 240 && orientation <= 300)) {
                    if (!isPortrait) {
                        mActivty.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        orientationEventListener.disable();
                    }
                }
            }
        };

        hideAll();

    }

    public void onConfigurationChanged(Configuration configuration){
        Log.d(TAG, "onConfigurationChanged: called");
        isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT;
        if (videoView == null || fullScreenOnly)return;
        if (mActivty instanceof AppCompatActivity){
            //隐藏可能存在的状态栏
            ActionBar bar = ((AppCompatActivity) mActivty).getSupportActionBar();
            if(bar!=null) bar.hide();
            //让Window全屏显示
            Window window = mActivty.getWindow();
            WindowManager.LayoutParams attrs = window.getAttributes();
            attrs.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setAttributes(attrs);

            orientationEventListener.disable();
        }

    }

    private void hideAll() {
        isShowing = false;
        mViewHelper.getView(ID_CONTROL_VIEW).gone();
        mViewHelper.getView(ID_PLAYLIST_VIEW).gone();
        mViewHelper.getView(ID_CENTER_VIEW).gone();
        mViewHelper.getView(ID_STATUS_VIEW).gone();
        if (isLocked)mViewHelper.getView(ID_LOCK_VIEW).show();
        else mViewHelper.getView(ID_LOCK_VIEW).gone();
        //不再更新进度
        mHandler.removeMessages(MESSAGE_SHOW_PROGRESS);
    }

    private void initControlView() {

        mViewHelper = new ViewHelper(mActivty);

        mViewHelper.initView(ID_VIDEO_BACK).click(mListener);
        mViewHelper.initView(ID_VIDEO_LIST).click(mListener);
        mViewHelper.initView(ID_LOCK_VIEW).click(mListener);
        mViewHelper.initView(ID_VIDEO_PLAY).click(mListener);
        mViewHelper.initView(ID_VIDEO_START).click(mListener);
        mViewHelper.initView(ID_VIDEO_FULLSCREEN).click(mListener);

        mSeekBar = (AppCompatSeekBar) mActivty.findViewById(R.id.sb_video_seekBar);
        mSeekBar.setMax(100);
        mSeekBar.setOnSeekBarChangeListener(mSeekListener);


        if (!playerSupport)toggleStatus(STATUS_ERROR);

        //init and hide all
        mViewHelper.initView(ID_CONTROL_VIEW).gone();
        mViewHelper.initView(ID_LOCK_VIEW).gone();
        mViewHelper.initView(ID_PLAYLIST_VIEW).gone();


    }

    private void initVideoView() {

        videoView = (IjkVideoView) mActivty.findViewById(R.id.video_view);
        videoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                toggleStatus(STATUS_PREPARED);
            }
        });
        videoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                toggleStatus(STATUS_COMPLETED);
            }
        });
        videoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                toggleStatus(STATUS_ERROR);
                // TODO: 2017/4/3 回调错误信息

                return true;
            }
        });
        videoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {

                switch (what){
                    case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                        toggleStatus(STATUS_PLAYING);
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        toggleStatus(STATUS_LOADING);
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        toggleStatus(STATUS_PLAYING);
                        break;
                    case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                        //显示下载速度（extra）
                        break;

                }
                return false;
            }
        });
    }

    /**
     * 手势结束时，隐藏center_view，seek to newPosition
     */
    private void onGestureEnd() {

        volume = -1;
        brightness = -1f;
        if (newPosition >= 0) {
            mHandler.removeMessages(MESSAGE_SEEK_NEW_POSITION);
            mHandler.sendEmptyMessage(MESSAGE_SEEK_NEW_POSITION);
        }
        mHandler.removeMessages(MESSAGE_HIDE_ALL);
        mHandler.sendEmptyMessageDelayed(MESSAGE_HIDE_ALL, 500);

    }

    /**
     * 切换播放器状态
     * @param status 状态参数
     */

    private void toggleStatus(int status) {
        this.currentStatus = status;
        hideAll();
        switch (currentStatus){
            case STATUS_LOADING:
                mViewHelper.getView(ID_VIDEO_LOADING).show();
                break;
            case STATUS_PLAYING:
                hideAll();
                break;
            case STATUS_COMPLETED:
                if (isLive)return;
                mViewHelper.getView(ID_VIDEO_PLAY).show();
                break;
            case STATUS_ERROR:
                mHandler.removeMessages(MESSAGE_SHOW_PROGRESS);
                hideAll();
                if (isLive && retryTime>0)mHandler.sendEmptyMessageDelayed(MESSAGE_RESTART_PLAY, retryTime);
                mViewHelper.getView(ID_VIDEO_ERROR).show();
                break;

        }

    }

    private void doOnPrepared() {


    }


    private int getScreenOrientation() {
        /**
         * http://stackoverflow.com/questions/10380989/how-do-i-get-the-current-orientation-activityinfo-screen-orientation-of-an-a
         */

        int rotation = mActivty.getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        mActivty.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0
                || rotation == Surface.ROTATION_180) && height > width ||
                (rotation == Surface.ROTATION_90
                        || rotation == Surface.ROTATION_270) && width > height) {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        }
        // if the device's natural orientation is landscape or if the device
        // is square:
        else {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }

        return orientation;
    }

    public void onPause() {
        showAll(3000);
        if (currentStatus == STATUS_PLAYING)videoView.pause();
        if (!isLive)lastPosition = videoView.getCurrentPosition();
    }

    public void onResume() {
        if (currentStatus != STATUS_PLAYING)return;
        if (isLive)videoView.seekTo(0);
        else videoView.seekTo(lastPosition > 0 ? lastPosition : 0);
    }

    public void onDestroy() {
        orientationEventListener.disable();
        mHandler.removeCallbacksAndMessages(null);
        videoView.stopPlayback();
    }

    public void onBackPressed() {
        if (!isPortrait)mActivty.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    private class ViewHelper {
        private Activity activity;
        private SparseArray<View> mViewMap;
        private View mView;

        public ViewHelper(Activity mActivty) {
            this.activity = mActivty;
            mViewMap = new SparseArray<>();
        }

        public ViewHelper initView(int id){
            mView = activity.findViewById(id);
            mViewMap.append(id,mView);
            return this;
        }

        public ViewHelper getView(int id){
            mView = mViewMap.get(id);
            if (mView == null){
                mView = mActivty.findViewById(id);
                mViewMap.append(id,mView);
            }
            return this;
        }

        public void click(View.OnClickListener listener){
            assert mView != null;
            mView.setOnClickListener(listener);
        }

        public void visible(int visible){
            assert mView != null;
            mView.setVisibility(View.VISIBLE);
        }

        public void image(int res){
            assert mView != null;
            ((ImageView)mView).setImageResource(res);
        }

        public void text(String text){
            assert mView != null;
            ((TextView)mView).setText(text);
        }

        public void gone(){
            assert mView != null;
            mView.setVisibility(View.GONE);
        }

        public void show(){
            assert mView != null;
            mView.setVisibility(View.VISIBLE);
        }
    }


    private class PlayerGestureListener extends GestureDetector.SimpleOnGestureListener{
        private boolean firstTouch;
        private boolean volumeControl;
        private boolean toSeek;
        /**
         * 双击
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            videoView.toggleAspectRatio();
            return true;
        }
        @Override
        public boolean onDown(MotionEvent e) {
            firstTouch = true;
            return super.onDown(e);

        }
        /**
         * 滑动
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            float deltaY = mOldY - e2.getY();
            float deltaX = mOldX - e2.getX();
            if (firstTouch) {
                toSeek = Math.abs(distanceX) >= Math.abs(distanceY);
                volumeControl=mOldX > screenWidthPixels * 0.5f;
                firstTouch = false;
            }

            mViewHelper.getView(ID_CENTER_VIEW).show();

            if (toSeek) {
                if (!isLive) {
                    onProgressSlide(-deltaX / videoView.getWidth());
                }
            } else {
                float percent = deltaY / videoView.getHeight();
                if (volumeControl) {
                    onVolumeSlide(percent);
                } else {
                    onBrightnessSlide(percent);
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (isShowing) {
                hideAll();
            } else {
                showAll(3000);
            }
            return true;
        }

    }
    private void showAll(long hideDelayedTime) {
        isShowing = true;
        //先清除以前发送的hide消息
        mHandler.removeMessages(MESSAGE_HIDE_ALL);
        //如果锁定了界面，那就只显示LockView
        if (isLocked)mViewHelper.getView(ID_LOCK_VIEW).show();
        else {
            //否则都显示
            mViewHelper.getView(ID_LOCK_VIEW).show();
            mViewHelper.getView(ID_CONTROL_VIEW).show();
            //显示进度
            mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_PROGRESS,1000);
        }
        //播放器显示3000ms后隐藏
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_HIDE_ALL),hideDelayedTime);
    }


    private void onProgressSlide(float percent) {

        long position = videoView.getCurrentPosition();
        long duration = videoView.getDuration();
        long deltaMax = Math.min(100 * 1000, duration - position);
        long delta = (long) (deltaMax * percent);

        newPosition = delta + position;
        if (newPosition > duration) {
            newPosition = duration;
        } else if (newPosition <= 0) {
            newPosition=0;
            delta=-position;
        }
        int showDelta = (int) delta / 1000;
        if (showDelta != 0) {
            mViewHelper.getView(ID_VIDEO_FORWARD).show();
            mViewHelper.getView(ID_VIDEO_FORWARD).text(generateTime(newPosition));

        }
    }

    private String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0 ? String.format(Locale.CHINA,"%02d:%02d:%02d", hours, minutes, seconds)
                : String.format(Locale.CHINA,"%02d:%02d", minutes, seconds);
    }

    private void onBrightnessSlide(float percent) {
        mViewHelper.getView(ID_VIDEO_VOLUME).gone();
        mViewHelper.getView(ID_VIDEO_BRIGHTNESS).show();

        if (brightness < 0) {
            brightness = mActivty.getWindow().getAttributes().screenBrightness;
            if (brightness <= 0.00f){
                brightness = 0.50f;
            }else if (brightness < 0.01f){
                brightness = 0.01f;
            }
        }

        Log.d(this.getClass().getSimpleName(),"brightness:"+brightness+",percent:"+ percent);
        WindowManager.LayoutParams lpa = mActivty.getWindow().getAttributes();
        lpa.screenBrightness = brightness + percent;
        if (lpa.screenBrightness > 1.0f){
            lpa.screenBrightness = 1.0f;
        }else if (lpa.screenBrightness < 0.01f){
            lpa.screenBrightness = 0.01f;
        }
        mViewHelper.getView(ID_VIDEO_BRIGHTNESS).text(((int) (lpa.screenBrightness * 100))+"%");
        mActivty.getWindow().setAttributes(lpa);
    }

    private void onVolumeSlide(float percent) {

        mViewHelper.getView(ID_VIDEO_BRIGHTNESS).gone();
        mViewHelper.getView(ID_VIDEO_VOLUME).show();

        if (volume == -1) {
            volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (volume < 0) volume = 0;
        }
        int index = (int) (percent * mMaxVolume) + volume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;
        // 变更声音
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        // 变更进度条
        int i = (int) (index * 1.0 / mMaxVolume * 100);
        String s = i + "%";
        if (i == 0) {
            s = "off";
        }
//        mViewHelper.getView(ID_VIDEO_BRIGHTNESS).gone();
        // 显示volume
        mViewHelper.getView(ID_VIDEO_VOLUME).text(s);
    }


    public VideoPlayer setRetryTime(long time){
        this.retryTime = time;
        return this;
    }

    public VideoPlayer setTitle(String s){
        this.title = s;
        mViewHelper.getView(ID_VIDEO_TITLE).text(s);
        return this;
    }

    public VideoPlayer setPlayList(ArrayList<PlayBean> list){
        this.playList = list;
        return this;
    }

    public VideoPlayer url(String url){
        this.url = url;
        return this;
    }

    public void play(){
        if (playerSupport && !TextUtils.isEmpty(url)){
            mViewHelper.getView(ID_VIDEO_LOADING).show();
            videoView.setVideoPath(url);
            videoView.start();
        }



    }




}
