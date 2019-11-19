package com.ring.myguide.login.view;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.ring.myguide.R;
import com.ring.myguide.base.BaseActivity;
import com.ring.myguide.entity.User;
import com.ring.myguide.login.LoginContract;
import com.ring.myguide.login.presenter.LoginPresenter;
import com.ring.myguide.main.view.MainActivity;

public class LoginActivity extends BaseActivity<LoginPresenter, LoginContract.View>
        implements LoginContract.View {

    private static final String TAG = "LoginActivity";

    //用户名
    private EditText mUserNameEdit;
    //密码
    private EditText mPasswordEdit;

    private String username;
    private String password;

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
                username = mUserNameEdit.getText().toString().trim();
                password = mPasswordEdit.getText().toString().trim();
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
     * 调用环信登录接口，开启聊天功能
     */
    @Override
    public void loginChat() {
        EMClient.getInstance().logout(true);
        EMClient.getInstance().login(username, password, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.i(TAG, "登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d(TAG, "登录聊天服务器失败！");
            }
        });
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
