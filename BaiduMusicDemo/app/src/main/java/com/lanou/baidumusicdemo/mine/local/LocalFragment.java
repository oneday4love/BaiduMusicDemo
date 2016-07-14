package com.lanou.baidumusicdemo.mine.local;

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
import com.lanou.baidumusicdemo.main.MainActivity;
import com.lanou.baidumusicdemo.main.Share;
import com.lanou.baidumusicdemo.mine.download.DownloadSingle;
import com.litesuits.orm.db.assit.QueryBuilder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by dllo on 16/7/7.
 */
public class LocalFragment extends BaseFragment {

    private ImageView backBtn;
    private ListView listView;
    private TextView songNum;
    private LocalLvAdapter adapter;
    private PopupWindow popupWindow;
    private ArrayList<LocalSongBean> locals;

    @Override
    public int setLayout() {
        return R.layout.fragment_local;
    }

    @Override
    public void initView(View view) {
        backBtn = (ImageView) view.findViewById(R.id.local_back_btn);
        listView = (ListView) view.findViewById(R.id.local_listview);
        songNum = (TextView) view.findViewById(R.id.local_total_num);
    }

    @Override
    public void initData() {
        ScanMusic.query(context);
        locals = LiteOrmSingle.getInstance().getLiteOrm().query(LocalSongBean.class);
        songNum.setText("共"+locals.size()+ "首");
        adapter = new LocalLvAdapter(context);
        adapter.setLocals(locals);
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
                EventBus.getDefault().post(LiteOrmSingle.getInstance().getLiteOrm().query(LocalSongBean.class).get(position));
                Log.d("LocalFragment", "LiteOrmSingle.getInstance().getLiteOrm().query(LocalSongBean.class).size():" + LiteOrmSingle.getInstance().getLiteOrm().query(LocalSongBean.class).get(position).getPath());
            }
        });

        adapter.setOnLocalGetMoreListener(new LocalLvAdapter.OnLocalGetMoreListener() {
            @Override
            public void onGetMore(int position) {
                showPopMore(locals,position);
            }
        });
    }

    public void showPopMore(final ArrayList<LocalSongBean> locals, final int position){
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_detail_more,null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        TextView title = (TextView) contentView.findViewById(R.id.pop_detail_more_title);
        ImageView likeIv = (ImageView) contentView.findViewById(R.id.pop_detail_like_iv);
        ImageView downLoadIv = (ImageView) contentView.findViewById(R.id.pop_detail_download_iv);
        TextView downloadTv = (TextView) contentView.findViewById(R.id.pop_detail_download_tv);
        ImageView shareIv = (ImageView) contentView.findViewById(R.id.pop_detail_share_iv);
        ImageView addIv = (ImageView) contentView.findViewById(R.id.pop_detail_add_iv);
        FrameLayout space = (FrameLayout) contentView.findViewById(R.id.pop_detail_space);
        downLoadIv.setVisibility(View.GONE);
        downloadTv.setVisibility(View.GONE);

        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        title.setText(locals.get(position).getTitle());

        QueryBuilder<MyFavoriteSong> queryBuilder = new QueryBuilder<>(MyFavoriteSong.class);
        queryBuilder.whereEquals("title", locals.get(position).getTitle());
        if (LiteOrmSingle.getInstance().getLiteOrm().query(queryBuilder).size() == 0){

            likeIv.setImageResource(R.mipmap.cust_dialog_hart);

        } else {
            likeIv.setImageResource(R.mipmap.cust_heart_press);
        }

        // 喜欢
        likeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QueryBuilder<MyFavoriteSong> queryBuilder = new QueryBuilder<>(MyFavoriteSong.class);
                queryBuilder.whereEquals("title", locals.get(position).getTitle());
                if (LiteOrmSingle.getInstance().getLiteOrm().query(queryBuilder).size() == 0){
                    LiteOrmSingle.getInstance().getLiteOrm().insert(new MyFavoriteSong(locals.get(position).getTitle(),
                            locals.get(position).getAuthor()));
                    Toast.makeText(MyApp.context, "已添加到我喜欢的单曲", Toast.LENGTH_SHORT).show();
                } else {
                    LiteOrmSingle.getInstance().getLiteOrm().delete(LiteOrmSingle.getInstance().getLiteOrm().query(queryBuilder));
                    Toast.makeText(MyApp.context, "已取消喜欢", Toast.LENGTH_SHORT).show();
                }
                // 刷新
                Intent intent = new Intent("REFRESH_MINE");
                context.sendBroadcast(intent);

                popupWindow.dismiss();

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

        popupWindow.showAtLocation(listView, Gravity.BOTTOM, 0 , 0);
    }
}
