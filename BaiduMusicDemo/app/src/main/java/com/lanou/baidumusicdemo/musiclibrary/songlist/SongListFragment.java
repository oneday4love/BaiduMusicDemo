package com.lanou.baidumusicdemo.musiclibrary.songlist;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.main.ClickToRankDetail;
import com.lanou.baidumusicdemo.main.ClickToSongListDetail;
import com.lanou.baidumusicdemo.main.MainActivity;
import com.lanou.baidumusicdemo.musiclibrary.songlist.songlistdetail.ListIdEvent;
import com.lanou.baidumusicdemo.volley.GsonRequest;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by dllo on 16/6/17.
 */
public class SongListFragment extends BaseFragment {

    private String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.diy.gedan&page_no=1&page_size=30&from=ios&version=5.2.3&from=ios&channel=appstore";

    private RecyclerView songListRv;
    private SongListRvAdapter adapter;
    private SongListBean bean;

    @Override
    public int setLayout() {
        return R.layout.fragment_song_list;
    }

    @Override
    public void initView(View view) {
        songListRv = (RecyclerView) view.findViewById(R.id.song_list_recycler_view);
        adapter = new SongListRvAdapter(context);
    }

    @Override
    public void initData() {
        GsonRequest gsonRequest = new GsonRequest<SongListBean>(url, SongListBean.class, new Response.Listener<SongListBean>() {
            @Override
            public void onResponse(SongListBean response) {
                adapter.setBean(response);
                bean = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueueSingleton.getInstance(context).getRequestQueue().add(gsonRequest);

        songListRv.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(context, 2);
        songListRv.setLayoutManager(manager);

        // 点击事件
        adapter.setSongListRvClickListener(new SongListRvAdapter.SongListRvClickListener() {
            @Override
            public void onItemClickListener(int position) {
                String listId = bean.getContent().get(position).getListid();
                ((ClickToSongListDetail)getActivity()).toSongListDetail(listId);
                List songIds = bean.getContent().get(position).getSongIds();
            }
        });
    }
}
