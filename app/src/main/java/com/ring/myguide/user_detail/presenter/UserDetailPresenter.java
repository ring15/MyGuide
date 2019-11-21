package com.ring.myguide.user_detail.presenter;

import android.util.Log;

import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.OtherUser;
import com.ring.myguide.entity.User;
import com.ring.myguide.user_detail.UserDetailContract;
import com.ring.myguide.user_detail.model.UserDetailModel;

/**
 * Created by ring on 2019/11/21.
 */
public class UserDetailPresenter extends UserDetailContract.Presenter {

    private static final String TAG = "UserDetailPresenter";

    private UserDetailContract.Model mModel;

    public UserDetailPresenter() {
        mModel = new UserDetailModel();
    }

    @Override
    public void init(String queryUserName) {
        User user = mModel.getUserFromCache();
        if (user == null || user.getUid() == null || queryUserName.equals(user.getUserName())) {
            mView.get().setOthers();
            return;
        }
        mModel.queryUser(queryUserName, new CallbackListener<OtherUser>() {
            @Override
            public void onSuccess(OtherUser data) {
                if (data == null || data.getUid() == null) {
                    mView.get().setOthers();
                } else if (data.getIsFriend() == 1 && data.getIsBlack() == 0) {
                    mView.get().setFriends();
                } else if (data.getIsFriend() == 1 && data.getIsBlack() == 1) {
                    mView.get().setFriendsAndBlacks();
                } else if (data.getIsFriend() == 0 && data.getIsBlack() == 1) {
                    mView.get().setBlacks();
                } else if (data.getIsFriend() == 0 && data.getIsBlack() == 0) {
                    mView.get().setStranger();
                } else {
                    mView.get().setOthers();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().onQueryFailed();
                    mView.get().showToast(throwable.getMessage());
                    Log.i(TAG, throwable.getMessage());
                }
            }
        });
    }

    @Override
    public void addFriend(String friendUserName) {
        mModel.addFriend(friendUserName, new CallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                if (isViewAttached()) {
                    mView.get().onAddFriendSuccess();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().onFailed();
                    mView.get().showToast(throwable.getMessage());
                    Log.i(TAG, throwable.getMessage());
                }
            }
        });
    }

    @Override
    public void deleteFriend(String friendUserName) {
        mModel.deleteFriend(friendUserName, new CallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                if (isViewAttached()) {
                    mView.get().onDeleteFriendSuccess();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().onFailed();
                    mView.get().showToast(throwable.getMessage());
                    Log.i(TAG, throwable.getMessage());
                }
            }
        });
    }

    @Override
    public void addBlack(String blackUserName) {
        mModel.addBlack(blackUserName, new CallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                if (isViewAttached()) {
                    mView.get().onAddBlackSuccess();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().onFailed();
                    mView.get().showToast(throwable.getMessage());
                    Log.i(TAG, throwable.getMessage());
                }
            }
        });
    }

    @Override
    public void deleteBlack(String blackUserName) {
        mModel.deleteBlack(blackUserName, new CallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                if (isViewAttached()) {
                    mView.get().onDeleteBlackSuccess();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().onFailed();
                    mView.get().showToast(throwable.getMessage());
                    Log.i(TAG, throwable.getMessage());
                }
            }
        });
    }
}
