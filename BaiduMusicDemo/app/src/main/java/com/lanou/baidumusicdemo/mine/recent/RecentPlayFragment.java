package com.lanou.baidumusicdemo.mine.recent;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lanou.baidumusicdemo.MyApp;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.db.LiteOrmSingle;
import com.lanou.baidumusicdemo.db.MyFavoriteSong;
import com.lanou.baidumusicdemo.db.PlayList;
import com.lanou.baidumusicdemo.db.RecentPlay;
import com.lanou.baidumusicdemo.main.MainActivity;
import com.lanou.baidumusicdemo.main.Share;
import com.lanou.baidumusicdemo.mine.download.DownloadSingle;
import com.lanou.baidumusicdemo.mine.favorite.FavoriteManager;
import com.lanou.baidumusicdemo.service.IndexEvent;
import com.lanou.baidumusicdemo.service.PlayListManager;
import com.lanou.baidumusicdemo.service.SongIdEvent;
import com.litesuits.orm.db.assit.QueryBuilder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dllo on 16/7/5.
 */
public class RecentPlayFragment extends BaseFragment {

    private ImageView backBtn;
    private ListView listView;
    private RecentLvAdapter adapter;
    private PopupWindow popupWindow;
    private ArrayList<RecentPlay> recentPlays;

    private MyFavoriteSong myFavoriteSong;

    @Override
    public int setLayout() {
        return R.layout.fragmnet_recent_play;
    }

    @Override
    public void initView(View view) {
        backBtn = (ImageView) view.findViewById(R.id.recent_play_back_btn);
        listView = (ListView) view.findViewById(R.id.recent_play_listview);
    }

    @Override
    public void initData() {
        recentPlays = LiteOrmSingle.getInstance().getLiteOrm().query(RecentPlay.class);
        adapter = new RecentLvAdapter(context);
        adapter.setRecentPlays(recentPlays);
        listView.setAdapter(adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).returnToPrev();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String songId = recentPlays.get(position).getSongId();
                EventBus.getDefault().post(new SongIdEvent(songId));
                EventBus.getDefault().post(new IndexEvent(position));
                PlayListManager.getInstance().getPlayList().clear();
                for (RecentPlay recentPlay : recentPlays) {
                    PlayListManager.getInstance().getPlayList().add(new PlayList(recentPlay.getSongId(), recentPlay.getTitle(), recentPlay.getAuthor()));
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LiteOrmSingle.getInstance().getLiteOrm().delete(LiteOrmSingle.getInstance().getLiteOrm().query(RecentPlay.class).get(position));
                adapter.setRecentPlays(LiteOrmSingle.getInstance().getLiteOrm().query(RecentPlay.class));
                return false;
            }
        });

        // 更多
        adapter.setOnRecentGetMoreListener(new RecentLvAdapter.OnRecentGetMoreListener() {
            @Override
            public void onGetMore(int position) {
                showPopMore(recentPlays, position);
            }
        });
    }

    public void showPopMore(final ArrayList<RecentPlay> recentPlays, final int position) {
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

        title.setText(recentPlays.get(position).getTitle());

//        QueryBuilder<MyFavoriteSong> queryBuilder = new QueryBuilder<>(MyFavoriteSong.class);
//        queryBuilder.whereEquals("title", recentPlays.get(position).getTitle());
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
            myFavoriteSong.setTitle(recentPlays.get(position).getTitle());
            myFavoriteSong.setAuthor(recentPlays.get(position).getAuthor());
            myFavoriteSong.setSongId(recentPlays.get(position).getSongId());

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
            queryBuilderLocal.whereEquals("title", recentPlays.get(position).getTitle());
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
                    addToNet(recentPlays, position, bmobUser);
                }

                popupWindow.dismiss();
            }
        });
        // 下载
        downLoadIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadSingle.getSingleDownload().DownLoad(recentPlays.get(position).getSongId());
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

        popupWindow.showAtLocation(listView, Gravity.BOTTOM, 0, 0);
    }


    // 加入本地数据库
    public void addToLocal(int position) {
        QueryBuilder<MyFavoriteSong> queryBuilder = new QueryBuilder<>(MyFavoriteSong.class);
        queryBuilder.whereEquals("title", recentPlays.get(position).getTitle());
        if (LiteOrmSingle.getInstance().getLiteOrm().query(queryBuilder).size() == 0) {
            LiteOrmSingle.getInstance().getLiteOrm().insert(new MyFavoriteSong(recentPlays.get(position).getSongId(),
                    recentPlays.get(position).getTitle(), recentPlays.get(position).getAuthor()));
            Toast.makeText(MyApp.context, "已添加到我喜欢的单曲", Toast.LENGTH_SHORT).show();
        } else {
            LiteOrmSingle.getInstance().getLiteOrm().delete(LiteOrmSingle.getInstance().getLiteOrm().query(queryBuilder));
            Toast.makeText(MyApp.context, "已取消喜欢", Toast.LENGTH_SHORT).show();
        }
    }

    // 加入网络
    public void addToNet(final ArrayList<RecentPlay> recentPlays, int position, BmobUser bmobUser) {

        myFavoriteSong = new MyFavoriteSong();
        myFavoriteSong.setUserName(bmobUser.getUsername());
        myFavoriteSong.setTitle(recentPlays.get(position).getTitle());
        myFavoriteSong.setAuthor(recentPlays.get(position).getAuthor());
        myFavoriteSong.setSongId(recentPlays.get(position).getSongId());

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
