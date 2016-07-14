package com.lanou.baidumusicdemo.playpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.db.PlayList;

import java.util.ArrayList;

/**
 * Created by dllo on 16/7/2.
 */
public class PlayPagePopAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PlayList> playLists;
    private OnDelClickListener onDelClickListener;

    public void setOnDelClickListener(OnDelClickListener onDelClickListener) {
        this.onDelClickListener = onDelClickListener;
    }

    public PlayPagePopAdapter(Context context) {
        this.context = context;
    }

    public void setPlayLists(ArrayList<PlayList> playLists) {
        this.playLists = playLists;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return playLists == null ? 0 : playLists.size();
    }

    @Override
    public Object getItem(int position) {
        return playLists == null ? null : playLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pop_play_page, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(playLists.get(position).getTitle());
        holder.author.setText(playLists.get(position).getAuthor());
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelClickListener.onDel(position);
            }
        });

        return convertView;
    }


    class ViewHolder {
        TextView title, author;
        ImageView removeBtn;
        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.pop_playpage_list_title);
            author = (TextView) view.findViewById(R.id.pop_playpage_list_author);
            removeBtn = (ImageView) view.findViewById(R.id.pop_playpage_list_del);
        }
    }

    interface OnDelClickListener{
        void onDel(int position);
    }
}
