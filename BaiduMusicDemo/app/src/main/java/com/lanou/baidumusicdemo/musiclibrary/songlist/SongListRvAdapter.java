package com.lanou.baidumusicdemo.musiclibrary.songlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.musiclibrary.rank.rankdetail.RankDetailBean;
import com.lanou.baidumusicdemo.service.SongPlayBean;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

import java.util.List;

/**
 * Created by dllo on 16/6/20.
 */
public class SongListRvAdapter extends RecyclerView.Adapter<SongListRvAdapter.MyViewHolder> {

    private SongListBean bean;
    private Context context;
    private SongListRvClickListener songListRvClickListener;

    public void setSongListRvClickListener(SongListRvClickListener songListRvClickListener) {
        this.songListRvClickListener = songListRvClickListener;
    }

    public SongListRvAdapter(Context context) {
        this.context = context;
    }

    public void setBean(SongListBean bean) {
        this.bean = bean;
        notifyDataSetChanged();
    }

    public void addBean (SongListBean songListBean){
        bean.getContent().addAll(songListBean.getContent());
        notifyItemInserted(bean.getContent().size());
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song_list_rv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        RequestQueueSingleton.getInstance(context).getImageLoader().get(bean.getContent().get(position).getPic_300()
                , ImageLoader.getImageListener(holder.coverIv, R.mipmap.default_playlist_list,R.mipmap.default_playlist_list));
        holder.title.setText(bean.getContent().get(position).getTitle());
        holder.listenNum.setText(bean.getContent().get(position).getListenum());
        holder.tag.setText(bean.getContent().get(position).getTag());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songListRvClickListener.onItemClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bean == null ? 0 : bean.getContent().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView coverIv;
        TextView listenNum, title, tag;
        RelativeLayout item;

        public MyViewHolder(View itemView) {
            super(itemView);
            coverIv = (ImageView) itemView.findViewById(R.id.item_song_list_rv_cover_iv);
            listenNum = (TextView) itemView.findViewById(R.id.item_song_list_rv_listen_tv);
            title = (TextView) itemView.findViewById(R.id.item_song_list_rv_title_tv);
            tag = (TextView) itemView.findViewById(R.id.item_song_list_rv_tag_tv);
            item = (RelativeLayout) itemView.findViewById(R.id.song_list_rv_item);
        }
    }

    interface SongListRvClickListener{
        void onItemClickListener(int position);
    }
}
