package com.ring.myguide.query_user.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.ring.myguide.R;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.User;
import com.ring.myguide.query_user.QueryUserContract;
import com.ring.myguide.query_user.model.QueryUserModel;

import java.util.LinkedList;

/**
 * Created by ring on 2019/11/21.
 */
public class QueryUserPresenter extends QueryUserContract.Presenter {

    private static final String TAG = "QueryUserPresenter";

    private QueryUserContract.Model mModel;

    public QueryUserPresenter() {
        mModel = new QueryUserModel();
    }

    @Override
    public void getUser(String username) {
        if (TextUtils.isEmpty(username)) {
            mView.get().showToast(R.string.query_not_empty);
            return;
        }
        mModel.requestUser(username, new CallbackListener<User>() {
            @Override
            public void onSuccess(User data) {
                if (isViewAttached()) {
                    putIntoCache(data);
                    mView.get().setUser(data);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().getUserFailed();
                    mView.get().showToast(throwable.getMessage());
                    Log.e(TAG, throwable.getMessage());
                }
            }
        });
    }

    private void putIntoCache(User data) {
        LinkedList<String> usernames = mModel.getUserNameFromCache();
        if (usernames == null) {
            usernames = new LinkedList<>();
            usernames.add(data.getUserName());
            mModel.putUserNameIntoCache(usernames);
        } else {
            usernames.remove(data.getUserName());
            usernames.add(0, data.getUserName());
            if (usernames.size() > 10) {
                for (int i = usernames.size() - 1; i >= 10; i--) {
                    usernames.remove(i);
                }
            }
            mModel.putUserNameIntoCache(usernames);
        }
    }

    @Override
    public void getUserName() {
        LinkedList<String> usernames = mModel.getUserNameFromCache();
        if (isViewAttached()) {
            if (usernames != null && usernames.size() > 0) {
                mView.get().setUserName(usernames);
            } else {
                mView.get().setNoUser();
            }
        }
    }

    @Override
    public void clearHistory() {
        mModel.putUserNameIntoCache(new LinkedList<>());
        getUserName();
    }
}
