package com.hnxjgou.xinjia.view.user;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hnxjgou.xinjia.R;
import com.hnxjgou.xinjia.model.entity.User;
import com.hnxjgou.xinjia.view.base.BaseFragment;
import com.hnxjgou.xinjia.widget.ImageTextView;

/**
 */
public class LoginFragment extends BaseFragment<User> implements View.OnClickListener {

    private Button btn_login; // 登录
    private TextView tv_forget_pwd, tv_regiester; // 忘记密码、注册
    private ImageTextView itv_wechat; // 微信登录

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionBarTitle(R.string.login);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        btn_login = view.findViewById(R.id.btn_login);
        tv_forget_pwd = view.findViewById(R.id.tv_forget_pwd);
        tv_regiester = view.findViewById(R.id.tv_register);
        itv_wechat = view.findViewById(R.id.itv_wechat);

        btn_login.setOnClickListener(this);
        tv_regiester.setOnClickListener(this);
        tv_forget_pwd.setOnClickListener(this);
        itv_wechat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login: // 登录
                break;
            case R.id.tv_forget_pwd: // 忘记密码
            case R.id.tv_register: // 注册
                if (fragmentCallback != null) {
                    fragmentCallback.onFragmentClickEvent(v.getId());
                }
                break;
            case R.id.itv_wechat:
                doGet("http://gank.io/api/data/Android/10/1");
                break;
        }
    }

    @Override
    public void showData(User data) {

    }
}
