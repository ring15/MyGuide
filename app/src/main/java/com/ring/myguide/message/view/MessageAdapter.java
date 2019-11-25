package com.ring.myguide.message.view;

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
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ring.myguide.R;
import com.ring.myguide.chat.view.ChatActivity;
import com.ring.myguide.entity.MessageList;
import com.ring.myguide.entity.User;
import com.ring.myguide.user_detail.view.UserDetailActivity;
import com.ring.myguide.utils.DateUtil;

import java.util.LinkedList;

/**
 * Created by ring on 2019/11/22.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private Context mContext;

    //消息列表数据源
    private LinkedList<MessageList> mMessageLists;

    public MessageAdapter(Context context) {
        mContext = context;
    }

    public void setMessageLists(LinkedList<MessageList> messageLists) {
        mMessageLists = messageLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_message, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (mMessageLists != null && mMessageLists.size() > position) {
            User user = mMessageLists.get(position).getUser();

            //设置头像图片
            Glide.with(mContext)
                    .load(user.getUserImg())
                    .error(R.drawable.icon_avatar_default)
                    .placeholder(R.drawable.icon_avatar_default)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
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
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("user", user);
                mContext.startActivity(intent);
            });

            //设置消息内容
            holder.mContent.setText(mMessageLists.get(position).getContent());
            //设置时间
            long time = mMessageLists.get(position).getTime();
            String pattern;
            if (System.currentTimeMillis() - time <= 86400000) {
                pattern = "HH:mm:ss";
            } else {
                pattern = "yyyy-MM-dd";
            }
            holder.mTimeText.setText(DateUtil.getDateToString(time, pattern));
        }
    }

    @Override
    public int getItemCount() {
        return mMessageLists == null ? 0 : mMessageLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        //昵称
        private TextView mNickName;
        //消息内容
        private TextView mContent;
        //头像
        private ImageView mUserAvatar;
        //管理员标签
        private ImageView mManagerImg;
        //时间
        private TextView mTimeText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mNickName = itemView.findViewById(R.id.tv_nickname);
            mUserAvatar = itemView.findViewById(R.id.img_user_avatar);
            mManagerImg = itemView.findViewById(R.id.img_manager);
            mContent = itemView.findViewById(R.id.tv_message);
            mTimeText = itemView.findViewById(R.id.tv_time);
        }
    }
}
