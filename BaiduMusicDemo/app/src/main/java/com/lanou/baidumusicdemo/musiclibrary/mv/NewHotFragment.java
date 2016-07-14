package com.lanou.baidumusicdemo.musiclibrary.mv;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.main.MainActivity;
import com.lanou.baidumusicdemo.volley.GsonRequest;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

/**
 * Created by dllo on 16/7/2.
 */
public class NewHotFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private MvRvAdapter adapter;
    private MvBean bean;



    @Override
    public int setLayout() {
        return R.layout.fragment_new_hot;
    }

    @Override
    public void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.mv_rv);
    }

    @Override
    public void initData() {

        String url = getArguments().getString("url");
        adapter = new MvRvAdapter(context);

        // 解析
        GsonRequest<MvBean> gsonRequest = new GsonRequest<MvBean>(url, MvBean.class, new Response.Listener<MvBean>() {
            @Override
            public void onResponse(MvBean response) {
                bean = response;
                adapter.setBean(bean);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueueSingleton.getInstance(context).getRequestQueue().add(gsonRequest);

        // 点击
        adapter.setOnItemClickListener(new MvRvAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                String mvId = bean.getResult().getMv_list().get(position).getMv_id();
                Intent intent = new Intent(context, MvPlayActivity.class);
                intent.putExtra("mvId", mvId);
                startActivity(intent);
            }
        });
    }
}
