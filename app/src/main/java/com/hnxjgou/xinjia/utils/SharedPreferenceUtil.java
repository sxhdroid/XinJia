package com.hnxjgou.xinjia.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceUtil {

    private final static String PREFERENCE_NAME = "com_hnxjgou_xinjia";

    /**
     * 存储数据，未提交。
     *
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     *
     * @return Returns a reference to the same Editor object, so you can
     * chain put calls together.
     */
    public static Editor saveData(Context context, String key, Object value){
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        if (value instanceof String) {
            try {
                editor.putString(key, AESUtil.encrypt((String) value));
            } catch (Exception e) {
            }
        }else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        }else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        }else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        }

        return editor;
    }

    /**
     * 存储数据，已经提交。
     * @param context
     * @param key
     * @param value
     */
    public static void saveDataCommited(Context context, String key, Object value) {
        saveData(context, key, value).commit();
    }

    /**
     * 获取存储的数据
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static Object getData(Context context, String key, Object defValue) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (defValue instanceof Integer) {
            return preferences.getInt(key, (Integer) defValue);
        }else if (defValue instanceof Long) {
            return preferences.getLong(key, (Long) defValue);
        }else if (defValue instanceof String) {
            try {
                return AESUtil.decrypt(preferences.getString(key, (String) defValue));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (defValue instanceof Boolean) {
            return preferences.getBoolean(key, (Boolean) defValue);
        }else {
            return preferences.getString(key, (String) defValue);
        }
        return null;
    }

    public static boolean remove(Context context, String key) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().remove(key).commit();
    }

    public static boolean contains(Context context, String key) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).contains(key);
    }

    /**将用户信息本地化存储*/
//    public void putUserInfo(UserBean userBean) {
//        if (userBean == null) {
//            cleanUserInfo();
//        } else {
//            editor.putFloat("assets", userBean.getAssets())
//                    .putInt("isVip", userBean.getIsVip())
//                    .putFloat("remainingSum", userBean.getRemainingSum())
//                    .putFloat("commission", userBean.getCommission())
//                    .putFloat("informationFee", userBean.getInformationFee())
//                    .putInt("age", userBean.getAge())
//                    .putString("headImage", userBean.getHeadImage())
//                    .putString("name", userBean.getName())
//                    .putString("ID", userBean.getID())
//                    .putString("telephone", userBean.getTelephone())
//                    .putString("bank", userBean.getBank())
//                    .putString("bankNumber", userBean.getBankNumber())
//                    .putString("city", userBean.getCity())
//                    .putString("occupation", userBean.getOccupation())
//                    .putLong("user_id", userBean.getUser_id())
//                    .putString("appUserId", userBean.getAppUserId()).commit();
//        }
//
//    }

    /**清除用户数据*/
//    public void cleanUserInfo() {
//        editor.remove("assets")
//                .remove("isVip")
//                .remove("remainingSum")
//                .remove("commission")
//                .remove("informationFee")
//                .remove("age")
//                .remove("headImage")
//                .remove("name")
//                .remove("ID")
//                .remove("telephone")
//                .remove("bank")
//                .remove("bankNumber")
//                .remove("city")
//                .remove("occupation")
//                .remove("user_id")
//                .remove("appUserId").commit();
//    }

    /**获取本地保存的用户信息*/
//    public UserBean getUserInfo(){
//        UserBean userBean = null;
//        if (contains("appUserId") && contains("telephone")){
//            //将本地数据取出
//            userBean = new UserBean();
//            userBean.setAssets(preferences.getFloat("assets", 0));
//            userBean.setIsVip(preferences.getInt("isVip", 2));
//            userBean.setRemainingSum(preferences.getFloat("remainingSum", 0));
//            userBean.setCommission(preferences.getFloat("commission", 0));
//            userBean.setInformationFee(preferences.getFloat("informationFee", 0));
//            userBean.setAge(preferences.getInt("age", 0));
//            userBean.setHeadImage(preferences.getString("headImage", null));
//            userBean.setName(preferences.getString("name", null));
//            userBean.setID(preferences.getString("ID", null));
//            userBean.setTelephone(preferences.getString("telephone", null));
//            userBean.setBank(preferences.getString("bank", null));
//            userBean.setBankNumber(preferences.getString("bankNumber", null));
//            userBean.setCity(preferences.getString("city", null));
//            userBean.setOccupation(preferences.getString("occupation", null));
//            userBean.setAppUserId(preferences.getString("appUserId", null));
//            userBean.setUser_id(preferences.getLong("user_id", 0));
//        }
//        return userBean;
//    }

}
