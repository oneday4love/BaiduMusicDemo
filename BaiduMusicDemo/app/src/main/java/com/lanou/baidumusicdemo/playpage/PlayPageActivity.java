package com.lanou.baidumusicdemo.playpage;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseActivity;
import com.lanou.baidumusicdemo.main.MainActivity;
import com.lanou.baidumusicdemo.service.IndexEvent;
import com.lanou.baidumusicdemo.service.MediaPlayService;
import com.lanou.baidumusicdemo.service.PlayListManager;
import com.lanou.baidumusicdemo.service.PlayPauseEvent;
import com.lanou.baidumusicdemo.service.ProgressEvent;
import com.lanou.baidumusicdemo.service.SongIdEvent;
import com.lanou.baidumusicdemo.service.SongPlayBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * Created by dllo on 16/6/28.
 */
public class PlayPageActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private PlayPageVpAdapter adapter;

    private TextView title, author, duration, playTime;
    private ImageView picLrc;
    private SeekBar seekBar;
    private ImageView nextBtn, playBtn, preBtn, showPlayList, patternBtn, closeBtn;
    private int pattern;

    private ServiceConnection connection;
    private MediaPlayService.PlayBinder playBinder;
    private PicFragment picFragment;

    private PopupWindow popupWindow;
    private PlayPagePopAdapter popAdapter;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_page);
        linearLayout = (LinearLayout) findViewById(R.id.playpage_layout);

        closeBtn = (ImageView) findViewById(R.id.play_page_return_btn);
        picLrc = (ImageView) findViewById(R.id.play_page_vp_switcher);
        seekBar = (SeekBar) findViewById(R.id.play_page_seekbar);
        title = (TextView) findViewById(R.id.play_page_titel);
        author = (TextView) findViewById(R.id.play_page_author);
        viewPager = (ViewPager) findViewById(R.id.play_page_vp);
        duration = (TextView) findViewById(R.id.play_page_play_duration_tv);
        playTime = (TextView) findViewById(R.id.play_page_play_time_tv);

        nextBtn = (ImageView) findViewById(R.id.play_page_next_btn);
        playBtn = (ImageView) findViewById(R.id.play_page_play_btn);
        preBtn = (ImageView) findViewById(R.id.play_page_pre_btn);
        showPlayList = (ImageView) findViewById(R.id.play_page_list_more);
        patternBtn = (ImageView) findViewById(R.id.play_page_pattern);

        closeBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        preBtn.setOnClickListener(this);
        showPlayList.setOnClickListener(this);
        patternBtn.setOnClickListener(this);


        picLrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == 0) {
                    viewPager.setCurrentItem(1);
                    picLrc.setImageResource(R.mipmap.bt_playpage_button_pic_press);
                } else {
                    viewPager.setCurrentItem(0);
                    picLrc.setImageResource(R.mipmap.bt_playpage_button_lyric_press);
                }
            }
        });

        adapter = new PlayPageVpAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    picLrc.setImageResource(R.mipmap.bt_playpage_button_lyric_press);
                } else {
                    picLrc.setImageResource(R.mipmap.bt_playpage_button_pic_press);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Intent serviceIntent = new Intent(this, MediaPlayService.class);
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                playBinder = (MediaPlayService.PlayBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(serviceIntent, connection, BIND_AUTO_CREATE);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playBinder.setProgress(seekBar.getProgress());
            }
        });

        initPlayPage();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        EventBus.getDefault().unregister(this);
    }


    // 初始化播放页面
    public void initPlayPage() {
        title.setText(getIntent().getStringExtra("songPlayBeanTitle"));
        author.setText(getIntent().getStringExtra("songPlayBeanAuthor"));
        pattern = getIntent().getIntExtra("pattern", 0);
        switch (pattern) {
            case 0:
                patternBtn.setImageResource(R.mipmap.bt_widget_mode_cycle_press);
                break;
            case 1:
                patternBtn.setImageResource(R.mipmap.bt_widget_mode_singlecycle_press);
                break;
            case 2:
                patternBtn.setImageResource(R.mipmap.bt_widget_mode_shuffle_press);
                break;
            case 3:
                patternBtn.setImageResource(R.mipmap.bt_widget_mode_order_press);
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setPlayPage(SongPlayBean songPlayBean) {
        title.setText(songPlayBean.getSonginfo().getTitle());
        author.setText(songPlayBean.getSonginfo().getAuthor());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setProgress(ProgressEvent progressEvent) {
        seekBar.setMax((int) playBinder.getDuration());
        seekBar.setProgress(progressEvent.getProgress());
        playTime.setText(progressEvent.getPlayTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        String dura = simpleDateFormat.format(playBinder.getDuration());
        duration.setText(dura);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setPlayBtn(PlayPauseEvent playPauseEvent) {
        if (playPauseEvent.isPlaying()) {
            playBtn.setImageResource(R.mipmap.bt_playpage_pause_press);
        } else {
            playBtn.setImageResource(R.mipmap.bt_playpage_play_press);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回
            case R.id.play_page_return_btn:
//                Intent intent = new Intent(PlayPageActivity.this, MainActivity.class);
//                startActivity(intent);
                finish();
                break;
            // 下一曲
            case R.id.play_page_next_btn:
                if (PlayListManager.getInstance().getPlayList().size() != 0) {
                    playBinder.playNext(playBinder.getPattern());
                }
                break;
            // 播放/暂停
            case R.id.play_page_play_btn:
                SharedPreferences sharedPreferences = getSharedPreferences("latestSong", MODE_PRIVATE);
                String songId = sharedPreferences.getString("songId", null);
                if (songId != null) {
                    EventBus.getDefault().post(new SongIdEvent(songId));
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("songId", null);
                    editor.commit();
                } else {
                    playBinder.playPause();
                }
                break;
            // 上一曲
            case R.id.play_page_pre_btn:
                playBinder.playPre();
                break;
            // 显示播放列表
            case R.id.play_page_list_more:
                showPlayList();
                break;
            // 播放模式
            case R.id.play_page_pattern:
                pattern++;
                if (pattern > 3) {
                    pattern = 0;
                }
                playBinder.setPattern(pattern);
                switch (pattern) {
                    case 0:
                        Toast.makeText(PlayPageActivity.this, "列表循环", Toast.LENGTH_SHORT).show();
                        patternBtn.setImageResource(R.mipmap.bt_widget_mode_cycle_press);
                        break;
                    case 1:
                        Toast.makeText(PlayPageActivity.this, "单曲循环", Toast.LENGTH_SHORT).show();
                        patternBtn.setImageResource(R.mipmap.bt_widget_mode_singlecycle_press);
                        break;
                    case 2:
                        Toast.makeText(PlayPageActivity.this, "随机播放", Toast.LENGTH_SHORT).show();
                        patternBtn.setImageResource(R.mipmap.bt_widget_mode_shuffle_press);
                        break;
                    case 3:
                        Toast.makeText(PlayPageActivity.this, "顺序播放", Toast.LENGTH_SHORT).show();
                        patternBtn.setImageResource(R.mipmap.bt_widget_mode_order_press);
                        break;
                }
                break;
        }
    }

    // 显示正在播放列表
    public void showPlayList() {

        popAdapter = new PlayPagePopAdapter(this);

        //获取屏幕高度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        //获取通知栏高度
//        Rect outRect = new Rect();
//        getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);

        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_playpage_play_list, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);

//        popupWindow.setAnimationStyle(R.style.contextMenuAnim);
//        popupWindow.showAtLocation(replaceLayout, Gravity.CENTER, 0,-300);

//        popupWindow.showAsDropDown(, 0, -displayMetrics.heightPixels / 4 * 3);

        popupWindow.showAtLocation(linearLayout, Gravity.BOTTOM, 0, 0);
        ListView popListView = (ListView) contentView.findViewById(R.id.pop_palypage_list_view);
        TextView clearAll = (TextView) contentView.findViewById(R.id.pop_playpage_clear_all);
        final ImageView playPattern = (ImageView) contentView.findViewById(R.id.pop_playpage_pattern);
        TextView playListTv = (TextView) contentView.findViewById(R.id.pop_playpage_list_tv);
        TextView closeTv = (TextView) contentView.findViewById(R.id.pop_playpage_close);

        popAdapter.setPlayLists(PlayListManager.getInstance().getPlayList());

        popListView.setAdapter(popAdapter);

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
        popAdapter.setOnDelClickListener(new PlayPagePopAdapter.OnDelClickListener() {
            @Override
            public void onDel(int position) {
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
                pattern++;
                if (pattern > 3) {
                    pattern = 0;
                }
                playBinder.setPattern(pattern);
                switch (pattern) {
                    case 0:
                        Toast.makeText(PlayPageActivity.this, "列表循环", Toast.LENGTH_SHORT).show();
                        playPattern.setImageResource(R.mipmap.bt_widget_mode_cycle_press);
                        break;
                    case 1:
                        Toast.makeText(PlayPageActivity.this, "单曲循环", Toast.LENGTH_SHORT).show();
                        playPattern.setImageResource(R.mipmap.bt_widget_mode_singlecycle_press);
                        break;
                    case 2:
                        Toast.makeText(PlayPageActivity.this, "随机播放", Toast.LENGTH_SHORT).show();
                        playPattern.setImageResource(R.mipmap.bt_widget_mode_shuffle_press);
                        break;
                    case 3:
                        Toast.makeText(PlayPageActivity.this, "顺序播放", Toast.LENGTH_SHORT).show();
                        playPattern.setImageResource(R.mipmap.bt_widget_mode_order_press);
                        break;
                }
            }
        });

        // 清空全部
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayListManager.getInstance().getPlayList().clear();
                popAdapter.setPlayLists(PlayListManager.getInstance().getPlayList());
                playBinder.stopPlay();
            }
        });

        //返回
        closeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
}
