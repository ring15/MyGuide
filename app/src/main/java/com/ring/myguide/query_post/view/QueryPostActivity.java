package com.ring.myguide.query_post.view;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ring.myguide.R;
import com.ring.myguide.base.BaseActivity;
import com.ring.myguide.entity.Post;
import com.ring.myguide.post_list.PostListActivity;
import com.ring.myguide.query_post.QueryPostContract;
import com.ring.myguide.query_post.presenter.QueryPostPresenter;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class QueryPostActivity extends BaseActivity<QueryPostPresenter, QueryPostContract.View>
        implements QueryPostContract.View {

    //搜索输入框
    private EditText mSearchEdit;
    //暂无搜索记录
    private LinearLayout mNoHistoryLayout;
    //搜索记录列表
    private ListView mListView;

    @Override
    protected int getIdResource() {
        return R.layout.activity_query_post;
    }

    @Override
    protected QueryPostPresenter getPresenter() {
        return new QueryPostPresenter();
    }

    @Override
    protected void findView() {
        mSearchEdit = findViewById(R.id.et_search);
        mNoHistoryLayout = findViewById(R.id.layout_no_history);
        mListView = findViewById(R.id.listview);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取历史缓存记录
        mPresenter.getKeywords();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.tv_clear:
                mPresenter.clearHistory();
                break;
            case R.id.img_search:
                //开始查询
                mPresenter.searchPost(mSearchEdit.getText().toString().trim());
                break;
        }
    }

    @Override
    public void switchPosts(List<Post> posts) {
        Intent intent = new Intent(this, PostListActivity.class);
        intent.putExtra("title", getString(R.string.query_search_result));
        intent.putExtra("post", (Serializable) posts);
        startActivity(intent);
    }

    @Override
    public void setNoSearch() {
        mNoHistoryLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setKeyword(LinkedList<String> keywords) {
        mNoHistoryLayout.setVisibility(View.GONE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                QueryPostActivity.this, android.R.layout.simple_list_item_1, keywords);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击历史记录开始查询
                mPresenter.searchPost(keywords.get(position));
            }
        });
    }

    @Override
    public void searchPostFailed() {
        mSearchEdit.setText("");
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
