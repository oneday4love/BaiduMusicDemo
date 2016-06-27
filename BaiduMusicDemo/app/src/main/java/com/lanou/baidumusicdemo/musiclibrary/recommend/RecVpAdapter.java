package com.lanou.baidumusicdemo.musiclibrary.recommend;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.lanou.baidumusicdemo.MyApp;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;
import com.squareup.picasso.Picasso;

/**
 * Created by dllo on 16/6/22.
 */
public class RecVpAdapter extends PagerAdapter {

    private RecVpBean bean;
    private OnRecVpListener onRecVpListener;

    public void setOnRecVpListener(OnRecVpListener onRecVpListener) {
        this.onRecVpListener = onRecVpListener;
    }

    public void setBean(RecVpBean bean) {
        this.bean = bean;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return bean == null ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(MyApp.context);
        RequestQueueSingleton.getInstance(MyApp.context).getImageLoader().get(bean.getPic().get(position % bean.getPic().size()).getRandpic(),
                ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecVpListener.onItemClick(position);
            }
        });
        imageView.setPadding(15, 15, 0, 0);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (container.getChildAt(position % bean.getPic().size()) == object) {
            container.removeViewAt(position % bean.getPic().size());
        }
    }

    interface OnRecVpListener{
        void onItemClick(int position);
    }
}
