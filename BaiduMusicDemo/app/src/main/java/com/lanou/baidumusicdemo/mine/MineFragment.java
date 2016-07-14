package com.lanou.baidumusicdemo.mine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.db.LiteOrmSingle;
import com.lanou.baidumusicdemo.db.MyFavoriteSong;
import com.lanou.baidumusicdemo.db.RecentPlay;
import com.lanou.baidumusicdemo.main.ClickToFavorite;
import com.lanou.baidumusicdemo.main.ClickToLocal;
import com.lanou.baidumusicdemo.main.ClickToRecentPlay;
import com.lanou.baidumusicdemo.mine.favorite.FavoriteManager;
import com.lanou.baidumusicdemo.mine.local.LocalSongBean;
import com.lanou.baidumusicdemo.mine.local.ScanMusic;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by dllo on 16/6/17.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout recent, local, downLoadManage, myFavorite;
    private TextView recentNum, localNum,downLoadNum, favoriteNum;

    private RefreshReceiver refreshReceiver;

    @Override
    public int setLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView(View view) {
        local = (LinearLayout) view.findViewById(R.id.mine_local_layout);
        recent = (LinearLayout) view.findViewById(R.id.mine_recent);
        downLoadManage = (LinearLayout) view.findViewById(R.id.mine_download);
        myFavorite = (LinearLayout) view.findViewById(R.id.mine_my_favorite);

        recentNum = (TextView) view.findViewById(R.id.mine_recent_song_num);
        localNum = (TextView) view.findViewById(R.id.mine_local_song_num);
        downLoadNum = (TextView) view.findViewById(R.id.mine_download_song_num);
        favoriteNum = (TextView) view.findViewById(R.id.mine_favorite_song_num);

        local.setOnClickListener(this);
        recent.setOnClickListener(this);
        downLoadManage.setOnClickListener(this);
        myFavorite.setOnClickListener(this);

        // 注册广播
        refreshReceiver = new RefreshReceiver();
        IntentFilter intentFilter = new IntentFilter("REFRESH_MINE");
        context.registerReceiver(refreshReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(refreshReceiver);
    }

    @Override
    public void initData() {
        // 扫描本地音乐
        ScanMusic.query(context);
        localNum.setText("共"+LiteOrmSingle.getInstance().getLiteOrm().query(LocalSongBean.class).size()+"首");

        recentNum.setText("共" + LiteOrmSingle.getInstance().getLiteOrm().query(RecentPlay.class).size() + "首");

        BmobUser bmobUser = BmobUser.getCurrentUser(context);
        if (bmobUser == null) {
            favoriteNum.setText("共" + LiteOrmSingle.getInstance().getLiteOrm().query(MyFavoriteSong.class).size() + "首");
        } else {
            favoriteNum.setText("共" + FavoriteManager.getInstance().getFavoriteSongs().size() + "首");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 本地音乐
            case R.id.mine_local_layout:
                ((ClickToLocal)getActivity()).toLocal();
                break;
            // 最近播放
            case R.id.mine_recent:
                ((ClickToRecentPlay)getActivity()).toRecent();
                break;
            // 下载管理
            case R.id.mine_download:

                break;
            // 喜欢的单曲
            case R.id.mine_my_favorite:
                ((ClickToFavorite)getActivity()).toFavorite();
                break;
        }
    }

    class RefreshReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            initData();
        }
    }
}
