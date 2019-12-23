package com.ring.myguide.friends_list.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ring.myguide.R;
import com.ring.myguide.entity.User;
import com.ring.myguide.user_detail.view.UserDetailActivity;

import java.util.List;

/**
 * Created by ring on 2019/11/20.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.MyAdapter> {

    private Context mContext;

    //数据集
    private List<User> mUserList;

    public FriendsAdapter(Context context) {
        mContext = context;
    }

    /**
     * 设置数据集
     *
     * @param userList
     */
    public void setUserList(List<User> userList) {
        mUserList = userList;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //基本上算是固定写法了，list中显示的item就是下边layout的样式
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_friend, parent, false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        if (mUserList != null && mUserList.size() > position) {
            User user = mUserList.get(position);
            //设置头像图片
            Glide.with(mContext)
                    .load(user.getUserImgPath())
                    .error(R.drawable.icon_avatar_default)
                    .placeholder(R.drawable.icon_avatar_default)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .skipMemoryCache(true) // 不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                    .into(holder.mUserAvatar);
            //设置昵称
            holder.mNickName.setText(user.getNickname());
            //判断是否为管理员
            if (user.getBadge() == 1) {
                holder.mManagerImg.setVisibility(View.VISIBLE);
            } else {
                holder.mManagerImg.setVisibility(View.GONE);
            }
            holder.itemView.setOnClickListener(v -> {
                //跳转到好友详情界面
                Intent intent = new Intent(mContext, UserDetailActivity.class);
                intent.putExtra("user", user);
                mContext.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        //当数据集为空时，数据大小为0；数据集不为空时，数据大小为数据集数据数量（必须加非空判断，否则可能会报错）
        return mUserList == null ? 0 : mUserList.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {

        //昵称
        private TextView mNickName;
        //头像
        private ImageView mUserAvatar;
        //管理员标签
        private ImageView mManagerImg;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            mNickName = itemView.findViewById(R.id.tv_nickname);
            mUserAvatar = itemView.findViewById(R.id.img_user_avatar);
            mManagerImg = itemView.findViewById(R.id.img_manager);
        }
    }
}
