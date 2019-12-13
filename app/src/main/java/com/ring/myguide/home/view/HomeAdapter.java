package com.ring.myguide.home.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ring.myguide.R;
import com.ring.myguide.WebActivity;
import com.ring.myguide.base.RequestImgListener;
import com.ring.myguide.entity.Banner;
import com.ring.myguide.entity.HomePage;
import com.ring.myguide.entity.Post;
import com.ring.myguide.entity.User;
import com.ring.myguide.home.presenter.HomePresenter;
import com.ring.myguide.post_list.PostListActivity;
import com.ring.myguide.utils.DateUtil;
import com.ring.myguide.utils.FileUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by ring on 2019/12/9.
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //首页最多展示条数
    private static final int MAX_NUM = 2;

    //滚动栏加标题
    private static final int BANNER_TITLE = 0;
    //美食推荐的标题
    private static final int FOOD_RECOMMEND_TITLE = 1;
    //美食推荐
    private static final int FOOD_RECOMMEND = 2;
    //景点推荐的标题
    private static final int PLACE_RECOMMEND_TITLE = 3;
    //景点推荐
    private static final int PLACE_RECOMMEND = 4;
    //用户推荐的标题
    private static final int USER_RECOMMEND_TITLE = 5;
    //用户推荐
    private static final int USER_RECOMMEND = 6;

    private Context mContext;
    //数据源
    private List<Banner> mBanners = new ArrayList<>();
    private List<Post> mFoodRecommends = new ArrayList<>();
    private List<Post> mPlaceRecommends = new ArrayList<>();
    private List<Post> mUserRecommends = new ArrayList<>();
    //presenter
    private HomePresenter mPresenter;
    //路径
    private String cachePath;

    public HomeAdapter(Context context) {
        mContext = context;
    }

    public void setHomePage(HomePage homePage) {
        if (homePage.getBanners() != null) {
            mBanners.clear();
            mBanners.addAll(homePage.getBanners());
        }
        if (homePage.getFoodPost() != null) {
            mFoodRecommends.clear();
            for (int i = 0; i < homePage.getFoodPost().size() && i < MAX_NUM; i++) {
                mFoodRecommends.add(homePage.getFoodPost().get(i));
            }
        }
        if (homePage.getPlacePost() != null) {
            mPlaceRecommends.clear();
            for (int i = 0; i < homePage.getPlacePost().size() && i < MAX_NUM; i++) {
                mPlaceRecommends.add(homePage.getPlacePost().get(i));
            }
        }
        if (homePage.getUserPost() != null) {
            mUserRecommends.clear();
            for (int i = 0; i < homePage.getUserPost().size() && i < MAX_NUM; i++) {
                mUserRecommends.add(homePage.getUserPost().get(i));
            }
        }
    }

    public void setPresenter(HomePresenter presenter) {
        mPresenter = presenter;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (position == 0) {
            type = BANNER_TITLE;
        } else if (position == 1) {
            type = FOOD_RECOMMEND_TITLE;
        } else if (position < mFoodRecommends.size() + 2) {
            type = FOOD_RECOMMEND;
        } else if (position == mFoodRecommends.size() + 2) {
            type = PLACE_RECOMMEND_TITLE;
        } else if (position < mFoodRecommends.size() + mPlaceRecommends.size() + 3) {
            type = PLACE_RECOMMEND;
        } else if (position == mFoodRecommends.size() + mPlaceRecommends.size() + 3) {
            type = USER_RECOMMEND_TITLE;
        } else {
            type = USER_RECOMMEND;
        }
        return type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view;
        if (viewType == BANNER_TITLE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_banner, parent, false);
            holder = new BannerViewHolder(view);
            ((BannerViewHolder) holder).mBanner.setAdapter(new BGABanner.Adapter() {
                @Override
                public void fillBannerItem(BGABanner banner, View itemView, @Nullable Object model, int position) {
                    ImageView imageView = itemView.findViewById(R.id.img_banner);
                    String path = cachePath + "/" + mBanners.get(position).getImgPath();
                    if (FileUtils.fileIsExists(path)) {
                        showImg(path, imageView);
                    } else {
                        mPresenter.requestImg((String) model, mBanners.get(position).getImgPath(), cachePath, new RequestImgListener() {
                            @Override
                            public void onSuccess() {
                                showImg(path, imageView);
                            }

                            @Override
                            public void onFailed() {

                            }
                        });
                    }
                }
            });
        } else if (viewType == FOOD_RECOMMEND_TITLE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_food_title, parent, false);
            holder = new FoodTitleViewHolder(view);
        } else if (viewType == FOOD_RECOMMEND) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_post, parent, false);
            holder = new FoodViewHolder(view);
        } else if (viewType == PLACE_RECOMMEND_TITLE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_place_title, parent, false);
            holder = new PlaceTitleViewHolder(view);
        } else if (viewType == PLACE_RECOMMEND) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_post, parent, false);
            holder = new PlaceViewHolder(view);
        } else if (viewType == USER_RECOMMEND_TITLE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_user_title, parent, false);
            holder = new UserTitleViewHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_post, parent, false);
            holder = new UserViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BannerViewHolder) {
            BannerViewHolder viewHolder = (BannerViewHolder) holder;
            if (mBanners.size() > 0) {
                viewHolder.mBanner.setVisibility(View.VISIBLE);
                viewHolder.mBannerTitle.setVisibility(View.VISIBLE);
                viewHolder.mBannerTitle.setText(mBanners.get(0).getTitle());
                List<String> urls = new ArrayList<>();
                for (Banner banner : mBanners) {
                    urls.add(banner.getImg());
                }
                viewHolder.mBanner.setData(R.layout.item_banner_glide, urls, null);
                viewHolder.mBanner.setDelegate(new BGABanner.Delegate() {
                    @Override
                    public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, WebActivity.class);
                        intent.putExtra("web_title", mBanners.get(position).getTitle());
                        intent.putExtra("web_url", mBanners.get(position).getClickUrl());
                        mContext.startActivity(intent);
                    }
                });
                viewHolder.mBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        viewHolder.mBannerTitle.setText(mBanners.get(position).getTitle());
                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            } else {
                viewHolder.mBanner.setVisibility(View.GONE);
                viewHolder.mBannerTitle.setVisibility(View.GONE);
            }
        } else if (holder instanceof FoodTitleViewHolder) {
            FoodTitleViewHolder viewHolder = (FoodTitleViewHolder) holder;
            viewHolder.mMoreText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PostListActivity.class);
                    intent.putExtra("title", mContext.getString(R.string.post_list_food));
                    intent.putExtra("post", (Serializable) mFoodRecommends);
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof FoodViewHolder) {
            FoodViewHolder viewHolder = (FoodViewHolder) holder;
            if (mFoodRecommends.size() > position - 2) {
                Post post = mFoodRecommends.get(position - 2);
                User user = post.getAuthor();
                String path = cachePath + "/" + user.getUserName();
                if (FileUtils.fileIsExists(path)) {
                    showImgCircle(path, viewHolder.mUserAvatarImg);
                } else {
                    mPresenter.requestImg(user.getUserImg(), user.getUserName(), cachePath, new RequestImgListener() {
                        @Override
                        public void onSuccess() {
                            showImgCircle(path, viewHolder.mUserAvatarImg);
                        }

                        @Override
                        public void onFailed() {

                        }
                    });
                }
                //设置昵称
                viewHolder.mNicknameText.setText(user.getNickname());
                //判断是否为管理员
                if (user.getBadge() == 1) {
                    viewHolder.mManagerImg.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.mManagerImg.setVisibility(View.GONE);
                }
                //判断是否精品
                if (post.isBoutique()) {
                    viewHolder.mBoutiqueImg.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.mBoutiqueImg.setVisibility(View.GONE);
                }
                long time = post.getTime();
                String pattern;
                if (System.currentTimeMillis() - time <= 86400000) {
                    pattern = "HH:mm:ss";
                } else {
                    pattern = "yyyy-MM-dd";
                }
                String date = DateUtil.getDateToString(time * 1000, pattern);
                viewHolder.mPostTimeText.setText(date);
                viewHolder.mLikeNumText.setText(post.getGoodNum() + "");
                viewHolder.mReplyNumText.setText(post.getReplyNum() + "");
                viewHolder.mPostTitleText.setText(post.getTitle());
                viewHolder.mPostContentText.setText(post.getContent());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        } else if (holder instanceof PlaceTitleViewHolder) {
            PlaceTitleViewHolder viewHolder = (PlaceTitleViewHolder) holder;
            viewHolder.mMoreText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PostListActivity.class);
                    intent.putExtra("title", mContext.getString(R.string.post_list_place));
                    intent.putExtra("post", (Serializable) mPlaceRecommends);
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof PlaceViewHolder) {
            PlaceViewHolder viewHolder = (PlaceViewHolder) holder;
            if (mPlaceRecommends.size() > position - 3 - mFoodRecommends.size()) {
                Post post = mPlaceRecommends.get(position - 3 - mFoodRecommends.size());
                User user = post.getAuthor();
                String path = cachePath + "/" + user.getUserName();
                if (FileUtils.fileIsExists(path)) {
                    showImgCircle(path, viewHolder.mUserAvatarImg);
                } else {
                    mPresenter.requestImg(user.getUserImg(), user.getUserName(), cachePath, new RequestImgListener() {
                        @Override
                        public void onSuccess() {
                            showImgCircle(path, viewHolder.mUserAvatarImg);
                        }

                        @Override
                        public void onFailed() {

                        }
                    });
                }
                //设置昵称
                viewHolder.mNicknameText.setText(user.getNickname());
                //判断是否为管理员
                if (user.getBadge() == 1) {
                    viewHolder.mManagerImg.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.mManagerImg.setVisibility(View.GONE);
                }
                //判断是否精品
                if (post.isBoutique()) {
                    viewHolder.mBoutiqueImg.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.mBoutiqueImg.setVisibility(View.GONE);
                }
                long time = post.getTime();
                String pattern;
                if (System.currentTimeMillis() - time <= 86400000) {
                    pattern = "HH:mm:ss";
                } else {
                    pattern = "yyyy-MM-dd";
                }
                viewHolder.mPostTimeText.setText(DateUtil.getDateToString(time * 1000, pattern));
                viewHolder.mLikeNumText.setText(post.getGoodNum() + "");
                viewHolder.mReplyNumText.setText(post.getReplyNum() + "");
                viewHolder.mPostTitleText.setText(post.getTitle());
                viewHolder.mPostContentText.setText(post.getContent());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        } else if (holder instanceof UserTitleViewHolder) {
            UserTitleViewHolder viewHolder = (UserTitleViewHolder) holder;
            viewHolder.mMoreText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PostListActivity.class);
                    intent.putExtra("title", mContext.getString(R.string.post_list_user));
                    intent.putExtra("post", (Serializable) mUserRecommends);
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof UserViewHolder) {
            UserViewHolder viewHolder = (UserViewHolder) holder;
            if (mUserRecommends.size() > position - 4 - mFoodRecommends.size() - mPlaceRecommends.size()) {
                Post post = mUserRecommends.get(position - 4 - mFoodRecommends.size() - mPlaceRecommends.size());
                User user = post.getAuthor();
                String path = cachePath + "/" + user.getUserName();
                if (FileUtils.fileIsExists(path)) {
                    showImgCircle(path, viewHolder.mUserAvatarImg);
                } else {
                    mPresenter.requestImg(user.getUserImg(), user.getUserName(), cachePath, new RequestImgListener() {
                        @Override
                        public void onSuccess() {
                            showImgCircle(path, viewHolder.mUserAvatarImg);
                        }

                        @Override
                        public void onFailed() {

                        }
                    });
                }
                //设置昵称
                viewHolder.mNicknameText.setText(user.getNickname());
                //判断是否为管理员
                if (user.getBadge() == 1) {
                    viewHolder.mManagerImg.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.mManagerImg.setVisibility(View.GONE);
                }
                //判断是否精品
                if (post.isBoutique()) {
                    viewHolder.mBoutiqueImg.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.mBoutiqueImg.setVisibility(View.GONE);
                }
                long time = post.getTime();
                String pattern;
                if (System.currentTimeMillis() - time <= 86400000) {
                    pattern = "HH:mm:ss";
                } else {
                    pattern = "yyyy-MM-dd";
                }
                viewHolder.mPostTimeText.setText(DateUtil.getDateToString(time * 1000, pattern));
                viewHolder.mLikeNumText.setText(post.getGoodNum() + "");
                viewHolder.mReplyNumText.setText(post.getReplyNum() + "");
                viewHolder.mPostTitleText.setText(post.getTitle());
                viewHolder.mPostContentText.setText(post.getContent());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }
    }

    private void showImg(String path, ImageView imageView) {
        Glide.with(mContext)
                .load(path)
                .error(R.drawable.icon_default_photo)
                .placeholder(R.drawable.icon_default_photo)
                .skipMemoryCache(true) // 不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                .into(imageView);
    }

    private void showImgCircle(String path, ImageView imageView) {
        Glide.with(mContext)
                .load(path)
                .error(R.drawable.icon_avatar_default)
                .placeholder(R.drawable.icon_avatar_default)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .skipMemoryCache(true) // 不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return mFoodRecommends.size() + mPlaceRecommends.size() + mUserRecommends.size() + 4;
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {

        private BGABanner mBanner;
        private TextView mBannerTitle;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            mBanner = itemView.findViewById(R.id.banner_home);
            mBannerTitle = itemView.findViewById(R.id.tv_banner_title);
        }
    }

    class FoodTitleViewHolder extends RecyclerView.ViewHolder {

        private TextView mMoreText;

        public FoodTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            mMoreText = itemView.findViewById(R.id.tv_more_food);
        }
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {

        //是否精品图标
        private ImageView mBoutiqueImg;
        //发帖人头像
        private ImageView mUserAvatarImg;
        //发帖人昵称
        private TextView mNicknameText;
        //发帖人是否是管理员图标
        private ImageView mManagerImg;
        //发帖时间
        private TextView mPostTimeText;
        //点赞数量
        private TextView mLikeNumText;
        //回复数量
        private TextView mReplyNumText;
        //帖子标题
        private TextView mPostTitleText;
        //帖子内容
        private TextView mPostContentText;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            mBoutiqueImg = itemView.findViewById(R.id.img_boutique);
            mUserAvatarImg = itemView.findViewById(R.id.img_user_avatar);
            mNicknameText = itemView.findViewById(R.id.tv_nickname);
            mManagerImg = itemView.findViewById(R.id.img_manager);
            mPostTimeText = itemView.findViewById(R.id.tv_post_time);
            mLikeNumText = itemView.findViewById(R.id.tv_like_num);
            mReplyNumText = itemView.findViewById(R.id.tv_reply_num);
            mPostTitleText = itemView.findViewById(R.id.tv_post_title);
            mPostContentText = itemView.findViewById(R.id.tv_post_content);
        }
    }

    class PlaceTitleViewHolder extends RecyclerView.ViewHolder {

        private TextView mMoreText;

        public PlaceTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            mMoreText = itemView.findViewById(R.id.tv_more_place);
        }
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {

        //是否精品图标
        private ImageView mBoutiqueImg;
        //发帖人头像
        private ImageView mUserAvatarImg;
        //发帖人昵称
        private TextView mNicknameText;
        //发帖人是否是管理员图标
        private ImageView mManagerImg;
        //发帖时间
        private TextView mPostTimeText;
        //点赞数量
        private TextView mLikeNumText;
        //回复数量
        private TextView mReplyNumText;
        //帖子标题
        private TextView mPostTitleText;
        //帖子内容
        private TextView mPostContentText;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            mBoutiqueImg = itemView.findViewById(R.id.img_boutique);
            mUserAvatarImg = itemView.findViewById(R.id.img_user_avatar);
            mNicknameText = itemView.findViewById(R.id.tv_nickname);
            mManagerImg = itemView.findViewById(R.id.img_manager);
            mPostTimeText = itemView.findViewById(R.id.tv_post_time);
            mLikeNumText = itemView.findViewById(R.id.tv_like_num);
            mReplyNumText = itemView.findViewById(R.id.tv_reply_num);
            mPostTitleText = itemView.findViewById(R.id.tv_post_title);
            mPostContentText = itemView.findViewById(R.id.tv_post_content);
        }
    }

    class UserTitleViewHolder extends RecyclerView.ViewHolder {

        private TextView mMoreText;

        public UserTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            mMoreText = itemView.findViewById(R.id.tv_more_user);
        }
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        //是否精品图标
        private ImageView mBoutiqueImg;
        //发帖人头像
        private ImageView mUserAvatarImg;
        //发帖人昵称
        private TextView mNicknameText;
        //发帖人是否是管理员图标
        private ImageView mManagerImg;
        //发帖时间
        private TextView mPostTimeText;
        //点赞数量
        private TextView mLikeNumText;
        //回复数量
        private TextView mReplyNumText;
        //帖子标题
        private TextView mPostTitleText;
        //帖子内容
        private TextView mPostContentText;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mBoutiqueImg = itemView.findViewById(R.id.img_boutique);
            mUserAvatarImg = itemView.findViewById(R.id.img_user_avatar);
            mNicknameText = itemView.findViewById(R.id.tv_nickname);
            mManagerImg = itemView.findViewById(R.id.img_manager);
            mPostTimeText = itemView.findViewById(R.id.tv_post_time);
            mLikeNumText = itemView.findViewById(R.id.tv_like_num);
            mReplyNumText = itemView.findViewById(R.id.tv_reply_num);
            mPostTitleText = itemView.findViewById(R.id.tv_post_title);
            mPostContentText = itemView.findViewById(R.id.tv_post_content);
        }
    }

}
