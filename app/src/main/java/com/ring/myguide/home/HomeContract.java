package com.ring.myguide.home;

import com.ring.myguide.base.BaseModel;
import com.ring.myguide.base.BasePresenter;
import com.ring.myguide.base.BaseView;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.base.RequestImgListener;
import com.ring.myguide.entity.HomePage;

/**
 * Created by ring on 2019/12/2.
 */
public interface HomeContract {

    interface Model extends BaseModel {
        void requestHomePage(String province, CallbackListener<HomePage> listener);

        String getCityFromCache();

    }

    interface View extends BaseView {
        void getHomePageSuccess(HomePage homePage);

        void getHomePageFailed();

        void setCity(String city);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void getHomePage(String province, String savePath);

        public abstract void getCity();

        public abstract void requestImg(String img, String name, String savePath, RequestImgListener listener);
    }
}
