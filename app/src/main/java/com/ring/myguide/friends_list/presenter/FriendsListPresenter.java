package com.ring.myguide.friends_list.presenter;

import android.util.Log;

import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.User;
import com.ring.myguide.friends_list.FriendsContract;
import com.ring.myguide.friends_list.model.FriendsListModel;

import java.util.List;

/**
 * Created by ring on 2019/11/20.
 */
public class FriendsListPresenter extends FriendsContract.Presenter {

    private static final String TAG = "FriendsListPresenter";

    private FriendsContract.Model mModel;

    public FriendsListPresenter() {
        mModel = new FriendsListModel();
    }

    @Override
    public void getFriends() {
        mModel.requestFriends(new CallbackListener<List<User>>() {
            @Override
            public void onSuccess(List<User> data) {
                if (isViewAttached()) {
                    mView.get().setFriendsList(data);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().showToast(throwable.getMessage());
                    mView.get().getFriendsFailed();
                    Log.i(TAG, throwable.getMessage());
                }
            }
        });
    }
}
