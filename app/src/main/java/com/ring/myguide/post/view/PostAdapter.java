package com.ring.myguide.post.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ring.myguide.R;
import com.ring.myguide.ShowImgActivity;
import com.ring.myguide.base.RequestImgListener;
import com.ring.myguide.entity.Post;
import com.ring.myguide.entity.Reply;
import com.ring.myguide.entity.User;
import com.ring.myguide.post.presenter.PostPresenter;
import com.ring.myguide.user_detail.view.UserDetailActivity;
import com.ring.myguide.utils.DateUtil;
import com.ring.myguide.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

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

    private List<Reply> mReplies = new ArrayList<>();

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

    public void setReplies(List<Reply> replies) {
        if (replies != null) {
            mReplies.addAll(replies);
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
            boolean isManager = mUser != null && mUser.getBadge() == 1;
            boolean isOwner = mUser != null && mUser.getUserName().equals(user.getUserName());
            if (isManager) {
                viewHolder.mPostMenuBtn.setVisibility(View.VISIBLE);
                viewHolder.mPostMenuBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //创建弹出式菜单对象（最低版本11）
                        PopupMenu popup = new PopupMenu(mContext, viewHolder.mPostMenuBtn);//第二个参数是绑定的那个view
                        //获取菜单填充器
                        MenuInflater inflater = popup.getMenuInflater();
                        //填充菜单
                        inflater.inflate(R.menu.menu_manager, popup.getMenu());
                        //绑定菜单项的点击事件
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.item_delete:
                                        //删除
                                        mPresenter.changeDelete(mPost.getThreadID());
                                        break;
                                    case R.id.item_boutique:
                                        //加精
                                        mPresenter.changeBoutique(mPost.getThreadID());
                                        break;
                                    case R.id.item_change_type:
                                        //修改分类
                                        String[] list = {mContext.getString(R.string.post_user), mContext.getString(R.string.post_place), mContext.getString(R.string.post_food)};
                                        AlertDialog dialog = new AlertDialog.Builder(mContext)
                                                .setTitle(null)
                                                .setItems(list, (dialog1, which) -> {
                                                    mPresenter.changeType(mPost.getThreadID(), which);
                                                }).show();
                                        break;
                                }
                                return false;
                            }
                        });
                        popup.show(); //这一行代码不要忘记了
                    }
                });
            } else if (isOwner) {
                viewHolder.mPostMenuBtn.setVisibility(View.VISIBLE);
                viewHolder.mPostMenuBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //创建弹出式菜单对象（最低版本11）
                        PopupMenu popup = new PopupMenu(mContext, viewHolder.mPostMenuBtn);//第二个参数是绑定的那个view
                        //获取菜单填充器
                        MenuInflater inflater = popup.getMenuInflater();
                        //填充菜单
                        inflater.inflate(R.menu.menu_owner, popup.getMenu());
                        //绑定菜单项的点击事件
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.item_delete:
                                        //删除
                                        mPresenter.changeDelete(mPost.getThreadID());
                                        break;
                                }
                                return false;
                            }
                        });
                        popup.show(); //这一行代码不要忘记了
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
            //点击头像查看好友详情
            viewHolder.mUserAvatarImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, UserDetailActivity.class);
                    intent.putExtra("user", user);
                    mContext.startActivity(intent);
                }
            });
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
                viewHolder.mPhotoImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ShowImgActivity.class);
                        intent.putExtra("path", path);
                        mContext.startActivity(intent);
                    }
                });
            }
        } else if (holder instanceof LikeViewHolder) {
            LikeViewHolder viewHolder = (LikeViewHolder) holder;
            //设置是否点赞
            if (mPost.isGood()) {
                viewHolder.mPostGoodImg.setSelected(true);
            } else {
                viewHolder.mPostGoodImg.setSelected(false);
            }
            viewHolder.mPostGoodNum.setText(mPost.getGoodNum() + "");
            viewHolder.mPostGoodImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPresenter != null) {
                        mPresenter.good(mPost.getThreadID());
                        mPost.setGood(!mPost.isGood());
                        if (mPost.isGood()) {
                            viewHolder.mPostGoodImg.setSelected(true);
                            mPost.setGoodNum(mPost.getGoodNum() + 1);
                            viewHolder.mPostGoodNum.setText(mPost.getGoodNum() + "");
                        } else {
                            viewHolder.mPostGoodImg.setSelected(false);
                            mPost.setGoodNum(mPost.getGoodNum() - 1);
                            viewHolder.mPostGoodNum.setText(mPost.getGoodNum() + "");
                        }
                    }
                }
            });
        } else if (holder instanceof ReplyTitleViewHolder) {
            ReplyTitleViewHolder viewHolder = (ReplyTitleViewHolder) holder;
        } else if (holder instanceof ReplyViewHolder) {
            ReplyViewHolder viewHolder = (ReplyViewHolder) holder;
            int index = position - imgNum - 5;
            if (mReplies.size() > index) {
                Reply reply = mReplies.get(index);
                User user = reply.getAuthor();
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
                boolean isManager = mUser != null && mUser.getBadge() == 1;
                boolean isOwner = mUser != null && mUser.getUserName().equals(user.getUserName());
                boolean isOwnerReply = mUser != null && mUser.getUserName().equals(mPost.getAuthor().getUserName());
                if (isManager || isOwner || isOwnerReply) {
                    viewHolder.mPostMenuBtn.setVisibility(View.VISIBLE);
                    viewHolder.mPostMenuBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //创建弹出式菜单对象（最低版本11）
                            PopupMenu popup = new PopupMenu(mContext, viewHolder.mPostMenuBtn);//第二个参数是绑定的那个view
                            //获取菜单填充器
                            MenuInflater inflater = popup.getMenuInflater();
                            //填充菜单
                            inflater.inflate(R.menu.menu_owner, popup.getMenu());
                            //绑定菜单项的点击事件
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.item_delete:
                                            //删除
                                            mPresenter.deleteReply(mPost.getThreadID(), reply.getFloor());
                                            break;
                                    }
                                    return false;
                                }
                            });
                            popup.show(); //这一行代码不要忘记了
                        }
                    });
                } else {
                    viewHolder.mPostMenuBtn.setVisibility(View.GONE);
                }
                //内容
                viewHolder.mPostFloorContentText.setText(reply.getContent());
                //楼层
                viewHolder.mPostFloorText.setText(mContext.getString(R.string.post_floor_num, reply.getFloor()));
                //点击头像查看好友详情
                viewHolder.mUserAvatarImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, UserDetailActivity.class);
                        intent.putExtra("user", user);
                        mContext.startActivity(intent);
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
        return imgNum + 5 + mReplies.size();
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
            mUserAvatarImg = itemView.findViewById(R.id.img_user_avatar);
            mNicknameText = itemView.findViewById(R.id.tv_nickname);
            mManagerImg = itemView.findViewById(R.id.img_manager);
            mPostFloorText = itemView.findViewById(R.id.tv_post_floor);
            mPostFloorContentText = itemView.findViewById(R.id.tv_post_floor_content);
            mPostMenuBtn = itemView.findViewById(R.id.post_menu);
        }
    }
}
