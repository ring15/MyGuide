package com.ring.myguide.send_post.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ring.myguide.R;
import com.ring.myguide.base.BaseActivity;
import com.ring.myguide.city_picker.CityPickerActivity;
import com.ring.myguide.entity.City;
import com.ring.myguide.send_post.SendPostContract;
import com.ring.myguide.send_post.presenter.SendPostPresenter;
import com.ring.myguide.utils.BitmapUtils;
import com.ring.myguide.utils.MyGlideEngine;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

public class SendPostActivity extends BaseActivity<SendPostPresenter, SendPostContract.View>
        implements SendPostContract.View, TextWatcher {

    //跳转到选择城市界面的请求码
    public static final int REQUEST_CHOOSE_CITY_CODE = 0x01;
    //图片选择请求码
    public static final int REQUEST_CODE_CHOOSE = 0x02;

    //帖子标题
    private EditText mPostTitleEdit;
    //帖子内容
    private EditText mPostContentEdit;
    //贴子内容数量
    private TextView mNumText;
    //图片
    private RecyclerView mRecyclerView;
    //类型选择框
    private RadioGroup mRadioGroup;
    //美食推荐类型
    private RadioButton mFoodRadio;
    //景点推荐类型
    private RadioButton mPlaceRadio;
    //用户推荐类型（默认）
    private RadioButton mUserRadio;
    //精品switch
    private Switch mBoutiqueSwitch;
    //精品text
    private TextView mBoutiqueText;
    //定位
    private TextView mLocalText;

    //存放选择的图片
    private List<String> mSelectedImgList;

    private PostPhotoAdapter mAdapter;

    //是否精品
    private int isBoutique = 0;
    //类型
    private int type = 0;

    //权限
    private String[] permissions = {Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.CAMERA};

    @Override
    protected int getIdResource() {
        return R.layout.activity_send_post;
    }

    @Override
    protected SendPostPresenter getPresenter() {
        return new SendPostPresenter();
    }

    @Override
    protected void findView() {
        mPostTitleEdit = findViewById(R.id.et_post_title);
        mPostContentEdit = findViewById(R.id.et_post_content);
        mNumText = findViewById(R.id.tv_num);
        mRecyclerView = findViewById(R.id.recycler_photo);
        mRadioGroup = findViewById(R.id.radio_group);
        mFoodRadio = findViewById(R.id.radio_food);
        mPlaceRadio = findViewById(R.id.radio_place);
        mUserRadio = findViewById(R.id.radio_user);
        mBoutiqueSwitch = findViewById(R.id.switch_is_boutique);
        mBoutiqueText = findViewById(R.id.tv_is_boutique);
        mLocalText = findViewById(R.id.tv_city);
    }

    @Override
    protected void init() {
        mSelectedImgList = new ArrayList<>();
        mNumText.setText(getString(R.string.send_post_post_content_num, 0));
        mPostContentEdit.addTextChangedListener(this);
        mPresenter.getUserBadge();
        mPresenter.getCity();
        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (mRadioGroup.getCheckedRadioButtonId()) {
                case R.id.radio_food:
                    type = 2;
                    break;
                case R.id.radio_place:
                    type = 1;
                    break;
                case R.id.radio_user:
                    type = 0;
                    break;
            }
        });
        mBoutiqueSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isBoutique = 1;
            } else {
                isBoutique = 0;
            }
        });
        mLocalText.setOnClickListener(v -> startActivityForResult(new Intent(SendPostActivity.this, CityPickerActivity.class), REQUEST_CHOOSE_CITY_CODE));
        mAdapter = new PostPhotoAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter.setListener(new onAdapterListener() {
            @Override
            public void clickAddPhoto() {
                addPhoto();
            }

            @Override
            public void deletePhoto(int item) {
                mSelectedImgList.remove(item);
                mAdapter.setImgPaths(mSelectedImgList);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addPhoto() {
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(SendPostActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
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
        int selectSize = 9;
        if (mSelectedImgList != null) {
            selectSize = 9 - mSelectedImgList.size();
        }
        Matisse.from(this)
                .choose(MimeType.ofAll())
                .countable(true)
                .capture(true)  // 使用相机，和 captureStrategy 一起使用
                .captureStrategy(new CaptureStrategy(true, "com.ring.myguide.fileProvider"))
                .maxSelectable(selectSize)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new MyGlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.tv_send:
                String title = mPostTitleEdit.getText().toString().trim();
                String content = mPostContentEdit.getText().toString().trim();
                String local = mLocalText.getText().toString();
                mPresenter.sendPost(title, content, mSelectedImgList, local, isBoutique, type);
                break;
        }
    }

    @Override
    public void sendSuccess() {
        finish();
    }

    @Override
    public void sendFailed() {

    }

    @Override
    public void setOfficialBadge() {
        mRadioGroup.setVisibility(View.VISIBLE);
        mBoutiqueSwitch.setVisibility(View.VISIBLE);
        mBoutiqueText.setVisibility(View.VISIBLE);
    }

    @Override
    public void setNormalBadge() {
        mRadioGroup.setVisibility(View.GONE);
        mBoutiqueSwitch.setVisibility(View.GONE);
        mBoutiqueText.setVisibility(View.GONE);
    }

    @Override
    public void showCity(String city) {
        mLocalText.setText(city);
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mNumText.setText(getString(R.string.send_post_post_content_num, s.length()));
    }

    /**
     * 跳转界面回来的回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CHOOSE_CITY_CODE:
                    City city = (City) data.getSerializableExtra("city");
                    if (city != null) {
                        mLocalText.setText(city.getName());
                    }
                    break;
                case REQUEST_CODE_CHOOSE:
                    List<Uri> imgUriList = Matisse.obtainResult(data);
                    for (Uri uri : imgUriList) {
                        mSelectedImgList.add(BitmapUtils.compressBitmap(SendPostActivity.this, uri));
                    }
                    if (mSelectedImgList.size() > 0) {
                        mAdapter.setImgPaths(mSelectedImgList);
                        mAdapter.notifyDataSetChanged();
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
                    Toast.makeText(SendPostActivity.this, "添加图片需要相应权限", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public interface onAdapterListener {
        void clickAddPhoto();

        void deletePhoto(int item);
    }
}
