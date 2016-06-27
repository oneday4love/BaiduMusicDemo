package com.lanou.baidumusicdemo.musiclibrary.songlist.songlistdetail;

/**
 * Created by dllo on 16/6/24.
 */
public class ListIdEvent {

    private String listId;

    public ListIdEvent(String listId) {
        this.listId = listId;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }
}
