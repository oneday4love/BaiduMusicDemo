package com.lanou.baidumusicdemo.musiclibrary;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.musiclibrary.mv.MVFragment;
import com.lanou.baidumusicdemo.musiclibrary.radio.RadioFragment;
import com.lanou.baidumusicdemo.musiclibrary.rank.RankFragment;
import com.lanou.baidumusicdemo.musiclibrary.recommend.RecommendFragment;
import com.lanou.baidumusicdemo.musiclibrary.songlist.SongListFragment;

import java.util.ArrayList;

/**
 * Created by dllo on 16/6/17.
 */
public class MusicLibraryFragment extends BaseFragment {

    private ViewPager musicLibViewPager;
    private TabLayout musicLibTabLayout;
    private MusicLibViewPagerAdapter adapter;
    private ArrayList<Fragment> fragments;

    @Override
    public int setLayout() {
        return R.layout.fragment_music_library;
    }

    @Override
    public void initView(View view) {
        musicLibTabLayout = (TabLayout) view.findViewById(R.id.music_lib_tab_layout);
        musicLibViewPager = (ViewPager) view.findViewById(R.id.music_lib_view_pager);
    }

    @Override
    public void initData() {
        adapter = new MusicLibViewPagerAdapter(getChildFragmentManager());
        initFragments();
        adapter.setFragments(fragments);
        musicLibViewPager.setAdapter(adapter);
        musicLibTabLayout.setupWithViewPager(musicLibViewPager);
        musicLibTabLayout.setTabTextColors(Color.GRAY, Color.rgb(0,191,225));
        musicLibTabLayout.setSelectedTabIndicatorColor(Color.rgb(0,191,225));


    }

    private void initFragments() {
        fragments = new ArrayList<>();
        fragments.add(new RecommendFragment());
        fragments.add(new RankFragment());
        fragments.add(new SongListFragment());
        fragments.add(new RadioFragment());
        fragments.add(new MVFragment());
    }
}
