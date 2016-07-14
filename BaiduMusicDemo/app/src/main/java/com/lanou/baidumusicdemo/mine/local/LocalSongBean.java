package com.lanou.baidumusicdemo.mine.local;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by dllo on 16/7/7.
 */
public class LocalSongBean {
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;
    @Column("title")
    private String title;
    private String author;
    private String path;

    public LocalSongBean() {
    }

    public LocalSongBean(String title, String author, String path) {
        this.title = title;
        this.author = author;
        this.path = path;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
