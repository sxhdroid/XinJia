package com.hnxjgou.xinjia.presenter;

import android.app.Activity;

import com.google.gson.JsonSyntaxException;
import com.hnxjgou.xinjia.R;
import com.hnxjgou.xinjia.model.Callback;
import com.hnxjgou.xinjia.model.OkHttpUtil;
import com.hnxjgou.xinjia.model.UserManager;
import com.hnxjgou.xinjia.utils.GsonUtil;
import com.hnxjgou.xinjia.utils.LogUtil;
import com.hnxjgou.xinjia.view.base.IBaseView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * MVP模式的，泛型支持List，但是List泛型的类型不能再是泛型类型。
 */

public class BasePresenter<T, V extends IBaseView> implements Callback<T> {

    protected final String TAG = getClass().getSimpleName();

    private List<Object> tags;

    public Type mType;

    public BasePresenter(Class<?> clazz){
        mType = getSuperClassGenricType(clazz, 0);
    }

    /**
     * 绑定的view
     */
    protected V mvpView;

    /**
     * 绑定view，一般在初始化中调用该方法
     */
    public void attachView(V mvpView) {
        this.mvpView = mvpView;
        tags = new ArrayList<>();
    }

    /**
     * 断开view，一般在onDestroy中调用
     */
    public void detachView() {
        this.mvpView = null;
        if (tags != null) {
            for (Object tag: tags) {
                OkHttpUtil.getInstance().cancelTag(tag);
            }
            tags.clear();
        }
        tags = null;
    }

    /**
     * 是否与View建立连接
     * 每次调用业务请求的时候都要出先调用方法检查是否与View建立连接
     */
    protected boolean isViewAttached() {
        return mvpView != null;
    }

    /**
     * 获取连接的view
     */
    protected V getView() {
        return mvpView;
    }

    /**
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     *
     *
     * @param clazz
     *            clazz The class to introspect
     * @param index
     *            the Index of the generic ddeclaration,start from 0.  @return the index generic declaration, or Object.class if cannot be
     *         determined
     */
    @SuppressWarnings("unchecked")
    protected Type getSuperClassGenricType(final Class<?> clazz, final int index) {

        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        //返回表示此类型实际类型参数的 Type 对象的数组。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }

        return params[index];
    }

    @Override
    public void onPrepare(final Object tag) {
        ((Activity)getView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mvpView.showLoading(tag);
            }
        });
    }

    @Override
    public void onSuccess(final T data, final Object tag) {
        if (getView() == null) return;
        ((Activity)getView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mvpView.showData(data, tag);
            }
        });
    }

    @Override
    public void onFailure(final Object tag, final String msg) {
        if (getView() == null) {
            return;
        }
        ((Activity)getView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mvpView.showErr(tag, msg);
            }
        });
    }

    @Override
    public void onError(final Object tag, final String error) {
        if (getView() == null) {
            return;
        }
        ((Activity)getView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mvpView.showErr(tag, error);
            }
        });
    }

    @Override
    public void onComplete(final Object tag) {
        if (getView() == null) {
            return;
        }
        ((Activity)getView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mvpView.hideLoading(tag);
            }
        });
        tags.remove(tag);
    }

    @Override
    public void onFailure(final Call call, final IOException e) {
        if (getView() == null) {
            return;
        }
        ((Activity)getView().getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onFailure(call.request().tag(), e.getMessage());
                onComplete(call.request().tag());
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (getView() == null) {
            return;
        }
        int code = response.code();
        LogUtil.i(TAG, "接口返回状态:" + code);
        if (code == 200) {
            try {
                String responseStr = response.body().string();
                LogUtil.i(TAG, "接口返回:" + responseStr);
                JSONObject jsonObject = new JSONObject(responseStr);
                int api_code = jsonObject.optInt("Code", -1);
                if (api_code == 200) { // 接口调用逻辑成功返回
                    UserManager.getInstance().setToken(jsonObject.optString("Token", ""));
                    if (mType.equals(String.class) || mType.equals(Object.class)) {
                        onSuccess((T) responseStr, response.request().tag());
                    }else if (mType instanceof ParameterizedType){
                        // 如果又是泛型类型，则使用List返回
                        try {
                            onSuccess((T) GsonUtil.jsonArray2List(responseStr, "Data"
                                    , ((ParameterizedType) mType).getActualTypeArguments()[0]), response.request().tag());
                        }catch (JsonSyntaxException e){
                            onError(response.request().tag(), getView().getContext().getString(R.string.data_parse_error));
                        }
                    }else {
                        try {
                            onSuccess((T) GsonUtil.json2Obj(responseStr, "Data", mType), response.request().tag());
                        }catch (JsonSyntaxException e){
                            onError(response.request().tag(), getView().getContext().getString(R.string.data_parse_error));
                        }
                    }
                }else {
                    // 接口调用出现异常
                    onError(response.request().tag(), jsonObject.optString("Message", getView().getContext().getString(R.string.data_parse_error)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            LogUtil.i(TAG, "错误信息: " + response.message());
            onError(response.request().tag(), response.message());
        }
        onComplete(response.request().tag());
        response.close();
    }

    public void onLoadDataByGet(String url, Object tag){
        if (isViewAttached()) {
            onPrepare(tag);
            tags.add(tag == null ? url : tag);
            OkHttpUtil.getInstance().requestGetAPI(url, tag, this);
        }
    }

    public void onLoadDataByPost(String url, Object tag, Map<String, String> params){
        if (isViewAttached()) {
            onPrepare(tag);
            tags.add(tag == null ? url : tag);
            OkHttpUtil.getInstance().requestPostAPI(url, tag, params, this);
        }
    }
}