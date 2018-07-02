package com.hnxjgou.xinjia.view.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.hnxjgou.xinjia.R;
import com.hnxjgou.xinjia.presenter.BasePresenter;

import java.util.Map;

/**
 */
public class BaseActivity<T> extends AppCompatActivity implements IBaseView<T>, IFragmentCallback {

    protected final String TAG = getClass().getSimpleName();

    private ProgressDialog progressDialog;

    private Toast mToast;
    protected ActionBar actionBar;

    private BasePresenter<T, IBaseView> basePresenter;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        mToast = Toast.makeText(this, null, Toast.LENGTH_SHORT);

        initActionBar();

        basePresenter = new BasePresenter<>(getClass());
        basePresenter.attachView(this);
    }

    private void initActionBar() {
        actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
    }

    public void setActionBarTitle(@StringRes int resId){
        if (actionBar ==  null) {
            return;
        }
        actionBar.setTitle(resId);
    }

    public void setActionBarTitle(String title){
        if (actionBar == null) {
            return;
        }
        actionBar.setTitle(title);
    }

    protected void hideActionBar(){
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    protected void showActionBar(){
        if (actionBar != null) {
            actionBar.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示指定的Fragment
     * @param fragment
     * @param addToBack 是否支持返回
     */
    protected void showFragment(Fragment fragment, boolean addToBack){
        if (fragment instanceof BaseFragment) ((BaseFragment) fragment).setFragmentCallback(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_right_out, R.anim.push_right_in, R.anim.push_left_out);
        transaction.replace(R.id.fl_container, fragment, fragment.getClass().getSimpleName());
        if (addToBack) transaction.addToBackStack(fragment.getTag());
        transaction.commit();
    }

    @Override
    public void finish() {
        //fragmentmanager栈中还有可以回退的fragment
        if (getSupportFragmentManager().getBackStackEntryCount() >= 1)
            getSupportFragmentManager().popBackStack();
        else super.finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        basePresenter.detachView();
    }

    @Override
    public void showLoading(Object tag) {
        progressDialog.show();
    }

    @Override
    public void hideLoading(Object tag) {
        progressDialog.dismiss();
    }

    @Override
    public void showToast(String msg) {
        if (TextUtils.isEmpty(msg)) return;
        mToast.setText(msg);
        mToast.show();
    }

    @Override
    public void showErr(String err) {
        showToast(err);
    }

    @Override
    public void showData(T data, Object tag) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onFragmentClickEvent(int clicked) {

    }
}
