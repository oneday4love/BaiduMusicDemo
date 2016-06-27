package com.lanou.baidumusicdemo.musiclibrary.songlist.songlistdetail;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.service.SongIdEvent;
import com.lanou.baidumusicdemo.util.GetDrawable;
import com.lanou.baidumusicdemo.volley.GsonRequest;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by dllo on 16/6/23.
 */
public class SongListDetailFragment extends BaseFragment {

    private CollapsingToolbarLayout toolbarLayout;
    private RecyclerView recyclerView;
    private SongListDetailRvAdapter adapter;
    private SongListDetailBean bean;
    private ImageView toolbarLayoutBg, backBtn;
    private Toolbar toolbar;

    @Override
    public int setLayout() {
        return R.layout.fragment_song_list_detail;
    }

    @Override
    public void initView(View view) {
        toolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.detail_rv);
        toolbarLayoutBg = (ImageView) view.findViewById(R.id.song_list_detail_toolbar_layout_bg);
        backBtn = (ImageView) view.findViewById(R.id.song_list_detail_back_btn);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

    }

    @Override
    public void initData() {
        adapter = new SongListDetailRvAdapter(context);
        String listId = this.getArguments().getString("listId");
        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.diy.gedanInfo&from=ios&listid=" + listId + "&version=5.2.3&from=ios&channel=appstore";
        GsonRequest<SongListDetailBean> gsonRequest = new GsonRequest<SongListDetailBean>(url, SongListDetailBean.class, new Response.Listener<SongListDetailBean>() {
            @Override
            public void onResponse(SongListDetailBean response) {
                bean = response;
                adapter.setBean(response);
                toolbarLayout.setTitle(response.getTitle());
                RequestQueueSingleton.getInstance(context).getImageLoader().get(bean.getPic_500(),
                        ImageLoader.getImageListener(toolbarLayoutBg, R.mipmap.default_album_topic_detail, R.mipmap.default_album_topic_detail));

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
        adapter.setOnSongListDetailItemListener(new SongListDetailRvAdapter.OnSongListDetailItemListener() {
            @Override
            public void onItemClick(int position) {
                String songId = bean.getContent().get(position).getSong_id();
                EventBus.getDefault().post(new SongIdEvent(songId));

            }
        });
    }


}
