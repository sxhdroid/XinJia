package com.hnxjgou.xinjia.model;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by apple on 2018/5/23.
 */

public class OkHttpUtil {

    private OkHttpClient httpClient;

    private static OkHttpUtil instance;


    private OkHttpUtil(){
        httpClient = new OkHttpClient();
    }

    public static OkHttpUtil getInstance() {
        if (instance == null) {
            synchronized (OkHttpUtil.class) {
                if (instance == null) {
                    instance = new OkHttpUtil();
                }
            }
        }
        return instance;
    }

    // 执行Get网络请求，此类看需求由自己选择写与不写
    public void requestGetAPI(String url, Callback callback) {
        //这里写具体的网络请求
        Request request = new Request.Builder()
                .get()
                .url(url)
                .tag(url)
                .build();
        httpClient.newCall(request).enqueue(callback);
    }

    // 执行Post网络请求，此类看需求由自己选择写与不写
    public void requestPostAPI(String url, String jsonParams, Callback callback) {
        //这里写具体的网络请求
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        RequestBody body = RequestBody.create(JSON, jsonParams);
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .tag(url)
                .build();
        httpClient.newCall(request).enqueue(callback);
    }

    /**根据tag取消请求*/
    public void cancelTag(String tag) {
        for (Call call : httpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : httpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }
}
