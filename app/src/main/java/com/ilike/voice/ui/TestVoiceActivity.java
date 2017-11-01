package com.ilike.voice.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ilike.voice.R;
import com.ilike.voice.adapter.EaseMessageAdapter;
import com.ilike.voice.model.MessageBean;
import com.ilike.voicerecorder.utils.TimeUtils;
import com.ilike.voicerecorder.widget.VoiceRecorderView;
import com.ilike.voicerecorder.widget.VoicePlayClickListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class TestVoiceActivity extends AppCompatActivity {

    protected VoiceRecorderView voiceRecorderView;
    protected ListView message_list;
    protected TextView tvRecorder;


    private List<MessageBean> voices;
    EaseMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        voices = new ArrayList<>();
        initView();
        getPermissions();
    }

    //运行授权，6.0以上系统需要
    private void getPermissions() {
        RxPermissions rxPermissions = new RxPermissions(TestVoiceActivity.this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean value) {
                        if (value) {
                            Toast.makeText(TestVoiceActivity.this, "同意权限", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TestVoiceActivity.this, "拒绝权限", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void initView() {

        message_list = findViewById(R.id.message_list);

        // hold to record voice
        //noinspection ConstantConditions
        voiceRecorderView = (VoiceRecorderView) findViewById(R.id.voice_recorder);

        tvRecorder = (TextView) findViewById(R.id.tv_touch_recorder);
        tvRecorder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return  voiceRecorderView.onPressToSpeakBtnTouch(v, event, new VoiceRecorderView.EaseVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        Log.e("voiceFilePath=", voiceFilePath + "  time = " + voiceTimeLength);
                        //   sendVoiceMessage(voiceFilePath, voiceTimeLength);
                        MessageBean bean = new MessageBean();
                        bean.path = voiceFilePath;
                        bean.msg = "image";
                        bean.second = voiceTimeLength;
                        bean.time = TimeUtils.getCurrentTimeInLong();
                        voices.add(bean);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });


        adapter = new EaseMessageAdapter(this, voices);
        message_list.setAdapter(adapter);
        adapter.setOnItemClickLister(new EaseMessageAdapter.onItemClickLister() {
            @Override
            public void onItemClick(ImageView imageView, String path, int position) {
                //播放语音
                new VoicePlayClickListener(imageView, path, TestVoiceActivity.this,
                        adapter).playVoice(path);
            }
        });

    }

}
