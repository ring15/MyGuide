package com.ring.myguide.query_post.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.ring.myguide.R;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.Post;
import com.ring.myguide.query_post.QueryPostContract;
import com.ring.myguide.query_post.model.QueryPostModel;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ring on 2019/12/17.
 */
public class QueryPostPresenter extends QueryPostContract.Presenter {

    private static final String TAG = "QueryPostPresenter";

    private QueryPostContract.Model mModel;

    public QueryPostPresenter() {
        mModel = new QueryPostModel();
    }

    @Override
    public void searchPost(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            mView.get().showToast(R.string.query_not_empty);
            return;
        }
        putIntoCache(keyword);
        mModel.requestPost(keyword, new CallbackListener<List<Post>>() {
            @Override
            public void onSuccess(List<Post> data) {
                if (isViewAttached()) {
                    mView.get().switchPosts(data);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().searchPostFailed();
                    mView.get().showToast(throwable.getMessage());
                    Log.e(TAG, throwable.getMessage());
                }
            }
        });
    }

    @Override
    public void getKeywords() {
        LinkedList<String> keywords = mModel.getKeywordsFromCache();
        if (isViewAttached()) {
            if (keywords != null && keywords.size() > 0) {
                mView.get().setKeyword(keywords);
            } else {
                mView.get().setNoSearch();
            }
        }
    }

    @Override
    public void clearHistory() {
        mModel.putKeywordsIntoCache(new LinkedList<>());
        getKeywords();
    }

    private void putIntoCache(String keyword) {
        LinkedList<String> keywords = mModel.getKeywordsFromCache();
        if (keywords == null) {
            keywords = new LinkedList<>();
            keywords.add(keyword);
            mModel.putKeywordsIntoCache(keywords);
        } else {
            keywords.remove(keyword);
            keywords.add(0, keyword);
            if (keywords.size() > 10) {
                for (int i = keywords.size() - 1; i >= 10; i--) {
                    keywords.remove(i);
                }
            }
            mModel.putKeywordsIntoCache(keywords);
        }
    }
}
