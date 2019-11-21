package com.ring.myguide.friends_list.view;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ring.myguide.R;
import com.ring.myguide.base.BaseActivity;
import com.ring.myguide.entity.User;
import com.ring.myguide.friends_list.FriendsContract;
import com.ring.myguide.friends_list.presenter.FriendsListPresenter;

import java.util.List;

public class FriendsListActivity extends BaseActivity<FriendsListPresenter, FriendsContract.View>
        implements FriendsContract.View {

    //没有消息时
    private LinearLayout mNoFriendsLayout;
    //刷新
    private SwipeRefreshLayout mRefreshLayout;
    //对话列表
    private RecyclerView mFriendsRecyclerView;

    //recyclerview的adapter
    private FriendsAdapter mAdapter;

    @Override
    protected int getIdResource() {
        return R.layout.activity_friends_list;
    }

    @Override
    protected FriendsListPresenter getPresenter() {
        return new FriendsListPresenter();
    }

    @Override
    protected void findView() {
        mNoFriendsLayout = findViewById(R.id.layout_no_friends);
        mRefreshLayout = findViewById(R.id.refresh_friends);
        mFriendsRecyclerView = findViewById(R.id.recycler_friends);
    }

    @Override
    protected void init() {
        //刷新监听事件
        mRefreshLayout.setOnRefreshListener(() -> mPresenter.getFriends());
        //初始化adapter
        mAdapter = new FriendsAdapter(this);
        //绑定adapter
        mFriendsRecyclerView.setAdapter(mAdapter);
        //设置布局格式
        mFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取好友列表
        mPresenter.getFriends();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
        }
    }

    @Override
    public void setFriendsList(List<User> friendsList) {
        if (friendsList == null || friendsList.size() <= 0) {
            //获取到的列表为空则显示当前没有好友界面
            mNoFriendsLayout.setVisibility(View.VISIBLE);
        }else {
            //好友列表不为空，设置数据源
            mAdapter.setUserList(friendsList);
            //通知更新，刷新界面显示
            mAdapter.notifyDataSetChanged();
        }
        //刷新成功，不设置一直显示刷新
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getFriendsFailed() {
        mNoFriendsLayout.setVisibility(View.VISIBLE);
        //刷新失败，不设置一直显示刷新
        mRefreshLayout.setRefreshing(false);
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
