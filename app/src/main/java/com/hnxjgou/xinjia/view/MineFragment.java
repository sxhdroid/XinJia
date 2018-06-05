package com.hnxjgou.xinjia.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hnxjgou.xinjia.R;
import com.hnxjgou.xinjia.view.base.BaseFragment;

/**
 * 我的页
 */
public class MineFragment extends BaseFragment<String> {


    public MineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }
}
