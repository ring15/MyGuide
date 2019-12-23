package com.ring.myguide.user_detail.view;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ring.myguide.R;
import com.ring.myguide.base.BaseActivity;
import com.ring.myguide.chat.view.ChatActivity;
import com.ring.myguide.entity.User;
import com.ring.myguide.user_detail.UserDetailContract;
import com.ring.myguide.user_detail.presenter.UserDetailPresenter;

public class UserDetailActivity extends BaseActivity<UserDetailPresenter, UserDetailContract.View>
        implements UserDetailContract.View {

    //用户头像
    private ImageView mUserAvatar;
    //用户名
    private TextView mNickName;
    //管理员图标
    private ImageView mManagerImg;
    //性别
    private ImageView mSexImg;
    //用户id
    private TextView mUserName;
    //出生日期
    private TextView mBirthdayText;
    //个人简介layout
    private LinearLayout mIntroduceLayout;
    //个人简介内容
    private TextView mIntroduceText;
    //添加好友按钮
    private Button mAddFriendBtn;
    //发送消息按钮
    private Button mSendMessageBtn;
    //删除好友按钮
    private Button mDeleteFriendBtn;
    //添加到黑名单按钮
    private Button mAddBlackBtn;
    //从黑名单中移除按钮
    private Button mDeleteBlackBtn;

    //查看的用户资料
    private User mUser;

    @Override
    protected int getIdResource() {
        return R.layout.activity_user_detail;
    }

    @Override
    protected UserDetailPresenter getPresenter() {
        return new UserDetailPresenter();
    }

    @Override
    protected void findView() {
        mUserAvatar = findViewById(R.id.img_user_avatar);
        mNickName = findViewById(R.id.tv_nickname);
        mManagerImg = findViewById(R.id.img_manager);
        mSexImg = findViewById(R.id.img_sex);
        mUserName = findViewById(R.id.tv_username);
        mBirthdayText = findViewById(R.id.tv_birthday);
        mIntroduceLayout = findViewById(R.id.layout_introduce);
        mIntroduceText = findViewById(R.id.tv_introduce);
        mAddFriendBtn = findViewById(R.id.btn_add_friend);
        mSendMessageBtn = findViewById(R.id.btn_send_msg);
        mDeleteFriendBtn = findViewById(R.id.btn_delete_friend);
        mAddBlackBtn = findViewById(R.id.btn_add_black);
        mDeleteBlackBtn = findViewById(R.id.btn_delete_black);
    }

    @Override
    protected void init() {
        mUser = (User) getIntent().getSerializableExtra("user");
        mPresenter.init(mUser.getUserName());

        if (mUser != null && mUser.getUid() != null) {
            Glide.with(this)
                    .load(getCacheDir().getPath() + "/" + mUser.getUserImgPath())
                    .error(R.drawable.icon_avatar_default)
                    .placeholder(R.drawable.icon_avatar_default)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .skipMemoryCache(true) // 不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                    .into(mUserAvatar);
            mNickName.setText(mUser.getNickname());
            mUserName.setText(mUser.getUserName());
            mIntroduceLayout.setVisibility(View.VISIBLE);
            if (mUser.getIntroduce() != null) {
                mIntroduceText.setText(mUser.getIntroduce());
            } else {
                mIntroduceText.setText(getString(R.string.me_introduction_content));
            }
            if (mUser.getBadge() == 1) {
                mManagerImg.setVisibility(View.VISIBLE);
            } else {
                mManagerImg.setVisibility(View.GONE);
            }
            if (mUser.getSex() == 1){
                mSexImg.setVisibility(View.VISIBLE);
                mSexImg.setImageResource(R.drawable.icon_man);
            } else if (mUser.getSex() == 2){
                mSexImg.setVisibility(View.VISIBLE);
                mSexImg.setImageResource(R.drawable.icon_woman);
            } else {
                mSexImg.setVisibility(View.GONE);
            }
            if (mUser.getBirthday() != null){
                mBirthdayText.setVisibility(View.VISIBLE);
                mBirthdayText.setText(mUser.getBirthday());
            } else {
                mBirthdayText.setVisibility(View.GONE);
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.btn_add_friend:
                //添加好友
                mPresenter.addFriend(mUser.getUserName());
                break;
            case R.id.btn_send_msg:
                //发送消息
                Intent intent = new Intent(UserDetailActivity.this, ChatActivity.class);
                intent.putExtra("user", mUser);
                startActivity(intent);
                break;
            case R.id.btn_delete_friend:
                //删除好友
                mPresenter.deleteFriend(mUser.getUserName());
                break;
            case R.id.btn_add_black:
                //添加黑名单
                mPresenter.addBlack(mUser.getUserName());
                break;
            case R.id.btn_delete_black:
                //从黑名单中删除
                mPresenter.deleteBlack(mUser.getUserName());
                break;
        }
    }

    /**
     * 陌生人，只可以添加好友，其余操作不可以完成
     */
    @Override
    public void setStranger() {
        mAddFriendBtn.setVisibility(View.VISIBLE);
        mSendMessageBtn.setVisibility(View.GONE);
        mDeleteFriendBtn.setVisibility(View.GONE);
        mAddBlackBtn.setVisibility(View.GONE);
        mDeleteBlackBtn.setVisibility(View.GONE);
    }

    /**
     * 好友且不在黑名单中
     */
    @Override
    public void setFriends() {
        mAddFriendBtn.setVisibility(View.GONE);
        mSendMessageBtn.setVisibility(View.VISIBLE);
        mDeleteFriendBtn.setVisibility(View.VISIBLE);
        mAddBlackBtn.setVisibility(View.VISIBLE);
        mDeleteBlackBtn.setVisibility(View.GONE);
    }

    /**
     * 好友但是在黑名单中
     */
    @Override
    public void setFriendsAndBlacks() {
        mAddFriendBtn.setVisibility(View.GONE);
        mSendMessageBtn.setVisibility(View.GONE);
        mDeleteFriendBtn.setVisibility(View.VISIBLE);
        mAddBlackBtn.setVisibility(View.GONE);
        mDeleteBlackBtn.setVisibility(View.VISIBLE);
    }

    /**
     * 非好友，但是在黑名单中
     */
    @Override
    public void setBlacks() {
        mAddFriendBtn.setVisibility(View.VISIBLE);
        mSendMessageBtn.setVisibility(View.GONE);
        mDeleteFriendBtn.setVisibility(View.GONE);
        mAddBlackBtn.setVisibility(View.GONE);
        mDeleteBlackBtn.setVisibility(View.VISIBLE);
    }

    /**
     * 其他情况，如，未登录状态下的查询
     */
    @Override
    public void setOthers() {
        mAddFriendBtn.setVisibility(View.GONE);
        mSendMessageBtn.setVisibility(View.GONE);
        mDeleteFriendBtn.setVisibility(View.GONE);
        mAddBlackBtn.setVisibility(View.GONE);
        mDeleteBlackBtn.setVisibility(View.GONE);
    }

    @Override
    public void onQueryFailed() {
        finish();
    }

    @Override
    public void onAddFriendSuccess() {
        setFriends();
    }

    @Override
    public void onDeleteFriendSuccess() {
        setStranger();
    }

    @Override
    public void onAddBlackSuccess() {
        setBlacks();
    }

    @Override
    public void onDeleteBlackSuccess() {
        setFriends();
    }

    @Override
    public void onFailed() {

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
