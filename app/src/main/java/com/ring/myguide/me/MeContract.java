package com.ring.myguide.me;

import com.ring.myguide.base.BaseModel;
import com.ring.myguide.base.BasePresenter;
import com.ring.myguide.base.BaseView;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.Post;
import com.ring.myguide.entity.User;

import java.util.List;

/**
 * Created by ring on 2019/11/19.
 */
public interface MeContract {
    interface Model extends BaseModel {
        User getUserFromCache();

        void requestMyPost(CallbackListener<List<Post>> listener);

        void requestMyFavorite(CallbackListener<List<Post>> listener);

        void requestMyLike(CallbackListener<List<Post>> listener);
    }

    interface View extends BaseView {
        void setUser(User user);

        void setNoUser();

        void getImgSuccess(String path);

        void getImgFailed();

        void switchActivity(List<Post> posts, int title);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void init();

        public abstract void getImg(String photoPath, String savePath, String photoName);

        public abstract void getMyPost();

        public abstract void getMyFavorite();

        public abstract void getMyLike();
    }
}
