package com.lanou.baidumusicdemo.main;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.ktv.KtvFragment;
import com.lanou.baidumusicdemo.live.LiveFragment;
import com.lanou.baidumusicdemo.loginregisterwelcome.UserActivity;
import com.lanou.baidumusicdemo.mine.MineFragment;
import com.lanou.baidumusicdemo.musiclibrary.MusicLibraryFragment;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dllo on 16/6/21.
 */
public class MainFragment extends BaseFragment implements View.OnClickListener {

    private TabLayout mainTl;
    private ViewPager mainVp;
    private ArrayList<Fragment> fragments;
    private MainVpAdapter adapter;
    private ImageView search, user;

    @Override
    public int setLayout() {
        return R.layout.fragment_main;
    }

    @Override
    public void initView(View view) {
        mainTl = (TabLayout) view.findViewById(R.id.main_tab_layout);
        mainVp = (ViewPager) view.findViewById(R.id.main_view_pager);
        search = (ImageView) view.findViewById(R.id.main_search_btn);
        user = (ImageView) view.findViewById(R.id.main_login);
    }

    @Override
    public void initData() {
        adapter = new MainVpAdapter(getFragmentManager());
        fragments = new ArrayList<>();
        fragments.add(new MineFragment());
        fragments.add(new MusicLibraryFragment());
//        fragments.add(new KtvFragment());
//        fragments.add(new LiveFragment());
        adapter.setFragments(fragments);
        mainVp.setAdapter(adapter);
        mainTl.setupWithViewPager(mainVp);
        mainTl.setSelectedTabIndicatorHeight(0);

        mainVp.setCurrentItem(1);
//        mainTl.setBackgroundColor(Color.BLUE);
        mainTl.setTabTextColors(Color.LTGRAY,Color.WHITE);

        search.setOnClickListener(this);
        user.setOnClickListener(this);

        login();

    }


    // 登录
    public void login(){
        BmobUser bmobUser = BmobUser.getCurrentUser(context);
        if (bmobUser != null){
            bmobUser.login(context, new SaveListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 搜索
            case R.id.main_search_btn:
                ((ClickToSearch)getActivity()).toSearch();
                break;
            // 用户
            case R.id.main_login:
                Intent intent = new Intent(context, UserActivity.class);
                startActivity(intent);
                break;
        }
    }
}
