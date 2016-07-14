package com.lanou.baidumusicdemo.db;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;
import com.litesuits.orm.db.model.ColumnsValue;

/**
 * Created by dllo on 16/7/2.
 */
public class RecentPlay {
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;
    @Column("songId")
    private String songId;
    @Column("title")
    private String title;
    private String author;

    public RecentPlay() {
    }

    public RecentPlay(String songId, String title, String author) {
        this.songId = songId;
        this.title = title;
        this.author = author;
    }

    public RecentPlay(int id, String songId, String title, String author) {
        this.id = id;
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
