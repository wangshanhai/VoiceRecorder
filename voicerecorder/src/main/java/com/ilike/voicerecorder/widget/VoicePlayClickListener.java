/**
 * Copyright (C) 2017 ilikeshatang. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ilike.voicerecorder.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.ilike.voicerecorder.R;
import com.ilike.voicerecorder.utils.VoiceProvider;

import java.io.File;

/**
 * desc:   voice play click listener
 * author: wangshanhai
 * email: ilikeshatang@gmail.com
 * date: 2017/10/31 15:29
 */
public class VoicePlayClickListener implements View.OnClickListener {
    private static final String TAG = "VoicePlayClickListener";

    ImageView voiceIconView;
    private boolean isSetingPlayingIcon = false;
    private int stopPlayingDrawableId = 0;


    private AnimationDrawable voiceAnimation = null;
    MediaPlayer mediaPlayer = null;
    Activity activity;
    private BaseAdapter adapter;

    public static boolean isPlaying = false;
    public static VoicePlayClickListener currentPlayListener = null;
    public static String playMsgId = "";
    public static String getLocalUrl = "";

    public VoicePlayClickListener(ImageView v, Activity context) {
        this.activity = context;
        voiceIconView = v;
    }

    /**
     * stop play voice
     */
    public void stopPlayVoice() {
        voiceAnimation.stop();
        if (stopPlayingDrawableId != 0) {
            voiceIconView.setImageResource(stopPlayingDrawableId);
        }else{
            voiceIconView.setImageResource(R.drawable.ease_chatto_voice_playing);
        }

        // stop play voice
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isPlaying = false;
        playMsgId = null;
        //   adapter.notifyDataSetChanged();
    }

    /**
     * play voice for path
     *
     * @param filePath
     */
    public void playVoice(String filePath) {
        this.getLocalUrl = filePath;
        if (isPlaying) {
            if (playMsgId != null) {
                currentPlayListener.stopPlayVoice();
            }

        }

        if (!(new File(filePath).exists())) {
            return;
        }

        playMsgId = filePath;
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);

        mediaPlayer = new MediaPlayer();
        if (VoiceProvider.getInstance().getSettingsProvider().isSpeakerOpened()) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        } else {
            audioManager.setSpeakerphoneOn(false);// 关闭扬声器
            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mediaPlayer.release();
                    mediaPlayer = null;
                    stopPlayVoice(); // stop animation
                }

            });
            isPlaying = true;
            currentPlayListener = this;
            mediaPlayer.start();
            showAnimation();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }


    /**
     * play voice for sd path
     *
     * @param filePath
     */
    public void playUrlVoice(String filePath) {
        this.getLocalUrl = filePath;
        if (isPlaying) {
            if (playMsgId != null) {
                currentPlayListener.stopPlayVoice();
            }
        }

        playMsgId = filePath;
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);

        mediaPlayer = new MediaPlayer();
        if (VoiceProvider.getInstance().getSettingsProvider().isSpeakerOpened()) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        } else {
            audioManager.setSpeakerphoneOn(false);// 关闭扬声器
            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mediaPlayer.release();
                    mediaPlayer = null;
                    stopPlayVoice(); // stop animation
                }

            });
            isPlaying = true;
            currentPlayListener = this;
            mediaPlayer.start();
            showAnimation();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }


    // show the voice playing animation
    private void showAnimation() {
        // play voice, and start animation
        if (!isSetingPlayingIcon) {
            /**
             * 使用外部设置进来的播放语音的动画
             */
            voiceIconView.setImageResource(R.drawable.voice_to_icon);
        }
        voiceAnimation = (AnimationDrawable) voiceIconView.getDrawable();
        voiceAnimation.start();
    }

    /**
     * 设置播放语音的动画
     *
     * @param drawableResoureId
     */
    public void setPlayingIconDrawableResoure(int drawableResoureId) {
        isSetingPlayingIcon = true;
        voiceIconView.setImageResource(R.drawable.voice_to_icon);
    }

    /**
     * 设置停止播放语音时，显示的静态icon
     *
     * @param drawableResoureId
     */
    public void setStopPlayIcon(int drawableResoureId) {
        this.stopPlayingDrawableId = drawableResoureId;
    }


    @Override
    public void onClick(View v) {
        String st = activity.getResources().getString(R.string.Is_download_voice_click_later);
        if (isPlaying) {
            if (playMsgId != null) {
                currentPlayListener.stopPlayVoice();
                return;
            }
            currentPlayListener.stopPlayVoice();
        }

        File file = new File(getLocalUrl);
        if (file.exists() && file.isFile()) {
            //  playVoice(getLocalUrl);
        } else
            Log.e(TAG, "file not exist");

    }

}
