package com.lanou.baidumusicdemo.playpage.lrc;

import android.util.Log;
import android.view.View;

import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.service.ProgressEvent;
import com.lanou.baidumusicdemo.service.SongPlayBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by dllo on 16/6/28.
 */
public class LrcFragment extends BaseFragment {

    private LyricView lyricView;

    @Override
    public int setLayout() {
        EventBus.getDefault().register(this);
        return R.layout.fragment_lrc;
    }

    @Override
    public void initView(View view) {
        lyricView = (LyricView) view.findViewById(R.id.lrc_view);
    }

    @Override
    public void initData() {
        String lrc = getActivity().getIntent().getStringExtra("songPlayBeanLrc");
        if (lrc != null) {
            lyricView.loadLrc(lrc);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

//    public void loadLrc(String lrc) {
//        lyricView.loadLrc(lrc);
//    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SeeBarControl(SongPlayBean bean) {
        lyricView.loadLrc(bean.getSonginfo().getLrclink());
    }

    //EventBus接收消息设置时间
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void radioPlayTime(ProgressEvent progressEvent) {
        if (lyricView.hasLrc()) {
            lyricView.updateTime(progressEvent.getProgress());
        }
    }
}
