package com.lanou.baidumusicdemo.musiclibrary.songlist.songlistdetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lanou.baidumusicdemo.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by dllo on 16/6/23.
 */
public class SongListDetailRvAdapter extends RecyclerView.Adapter<SongListDetailRvAdapter.MyViewHolder> {

    private Context context;
    private SongListDetailBean bean;
    private OnSongListDetailItemListener onSongListDetailItemListener;



    public void setOnSongListDetailItemListener(OnSongListDetailItemListener onSongListDetailItemListener) {
        this.onSongListDetailItemListener = onSongListDetailItemListener;
    }

    public SongListDetailRvAdapter(Context context) {
        this.context = context;
    }

    public void setBean(SongListDetailBean bean) {
        this.bean = bean;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song_list_detail_rv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.title.setText(bean.getContent().get(position).getTitle());
        holder.author.setText(bean.getContent().get(position).getAuthor());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSongListDetailItemListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bean == null ? 0 : bean.getContent().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title, author;
        ImageView detailBtn;
        RelativeLayout item;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_song_list_detail_rv_title);
            author = (TextView) itemView.findViewById(R.id.item_song_list_detail_rv_author);
            detailBtn = (ImageView) itemView.findViewById(R.id.item_song_list_detail_detail_iv);
            item = (RelativeLayout) itemView.findViewById(R.id.item_song_list_detail);
        }
    }


    interface OnSongListDetailItemListener{
        void onItemClick(int position);
    }

}
