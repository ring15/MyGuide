package com.ring.myguide.post_list;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ring.myguide.R;
import com.ring.myguide.base.BaseActivity;
import com.ring.myguide.entity.Post;

import java.util.List;

public class PostListActivity extends BaseActivity<PostListPresenter, PostListContract.View>
        implements PostListContract.View {

    //标题
    private TextView tvTitle;
    private RecyclerView mPostListRecycler;
    private PostListAdapter mAdapter;

    @Override
    protected int getIdResource() {
        return R.layout.activity_post_list;
    }

    @Override
    protected PostListPresenter getPresenter() {
        return new PostListPresenter();
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        List<Post> posts = (List<Post>) intent.getSerializableExtra("post");
        //设置标题
        tvTitle.setText(title);
        mAdapter = new PostListAdapter(this);
        mAdapter.setCachePath(getCacheDir().getPath());
        mAdapter.setPresenter(mPresenter);
        mAdapter.setPosts(posts);
        mPostListRecycler.setAdapter(mAdapter);
        mPostListRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void findView() {
        tvTitle = findViewById(R.id.tv_title);
        mPostListRecycler = findViewById(R.id.recycler_post_list);
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
