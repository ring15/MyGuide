package com.ring.myguide.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by ring on 2019/11/19.
 */
public class JsonParse {

    public static JSONObject parseString(String response) throws Exception {
        JSONObject root = JSON.parseObject(response);
        String status = root.getString("status");
        if (status.equals("failed")) {
            throw new Exception(root.getString("data"));
        } else {
            return root.getJSONObject("data");
        }
    }

}
