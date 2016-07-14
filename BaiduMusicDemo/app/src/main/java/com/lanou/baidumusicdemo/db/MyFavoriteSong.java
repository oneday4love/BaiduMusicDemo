package com.lanou.baidumusicdemo.db;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

import cn.bmob.v3.BmobObject;

/**
 * Created by dllo on 16/7/5.
 */
public class MyFavoriteSong extends BmobObject{
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;
    private String userName;
    private String songId;
    @Column("title")
    private String title;
    private String author;

    public MyFavoriteSong() {
    }

    public MyFavoriteSong(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public MyFavoriteSong(String songId, String title, String author) {
        this.songId = songId;
        this.title = title;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
