package com.ring.myguide.city_picker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ring.myguide.R;
import com.ring.myguide.entity.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ring on 2019/12/5.
 */
public class CityPickerAdapter extends RecyclerView.Adapter<CityPickerAdapter.MyViewHolder> {

    private Context mContext;
    //城市列表数据
    private List<City> mCities = new ArrayList<>();
    //点击事件回调
    private CityPickerActivity.clickListener mClickListener;

    public CityPickerAdapter(Context context) {
        mContext = context;
    }

    public void setCities(List<City> cities) {
        mCities = cities;
    }

    public void setClickListener(CityPickerActivity.clickListener clickListener) {
        mClickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_city, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (mCities != null && mCities.size() > position) {
            holder.mCityText.setText(mCities.get(position).getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onClickItem(mCities.get(position));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mCities == null ? 0 : mCities.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mCityText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCityText = (TextView) itemView;
        }
    }
}
