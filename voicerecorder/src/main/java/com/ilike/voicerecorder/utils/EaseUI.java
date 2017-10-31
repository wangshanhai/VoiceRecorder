package com.ilike.voicerecorder.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class EaseUI {
    private static final String TAG = EaseUI.class.getSimpleName();

    /**
     * the global EaseUI instance
     */
    private static EaseUI instance = null;


    /**
     * application context
     */
    private Context appContext = null;

    /**
     * init flag: test if the sdk has been inited before, we don't need to init again
     */
    private boolean sdkInited = false;


    private EaseSettingsProvider settingsProvider;

    /**
     * save foreground Activity which registered eventlistener
     */
    private List<Activity> activityList = new ArrayList<Activity>();

    public void pushActivity(Activity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(0, activity);
        }
    }

    public void popActivity(Activity activity) {
        activityList.remove(activity);
    }


    private EaseUI() {
    }

    /**
     * get instance of EaseUI
     *
     * @return
     */
    public synchronized static EaseUI getInstance() {
        if (instance == null) {
            instance = new EaseUI();
        }
        return instance;
    }

    /**
     * this function will initialize the SDK and easeUI kit
     *
     * @param context
     * @param options use default if options is null
     * @return
     */
    public synchronized boolean init(Context context) {
        if (sdkInited) {
            return true;
        }
        appContext = context;

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);

        Log.d(TAG, "process app name : " + processAppName);

        // if there is application has remote service, application:onCreate() maybe called twice
        // this check is to make sure SDK will initialized only once
        // return if process name is not application's name since the package name is the default process name
        if (processAppName == null || !processAppName.equalsIgnoreCase(appContext.getPackageName())) {
            Log.e(TAG, "enter the service process!");
            return false;
        }

        if(settingsProvider == null){
            settingsProvider = new DefaultSettingsProvider();
        }

        sdkInited = true;
        return true;
    }


    /**
     * check the application process name if process name is not qualified, then we think it is a service process and we will not init SDK
     *
     * @param pID
     * @return
     */
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = appContext.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }




    public void setSettingsProvider(EaseSettingsProvider settingsProvider){
        this.settingsProvider = settingsProvider;
    }

    public EaseSettingsProvider getSettingsProvider(){
        if(settingsProvider == null){
            settingsProvider = new DefaultSettingsProvider();
        }

        return settingsProvider;
    }



    /**
     * new message options provider
     */
    public interface EaseSettingsProvider {
        boolean isSpeakerOpened();
    }

    /**
     * default settings provider
     */
    protected class DefaultSettingsProvider implements EaseSettingsProvider {


        @Override
        public boolean isSpeakerOpened() {
            return true;
        }
    }

    public Context getContext() {
        return appContext;
    }
}
