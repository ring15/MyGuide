package com.ring.myguide.query_post;

import com.ring.myguide.base.BaseModel;
import com.ring.myguide.base.BasePresenter;
import com.ring.myguide.base.BaseView;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.Post;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ring on 2019/12/17.
 */
public interface QueryPostContract {

    interface Model extends BaseModel {
        void putKeywordsIntoCache(LinkedList<String> keywords);

        LinkedList<String> getKeywordsFromCache();

        void requestPost(String keyword, CallbackListener<List<Post>> listener);
    }

    interface View extends BaseView {
        void switchPosts(List<Post> posts);

        void setNoSearch();

        void setKeyword(LinkedList<String> keywords);

        void searchPostFailed();
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void searchPost(String keyword);

        public abstract void getKeywords();

        public abstract void clearHistory();
    }
}
