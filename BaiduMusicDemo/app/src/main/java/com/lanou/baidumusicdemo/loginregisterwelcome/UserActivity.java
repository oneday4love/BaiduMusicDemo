package com.lanou.baidumusicdemo.loginregisterwelcome;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lanou.baidumusicdemo.MyApp;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseActivity;
import com.lanou.baidumusicdemo.service.DelayEvent;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.BmobUser;

/**
 * Created by dllo on 16/7/6.
 */
public class UserActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backBtn;
    private TextView loginNow;

    private ImageView icon;
    private TextView name, logout;

    private LinearLayout loginLayout, logoutLayout;

    private ImageView wifi, delayExit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        backBtn = (ImageView) findViewById(R.id.user_back_btn);
        loginNow = (TextView) findViewById(R.id.user_login_now);

        icon = (ImageView) findViewById(R.id.user_icon_iv);
        name = (TextView) findViewById(R.id.user_name_tv);
        logout = (TextView) findViewById(R.id.user_logout_tv);
        loginLayout = (LinearLayout) findViewById(R.id.user_login_layout);
        logoutLayout = (LinearLayout) findViewById(R.id.user_unlogin_layout);
        wifi = (ImageView) findViewById(R.id.user_wifi);
        delayExit = (ImageView) findViewById(R.id.user_delay_exit);

        BmobUser bmobUser = BmobUser.getCurrentUser(this);
        if (bmobUser == null){
            logoutLayout.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.GONE);
        } else {
            loginLayout.setVisibility(View.VISIBLE);
            logoutLayout.setVisibility(View.GONE);
            name.setText(bmobUser.getUsername());
        }

        backBtn.setOnClickListener(this);
        loginNow.setOnClickListener(this);
        logout.setOnClickListener(this);
        wifi.setOnClickListener(this);
        delayExit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 返回
            case R.id.user_back_btn:
                finish();
                break;
            // 立即登录
            case R.id.user_login_now:
                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            // 退出登录
            case R.id.user_logout_tv:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("退出登录");
                builder.setMessage("退出登录? 请确认");
                builder.setPositiveButton("确认退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BmobUser user = new BmobUser();
                        user.logOut(MyApp.context);
                        logoutLayout.setVisibility(View.VISIBLE);
                        loginLayout.setVisibility(View.GONE);
                        // 刷新
                        Intent intent1 = new Intent("REFRESH_MINE");
                        sendBroadcast(intent1);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
                break;
            // 仅WiFi联网
            case R.id.user_wifi:

                break;
            // 延时关闭
            case R.id.user_delay_exit:
                showAlertDelay();
                break;

        }
    }

    // 显示延时退出Dialog
    public void showAlertDelay(){
        final int[] delayTime = {0};
        AlertDialog.Builder builderDelay = new AlertDialog.Builder(this);
        builderDelay.setTitle("定时关闭");
        View view = LayoutInflater.from(this).inflate(R.layout.alert_delay_exit,null);
        final TextView msg = (TextView) view.findViewById(R.id.alert_delay_message);
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.alert_delay_seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                delayTime[0] = progress * 5;
                msg.setText("将在"+ delayTime[0] +"分钟后停止播放音乐并退出");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        builderDelay.setView(view);

        builderDelay.setPositiveButton("定时关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EventBus.getDefault().post(new DelayEvent(delayTime[0]));
                Toast.makeText(UserActivity.this, "将在" + delayTime[0] + "分钟后关闭", Toast.LENGTH_SHORT).show();
            }
        });
        builderDelay.setNegativeButton("取消", null);
        builderDelay.show();
    }


//    public boolean wifi(){
//        ConnectivityManager mConnectivity = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
//        TelephonyManager mTelephony = (TelephonyManager)this.getSystemService(TELEPHONY_SERVICE);
//        //检查网络连接，如果无网络可用，就不需要进行连网操作等
//        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
//
//        if (info == null || !mConnectivity.getBackgroundDataSetting()) {
//            return false;
//        }
//
//        //判断网络连接类型，只有在3G或wifi里进行一些数据更新。
//        int netType = info.getType();
//        int netSubtype = info.getSubtype();
//
//        if (netType == ConnectivityManager.TYPE_WIFI) {
//            return info.isConnected();
//        }  else {
//            return false;
//        }
//
//    }
}
