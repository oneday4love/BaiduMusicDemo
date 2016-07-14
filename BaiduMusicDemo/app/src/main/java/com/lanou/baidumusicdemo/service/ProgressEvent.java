package com.lanou.baidumusicdemo.service;

/**
 * Created by dllo on 16/7/1.
 */
public class ProgressEvent {
    private int progress;
    private String playTime;

    public ProgressEvent(int progress) {
        this.progress = progress;
    }

    public ProgressEvent(String playTime) {
        this.playTime = playTime;
    }

    public ProgressEvent(int progress, String playTime) {
        this.progress = progress;
        this.playTime = playTime;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }
}
