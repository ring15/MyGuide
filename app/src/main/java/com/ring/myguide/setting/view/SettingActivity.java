package com.ring.myguide.setting.view;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.hyphenate.chat.EMClient;
import com.ring.myguide.R;
import com.ring.myguide.base.BaseActivity;
import com.ring.myguide.entity.User;
import com.ring.myguide.login.view.LoginActivity;
import com.ring.myguide.setting.SettingContract;
import com.ring.myguide.setting.presenter.SettingPresenter;
import com.ring.myguide.utils.CacheUtils;
import com.ring.myguide.utils.FileUtils;

import static com.ring.myguide.login.view.LoginActivity.FROM_SETTIGN;

public class SettingActivity extends BaseActivity<SettingPresenter, SettingContract.View>
        implements SettingContract.View {

    private static final String TAG = "SettingActivity";

    //缓存大小
    private TextView mCacheSize;
    //登录、注销按钮
    private Button mLogoutBtn;

    @Override
    protected int getIdResource() {
        return R.layout.activity_setting;
    }

    @Override
    protected SettingPresenter getPresenter() {
        return new SettingPresenter();
    }

    @Override
    protected void findView() {
        mCacheSize = findViewById(R.id.tv_cache_size);
        mLogoutBtn = findViewById(R.id.btn_logout);
    }

    @Override
    protected void init() {
        try {
            mCacheSize.setText(getString(R.string.setting_cache, FileUtils.getTotalCacheSize(this)));
        } catch (Exception e) {
            mCacheSize.setText(getString(R.string.setting_cache_error));
            e.printStackTrace();
        }
        mPresenter.init();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.layout_clean_cache:
                onClickCleanCache();
                break;
        }
    }

    /**
     * 清除缓存
     */
    private void onClickCleanCache() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.setting_clean_cache)
                .setMessage(R.string.setting_clean_cache_message)
                .setPositiveButton(R.string.setting_sure, (dialog1, which) -> {
                    FileUtils.clearAllCache(SettingActivity.this);
                    try {
                        mCacheSize.setText(getString(R.string.setting_cache, FileUtils.getTotalCacheSize(SettingActivity.this)));
                    } catch (Exception e) {
                        mCacheSize.setText(getString(R.string.setting_cache_error));
                        e.printStackTrace();
                    }
                })
                .setNegativeButton(R.string.setting_cancel, ((dialog1, which) -> {
                    dialog1.dismiss();
                })).create();
        dialog.show();

    }

    @Override
    public void setUser(User user) {
        mLogoutBtn.setText(R.string.logout);
        mLogoutBtn.setOnClickListener(v -> {
            CacheUtils.putUser(new User());
            EMClient.getInstance().logout(true);
            Toast.makeText(SettingActivity.this, R.string.logout_toast, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public void setNoUser() {
        mLogoutBtn.setText(R.string.login);
        mLogoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
            intent.putExtra("from", FROM_SETTIGN);
            startActivity(intent);
        });
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
