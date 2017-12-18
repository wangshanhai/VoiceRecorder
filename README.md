
[![Bintray](https://img.shields.io/bintray/v/ilike/maven/voicerecorder.svg)](https://bintray.com/ilike/maven/voicerecorder)
[![API](https://img.shields.io/badge/API-9%2B-brightgreen.svg)](https://android-arsenal.com/api?level=9) 
[![download](https://img.shields.io/badge/downloadZip-v1.0.2-orange.svg)](https://github.com/wangshanhai/VoiceRecorder/archive/master.zip)
[![license](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)



## 介绍

这是一款参考环信的语音录制和播放库，轻量级，自带语音录制时动画效果，根据声音大小进行动画展示：
 
具体看图和gif动画效果：

![voicerecorder.gif](https://github.com/wangshanhai/VoiceRecorder/blob/master/image/voicerecorder2.gif)



### **使用步骤：**

## **注意，目前还在开发阶段，有bug记得提issues哈**
## **注意，6.0以上系统需要运行时授权读取sd卡和音频**

## **注意，需要权限** ##
```java

    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />

```

#### 1.添加Jcenter仓库 Gradle依赖：
```java

compile 'com.ilike:voicerecorder:1.0.2'

```
或者

#### Maven
```
<dependency>
  <groupId>com.ilike</groupId>
  <artifactId>voicerecorder</artifactId>
  <version>1.0.2</version>
  <type>pom</type>
</dependency>

```
#### 2.在Activity中添加如下代码：

```java
/**
* 设置文件存放目录，存放路径如：/Android/data/包名/chat/voice/
* 默认不设置，路径存放为：/Android/data/包名/chat/voice/
*/
PathUtil.getInstance().createDirs("chat", "voice", appContext);


```



```java
/**
* 自定义命名文件
* 默认不设置是用时间戳
*/
voiceRecorderView.setCustomNamingFile(true,"语音命名.mp3");

```

```java
/**
* 自定义语音录制过程中，声音大小的动画，默认使用库文件中的动画，
* 目前默认需要设置14张图片，以后更新自定义动画帧数
*/
voiceRecorderView.setDrawableAnimation(Drawable[] animationDrawable)

```

```java
/**
* 设置停止播放语音时，显示的静态icon
*/
VoicePlayClickListener.setStopPlayIcon(R.drawable.ease_chatto_voice_playing)

```

```java
/**
* 设置播放语音的帧动画，
*/
VoicePlayClickListener.setPlayingIconDrawableResoure(R.drawable.voice_to_icon)

-------------------------------------------------------
比如这样的动画:
<?xml version="1.0" encoding="utf-8"?>
   <animation-list xmlns:android="http://schemas.android.com/apk/res/android"
       android:oneshot="false">
       <item
           android:drawable="@drawable/ease_chatto_voice_playing_f1"
           android:duration="200" />
       <item
           android:drawable="@drawable/ease_chatto_voice_playing_f2"
           android:duration="200" />
       <item
           android:drawable="@drawable/ease_chatto_voice_playing_f3"
           android:duration="200" />
</animation-list>
-------------------------------------------------------

```


```java
/**
* 录制语音
*/
tvRecorder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return  voiceRecorderView.onPressToSpeakBtnTouch(v, event, new VoiceRecorderView.EaseVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        Log.e("voiceFilePath=", voiceFilePath + "  time = " + voiceTimeLength);
                        /**
                        *voiceFilePath 为录音文件存放在sd的路径
                        * voiceTimeLength 录音文件的时长
                        */
                    }
                });
            }
        });

```

```java

/**
* 播放SD卡本地语音路径
* imageView显示动画
* Context，上下文
* path ，语音路径
*/
该方法废掉
new VoicePlayClickListener(imageView, Context).playVoice(path);

这时候使用服务来播放音频更好
demo中是这样使用
 AppCache.getPlayService().play(path);

/**
* 播放网络语音路径
* imageView显示动画
* Context，上下文
* path ，语音路径
*/
new VoicePlayClickListener(imageView, Context).playUrlVoice(path);



```

#### 3.在布局文件xml中添加如下：
 
  ```xml
    
    
        <com.ilike.voicerecorder.widget.VoiceRecorderView
            android:id="@+id/voice_recorder"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            android:visibility="invisible" />
    
  ```
![recordingView.jpg](https://github.com/wangshanhai/VoiceRecorder/blob/master/image/recordingView.jpg)




## License

```
Copyright 2017 ilikeshatang
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```



