package com.hnxjgou.xinjia.view.base;

import android.content.Context;

/**
 * Created by apple on 2018/5/4.
 */

public interface IBaseView<T> {

    /**
     * 显示正在加载view
     */
    void showLoading();

    /**
     * 关闭正在加载view
     */
    void hideLoading();

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
     * 请求成功显示数据
     * @param data
     */
    void showData(T data);

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    Context getContext();
}
