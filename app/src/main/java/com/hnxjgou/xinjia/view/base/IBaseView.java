package com.hnxjgou.xinjia.view.base;

import android.content.Context;

/**
 * Created by apple on 2018/5/4.
 */

public interface IBaseView<T> {

    /**
     * 网络请求开始时执行该方法
     */
    void showLoading(Object tag);

    /**
     * 网络请求完成时执行该方法
     */
    void hideLoading(Object tag);

    /**
     * 显示提示
     *
     * @param msg
     */
    void showToast(String msg);

    /**
     * 显示请求错误提示
     */
    void showErr(String err);

    /**
     *  显示请求错误提示
     * @param tag 用来区分不同业务逻辑
     * @param err 错误消息
     */
    void showErr(Object tag, String err);


    /**
     * 请求成功显示数据
     * @param data
     * @param tag 用以区分不同请求的数据显示。与请求传入的一致。
     */
    void showData(T data, Object tag);

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    Context getContext();
}
