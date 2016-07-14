package com.lanou.baidumusicdemo.service;

import com.lanou.baidumusicdemo.db.PlayList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dllo on 16/6/24.
 */
public class PlayListManager {

    private ArrayList<PlayList> playList;
    private static PlayListManager playListManager;

    public ArrayList<PlayList> getPlayList() {
        return playList;
    }

    private PlayListManager() {
        playList = new ArrayList<>();
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




}
