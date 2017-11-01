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
package com.ilike.voicerecorder.core;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;

import com.ilike.voicerecorder.utils.EMError;
import com.ilike.voicerecorder.utils.PathUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * desc:   声音录制类
 * author: wangshanhai
 * email: ilikeshatang@gmail.com
 * date: 2017/10/30 18:36
 */
public class VoiceRecorder {
    MediaRecorder recorder;

    static final String PREFIX = "voice";
    static final String EXTENSION = ".amr";

    private boolean isRecording = false;
    private long startTime;
    private String voiceFilePath = null;
    private String voiceFileName = null;
    private File file;
    private Handler handler;

    private boolean isCustomNamingFile =false;

    public VoiceRecorder(Handler handler) {
        this.handler = handler;
    }

    /**
     * start recording to the file
     *
     * @return string
     */
    public String startRecording(Context appContext) {
        file = null;
        try {
            // need to create recorder every time, otherwise, will got exception
            // from setOutputFile when try to reuse
            if (recorder != null) {
                recorder.release();
                recorder = null;
            }
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setAudioChannels(1); // MONO
            recorder.setAudioSamplingRate(8000); // 8000Hz
            recorder.setAudioEncodingBitRate(64); // seems if change this to
            // 128, still got same file
            // size.
            // one easy way is to use temp file
            // file = File.createTempFile(PREFIX + userId, EXTENSION,
            // User.getVoicePath());

            if(!isCustomNamingFile){
                //默认命名是用时间戳0, 15位
                voiceFileName = getVoiceFileName(System.currentTimeMillis() + "");
            }

            if (!isDirExist()) {
                /**
                 * not exist ,so create default folder(/Android/data/包名/chat/voice)
                 */
                PathUtil.getInstance().createDirs("chat", "voice", appContext);
            }

            voiceFilePath = PathUtil.getInstance().getVoicePath() + "/" + voiceFileName;
            file = new File(voiceFilePath);
            recorder.setOutputFile(file.getAbsolutePath());
            recorder.prepare();
            isRecording = true;
            recorder.start();
        } catch (IOException e) {
            Log.e("voice", "prepare() failed");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isRecording) {
                        /**
                         * 监听音量，改变录音显示icon
                         */
                        android.os.Message msg = new android.os.Message();
                        msg.what = recorder.getMaxAmplitude() * 13 / 0x7FFF;
                        handler.sendMessage(msg);
                        SystemClock.sleep(100);
                    }
                } catch (Exception e) {
                    // from the crash report website, found one NPE crash from
                    // one android 4.0.4 htc phone
                    // maybe handler is null for some reason
                    Log.e("voice", e.toString());
                }
            }
        }).start();
        startTime = new Date().getTime();
        Log.d("voice", "start voice recording to file:" + file.getAbsolutePath());
        return file == null ? null : file.getAbsolutePath();
    }


    /**
     * stop the recoding
     * seconds of the voice recorded
     */
    public void discardRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
                recorder = null;
                if (file != null && file.exists() && !file.isDirectory()) {
                    file.delete();
                }
            } catch (IllegalStateException e) {
            } catch (RuntimeException e) {
            }
            isRecording = false;
        }
    }

    public int stopRecoding() {
        if (recorder != null) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;

            if (file == null || !file.exists() || !file.isFile()) {
                return EMError.FILE_INVALID;
            }
            if (file.length() == 0) {
                file.delete();
                return EMError.FILE_INVALID;
            }
            int seconds = (int) (new Date().getTime() - startTime) / 1000;
            Log.d("voice", "voice recording finished. seconds:" + seconds + " file length:" + file.length());
            return seconds;
        }
        return 0;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (recorder != null) {
            recorder.release();
        }
    }

    private String getVoiceFileName(String uid) {
        Time now = new Time();
        now.setToNow();
        return uid + now.toString().substring(0, 15) + EXTENSION;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public String getVoiceFilePath() {
        return voiceFilePath;
    }

    public String getVoiceFileName() {
        return voiceFileName;
    }




    /**
     * 判断是否存在缓存文件夹
     *
     * @return
     */
    public boolean isDirExist() {
        if (TextUtils.isEmpty(PathUtil.pathPrefix)) {
            return false;
        } else {
            return true;
        }
    }


    public void isCustomNamingFile(boolean isTrue, String name) {
        if (isTrue) {
            isCustomNamingFile = isTrue;
            setVoiceFileName(name);
        }
    }

    /**
     * 自定义音频文件命名
     *
     * @param voiceFileName
     */
    public void setVoiceFileName(String voiceFileName) {
        this.voiceFileName = voiceFileName+EXTENSION;
    }
}
