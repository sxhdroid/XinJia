package com.hnxjgou.xinjia.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.hnxjgou.xinjia.ApiConfig;
import com.hnxjgou.xinjia.R;
import com.hnxjgou.xinjia.common.Constant;
import com.hnxjgou.xinjia.model.UserManager;
import com.hnxjgou.xinjia.model.entity.User;
import com.hnxjgou.xinjia.utils.LogUtil;
import com.hnxjgou.xinjia.utils.SharedPreferenceUtil;
import com.hnxjgou.xinjia.view.base.BaseActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 过渡页。可以用作启动广告展示页。
 */
public class SplashActivity extends BaseActivity<User> {

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        doLogin();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);
    }

    //如果登录过执行自动登录
    private void doLogin() {
        if (SharedPreferenceUtil.contains(this, Constant.SHARED_KEY_PHONE)
                && SharedPreferenceUtil.contains(this, Constant.SHARED_KEY_PWD)) {
            Map<String, String> params = new HashMap<>();
            params.put("phone", (String) SharedPreferenceUtil.getData(this, Constant.SHARED_KEY_PHONE, ""));
            params.put("password", (String) SharedPreferenceUtil.getData(this, Constant.SHARED_KEY_PWD, ""));
            doPost(ApiConfig.user.build_url(ApiConfig.user.API_LOGIN),null, params);
        }
    }

    // 重写此方法，以便不调用父类的显示进度条对话框
    @Override
    public void showLoading(Object tag) {
    }

    @Override
    public void showData(User data, Object tag) {
        LogUtil.i(TAG, "自动登录成功");
        UserManager.getInstance().updataUserInfo(data); // 更新用户信息
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
    }
}
