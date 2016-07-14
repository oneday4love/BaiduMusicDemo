package com.lanou.baidumusicdemo.musiclibrary.mv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

/**
 * Created by dllo on 16/7/2.
 */
public class MvRvAdapter extends RecyclerView.Adapter<MvRvAdapter.MyViewHolder> {

    private MvBean bean;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MvRvAdapter(Context context) {
        this.context = context;
    }

    public void setBean(MvBean bean) {
        this.bean = bean;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mv_rv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        RequestQueueSingleton.getInstance(context).getImageLoader().get(bean.getResult().getMv_list().get(position).getThumbnail2(),
                ImageLoader.getImageListener(holder.cover, R.mipmap.default_mv, R.mipmap.default_mv));
        holder.title.setText(bean.getResult().getMv_list().get(position).getTitle());
        holder.author.setText(bean.getResult().getMv_list().get(position).getArtist());
        holder.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bean == null ? 0 : bean.getResult().getMv_list().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView cover;
        TextView title, author;

        public MyViewHolder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.item_mv_cover_iv);
            title = (TextView) itemView.findViewById(R.id.item_mv_title_tv);
            author = (TextView) itemView.findViewById(R.id.item_mv_author_tv);
        }
    }


    interface OnItemClickListener{
        void OnItemClick(int position);
    }
}
