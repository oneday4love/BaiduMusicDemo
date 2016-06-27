package com.lanou.baidumusicdemo.musiclibrary.radio;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.lanou.baidumusicdemo.MyApp;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseFragment;
import com.lanou.baidumusicdemo.musiclibrary.radio.all_radio.AllRadioActivity;

/**
 * Created by dllo on 16/6/17.
 */
public class RadioFragment extends BaseFragment implements View.OnClickListener {

    private TextView radioMore;

    @Override
    public int setLayout() {
        return R.layout.fragment_radio;
    }

    @Override
    public void initView(View view) {
        radioMore = (TextView) view.findViewById(R.id.radio_more_tv);
    }

    @Override
    public void initData() {
        radioMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.radio_more_tv:
                Intent intent = new Intent(MyApp.context, AllRadioActivity.class);
                startActivity(intent);
                break;
        }
    }
}
