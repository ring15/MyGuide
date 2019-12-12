package com.ring.myguide.post_list;

import com.ring.myguide.base.BasePresenter;
import com.ring.myguide.base.BaseView;
import com.ring.myguide.base.RequestImgListener;

/**
 * Created by ring on 2019/12/12.
 */
public interface PostListContract {

    interface View extends BaseView {

    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void requestImg(String img, String name, String savePath, RequestImgListener listener);
    }

}
