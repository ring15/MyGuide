package com.ring.myguide.post.view;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ring.myguide.R;
import com.ring.myguide.base.BaseActivity;
import com.ring.myguide.entity.Post;
import com.ring.myguide.post.PostContract;
import com.ring.myguide.post.presenter.PostPresenter;

public class PostActivity extends BaseActivity<PostPresenter, PostContract.View>
        implements PostContract.View {

    private RecyclerView mRecyclerView;
    private ImageView mFavoriteImg;
    private PostAdapter mAdapter;

    @Override
    protected int getIdResource() {
        return R.layout.activity_post;
    }

    @Override
    protected PostPresenter getPresenter() {
        return new PostPresenter();
    }

    @Override
    protected void findView() {
        mRecyclerView = findViewById(R.id.recycler_post);
        mFavoriteImg = findViewById(R.id.btn_favorite);
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        Post post = (Post) intent.getSerializableExtra("post");
        mAdapter = new PostAdapter(this);
        mAdapter.setCachePath(getCacheDir().getPath());
        mAdapter.setPresenter(mPresenter);
        mAdapter.setPost(post);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
        }
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

}
