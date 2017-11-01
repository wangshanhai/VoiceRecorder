
## 介绍

这是一款参考环信的语音录制和播放库，轻量级，自带语音录制时动画效果，根据声音大小进行动画展示：
 
具体看图和gif动画效果：

![voicerecorder.gif](https://github.com/wangshanhai/VoiceRecorder/blob/master/image/voicerecorder2.gif)



### **使用步骤：**

## **注意，目前还在开发阶段，发现bug记得给我提issues哈**
## **注意，6.0以上系统需要运行时授权读取sd卡和音频**

#### 1.添加Jcenter仓库 Gradle依赖：
```java
compile 'com.ilike:voicerecorder:1.0.0'
```
或者

#### Maven
```
<dependency>
  <groupId>com.ilike</groupId>
  <artifactId>voicerecorder</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```
#### 2.在Activity中添加如下代码：

```java

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
