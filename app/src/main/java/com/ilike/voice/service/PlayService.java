package com.ilike.voice.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;


import com.ilike.voice.R;

import java.io.IOException;

/**
 * desc   : 后台播放语音
 * author : wangshanhai
 * email  : ilikeshatang@gmail.com
 * date   : 2017/11/3 11:28
 */
public class PlayService extends Service implements MediaPlayer.OnCompletionListener {
    private static final String TAG = "Service";

    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PLAYING = 2;
    private static final int STATE_PAUSE = 3;

    private MediaPlayer mPlayer = new MediaPlayer();

    private int mPlayState = STATE_IDLE;

    private String playUrl = "";


    ImageView voiceIconView;
    private AnimationDrawable voiceAnimation = null;
    public static boolean isPlaying = false;


    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer.setOnCompletionListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("url")) {
            playUrl = intent.getStringExtra("url");
        }
        return START_NOT_STICKY;
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.e("onCompletion-->", "onCompletion 100%");
        stopPlayVoiceAnimation();
        quit();
    }

    public void play(String url) {
        if (isPlaying) {
            if (playUrl != null) {
                stopPlayVoiceAnimation();
            }
        }
        try {
            mPlayer.reset();
            mPlayer.setDataSource(url);
            mPlayer.prepareAsync();
            mPlayState = STATE_PREPARING;
            mPlayer.setOnPreparedListener(mPreparedListener);
            mPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            isPlaying = true;
            playUrl = url;
            showAnimation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void stopPlayVoiceAnimation() {
        if (voiceAnimation != null && voiceAnimation.isRunning()) {
            voiceAnimation.stop();
            voiceIconView.setImageResource(R.drawable.ease_chatto_voice_playing);
        }
        isPlaying = false;
        playUrl = null;
    }

    public void stopPlayVoiceAnimation2() {
        if (voiceAnimation != null) {
            voiceAnimation.stop();
            voiceIconView.setImageResource(R.drawable.ease_chatto_voice_playing);
        }
        if(isPlaying){
            stopPlaying();
        }
        isPlaying = false;
        playUrl = null;
    }


    // show the voice playing animation
    private void showAnimation() {
        // play voice, and start animation
        voiceIconView.setImageResource(R.drawable.voice_to_icon);
        voiceAnimation = (AnimationDrawable) voiceIconView.getDrawable();
        voiceAnimation.start();
    }

    public void setImageView(ImageView imageView){
        stopPlayVoiceAnimation();
        this.voiceIconView = imageView;
    }


    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (isPreparing()) {
                start();
            }
        }
    };

    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            Log.e("onBufferingUpdate--->", percent + "%");
        }
    };


    void start() {
        if (!isPreparing() && !isPausing()) {
            return;
        }

        mPlayer.start();
        mPlayState = STATE_PLAYING;


    }

    public void stopPlaying() {
        // stop play voice
        if (mPlayer != null) {
            mPlayer.stop();
        }
    }


    public void stop() {
        if (isIdle()) {
            return;
        }

        mPlayer.reset();
        mPlayState = STATE_IDLE;
    }


    public boolean isPlaying() {
        return mPlayState == STATE_PLAYING;
    }

    public boolean isPausing() {
        return mPlayState == STATE_PAUSE;
    }

    public boolean isPreparing() {
        return mPlayState == STATE_PREPARING;
    }

    public boolean isIdle() {
        return mPlayState == STATE_IDLE;
    }


    @Override
    public void onDestroy() {
        mPlayer.reset();
        mPlayer.release();
        mPlayer = null;
        super.onDestroy();
        Log.i(TAG, "onDestroy: " + getClass().getSimpleName());
    }

    public void quit() {
        stop();
        stopSelf();
    }

    public class PlayBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }
}
