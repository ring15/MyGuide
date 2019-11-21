package com.ring.myguide.query_user.view;

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
import com.ring.myguide.entity.User;
import com.ring.myguide.query_user.QueryUserContract;
import com.ring.myguide.query_user.presenter.QueryUserPresenter;
import com.ring.myguide.user_detail.view.UserDetailActivity;

import java.util.LinkedList;

public class QueryUserActivity extends BaseActivity<QueryUserPresenter, QueryUserContract.View>
        implements QueryUserContract.View {

    //搜索输入框
    private EditText mSearchEdit;
    //暂无搜索记录
    private LinearLayout mNoHistoryLayout;
    //搜索记录列表
    private ListView mListView;

    @Override
    protected int getIdResource() {
        return R.layout.activity_query_user;
    }

    @Override
    protected QueryUserPresenter getPresenter() {
        return new QueryUserPresenter();
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
        mPresenter.getUserName();
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
                mPresenter.getUser(mSearchEdit.getText().toString().trim());
                break;
        }
    }

    /**
     * 展示历史搜索记录
     *
     * @param userName
     */
    @Override
    public void setUserName(LinkedList<String> userName) {
        mNoHistoryLayout.setVisibility(View.GONE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                QueryUserActivity.this, android.R.layout.simple_list_item_1, userName);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击历史记录开始查询
                mPresenter.getUser(userName.get(position));
            }
        });
    }

    /**
     * 没有历史搜索记录
     */
    @Override
    public void setNoUser() {
        mNoHistoryLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 查询到用户，跳转到用户详情界面
     *
     * @param user
     */
    @Override
    public void setUser(User user) {
        mSearchEdit.setText("");
        Intent intent = new Intent(QueryUserActivity.this, UserDetailActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void getUserFailed() {
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
