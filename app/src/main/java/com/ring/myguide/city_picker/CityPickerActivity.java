package com.ring.myguide.city_picker;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ring.myguide.R;
import com.ring.myguide.entity.City;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CityPickerActivity extends AppCompatActivity {

    //城市列表
    private RecyclerView mRecyclerView;
    private CityPickerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_picker);
        findView();
        init();
    }

    private void findView() {
        mRecyclerView = findViewById(R.id.recycler_city);
    }

    private void init() {
        mAdapter = new CityPickerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setClickListener(city -> {
            Intent intent = new Intent();
            intent.putExtra("city", city);
            setResult(RESULT_OK, intent);
            finish();
        });
        String json = getJson("city.json");
        List<City> cities = JsonCity(json);
        mAdapter.setCities(cities);
        mAdapter.notifyDataSetChanged();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
        }
    }

    private List<City> JsonCity(String json) {
        JSONObject root = JSON.parseObject(json);
        JSONObject data = root.getJSONObject("data");
        List<City> cities = new ArrayList<>();
        JSONArray cityArray = data.getJSONArray("areas");
        for (int i = 0; i < cityArray.size(); i++) {
            String string = cityArray.getString(i);
            JSONObject object = JSON.parseObject(string);
            City city = JSON.toJavaObject(object, City.class);
            cities.add(city);
        }
        return cities;
    }

    private String getJson(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public interface clickListener{
        void onClickItem(City city);
    }
}
