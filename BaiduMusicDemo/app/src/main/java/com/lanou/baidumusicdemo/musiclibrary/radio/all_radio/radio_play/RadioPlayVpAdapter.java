package com.lanou.baidumusicdemo.musiclibrary.radio.all_radio.radio_play;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.Image;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.lanou.baidumusicdemo.MyApp;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.service.MediaPlayService;
import com.lanou.baidumusicdemo.service.PlayPauseEvent;
import com.lanou.baidumusicdemo.service.ProgressEvent;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dllo on 16/6/25.
 */
public class RadioPlayVpAdapter extends PagerAdapter {

    private ImageView playBtn;
    private TextView countDownTime;

    private RadioPlayBean bean;
    private Context context;
    private ServiceConnection connection;
    private MediaPlayService.PlayBinder playBinder;

    private int pos;
    private ArrayList<View> views;

    public void setPos(int pos) {
        this.pos = pos;
    }

    public RadioPlayVpAdapter(Context context) {
        EventBus.getDefault().register(this);
        this.context = context;
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                playBinder = (MediaPlayService.PlayBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent = new Intent(context, MediaPlayService.class);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void setBean(RadioPlayBean bean) {
        this.bean = bean;
        views = new ArrayList<>();
        for (RadioPlayBean.ResultBean.SonglistBean songlistBean : bean.getResult().getSonglist()) {
            views.add(null);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return bean == null ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_radio_play, container, false);
        ImageView cover = (ImageView) view.findViewById(R.id.radio_play_cover);
        TextView title = (TextView) view.findViewById(R.id.radio_play_title);
        TextView author = (TextView) view.findViewById(R.id.radio_play_author);
//        countDownTime = (TextView) view.findViewById(R.id.radio_play_countdown_time);
        playBtn = (ImageView) view.findViewById(R.id.radio_play_pause_play_btn);

        title.setText(bean.getResult().getSonglist().get(position).getTitle());
        author.setText(bean.getResult().getSonglist().get(position).getAuthor());
        RequestQueueSingleton.getInstance(context).getImageLoader().get(bean.getResult().getSonglist().get(position).getPic_premium(),
                ImageLoader.getImageListener(cover, R.mipmap.radio_play_default_img, R.mipmap.radio_play_default_img));

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playBinder.playPause();
            }
        });

        view.setPadding(30, 30 ,0 ,0);
        views.set(position, view);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (container.getChildAt(position) == object) {
            container.removeViewAt(position);
        }
    }

    // 设置播放暂停
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setPlayBtn(PlayPauseEvent playPauseEvent){
        playBtn = (ImageView) views.get(pos).findViewById(R.id.radio_play_pause_play_btn);
        if (playPauseEvent.isPlaying()){
            playBtn.setImageResource(R.mipmap.radio_play_pause);
        } else {
            playBtn.setImageResource(R.mipmap.radio_play_play);
        }
    }

    // 设置倒计时
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setCountDownTime(ProgressEvent progressEvent){
        countDownTime = (TextView) views.get(pos).findViewById(R.id.radio_play_countdown_time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        int count = (int) (playBinder.getDuration() - progressEvent.getProgress());
        countDownTime.setText("-" + simpleDateFormat.format(new Date(count)));
    }

}
