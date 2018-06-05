package com.hnxjgou.xinjia.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hnxjgou.xinjia.R;
import com.hnxjgou.xinjia.view.base.BaseFragment;

/**
 * 购物车页
 */
public class CartFragment extends BaseFragment<String> {


    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }
}
