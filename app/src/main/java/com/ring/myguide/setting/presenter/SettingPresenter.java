package com.ring.myguide.setting.presenter;

import com.ring.myguide.entity.User;
import com.ring.myguide.setting.SettingContract;
import com.ring.myguide.setting.model.SettingModel;

/**
 * Created by ring on 2019/11/19.
 */
public class SettingPresenter extends SettingContract.Presenter {

    private SettingContract.Model mModel;

    public SettingPresenter() {
        mModel = new SettingModel();
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
