package com.ring.myguide.me.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ring.myguide.R;
import com.ring.myguide.base.BaseFragment;
import com.ring.myguide.center.view.CenterActivity;
import com.ring.myguide.entity.Post;
import com.ring.myguide.entity.User;
import com.ring.myguide.login.view.LoginActivity;
import com.ring.myguide.me.MeContract;
import com.ring.myguide.me.presenter.MePresenter;
import com.ring.myguide.post_list.PostListActivity;
import com.ring.myguide.setting.view.SettingActivity;
import com.ring.myguide.utils.FileUtils;

import java.io.Serializable;
import java.util.List;

import static com.ring.myguide.login.view.LoginActivity.FROM_MEFRAGMENT;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeFragment extends BaseFragment<MePresenter, MeContract.View>
        implements MeContract.View, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //刷新
    private SwipeRefreshLayout mRefreshLayout;

    //用户头像
    private ImageView mUserAvatar;
    //用户名
    private TextView mNickName;
    //管理员图标
    private ImageView mManagerImg;
    //用户id
    private TextView mUserName;
    //个人简介layout
    private LinearLayout mIntroduceLayout;
    //个人简介内容
    private TextView mIntroduceText;

    //个人资料
    private LinearLayout mUserInfoLayout;
    //我的帖子
    private LinearLayout mMyPostLayout;
    //我的收藏
    private LinearLayout mFavoriteLayout;
    //我的点赞
    private LinearLayout mLikeLayout;
    //设置
    private LinearLayout mSettingLayout;

    //判断是否为已登录状态
    private boolean isLogin = false;

    public MeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected int getIdResource() {
        return R.layout.fragment_me;
    }

    @Override
    protected void findView(View view) {
        mRefreshLayout = view.findViewById(R.id.layout_refresh);

        mUserAvatar = view.findViewById(R.id.img_user_avatar);
        mNickName = view.findViewById(R.id.tv_nickname);
        mManagerImg = view.findViewById(R.id.img_manager);
        mUserName = view.findViewById(R.id.tv_username);
        mIntroduceLayout = view.findViewById(R.id.layout_introduce);
        mIntroduceText = view.findViewById(R.id.tv_introduce);

        mUserInfoLayout = view.findViewById(R.id.layout_user_info);
        mMyPostLayout = view.findViewById(R.id.layout_my_post);
        mFavoriteLayout = view.findViewById(R.id.layout_favorite);
        mLikeLayout = view.findViewById(R.id.layout_like);
        mSettingLayout = view.findViewById(R.id.layout_setting);
    }

    @Override
    protected void init() {
        mRefreshLayout.setOnRefreshListener(() -> {
            mPresenter.init();
        });
        mUserInfoLayout.setOnClickListener(this);
        mMyPostLayout.setOnClickListener(this);
        mFavoriteLayout.setOnClickListener(this);
        mLikeLayout.setOnClickListener(this);
        mSettingLayout.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.init();
    }

    @Override
    protected MePresenter getPresenter() {
        return new MePresenter();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_user_info:
                if (isLogin) {
                    startActivity(new Intent(getActivity(), CenterActivity.class));
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("from", FROM_MEFRAGMENT);
                    startActivity(intent);
                }
                break;
            case R.id.layout_my_post:
                if (isLogin) {
                    mPresenter.getMyPost();
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("from", FROM_MEFRAGMENT);
                    startActivity(intent);
                }
                break;
            case R.id.layout_favorite:
                if (isLogin) {
                    mPresenter.getMyFavorite();
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("from", FROM_MEFRAGMENT);
                    startActivity(intent);
                }
                break;
            case R.id.layout_like:
                if (isLogin) {
                    mPresenter.getMyLike();
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("from", FROM_MEFRAGMENT);
                    startActivity(intent);
                }
                break;
            case R.id.layout_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String toast) {
        Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUser(User user) {
        isLogin = true;
        if (user.getUserImgPaht() != null && FileUtils.fileIsExists(user.getUserImgPaht())) {
            Glide.with(this)
                    .load(user.getUserImgPaht())
                    .error(R.drawable.icon_avatar_default)
                    .placeholder(R.drawable.icon_avatar_default)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .skipMemoryCache(true) // 不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                    .into(mUserAvatar);
        } else if (user.getUserImg() != null) {
            mPresenter.getImg(user.getUserImg(), getActivity().getCacheDir().getPath(), user.getUserName() + ".jpg");
        }
        mNickName.setText(user.getNickname());
        mUserName.setText(user.getUserName());
        mIntroduceLayout.setVisibility(View.VISIBLE);
        if (user.getIntroduce() != null) {
            mIntroduceText.setText(user.getIntroduce());
        } else {
            mIntroduceText.setText(getString(R.string.me_introduction_content));
        }
        if (user.getBadge() == 1) {
            mManagerImg.setVisibility(View.VISIBLE);
        } else {
            mManagerImg.setVisibility(View.GONE);
        }
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setNoUser() {
        isLogin = false;
        Glide.with(this)
                .load(R.drawable.icon_avatar_default)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(mUserAvatar);
        mNickName.setText(getString(R.string.me_user_nickname));
        mUserName.setText(getString(R.string.me_username));
        mIntroduceLayout.setVisibility(View.GONE);
        mManagerImg.setVisibility(View.GONE);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getImgSuccess(String path) {
        Glide.with(this)
                .load(path)
                .error(R.drawable.icon_avatar_default)
                .placeholder(R.drawable.icon_avatar_default)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .skipMemoryCache(true) // 不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                .into(mUserAvatar);
    }

    @Override
    public void getImgFailed() {

    }

    @Override
    public void switchActivity(List<Post> posts, int title) {
        Intent intent = new Intent(getActivity(), PostListActivity.class);
        intent.putExtra("title", getString(title));
        intent.putExtra("post", (Serializable) posts);
        startActivity(intent);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
