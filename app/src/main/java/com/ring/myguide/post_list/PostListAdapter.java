package com.ring.myguide.post_list;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ring.myguide.R;
import com.ring.myguide.base.RequestImgListener;
import com.ring.myguide.entity.Post;
import com.ring.myguide.entity.User;
import com.ring.myguide.post.view.PostActivity;
import com.ring.myguide.utils.DateUtil;
import com.ring.myguide.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ring on 2019/12/12.
 */
public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.MyViewHolder> {

    private Context mContext;

    private List<Post> mPosts = new ArrayList<>();
    private PostListPresenter mPresenter;
    //路径
    private String cachePath;

    public PostListAdapter(Context context) {
        mContext = context;
    }

    public void setPosts(List<Post> posts) {
        if (posts != null) {
            mPosts.addAll(posts);
        }
    }

    public void setPresenter(PostListPresenter presenter) {
        mPresenter = presenter;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_post, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (mPosts.size() > position) {
            Post post = mPosts.get(position);
            User user = post.getAuthor();
            String path = cachePath + "/" + user.getUserImgPath();
            if (FileUtils.fileIsExists(path)) {
                showImgCircle(path, holder.mUserAvatarImg);
            } else {
                mPresenter.requestImg(user.getUserImg(), user.getUserImgPath(), cachePath, new RequestImgListener() {
                    @Override
                    public void onSuccess() {
                        showImgCircle(path, holder.mUserAvatarImg);
                    }

                    @Override
                    public void onFailed() {

                    }
                });
            }
            //设置昵称
            holder.mNicknameText.setText(user.getNickname());
            //判断是否为管理员
            if (user.getBadge() == 1) {
                holder.mManagerImg.setVisibility(View.VISIBLE);
            } else {
                holder.mManagerImg.setVisibility(View.GONE);
            }
            //判断是否精品
            if (post.isBoutique()) {
                holder.mBoutiqueImg.setVisibility(View.VISIBLE);
            } else {
                holder.mBoutiqueImg.setVisibility(View.GONE);
            }
            long time = post.getTime();
            String pattern;
            if (System.currentTimeMillis() - time <= 86400000) {
                pattern = "HH:mm:ss";
            } else {
                pattern = "yyyy-MM-dd";
            }
            String date = DateUtil.getDateToString(time * 1000, pattern);
            holder.mPostTimeText.setText(date);
            holder.mLikeNumText.setText(post.getGoodNum() + "");
            holder.mReplyNumText.setText(post.getReplyNum() + "");
            holder.mPostTitleText.setText(post.getTitle());
            holder.mPostContentText.setText(post.getContent());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PostActivity.class);
                    intent.putExtra("post", post);
                    mContext.startActivity(intent);
                }
            });
            if (post.isDelete()){
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray));
                holder.itemView.setClickable(false);
            }
        }
    }

    private void showImgCircle(String path, ImageView imageView) {
        Glide.with(mContext)
                .load(path)
                .error(R.drawable.icon_avatar_default)
                .placeholder(R.drawable.icon_default_photo)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .skipMemoryCache(true) // 不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

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

        public MyViewHolder(@NonNull View itemView) {
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
