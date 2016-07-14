package com.lanou.baidumusicdemo.service;

/**
 * Created by dllo on 16/7/9.
 */
public class DelayEvent {
    private int delayTime;

    public DelayEvent() {
    }

    public DelayEvent(int delayTime) {
        this.delayTime = delayTime;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }
}
