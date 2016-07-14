package com.lanou.baidumusicdemo.mine.favorite;

import com.lanou.baidumusicdemo.db.MyFavoriteSong;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dllo on 16/7/7.
 */
public class FavoriteManager {
    private static FavoriteManager favoriteManager;
    private ArrayList<MyFavoriteSong> favoriteSongs;

    private FavoriteManager(){
        favoriteSongs = new ArrayList<>();
    }

    public ArrayList<MyFavoriteSong> getFavoriteSongs() {
        return favoriteSongs;
    }

    public static FavoriteManager getInstance(){
        if (favoriteManager == null){
            synchronized (FavoriteManager.class){
                favoriteManager = new FavoriteManager();
            }
        }
        return favoriteManager;
    }
}
