package com.ring.myguide.main.presenter;

import com.ring.myguide.main.MainContract;

import static com.ring.myguide.main.view.MainActivity.TAB_COUNTS;

/**
 * Created by ring on 2019/11/18.
 */
public class MainPresenter extends MainContract.Presenter {
    @Override
    public void setCurrentItem(int currentItem) {
        for (int i = 0; i < TAB_COUNTS; i++) {
            if (i == currentItem) {
                mView.get().setSelectedTab(i);
            } else {
                mView.get().setNormalTab(i);
            }
        }
    }
}