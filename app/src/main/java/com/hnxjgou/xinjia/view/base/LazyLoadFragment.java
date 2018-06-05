package com.hnxjgou.xinjia.view.base;

import android.os.Bundle;

/**
 * Created by apple on 2018/5/30.
 */

public abstract class LazyLoadFragment<T> extends BaseFragment<T> {

    protected boolean isViewInitiated;
    protected boolean isDataLoaded;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareRequestData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        prepareRequestData();
    }

    public boolean prepareRequestData() {
        return prepareRequestData(false);
    }

    public boolean prepareRequestData(boolean forceUpdate) {
        if (getUserVisibleHint() && isViewInitiated && (!isDataLoaded || forceUpdate)) {
            requestData();
            isDataLoaded = true;
            return true;
        }
        return false;
    }

    /**
     * 实现懒加载数据逻辑
     */
    public abstract void requestData();
}
