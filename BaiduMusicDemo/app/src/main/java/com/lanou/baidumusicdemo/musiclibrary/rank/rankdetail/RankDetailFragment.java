package com.lanou.baidumusicdemo.musiclibrary.rank.rankdetail;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
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
import com.lanou.baidumusicdemo.musiclibrary.songlist.songlistdetail.SongListDetailBean;
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
 * Created by dllo on 16/6/21.
 */
public class RankDetailFragment extends BaseFragment implements View.OnClickListener {

    private RankDetailBean bean;
    private CollapsingToolbarLayout toolbarLayout;
    private RankDetailRvAdapter adapter;
    private RecyclerView recyclerView;
    private ImageView toolBarLayoutBg;
    private TextView songNum;
    private ImageView backBtn, downLoadBtn, shareBtn;
    private PopupWindow popupWindow;

    private MyFavoriteSong myFavoriteSong;

    @Override
    public int setLayout() {
        return R.layout.fragment_rank_detail;
    }

    @Override
    public void initView(View view) {
        toolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.rank_detail_toolbar_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.rank_detail_rv);
        toolBarLayoutBg = (ImageView) view.findViewById(R.id.rank_detail_toolbar_layout_bg);
        songNum = (TextView) view.findViewById(R.id.rank_detail_total_songs);
        backBtn = (ImageView) view.findViewById(R.id.rank_detail_back_btn);
        downLoadBtn = (ImageView) view.findViewById(R.id.rank_detail_down_load_all);
        shareBtn = (ImageView) view.findViewById(R.id.rank_detail_share_song_list);
    }

    @Override
    public void initData() {
        adapter = new RankDetailRvAdapter(context);
        int rankType = this.getArguments().getInt("rankType");
        String url = UrlValues.RANK_DETAIL_FRONT_URL + rankType + UrlValues.RANK_DETAIL_BEHIND_URL;
        GsonRequest<RankDetailBean> gsonRequest = new GsonRequest<RankDetailBean>(url, RankDetailBean.class, new Response.Listener<RankDetailBean>() {
            @Override
            public void onResponse(RankDetailBean response) {
                adapter.setBean(response);
                bean = response;
                toolbarLayout.setTitle(response.getBillboard().getName());
                toolbarLayout.setExpandedTitleTextAppearance(R.style.DetailTitle);
                RequestQueueSingleton.getInstance(context).getImageLoader().get(response.getBillboard().getPic_s210(),
                        ImageLoader.getImageListener(toolBarLayoutBg, R.mipmap.default_album_topic_detail, R.mipmap.default_album_topic_detail));
                if (Integer.valueOf(bean.getBillboard().getBillboard_songnum()) > 100) {
                    songNum.setText("共有100首歌");
                } else {
                    songNum.setText("共有" + bean.getBillboard().getBillboard_songnum() + "首歌");
                }
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
                EventBus.getDefault().post(new IndexEvent(position));
                PlayListManager.getInstance().getPlayList().clear();
                for (RankDetailBean.SongListBean songListBean : bean.getSong_list()) {
                    PlayListManager.getInstance().getPlayList().add(new PlayList(songListBean.getSong_id(), songListBean.getTitle(), songListBean.getAuthor()));
                }
            }
        });

        // 更多
        adapter.setOnGetMoreListener(new RankDetailRvAdapter.OnGetMoreListener() {
            @Override
            public void OnMoreClick(int position) {
                showPopMore(bean, position);
            }
        });

        backBtn.setOnClickListener(this);
        downLoadBtn.setOnClickListener(this);
        shareBtn.setOnClickListener(this);

    }


    public void showPopMore(final RankDetailBean bean, final int position) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_detail_more, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView title = (TextView) contentView.findViewById(R.id.pop_detail_more_title);
        ImageView likeIv = (ImageView) contentView.findViewById(R.id.pop_detail_like_iv);
        ImageView downLoadIv = (ImageView) contentView.findViewById(R.id.pop_detail_download_iv);
        ImageView shareIv = (ImageView) contentView.findViewById(R.id.pop_detail_share_iv);
        ImageView addIv = (ImageView) contentView.findViewById(R.id.pop_detail_add_iv);
        FrameLayout space = (FrameLayout) contentView.findViewById(R.id.pop_detail_space);

        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        title.setText(bean.getSong_list().get(position).getTitle());

//        QueryBuilder<MyFavoriteSong> queryBuilder = new QueryBuilder<>(MyFavoriteSong.class);
//        queryBuilder.whereEquals("title", bean.getSong_list().get(position).getTitle());
//        if (LiteOrmSingle.getInstance().getLiteOrm().query(queryBuilder).size() == 0) {
//
//            likeIv.setImageResource(R.mipmap.cust_dialog_hart);
//
//        } else {
//            likeIv.setImageResource(R.mipmap.cust_heart_press);
//        }

        BmobUser bmobUser = BmobUser.getCurrentUser(context);
        if (bmobUser != null){
            final MyFavoriteSong myFavoriteSong = new MyFavoriteSong();
            myFavoriteSong.setUserName(bmobUser.getUsername());
            myFavoriteSong.setTitle(bean.getSong_list().get(position).getTitle());
            myFavoriteSong.setAuthor(bean.getSong_list().get(position).getAuthor());
            myFavoriteSong.setSongId(bean.getSong_list().get(position).getSong_id());

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
            queryBuilderLocal.whereEquals("title", bean.getSong_list().get(position).getTitle());
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
                DownloadSingle.getSingleDownload().DownLoad(bean.getSong_list().get(position).getSong_id());
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

        popupWindow.showAtLocation(recyclerView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回上一页
            case R.id.rank_detail_back_btn:
                ((MainActivity) getActivity()).returnToPrev();
                break;
            // 下载
            case R.id.rank_detail_down_load_all:

                break;
            // 分享
            case R.id.rank_detail_share_song_list:

                break;
        }
    }

    // 加入本地数据库
    public void addToLocal(int position) {
        QueryBuilder<MyFavoriteSong> queryBuilder = new QueryBuilder<>(MyFavoriteSong.class);
        queryBuilder.whereEquals("title", bean.getSong_list().get(position).getTitle());
        if (LiteOrmSingle.getInstance().getLiteOrm().query(queryBuilder).size() == 0) {
            LiteOrmSingle.getInstance().getLiteOrm().insert(new MyFavoriteSong(bean.getSong_list().get(position).getSong_id(),
                    bean.getSong_list().get(position).getTitle(), bean.getSong_list().get(position).getAuthor()));
            Toast.makeText(MyApp.context, "已添加到我喜欢的单曲", Toast.LENGTH_SHORT).show();
        } else {
            LiteOrmSingle.getInstance().getLiteOrm().delete(LiteOrmSingle.getInstance().getLiteOrm().query(queryBuilder));
            Toast.makeText(MyApp.context, "已取消喜欢", Toast.LENGTH_SHORT).show();
        }
    }

    // 加入网络
    public void addToNet(final RankDetailBean bean, int position, BmobUser bmobUser) {

        myFavoriteSong = new MyFavoriteSong();
        myFavoriteSong.setUserName(bmobUser.getUsername());
        myFavoriteSong.setTitle(bean.getSong_list().get(position).getTitle());
        myFavoriteSong.setAuthor(bean.getSong_list().get(position).getAuthor());
        myFavoriteSong.setSongId(bean.getSong_list().get(position).getSong_id());

        int number = 0;
        for (MyFavoriteSong favoriteSong : FavoriteManager.getInstance().getFavoriteSongs()) {
            if (favoriteSong.getTitle().equals(myFavoriteSong.getTitle())) {
                number = number + 1;
                myFavoriteSong = favoriteSong;
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

}
