package com.lanou.baidumusicdemo.author;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

/**
 * Created by dllo on 16/6/27.
 */
public class AuthorTypeLvAdapter extends BaseAdapter {

    private Context context;
    private AuthorTypeBean bean;

    public AuthorTypeLvAdapter(Context context) {
        this.context = context;
    }

    public void setBean(AuthorTypeBean bean) {
        this.bean = bean;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return bean == null ? 0 : bean.getArtist().size();
    }

    @Override
    public Object getItem(int position) {
        return bean == null ? null : bean.getArtist().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null ){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_author_type_list_view, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.artist.setText(bean.getArtist().get(position).getName());
        RequestQueueSingleton.getInstance(context).getImageLoader().get(bean.getArtist().get(position).getAvatar_big(),
                ImageLoader.getImageListener(holder.cover, R.mipmap.minibar_default_img,R.mipmap.minibar_default_img));
        return convertView;
    }

    class ViewHolder {
        ImageView cover;
        TextView artist;
        public ViewHolder(View view) {
            cover = (ImageView) view.findViewById(R.id.item_author_type_cover);
            artist = (TextView) view.findViewById(R.id.item_author_type_artist);
        }
    }
}
