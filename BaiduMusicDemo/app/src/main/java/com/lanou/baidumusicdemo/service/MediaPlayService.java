package com.lanou.baidumusicdemo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.lanou.baidumusicdemo.MyApp;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.UrlValues;
import com.lanou.baidumusicdemo.db.LiteOrmSingle;
import com.lanou.baidumusicdemo.db.PlayList;
import com.lanou.baidumusicdemo.db.RecentPlay;
import com.lanou.baidumusicdemo.main.MainActivity;
import com.lanou.baidumusicdemo.mine.local.LocalSongBean;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dllo on 16/6/21.
 */
public class MediaPlayService extends Service {

    private MediaPlayer player = new MediaPlayer();
    private PlayBinder playBinder = new PlayBinder();
    private int index;
    private int pattern;
    private long duration;
    private String playTime;
    private NotificationManager notificationManager;

    private SongPlayBean songPlayBean;

    private static final int LIST_LOOP = 0;
    private static final int SINGLE_LOOP = 1;
    private static final int RANDOM = 2;
    private static final int ORDER = 3;
    private boolean firstTime = true;

    private PrevReceiver prevReceiver;
    private PlayReceiver playReceiver;
    private NextReceiver nextReceiver;
    private CloseReceiver closeReceiver;


    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (PlayListManager.getInstance().getPlayList().size() > 0) {
                    playBinder.playNext(pattern);
                }
            }
        });
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                player.start();
                // 加入最近播放
                addToRecent();
                showNotificationCtrl(new PlayPauseEvent(player.isPlaying()));
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (player.isPlaying()) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
//                        int currentTime;
//                        if(!firstTime){
//                            currentTime = player.getCurrentPosition() + 15000;
//                        }else {
//                            currentTime = player.getCurrentPosition();
//                            if(15000 -currentTime < 1000){
//                                firstTime = false;
//                            }
//                        }
                        playTime = simpleDateFormat.format(new Date(player.getCurrentPosition()));
                        playBinder.setDuration(player.getDuration());
                        EventBus.getDefault().post(songPlayBean);
                    EventBus.getDefault().post(new ProgressEvent(player.getCurrentPosition(), playTime));
                    }
                    EventBus.getDefault().post(new PlayPauseEvent(player.isPlaying()));
                }
            }
        }).start();

        prevReceiver = new PrevReceiver();
        playReceiver = new PlayReceiver();
        nextReceiver = new NextReceiver();
        closeReceiver = new CloseReceiver();
        IntentFilter prevFilter = new IntentFilter(getPackageName() + ".PREV");
        IntentFilter playFilter = new IntentFilter(getPackageName() + ".PLAY");
        IntentFilter nextFilter = new IntentFilter(getPackageName() + ".NEXT");
        IntentFilter closeFilter = new IntentFilter(getPackageName() + ".CLOSE");
        registerReceiver(prevReceiver, prevFilter);
        registerReceiver(playReceiver, playFilter);
        registerReceiver(nextReceiver, nextFilter);
        registerReceiver(closeReceiver, closeFilter);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return playBinder;
    }

    public class PlayBinder extends Binder {
        // 播放下一首
        public void playNext(int pattern) {
//            firstTime = true;
            switch (pattern) {
                // 列表循环
                case LIST_LOOP:
                    index++;
                    if (index >= PlayListManager.getInstance().getPlayList().size()) {
                        index = 0;
                    }
                    playSelected(new SongIdEvent(PlayListManager.getInstance().getPlayList().get(index).getSongId()));
                    break;
                // 单曲循环
                case SINGLE_LOOP:
                    playSelected(new SongIdEvent(PlayListManager.getInstance().getPlayList().get(index).getSongId()));
                    break;
                // 随机播放
                case RANDOM:
                    Random random = new Random();
                    index = random.nextInt(PlayListManager.getInstance().getPlayList().size());
                    playSelected(new SongIdEvent(PlayListManager.getInstance().getPlayList().get(index).getSongId()));
                    break;
                // 顺序播放
                case ORDER:
                    index++;
                    if (index < PlayListManager.getInstance().getPlayList().size()) {
                        playSelected(new SongIdEvent(PlayListManager.getInstance().getPlayList().get(index).getSongId()));
                    } else {
                        player.stop();
                    }
                    break;
            }
        }

        // 播放暂停
        public void playPause() {
            if (player.isPlaying()) {
                player.pause();
            } else {
                player.start();
            }
        }

        // 播放上一首
        public void playPre() {
            if (index <= 0) {
                Toast.makeText(MyApp.context, "已经是第一首了", Toast.LENGTH_SHORT).show();
            } else {
                index--;
                playSelected(new SongIdEvent(PlayListManager.getInstance().getPlayList().get(index).getSongId()));
            }
        }

        public boolean isDelPlaying(int position) {
            return index == position;
        }

        public void stopPlay() {
            player.stop();
        }

        // 是否正在播放
        public boolean isPlaying() {
            return player.isPlaying();
        }

        // 播放模式
        public void setPattern(int mPattern) {
            pattern = mPattern;
        }

        public int getPattern() {
            return pattern;
        }

        // 歌曲时间
        public long getDuration() {
            return duration;
        }

        public void setDuration(long mDuration) {
            duration = mDuration;
        }

        // 播放时间
        public String getPlayTime() {
            return playTime;
        }

        // 进度
        public void setProgress(int progress) {
            player.seekTo(progress);
        }

        public void releaseRes() {
            player.release();
        }


        public int getIndex(){
            return index;
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(prevReceiver);
        unregisterReceiver(playReceiver);
        unregisterReceiver(nextReceiver);
        unregisterReceiver(closeReceiver);
    }


    @Subscribe
    public void initIndex(IndexEvent indexEvent) {
        index = indexEvent.getIndex();
    }

    // 播放点击的歌曲
    @Subscribe
    public void playSelected(SongIdEvent songIdEvent) {
        final String songId = songIdEvent.getSongId();
        String songUrl = UrlValues.SONG_PLAY_FRONT_URL + songId + UrlValues.SONG_PLAY_BEH_URL;
        StringRequest playRequest = new StringRequest(songUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String s = response.substring(1, response.length() - 2);
                Gson gson = new Gson();
                songPlayBean = gson.fromJson(s, SongPlayBean.class);

                player.reset();
                try {
                    player.setDataSource(songPlayBean.getBitrate().getFile_link());
                    player.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mp) {
//                        player.start();
//                        // 加入最近播放
//                        addToRecent();
//
//                    }
//                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueueSingleton.getInstance(this).getRequestQueue().add(playRequest);
    }


    // 播放本地歌曲
    @Subscribe
    public void playLocalMusic(LocalSongBean localSongBean) {
        Log.d("ddd", "dddddddddd");
        Log.d("ddd", localSongBean.getPath());
        player.reset();
        try {
            player.setDataSource(localSongBean.getPath());
            player.prepareAsync();
            Log.d("ddd", "ffffff");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 加入最近播放
    public void addToRecent() {
        if (LiteOrmSingle.getInstance().getLiteOrm().query(RecentPlay.class).size() == 0) {
            LiteOrmSingle.getInstance().getLiteOrm().insert(new RecentPlay(songPlayBean.getSonginfo().getSong_id(),
                    songPlayBean.getSonginfo().getTitle(), songPlayBean.getSonginfo().getAuthor()));
        } else {
            QueryBuilder<RecentPlay> queryBuilder = new QueryBuilder<>(RecentPlay.class);
            queryBuilder.whereEquals("title", songPlayBean.getSonginfo().getTitle());
            if (LiteOrmSingle.getInstance().getLiteOrm().query(queryBuilder).size() == 0) {
                LiteOrmSingle.getInstance().getLiteOrm().insert(new RecentPlay(songPlayBean.getSonginfo().getSong_id(),
                        songPlayBean.getSonginfo().getTitle(), songPlayBean.getSonginfo().getAuthor()));
            }
        }
        // 通知刷新
        Intent intent = new Intent("REFRESH_MINE");
        MyApp.context.sendBroadcast(intent);
    }

    // 延时退出
    @Subscribe
    public void delayExit(DelayEvent delayEvent){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (notificationManager != null) {
                    notificationManager.cancel(3001);
                }
                stopSelf();
                System.exit(0);
            }
        }, delayEvent.getDelayTime() * 60000);
    }

    // 显示通知栏
    @Subscribe
    public void showNotificationCtrl(PlayPauseEvent playPauseEvent) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);

        builder.setSmallIcon(R.mipmap.icon_logo);


        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.notification_song_name_tv, songPlayBean.getSonginfo().getTitle());
        remoteViews.setTextViewText(R.id.notification_singer_tv, songPlayBean.getSonginfo().getAuthor());

        if (playPauseEvent.isPlaying()){
            remoteViews.setImageViewResource(R.id.notification_play_btn, R.mipmap.bt_notificationbar_pause);
        } else {
            remoteViews.setImageViewResource(R.id.notification_play_btn, R.mipmap.bt_notificationbar_play);
        }

        // 上一曲
        Intent prevIntent = new Intent(getPackageName() + ".PREV");
        PendingIntent prevPending = PendingIntent.getBroadcast(this, 2001, prevIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_prev_btn, prevPending);
        // 播放/暂停
        Intent playIntent = new Intent(getPackageName() + ".PLAY");
        PendingIntent playPending = PendingIntent.getBroadcast(this, 2002, playIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_play_btn, playPending);
        // 下一曲
        Intent nextIntnet = new Intent(getPackageName() + ".NEXT");
        PendingIntent nextPending = PendingIntent.getBroadcast(this, 2003, nextIntnet, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_next_btn, nextPending);
        // 关闭
        Intent closeIntent = new Intent(getPackageName() + ".CLOSE");
        PendingIntent closePending = PendingIntent.getBroadcast(this, 2004, closeIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_close_btn, closePending);

        Intent toMainIntent = new Intent(this, MainActivity.class);
        PendingIntent toMainPending = PendingIntent.getActivity(this, 2005, toMainIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_cover_iv, toMainPending);

        builder.setContent(remoteViews);
        builder.setOngoing(true);
        Notification notification = builder.build();

        notificationManager.notify(3001, notification);
        Picasso.with(MyApp.context).load(songPlayBean.getSonginfo().getPic_big()).into(remoteViews, R.id.notification_cover_iv, 3001, notification);

    }


    // 上一曲
    class PrevReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            playBinder.playPre();
        }
    }

    // 播放暂停
    class PlayReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            playBinder.playPause();
        }
    }

    // 下一曲
    class NextReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            playBinder.playNext(pattern);
        }
    }

    // 关闭
    class CloseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (notificationManager != null) {
                notificationManager.cancel(3001);
            }
            stopSelf();
            LiteOrmSingle.getInstance().getLiteOrm().deleteAll(PlayList.class);
            LiteOrmSingle.getInstance().getLiteOrm().insert(PlayListManager.getInstance().getPlayList());
            System.exit(0);
        }
    }
}
