package com.lanou.baidumusicdemo.musiclibrary.radio.all_radio.radio_play;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseActivity;
import com.lanou.baidumusicdemo.base.UrlValues;
import com.lanou.baidumusicdemo.musiclibrary.radio.all_radio.AllRadioActivity;
import com.lanou.baidumusicdemo.service.SongIdEvent;
import com.lanou.baidumusicdemo.volley.GsonRequest;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by dllo on 16/6/25.
 */
public class RadioPlayActivity extends BaseActivity implements View.OnClickListener {

    private TextView sceneTv;
    private ImageView returnBtn;

    private RadioPlayBean bean;
    private ViewPager viewPager;
    private RadioPlayVpAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_play);
        sceneTv = (TextView) findViewById(R.id.radio_play_scene_tv);
        returnBtn = (ImageView) findViewById(R.id.radio_play_pull_down_btn);
        returnBtn.setOnClickListener(this);
        sceneTv.setOnClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.radio_play_vp);
        adapter = new RadioPlayVpAdapter(this);
        viewPager.setAdapter(adapter);


        String sceneId = getIntent().getStringExtra("sceneId");
        String url = UrlValues.RADIO_PLAY_FRONT_URL + sceneId + UrlValues.RADIO_PLAY_BEHIND_URL;
        GsonRequest<RadioPlayBean> gsonRequest = new GsonRequest<RadioPlayBean>(url, RadioPlayBean.class, new Response.Listener<RadioPlayBean>() {
            @Override
            public void onResponse(RadioPlayBean response) {
                bean = response;
                // 进入播放第一首
                adapter.setBean(bean);
                adapter.setPos(0);
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
                adapter.setPos(position);
                String songId = bean.getResult().getSonglist().get(position).getSong_id();
                EventBus.getDefault().post(new SongIdEvent(songId));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 设置scene_name
        sceneTv.setText(getIntent().getStringExtra("sceneName"));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 返回
            case R.id.radio_play_pull_down_btn:
                finish();
                break;
            case R.id.radio_play_scene_tv:
                Intent intent = new Intent(this, AllRadioActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
