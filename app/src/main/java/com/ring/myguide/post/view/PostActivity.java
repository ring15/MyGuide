package com.ring.myguide.post.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ring.myguide.R;
import com.ring.myguide.base.BaseActivity;
import com.ring.myguide.entity.Post;
import com.ring.myguide.entity.Reply;
import com.ring.myguide.entity.User;
import com.ring.myguide.post.PostContract;
import com.ring.myguide.post.presenter.PostPresenter;

import java.util.List;

public class PostActivity extends BaseActivity<PostPresenter, PostContract.View>
        implements PostContract.View {

    private RecyclerView mRecyclerView;
    private ImageView mFavoriteImg;
    private PostAdapter mAdapter;
    private EditText mMessageEdit;
    private FloatingActionButton mFAB;
    //帖子信息
    private Post mPost;
    //是否收藏
    private boolean isFavorite;

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
        mMessageEdit = findViewById(R.id.et_message);
        mFAB = findViewById(R.id.fab_post);
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        mPost = (Post) intent.getSerializableExtra("post");
        isFavorite = mPost.isFavorite();
        if (isFavorite) {
            mFavoriteImg.setSelected(true);
        } else {
            mFavoriteImg.setSelected(false);
        }
        mAdapter = new PostAdapter(this);
        mAdapter.setCachePath(getCacheDir().getPath());
        mAdapter.setPresenter(mPresenter);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPresenter.init(mPost.getThreadID());
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.btn_favorite:
                mPresenter.favorite(mPost == null ? 0 : mPost.getThreadID());
                if (isFavorite) {
                    onFavorite();
                } else {
                    onNotFavorite();
                }
                break;
            case R.id.btn_send:
                String reply = mMessageEdit.getText().toString().trim();
                mPresenter.sendReply(reply, mPost == null ? 0 : mPost.getThreadID());
                break;
            case R.id.et_message:
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.showSoftInput(mMessageEdit, 0);
                }
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

    @Override
    public void setReplies(List<Reply> replies) {
        mAdapter.setReplies(replies);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setUser(User user) {
        mAdapter.setPost(mPost);
        mAdapter.setUser(user);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setNoUser() {
        mAdapter.setPost(mPost);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void sendSuccess() {
        mMessageEdit.setText("");
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(getWindow().peekDecorView().getWindowToken(), 0);
        }
    }

    @Override
    public void sendFailure() {

    }

    @Override
    public void onFavorite() {
        mFavoriteImg.setSelected(true);
        isFavorite = true;
    }

    @Override
    public void onNotFavorite() {
        mFavoriteImg.setSelected(false);
        isFavorite = false;
    }
}
