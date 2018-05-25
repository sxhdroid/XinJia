package com.hnxjgou.xinjia.view.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.hnxjgou.xinjia.R;
import com.hnxjgou.xinjia.view.base.BaseActivity;
import com.hnxjgou.xinjia.view.base.BaseFragment;

public class UserActivity extends BaseActivity<String> {

    private final static String TAG = "UserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setActionBarTitle(R.string.login);

        showFragment(LoginFragment.newInstance(), false);
    }

    private void showFragment(Fragment fragment, boolean addToBack){
        if (fragment instanceof BaseFragment) ((BaseFragment) fragment).setFragmentCallback(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, fragment, fragment.getClass().getSimpleName());
        if (addToBack) transaction.addToBackStack(fragment.getTag());
        transaction.commit();
    }

    @Override
    public void onFragmentClickEvent(int clicked) {
        if (clicked == R.id.tv_register){
            //注册
            showFragment(new RegisterFragment(), true);
        }
    }
}
