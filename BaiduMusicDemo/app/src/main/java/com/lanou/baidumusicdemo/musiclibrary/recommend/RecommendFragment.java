package com.lanou.baidumusicdemo.musiclibrary.recommend;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.main.ClickToAllAuthor;
import com.lanou.baidumusicdemo.main.ClickToSongListDetail;
import com.lanou.baidumusicdemo.main.MainActivity;
import com.lanou.baidumusicdemo.volley.GsonRequest;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

import java.util.Timer;

/**
 * Created by dllo on 16/6/17.
 */
public class RecommendFragment extends BaseFragment implements View.OnClickListener {

    private ViewPager recVp;
    private RecVpAdapter vpAdapter;
    private RecRvAdapter rvAdapter;
    private RecyclerView recRv;
    private LinearLayout dots;
    private ImageView[] dotIvs;
    private RecRvBean rvBean;
    private RecVpBean vpBean;

    private ImageView allAuthor;

    @Override
    public int setLayout() {
        return R.layout.fragment_recommend;
    }

    @Override
    public void initView(View view) {
        recVp = (ViewPager) view.findViewById(R.id.recommend_vp);
        dots = (LinearLayout) view.findViewById(R.id.recommend_dots);
        recRv = (RecyclerView) view.findViewById(R.id.rec_rv);
        allAuthor = (ImageView) view.findViewById(R.id.rec_all_author);
    }

    @Override
    public void initData() {

        allAuthor.setOnClickListener(this);

        rvAdapter = new RecRvAdapter(context);
        vpAdapter = new RecVpAdapter();

        //解析轮播图
        String vpUrl = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.plaza.getFocusPic&format=json&from=ios&version=5.2.3&from=ios&channel=appstore";
        GsonRequest<RecVpBean> gsonRequest = new GsonRequest<RecVpBean>(vpUrl, RecVpBean.class, new Response.Listener<RecVpBean>() {
            @Override
            public void onResponse(RecVpBean response) {
                vpAdapter.setBean(response);
                vpBean = response;
                // 加点
                dotIvs = new ImageView[response.getPic().size()];
                for (int i = 0; i < dotIvs.length; i++) {
                    ImageView dotIv = new ImageView(context);
                    if (i == 0) {
                        dotIv.setImageResource(R.mipmap.ic_dot_default_selected);
                    } else {
                        dotIv.setImageResource(R.mipmap.ic_dot_default_unselected);
                    }
                    dotIvs[i] = dotIv;
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    lp.leftMargin = 10;
                    lp.rightMargin = 10;
                    dots.addView(dotIv,lp);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueueSingleton.getInstance(context).getRequestQueue().add(gsonRequest);
        recVp.setAdapter(vpAdapter);

        recVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotIvs.length; i++) {
                    if (i == position % dotIvs.length){
                        dotIvs[i].setImageResource(R.mipmap.ic_dot_default_selected);
                    } else {
                        dotIvs[i].setImageResource(R.mipmap.ic_dot_default_unselected);
                    }
                }

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        vpAdapter.setOnRecVpListener(new RecVpAdapter.OnRecVpListener() {
            @Override
            public void onItemClick(int position) {
                MainActivity mainActivity = (MainActivity) getActivity();
                int pos = position % vpBean.getPic().size();
                mainActivity.toRankDetail(vpBean.getPic().get(pos).getType());
            }
        });


        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                recVp.setCurrentItem(recVp.getCurrentItem() + 1);
                handler.postDelayed(this, 3500);
            }
        };
        handler.postDelayed(runnable, 3500);

        // 解析推荐歌单
        String rvUrl = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.diy.getHotGeDanAndOfficial&num=6&version=5.2.3&from=ios&channel=appstore";
        GsonRequest<RecRvBean> RvGsonRequest = new GsonRequest<RecRvBean>(rvUrl, RecRvBean.class, new Response.Listener<RecRvBean>() {
            @Override
            public void onResponse(RecRvBean response) {
                rvAdapter.setBean(response);
                rvBean = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueueSingleton.getInstance(context).getRequestQueue().add(RvGsonRequest);
        recRv.setAdapter(rvAdapter);
        recRv.setLayoutManager(new GridLayoutManager(context, 3));
        recRv.setFocusable(false); // 取消焦点

        // RV点击
        rvAdapter.setOnRecRvListener(new RecRvAdapter.OnRecRvListener() {
            @Override
            public void onItemClick(int position) {
                ((ClickToSongListDetail)getActivity()).toSongListDetail(rvBean.getContent().getList().get(position).getListid());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 全部歌手
            case R.id.rec_all_author:
                ((ClickToAllAuthor)getActivity()).toAllAuthor();
                break;
        }
    }

}
