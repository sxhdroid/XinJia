package com.hnxjgou.xinjia.view.user;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hnxjgou.xinjia.ApiConfig;
import com.hnxjgou.xinjia.R;
import com.hnxjgou.xinjia.common.Constant;
import com.hnxjgou.xinjia.model.UserManager;
import com.hnxjgou.xinjia.model.entity.User;
import com.hnxjgou.xinjia.utils.LogUtil;
import com.hnxjgou.xinjia.utils.SharedPreferenceUtil;
import com.hnxjgou.xinjia.utils.StringUtil;
import com.hnxjgou.xinjia.view.base.BaseFragment;
import com.hnxjgou.xinjia.widget.ImageTextView;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class LoginFragment extends BaseFragment<User> implements View.OnClickListener, TextWatcher {

    private EditText et_phone, et_pwd;
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

        et_phone = view.findViewById(R.id.et_phone);
        et_pwd = view.findViewById(R.id.et_pwd);

        et_phone.addTextChangedListener(this);
        et_pwd.addTextChangedListener(this);

        btn_login.setOnClickListener(this);
        tv_regiester.setOnClickListener(this);
        tv_forget_pwd.setOnClickListener(this);
        itv_wechat.setOnClickListener(this);

        if (TextUtils.isEmpty(et_pwd.getText()) || TextUtils.isEmpty(et_phone.getText())) {
            btn_login.setEnabled(false);
        }else {
            btn_login.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login: // 登录
                if (validate()){
                    doLogin();
                }
                break;
            case R.id.tv_forget_pwd: // 忘记密码
            case R.id.tv_register: // 注册
                if (fragmentCallback != null) {
                    fragmentCallback.onFragmentClickEvent(v.getId());
                }
                break;
            case R.id.itv_wechat:

                break;
        }
    }

    @Override
    public void showData(User data, Object tag) {
        if ((int)tag == R.id.btn_login){
            // 登录返回
            LogUtil.i(TAG, "登录成功");
            //保存登录信息
            SharedPreferenceUtil.putString(getContext(), Constant.SHARED_KEY_PHONE, et_phone.getText().toString());
            SharedPreferenceUtil .putString(getContext(), Constant.SHARED_KEY_PWD, et_pwd.getText().toString());
            UserManager.getInstance().updataUserInfo(data); // 更新用户信息
            getActivity().setResult(Activity.RESULT_OK);
            finish();
        }else if ((int)tag == R.id.itv_wechat){
            // 微信登录返回
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(et_phone.getText()) || TextUtils.isEmpty(et_pwd.getText())) {
            btn_login.setEnabled(false);
        }else {
            btn_login.setEnabled(true);
        }
    }

    private void doLogin() {
        Map<String, String> params = new HashMap<>();
        params.put("phone", et_phone.getText().toString());
        params.put("password", et_pwd.getText().toString());
        doPost(ApiConfig.user.build_url(ApiConfig.user.API_LOGIN), R.id.btn_login, params);
    }

    // 验证输入框的合法性
    private boolean validate(){

        boolean isValidate = false;

        if (!StringUtil.isMobileNO(et_phone.getText().toString())){ // 手机号码不正确
            showToast(getString(R.string.rightful_phone));
        }else {
            isValidate = true;
        }

        return  isValidate;
    }
}
