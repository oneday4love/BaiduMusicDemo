package com.lanou.baidumusicdemo.main;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.lanou.baidumusicdemo.MyApp;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.author.AuthorAllFragment;
import com.lanou.baidumusicdemo.author.AuthorDetailFragment;
import com.lanou.baidumusicdemo.author.AuthorTypeFragment;
import com.lanou.baidumusicdemo.base.BaseActivity;
import com.lanou.baidumusicdemo.db.LiteOrmSingle;
import com.lanou.baidumusicdemo.db.PlayList;
import com.lanou.baidumusicdemo.db.RecentPlay;
import com.lanou.baidumusicdemo.mine.favorite.FavoriteFragment;
import com.lanou.baidumusicdemo.mine.local.LocalFragment;
import com.lanou.baidumusicdemo.mine.recent.RecentPlayFragment;
import com.lanou.baidumusicdemo.musiclibrary.rank.rankdetail.RankDetailFragment;
import com.lanou.baidumusicdemo.musiclibrary.songlist.songlistdetail.SongListDetailFragment;
import com.lanou.baidumusicdemo.playpage.PlayPageActivity;
import com.lanou.baidumusicdemo.search.SearchFragment;
import com.lanou.baidumusicdemo.service.IndexEvent;
import com.lanou.baidumusicdemo.service.MediaPlayService;
import com.lanou.baidumusicdemo.service.PlayListManager;
import com.lanou.baidumusicdemo.service.PlayPauseEvent;
import com.lanou.baidumusicdemo.service.SongIdEvent;
import com.lanou.baidumusicdemo.service.SongPlayBean;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends BaseActivity implements ClickToRankDetail, ClickToSongListDetail,
        ClickToAllAuthor, ClickToAuthorType, ClickToAuthorDetail, View.OnClickListener, ClickToSearch,
        ClickToRecentPlay, ClickToFavorite, ClickToLocal {

    private SongPlayBean songPlayBean;
    private static Boolean isExit = false;

    private FrameLayout replaceLayout;
    private MainFragment mainFragment = new MainFragment();
    private RankDetailFragment rankDetailFragment = new RankDetailFragment();
    private SongListDetailFragment songListDetailFragment = new SongListDetailFragment();
    private AuthorAllFragment authorAllFragment = new AuthorAllFragment();
    private AuthorTypeFragment authorTypeFragment = new AuthorTypeFragment();
    private AuthorDetailFragment authorDetailFragment = new AuthorDetailFragment();
    private SearchFragment searchFragment = new SearchFragment();
    private RecentPlayFragment recentPlayFragment = new RecentPlayFragment();
    private FavoriteFragment favoriteFragment = new FavoriteFragment();
    private LocalFragment localFragment = new LocalFragment();
    private int container = R.id.replace_layout;

    private MediaPlayService.PlayBinder playBinder;
    private ServiceConnection serviceConnection;
    private int pattern = 0;
    private int progress;

    private ImageView miniBarCover;
    private TextView miniBarTitle, miniBarAuthor;
    private ImageView playListBtn, playBtn, nextBtn;
    private PopupWindow popupWindow;
    private PopListViewAdapter popAdapter;
    private RelativeLayout minibarLayout;
    private LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        miniBarCover = (ImageView) findViewById(R.id.minibar_cover_iv);
        miniBarTitle = (TextView) findViewById(R.id.minibar_title_tv);
        miniBarAuthor = (TextView) findViewById(R.id.minibar_author_tv);
        playListBtn = (ImageView) findViewById(R.id.minibar_show_playinglist_btn);
        playBtn = (ImageView) findViewById(R.id.minibar_play_btn);
        nextBtn = (ImageView) findViewById(R.id.minibar_next_btn);
        minibarLayout = (RelativeLayout) findViewById(R.id.minibar_layout);

        playListBtn.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        minibarLayout.setOnClickListener(this);

        replaceLayout = (FrameLayout) findViewById(R.id.replace_layout);
        getSupportFragmentManager().beginTransaction().add(container, mainFragment, "mainFragment").commit();

        setLatestPlay();// 最后播放

        // 绑定服务
        Intent playIntent = new Intent(this, MediaPlayService.class);
        startService(playIntent);
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
        LiteOrmSingle.getInstance().getLiteOrm().deleteAll(PlayList.class);
        LiteOrmSingle.getInstance().getLiteOrm().insert(PlayListManager.getInstance().getPlayList());
        unbindService(serviceConnection);
        saveLatestPlay();

    }

    // 储存最后播放
    public void saveLatestPlay() {
        if (songPlayBean != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("latestSong", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("title", songPlayBean.getSonginfo().getTitle());
            editor.putString("author", songPlayBean.getSonginfo().getAuthor());
            editor.putString("songId", songPlayBean.getSonginfo().getSong_id());
            editor.putString("cover", songPlayBean.getSonginfo().getPic_big());
            editor.putInt("index", playBinder.getIndex());
            editor.commit();
        }

    }

    // 设置最后播放到minibar
    public void setLatestPlay() {
        SharedPreferences sharedPreferences = getSharedPreferences("latestSong", MODE_PRIVATE);
        String title = sharedPreferences.getString("title", "百度音乐 听到极致");
        String author = sharedPreferences.getString("author", "");
        String songId = sharedPreferences.getString("songId", "");
        String cover = sharedPreferences.getString("cover", "");
        miniBarTitle.setText(title);
        miniBarAuthor.setText(author);
        RequestQueueSingleton.getInstance(this).getImageLoader().get(cover, ImageLoader.getImageListener(miniBarCover,
                R.mipmap.minibar_default_img, R.mipmap.minibar_default_img));
    }

    // 设置minibar
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void initMiniBar(SongPlayBean songPlayBean) {
        this.songPlayBean = songPlayBean;
        RequestQueueSingleton.getInstance(this).getImageLoader().get(songPlayBean.getSonginfo().getPic_big(),
                ImageLoader.getImageListener(miniBarCover, R.mipmap.minibar_default_img, R.mipmap.minibar_default_img));
        miniBarTitle.setText(songPlayBean.getSonginfo().getTitle());
        miniBarAuthor.setText(songPlayBean.getSonginfo().getAuthor());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setPlayBtn(PlayPauseEvent playPauseEvent) {
        if (playPauseEvent.isPlaying()) {
            playBtn.setImageResource(R.mipmap.bt_minibar_pause_normal);
        } else {
            playBtn.setImageResource(R.mipmap.bt_minibar_play_normal);
        }
    }

    // 重写返回键方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnToPrev();
        }
        return isExit;
    }

    // 双击退出
    public void doubleClickExit() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
        }
    }

    // 返回上一页
    public void returnToPrev() {
        if (authorDetailFragment.isAdded()) {
            if (authorTypeFragment.isHidden()) {
                getSupportFragmentManager().beginTransaction().remove(authorDetailFragment).show(authorTypeFragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().remove(authorDetailFragment).show(authorAllFragment).commit();
            }
        } else if (authorTypeFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(authorTypeFragment).show(authorAllFragment).commit();
        } else if (mainFragment.isHidden()) {
            getSupportFragmentManager().beginTransaction().remove(songListDetailFragment).commit();
            getSupportFragmentManager().beginTransaction().remove(rankDetailFragment).commit();
            getSupportFragmentManager().beginTransaction().remove(authorAllFragment).commit();
            getSupportFragmentManager().beginTransaction().remove(searchFragment).commit();
            getSupportFragmentManager().beginTransaction().remove(recentPlayFragment).commit();
            getSupportFragmentManager().beginTransaction().remove(favoriteFragment).commit();
            getSupportFragmentManager().beginTransaction().remove(localFragment).commit();
            getSupportFragmentManager().beginTransaction().show(mainFragment).commit();
        } else {
            doubleClickExit();
        }
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

    // 跳转到全部歌手
    @Override
    public void toAllAuthor() {
        getSupportFragmentManager().beginTransaction().add(container, authorAllFragment).hide(mainFragment).commit();
    }

    // 跳转到歌手分类
    @Override
    public void toAuthorType(String typeUrl, String type) {
        getSupportFragmentManager().beginTransaction().add(container, authorTypeFragment).hide(authorAllFragment).commit();
        Bundle bundle = new Bundle();
        bundle.putString("typeUrl", typeUrl);
        bundle.putString("type", type);
        authorTypeFragment.setArguments(bundle);
    }

    // 跳转到歌手详情
    @Override
    public void toAuthorDetail(String tingUid, String authorPic, String author) {
        if (authorTypeFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().add(container, authorDetailFragment).hide(authorTypeFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().add(container, authorDetailFragment).hide(authorAllFragment).commit();
        }
        Bundle bundle = new Bundle();
        bundle.putString("tingUid", tingUid);
        bundle.putString("authorPic", authorPic);
        bundle.putString("author", author);
        authorDetailFragment.setArguments(bundle);
    }

    // 跳转搜索
    @Override
    public void toSearch() {
        getSupportFragmentManager().beginTransaction().add(container, searchFragment).hide(mainFragment).commit();
    }

    // 调转最近播放
    @Override
    public void toRecent() {
        getSupportFragmentManager().beginTransaction().add(container, recentPlayFragment).hide(mainFragment).commit();
    }

    // 跳转喜欢的单曲
    @Override
    public void toFavorite() {
        getSupportFragmentManager().beginTransaction().add(container, favoriteFragment).hide(mainFragment).commit();
    }

    // 跳转本地音乐
    @Override
    public void toLocal() {
        getSupportFragmentManager().beginTransaction().add(container, localFragment).hide(mainFragment).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 显示播放列表
            case R.id.minibar_show_playinglist_btn:
                if (popupWindow == null || !popupWindow.isShowing()) {
                    showPlayList();
                } else {
                    popupWindow.dismiss();
                }
                break;
            // 播放/暂停
            case R.id.minibar_play_btn:
                SharedPreferences sharedPreferences = getSharedPreferences("latestSong", MODE_PRIVATE);
                String songId = sharedPreferences.getString("songId", null);
                int index = sharedPreferences.getInt("index", 0);
                if (PlayListManager.getInstance().getPlayList().size() == 0) {
                    Toast.makeText(this, "当前没有正在播放的歌曲", Toast.LENGTH_SHORT).show();
                } else if (songId != null) {
                    EventBus.getDefault().post(new SongIdEvent(songId));
                    EventBus.getDefault().post(new IndexEvent(index));
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("songId",null);
                    editor.putString("title", null);
                    editor.putString("author", null);
                    editor.putString("cover", null);
                    editor.commit();
                } else {
                    playBinder.playPause();
                }
                break;
            // 下一曲
            case R.id.minibar_next_btn:
                if (PlayListManager.getInstance().getPlayList().size() == 0) {
                    Toast.makeText(this, "当前没有正在播放的歌曲", Toast.LENGTH_SHORT).show();
                } else {
                    playBinder.playNext(playBinder.getPattern());
                }
                break;
            // 点击minibar进入playPage
            case R.id.minibar_layout:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                SharedPreferences sharedP = getSharedPreferences("latestSong", MODE_PRIVATE);
                String songId1 = sharedP.getString("songId", null);
                Intent intent = new Intent(this, PlayPageActivity.class);
                if (songPlayBean != null) {
                    intent.putExtra("songPlayBeanTitle", songPlayBean.getSonginfo().getTitle());
                    intent.putExtra("songPlayBeanAuthor", songPlayBean.getSonginfo().getAuthor());
                    intent.putExtra("songPlayBeanPic", songPlayBean.getSonginfo().getPic_premium());
                    intent.putExtra("songPlayBeanLrc", songPlayBean.getSonginfo().getLrclink());
                    intent.putExtra("pattern", playBinder.getPattern());
                } else {
                    intent.putExtra("songPlayBeanTitle", sharedP.getString("title", null));
                    intent.putExtra("songPlayBeanAuthor", sharedP.getString("author", null));
                    intent.putExtra("songPlayBeanPic", "");
                    intent.putExtra("songPlayBeanLrc", "");
                    intent.putExtra("pattern", 0);
                }
                startActivity(intent);
//                finish();
                break;
        }
    }

    // 显示播放列表
    public void showPlayList() {

        popAdapter = new PopListViewAdapter(this);

        //获取屏幕高度
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_main_play_list, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT
                , displayMetrics.heightPixels - (miniBarCover.getHeight() + getStatusBarHeight()));

        popupWindow.showAtLocation(replaceLayout, Gravity.TOP, 0, 0);
        ListView popListView = (ListView) contentView.findViewById(R.id.pop_list_view);
        TextView clearAll = (TextView) contentView.findViewById(R.id.pop_clear_all);
        final ImageView playPattern = (ImageView) contentView.findViewById(R.id.pop_play_pattern);
        TextView playListTv = (TextView) contentView.findViewById(R.id.pop_play_list_tv);
        final TextView none = (TextView) contentView.findViewById(R.id.pop_list_play_none);
        FrameLayout space = (FrameLayout) contentView.findViewById(R.id.pop_play_list_space);


        popAdapter.setPlayLists(PlayListManager.getInstance().getPlayList());

        popListView.setAdapter(popAdapter);

        if (PlayListManager.getInstance().getPlayList().size() == 0) {
            none.setVisibility(View.VISIBLE);
        }

        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        // 初始化播放模式
        switch (playBinder.getPattern()) {
            case 0:
                playPattern.setImageResource(R.mipmap.bt_widget_mode_cycle_press);
                break;
            case 1:
                playPattern.setImageResource(R.mipmap.bt_widget_mode_singlecycle_press);
                break;
            case 2:
                playPattern.setImageResource(R.mipmap.bt_widget_mode_shuffle_press);
                break;
            case 3:
                playPattern.setImageResource(R.mipmap.bt_widget_mode_order_press);
                break;
        }


        // 点击播放
        popListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new SongIdEvent
                        (PlayListManager.getInstance().getPlayList().get(position).getSongId()));
                EventBus.getDefault().post(new IndexEvent(position));
            }
        });

        // 点击删除
        popAdapter.setOnRemoveListener(new PopListViewAdapter.OnRemoveListener() {
            @Override
            public void onRemoveClick(int position) {
                PlayListManager.getInstance().getPlayList().remove(position);
                popAdapter.setPlayLists(PlayListManager.getInstance().getPlayList());
                if (playBinder.isDelPlaying(position)) {

                    if (playBinder.getPattern() == 2) {
                        Random random = new Random();
                        int pos = random.nextInt(PlayListManager.getInstance().getPlayList().size());
                        EventBus.getDefault().post(new IndexEvent(pos));
                    } else if (playBinder.getPattern() == 1) {
                        EventBus.getDefault().post(new IndexEvent(position));
                    } else {
                        EventBus.getDefault().post(new IndexEvent(position - 1));
                    }
                    playBinder.playNext(playBinder.getPattern());
                }
            }
        });

        // 设置播放模式
        playPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pattern = playBinder.getPattern();
                pattern++;
                if (pattern > 3) {
                    pattern = 0;
                }
                playBinder.setPattern(pattern);
                switch (pattern) {
                    case 0:
                        Toast.makeText(MainActivity.this, "列表循环", Toast.LENGTH_SHORT).show();
                        playPattern.setImageResource(R.mipmap.bt_widget_mode_cycle_press);
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "单曲循环", Toast.LENGTH_SHORT).show();
                        playPattern.setImageResource(R.mipmap.bt_widget_mode_singlecycle_press);
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "随机播放", Toast.LENGTH_SHORT).show();
                        playPattern.setImageResource(R.mipmap.bt_widget_mode_shuffle_press);
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "顺序播放", Toast.LENGTH_SHORT).show();
                        playPattern.setImageResource(R.mipmap.bt_widget_mode_order_press);
                        break;
                }
            }
        });

        // 清空全部
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                none.setVisibility(View.VISIBLE);
                PlayListManager.getInstance().getPlayList().clear();
                popAdapter.setPlayLists(PlayListManager.getInstance().getPlayList());
                playBinder.stopPlay();
                SharedPreferences sharedPreferences = getSharedPreferences("latestSong",MODE_PRIVATE);
                miniBarTitle.setText("百度音乐 听到极致");
                miniBarAuthor.setText(null);
                miniBarCover.setImageResource(R.mipmap.minibar_default_img);
                songPlayBean = null;
            }
        });

        //返回
        playListTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


    }


    // 获取原始高度
    private int getDpi() {
        int dpi = 0;
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            dpi = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    // 获取通知栏高度
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


}
