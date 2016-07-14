package com.lanou.baidumusicdemo.musiclibrary.rank.rankdetail;

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
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

/**
 * Created by dllo on 16/6/22.
 */
public class RankDetailRvAdapter extends RecyclerView.Adapter<RankDetailRvAdapter.MyViewHolder> {

    private RankDetailBean bean;
    private Context context;
    private OnRankDetailRvListener onRankDetailRvListener;
    private OnGetMoreListener onGetMoreListener;

    public void setOnGetMoreListener(OnGetMoreListener onGetMoreListener) {
        this.onGetMoreListener = onGetMoreListener;
    }

    public void setOnRankDetailRvListener(OnRankDetailRvListener onRankDetailRvListener) {
        this.onRankDetailRvListener = onRankDetailRvListener;
    }

    public RankDetailRvAdapter(Context context) {
        this.context = context;
    }

    public void setBean(RankDetailBean bean) {
        this.bean = bean;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rank_detail_rv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.title.setText(bean.getSong_list().get(position).getTitle());
        holder.author.setText(bean.getSong_list().get(position).getAuthor());
        RequestQueueSingleton.getInstance(context).getImageLoader().get(bean.getSong_list().get(position).getPic_big(),
                ImageLoader.getImageListener(holder.coverIv ,R.mipmap.minibar_default_img, R.mipmap.minibar_default_img));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRankDetailRvListener.onItemClick(position);
            }
        });
        holder.moreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGetMoreListener.OnMoreClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bean == null ? 0 : bean.getSong_list().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView coverIv, moreIv;
        TextView title, author;
        RelativeLayout item;
        public MyViewHolder(View itemView) {
            super(itemView);
            moreIv = (ImageView) itemView.findViewById(R.id.item_rank_detail_rv_more_iv);
            coverIv = (ImageView) itemView.findViewById(R.id.item_rank_detail_rv_cover_iv);
            title = (TextView) itemView.findViewById(R.id.item_rank_detail_rv_title);
            author = (TextView) itemView.findViewById(R.id.item_rank_detail_rv_author);
            item = (RelativeLayout) itemView.findViewById(R.id.item_rank_detail_rv);
        }
    }

    interface OnRankDetailRvListener{
        void onItemClick(int position);
    }

    interface OnGetMoreListener{
        void OnMoreClick(int position);
    }
}
