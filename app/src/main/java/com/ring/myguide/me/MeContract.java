package com.ring.myguide.me;

import com.ring.myguide.base.BaseModel;
import com.ring.myguide.base.BasePresenter;
import com.ring.myguide.base.BaseView;
import com.ring.myguide.entity.User;

/**
 * Created by ring on 2019/11/19.
 */
public interface MeContract {
    interface Model extends BaseModel {
        User getUserFromCache();
    }

    interface View extends BaseView {
        void setUser(User user);

        void setNoUser();
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void init();
    }
}
