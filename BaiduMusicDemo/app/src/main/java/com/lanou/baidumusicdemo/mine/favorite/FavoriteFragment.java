package com.lanou.baidumusicdemo.mine.favorite;

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
import com.lanou.baidumusicdemo.mine.recent.RecentLvAdapter;
import com.lanou.baidumusicdemo.service.IndexEvent;
import com.lanou.baidumusicdemo.service.PlayListManager;
import com.lanou.baidumusicdemo.service.SongIdEvent;
import com.litesuits.orm.db.assit.QueryBuilder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dllo on 16/7/6.
 */
public class FavoriteFragment extends BaseFragment {

    private ImageView backBtn;
    private ListView listView;
    private TextView songNum;
    private FavoriteLvAdapter adapter;
    private PopupWindow popupWindow;
    private ArrayList<MyFavoriteSong> favoriteSongs;
    private MyFavoriteSong myFavoriteSong;


    @Override
    public int setLayout() {
        return R.layout.fragment_favorite;
    }

    @Override
    public void initView(View view) {
        backBtn = (ImageView) view.findViewById(R.id.favorite_back_btn);
        listView = (ListView) view.findViewById(R.id.favorite_listview);
        songNum = (TextView) view.findViewById(R.id.favoite_total_num);
    }

    @Override
    public void initData() {
        adapter = new FavoriteLvAdapter(context);

        BmobUser bmobUser = BmobUser.getCurrentUser(context);
        if (bmobUser == null){
            favoriteSongs = LiteOrmSingle.getInstance().getLiteOrm().query(MyFavoriteSong.class);
            songNum.setText("共"+favoriteSongs.size()+"首");
            adapter.setFavoriteSongs(favoriteSongs);
        } else {
            favoriteSongs = FavoriteManager.getInstance().getFavoriteSongs();
            songNum.setText("共" + FavoriteManager.getInstance().getFavoriteSongs().size() + "首");
            adapter.setFavoriteSongs(FavoriteManager.getInstance().getFavoriteSongs());
        }

        listView.setAdapter(adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).returnToPrev();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<MyFavoriteSong> f = LiteOrmSingle.getInstance().getLiteOrm().query(MyFavoriteSong.class);
                String songId = favoriteSongs.get(position).getSongId();
                EventBus.getDefault().post(new SongIdEvent(songId));
                EventBus.getDefault().post(new IndexEvent(position));
                PlayListManager.getInstance().getPlayList().clear();
                for (MyFavoriteSong favoriteSong : favoriteSongs) {
                    PlayListManager.getInstance().getPlayList().add(new PlayList(favoriteSong.getSongId(), favoriteSong.getTitle(), favoriteSong.getAuthor()));
                }
            }
        });

        adapter.setOnFavoriteGetMoreListener(new FavoriteLvAdapter.OnFavoriteGetMoreListener() {
            @Override
            public void onGetMore(int position) {
                showPopMore(favoriteSongs, position);
            }
        });
    }

    public void showPopMore(final ArrayList<MyFavoriteSong> favoriteSongs, final int position){
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

        title.setText(favoriteSongs.get(position).getTitle());

//        QueryBuilder<MyFavoriteSong> queryBuilder = new QueryBuilder<>(MyFavoriteSong.class);
//        queryBuilder.whereEquals("title", favoriteSongs.get(position).getTitle());
//        if (LiteOrmSingle.getInstance().getLiteOrm().query(queryBuilder).size() == 0){
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
            myFavoriteSong.setTitle(favoriteSongs.get(position).getTitle());
            myFavoriteSong.setAuthor(favoriteSongs.get(position).getAuthor());
            myFavoriteSong.setSongId(favoriteSongs.get(position).getSongId());

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
            queryBuilderLocal.whereEquals("title", favoriteSongs.get(position).getTitle());
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
                BmobUser bmobUser1 = BmobUser.getCurrentUser(context);
                if (bmobUser1 == null) {
                    addToLocal(position);
                    songNum.setText("共"+LiteOrmSingle.getInstance().getLiteOrm().query(MyFavoriteSong.class).size()+"首");
                    adapter.setFavoriteSongs(LiteOrmSingle.getInstance().getLiteOrm().query(MyFavoriteSong.class));
                    // 通知刷新
                    Intent intent = new Intent("REFRESH_MINE");
                    context.sendBroadcast(intent);
                } else {
                    addToNet(favoriteSongs, position, bmobUser1);
                }

                popupWindow.dismiss();

            }
        });
        // 下载
        downLoadIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadSingle.getSingleDownload().DownLoad(favoriteSongs.get(position).getSongId());
            }
        });
        // 分享
        shareIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // 添加
        addIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share.share(context);
            }
        });
        // 点击空白
        space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(listView, Gravity.BOTTOM, 0 , 0);
    }

    // 加入本地数据库
    public void addToLocal(int position) {
        QueryBuilder<MyFavoriteSong> queryBuilder = new QueryBuilder<>(MyFavoriteSong.class);
        queryBuilder.whereEquals("title", favoriteSongs.get(position).getTitle());
        if (LiteOrmSingle.getInstance().getLiteOrm().query(queryBuilder).size() == 0) {
            LiteOrmSingle.getInstance().getLiteOrm().insert(new MyFavoriteSong(favoriteSongs.get(position).getSongId(),
                    favoriteSongs.get(position).getTitle(), favoriteSongs.get(position).getAuthor()));
            Toast.makeText(MyApp.context, "已添加到我喜欢的单曲", Toast.LENGTH_SHORT).show();
        } else {
            LiteOrmSingle.getInstance().getLiteOrm().delete(LiteOrmSingle.getInstance().getLiteOrm().query(queryBuilder));
            Toast.makeText(MyApp.context, "已取消喜欢", Toast.LENGTH_SHORT).show();
        }
    }

    // 加入网络
    public void addToNet(final ArrayList<MyFavoriteSong> favoriteSongs, int position, BmobUser bmobUser) {

        myFavoriteSong = new MyFavoriteSong();
        myFavoriteSong.setUserName(bmobUser.getUsername());
        myFavoriteSong.setTitle(favoriteSongs.get(position).getTitle());
        myFavoriteSong.setAuthor(favoriteSongs.get(position).getAuthor());
        myFavoriteSong.setSongId(favoriteSongs.get(position).getSongId());

        int number = 0;
        for (MyFavoriteSong favoriteSong : FavoriteManager.getInstance().getFavoriteSongs()) {
            if (favoriteSong.getTitle().equals(myFavoriteSong.getTitle())) {
                number = number + 1;
//                myFavoriteSong.setObjectId(favoriteSong.getObjectId());
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
                    // 通知刷新
                    Intent intent = new Intent("REFRESH_MINE");
                    context.sendBroadcast(intent);

                    songNum.setText("共" + FavoriteManager.getInstance().getFavoriteSongs().size() + "首");
                    adapter.setFavoriteSongs(FavoriteManager.getInstance().getFavoriteSongs());
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
                    // 通知刷新
                    Intent intent = new Intent("REFRESH_MINE");
                    context.sendBroadcast(intent);

                    songNum.setText("共" + FavoriteManager.getInstance().getFavoriteSongs().size() + "首");
                    adapter.setFavoriteSongs(FavoriteManager.getInstance().getFavoriteSongs());
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.d("RecentPlayFragment", "del"+s);
                }
            });
        }
    }
}
