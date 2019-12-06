package com.ring.myguide.send_post;

import com.ring.myguide.base.BaseModel;
import com.ring.myguide.base.BasePresenter;
import com.ring.myguide.base.BaseView;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.User;

import java.io.File;
import java.util.List;

/**
 * Created by ring on 2019/12/3.
 */
public interface SendPostContract {
    interface Model extends BaseModel {
        void addPost(String title, String content, List<String> path, String city,
                     int isBoutique, int type, CallbackListener<String> listener);

        void upload(List<File> files, CallbackListener<List<String>> listener);

        User getUserFromCache();

        String getCityFromCache();
    }

    interface View extends BaseView {
        void sendSuccess();

        void sendFailed();

        void setOfficialBadge();

        void setNormalBadge();

        void showCity(String city);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void sendPost(String title, String content, List<String> paths,
                                      String city, int isBoutique, int type);

        public abstract void getUserBadge();

        public abstract void getCity();
    }
}
