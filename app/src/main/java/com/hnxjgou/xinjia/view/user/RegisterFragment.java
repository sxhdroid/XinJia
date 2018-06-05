package com.hnxjgou.xinjia.view.user;

import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.hnxjgou.xinjia.utils.StringUtil;
import com.hnxjgou.xinjia.view.base.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 2018/5/21.
 */

public class RegisterFragment extends BaseFragment<String> implements View.OnClickListener, TextWatcher {

    private EditText edt_phone, edt_pwd, edt_confirm_pwd, edt_code; // 手机号码、密码、确认密码、验证码输入框
    private Button btn_register;// 注册按钮
    private TextView tv_get_code; // 获取验证码按钮

    private CountDownTimer countDownTimer; // 验证码发送倒计时


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
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
        countDownTimer = null;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        edt_phone = view.findViewById(R.id.et_phone);
        edt_pwd = view.findViewById(R.id.et_pwd);
        edt_confirm_pwd = view.findViewById(R.id.et_confirm_pwd);
        edt_code = view.findViewById(R.id.et_code);

        btn_register = view.findViewById(R.id.btn_register);

        tv_get_code = view.findViewById(R.id.tv_get_code);

        btn_register.setOnClickListener(this);
        tv_get_code.setOnClickListener(this);

        edt_phone.addTextChangedListener(this);
        edt_code.addTextChangedListener(this);
        edt_pwd.addTextChangedListener(this);
        edt_confirm_pwd.addTextChangedListener(this);

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_get_code.setClickable(false);
                tv_get_code.setText(getString(R.string.re_get_code, millisUntilFinished / 1000));
                tv_get_code.setBackgroundResource(R.drawable.shape_rec_bbbbbb_without_fill);
                tv_get_code.setTextColor(getResources().getColor(R.color.color_bbbbbb));
            }

            @Override
            public void onFinish() {
                tv_get_code.setBackgroundResource(R.drawable.shape_rec_e49d35);
                tv_get_code.setText(R.string.get_code);
                tv_get_code.setTextColor(getResources().getColor(R.color.color_e49d35));
                tv_get_code.setClickable(true);
            }
        };
    }

    @Override
    public void showLoading(Object tag) {
        if ((int)tag == R.id.btn_register) {
            super.showLoading(tag);
        }else {
            countDownTimer.start();
        }
    }

    @Override
    public void hideLoading(Object tag) {
        if ((int)tag == R.id.btn_register) {
            super.hideLoading(tag);
        }else {
//            countDownTimer.cancel();
//            tv_get_code.setBackgroundResource(R.drawable.shape_rec_e49d35);
//            tv_get_code.setText(R.string.get_code);
//            tv_get_code.setTextColor(getResources().getColor(R.color.color_e49d35));
//            tv_get_code.setClickable(true);
        }
    }

    @Override
    public void showData(String data, Object tag) {
        if ((int)tag == R.id.btn_register) {// 注册成功返回
            finish();
        }else {
            // 获取验证码成功
            showToast(getString(R.string.get_code_successed));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_register){
            // 注册按钮点击事件
            if (validate()){
                // 执行注册逻辑
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("phone", edt_phone.getText().toString());
                    jsonObject.put("code", edt_code.getText().toString());
                    jsonObject.put("password", edt_confirm_pwd.getText().toString());
                    Map<String, String> params = new HashMap<>();
                    params.put("json", jsonObject.toString());
                    doPost(ApiConfig.user.build_url(ApiConfig.user.API_REGISTER), v.getId(), params);
                }catch (JSONException e){
                }
            }
        }else {
            // 获取验证码点击事件
            if (TextUtils.isEmpty(edt_phone.getText())){
                showToast(edt_phone.getHint().toString());
            }else if (!StringUtil.isMobileNO(edt_phone.getText().toString())){
                showToast(getString(R.string.rightful_phone));
            }else {
                // 获取验证码
                Map<String, String> params = new HashMap<>();
                params.put("phone", edt_phone.getText().toString());
                params.put("type", "1");
                doPost(ApiConfig.user.build_url(ApiConfig.user.API_CODE), v.getId(), params);
            }
        }
    }

    // 验证输入框的合法性
    private boolean validate(){

        boolean isValidate = false;

        if (!StringUtil.isMobileNO(edt_phone.getText().toString())){ // 手机号码不正确
            showToast(getString(R.string.rightful_phone));
        }else if (!edt_pwd.getText().toString().equals(edt_confirm_pwd.getText().toString())) {
            // 两次密码不一致
            showToast(getString(R.string.pwd_unidentical));
        }else {
            isValidate = true;
        }

        return  isValidate;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(edt_phone.getText()) || TextUtils.isEmpty(edt_code.getText())
                || TextUtils.isEmpty(edt_pwd.getText()) || TextUtils.isEmpty(edt_confirm_pwd.getText())) {
            btn_register.setEnabled(false);
        }else {
            btn_register.setEnabled(true);
        }
    }
}
