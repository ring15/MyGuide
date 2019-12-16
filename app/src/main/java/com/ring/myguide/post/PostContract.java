package com.ring.myguide.post;

import com.ring.myguide.base.BaseModel;
import com.ring.myguide.base.BasePresenter;
import com.ring.myguide.base.BaseView;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.base.RequestImgListener;
import com.ring.myguide.entity.Reply;
import com.ring.myguide.entity.User;

import java.util.List;

/**
 * Created by ring on 2019/12/13.
 */
public interface PostContract {
    interface Model extends BaseModel {
        void doSendReply(String reply, int threadId, CallbackListener<String> listener);

        void requestReply(int threadId, CallbackListener<List<Reply>> listener);

        void doFavorite(int threadId, CallbackListener<Boolean> listener);

        void doGood(int threadId, CallbackListener<Boolean> listener);

        void doChangeBoutique(int threadId, CallbackListener<String> listener);

        void doChangeType(int threadId, int type, CallbackListener<String> listener);

        void doChangeDelete(int threadId, CallbackListener<String> listener);

        void deleteReply(int threadId, int floor, CallbackListener<String> listener);

        User getUserFromCache();
    }

    interface View extends BaseView {
        void setReplies(List<Reply> replies);

        void setUser(User user);

        void setNoUser();

        void sendSuccess();

        void sendFailure();

        void onFavorite();

        void onNotFavorite();

        void finishActivity();
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void init(int threadId);

        public abstract void requestImg(String img, String name, String savePath, RequestImgListener listener);

        public abstract void sendReply(String reply, int threadId);

        public abstract void favorite(int threadId);

        public abstract void good(int threadId);

        public abstract void changeBoutique(int threadId);

        public abstract void changeType(int threadId, int type);

        public abstract void changeDelete(int threadId);

        public abstract void deleteReply(int threadId, int floor);
    }
}
