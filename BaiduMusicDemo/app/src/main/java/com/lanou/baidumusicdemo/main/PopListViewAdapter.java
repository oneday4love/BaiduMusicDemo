package com.lanou.baidumusicdemo.main;

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
 * Created by dllo on 16/6/29.
 */
public class PopListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PlayList> playLists;
    private OnRemoveListener onRemoveListener;

    public void setOnRemoveListener(OnRemoveListener onRemoveListener) {
        this.onRemoveListener = onRemoveListener;
    }

    public PopListViewAdapter(Context context) {
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
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pop_main_list_view,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(playLists.get(position).getTitle());
        holder.author.setText(playLists.get(position).getAuthor());
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRemoveListener.onRemoveClick(position);
            }
        });

        return convertView;
    }


    class ViewHolder {
        TextView title,author;
        ImageView remove;
        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.pop_list_title);
            author = (TextView) view.findViewById(R.id.pop_list_author);
            remove = (ImageView) view.findViewById(R.id.pop_list_del);
        }
    }

    interface OnRemoveListener{
        void onRemoveClick(int position);
    }
}
