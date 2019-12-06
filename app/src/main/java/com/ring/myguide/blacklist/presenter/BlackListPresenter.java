package com.ring.myguide.blacklist.presenter;

import android.util.Log;

import com.ring.myguide.RequestImgModel;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.blacklist.BlacksContract;
import com.ring.myguide.blacklist.model.BlackListModel;
import com.ring.myguide.entity.User;
import com.ring.myguide.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ring on 2019/11/20.
 */
public class BlackListPresenter extends BlacksContract.Presenter {

    private static final String TAG = "BlackListPresenter";

    private BlacksContract.Model mModel;
    private List<User> mUserList = new ArrayList<>();
    private String mSavePath;

    public BlackListPresenter() {
        mModel = new BlackListModel();
    }

    @Override
    public void getBlacks(String savePath) {
        mSavePath = savePath;
        mModel.requestBlacks(new CallbackListener<List<User>>() {
            @Override
            public void onSuccess(List<User> data) {
                mUserList.clear();
                requestImg(new ArrayList<>(data));
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().showToast(throwable.getMessage());
                    mView.get().getBlacksFailed();
                    Log.i(TAG, throwable.getMessage());
                }
            }
        });
    }

    private void requestImg(List<User> users) {
        if (users != null && users.size() > 0) {
            User user = users.get(0);
            if (user.getUserImg() != null) {
                if (user.getUserImgPaht() == null || !FileUtils.fileIsExists(user.getUserImgPaht())) {
                    RequestImgModel model = new RequestImgModel();
                    model.requestImg(user.getUserImg(), mSavePath, user.getUserName() + "_other.jpg", new CallbackListener<String>() {
                        @Override
                        public void onSuccess(String data) {
                            user.setUserImgPaht(data);
                            mUserList.add(user);
                            users.remove(user);
                            requestImg(users);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            mUserList.add(user);
                            users.remove(user);
                            requestImg(users);
                        }
                    });
                } else {
                    mUserList.add(user);
                    users.remove(user);
                    requestImg(users);
                }
            } else {
                mUserList.add(user);
                users.remove(user);
                requestImg(users);
            }
        } else {
            if (isViewAttached()) {
                mView.get().setBlacksList(mUserList);
            }
        }

    }
}
