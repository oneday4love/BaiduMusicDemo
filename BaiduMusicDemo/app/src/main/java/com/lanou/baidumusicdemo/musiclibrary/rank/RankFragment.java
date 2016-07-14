package com.lanou.baidumusicdemo.musiclibrary.rank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.base.UrlValues;
import com.lanou.baidumusicdemo.main.ClickToRankDetail;
import com.lanou.baidumusicdemo.main.MainActivity;
import com.lanou.baidumusicdemo.musiclibrary.rank.rankdetail.RankDetailFragment;
import com.lanou.baidumusicdemo.volley.GsonRequest;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

/**
 * Created by dllo on 16/6/17.
 */
public class RankFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView rankListView;
    private RankListViewAdapter adapter;
    private RankBean rankBean;

    private String url = UrlValues.RANK_URL;

    @Override
    public int setLayout() {
        return R.layout.fragment_rank;
    }

    @Override
    public void initView(View view) {
        rankListView = (ListView) view.findViewById(R.id.rank_list_view);
        adapter = new RankListViewAdapter(context);
    }

    @Override
    public void initData() {
        GsonRequest gsonRequest = new GsonRequest<RankBean>(url, RankBean.class, new Response.Listener<RankBean>() {
            @Override
            public void onResponse(RankBean response) {
                rankBean = response;
                adapter.setBean(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueueSingleton.getInstance(context).getRequestQueue().add(gsonRequest);
        rankListView.setAdapter(adapter);

        rankListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int rankType = rankBean.getContent().get(position).getType();
        ((ClickToRankDetail)getActivity()).toRankDetail(rankType);
    }

}
