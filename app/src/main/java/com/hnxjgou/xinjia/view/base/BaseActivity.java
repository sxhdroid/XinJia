package com.hnxjgou.xinjia.view.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

/**
 */
public class BaseActivity<T> extends AppCompatActivity implements IBaseView<T>, IFragmentCallback {

    private final String TAG = "BaseActivity";

    private ProgressDialog progressDialog;

    private Toast mToast;
    private ActionBar actionBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        mToast = Toast.makeText(this, null, Toast.LENGTH_SHORT);

        initActionBar();
    }

    private void initActionBar() {
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    public void setActionBarTitle(@StringRes int resId){
        actionBar.setTitle(resId);
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
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void showToast(String msg) {
        mToast.setText(msg);
        mToast.show();
    }

    @Override
    public void showErr(String err) {
        showToast(err);
    }

    @Override
    public void showData(T data) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onFragmentClickEvent(int clicked) {

    }
}
