package com.ring.myguide.send_post.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.ring.myguide.R;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.User;
import com.ring.myguide.send_post.SendPostContract;
import com.ring.myguide.send_post.model.SendPostModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ring on 2019/12/3.
 */
public class SendPostPresenter extends SendPostContract.Presenter {

    private static final String TAG = "SendPostPresenter";

    private SendPostContract.Model mModel;
    private File mFile;

    public SendPostPresenter() {
        mModel = new SendPostModel();
    }

    @Override
    public void sendPost(String title, String content, List<String> paths, String city, int isBoutique, int type) {
        if (TextUtils.isEmpty(title)) {
            mView.get().showToast(R.string.send_post_post_title_hint);
            return;
        }
        if (TextUtils.isEmpty(city)) {
            city = "天津";
        }
        String location = city;
        if (paths != null && paths.size() > 0) {
            List<File> files = new ArrayList<>();
            for (int i = 0; i < paths.size(); i++) {
                if (fileIsExists(paths.get(i))) {
                    files.add(mFile);
                }
            }
            mModel.upload(files, new CallbackListener<List<String>>() {
                @Override
                public void onSuccess(List<String> data) {
                    addPost(title, content, data, location, isBoutique, type);
                }

                @Override
                public void onError(Throwable throwable) {
                    addPost(title, content, null, location, isBoutique, type);
                }
            });
        } else {
            addPost(title, content, null, location, isBoutique, type);
        }
    }

    @Override
    public void getUserBadge() {
        User user = mModel.getUserFromCache();
        if (isViewAttached()) {
            if (user != null && user.getUserName() != null) {
                if (user.getBadge() == 0) {
                    mView.get().setNormalBadge();
                } else {
                    mView.get().setOfficialBadge();
                }
            }
        }
    }

    /**
     * 设置当前城市（缓存存在则从缓存中读取，不存在进行下一步操作，现在是直接赋值"天津"，也可以跳转到定位）
     */
    @Override
    public void getCity() {
        String city = mModel.getCityFromCache();
        if (isViewAttached()) {
            if (!TextUtils.isEmpty(city)) {
                mView.get().showCity(city);
            } else {
                mView.get().showCity("天津");
            }
        }
    }

    private void addPost(String title, String content, List<String> paths, String city, int isBoutique, int type) {
        mModel.addPost(title, content, paths, city, isBoutique, type, new CallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                if (isViewAttached()) {
                    mView.get().sendSuccess();
                    mView.get().showToast(data);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().sendFailed();
                    mView.get().showToast(throwable.getMessage());
                    Log.i(TAG, throwable.getMessage());
                }
            }
        });
    }

    //判断文件是否存在
    private boolean fileIsExists(String strFile) {
        try {
            mFile = new File(strFile);
            if (!mFile.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
