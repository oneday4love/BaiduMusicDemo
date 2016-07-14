package com.lanou.baidumusicdemo.musiclibrary.songlist;

import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.base.UrlValues;
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

    private String url = UrlValues.SONG_LIST_URL;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView songListRv;
    private SongListRvAdapter adapter;
    private SongListBean bean;
    private GridLayoutManager gridLayoutManager;
    private int lastVisibleItem;
    private  int num = 2;

    @Override
    public int setLayout() {
        return R.layout.fragment_song_list;
    }

    @Override
    public void initView(View view) {
        songListRv = (RecyclerView) view.findViewById(R.id.song_list_recycler_view);
//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.song_list_swipe_refresh_layout);
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
        gridLayoutManager = new GridLayoutManager(context, 2);
        songListRv.setLayoutManager(gridLayoutManager);

        // 点击事件
        adapter.setSongListRvClickListener(new SongListRvAdapter.SongListRvClickListener() {
            @Override
            public void onItemClickListener(int position) {
                String listId = bean.getContent().get(position).getListid();
                ((ClickToSongListDetail)getActivity()).toSongListDetail(listId);
            }
        });

        refresh();

    }

    private void refresh() {
//        swipeRefreshLayout.setProgressViewOffset(false,0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,24,getResources().getDisplayMetrics()));

        songListRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    String urlMore = UrlValues.SONG_LIST_MORE_FRONT_URL + num + UrlValues.SONG_LIST_MORE_BEHIND_URL;
                    GsonRequest<SongListBean> gsonRequest1 = new GsonRequest<SongListBean>(urlMore, SongListBean.class, new Response.Listener<SongListBean>() {
                        @Override
                        public void onResponse(SongListBean response) {
//                            swipeRefreshLayout.setRefreshing(false);

                            adapter.addBean(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    RequestQueueSingleton.getInstance(context).getRequestQueue().add(gsonRequest1);
//                    swipeRefreshLayout.setRefreshing(true);
                    num += 1;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                Log.d("SongListFragment", "lastVisibleItem:" + lastVisibleItem);
            }
        });

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
    }
}
