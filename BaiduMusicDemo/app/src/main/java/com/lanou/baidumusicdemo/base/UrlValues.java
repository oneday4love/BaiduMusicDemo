package com.lanou.baidumusicdemo.base;

/**
 * Created by dllo on 16/6/27.
 */
public class UrlValues {

    // mv Url
    public static final String MV_NEW_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.8.0.1&channel=xiaomi&operator=2&provider=11%2C12&method=baidu.ting.mv.searchMV&format=json&order=1&page_num=1&page_size=20&query=全部";
    public static final String MV_HOT_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.8.0.1&channel=xiaomi&operator=2&provider=11%2C12&method=baidu.ting.mv.searchMV&format=json&order=0&page_num=1&page_size=20&query=%E5%85%A8%E9%83%A8";
    public static final String MV_PLAY_FRONT_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.7.3.0&channel=xiaomi&operator=3&provider=11%2C12&method=baidu.ting.mv.playMV&format=json&mv_id=";
    public static final String MV_PLAY_BEHIND_URL = "&song_id=&definition=0";

    // 电台url
    public static final String RADIO_ALL_FRONT_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting/?method=baidu.ting.scene.getCategoryScene&category_id=";
    public static final String RADIO_ALL_BEHIND_URL = "&version=5.2.5&from=ios&channel=appstore";
    public static final String RADIO_PLAY_FRONT_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.song.getSmartSongList&page_no=1&page_size=50&scene_id=";
    public static final String RADIO_PLAY_BEHIND_URL = "&item_id=0&version=5.2.5&from=ios&channel=appstore";

    // 排行url
    public static final String RANK_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.billboard.billCategory&format=json&from=ios&version=5.2.1&from=ios&channel=appstore";
    public static final String RANK_DETAIL_FRONT_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.billboard.billList&type=";
    public static final String RANK_DETAIL_BEHIND_URL = "&format=json&offset=0&size=100&from=ios&fields=title,song_id,author,resource_type,havehigh,is_new,has_mv_mobile,album_title,ting_uid,album_id,charge,all_rate&version=5.2.1&from=ios&channel=appstore";

    // 推荐url
    public static final String REC_VP_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.plaza.getFocusPic&format=json&from=ios&version=5.2.3&from=ios&channel=appstore";
    public static final String REC_SONG_LIST_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.diy.getHotGeDanAndOfficial&num=6&version=5.2.3&from=ios&channel=appstore";

    // 歌单url
    public static final String SONG_LIST_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.diy.gedan&page_no=1&page_size=30&from=ios&version=5.2.3&from=ios&channel=appstore";
    public static final String SONG_LIST_MORE_FRONT_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.diy.gedan&page_no=";
    public static final String SONG_LIST_MORE_BEHIND_URL = "&page_size=30&from=ios&version=5.2.3&from=ios&channel=appstore";
    public static final String SONG_LIST_DETAIL_FRONT_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.diy.gedanInfo&from=ios&listid=";
    public static final String SONG_LIST_DETAIL_BEHIND_URL = "&version=5.2.3&from=ios&channel=appstore";

    // 播放歌曲url
    public static final String SONG_PLAY_FRONT_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=webapp_music&method=baidu.ting.song.play&format=json&callback=&songid=";
    public static final String SONG_PLAY_BEH_URL = "&_=1413017198449";

    // 歌手详情
    public static final String AUTHOR_DETAIL_FRONT_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getSongList&format=json&tinguid=";
    public static final String AUTHOR_DETAIL_BEHIND_URL = "&artistid(null)&limits=30&order=2&offset=0&version=5.2.5&from=ios&channel=appstore";

    // 热门歌手url
    public static final String HOT_AUTHOR_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=12&offset=0&area=0&sex=0&abc=&from=ios&version=5.2.1&from=ios&channel=appstore";

    //华语男歌手url
    public static final String CHN_MALE_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=6&sex=1&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";

    //华语女歌手url
    public static final String CHN_FEMALE_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=6&sex=2&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";

    //华语组合url
    public static final String CHN_GROUP_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=6&sex=3&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";


    //欧美男歌手url
    public static final String ENG_MALE_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=3&sex=1&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";

    //欧美女歌手url
    public static final String ENG_FEMALE_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=3&sex=2&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";

    //欧美组合url
    public static final String ENG_GROUP_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=3&sex=3&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";

    // 韩国男歌手url
    public static final String KRN_MALE_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=7&sex=1&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";

    //韩国女歌手url
    public static final String KRN_FEMALE_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=7&sex=2&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";

    //韩国组合url
    public static final String KRN_GROUP_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=7&sex=3&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";

    //日本男歌手url
    public static final String JPN_MALE_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=60&sex=1&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";

    //日本女歌手url
    public static final String JPN_FEMALE_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=60&sex=2&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";

    //日本组合url
    public static final String JPN_GROUP_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=60&sex=3&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";

    //其他歌手url
    public static final String OTHER_AUTHOR = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=5&sex=4&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";
}
