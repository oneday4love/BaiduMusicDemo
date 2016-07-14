package com.lanou.baidumusicdemo.mine.download;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.lanou.baidumusicdemo.MyApp;
import com.lanou.baidumusicdemo.base.UrlValues;
import com.lanou.baidumusicdemo.db.LiteOrmSingle;
import com.lanou.baidumusicdemo.mine.local.ScanMusic;
import com.lanou.baidumusicdemo.service.SongPlayBean;
import com.lanou.baidumusicdemo.volley.GsonRequest;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;
import com.litesuits.orm.db.assit.QueryBuilder;

/**
 * Created by dllo on 16/7/7.
 */
public class DownloadSingle {
    private static DownloadSingle singleDownload;

    private DownloadManager downloadManager;


    private DownloadSingle() {
        downloadManager = (DownloadManager) MyApp.context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public static DownloadSingle getSingleDownload() {
        if (singleDownload == null) {
            synchronized (DownloadSingle.class) {
                if (singleDownload == null) {
                    singleDownload = new DownloadSingle();
                }
            }
        }
        return singleDownload;
    }

    public void DownLoad(String songId) {
        String url = UrlValues.SONG_PLAY_FRONT_URL + songId + UrlValues.SONG_PLAY_BEH_URL;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String string = s.substring(1, s.length() - 2);
                Gson gson = new Gson();
                SongPlayBean songPlayBean = gson.fromJson(string, SongPlayBean.class);
                String downUrl = songPlayBean.getBitrate().getFile_link();
                String songId = songPlayBean.getSonginfo().getSong_id();
                String title = songPlayBean.getSonginfo().getTitle();
                String author = songPlayBean.getSonginfo().getAuthor();
                QueryBuilder<DownloadBean> queryBuilder = new QueryBuilder<>(DownloadBean.class);
                queryBuilder.whereEquals("title", title);
                if (LiteOrmSingle.getInstance().getLiteOrm().query(queryBuilder).size() != 0){
                    Toast.makeText(MyApp.context, "已经下载过该歌曲,请勿重复下载", Toast.LENGTH_SHORT).show();
                } else {
                    // 开始下载
                    Uri resource = Uri.parse(downUrl);
                    DownloadManager.Request request = new DownloadManager.Request(resource);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                    request.setAllowedOverRoaming(false);
                    // 设置文件类型
                    MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                    String mimiString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(downUrl));
                    request.setMimeType(mimiString);
                    //在通知栏中显示
                    request.setShowRunningNotification(true);
                    request.setVisibleInDownloadsUi(true);
                    //sdcard的目录下的download文件夹
                    request.setDestinationInExternalPublicDir("/music/mp3", title + ".mp3");
                    long id = downloadManager.enqueue(request);
                    LiteOrmSingle.getInstance().getLiteOrm().insert(new DownloadBean(title,author,songId));}

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        RequestQueueSingleton.getInstance(MyApp.context).getRequestQueue().add(stringRequest);
    }
}
//        GsonRequest<SongPlayBean> gsonRequest = new GsonRequest<SongPlayBean>(url, SongPlayBean.class, new Response.Listener<SongPlayBean>() {
//            @Override
//            public void onResponse(SongPlayBean songPlayBean) {
//                String downUrl = songPlayBean.getBitrate().getFile_link();
//                String songId = songPlayBean.getSonginfo().getSong_id();
//                String title = songPlayBean.getSonginfo().getTitle();
//                String author = songPlayBean.getSonginfo().getAuthor();
//                QueryBuilder<DownloadBean> queryBuilder = new QueryBuilder<>(DownloadBean.class);
//                queryBuilder.whereEquals("title", title);
//                if (LiteOrmSingle.getInstance().getLiteOrm().query(queryBuilder).size() != 0){
//                    Toast.makeText(MyApp.context, "已经下载过该歌曲,请勿重复下载", Toast.LENGTH_SHORT).show();
//                } else {
//                    // 开始下载
//                    Uri resource = Uri.parse(downUrl);
//                    DownloadManager.Request request = new DownloadManager.Request(resource);
//                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
//                    request.setAllowedOverRoaming(false);
//                    // 设置文件类型
//                    MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//                    String mimiString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(downUrl));
//                    request.setMimeType(mimiString);
//                    //在通知栏中显示
//                    request.setShowRunningNotification(true);
//                    request.setVisibleInDownloadsUi(true);
//                    //sdcard的目录下的download文件夹
//                    request.setDestinationInExternalPublicDir("/music/mp3", songId + ".mp3");
//                    long id = downloadManager.enqueue(request);
//                    LiteOrmSingle.getInstance().getLiteOrm().insert(new DownloadBean(title,author,songId));
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        });



//        MyVolley.getMyVolley().initGson(url, new MyVolley.GetGsonListener<SongPlayData>() {
//            @Override
//            public void getGsonListener(SongPlayData data) {
//                String url = data.getBitrate().getFile_link();
//                String songId = data.getSonginfo().getSong_id();
//                String title = data.getSonginfo().getTitle();
//                if (SingleLiteOrm.getInstance().getLiteOrm().query(new QueryBuilder<DownLoadSongId>(DownLoadSongId.class).where(DownLoadSongId.SONG_ID + " LIKE ?", new String[]{songId})).size() != 0) {
//                    Toast.makeText(MyApp.context, "已经下载过该歌曲,请勿重复下载", Toast.LENGTH_SHORT).show();
//                } else {
//                    //开始下载
//                    Uri resource = Uri.parse(url);
//                    DownloadManager.Request request = new DownloadManager.Request(resource);
//                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
//                    request.setAllowedOverRoaming(false);
//                    //设置文件类型
//                    MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//                    String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
//                    request.setMimeType(mimeString);
//                    //在通知栏中显示
//                    request.setShowRunningNotification(true);
//                    request.setVisibleInDownloadsUi(true);
//                    //sdcard的目录下的download文件夹
//                    request.setDestinationInExternalPublicDir("/music/mp3", songId + ".mp3");
//                    long id = downloadManager.enqueue(request);
//                    SingleLiteOrm.getInstance().getLiteOrm().insert(new DownLoadSongId(songId, title));
//                }
//            }
//        }, SongPlayData.class);


