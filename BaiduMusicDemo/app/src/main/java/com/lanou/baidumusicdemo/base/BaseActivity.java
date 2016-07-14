package com.lanou.baidumusicdemo.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.PopupWindow;

import com.lanou.baidumusicdemo.R;

/**
 * Created by dllo on 16/6/17.
 */
public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.activity_anim, R.anim.activity_anim);
    }
}
