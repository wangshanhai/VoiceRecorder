package com.layuva.activity.ui;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.layuva.R;
import com.layuva.activity.adapter.EaseMessageAdapter;
import com.layuva.activity.model.MessageBean;
import com.layuva.activity.utils.emoji.EaseEmojicon;
import com.layuva.activity.utils.TimeUtils;
import com.layuva.activity.widget.EaseChatExtendMenu;
import com.layuva.activity.widget.EaseChatInputMenu;
import com.layuva.activity.widget.EaseVoiceRecorderView;
import com.layuva.activity.widget.chatrow.EaseChatRowVoicePlayClickListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class MainActivity extends AppCompatActivity {


    protected static final String TAG = "EaseChatFragment";
    protected static final int REQUEST_CODE_MAP = 1;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;

    protected EaseChatInputMenu inputMenu;


    protected InputMethodManager inputManager;


    protected EaseVoiceRecorderView voiceRecorderView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ListView message_list;


    static final int ITEM_TAKE_PICTURE = 1;
    static final int ITEM_PICTURE = 2;
    static final int ITEM_LOCATION = 3;


    protected int[] itemStrings = {R.string.attach_take_pic,
            R.string.attach_picture,
            R.string.attach_location,
            R.string.attach_take_pic,
            R.string.attach_picture,
            R.string.attach_location,
            R.string.attach_picture,
            R.string.attach_location};
    protected int[] itemdrawables = {R.drawable.ease_chat_takepic_selector,
            R.drawable.ease_chat_image_selector,
            R.drawable.ease_chat_location_selector,
            R.drawable.ease_chat_takepic_selector,
            R.drawable.ease_chat_image_selector,
            R.drawable.ease_chat_location_selector,
            R.drawable.ease_chat_image_selector,
            R.drawable.ease_chat_location_selector};
    protected int[] itemIds = {ITEM_TAKE_PICTURE, ITEM_PICTURE, ITEM_LOCATION,
            ITEM_TAKE_PICTURE, ITEM_PICTURE, ITEM_LOCATION, ITEM_PICTURE, ITEM_LOCATION};
    private boolean isMessageListInited;
    protected MyItemClickListener extendMenuItemClickListener;
    protected boolean isRoaming = false;


    private List<MessageBean> voices;
    EaseMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        voices = new ArrayList<>();
        initView();
        getPermissions();
    }

    //运行授权，6.0以上系统需要
    private void getPermissions() {
        RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean value) {
                        if (value) {
                            Toast.makeText(MainActivity.this, "同意权限", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "拒绝权限", Toast.LENGTH_SHORT).show();
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
        voiceRecorderView = (EaseVoiceRecorderView) findViewById(R.id.voice_recorder);


        extendMenuItemClickListener = new MyItemClickListener();
        inputMenu = (EaseChatInputMenu) findViewById(R.id.input_menu);
        registerExtendMenuItem();
        // init input menu
        inputMenu.init(null);
        inputMenu.setChatInputMenuListener(new EaseChatInputMenu.ChatInputMenuListener() {

            @Override
            public void onSendMessage(String content) {
                Log.e("onSendMessage", content);
                /**
                 * 发送文字和表情
                 */
                sendTextMessage(content);
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                return voiceRecorderView.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderView.EaseVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        /**
                         * 录制语音完成
                         */
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

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {
                Log.e("onBigExpressionClicked", emojicon.getEmojiText());
                //  sendBigExpressionMessage(emojicon.getName(), emojicon.getIdentityCode());
            }
        });

        inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        adapter = new EaseMessageAdapter(this, voices);
        message_list.setAdapter(adapter);
        adapter.setOnItemClickLister(new EaseMessageAdapter.onItemClickLister() {
            @Override
            public void onItemClick(ImageView imageView, String path, int position) {
                //播放语音
                new EaseChatRowVoicePlayClickListener(imageView, path, MainActivity.this,
                        adapter).playVoice(path);
            }
        });

    }


    private void sendTextMessage(String content) {
        MessageBean bean = new MessageBean();
        bean.path = "";
        bean.msg = content;
        bean.second = 0;
        bean.time = TimeUtils.getCurrentTimeInLong();
        voices.add(bean);
        adapter.notifyDataSetChanged();
    }


    /**
     * register extend menu, item id need > 3 if you override this method and keep exist item
     */
    protected void registerExtendMenuItem() {
        for (int i = 0; i < itemStrings.length; i++) {
            inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], extendMenuItemClickListener);
        }
    }


    /**
     * handle the click event for extend menu
     */
    class MyItemClickListener implements EaseChatExtendMenu.EaseChatExtendMenuItemClickListener {

        @Override
        public void onClick(int itemId, View view) {
          /*  if(chatFragmentHelper != null){
                if(chatFragmentHelper.onExtendMenuItemClick(itemId, view)){
                    return;
                }
            }*/
            switch (itemId) {
                case ITEM_TAKE_PICTURE:
                    //  selectPicFromCamera();
                    break;
                case ITEM_PICTURE:
                    //   selectPicFromLocal();
                    break;
                case ITEM_LOCATION:
                    //  startActivityForResult(new Intent(getActivity(), EaseBaiduMapActivity.class), REQUEST_CODE_MAP);
                    break;

                default:
                    break;
            }
        }

    }
}
