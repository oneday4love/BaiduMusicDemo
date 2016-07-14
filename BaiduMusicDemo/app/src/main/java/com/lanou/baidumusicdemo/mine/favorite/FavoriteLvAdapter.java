package com.lanou.baidumusicdemo.mine.favorite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.db.MyFavoriteSong;
import com.lanou.baidumusicdemo.db.RecentPlay;

import java.util.ArrayList;

/**
 * Created by dllo on 16/7/5.
 */
public class FavoriteLvAdapter extends BaseAdapter {

    private ArrayList<MyFavoriteSong> favoriteSongs;
    private Context context;
    private OnFavoriteGetMoreListener onFavoriteGetMoreListener;

    public void setOnFavoriteGetMoreListener(OnFavoriteGetMoreListener onFavoriteGetMoreListener) {
        this.onFavoriteGetMoreListener = onFavoriteGetMoreListener;
    }

    public FavoriteLvAdapter(Context context) {
        this.context = context;
    }

    public void setFavoriteSongs(ArrayList<MyFavoriteSong> favoriteSongs) {
        this.favoriteSongs = favoriteSongs;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return favoriteSongs == null ? 0 : favoriteSongs.size();
    }

    @Override
    public Object getItem(int position) {
        return favoriteSongs == null ? null : favoriteSongs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_recent_play_lv,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(favoriteSongs.get(position).getTitle());
        holder.author.setText(favoriteSongs.get(position).getAuthor());
        holder.moreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavoriteGetMoreListener.onGetMore(position);
            }
        });

        return convertView;
    }


    class ViewHolder {
        TextView title, author;
        ImageView moreIv;
        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.recent_play_lv_title);
            author = (TextView) view.findViewById(R.id.recent_play_lv_author);
            moreIv = (ImageView) view.findViewById(R.id.recent_play_more_iv);
        }
    }

    interface OnFavoriteGetMoreListener{
        void onGetMore(int position);
    }
}
