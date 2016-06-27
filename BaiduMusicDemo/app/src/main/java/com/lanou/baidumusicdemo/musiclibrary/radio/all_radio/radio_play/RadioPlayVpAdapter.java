package com.lanou.baidumusicdemo.musiclibrary.radio.all_radio.radio_play;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by dllo on 16/6/25.
 */
public class RadioPlayVpAdapter extends FragmentStatePagerAdapter {



    public RadioPlayVpAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        RadioPlayFragment fragment = new RadioPlayFragment();
        return fragment;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
}
