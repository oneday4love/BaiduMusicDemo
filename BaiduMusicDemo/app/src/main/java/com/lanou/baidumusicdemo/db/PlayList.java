package com.lanou.baidumusicdemo.db;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by dllo on 16/6/29.
 */
public class PlayList {
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;
    private String songId;
    private String title;
    private String author;

    public PlayList() {
    }

    public PlayList(String songId, String title, String author) {
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
}
