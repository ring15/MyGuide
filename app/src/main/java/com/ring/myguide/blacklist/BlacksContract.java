package com.ring.myguide.blacklist;

import com.ring.myguide.base.BaseModel;
import com.ring.myguide.base.BasePresenter;
import com.ring.myguide.base.BaseView;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.User;

import java.util.List;

/**
 * Created by ring on 2019/11/20.
 */
public interface BlacksContract {

    interface Model extends BaseModel {
        void requestBlacks(CallbackListener<List<User>> listener);
    }

    interface View extends BaseView {
        void setBlacksList(List<User> blacksList);

        void getBlacksFailed();
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void getBlacks(String savePath);
    }

}
