package com.ring.myguide.post.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.ring.myguide.R;
import com.ring.myguide.RequestImgModel;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.base.RequestImgListener;
import com.ring.myguide.entity.Reply;
import com.ring.myguide.entity.User;
import com.ring.myguide.post.PostContract;
import com.ring.myguide.post.model.PostModel;

import java.util.List;

/**
 * Created by ring on 2019/12/13.
 */
public class PostPresenter extends PostContract.Presenter {

    private static final String TAG = "PostPresenter";

    private PostContract.Model mModel;
    private User mUser;

    public PostPresenter() {
        mModel = new PostModel();
    }

    @Override
    public void init(int threadId) {
        mUser = mModel.getUserFromCache();
        if (mUser != null && mUser.getUserName() != null) {
            mView.get().setUser(mUser);
        } else {
            mView.get().setNoUser();
        }
        getReply(threadId);
    }

    private void getReply(int threadId) {
        mModel.requestReply(threadId, new CallbackListener<List<Reply>>() {
            @Override
            public void onSuccess(List<Reply> data) {
                if (data != null && data.size() > 0) {
                    if (isViewAttached()) {
                        mView.get().setReplies(data);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, throwable.getMessage());
            }
        });
    }

    @Override
    public void requestImg(String img, String name, String savePath, RequestImgListener listener) {
        RequestImgModel model = new RequestImgModel();
        model.requestImg(img, savePath, name, new CallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                listener.onSuccess();
            }

            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, throwable.getMessage());
                listener.onFailed();
            }
        });
    }

    @Override
    public void sendReply(String reply, int threadId) {
        if (TextUtils.isEmpty(reply)) {
            if (isViewAttached()) {
                mView.get().showToast(R.string.post_send_reply_empty);
            }
            return;
        }
        if (mUser == null || mUser.getUserName() == null) {
            if (isViewAttached()) {
                mView.get().showToast(R.string.post_send_reply_not_login);
            }
            return;
        }
        mModel.doSendReply(reply, threadId, new CallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                if (isViewAttached()) {
                    mView.get().showToast(R.string.post_send_reply_success);
                    mView.get().sendSuccess();
                    getReply(threadId);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().showToast(throwable.getMessage());
                    mView.get().sendFailure();
                }
                Log.i(TAG, throwable.getMessage());
            }
        });
    }

    @Override
    public void favorite(int threadId) {
        if (mUser == null || mUser.getUserName() == null) {
            if (isViewAttached()) {
                mView.get().showToast(R.string.post_send_reply_not_login);
            }
            return;
        }
        mModel.doFavorite(threadId, new CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                if (isViewAttached()) {
                    mView.get().showToast(R.string.post_operate_success);
                    if (data) {
                        mView.get().onFavorite();
                    } else {
                        mView.get().onNotFavorite();
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().showToast(throwable.getMessage());
                }
                Log.i(TAG, throwable.getMessage());
            }
        });
    }

    @Override
    public void good(int threadId) {
        if (mUser == null || mUser.getUserName() == null) {
            if (isViewAttached()) {
                mView.get().showToast(R.string.post_send_reply_not_login);
            }
            return;
        }
        mModel.doGood(threadId, new CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                if (isViewAttached()) {
                    mView.get().showToast(R.string.post_operate_success);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().showToast(throwable.getMessage());
                }
                Log.i(TAG, throwable.getMessage());
            }
        });
    }

    @Override
    public void changeBoutique(int threadId) {
        mModel.doChangeBoutique(threadId, new CallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                if (isViewAttached()) {
                    mView.get().showToast(R.string.post_operate_success);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().showToast(throwable.getMessage());
                }
                Log.i(TAG, throwable.getMessage());
            }
        });
    }

    @Override
    public void changeType(int threadId, int type) {
        mModel.doChangeType(threadId, type, new CallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                if (isViewAttached()) {
                    mView.get().showToast(R.string.post_operate_success);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().showToast(throwable.getMessage());
                }
                Log.i(TAG, throwable.getMessage());
            }
        });
    }

    @Override
    public void changeDelete(int threadId) {
        mModel.doChangeDelete(threadId, new CallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                if (isViewAttached()) {
                    mView.get().showToast(R.string.post_operate_success);
                    mView.get().finishActivity();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().showToast(throwable.getMessage());
                }
                Log.i(TAG, throwable.getMessage());
            }
        });
    }

    @Override
    public void deleteReply(int threadId, int floor) {
        mModel.deleteReply(threadId, floor, new CallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                if (isViewAttached()) {
                    mView.get().showToast(R.string.post_operate_success);
                    getReply(threadId);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().showToast(throwable.getMessage());
                }
                Log.i(TAG, throwable.getMessage());
            }
        });
    }
}
