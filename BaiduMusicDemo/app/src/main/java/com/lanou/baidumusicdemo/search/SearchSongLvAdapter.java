package com.lanou.baidumusicdemo.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanou.baidumusicdemo.R;

/**
 * Created by dllo on 16/7/5.
 */
public class SearchSongLvAdapter extends BaseAdapter {
    private Context context;
    private SearchBean bean;

    public SearchSongLvAdapter(Context context) {
        this.context = context;
    }

    public void setBean(SearchBean bean) {
        this.bean = bean;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return bean == null ? 0 : bean.getResult().getSong_info().getSong_list().size();
    }

    @Override
    public Object getItem(int position) {
        return bean == null ? null : bean.getResult().getSong_info().getSong_list().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_song_lv, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(bean.getResult().getSong_info().getSong_list().get(position).getTitle());
        holder.desc.setText(bean.getResult().getSong_info().getSong_list().get(position).getInfo());
        holder.author.setText(bean.getResult().getSong_info().getSong_list().get(position).getAuthor());

        return convertView;
    }

    class ViewHolder {
        TextView title, desc, author;
        ImageView detail;
        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.search_song_item_title);
            desc = (TextView) view.findViewById(R.id.search_song_item_desc);
            author = (TextView) view.findViewById(R.id.search_song_item_author);
            detail = (ImageView) view.findViewById(R.id.search_song_item_detail);
        }
    }
}
