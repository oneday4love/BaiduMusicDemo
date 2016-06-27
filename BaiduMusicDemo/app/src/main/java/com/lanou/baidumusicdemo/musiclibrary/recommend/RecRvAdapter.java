package com.lanou.baidumusicdemo.musiclibrary.recommend;

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
 * Created by dllo on 16/6/23.
 */
public class RecRvAdapter extends RecyclerView.Adapter<RecRvAdapter.MyViewHolder> {

    private RecRvBean bean;
    private Context context;
    private OnRecRvListener onRecRvListener;

    public void setOnRecRvListener(OnRecRvListener onRecRvListener) {
        this.onRecRvListener = onRecRvListener;
    }

    public RecRvAdapter(Context context) {
        this.context = context;
    }

    public void setBean(RecRvBean bean) {
        this.bean = bean;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rec_rv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.title.setText(bean.getContent().getList().get(position).getTitleSong());
        holder.listenNum.setText(bean.getContent().getList().get(position).getListenum());
        RequestQueueSingleton.getInstance(context).getImageLoader().get(bean.getContent().getList().get(position).getPic(),
                ImageLoader.getImageListener(holder.cover, R.mipmap.default_playlist_list, R.mipmap.default_playlist_list));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecRvListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bean == null ? 0 : bean.getContent().getList().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView cover;
        TextView listenNum, title;
        RelativeLayout item;
        public MyViewHolder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.item_rec_rv_cover);
            listenNum = (TextView) itemView.findViewById(R.id.item_rec_rv_lisn_num);
            title = (TextView) itemView.findViewById(R.id.item_rec_rv_title);
            item = (RelativeLayout) itemView.findViewById(R.id.item_rec_rv);
        }
    }

    interface OnRecRvListener{
        void onItemClick(int position);
    }
}
