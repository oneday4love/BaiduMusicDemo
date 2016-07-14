package com.lanou.baidumusicdemo.service;

/**
 * Created by dllo on 16/7/2.
 */
public class PlayPauseEvent {
    private boolean isPlaying;

    public PlayPauseEvent(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
