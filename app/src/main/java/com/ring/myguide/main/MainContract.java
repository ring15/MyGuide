package com.ring.myguide.main;

import com.ring.myguide.base.BaseModel;
import com.ring.myguide.base.BasePresenter;
import com.ring.myguide.base.BaseView;

/**
 * Created by ring on 2019/11/18.
 */
public interface MainContract {
    interface Model extends BaseModel {
        String getCityFromCache();

        void putCityIntoCache(String city);
    }

    interface View extends BaseView {
        void setNormalTab(int count);

        void setSelectedTab(int count);

        void setHome();

        void setMessage();

        void setMe();

        void noUnreadMessage();

        void hasUnreadMessage();

        void showCity(String city);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void setCurrentItem(int currentItem);

        public abstract void init(int unreadCount);

        public abstract void getCity();

        public abstract void setCity(String city);
    }
}
