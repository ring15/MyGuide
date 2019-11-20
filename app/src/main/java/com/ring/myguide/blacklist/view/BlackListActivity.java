package com.ring.myguide.blacklist.view;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ring.myguide.R;
import com.ring.myguide.base.BaseActivity;
import com.ring.myguide.blacklist.BlacksContract;
import com.ring.myguide.blacklist.presenter.BlackListPresenter;
import com.ring.myguide.entity.User;

import java.util.List;

public class BlackListActivity extends BaseActivity<BlackListPresenter, BlacksContract.View>
        implements BlacksContract.View {

    //没有消息时
    private LinearLayout mNoBlacksLayout;
    //刷新
    private SwipeRefreshLayout mRefreshLayout;
    //对话列表
    private RecyclerView mBlacksRecyclerView;

    //recyclerview的adapter
    private BlacksAdapter mAdapter;

    @Override
    protected int getIdResource() {
        return R.layout.activity_black_list;
    }

    @Override
    protected BlackListPresenter getPresenter() {
        return new BlackListPresenter();
    }

    @Override
    protected void findView() {
        mNoBlacksLayout = findViewById(R.id.layout_no_blacks);
        mRefreshLayout = findViewById(R.id.refresh_blacks);
        mBlacksRecyclerView = findViewById(R.id.recycler_blacks);
    }

    @Override
    protected void init() {
        //获取好友列表
        mPresenter.getBlacks();
        //刷新监听事件
        mRefreshLayout.setOnRefreshListener(() -> mPresenter.getBlacks());
        //初始化adapter
        mAdapter = new BlacksAdapter(this);
        //绑定adapter
        mBlacksRecyclerView.setAdapter(mAdapter);
        //设置布局格式
        mBlacksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
        }
    }

    @Override
    public void setBlacksList(List<User> blacksList) {
        if (blacksList == null || blacksList.size() <= 0) {
            //获取到的列表为空则显示当前没有好友界面
            mNoBlacksLayout.setVisibility(View.VISIBLE);
        } else {
            //好友列表不为空，设置数据源
            mAdapter.setUserList(blacksList);
            //通知更新，刷新界面显示
            mAdapter.notifyDataSetChanged();
        }
        //刷新成功，不设置一直显示刷新
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getBlacksFailed() {
        mNoBlacksLayout.setVisibility(View.VISIBLE);
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
