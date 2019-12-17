package com.ring.myguide.login.view;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.ring.myguide.R;
import com.ring.myguide.base.BaseActivity;
import com.ring.myguide.entity.User;
import com.ring.myguide.login.LoginContract;
import com.ring.myguide.login.presenter.LoginPresenter;
import com.ring.myguide.main.view.MainActivity;
import com.ring.myguide.register.view.RegisterActivity;

public class LoginActivity extends BaseActivity<LoginPresenter, LoginContract.View>
        implements LoginContract.View {

    private static final String TAG = "LoginActivity";

    //判断从哪里打开的登录界面
    public static final int FROM_SPLASH = 0;
    public static final int FROM_SETTING = 1;
    public static final int FROM_MEFRAGMENT = 2;

    //跳转到注册界面的请求码
    public static final int REQUEST_REGISTER_CODE = 0x01;

    //用户名
    private EditText mUserNameEdit;
    //密码
    private EditText mPasswordEdit;
    //返回按钮
    private ImageView mReturnImg;
    //跳过按钮
    private TextView mSkipText;

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
        mReturnImg = findViewById(R.id.img_return);
        mSkipText = findViewById(R.id.btn_skip);
    }

    @Override
    protected void init() {
        int from = getIntent().getIntExtra("from", FROM_SPLASH);
        if (FROM_SPLASH == from) {
            //从启动页打开的登录界面没有返回按钮
            mReturnImg.setVisibility(View.GONE);
            mSkipText.setVisibility(View.VISIBLE);
        } else {
            //从设置界面打开的登录界面有返回按钮
            mReturnImg.setVisibility(View.VISIBLE);
            mSkipText.setVisibility(View.GONE);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            //登录按钮
            case R.id.btn_login:
                //获取用户名和密码
                username = mUserNameEdit.getText().toString().trim();
                password = mPasswordEdit.getText().toString().trim();
                //登录
                mPresenter.doLogin(username, password, getCacheDir().getPath());
                break;
            case R.id.tv_goto_register:
                //跳转到注册界面
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), REQUEST_REGISTER_CODE);
                break;
            case R.id.img_return:
                //返回上一个界面
                finish();
                break;
            case R.id.btn_skip:
                //跳过登录，直接跳转主界面
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
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
                Log.i(TAG, username + "登录聊天服务器成功！");
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

    /**
     * 跳转界面回来的回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_REGISTER_CODE) {
                //跳转到注册界面，注册成功后，将注册的用户名及密码填到输入框中
                String registerUsername = data.getStringExtra("username");
                String registerPassword = data.getStringExtra("password");
                if (!TextUtils.isEmpty(registerUsername)) {
                    mUserNameEdit.setText(registerUsername);
                }
                if (!TextUtils.isEmpty(registerPassword)) {
                    mPasswordEdit.setText(registerPassword);
                }
            }
        }
    }
}
