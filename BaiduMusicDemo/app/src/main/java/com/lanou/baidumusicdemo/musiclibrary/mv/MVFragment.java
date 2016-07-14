package com.lanou.baidumusicdemo.musiclibrary.mv;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.base.UrlValues;

/**
 * Created by dllo on 16/6/17.
 */
public class MVFragment extends BaseFragment implements View.OnClickListener {

    private RadioButton newestRb, hotestRb;
    private FrameLayout mvReplace;

    private String newUrl = UrlValues.MV_NEW_URL;
    private String hotUrl = UrlValues.MV_HOT_URL;

    @Override
    public int setLayout() {
        return R.layout.fragment_mv;
    }

    @Override
    public void initView(View view) {
        newestRb = (RadioButton) view.findViewById(R.id.mv_newest_rb);
        hotestRb = (RadioButton) view.findViewById(R.id.mv_hotest_rb);
        mvReplace = (FrameLayout) view.findViewById(R.id.mv_replace_layout);
    }

    @Override
    public void initData() {
        newestRb.setOnClickListener(this);
        hotestRb.setOnClickListener(this);

        newestRb.setTextColor(Color.GREEN);
        hotestRb.setTextColor(Color.LTGRAY);

        NewHotFragment newHotFragment = new NewHotFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", newUrl);
        newHotFragment.setArguments(bundle);
        getChildFragmentManager().beginTransaction().replace(R.id.mv_replace_layout, newHotFragment).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 最新
            case R.id.mv_newest_rb:
                newestRb.setTextColor(Color.GREEN);
                hotestRb.setTextColor(Color.LTGRAY);
                NewHotFragment newHotFragment = new NewHotFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", newUrl);
                newHotFragment.setArguments(bundle);
                getChildFragmentManager().beginTransaction().replace(R.id.mv_replace_layout, newHotFragment).commit();
                break;
            // 最热
            case R.id.mv_hotest_rb:
                newestRb.setTextColor(Color.LTGRAY);
                hotestRb.setTextColor(Color.GREEN);
                NewHotFragment newHotFragment1 = new NewHotFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("url", hotUrl);
                newHotFragment1.setArguments(bundle1);
                getChildFragmentManager().beginTransaction().replace(R.id.mv_replace_layout, newHotFragment1).commit();
                break;
        }
    }
}
