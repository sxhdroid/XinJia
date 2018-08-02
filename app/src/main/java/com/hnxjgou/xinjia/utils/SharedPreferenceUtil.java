package com.hnxjgou.xinjia.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceUtil {


    private final static String PREFERENCE_NAME = "com_hnxjgou_xinjia";


    public static void putString(Context context, String key, String value){
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        try {
            editor.putString(key, AESUtil.encrypt(value));
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
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
}
