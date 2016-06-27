package com.lanou.baidumusicdemo.service;

/**
 * Created by dllo on 16/6/24.
 */
public class SongIdEvent {

    private String songId;

    public SongIdEvent(String songId) {
        this.songId = songId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }
}
