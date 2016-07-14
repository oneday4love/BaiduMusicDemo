package com.lanou.baidumusicdemo.loginregisterwelcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lanou.baidumusicdemo.R;
import com.lanou.baidumusicdemo.base.BaseActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dllo on 16/7/6.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backBtn;
    private EditText userNameEt, pswEt;
    private CheckBox checkBox;
    private RelativeLayout registerBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        backBtn = (ImageView) findViewById(R.id.register_back_btn);
        userNameEt = (EditText) findViewById(R.id.register_user_name_et);
        pswEt = (EditText) findViewById(R.id.register_user_password_et);
        checkBox = (CheckBox) findViewById(R.id.register_psw_eye);
        registerBtn = (RelativeLayout) findViewById(R.id.register_bt_login);

        backBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        checkBox.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 返回
            case R.id.register_back_btn:
                finish();
                break;
            // 密码可见
            case R.id.register_psw_eye:
                if (checkBox.isChecked()) {
                    pswEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    pswEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            // 注册
            case R.id.register_bt_login:
                if (userNameEt.getText().length() == 0 || pswEt.getText().length() == 0){
                    Toast.makeText(this, "请输入账号或密码", Toast.LENGTH_SHORT).show();
                } else {
                    BmobUser bmobUser = new BmobUser();
                    bmobUser.setUsername(userNameEt.getText().toString());
                    bmobUser.setPassword(pswEt.getText().toString());
                    bmobUser.signUp(this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }



                break;
        }
    }
}
