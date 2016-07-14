package com.lanou.baidumusicdemo.musiclibrary.radio.all_radio;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.base.UrlValues;
import com.lanou.baidumusicdemo.musiclibrary.radio.all_radio.radio_play.RadioPlayActivity;
import com.lanou.baidumusicdemo.volley.GsonRequest;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

/**
 * Created by dllo on 16/6/24.
 */
public class AllRadioFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private AllRadioRvAdapter adapter;
    private AllRadioBean bean;

    @Override
    public int setLayout() {
        return R.layout.fragment_all_radio;
    }

    @Override
    public void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.all_radio_rv);
    }

    @Override
    public void initData() {
        adapter = new AllRadioRvAdapter(context);

        int id = getArguments().getInt("id");

        String url = UrlValues.RADIO_ALL_FRONT_URL +id+UrlValues.RADIO_ALL_BEHIND_URL;
        GsonRequest<AllRadioBean> gsonRequest = new GsonRequest<AllRadioBean>(url, AllRadioBean.class,
                new Response.Listener<AllRadioBean>() {
            @Override
            public void onResponse(AllRadioBean response) {
                bean = response;
                adapter.setBean(response);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(context, 3));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueueSingleton.getInstance(context).getRequestQueue().add(gsonRequest);

        // 点击到电台播放界面
        adapter.setClickToRadioPlay(new AllRadioRvAdapter.ClickToRadioPlay() {
            @Override
            public void onItemClick(int position) {
                String sceneId = bean.getResult().get(position).getScene_id();
                Intent intent = new Intent(context, RadioPlayActivity.class);
                intent.putExtra("sceneId", sceneId);
                intent.putExtra("sceneName",bean.getResult().get(position).getScene_name());
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
