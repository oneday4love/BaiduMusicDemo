package com.lanou.baidumusicdemo.musiclibrary.radio.all_radio.radio_play;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.service.SongPlayBean;
import com.lanou.baidumusicdemo.volley.GsonRequest;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by dllo on 16/6/25.
 */
public class RadioPlayFragment extends BaseFragment {

    private RadioPlayBean bean;
    private ImageView cover, playBtn;
    private TextView title, author;

    @Override
    public int setLayout() {
        EventBus.getDefault().register(this);
        return R.layout.fragment_radio_play;
    }

    @Override
    public void initView(View view) {
        cover = (ImageView) view.findViewById(R.id.radio_play_cover);
        playBtn = (ImageView) view.findViewById(R.id.radio_play_pause_play_btn);
        title = (TextView) view.findViewById(R.id.radio_play_title);
        author = (TextView) view.findViewById(R.id.radio_play_author);
    }

    @Override
    public void initData() {


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void initRadioPlay(SongPlayBean songPlayBean){
        title.setText(songPlayBean.getSonginfo().getTitle());
        author.setText(songPlayBean.getSonginfo().getAuthor());
        RequestQueueSingleton.getInstance(context).getImageLoader().get(songPlayBean.getSonginfo().getPic_huge(),
                ImageLoader.getImageListener(cover, R.mipmap.radio_play_default_img, R.mipmap.radio_play_default_img));
    }
}
