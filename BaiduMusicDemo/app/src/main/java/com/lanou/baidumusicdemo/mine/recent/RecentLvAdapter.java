package com.lanou.baidumusicdemo.mine.recent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.db.RecentPlay;

import java.util.ArrayList;

/**
 * Created by dllo on 16/7/5.
 */
public class RecentLvAdapter extends BaseAdapter {

    private ArrayList<RecentPlay> recentPlays;
    private Context context;
    private OnRecentGetMoreListener onRecentGetMoreListener;

    public void setOnRecentGetMoreListener(OnRecentGetMoreListener onRecentGetMoreListener) {
        this.onRecentGetMoreListener = onRecentGetMoreListener;
    }

    public RecentLvAdapter(Context context) {
        this.context = context;
    }

    public void setRecentPlays(ArrayList<RecentPlay> recentPlays) {
        this.recentPlays = recentPlays;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return recentPlays == null ? 0 : recentPlays.size();
    }

    @Override
    public Object getItem(int position) {
        return recentPlays == null ? null : recentPlays.get(position);
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

        holder.title.setText(recentPlays.get(position).getTitle());
        holder.author.setText(recentPlays.get(position).getAuthor());
        holder.moreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecentGetMoreListener.onGetMore(position);
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

    interface OnRecentGetMoreListener{
        void onGetMore(int position);
    }
}
