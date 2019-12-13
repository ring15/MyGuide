package com.ring.myguide.post.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ring.myguide.R;
import com.ring.myguide.base.RequestImgListener;
import com.ring.myguide.entity.Post;
import com.ring.myguide.entity.User;
import com.ring.myguide.post.presenter.PostPresenter;
import com.ring.myguide.utils.DateUtil;
import com.ring.myguide.utils.FileUtils;

/**
 * Created by ring on 2019/12/13.
 */
public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //发帖人信息
    private static final int USER = 0;
    //帖子标题
    private static final int POST_TITLE = 1;
    //帖子内容
    private static final int POST_CONTENT = 2;
    //帖子中图片
    private static final int POST_IMG = 3;
    //点赞
    private static final int POST_LIKE = 4;
    //评论部分标题
    private static final int POST_REPLY_TITLE = 5;
    //评论部分
    private static final int POST_REPLY = 6;

    private Context mContext;

    private Post mPost;

    //当前登录user
    private User mUser;
    private PostPresenter mPresenter;
    private String cachePath;
    private int imgNum;

    public PostAdapter(Context context) {
        mContext = context;
    }

    public void setPost(Post post) {
        mPost = post;
        if (mPost != null && mPost.getImgList() != null) {
            imgNum = mPost.getImgList().size();
        }
    }

    public void setPresenter(PostPresenter presenter) {
        mPresenter = presenter;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    public void setUser(User user) {
        mUser = user;
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (position == 0) {
            type = USER;
        } else if (position == 1) {
            type = POST_TITLE;
        } else if (position == 2) {
            type = POST_CONTENT;
        } else if (position < imgNum + 3) {
            type = POST_IMG;
        } else if (position == imgNum + 3) {
            type = POST_LIKE;
        } else if (position == imgNum + 4) {
            type = POST_REPLY_TITLE;
        } else {
            type = POST_REPLY;
        }
        return type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view;
        switch (viewType) {
            case USER:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_post_user, parent, false);
                holder = new UserViewHolder(view);
                break;
            case POST_TITLE:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_post_title, parent, false);
                holder = new PostTitleViewHolder(view);
                break;
            case POST_CONTENT:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_post_content, parent, false);
                holder = new PostContentViewHolder(view);
                break;
            case POST_IMG:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_post_photo, parent, false);
                holder = new PostImgViewHolder(view);
                break;
            case POST_LIKE:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_post_favorite, parent, false);
                holder = new LikeViewHolder(view);
                break;
            case POST_REPLY_TITLE:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_post_reply_title, parent, false);
                holder = new ReplyTitleViewHolder(view);
                break;
            default:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_post_reply, parent, false);
                holder = new ReplyViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserViewHolder) {
            UserViewHolder viewHolder = (UserViewHolder) holder;
            User user = mPost.getAuthor();
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
            //判断是否为管理员
            if (user.getBadge() == 1) {
                viewHolder.mPostMenuBtn.setVisibility(View.VISIBLE);
                viewHolder.mPostMenuBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除
                        //加精
                        //修改分类
                    }
                });
            } else if (mUser != null && mUser.getUserName().equals(user.getUserName())) {
                viewHolder.mPostMenuBtn.setVisibility(View.VISIBLE);
                viewHolder.mPostMenuBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除
                    }
                });
            } else {
                viewHolder.mPostMenuBtn.setVisibility(View.GONE);
            }
            //判断是否精品
            if (mPost.isBoutique()) {
                viewHolder.mBoutiqueImg.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mBoutiqueImg.setVisibility(View.GONE);
            }
            long time = mPost.getTime();
            String pattern;
            if (System.currentTimeMillis() - time <= 86400000) {
                pattern = "HH:mm:ss";
            } else {
                pattern = "yyyy-MM-dd";
            }
            String date = DateUtil.getDateToString(time * 1000, pattern);
            viewHolder.mPostTimeText.setText(date);
        } else if (holder instanceof PostTitleViewHolder) {
            PostTitleViewHolder viewHolder = (PostTitleViewHolder) holder;
            viewHolder.mPostTitleText.setText(mPost.getTitle());
        } else if (holder instanceof PostContentViewHolder) {
            PostContentViewHolder viewHolder = (PostContentViewHolder) holder;
            viewHolder.mPostContentText.setText(mPost.getContent());
        } else if (holder instanceof PostImgViewHolder) {
            if (mPost.getImgs() != null && mPost.getImgs().size() > position - 3) {
                PostImgViewHolder viewHolder = (PostImgViewHolder) holder;
                String path = cachePath + "/" + mPost.getImgList().get(position - 3);
                if (FileUtils.fileIsExists(path)) {
                    showImg(path, viewHolder.mPhotoImg);
                } else {
                    mPresenter.requestImg(mPost.getImgs().get(position - 3), mPost.getImgList().get(position - 3), cachePath, new RequestImgListener() {
                        @Override
                        public void onSuccess() {
                            showImg(path, viewHolder.mPhotoImg);
                        }

                        @Override
                        public void onFailed() {

                        }
                    });
                }
            }
        } else if (holder instanceof LikeViewHolder) {
            LikeViewHolder viewHolder = (LikeViewHolder) holder;
        } else if (holder instanceof ReplyTitleViewHolder) {
            ReplyTitleViewHolder viewHolder = (ReplyTitleViewHolder) holder;
        } else if (holder instanceof ReplyViewHolder) {
            ReplyViewHolder viewHolder = (ReplyViewHolder) holder;
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
        return imgNum + 5;
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
        //更多操作
        private Button mPostMenuBtn;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mBoutiqueImg = itemView.findViewById(R.id.img_boutique);
            mUserAvatarImg = itemView.findViewById(R.id.img_user_avatar);
            mNicknameText = itemView.findViewById(R.id.tv_nickname);
            mManagerImg = itemView.findViewById(R.id.img_manager);
            mPostTimeText = itemView.findViewById(R.id.tv_post_time);
            mPostMenuBtn = itemView.findViewById(R.id.post_menu);
        }
    }

    class PostTitleViewHolder extends RecyclerView.ViewHolder {

        private TextView mPostTitleText;

        public PostTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            mPostTitleText = itemView.findViewById(R.id.tv_post_title);
        }
    }

    class PostContentViewHolder extends RecyclerView.ViewHolder {

        private TextView mPostContentText;

        public PostContentViewHolder(@NonNull View itemView) {
            super(itemView);
            mPostContentText = itemView.findViewById(R.id.tv_content);
        }
    }

    class PostImgViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPhotoImg;

        public PostImgViewHolder(@NonNull View itemView) {
            super(itemView);
            mPhotoImg = itemView.findViewById(R.id.img_photo);
        }
    }

    class LikeViewHolder extends RecyclerView.ViewHolder {

        private ImageButton mPostGoodImg;
        private TextView mPostGoodNum;

        public LikeViewHolder(@NonNull View itemView) {
            super(itemView);
            mPostGoodImg = itemView.findViewById(R.id.img_post_good);
            mPostGoodNum = itemView.findViewById(R.id.tv_post_good_num);
        }
    }

    class ReplyTitleViewHolder extends RecyclerView.ViewHolder {

        public ReplyTitleViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class ReplyViewHolder extends RecyclerView.ViewHolder {

        //是否精品图标
        private ImageView mBoutiqueImg;
        //发帖人头像
        private ImageView mUserAvatarImg;
        //发帖人昵称
        private TextView mNicknameText;
        //发帖人是否是管理员图标
        private ImageView mManagerImg;
        //回复楼层
        private TextView mPostFloorText;
        //回复内容
        private TextView mPostFloorContentText;
        //更多操作
        private Button mPostMenuBtn;

        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            mBoutiqueImg = itemView.findViewById(R.id.img_boutique);
            mUserAvatarImg = itemView.findViewById(R.id.img_user_avatar);
            mNicknameText = itemView.findViewById(R.id.tv_nickname);
            mManagerImg = itemView.findViewById(R.id.img_manager);
            mPostFloorText = itemView.findViewById(R.id.tv_post_floor);
            mPostFloorContentText = itemView.findViewById(R.id.tv_post_floor_content);
            mPostMenuBtn = itemView.findViewById(R.id.post_menu);
        }
    }
}
