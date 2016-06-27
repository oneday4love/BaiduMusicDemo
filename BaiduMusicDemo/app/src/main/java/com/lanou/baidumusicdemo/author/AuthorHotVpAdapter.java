package com.lanou.baidumusicdemo.author;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.lanou.baidumusicdemo.MyApp;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

/**
 * Created by dllo on 16/6/27.
 */
public class AuthorHotVpAdapter extends PagerAdapter {

    private Context context;
    private AuthorHotBean bean;

    public AuthorHotVpAdapter(Context context) {
        this.context = context;
    }

    public void setBean(AuthorHotBean bean) {
        this.bean = bean;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return bean == null ? 0 : bean.getArtist().size() / 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LinearLayout item = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.author_hot_vp_item, container, false);
        ImageView img1 = (ImageView) item.findViewById(R.id.item_img1);
        ImageView img2 = (ImageView) item.findViewById(R.id.item_img2);
        ImageView img3 = (ImageView) item.findViewById(R.id.item_img3);
        TextView author1 = (TextView) item.findViewById(R.id.item_author1);
        TextView author2 = (TextView) item.findViewById(R.id.item_author2);
        TextView author3 = (TextView) item.findViewById(R.id.item_author3);
        int pos = position * 3;
        author1.setText(bean.getArtist().get(pos).getName());
        author2.setText(bean.getArtist().get(pos + 1).getName());
        author3.setText(bean.getArtist().get(pos + 2).getName());
        RequestQueueSingleton.getInstance(MyApp.context).getImageLoader().get(bean.getArtist().get(pos).getAvatar_big(),
                ImageLoader.getImageListener(img1, R.mipmap.default_artist, R.mipmap.default_artist));
        RequestQueueSingleton.getInstance(context).getImageLoader().get(bean.getArtist().get(pos + 1).getAvatar_big(),
                ImageLoader.getImageListener(img2, R.mipmap.default_artist, R.mipmap.default_artist));
        RequestQueueSingleton.getInstance(context).getImageLoader().get(bean.getArtist().get(pos + 2).getAvatar_big(),
                ImageLoader.getImageListener(img3, R.mipmap.default_artist, R.mipmap.default_artist));

        container.addView(item);
        return item;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}
