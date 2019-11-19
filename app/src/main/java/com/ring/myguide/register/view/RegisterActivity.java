package com.ring.myguide.register.view;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ring.myguide.R;
import com.ring.myguide.base.BaseActivity;
import com.ring.myguide.register.RegisterContract;
import com.ring.myguide.register.presenter.RegisterPresenter;

public class RegisterActivity extends BaseActivity<RegisterPresenter, RegisterContract.View>
        implements RegisterContract.View {

    private static final String TAG = "RegisterActivity";

    //用户名
    private EditText mUserNameEdit;
    //密码
    private EditText mPasswordEdit;
    //确认密码
    private EditText mPasswordAgainEdit;

    private String username;
    private String password;
    private String passwordAgain;

    @Override
    protected int getIdResource() {
        return R.layout.activity_register;
    }

    @Override
    protected RegisterPresenter getPresenter() {
        return new RegisterPresenter();
    }

    @Override
    protected void findView() {
        mUserNameEdit = findViewById(R.id.et_username);
        mPasswordEdit = findViewById(R.id.et_password);
        mPasswordAgainEdit = findViewById(R.id.et_password_again);
    }

    @Override
    protected void init() {

    }

    public void onClick(View view) {
        switch (view.getId()) {
            //注册按钮
            case R.id.btn_register:
                //获取用户名和密码
                username = mUserNameEdit.getText().toString().trim();
                password = mPasswordEdit.getText().toString().trim();
                passwordAgain = mPasswordAgainEdit.getText().toString().trim();
                //注册
                mPresenter.doRegister(username, password, passwordAgain);
                break;
            case R.id.img_return:
                //返回上一个界面
                finish();
                break;
        }
    }

    /**
     * 注册成功
     */
    @Override
    public void registerSuccess() {
        Intent intent = new Intent();
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 注册失败
     */
    @Override
    public void registerFailed() {
        //清空输入栏的密码
        mPasswordEdit.setText("");
        mPasswordAgainEdit.setText("");
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
