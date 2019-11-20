package com.ring.myguide.blacklist.presenter;

import android.util.Log;

import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.blacklist.BlacksContract;
import com.ring.myguide.blacklist.model.BlackListModel;
import com.ring.myguide.entity.User;

import java.util.List;

/**
 * Created by ring on 2019/11/20.
 */
public class BlackListPresenter extends BlacksContract.Presenter {

    private static final String TAG = "BlackListPresenter";

    private BlacksContract.Model mModel;

    public BlackListPresenter() {
        mModel = new BlackListModel();
    }

    @Override
    public void getBlacks() {
        mModel.requestBlacks(new CallbackListener<List<User>>() {
            @Override
            public void onSuccess(List<User> data) {
                if (isViewAttached()) {
                    mView.get().setBlacksList(data);
                }
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
}
