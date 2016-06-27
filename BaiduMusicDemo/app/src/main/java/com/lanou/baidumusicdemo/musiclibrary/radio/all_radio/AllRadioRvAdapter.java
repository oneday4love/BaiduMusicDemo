package com.lanou.baidumusicdemo.musiclibrary.radio.all_radio;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.volley.RequestQueueSingleton;

/**
 * Created by dllo on 16/6/24.
 */
public class AllRadioRvAdapter extends RecyclerView.Adapter<AllRadioRvAdapter.MyViewHolder> {

    private Context context;
    private AllRadioBean bean;
    private ClickToRadioPlay clickToRadioPlay;

    public void setClickToRadioPlay(ClickToRadioPlay clickToRadioPlay) {
        this.clickToRadioPlay = clickToRadioPlay;
    }

    public AllRadioRvAdapter(Context context) {
        this.context = context;
    }

    public void setBean(AllRadioBean bean) {
        this.bean = bean;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_all_radio_rv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.name.setText(bean.getResult().get(position).getScene_name());
        RequestQueueSingleton.getInstance(context).getImageLoader().get(bean.getResult().get(position).getIcon_android(),
                ImageLoader.getImageListener(holder.icon, R.mipmap.minibar_default_img, R.mipmap.minibar_default_img));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickToRadioPlay.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bean == null ? 0 : bean.getResult().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView icon;
        TextView name;
        LinearLayout item;
        public MyViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.item_all_radio_icon_iv);
            name = (TextView) itemView.findViewById(R.id.item_all_radio_name_tv);
            item  = (LinearLayout) itemView.findViewById(R.id.all_radio_item);
        }
    }

    interface ClickToRadioPlay{
        void onItemClick(int position);
    }
}
