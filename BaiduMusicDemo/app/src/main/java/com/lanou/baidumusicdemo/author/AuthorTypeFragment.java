package com.lanou.baidumusicdemo.author;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.volley.GsonRequest;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

/**
 * Created by dllo on 16/6/27.
 */
public class AuthorTypeFragment extends BaseFragment {

    private ListView listView;
    private ImageView backBtn;
    private TextView authorType;
    private AuthorTypeLvAdapter adapter;
    private AuthorTypeBean bean;

    @Override
    public int setLayout() {
        return R.layout.fragment_author_type;
    }

    @Override
    public void initView(View view) {
        listView = (ListView) view.findViewById(R.id.author_type_list_view);
        backBtn = (ImageView) view.findViewById(R.id.author_type_back_btn);
        authorType = (TextView) view.findViewById(R.id.author_type_tv);
    }

    @Override
    public void initData() {
        adapter = new AuthorTypeLvAdapter(context);

        String typeUrl = getArguments().getString("typeUrl");
        GsonRequest<AuthorTypeBean> gsonRequest = new GsonRequest<AuthorTypeBean>(typeUrl, AuthorTypeBean.class, new Response.Listener<AuthorTypeBean>() {
            @Override
            public void onResponse(AuthorTypeBean response) {
                bean = response;
                adapter.setBean(bean);
                listView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueueSingleton.getInstance(context).getRequestQueue().add(gsonRequest);
    }
}
