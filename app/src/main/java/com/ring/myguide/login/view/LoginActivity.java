package com.ring.myguide.login.view;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ring.myguide.R;
import com.ring.myguide.base.BaseActivity;
import com.ring.myguide.entity.User;
import com.ring.myguide.login.LoginContract;
import com.ring.myguide.login.presenter.LoginPresenter;
import com.ring.myguide.main.view.MainActivity;

public class LoginActivity extends BaseActivity<LoginPresenter, LoginContract.View>
        implements LoginContract.View {

    //用户名
    private EditText mUserNameEdit;
    //密码
    private EditText mPasswordEdit;

    @Override
    protected int getIdResource() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPresenter getPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void findView() {
        mUserNameEdit = findViewById(R.id.et_username);
        mPasswordEdit = findViewById(R.id.et_password);
    }

    @Override
    protected void init() {

    }

    public void onClick(View view) {
        switch (view.getId()) {
            //登录按钮
            case R.id.btn_login:
                //获取用户名和密码
                String username = mUserNameEdit.getText().toString().trim();
                String password = mPasswordEdit.getText().toString().trim();
                //登录
                mPresenter.doLogin(username, password);
                break;
            case R.id.tv_goto_register:
                //跳转到注册界面
//                startActivity(new Intent(LoginActivity.this, ));
                break;
        }
    }


    /**
     * 用户登录成功后进行的操作
     *
     * @param user
     */
    @Override
    public void loginSuccess(User user) {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    /**
     * 用户登录失败后进行的操作
     */
    @Override
    public void loginFailed() {
        //清空输入栏的密码
        mPasswordEdit.setText("");
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }
}
