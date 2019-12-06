package com.ring.myguide.main.presenter;

import android.text.TextUtils;

import com.ring.myguide.main.MainContract;
import com.ring.myguide.main.model.MainModel;

import static com.ring.myguide.main.view.MainActivity.TAB_COUNTS;

/**
 * Created by ring on 2019/11/18.
 */
public class MainPresenter extends MainContract.Presenter {

    private MainContract.Model mModel;

    public MainPresenter() {
        mModel = new MainModel();
    }

    @Override
    public void setCurrentItem(int currentItem) {
        if (isViewAttached()) {
            if (currentItem == 0) {
                mView.get().setHome();
            } else if (currentItem == 1) {
                mView.get().setMessage();
            } else {
                mView.get().setMe();
            }
            for (int i = 0; i < TAB_COUNTS; i++) {
                if (i == currentItem) {
                    mView.get().setSelectedTab(i);
                } else {
                    mView.get().setNormalTab(i);
                }
            }
        }
    }

    @Override
    public void init(int unreadCount) {
        if (isViewAttached()) {
            if (unreadCount <= 0) {
                mView.get().noUnreadMessage();
            } else {
                mView.get().hasUnreadMessage();
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

    @Override
    public void setCity(String city) {
        if (!TextUtils.isEmpty(city)) {
            mModel.putCityIntoCache(city);
        }
    }
}
