package com.lanou.baidumusicdemo.search;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.db.PlayList;
import com.lanou.baidumusicdemo.main.ClickToSearch;
import com.lanou.baidumusicdemo.main.MainActivity;
import com.lanou.baidumusicdemo.service.IndexEvent;
import com.lanou.baidumusicdemo.service.PlayListManager;
import com.lanou.baidumusicdemo.service.SongIdEvent;
import com.lanou.baidumusicdemo.volley.GsonRequest;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by dllo on 16/7/5.
 */
public class SearchFragment extends BaseFragment implements View.OnClickListener {

    private ImageView backBtn, searchBtn;
    private EditText editText;
    private ListView songListView;
    private SearchSongLvAdapter adapter;
    private SearchBean bean;

    private String url;

    @Override
    public int setLayout() {
        return R.layout.fragment_search;
    }

    @Override
    public void initView(View view) {
        backBtn = (ImageView) view.findViewById(R.id.search_back_btn);
        searchBtn = (ImageView) view.findViewById(R.id.search_btn);
        editText = (EditText) view.findViewById(R.id.search_et);
        songListView = (ListView) view.findViewById(R.id.search_listview);
    }

    @Override
    public void initData() {
        backBtn.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
        adapter = new SearchSongLvAdapter(context);

        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EventBus.getDefault().post(new SongIdEvent(bean.getResult().getSong_info().getSong_list().get(position).getSong_id()));
                EventBus.getDefault().post(new IndexEvent(position));
                PlayListManager.getInstance().getPlayList().clear();
                for (SearchBean.ResultBean.SongInfoBean.SongListBean songListBean : bean.getResult().getSong_info().getSong_list()) {
                    PlayListManager.getInstance().getPlayList().add(new PlayList(songListBean.getSong_id(), songListBean.getTitle(), songListBean.getAuthor()));
                }
            }
        });

    }

    public void search (){
        String query = editText.getText().toString();
        url = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.search.merge&query="+ query +"&page_size=50&page_no=1&type=-1&format=json&from=ios&version=5.2.5&from=ios&channel=appstore";
        GsonRequest<SearchBean> gsonRequest = new GsonRequest<SearchBean>(url, SearchBean.class, new Response.Listener<SearchBean>() {
            @Override
            public void onResponse(SearchBean response) {
                bean = response;
                adapter.setBean(bean);
                songListView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueueSingleton.getInstance(context).getRequestQueue().add(gsonRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 返回
            case R.id.search_back_btn:
                ((MainActivity)getActivity()).returnToPrev();
                break;
            // 搜索
            case R.id.search_btn:
                if (editText.getText() != null){
                    search();
                }
                break;
        }
    }
}
