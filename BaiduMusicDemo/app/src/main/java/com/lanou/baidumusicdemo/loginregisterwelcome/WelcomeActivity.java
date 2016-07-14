package com.lanou.baidumusicdemo.loginregisterwelcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.lanou.baidumusicdemo.MyApp;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseActivity;
import com.lanou.baidumusicdemo.db.LiteOrmSingle;
import com.lanou.baidumusicdemo.db.MyFavoriteSong;
import com.lanou.baidumusicdemo.db.PlayList;
import com.lanou.baidumusicdemo.main.MainActivity;
import com.lanou.baidumusicdemo.mine.favorite.FavoriteManager;
import com.lanou.baidumusicdemo.service.PlayListManager;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dllo on 16/7/2.
 */
public class WelcomeActivity extends BaseActivity {

    private CountDownTimer timer;
    private TextView countdownTime, passBtn;
    private MyFavoriteSong myFavoriteSong;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        countdownTime = (TextView) findViewById(R.id.countdown_time);
        passBtn = (TextView) findViewById(R.id.pass_btn);

        timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownTime.setText("还有" + millisUntilFinished / 1000 + "s");

            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();

        passBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                timer.cancel();
                finish();
            }
        });

        // 播放列表
        PlayListManager.getInstance().getPlayList().clear();
        for (PlayList playList : LiteOrmSingle.getInstance().getLiteOrm().query(PlayList.class)) {
            PlayListManager.getInstance().getPlayList().add(playList);
        }


        BmobUser bmobUser = BmobUser.getCurrentUser(this);
        if (bmobUser != null) {
            myFavoriteSong = new MyFavoriteSong();

            //
            BmobQuery<MyFavoriteSong> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("userName", bmobUser.getUsername());
            bmobQuery.findObjects(this, new FindListener<MyFavoriteSong>() {
                @Override
                public void onSuccess(List<MyFavoriteSong> list) {
                    for (MyFavoriteSong myFavoriteSong : list) {
                        FavoriteManager.getInstance().getFavoriteSongs().add(myFavoriteSong);
                    }
                    int number = 0;
                    for (MyFavoriteSong song : LiteOrmSingle.getInstance().getLiteOrm().query(MyFavoriteSong.class)) {
                        for (MyFavoriteSong favoriteSong : FavoriteManager.getInstance().getFavoriteSongs()) {
                            if (song.getTitle().equals(favoriteSong.getTitle())) {
                                number++;
                            }
                        }

                        if (number == 0){
                            song.setUserName(BmobUser.getCurrentUser(MyApp.context).getUsername());
                            song.save(MyApp.context, new SaveListener() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onFailure(int i, String s) {

                                }
                            });
                        } else {
                            number = 0;
                        }
                    }
                    getNewData();
                }

                @Override
                public void onError(int i, String s) {

                }
            });

        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            timer.cancel();
        }
        return super.onKeyDown(keyCode, event);
    }

    // 获取最新数据
    public void getNewData(){
        if (BmobUser.getCurrentUser(MyApp.context) != null){
            BmobQuery<MyFavoriteSong> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("userName", BmobUser.getCurrentUser(MyApp.context).getUsername());
            bmobQuery.findObjects(this, new FindListener<MyFavoriteSong>() {
                @Override
                public void onSuccess(List<MyFavoriteSong> list) {
                    FavoriteManager.getInstance().getFavoriteSongs().clear();
                    for (MyFavoriteSong myFavoriteSong : list) {
                        FavoriteManager.getInstance().getFavoriteSongs().add(myFavoriteSong);
                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }
    }
}
