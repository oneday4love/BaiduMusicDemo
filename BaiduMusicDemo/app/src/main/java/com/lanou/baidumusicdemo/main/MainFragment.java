package com.lanou.baidumusicdemo.main;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.ktv.KtvFragment;
import com.lanou.baidumusicdemo.live.LiveFragment;
import com.lanou.baidumusicdemo.mine.MineFragment;
import com.lanou.baidumusicdemo.musiclibrary.MusicLibraryFragment;

import java.util.ArrayList;

/**
 * Created by dllo on 16/6/21.
 */
public class MainFragment extends BaseFragment {

    private TabLayout mainTl;
    private ViewPager mainVp;
    private ArrayList<Fragment> fragments;
    private MainVpAdapter adapter;

    @Override
    public int setLayout() {
        return R.layout.fragment_main;
    }

    @Override
    public void initView(View view) {
        mainTl = (TabLayout) view.findViewById(R.id.main_tab_layout);
        mainVp = (ViewPager) view.findViewById(R.id.main_view_pager);
    }

    @Override
    public void initData() {
        adapter = new MainVpAdapter(getFragmentManager());
        fragments = new ArrayList<>();
        fragments.add(new MineFragment());
        fragments.add(new MusicLibraryFragment());
        fragments.add(new KtvFragment());
        fragments.add(new LiveFragment());
        adapter.setFragments(fragments);
        mainVp.setAdapter(adapter);
        mainTl.setupWithViewPager(mainVp);
        mainTl.setSelectedTabIndicatorHeight(0);

        mainVp.setCurrentItem(1);
    }
}
