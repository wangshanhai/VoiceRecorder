package com.ilike.voicerecorder.utils;

public final class EaseUI {
    private static final String TAG = EaseUI.class.getSimpleName();

    /**
     * the global EaseUI instance
     */
    private static EaseUI instance = null;

    private SettingsProvider settingsProvider;


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


    public void setSettingsProvider(SettingsProvider settingsProvider){
        this.settingsProvider = settingsProvider;
    }

    public SettingsProvider getSettingsProvider(){
        if(settingsProvider == null){
            settingsProvider = new DefaultSettingsProvider();
        }

        return settingsProvider;
    }



    /**
     * new message options provider
     */
    public interface SettingsProvider {
        boolean isSpeakerOpened();
    }

    /**
     * default settings provider
     */
    protected class DefaultSettingsProvider implements SettingsProvider {


        @Override
        public boolean isSpeakerOpened() {
            return true;
        }
    }

}
