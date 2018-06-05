package com.hnxjgou.xinjia.view.user;

import android.os.Bundle;

import com.hnxjgou.xinjia.R;
import com.hnxjgou.xinjia.view.base.BaseActivity;

public class UserActivity extends BaseActivity<String> {

    private final static String TAG = "UserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setActionBarTitle(R.string.login);

        showFragment(LoginFragment.newInstance(), false);
    }

    @Override
    public void onFragmentClickEvent(int clicked) {
        if (clicked == R.id.tv_register){
            //注册
            showFragment(new RegisterFragment(), true);
        }else {

        }
    }
}
