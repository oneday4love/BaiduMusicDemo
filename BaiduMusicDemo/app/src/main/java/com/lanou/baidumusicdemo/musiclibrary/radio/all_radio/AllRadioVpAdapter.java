package com.lanou.baidumusicdemo.musiclibrary.radio.all_radio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by dllo on 16/6/24.
 */
public class AllRadioVpAdapter extends FragmentPagerAdapter {



    private String[] titles = {"活动","主题","天气","心情","语言","年代","曲风"};



    public AllRadioVpAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        AllRadioFragment fragment = new AllRadioFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id",position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
