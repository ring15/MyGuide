package com.ring.myguide.data;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by ring on 2019/11/27.
 */
public class TransToRequestBody {

    public static RequestBody toRequestBody(String value) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), value);
        return requestBody;
    }

}
