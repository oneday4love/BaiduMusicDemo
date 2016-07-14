package com.lanou.baidumusicdemo.playpage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanou.baidumusicdemo.playpage.lrc.LrcFragment;

/**
 * Created by dllo on 16/6/28.
 */
public class PlayPageVpAdapter extends FragmentPagerAdapter {



    public PlayPageVpAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        if (position == 0){
            PicFragment picFragment = new PicFragment();
            fragment = picFragment;
        } else {
            LrcFragment lrcFragment = new LrcFragment();
            fragment = lrcFragment;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
