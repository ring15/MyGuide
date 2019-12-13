package com.ring.myguide.post;

import com.ring.myguide.base.BaseModel;
import com.ring.myguide.base.BasePresenter;
import com.ring.myguide.base.BaseView;
import com.ring.myguide.base.RequestImgListener;

/**
 * Created by ring on 2019/12/13.
 */
public interface PostContract {
    interface Model extends BaseModel {

    }

    interface View extends BaseView {

    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void requestImg(String img, String name, String savePath, RequestImgListener listener);
    }
}
