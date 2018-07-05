package com.hnxjgou.xinjia.model;

import android.app.Activity;
import android.content.Intent;

import com.hnxjgou.xinjia.model.entity.User;
import com.hnxjgou.xinjia.view.user.UserActivity;

/**
 * 用户管理
 */
public class UserManager {

    private static UserManager instance = null;
    // 用户实体类
    private User user;
    private String token; // 后台权限认证使用

    private UserManager() {
    }

    public static UserManager getInstance() {
        synchronized (UserManager.class) {
            if (instance == null) {
                instance = new UserManager();
            }
        }
        return instance;
    }

    /**
     * 更新用户信息，一般登录成功后调用
     * @param user
     */
    public void updataUserInfo(User user) {
        this.user = user;
    }

    /**
     * 判断用户是否登录
     * @return
     */
    public boolean isLogin(){
        return user !=  null;
    }

    public boolean isBusiness(){
        if (user == null) {
            return false;
        }

        if (user.Type == 0 || user.Type == 1) {
            return false;
        }

        return true;
    }

    public int getUserId() {
        return user == null ? 0 : user.Userid;
    }

    /**
     * 设置服务器返回的令牌
     * @param token
     */
    public void setToken(String token){
        this.token = token;
    }

    public String getToken() {
        return token == null ? "" : token;
    }

    /**
     * 以startActivityForResult方式跳转到登录界面
     * @param context
     * @param requestCode
     */
    public void toLoginActivity(Activity context, int requestCode){
        context.startActivityForResult(new Intent(context, UserActivity.class), 1);
    }
}
