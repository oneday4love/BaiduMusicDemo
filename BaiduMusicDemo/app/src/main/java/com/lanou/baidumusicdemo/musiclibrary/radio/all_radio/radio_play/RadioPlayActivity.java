package com.lanou.baidumusicdemo.musiclibrary.radio.all_radio.radio_play;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseActivity;
import com.lanou.baidumusicdemo.service.SongIdEvent;
import com.lanou.baidumusicdemo.volley.GsonRequest;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by dllo on 16/6/25.
 */
public class RadioPlayActivity extends BaseActivity {

    private TextView title;

    private RadioPlayBean bean;
    private ViewPager viewPager;
    private RadioPlayVpAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_play);
        title = (TextView) findViewById(R.id.radio_play_scene_tv);


        viewPager = (ViewPager) findViewById(R.id.radio_play_vp);
        adapter = new RadioPlayVpAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);


        String sceneId = getIntent().getStringExtra("sceneId");
        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.song.getSmartSongList&page_no=1&page_size=50&scene_id=" + sceneId + "&item_id=0&version=5.2.5&from=ios&channel=appstore";
        GsonRequest<RadioPlayBean> gsonRequest = new GsonRequest<RadioPlayBean>(url, RadioPlayBean.class, new Response.Listener<RadioPlayBean>() {
            @Override
            public void onResponse(RadioPlayBean response) {
                bean = response;
                // 进入播放第一首
                EventBus.getDefault().post(new SongIdEvent(bean.getResult().getSonglist().get(0).getSong_id()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueueSingleton.getInstance(this).getRequestQueue().add(gsonRequest);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 滑动切歌
                String songId = bean.getResult().getSonglist().get(position).getSong_id();
                EventBus.getDefault().post(new SongIdEvent(songId));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 设置scene_name
        title.setText(getIntent().getStringExtra("sceneName"));


    }

}
