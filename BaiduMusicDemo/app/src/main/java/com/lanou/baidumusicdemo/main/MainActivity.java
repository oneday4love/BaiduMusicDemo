package com.lanou.baidumusicdemo.main;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.lanou.baidumusicdemo.MyApp;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.author.AuthorAllFragment;
import com.lanou.baidumusicdemo.author.AuthorTypeFragment;
import com.lanou.baidumusicdemo.base.BaseActivity;
import com.lanou.baidumusicdemo.musiclibrary.rank.rankdetail.RankDetailFragment;
import com.lanou.baidumusicdemo.musiclibrary.songlist.songlistdetail.SongListDetailFragment;
import com.lanou.baidumusicdemo.service.MediaPlayService;
import com.lanou.baidumusicdemo.service.SongPlayBean;
import com.lanou.baidumusicdemo.volley.GsonRequest;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends BaseActivity implements ClickToRankDetail, ClickToSongListDetail,
        ClickToAllAuthor, ClickToAuthorType {

    private FrameLayout replaceLayout;
    private MainFragment mainFragment = new MainFragment();
    private RankDetailFragment rankDetailFragment = new RankDetailFragment();
    private SongListDetailFragment songListDetailFragment = new SongListDetailFragment();
    private AuthorAllFragment authorAllFragment = new AuthorAllFragment();
    private AuthorTypeFragment authorTypeFragment = new AuthorTypeFragment();
    private int container = R.id.replace_layout;

    private MediaPlayService.PlayBinder playBinder;
    private ServiceConnection serviceConnection;

    private ImageView miniBarCover;
    private TextView miniBarTitle, miniBarAuthor;
    private ImageView playListBtn, playBtn, nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        miniBarCover = (ImageView) findViewById(R.id.minibar_cover_iv);
        miniBarTitle = (TextView) findViewById(R.id.minibar_title_tv);
        miniBarAuthor = (TextView) findViewById(R.id.minibar_author_tv);
        playListBtn = (ImageView) findViewById(R.id.minibar_show_playinglist_btn);
        playBtn = (ImageView) findViewById(R.id.minibar_play_btn);
        nextBtn = (ImageView) findViewById(R.id.minibar_next_btn);

        replaceLayout = (FrameLayout) findViewById(R.id.replace_layout);
        getSupportFragmentManager().beginTransaction().add(container, mainFragment, "mainFragment").commit();


        // 绑定服务
        Intent playIntent = new Intent(this, MediaPlayService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                playBinder = (MediaPlayService.PlayBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(playIntent, serviceConnection, BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    // 设置minibar
    @Subscribe
    public void initMiniBar(SongPlayBean songPlayBean) {
        RequestQueueSingleton.getInstance(this).getImageLoader().get(songPlayBean.getSonginfo().getPic_big(),
                ImageLoader.getImageListener(miniBarCover, R.mipmap.minibar_default_img, R.mipmap.minibar_default_img));
        miniBarTitle.setText(songPlayBean.getSonginfo().getTitle());
        miniBarAuthor.setText(songPlayBean.getSonginfo().getAuthor());
    }

    // 重写返回键方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (authorTypeFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(authorTypeFragment).show(authorAllFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().remove(songListDetailFragment).commit();
            getSupportFragmentManager().beginTransaction().remove(rankDetailFragment).commit();
            getSupportFragmentManager().beginTransaction().remove(authorAllFragment).commit();
            getSupportFragmentManager().beginTransaction().show(mainFragment).commit();
        }
        return false;
    }

    // 跳转排行详情
    @Override
    public void toRankDetail(int rankType) {
        getSupportFragmentManager().beginTransaction().add(container, rankDetailFragment, "rankDetailFragment").commit();
        getSupportFragmentManager().beginTransaction().hide(mainFragment).commit();
        Bundle bundle = new Bundle();
        bundle.putInt("rankType", rankType);
        rankDetailFragment.setArguments(bundle);
    }

    // 跳转歌单详情
    @Override
    public void toSongListDetail(String listId) {
        getSupportFragmentManager().beginTransaction().add(container, songListDetailFragment, "sonListDetailFragment").commit();
        getSupportFragmentManager().beginTransaction().hide(mainFragment).commit();
        Bundle bundle = new Bundle();
        bundle.putString("listId", listId);
        songListDetailFragment.setArguments(bundle);
    }

    // 跳转全部歌手
    @Override
    public void toAllAuthor() {
        getSupportFragmentManager().beginTransaction().add(container, authorAllFragment).hide(mainFragment).commit();
    }

    // 跳转到歌手分类
    @Override
    public void toAuthorType(String typeUrl) {
        getSupportFragmentManager().beginTransaction().add(container, authorTypeFragment).hide(authorAllFragment).commit();
        Bundle bundle = new Bundle();
        bundle.putString("typeUrl", typeUrl);
        authorTypeFragment.setArguments(bundle);
    }
}
