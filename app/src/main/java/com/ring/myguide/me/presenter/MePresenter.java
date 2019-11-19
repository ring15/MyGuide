package com.ring.myguide.me.presenter;

import com.ring.myguide.entity.User;
import com.ring.myguide.me.MeContract;
import com.ring.myguide.me.model.MeModel;

/**
 * Created by ring on 2019/11/19.
 */
public class MePresenter extends MeContract.Presenter {

    private MeContract.Model mModel;

    public MePresenter() {
        mModel = new MeModel();
    }

    @Override
    public void init() {
        User user = mModel.getUserFromCache();
        if (isViewAttached()) {
            if (user != null && user.getUid() != null) {
                mView.get().setUser(user);
            } else {
                mView.get().setNoUser();
            }
        }
    }

}
