package com.ring.myguide.main.view;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ring.myguide.R;
import com.ring.myguide.base.BaseActivity;
import com.ring.myguide.base.MyFragmentPagerAdapter;
import com.ring.myguide.home.HomeFragment;
import com.ring.myguide.main.MainContract;
import com.ring.myguide.main.presenter.MainPresenter;
import com.ring.myguide.me.MeFragment;
import com.ring.myguide.message.MessageFragment;

public class MainActivity extends BaseActivity<MainPresenter, MainContract.View> implements MainContract.View,
        HomeFragment.OnFragmentInteractionListener, MessageFragment.OnFragmentInteractionListener,
        MeFragment.OnFragmentInteractionListener {

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
        mViewPager.addOnPageChangeListener(new MyOnPageChangeListner());

        //设置当前选中版块
        mPresenter.setCurrentItem(mCurrentItem);
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
        }
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

    }

    /**
     * ViewPager滑动监听事件
     */
    private class MyOnPageChangeListner implements ViewPager.OnPageChangeListener {
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
}
