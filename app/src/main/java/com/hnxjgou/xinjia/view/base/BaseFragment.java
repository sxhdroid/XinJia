package com.hnxjgou.xinjia.view.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.View;

import com.hnxjgou.xinjia.presenter.BasePresenter;

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

    /**
     * POST方式获取数据
     * @param url
     * @param jsonParams
     */
    protected void doPost(String url, String jsonParams){
        basePresenter.onLoadDataByPost(url, jsonParams);
    }

    /**
     * GET方式请求获取数据
     * @param url
     */
    protected void doGet(String url){
        basePresenter.onLoadDataByGet(url);
    }

    @Override
    public void showLoading() {
        if (context != null && context instanceof BaseActivity) ((BaseActivity) context).showLoading();
    }

    @Override
    public void hideLoading() {
        if (context != null && context instanceof BaseActivity) ((BaseActivity) context).hideLoading();
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
    public void showData(T data) {

    }
}
