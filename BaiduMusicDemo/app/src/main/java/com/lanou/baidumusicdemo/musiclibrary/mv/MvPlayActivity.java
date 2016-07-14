package com.lanou.baidumusicdemo.musiclibrary.mv;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseActivity;
import com.lanou.baidumusicdemo.base.UrlValues;
import com.lanou.baidumusicdemo.volley.GsonRequest;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

import java.io.IOException;


/**
 * Created by dllo on 16/7/4.
 */
public class MvPlayActivity extends BaseActivity
//        implements MediaPlayer.OnCompletionListener,
//        MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener,
//        MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener,
//        MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback
{

    private Display currDisplay;
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private MediaPlayer player;
    private int vWidth,vHeight;
    private MvPlayBean bean;
    private VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitty_mv_play);

        videoView = (VideoView) findViewById(R.id.mv_play_videoview);

        String mvId = getIntent().getStringExtra("mvId");
        String url = UrlValues.MV_PLAY_FRONT_URL + mvId + UrlValues.MV_PLAY_BEHIND_URL;
        GsonRequest<MvPlayBean> gsonRequest = new GsonRequest<MvPlayBean>(url, MvPlayBean.class, new Response.Listener<MvPlayBean>() {
            @Override
            public void onResponse(MvPlayBean response) {
                bean = response;
                String playUrl = bean.getResult().getVideo_info().getSourcepath().substring(31);
                playUrl = playUrl.replace(".html","");
                playUrl = "http://www.yinyuetai.com/mv/video-url/" + playUrl;
                Uri uri = Uri.parse(playUrl);
                Log.d("MvPlayActivity", "uri:" + uri);
                videoView.setMediaController(new MediaController(MvPlayActivity.this));
                videoView.setVideoURI(uri);
                videoView.start();
                videoView.requestFocus();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueueSingleton.getInstance(this).getRequestQueue().add(gsonRequest);



//        surfaceView = (SurfaceView) findViewById(R.id.mv_play_surface_view);
//        //给SurfaceView添加CallBack监听
//        holder = surfaceView.getHolder();
//        holder.addCallback(this);
//        //为了可以播放视频或者使用Camera预览，我们需要指定其Buffer类型
//        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//
//        //下面开始实例化MediaPlayer对象
//        player = new MediaPlayer();
//        player.setOnCompletionListener(this);
//        player.setOnErrorListener(this);
//        player.setOnInfoListener(this);
//        player.setOnPreparedListener(this);
//        player.setOnSeekCompleteListener(this);
//        player.setOnVideoSizeChangedListener(this);
//
//        String mvId = getIntent().getStringExtra("mvId");
//        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.7.3.0&channel=xiaomi&operator=3&provider=11%2C12&method=baidu.ting.mv.playMV&format=json&mv_id="+ mvId + "&song_id=&definition=0";
//        GsonRequest<MvPlayBean> gsonRequest = new GsonRequest<MvPlayBean>(url, MvPlayBean.class, new Response.Listener<MvPlayBean>() {
//            @Override
//            public void onResponse(MvPlayBean response) {
//                bean = response;
//                try {
//                    player.setDataSource(response.getResult().getFiles().getValue51().getFile_link());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        RequestQueueSingleton.getInstance(this).getRequestQueue().add(gsonRequest);
//
//        currDisplay = getWindowManager().getDefaultDisplay();
//
//    }
//
//    @Override
//    public void onCompletion(MediaPlayer mp) {
//        finish();
//    }
//
//    @Override
//    public boolean onError(MediaPlayer mp, int what, int extra) {
//        return false;
//    }
//
//    @Override
//    public boolean onInfo(MediaPlayer mp, int what, int extra) {
//        // 当一些特定信息出现或者警告时触发
//        switch(what){
//            case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
//                break;
//            case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
//                break;
//            case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
//                break;
//            case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
//                break;
//        }
//        return false;
//    }
//
//    @Override
//    public void onPrepared(MediaPlayer mp) {
//        // 当prepare完成后，该方法触发，在这里我们播放视频
//
//        //首先取得video的宽和高
//        vWidth = player.getVideoWidth();
//        vHeight = player.getVideoHeight();
//
//        if(vWidth > currDisplay.getWidth() || vHeight > currDisplay.getHeight()){
//            //如果video的宽或者高超出了当前屏幕的大小，则要进行缩放
//            float wRatio = (float)vWidth/(float)currDisplay.getWidth();
//            float hRatio = (float)vHeight/(float)currDisplay.getHeight();
//
//            //选择大的一个进行缩放
//            float ratio = Math.max(wRatio, hRatio);
//
//            vWidth = (int)Math.ceil((float)vWidth/ratio);
//            vHeight = (int)Math.ceil((float)vHeight/ratio);
//
//            //设置surfaceView的布局参数
//            surfaceView.setLayoutParams(new LinearLayout.LayoutParams(vWidth, vHeight));
//
//            //然后开始播放视频
//
//            player.start();
//        }
//    }
//
//    @Override
//    public void onSeekComplete(MediaPlayer mp) {
//
//    }
//
//    @Override
//    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        // 当SurfaceView中的Surface被创建的时候被调用
//        //在这里我们指定MediaPlayer在当前的Surface中进行播放
//        player.setDisplay(holder);
//        //在指定了MediaPlayer播放的容器后，我们就可以使用prepare或者prepareAsync来准备播放了
//        player.prepareAsync();
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//
//    }
    }
}
