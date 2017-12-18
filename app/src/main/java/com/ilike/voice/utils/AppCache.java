package com.ilike.voice.utils;

import android.app.Application;
import android.content.Context;

import com.ilike.voice.service.PlayService;

/**
 * desc   :
 * author : wangshanhai
 * email  : ilikeshatang@gmail.com
 * date   : 2017/11/3 12:38
 */
public class AppCache {

    private Context mContext;
    private PlayService mPlayService;

    private AppCache() {
    }

    private static class SingletonHolder {
        private static AppCache sAppCache = new AppCache();
    }

    private static AppCache getInstance() {
        return SingletonHolder.sAppCache;
    }

    public static void initAppCache() {
        getContext();
    }

    public static void init(Application application) {
        getInstance().onInit(application);
    }

    private void onInit(Application application) {
        mContext = application.getApplicationContext();
    }

    public static Context getContext() {
        return getInstance().mContext;
    }

    public static PlayService getPlayService() {
        return getInstance().mPlayService;
    }

    public static void setPlayService(PlayService service) {
        getInstance().mPlayService = service;
    }


}
