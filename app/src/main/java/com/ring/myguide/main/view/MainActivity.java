package com.ring.myguide.main.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.ring.myguide.R;
import com.ring.myguide.base.BaseActivity;
import com.ring.myguide.base.MyFragmentPagerAdapter;
import com.ring.myguide.blacklist.view.BlackListActivity;
import com.ring.myguide.friends_list.view.FriendsListActivity;
import com.ring.myguide.home.HomeFragment;
import com.ring.myguide.main.MainContract;
import com.ring.myguide.main.presenter.MainPresenter;
import com.ring.myguide.me.view.MeFragment;
import com.ring.myguide.message.view.MessageFragment;
import com.ring.myguide.query_user.view.QueryUserActivity;

import java.util.List;

public class MainActivity extends BaseActivity<MainPresenter, MainContract.View> implements MainContract.View,
        HomeFragment.OnFragmentInteractionListener, MessageFragment.OnFragmentInteractionListener,
        MeFragment.OnFragmentInteractionListener, PopupMenu.OnMenuItemClickListener {

    //版块的数量
    public static final int TAB_COUNTS = 3;

    //定位的点击位置
    private LinearLayout mLocalLayout;
    //定位地址
    private TextView mLocalText;
    //标题
    private TextView mTitleText;
    //发帖按钮
    private ImageView mAddImg;
    //搜索按钮
    private ImageView mSearchImg;
    //消息界面菜单按钮
    private ImageView mMenuImg;
    //切换版块
    private ViewPager mViewPager;
    //首页版块
    private RelativeLayout mHomeLayout;
    //首页图片
    private ImageView mHomeImg;
    //首页文字
    private TextView mHomeText;
    //消息版块
    private RelativeLayout mMessageLayout;
    //消息图片
    private ImageView mMessageImg;
    //消息文字
    private TextView mMessageText;
    //消息小红点
    private View mRedPoint;
    //我的版块
    private RelativeLayout mMeLayout;
    //我的图片
    private ImageView mMeImg;
    //我的文字
    private TextView mMeText;

    //fragment管理器
    private FragmentManager mFManager;
    //fragment集合
    private Fragment[] mFragments = new Fragment[TAB_COUNTS];
    //当前选中fragment
    private int mCurrentItem = 0;
    //底部tab中版块集合
    private RelativeLayout[] mRelativeLayouts = new RelativeLayout[TAB_COUNTS];
    //底部tab中图片集合
    private ImageView[] mImageViews = new ImageView[TAB_COUNTS];
    //底部tab中文字集合
    private TextView[] mTextViews = new TextView[TAB_COUNTS];
    //标题名
    private int[] mTitleTexts = {R.string.home, R.string.message, R.string.me};

    //刷新主线程
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mRedPoint.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected int getIdResource() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter getPresenter() {
        return new MainPresenter();
    }

    @Override
    protected void findView() {
        mLocalLayout = findViewById(R.id.layout_local);
        mLocalText = findViewById(R.id.tv_local);
        mTitleText = findViewById(R.id.tv_title);
        mAddImg = findViewById(R.id.btn_add);
        mSearchImg = findViewById(R.id.btn_search);
        mMenuImg = findViewById(R.id.btn_menu);
        mViewPager = findViewById(R.id.home_view_pager);
        mHomeLayout = findViewById(R.id.layout_home);
        mHomeImg = findViewById(R.id.img_home);
        mHomeText = findViewById(R.id.tv_home);
        mMessageLayout = findViewById(R.id.layout_message);
        mMessageImg = findViewById(R.id.img_message);
        mMessageText = findViewById(R.id.tv_message);
        mRedPoint = findViewById(R.id.red_point);
        mMeLayout = findViewById(R.id.layout_me);
        mMeImg = findViewById(R.id.img_me);
        mMeText = findViewById(R.id.tv_me);
    }

    @Override
    protected void init() {
        //将版块添加到集合中
        mRelativeLayouts[0] = mHomeLayout;
        mRelativeLayouts[1] = mMessageLayout;
        mRelativeLayouts[2] = mMeLayout;

        //将图片添加到集合中
        mImageViews[0] = mHomeImg;
        mImageViews[1] = mMessageImg;
        mImageViews[2] = mMeImg;

        //将文字添加到集合中
        mTextViews[0] = mHomeText;
        mTextViews[1] = mMessageText;
        mTextViews[2] = mMeText;

        mFManager = getSupportFragmentManager();
        //建立fragment
        mFragments[0] = HomeFragment.newInstance("", "");
        mFragments[1] = MessageFragment.newInstance("", "");
        mFragments[2] = MeFragment.newInstance("", "");

        //将fragment连接到viewpager
        mViewPager.setAdapter(new MyFragmentPagerAdapter(mFManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mFragments));
        mViewPager.setOffscreenPageLimit(TAB_COUNTS);
        mViewPager.setCurrentItem(mCurrentItem);
        mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());

        //设置当前选中版块
        mPresenter.setCurrentItem(mCurrentItem);

        //注册环信消息监听
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int num = EMClient.getInstance().chatManager().getUnreadMessageCount();
        mPresenter.init(num);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_home:
                mCurrentItem = 0;
                mViewPager.setCurrentItem(mCurrentItem);
                mPresenter.setCurrentItem(mCurrentItem);
                break;
            case R.id.layout_message:
                mCurrentItem = 1;
                mViewPager.setCurrentItem(mCurrentItem);
                mPresenter.setCurrentItem(mCurrentItem);
                break;
            case R.id.layout_me:
                mCurrentItem = 2;
                mViewPager.setCurrentItem(mCurrentItem);
                mPresenter.setCurrentItem(mCurrentItem);
                break;
            case R.id.btn_menu:
                //创建弹出式菜单对象（最低版本11）
                PopupMenu popup = new PopupMenu(this, mMenuImg);//第二个参数是绑定的那个view
                //获取菜单填充器
                MenuInflater inflater = popup.getMenuInflater();
                //填充菜单
                inflater.inflate(R.menu.menu_main, popup.getMenu());
                //绑定菜单项的点击事件
                popup.setOnMenuItemClickListener(this);
                popup.show(); //这一行代码不要忘记了
                break;
        }
    }


    //弹出式菜单的单击事件处理
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.item_search_friend:
                startActivity(new Intent(MainActivity.this, QueryUserActivity.class));
                break;

            case R.id.item_friend_list:
                startActivity(new Intent(MainActivity.this, FriendsListActivity.class));
                break;

            case R.id.item_black_list:
                startActivity(new Intent(MainActivity.this, BlackListActivity.class));
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * 设置未选中版块格式
     *
     * @param count
     */
    @Override
    public void setNormalTab(int count) {
        mImageViews[count].setSelected(false);
        mTextViews[count].setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    /**
     * 设置选中版块格式
     *
     * @param count
     */
    @Override
    public void setSelectedTab(int count) {
        mImageViews[count].setSelected(true);
        mTextViews[count].setTextColor(ContextCompat.getColor(this, R.color.foreGround));
        mTitleText.setText(mTitleTexts[count]);
    }

    /**
     * 当前界面为首页时图标显示
     */
    @Override
    public void setHome() {
        mLocalLayout.setVisibility(View.VISIBLE);
        mAddImg.setVisibility(View.VISIBLE);
        mSearchImg.setVisibility(View.VISIBLE);
        mMenuImg.setVisibility(View.GONE);
    }

    /**
     * 当前界面为消息时图标显示
     */
    @Override
    public void setMessage() {
        mLocalLayout.setVisibility(View.GONE);
        mAddImg.setVisibility(View.GONE);
        mSearchImg.setVisibility(View.GONE);
        mMenuImg.setVisibility(View.VISIBLE);
    }

    /**
     * 当前界面为我的时图标显示
     */
    @Override
    public void setMe() {
        mLocalLayout.setVisibility(View.GONE);
        mAddImg.setVisibility(View.GONE);
        mSearchImg.setVisibility(View.GONE);
        mMenuImg.setVisibility(View.GONE);
    }

    @Override
    public void noUnreadMessage() {
        mRedPoint.setVisibility(View.GONE);
    }

    @Override
    public void hasUnreadMessage() {
        mRedPoint.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    /**
     * 这个是fragment的回调，用来实现fragment和activity之间的通信
     *
     * @param uri
     */
    @Override
    public void onFragmentInteraction(Uri uri) {
        String text = uri.toString();
        if ("false".equals(text)) {
            mMenuImg.setClickable(false);
        } else if ("true".equals(text)) {
            mMenuImg.setClickable(true);
        }
        if ("visible".equals(text)){
            mRedPoint.setVisibility(View.VISIBLE);
        } else if ("gone".equals(text)){
            mRedPoint.setVisibility(View.GONE);
        }
    }

    //环信消息监听
    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            ((MessageFragment) mFragments[1]).onMessageReceived(messages);
            mHandler.sendEmptyMessage(0x01);
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            ((MessageFragment) mFragments[1]).onCmdMessageReceived(messages);
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
            ((MessageFragment) mFragments[1]).onMessageRead(messages);
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
            ((MessageFragment) mFragments[1]).onMessageDelivered(message);
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            //消息被撤回
            ((MessageFragment) mFragments[1]).onMessageRecalled(messages);
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
            ((MessageFragment) mFragments[1]).onMessageChanged(message, change);
        }
    };

    /**
     * ViewPager滑动监听事件
     */
    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //滑动到下一个界面时调用该方法
            mCurrentItem = position;
            mPresenter.setCurrentItem(mCurrentItem);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在不需要的时候移除listener，如在activity的onDestroy()时
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }
}
