package com.lanou.baidumusicdemo.service;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dllo on 16/6/24.
 */
public class PlayListManager {

    private HashMap<String,SongPlayBean> playList;
    private static PlayListManager playListManager;

    public HashMap<String, SongPlayBean> getPlayList() {
        return playList;
    }

    private PlayListManager() {
        playList = new HashMap<>();
    }

    public static PlayListManager getInstance(){
        if (playListManager == null){
            synchronized (PlayListManager.class){
                if (playListManager == null){
                    playListManager = new PlayListManager();
                }
            }
        }
        return playListManager;
    }

    // 添加一个
    public void addSong(SongPlayBean songPlayBean){
        String songId = songPlayBean.getSonginfo().getSong_id();
        if (!playList.keySet().contains(songId)){
            playList.put(songId, songPlayBean);
        }
    }

    // 移除一个
    public void removeSong(SongPlayBean songPlayBean){
        playList.remove(songPlayBean.getSonginfo().getSong_id());
    }

    // 移除全部
    public void removeAll(){
        playList.clear();
    }
}
