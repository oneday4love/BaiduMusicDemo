package com.lanou.baidumusicdemo.mine.local;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.db.MyFavoriteSong;

import java.util.ArrayList;

/**
 * Created by dllo on 16/7/5.
 */
public class LocalLvAdapter extends BaseAdapter {

    private ArrayList<LocalSongBean> locals;
    private Context context;
    private OnLocalGetMoreListener onLocalGetMoreListener;

    public void setOnLocalGetMoreListener(OnLocalGetMoreListener onLocalGetMoreListener) {
        this.onLocalGetMoreListener = onLocalGetMoreListener;
    }

    public LocalLvAdapter(Context context) {
        this.context = context;
    }

    public void setLocals(ArrayList<LocalSongBean> locals) {
        this.locals = locals;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return locals == null ? 0 : locals.size();
    }

    @Override
    public Object getItem(int position) {
        return locals == null ? null : locals.get(position);
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

        holder.title.setText(locals.get(position).getTitle());
        holder.author.setText(locals.get(position).getAuthor());
        holder.moreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLocalGetMoreListener.onGetMore(position);
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

    interface OnLocalGetMoreListener{
        void onGetMore(int position);
    }
}
