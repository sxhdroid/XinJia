package com.hnxjgou.xinjia.model;

/**
 * Created by apple on 2018/5/3.
 * model回调接口
 */

public interface Callback<T> extends okhttp3.Callback {

    /**
     * 请求开始时调用
     */
    void onPrepare(Object tag);

    /**
     * 数据请求成功
     *
     * @param data 请求到的数据
     * @param tag 用来区分请求结果的标志。与请求传入的标志一致。
     */
    void onSuccess(T data, Object tag);

    /**
     * 请求数据失败，指在请求网络API接口请求方式时，出现无法联网、
     * 缺少权限，内存泄露等原因导致无法连接到请求数据源。
     */
    void onFailure(Object tag, String msg);

    /**
     * 使用网络API接口请求方式时，虽然已经请求成功但是由
     * 于{@code msg}的原因无法正常返回数据。
     */
    void onError(Object tag, String error);

    /**
     * 当请求数据结束时，无论请求结果是成功，失败或是抛出异常都会执行此方法给用户做处理，通常做网络
     * 请求时可以在此处隐藏“正在加载”的等待控件
     */
    void onComplete(Object tag);
}
