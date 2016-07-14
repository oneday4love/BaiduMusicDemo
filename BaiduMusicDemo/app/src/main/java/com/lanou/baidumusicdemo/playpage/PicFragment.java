package com.lanou.baidumusicdemo.playpage;

import android.view.View;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.service.SongPlayBean;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by dllo on 16/6/28.
 */
public class PicFragment extends BaseFragment {

    private ImageView playPageCover;

    @Override
    public int setLayout() {
        EventBus.getDefault().register(this);
        return R.layout.fragment_pic;
    }

    @Override
    public void initView(View view) {
        playPageCover = (ImageView) view.findViewById(R.id.play_page_pic);
    }

    @Override
    public void initData() {
        String pic = getActivity().getIntent().getStringExtra("songPlayBeanPic");
        RequestQueueSingleton.getInstance(context).getImageLoader().get(pic,
                ImageLoader.getImageListener(playPageCover, R.mipmap.icon_cover_large, R.mipmap.icon_cover_large));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void  initPic(SongPlayBean songPlayBean){
        RequestQueueSingleton.getInstance(context).getImageLoader().get(songPlayBean.getSonginfo().getPic_premium(),
                ImageLoader.getImageListener(playPageCover, R.mipmap.icon_cover_large, R.mipmap.icon_cover_large));
    }


}
