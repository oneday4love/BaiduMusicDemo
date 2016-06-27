package com.lanou.baidumusicdemo.musiclibrary.radio.all_radio;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseActivity;
import com.lanou.baidumusicdemo.volley.GsonRequest;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

import java.util.ArrayList;

/**
 * Created by dllo on 16/6/24.
 */
public class AllRadioActivity extends BaseActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AllRadioVpAdapter adapter;
    private ImageView close;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_radio);
        tabLayout = (TabLayout) findViewById(R.id.all_radio_tab);
        viewPager = (ViewPager) findViewById(R.id.all_radio_vp);
        close = (ImageView) findViewById(R.id.all_radio_close);
        close.setOnClickListener(this);

        adapter = new AllRadioVpAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabTextColors(Color.GRAY, Color.WHITE);
        tabLayout.setSelectedTabIndicatorHeight(0);
    }


    @Override
    public void onClick(View v) {
        finish();
    }
}
