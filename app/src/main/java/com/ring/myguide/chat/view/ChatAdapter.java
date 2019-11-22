package com.ring.myguide.chat.view;

import android.content.Context;
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
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.ring.myguide.R;
import com.ring.myguide.entity.User;
import com.ring.myguide.utils.DateUtil;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FIRST_ME = 0;
    private static final int TYPE_FIRST_SOMEONE = 1;

    private Context mContect;
    //消息
    private List<EMMessage> mEMMessages;
    //当前登录用户
    private User mUser;
    //聊天对象
    private User mOtherUser;

    public ChatAdapter(Context context) {
        mContect = context;
    }

    public void setEMMessages(List<EMMessage> EMMessages) {
        if (mEMMessages != null){
            mEMMessages.clear();
        }
        this.mEMMessages = EMMessages;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public void setOtherUser(User otherUser) {
        mOtherUser = otherUser;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (mEMMessages != null && mEMMessages.size() > position && mUser != null) {
            if (mEMMessages.get(position).getFrom().equals(mUser.getUserName())) {
                viewType = TYPE_FIRST_ME;
            } else if (mEMMessages.get(position).getTo().equals(mUser.getUserName())) {
                viewType = TYPE_FIRST_SOMEONE;
            }
        }
        return viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        switch (i) {
            case TYPE_FIRST_ME:
                view = LayoutInflater.from(mContect).inflate(R.layout.item_chat_me, viewGroup, false);
                viewHolder = new MeViewHolder(view);
                break;
            case TYPE_FIRST_SOMEONE:
                view = LayoutInflater.from(mContect).inflate(R.layout.item_chat_someone, viewGroup, false);
                viewHolder = new SomeOneViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (mEMMessages != null && mEMMessages.size() > i) {
            if (viewHolder instanceof MeViewHolder) {
                MeViewHolder holder = (MeViewHolder) viewHolder;
                holder.tvTime.setText(DateUtil.getDateToString(mEMMessages.get(i).getMsgTime(), "yyyy-MM-dd HH:mm:ss"));
                if (mEMMessages.get(i).getBody() instanceof EMTextMessageBody) {
                    holder.tvMailContent.setText(((EMTextMessageBody) mEMMessages.get(i).getBody()).getMessage());
                }
                Glide.with(mContect)
                        .load(mUser.getUserImg())
                        .placeholder(R.drawable.icon_avatar_default)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(holder.ivUserAvatar);
            } else if (viewHolder instanceof SomeOneViewHolder) {
                SomeOneViewHolder holder = (SomeOneViewHolder) viewHolder;
                holder.tvTime.setText(DateUtil.getDateToString(mEMMessages.get(i).getMsgTime(), "yyyy-MM-dd HH:mm:ss"));
                if (mEMMessages.get(i).getBody() instanceof EMTextMessageBody) {
                    holder.tvMailContent.setText(((EMTextMessageBody) mEMMessages.get(i).getBody()).getMessage());
                }
                Glide.with(mContect)
                        .load(mOtherUser.getUserImg())
                        .placeholder(R.drawable.icon_avatar_default)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(holder.ivUserAvatar);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mEMMessages == null) ? 0 : mEMMessages.size();
    }

    //消息来自自己的viewholder
    public class MeViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTime;
        private ImageView ivUserAvatar;
        private TextView tvMailContent;

        public MeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            tvMailContent = itemView.findViewById(R.id.tv_chat_content);
        }
    }

    //消息来自自己的viewholder
    public class SomeOneViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTime;
        private ImageView ivUserAvatar;
        private TextView tvMailContent;

        public SomeOneViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            tvMailContent = itemView.findViewById(R.id.tv_chat_content);
        }
    }
}
