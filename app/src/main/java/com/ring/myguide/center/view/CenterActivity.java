package com.ring.myguide.center.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ring.myguide.R;
import com.ring.myguide.base.BaseActivity;
import com.ring.myguide.center.CenterContract;
import com.ring.myguide.center.presenter.CenterPresenter;
import com.ring.myguide.entity.User;
import com.ring.myguide.utils.BitmapUtils;
import com.ring.myguide.utils.MyGlideEngine;
import com.ring.myguide.widget.InputDialog;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CenterActivity extends BaseActivity<CenterPresenter, CenterContract.View>
        implements CenterContract.View {

    //图片选择请求码
    public static final int REQUEST_CODE_CHOOSE = 0x01;

    //头像
    private ImageView mAvatarImg;
    //昵称
    private TextView mNickNmeText;
    //生日
    private TextView mBirthdayText;
    //性别
    private TextView mSexText;
    //个人介绍
    private TextView mIntroductionText;

    //存放生日的年、月、日
    private List<Integer> datas = new ArrayList<>();
    //存放user
    private User mUser;

    //存放选择的图片
    private List<String> mSelectedImgList;

    //权限
    private String[] permissions = {Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.CAMERA};

    @Override
    protected int getIdResource() {
        return R.layout.activity_center;
    }

    @Override
    protected CenterPresenter getPresenter() {
        return new CenterPresenter();
    }

    @Override
    protected void findView() {
        mAvatarImg = findViewById(R.id.img_user_avatar);
        mNickNmeText = findViewById(R.id.tv_nickname);
        mBirthdayText = findViewById(R.id.tv_birthday);
        mSexText = findViewById(R.id.tv_sex);
        mIntroductionText = findViewById(R.id.tv_introduction);
    }

    @Override
    protected void init() {
        mPresenter.getUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.tv_save:
                //保存修改
                doUpdateInfo();
                break;
            case R.id.layout_avatar:
                //修改头像
                changeAvatar();
                break;
            case R.id.layout_nickname:
                //修改昵称
                changeNickName();
                break;
            case R.id.layout_birthday:
                //修改生日
                changeBirthday();
                break;
            case R.id.layout_sex:
                //修改性别
                changeSex();
                break;
            case R.id.layout_introduction:
                //修改个人介绍
                changeIntroduction();
                break;
        }
    }

    private void doUpdateInfo() {
        if (mSelectedImgList != null && mSelectedImgList.size() == 1) {
            mPresenter.changeInfo(mUser, mSelectedImgList.get(0), getCacheDir().getPath());
        } else {
            mPresenter.changeInfo(mUser, null, getCacheDir().getPath());
        }
    }

    private void changeAvatar() {
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(CenterActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (permissionList.size() != 0) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[0]), 1001);
        } else {
            choosePhoto();
        }
    }

    private void choosePhoto() {
        Matisse.from(this)
                .choose(MimeType.ofAll())
                .countable(true)
                .capture(true)  // 使用相机，和 captureStrategy 一起使用
                .captureStrategy(new CaptureStrategy(true, "com.ring.myguide.fileProvider"))
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new MyGlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    private void changeIntroduction() {
        InputDialog dialog = new InputDialog.Builder(this)
                .setTitle(getString(R.string.center_change_introduction))
                .setContent(mUser.getIntroduce())
                .setListener(new InputDialog.myOnclickListener() {
                    @Override
                    public void onSure(InputDialog dialog, String content) {
                        mIntroductionText.setText(content);
                        mUser.setIntroduce(content);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancel(InputDialog dialog) {
                        dialog.dismiss();
                    }
                }).build();
        dialog.show();
    }

    private void changeNickName() {
        InputDialog dialog = new InputDialog.Builder(this)
                .setTitle(getString(R.string.center_change_nickname))
                .setContent(mUser.getNickname())
                .setListener(new InputDialog.myOnclickListener() {
                    @Override
                    public void onSure(InputDialog dialog, String content) {
                        mNickNmeText.setText(content);
                        mUser.setNickname(content);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancel(InputDialog dialog) {
                        dialog.dismiss();
                    }
                }).build();
        dialog.show();
    }

    private void changeSex() {
        String[] list = {getString(R.string.center_sex_man), getString(R.string.center_sex_woman)};
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(null)
                .setItems(list, (dialog1, which) -> {
                    if (which + 1 == 1) {
                        mSexText.setText(R.string.center_sex_man);
                    } else if (which + 1 == 2) {
                        mSexText.setText(R.string.center_sex_woman);
                    } else {
                        mSexText.setText(R.string.center_sex_unknown);
                    }
                    mUser.setSex(which + 1);
                }).show();
    }

    private void changeBirthday() {
        Calendar calendar = Calendar.getInstance();
        //设置默认时间为当前时间
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        //若生日不为空，则设置日期选择器初始时间为上次选择的生日
        if (datas.size() == 3) {
            year = datas.get(0);
            //时间选择器设置的时间从0开始
            month = datas.get(1) - 1;
            day = datas.get(2);
        }
        //新建日期选择器
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, i, i1, i2) -> {
            //点击确认按钮后，开始显示选中的日期
            mBirthdayText.setVisibility(View.VISIBLE);
            mBirthdayText.setText(getString(R.string.center_birthday, i, i1 + 1, i2));
            //清空当前日期，设置为选中时间（当前只是缓存，需点击保存方可上传）
            datas.clear();
            datas.add(i);
            datas.add(i1 + 1);
            datas.add(i2);
            mUser.setBirthday(i + "-" + i1 + "-" + i2);
        }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void setUser(User user) {
        mUser = user;
        //加载头像
        Glide.with(this)
                .load(user.getUserImgPaht())
                .error(R.drawable.icon_avatar_default)
                .placeholder(R.drawable.icon_avatar_default)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .skipMemoryCache(true) // 不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                .into(mAvatarImg);
        //昵称
        mNickNmeText.setText(user.getNickname());
        //个人介绍
        if (user.getIntroduce() != null) {
            mIntroductionText.setText(user.getIntroduce());
        } else {
            mIntroductionText.setText(getString(R.string.me_introduction_content));
        }
        //生日，若为空则不显示
        if (user.getBirthday() != null) {
            mBirthdayText.setVisibility(View.VISIBLE);
            mBirthdayText.setText(user.getBirthday());
            String[] birthdays = user.getBirthday().split("-");
            datas.clear();
            try {
                for (String birthday : birthdays) {
                    datas.add(Integer.parseInt(birthday));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mBirthdayText.setVisibility(View.INVISIBLE);
        }
        //性别，1为男，2为女，其余未知
        if (user.getSex() == 1) {
            mSexText.setText(R.string.center_sex_man);
        } else if (user.getSex() == 2) {
            mSexText.setText(R.string.center_sex_woman);
        } else {
            mSexText.setText(R.string.center_sex_unknown);
        }
    }

    @Override
    public void setNoUser() {
        finish();
    }

    @Override
    public void updateSuccess() {
        finish();
    }

    @Override
    public void updateFailed() {

    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE:
                    List<Uri> imgUriList = Matisse.obtainResult(data);
                    mSelectedImgList = new ArrayList<>();
                    for (Uri uri : imgUriList) {
                        mSelectedImgList.add(BitmapUtils.compressBitmap(CenterActivity.this, uri));
                    }
                    if (mSelectedImgList.size() > 0) {
                        Glide.with(this)
                                .load(mSelectedImgList.get(0))
                                .error(R.drawable.icon_avatar_default)
                                .placeholder(R.drawable.icon_avatar_default)
                                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                .skipMemoryCache(true) // 不使用内存缓存
                                .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                                .into(mAvatarImg);
                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhoto();
                } else {
                    Toast.makeText(CenterActivity.this, "修改头像需要相应权限", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
