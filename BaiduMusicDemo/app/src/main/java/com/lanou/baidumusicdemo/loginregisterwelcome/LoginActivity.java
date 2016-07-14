package com.lanou.baidumusicdemo.loginregisterwelcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lanou.baidumusicdemo.MyApp;
import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseActivity;
import com.lanou.baidumusicdemo.db.LiteOrmSingle;
import com.lanou.baidumusicdemo.db.MyFavoriteSong;
import com.lanou.baidumusicdemo.main.MainActivity;
import com.lanou.baidumusicdemo.mine.favorite.FavoriteManager;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dllo on 16/7/6.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backBtn;
    private EditText userNameEt, pswEt;
    private CheckBox checkBox;
    private RelativeLayout loginBt;
    private TextView toRegister;

    private MyFavoriteSong myFavoriteSong;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        backBtn = (ImageView) findViewById(R.id.login_back_btn);
        userNameEt = (EditText) findViewById(R.id.login_user_name_et);
        pswEt = (EditText) findViewById(R.id.login_user_password_et);
        checkBox = (CheckBox) findViewById(R.id.login_psw_eye);
        loginBt = (RelativeLayout) findViewById(R.id.login_bt_login);
        toRegister = (TextView) findViewById(R.id.login_to_register);

        backBtn.setOnClickListener(this);
        loginBt.setOnClickListener(this);
        toRegister.setOnClickListener(this);
        checkBox.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 返回
            case R.id.login_back_btn:
                finish();
                break;
            // 密码可见
            case R.id.login_psw_eye:
                if (checkBox.isChecked()) {
                    pswEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    pswEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            // 登录
            case R.id.login_bt_login:

                if (userNameEt.getText().length() == 0 || pswEt.getText().length() == 0){
                    Toast.makeText(this, "请输入账号或密码", Toast.LENGTH_SHORT).show();
                } else {
                    BmobUser bmobUser = new BmobUser();
                    bmobUser.setUsername(userNameEt.getText().toString());
                    bmobUser.setPassword(pswEt.getText().toString());
                    bmobUser.login(this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            getNetData(); // 获取网络数据
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            // 去注册
            case R.id.login_to_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }


    // 登录后获取网络数据
    public void getNetData(){
        BmobUser bmobUser = BmobUser.getCurrentUser(this);
        if (bmobUser != null) {
            myFavoriteSong = new MyFavoriteSong();

            //
            BmobQuery<MyFavoriteSong> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("userName", bmobUser.getUsername());
            bmobQuery.findObjects(this, new FindListener<MyFavoriteSong>() {
                @Override
                public void onSuccess(List<MyFavoriteSong> list) {
                    FavoriteManager.getInstance().getFavoriteSongs().clear();
                    for (MyFavoriteSong myFavoriteSong : list) {
                        FavoriteManager.getInstance().getFavoriteSongs().add(myFavoriteSong);
                    }
                    int number = 0;
                    for (MyFavoriteSong song : LiteOrmSingle.getInstance().getLiteOrm().query(MyFavoriteSong.class)) {
                        for (MyFavoriteSong favoriteSong : FavoriteManager.getInstance().getFavoriteSongs()) {
                            if (song.getTitle().equals(favoriteSong.getTitle())) {
                                number++;
                            }
                        }
                        if (number == 0){
                            song.setUserName(BmobUser.getCurrentUser(MyApp.context).getUsername());
                            song.save(MyApp.context, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    getNewData();
                                }

                                @Override
                                public void onFailure(int i, String s) {

                                }
                            });
                        } else {
                            number = 0;
                        }
                    }

                    // 刷新
                    Intent intent = new Intent("REFRESH_MINE");
                    sendBroadcast(intent);



                }

                @Override
                public void onError(int i, String s) {

                }
            });

        }
    }

    public void getNewData(){
        if (BmobUser.getCurrentUser(MyApp.context) != null){
            BmobQuery<MyFavoriteSong> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("userName", BmobUser.getCurrentUser(MyApp.context).getUsername());
            bmobQuery.findObjects(this, new FindListener<MyFavoriteSong>() {
                @Override
                public void onSuccess(List<MyFavoriteSong> list) {
                    FavoriteManager.getInstance().getFavoriteSongs().clear();
                    Log.d("LoginActivity", "list.size():" + list.size());
                    for (MyFavoriteSong myFavoriteSong : list) {
                        FavoriteManager.getInstance().getFavoriteSongs().add(myFavoriteSong);
                        Log.d("LoginActivity", myFavoriteSong.getTitle());
                    }

                    Log.d("LoginActivity", "LiteOrmSingle.getInstance().getLiteOrm().query(MyFavoriteSong.class).size():" + LiteOrmSingle.getInstance().getLiteOrm().query(MyFavoriteSong.class).size());

                    // 刷新
                    Intent intent = new Intent("REFRESH_MINE");
                    sendBroadcast(intent);

                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }
    }

}
