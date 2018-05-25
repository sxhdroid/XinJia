package com.hnxjgou.xinjia.view.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hnxjgou.xinjia.R;
import com.hnxjgou.xinjia.view.base.BaseFragment;

/**
 * Created by apple on 2018/5/21.
 */

public class RegisterFragment extends BaseFragment {

    @Override
    public void onResume() {
        super.onResume();
        setActionBarTitle(R.string.register);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }



}
