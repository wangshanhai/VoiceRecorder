package com.ilike.voicerecorder.utils;

public final class VoiceProvider {
    private static final String TAG = VoiceProvider.class.getSimpleName();

    /**
     * the global EaseUI instance
     */
    private static VoiceProvider instance = null;

    private SettingsProvider settingsProvider;


    private VoiceProvider() {
    }

    /**
     * get instance of EaseUI
     *
     * @return
     */
    public synchronized static VoiceProvider getInstance() {
        if (instance == null) {
            instance = new VoiceProvider();
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
