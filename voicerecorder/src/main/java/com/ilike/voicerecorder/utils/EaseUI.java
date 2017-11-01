package com.ilike.voicerecorder.utils;

public final class EaseUI {
    private static final String TAG = EaseUI.class.getSimpleName();

    /**
     * the global EaseUI instance
     */
    private static EaseUI instance = null;



    private EaseSettingsProvider settingsProvider;


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

}
