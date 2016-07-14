package com.lanou.baidumusicdemo.author;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lanou.baidumusicdemo.R;


/**
 * Created by dllo on 16/6/27.
 */
public class AuthorDetailRvAdapter extends RecyclerView.Adapter<AuthorDetailRvAdapter.MyViewHolder> {

    private Context context;
    private AuthorDetailBean bean;
    private OnAuthorDetailRvListener onAuthorDetailRvListener;
    private OnAuthorDetailGetMoreListener onAuthorDetailGetMoreListener;

    public void setOnAuthorDetailGetMoreListener(OnAuthorDetailGetMoreListener onAuthorDetailGetMoreListener) {
        this.onAuthorDetailGetMoreListener = onAuthorDetailGetMoreListener;
    }

    public void setOnAuthorDetailRvListener(OnAuthorDetailRvListener onAuthorDetailRvListener) {
        this.onAuthorDetailRvListener = onAuthorDetailRvListener;
    }

    public AuthorDetailRvAdapter(Context context) {
        this.context = context;
    }

    public void setBean(AuthorDetailBean bean) {
        this.bean = bean;
        notifyDataSetChanged();
    }

    public void addBean (AuthorDetailBean authorDetailBean){
        bean.getSonglist().clear();
        bean.getSonglist().addAll(authorDetailBean.getSonglist());
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song_list_detail_rv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.title.setText(bean.getSonglist().get(position).getTitle());
        if (bean.getSonglist().get(position).getAlbum_title() == null){
            holder.album.setText("单曲");
        } else {
            holder.album.setText("《"+bean.getSonglist().get(position).getAlbum_title()+"》");
        }
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAuthorDetailRvListener.onItemClick(position);
            }
        });
        holder.moreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAuthorDetailGetMoreListener.onMoreClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bean == null ? 0 : bean.getSonglist().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title, album;
        RelativeLayout item;
        ImageView moreIv;
        public MyViewHolder(View itemView) {
            super(itemView);
            moreIv = (ImageView) itemView.findViewById(R.id.item_song_list_detail_more_iv);
            title = (TextView) itemView.findViewById(R.id.item_song_list_detail_rv_title);
            album = (TextView) itemView.findViewById(R.id.item_song_list_detail_rv_author);
            item = (RelativeLayout) itemView.findViewById(R.id.item_song_list_detail);
        }
    }

    interface OnAuthorDetailRvListener{
        void onItemClick(int position);
    }

    interface OnAuthorDetailGetMoreListener{
        void onMoreClick(int position);
    }
}
