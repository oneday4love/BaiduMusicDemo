package com.lanou.baidumusicdemo.musiclibrary.rank.rankdetail;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.service.SongIdEvent;
import com.lanou.baidumusicdemo.volley.GsonRequest;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by dllo on 16/6/21.
 */
public class RankDetailFragment extends BaseFragment {

    private RankDetailBean bean;
    private CollapsingToolbarLayout toolbarLayout;
    private RankDetailRvAdapter adapter;
    private RecyclerView recyclerView;
    private ImageView toolBarLayoutBg;

    @Override
    public int setLayout() {
        return R.layout.fragment_rank_detail;
    }

    @Override
    public void initView(View view) {
        toolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.detail_rv);
        toolBarLayoutBg = (ImageView) view.findViewById(R.id.rank_detail_toolbar_layout_bg);
    }

    @Override
    public void initData() {
        adapter = new RankDetailRvAdapter(context);
        int rankType = this.getArguments().getInt("rankType");
        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.billboard.billList&type=" + rankType + "&format=json&offset=0&size=100&from=ios&fields=title,song_id,author,resource_type,havehigh,is_new,has_mv_mobile,album_title,ting_uid,album_id,charge,all_rate&version=5.2.1&from=ios&channel=appstore";
        GsonRequest<RankDetailBean> gsonRequest = new GsonRequest<RankDetailBean>(url, RankDetailBean.class, new Response.Listener<RankDetailBean>() {
            @Override
            public void onResponse(RankDetailBean response) {
                adapter.setBean(response);
                bean = response;
                toolbarLayout.setTitle(response.getBillboard().getName());
                RequestQueueSingleton.getInstance(context).getImageLoader().get(response.getBillboard().getPic_s210(),
                        ImageLoader.getImageListener(toolBarLayoutBg, R.mipmap.default_album_topic_detail,R.mipmap.default_album_topic_detail));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueueSingleton.getInstance(context).getRequestQueue().add(gsonRequest);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // 点击
        adapter.setOnRankDetailRvListener(new RankDetailRvAdapter.OnRankDetailRvListener() {
            @Override
            public void onItemClick(int position) {
                String songId = bean.getSong_list().get(position).getSong_id();
                EventBus.getDefault().post(new SongIdEvent(songId));
            }
        });
    }
}
