package com.lanou.baidumusicdemo.author;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.base.UrlValues;
import com.lanou.baidumusicdemo.main.ClickToAuthorType;
import com.lanou.baidumusicdemo.volley.GsonRequest;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

/**
 * Created by dllo on 16/6/26.
 */
public class AuthorAllFragment extends BaseFragment implements View.OnClickListener {

    private ViewPager viewPager;
    private AuthorHotVpAdapter adapter;
    private AuthorHotBean bean;
    private LinearLayout dots;
    private ImageView[] dotIvs;
    private TextView hotMore;
//    private LinearLayout chnMale,chnFemale, chnGroup,engMale,engFemale,engGroup,
//    krnMale,krnFemale,krnGroup,jpnMalem,jpnFemale,jpnGroup,other;
    private int[] ids = {R.id.author_chn_male,R.id.author_chn_female,R.id.author_chn_group, R.id.author_eng_male, R.id.author_eng_female,R.id.author_eng_group,
    R.id.author_krn_male,R.id.author_krn_female,R.id.author_krn_group,R.id.author_jpn_male,R.id.author_jpn_female,R.id.author_jpn_group,R.id.author_other };



    @Override
    public int setLayout() {
        return R.layout.fragment_author_all;
    }

    @Override
    public void initView(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.author_hot_vp);
        dots = (LinearLayout) view.findViewById(R.id.author_hot_dots);
        hotMore = (TextView) view.findViewById(R.id.author_all_hot_more_tv);
        hotMore.setOnClickListener(this);
        for (int i = 0; i < ids.length; i++) {
            view.findViewById(ids[i]).setOnClickListener(this);
        }

    }


    @Override
    public void initData() {
        adapter = new AuthorHotVpAdapter(context);

        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=12&offset=0&area=0&sex=0&abc=&from=ios&version=5.2.1&from=ios&channel=appstore";
        GsonRequest<AuthorHotBean> gsonRequest = new GsonRequest<AuthorHotBean>(url, AuthorHotBean.class, new Response.Listener<AuthorHotBean>() {
            @Override
            public void onResponse(AuthorHotBean response) {
                Log.d("AuthorAllFragment", "response.getArtist().size():" + response.getArtist().size());
                bean = response;
                adapter.setBean(bean);
                viewPager.setAdapter(adapter);
                // 加点
                dotIvs = new ImageView[bean.getArtist().size() / 3];
                for (int i = 0; i < bean.getArtist().size() / 3; i++) {
                    ImageView dot = new ImageView(context);
                    if (i== 0) {
                        dot.setImageResource(R.mipmap.ic_dot_default_selected);
                    } else {
                        dot.setImageResource(R.mipmap.ic_dot_default_unselected);
                    }
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    lp.rightMargin = 10;
                    lp.rightMargin = 10;
                    dotIvs[i] = dot;
                    dots.addView(dot, lp);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueueSingleton.getInstance(context).getRequestQueue().add(gsonRequest);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < bean.getArtist().size() / 3; i++) {
                    if (i == position){
                        dotIvs[i].setImageResource(R.mipmap.ic_dot_default_selected);
                    }else {
                        dotIvs[i].setImageResource(R.mipmap.ic_dot_default_unselected);
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 更多
            case R.id.author_all_hot_more_tv:

                break;
            // 华语男
            case R.id.author_chn_male:
                ((ClickToAuthorType)getActivity()).toAuthorType(UrlValues.CHN_MALE_URL);
                break;
            // 华语女
            case R.id.author_chn_female:
                ((ClickToAuthorType)getActivity()).toAuthorType(UrlValues.CHN_FEMALE_URL);
                break;
            // 华语组合
            case R.id.author_chn_group:
                ((ClickToAuthorType)getActivity()).toAuthorType(UrlValues.CHN_GROUP_URL);
                break;
            // 欧美男
            case R.id.author_eng_male:
                ((ClickToAuthorType)getActivity()).toAuthorType(UrlValues.ENG_MALE_URL);
                break;
            // 欧美女
            case R.id.author_eng_female:
                ((ClickToAuthorType)getActivity()).toAuthorType(UrlValues.ENG_FEMALE_URL);
                break;
            // 欧美组合
            case R.id.author_eng_group:
                ((ClickToAuthorType)getActivity()).toAuthorType(UrlValues.ENG_GROUP_URL);
                break;
            // 韩国男
            case R.id.author_krn_male:
                ((ClickToAuthorType)getActivity()).toAuthorType(UrlValues.KRN_MALE_URL);
                break;
            // 韩国女
            case R.id.author_krn_female:
                ((ClickToAuthorType)getActivity()).toAuthorType(UrlValues.KRN_FEMALE_URL);
                break;
            // 韩国组合
            case R.id.author_krn_group:
                ((ClickToAuthorType)getActivity()).toAuthorType(UrlValues.KRN_GROUP_URL);
                break;
            // 日本男
            case R.id.author_jpn_male:
                ((ClickToAuthorType)getActivity()).toAuthorType(UrlValues.JPN_MALE_URL);
                break;
            // 日本女
            case R.id.author_jpn_female:
                ((ClickToAuthorType)getActivity()).toAuthorType(UrlValues.JPN_FEMALE_URL);
                break;
            // 日本组合
            case R.id.author_jpn_group:
                ((ClickToAuthorType)getActivity()).toAuthorType(UrlValues.JPN_GROUP_URL);
                break;
            // 其他
            case R.id.author_other:
                ((ClickToAuthorType)getActivity()).toAuthorType(UrlValues.OTHER_AUTHOR);
                break;

        }
    }
}
