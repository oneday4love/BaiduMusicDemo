package com.lanou.baidumusicdemo.musiclibrary.recommend.detail;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;

/**
 * Created by dllo on 16/6/23.
 */
public class RecVpDetailFragment extends BaseFragment {

    private CollapsingToolbarLayout toolbarLayout;
    private RecyclerView recyclerView;

    @Override
    public int setLayout() {
        return R.layout.fragment_rec_vp_detail;
    }

    @Override
    public void initView(View view) {
        toolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.detail_rv);
    }

    @Override
    public void initData() {

    }
}
