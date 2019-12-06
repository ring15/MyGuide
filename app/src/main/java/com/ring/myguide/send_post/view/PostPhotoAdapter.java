package com.ring.myguide.send_post.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ring.myguide.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ring on 2019/12/5.
 */
public class PostPhotoAdapter extends RecyclerView.Adapter<PostPhotoAdapter.MyViewHolder> {

    private Context mContext;
    //数据集
    private List<String> imgPaths = new ArrayList<>();
    //监听回调
    private SendPostActivity.onAdapterListener mListener;

    public PostPhotoAdapter(Context context) {
        mContext = context;
    }

    public void setImgPaths(List<String> imgPaths) {
        this.imgPaths = imgPaths;
    }

    public void setListener(SendPostActivity.onAdapterListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (position == 0) {
            holder.mPhotoImg.setImageResource(R.drawable.icon_add_photo);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imgPaths != null && imgPaths.size() < 9 && mListener != null) {
                        mListener.clickAddPhoto();
                    }
                }
            });
        } else {
            if (imgPaths != null && imgPaths.size() > position - 1) {
                Glide.with(mContext)
                        .load(imgPaths.get(position - 1))
                        .placeholder(R.drawable.icon_default_photo)
                        .error(R.drawable.icon_default_photo)
                        .into(holder.mPhotoImg);
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        holder.mDeletePhotoImg.setVisibility(View.VISIBLE);
                        return true;
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.mDeletePhotoImg.setVisibility(View.GONE);
                    }
                });
                holder.mDeletePhotoImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.deletePhoto(position - 1);
                        }
                        holder.mDeletePhotoImg.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return imgPaths == null ? 1 : 1 + imgPaths.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPhotoImg;
        private ImageView mDeletePhotoImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mPhotoImg = itemView.findViewById(R.id.img_photo);
            mDeletePhotoImg = itemView.findViewById(R.id.img_delete);
        }
    }

}
