package com.hnxjgou.xinjia.view.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.View;

import com.hnxjgou.xinjia.presenter.BasePresenter;

import java.util.Map;

/**
 * Created by apple on 2018/5/9.
 */

public abstract class BaseFragment<T> extends Fragment implements IBaseView<T> {

    protected final String TAG = getClass().getSimpleName();

    protected IFragmentCallback fragmentCallback;
    private Context context;
    private BasePresenter<T, IBaseView> basePresenter;

    public void setFragmentCallback(IFragmentCallback fragmentCallback) {
        this.fragmentCallback = fragmentCallback;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
    }

    protected abstract void initView(View view, @Nullable Bundle savedInstanceState);

    protected void setActionBarTitle(@StringRes int resId) {
        if (context instanceof BaseActivity) ((BaseActivity) context).setActionBarTitle(resId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        basePresenter = new BasePresenter<>(getClass());
        basePresenter.attachView(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
        basePresenter.detachView();
    }

    @Override
    public Context getContext() {
        return context;
    }

    protected void finish() {
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }

    /**
     * POST方式获取数据
     * @param url
     * @param params
     * @param tag 用来标志多个请求的返回。传null默认为url。
     */
    protected void doPost(String url, Object tag, Map<String, String> params){
        basePresenter.onLoadDataByPost(url, tag, params);
    }

    /**
     * GET方式请求获取数据
     * @param url
     * @param tag 用来标志多个请求的返回。传null默认为url。
     */
    protected void doGet(String url, Object tag){
        basePresenter.onLoadDataByGet(url, tag);
    }

    @Override
    public void showLoading(Object tag) {
        if (context != null && context instanceof BaseActivity) ((BaseActivity) context).showLoading(tag);
    }

    @Override
    public void hideLoading(Object tag) {
        if (context != null && context instanceof BaseActivity) ((BaseActivity) context).hideLoading(tag);
    }

    @Override
    public void showToast(String msg) {
        if (context != null && context instanceof BaseActivity) ((BaseActivity) context).showToast(msg);
    }

    @Override
    public void showErr(String err) {
        if (context != null && context instanceof BaseActivity) ((BaseActivity) context).showErr(err);
    }

    @Override
    public void showData(T data, Object tag) {

    }
}
