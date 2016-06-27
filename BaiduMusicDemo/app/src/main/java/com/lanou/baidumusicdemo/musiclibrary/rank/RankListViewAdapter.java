package com.lanou.baidumusicdemo.musiclibrary.rank;

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

import java.util.List;

/**
 * Created by dllo on 16/6/19.
 */
public class RankListViewAdapter extends BaseAdapter {

    private Context context;
    private RankBean bean;

    public RankListViewAdapter(Context context) {
        this.context = context;
    }

    public void setBean(RankBean bean) {
        this.bean = bean;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return bean == null ? 0 : bean.getContent().size();
    }

    @Override
    public Object getItem(int position) {
        return bean == null ? null : bean.getContent().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_rank_list_view, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        List<RankBean.ContentBean.DetailContentBean> detailContentBeen = bean.getContent().get(position).getDetailContent();

        RequestQueueSingleton.getInstance(context).getImageLoader().get(bean.getContent().get(position).getPic_s260()
                , ImageLoader.getImageListener(holder.coverIv,R.mipmap.default_playlist_list, R.mipmap.default_playlist_list));
        holder.songListName.setText(bean.getContent().get(position).getName());
        holder.firstSong.setText(detailContentBeen.get(0).getTitle());
        holder.firstAuthor.setText(detailContentBeen.get(0).getAuthor());
        holder.secondSong.setText(detailContentBeen.get(1).getTitle());
        holder.secondAuthor.setText(detailContentBeen.get(1).getAuthor());
        holder.thirdSong.setText(detailContentBeen.get(2).getTitle());
        holder.thirdAuthor.setText(detailContentBeen.get(2).getAuthor());
        return convertView;
    }

    class ViewHolder {
        ImageView coverIv, playBtn, detailBtn;
        TextView songListName, firstSong, secondSong, thirdSong, firstAuthor, secondAuthor, thirdAuthor;
        public ViewHolder(View view) {
            coverIv = (ImageView) view.findViewById(R.id.item_rank_list_view_cover_iv);
            playBtn = (ImageView) view.findViewById(R.id.item_rank_list_view_play_btn);
            detailBtn = (ImageView) view.findViewById(R.id.item_rank_list_view_detail_btn);
            songListName = (TextView) view.findViewById(R.id.item_rank_list_view_song_list_name);
            firstSong = (TextView) view.findViewById(R.id.item_rank_list_view_first_song_title);
            secondSong = (TextView) view.findViewById(R.id.item_rank_list_view_second_song_title);
            thirdSong = (TextView) view.findViewById(R.id.item_rank_list_view_third_song_title);
            firstAuthor = (TextView) view.findViewById(R.id.item_rank_list_view_first_song_author);
            secondAuthor = (TextView) view.findViewById(R.id.item_rank_list_view_second_song_author);
            thirdAuthor = (TextView) view.findViewById(R.id.item_rank_list_view_third_song_author);
        }
    }
}
