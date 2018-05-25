package com.hnxjgou.xinjia.view.base;

/**
 * Fragment与宿主Activity通信的回调接口
 */
public interface IFragmentCallback {

    /**
     * fragment界面点击事件回调
     * @param clicked 点击的view ID
     */
    void onFragmentClickEvent(int clicked);
}
