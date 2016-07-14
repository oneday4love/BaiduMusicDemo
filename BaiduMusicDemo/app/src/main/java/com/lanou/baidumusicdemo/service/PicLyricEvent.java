package com.lanou.baidumusicdemo.service;

/**
 * Created by dllo on 16/7/2.
 */
public class PicLyricEvent {
    private String pic;
    private String lyric;

    public PicLyricEvent(String pic, String lyric) {
        this.pic = pic;
        this.lyric = lyric;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }
}
