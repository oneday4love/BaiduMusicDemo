package com.lanou.baidumusicdemo.author;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.lanou.baidumusicdemo.MyApp;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.base.UrlValues;
import com.lanou.baidumusicdemo.db.LiteOrmSingle;
import com.lanou.baidumusicdemo.db.MyFavoriteSong;
import com.lanou.baidumusicdemo.db.PlayList;
import com.lanou.baidumusicdemo.main.MainActivity;
import com.lanou.baidumusicdemo.main.Share;
import com.lanou.baidumusicdemo.mine.download.DownloadSingle;
import com.lanou.baidumusicdemo.mine.favorite.FavoriteManager;
import com.lanou.baidumusicdemo.mine.local.ScanMusic;
import com.lanou.baidumusicdemo.musiclibrary.rank.rankdetail.RankDetailBean;
import com.lanou.baidumusicdemo.service.IndexEvent;
import com.lanou.baidumusicdemo.service.PlayListManager;
import com.lanou.baidumusicdemo.service.SongIdEvent;
import com.lanou.baidumusicdemo.volley.GsonRequest;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;
import com.litesuits.orm.db.assit.QueryBuilder;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dllo on 16/6/27.
 */
public class AuthorDetailFragment extends BaseFragment implements View.OnClickListener {

    private TextView songNum;
    private ImageView backBtn,collectBtn, downLoadBtn;

    private PopupWindow popupWindow;
    private CollapsingToolbarLayout toolbarLayout;
    private ImageView toolBarLayoutBg;
    private RecyclerView recyclerView;
    private AuthorDetailRvAdapter adapter;
    private AuthorDetailBean bean;
    private int lastVisibleItem, firstVisibleItem, visibleItemCount;
    private LinearLayoutManager layoutManager;
    private String tingUid, authorPic, author;
    private int limit = 0, haveMore;
    private MyFavoriteSong myFavoriteSong;

    @Override
    public int setLayout() {
        return R.layout.fragment_author_detail;
    }

    @Override
    public void initView(View view) {
        backBtn = (ImageView) view.findViewById(R.id.author_detail_back_btn);
        songNum= (TextView) view.findViewById(R.id.author_detail_total_songs);
        collectBtn = (ImageView) view.findViewById(R.id.author_detail_collect_song_list);
        downLoadBtn = (ImageView) view.findViewById(R.id.author_detail_down_load_all);

        toolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.author_detail_toolbar_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.author_detail_rv);
        toolBarLayoutBg = (ImageView) view.findViewById(R.id.auhtor_detail_toolbar_layout_bg);
    }

    @Override
    public void initData() {
        adapter = new AuthorDetailRvAdapter(context);
        tingUid = getArguments().getString("tingUid");
        authorPic = getArguments().getString("authorPic");
        author = getArguments().getString("author");
        String url = UrlValues.AUTHOR_DETAIL_FRONT_URL + tingUid + UrlValues.AUTHOR_DETAIL_BEHIND_URL;
        GsonRequest<AuthorDetailBean> gsonRequest = new GsonRequest<AuthorDetailBean>(url, AuthorDetailBean.class, new Response.Listener<AuthorDetailBean>() {
            @Override
            public void onResponse(AuthorDetailBean response) {
                bean = response;
                songNum.setText("共有"+ bean.getSongnums()+ "首歌");
                adapter.setBean(bean);
                recyclerView.setAdapter(adapter);
                layoutManager = new GridLayoutManager(context,1);
                recyclerView.setLayoutManager(layoutManager);
                haveMore = bean.getHavemore();

                toolbarLayout.setTitle(author);
                toolbarLayout.setExpandedTitleTextAppearance(R.style.DetailTitle);
                RequestQueueSingleton.getInstance(context).getImageLoader().get(authorPic, ImageLoader.getImageListener(
                        toolBarLayoutBg, R.mipmap.default_album_topic_detail, R.mipmap.default_album_topic_detail
                ));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueueSingleton.getInstance(context).getRequestQueue().add(gsonRequest);

        // 点击
        adapter.setOnAuthorDetailRvListener(new AuthorDetailRvAdapter.OnAuthorDetailRvListener() {
            @Override
            public void onItemClick(int position) {
                EventBus.getDefault().post(new SongIdEvent(bean.getSonglist().get(position).getSong_id()));
                EventBus.getDefault().post(new IndexEvent(position));
                PlayListManager.getInstance().getPlayList().clear();
                for (AuthorDetailBean.SonglistBean songlistBean : bean.getSonglist()) {
                    PlayListManager.getInstance().getPlayList().add(new PlayList(songlistBean.getSong_id(),songlistBean.getTitle(),songlistBean.getAuthor()));
                }
            }
        });
        // 更多
        adapter.setOnAuthorDetailGetMoreListener(new AuthorDetailRvAdapter.OnAuthorDetailGetMoreListener() {
            @Override
            public void onMoreClick(int position) {
                showPopMore(bean, position);
            }
        });

        backBtn.setOnClickListener(this);
        collectBtn.setOnClickListener(this);
        downLoadBtn.setOnClickListener(this);


//        loadMore();

    }

    public void showPopMore(final AuthorDetailBean bean, final int position){
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_detail_more,null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        TextView title = (TextView) contentView.findViewById(R.id.pop_detail_more_title);
        ImageView likeIv = (ImageView) contentView.findViewById(R.id.pop_detail_like_iv);
        ImageView downLoadIv = (ImageView) contentView.findViewById(R.id.pop_detail_download_iv);
        ImageView shareIv = (ImageView) contentView.findViewById(R.id.pop_detail_share_iv);
        ImageView addIv = (ImageView) contentView.findViewById(R.id.pop_detail_add_iv);
        FrameLayout space = (FrameLayout) contentView.findViewById(R.id.pop_detail_space);

        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        title.setText(bean.getSonglist().get(position).getTitle());

        BmobUser bmobUser = BmobUser.getCurrentUser(context);
        if (bmobUser != null){
            final MyFavoriteSong myFavoriteSong = new MyFavoriteSong();
            myFavoriteSong.setUserName(bmobUser.getUsername());
            myFavoriteSong.setTitle(bean.getSonglist().get(position).getTitle());
            myFavoriteSong.setAuthor(bean.getSonglist().get(position).getAuthor());
            myFavoriteSong.setSongId(bean.getSonglist().get(position).getSong_id());

            int number = 0;
            for (MyFavoriteSong favoriteSong : FavoriteManager.getInstance().getFavoriteSongs()) {
                if (favoriteSong.getTitle().equals(myFavoriteSong.getTitle())) {
                    number = number + 1;
                }
            }
            if (number == 0){
                likeIv.setImageResource(R.mipmap.cust_dialog_hart);
            } else {
                likeIv.setImageResource(R.mipmap.cust_heart_press);
            }
        } else {
            QueryBuilder<MyFavoriteSong> queryBuilderLocal = new QueryBuilder<>(MyFavoriteSong.class);
            queryBuilderLocal.whereEquals("title", bean.getSonglist().get(position).getTitle());
            if (LiteOrmSingle.getInstance().getLiteOrm().query(queryBuilderLocal).size() == 0) {

                likeIv.setImageResource(R.mipmap.cust_dialog_hart);

            } else {
                likeIv.setImageResource(R.mipmap.cust_heart_press);
            }
        }

        // 喜欢
        likeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BmobUser bmobUser = BmobUser.getCurrentUser(context);
                if (bmobUser == null) {
                    addToLocal(position);
                    // 刷新
                    Intent intent = new Intent("REFRESH_MINE");
                    context.sendBroadcast(intent);
                } else {
                    addToNet(bean, position, bmobUser);
                }
                popupWindow.dismiss();

            }
        });
        // 下载
        downLoadIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadSingle.getSingleDownload().DownLoad(bean.getSonglist().get(position).getSong_id());
                // 通知刷新
                Intent intent = new Intent("REFRESH_MINE");
                context.sendBroadcast(intent);
            }
        });
        // 分享
        shareIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share.share(context);
            }
        });
        // 添加
        addIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // 点击空白
        space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(recyclerView, Gravity.BOTTOM, 0 , 0);
    }


    // 加入本地数据库
    public void addToLocal(int position) {
        QueryBuilder<MyFavoriteSong> queryBuilder = new QueryBuilder<>(MyFavoriteSong.class);
        queryBuilder.whereEquals("title", bean.getSonglist().get(position).getTitle());
        if (LiteOrmSingle.getInstance().getLiteOrm().query(queryBuilder).size() == 0) {
            LiteOrmSingle.getInstance().getLiteOrm().insert(new MyFavoriteSong(bean.getSonglist().get(position).getSong_id(),
                    bean.getSonglist().get(position).getTitle(), bean.getSonglist().get(position).getAuthor()));
            Toast.makeText(MyApp.context, "已添加到我喜欢的单曲", Toast.LENGTH_SHORT).show();
        } else {
            LiteOrmSingle.getInstance().getLiteOrm().delete(LiteOrmSingle.getInstance().getLiteOrm().query(queryBuilder));
            Toast.makeText(MyApp.context, "已取消喜欢", Toast.LENGTH_SHORT).show();
        }
    }

    // 加入网络
    public void addToNet(final AuthorDetailBean bean, int position, BmobUser bmobUser) {

        myFavoriteSong = new MyFavoriteSong();
        myFavoriteSong.setUserName(bmobUser.getUsername());
        myFavoriteSong.setTitle(bean.getSonglist().get(position).getTitle());
        myFavoriteSong.setAuthor(bean.getSonglist().get(position).getAuthor());
        myFavoriteSong.setSongId(bean.getSonglist().get(position).getSong_id());

        int number = 0;
        for (MyFavoriteSong favoriteSong : FavoriteManager.getInstance().getFavoriteSongs()) {
            if (favoriteSong.getTitle().equals(myFavoriteSong.getTitle())) {
                number = number + 1;
                myFavoriteSong = favoriteSong;
                Log.d("RecentPlayFragment", "number:" + number);
            }
        }


        if (number == 0) {
            myFavoriteSong.save(context, new SaveListener() {
                @Override
                public void onSuccess() {
                    FavoriteManager.getInstance().getFavoriteSongs().add(myFavoriteSong);
                    Toast.makeText(MyApp.context, "已添加到我喜欢的单曲", Toast.LENGTH_SHORT).show();
                    // 刷新
                    Intent intent = new Intent("REFRESH_MINE");
                    context.sendBroadcast(intent);
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.d("RecentPlayFragment", "save" + s);
                }
            });
        } else {
            myFavoriteSong.delete(context, new DeleteListener() {
                @Override
                public void onSuccess() {
                    FavoriteManager.getInstance().getFavoriteSongs().remove(myFavoriteSong);
                    Toast.makeText(MyApp.context, "已取消喜欢", Toast.LENGTH_SHORT).show();
                    // 刷新
                    Intent intent = new Intent("REFRESH_MINE");
                    context.sendBroadcast(intent);
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.d("RecentPlayFragment", "del"+s);
                }
            });
        }


    }


    // 加载更多
    private void loadMore() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && haveMore == 1
                        && lastVisibleItem + 1 == layoutManager.getItemCount()){
                    String urlMore = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getSongList&format=json&tinguid="
                            +tingUid +"&artistid(null)&limits="+limit+"&order=2&offset=0&version=5.2.5&from=ios&channel=appstore";
                    GsonRequest<AuthorDetailBean> moreRequest = new GsonRequest<AuthorDetailBean>(urlMore, AuthorDetailBean.class, new Response.Listener<AuthorDetailBean>() {
                        @Override
                        public void onResponse(AuthorDetailBean response) {
                            adapter.addBean(response);
                            haveMore = response.getHavemore();
                            limit = limit + 30 * haveMore;
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    RequestQueueSingleton.getInstance(context).getRequestQueue().add(moreRequest);

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                Log.d("AuthorDetailFragment", "lastVisibleItem:" + lastVisibleItem);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 返回
            case R.id.author_detail_back_btn:
                ((MainActivity)getActivity()).returnToPrev();
                break;
            // 收藏
            case R.id.author_detail_collect_song_list:

                break;
            // 下载
            case R.id.author_detail_down_load_all:

                break;
        }
    }
}
