package com.ring.myguide.main;

import com.ring.myguide.base.BaseModel;
import com.ring.myguide.base.BasePresenter;
import com.ring.myguide.base.BaseView;

/**
 * Created by ring on 2019/11/18.
 */
public interface MainContract {
    interface Model extends BaseModel {
    }

    interface View extends BaseView {
        void setNormalTab(int count);

        void setSelectedTab(int count);

        void setHome();

        void setMessage();

        void setMe();
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void setCurrentItem(int currentItem);
    }
}
