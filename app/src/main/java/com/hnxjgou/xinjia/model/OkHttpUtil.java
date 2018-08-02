package com.hnxjgou.xinjia.model;

import com.hnxjgou.xinjia.utils.LogUtil;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by apple on 2018/5/23.
 */

public class OkHttpUtil {

    private OkHttpClient httpClient;

    private static OkHttpUtil instance;


    private OkHttpUtil(){
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
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

    /**
     * 执行Get网络请求
     * @param url 请求地址
     * @param tag
     */
    public void requestGetAPI(String url, Object tag, Callback callback) {
        //这里写具体的网络请求
        Request request = new Request.Builder()
                .get()
                .url(url)
                .tag(tag == null ? url : tag)
                .build();
        httpClient.newCall(request).enqueue(callback);
    }

    // 执行Post网络请求
    public void requestPostAPI(String url, Object tag, Map<String, String> params, Callback callback) {
        //这里写具体的网络请求
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key) == null ? "" : params.get(key));
            }
        }
        builder.add("user_token", UserManager.getInstance().getToken());
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .tag(tag == null ? url : tag)
                .build();
        LogUtil.i("requestPostAPI", "url = " + url);
//        LogUtil.i("requestPostAPI", "token = " + UserManager.getInstance().getToken());
        httpClient.newCall(request).enqueue(callback);
    }

    /**根据tag取消请求*/
    public void cancelTag(Object tag) {
        if (tag == null) return;
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
