package com.lanou.baidumusicdemo.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.lanou.baidumusicdemo.MyApp;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

/**
 * Created by dllo on 16/6/21.
 */
public class MediaPlayService extends Service {

    private MediaPlayer player = new MediaPlayer();
    private PlayBinder playBinder = new PlayBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                player.start();
            }
        });

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return playBinder;
    }

    public class PlayBinder extends Binder{

        public void setSong(String url){
            try {
                player.reset();
                player.setDataSource(url);
                player.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    // 播放点击的歌曲
    @Subscribe
    public void playSelected(SongIdEvent songIdEvent){
        String songId = songIdEvent.getSongId();
        String songUrl = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=webapp_music&method=baidu.ting.song.play&format=json&callback=&songid="+songId+"&_=1413017198449";
        StringRequest playRequest = new StringRequest(songUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String s = response.substring(1,response.length()-2);
                Gson gson = new Gson();
                SongPlayBean songPlayBean = gson.fromJson(s, SongPlayBean.class);

                Log.d("MainActivity", "songPlayBean:" + songPlayBean.getBitrate().getFile_link());

                player.reset();
                try {
                    player.setDataSource(songPlayBean.getBitrate().getFile_link());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.prepareAsync();

                EventBus.getDefault().post(songPlayBean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueueSingleton.getInstance(this).getRequestQueue().add(playRequest);
    }
}
