package com.hnxjgou.xinjia.presenter;

import com.hnxjgou.xinjia.model.Callback;
import com.hnxjgou.xinjia.model.OkHttpUtil;
import com.hnxjgou.xinjia.utils.GsonUtil;
import com.hnxjgou.xinjia.utils.LogUtil;
import com.hnxjgou.xinjia.view.base.IBaseView;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by apple on 2018/5/3.
 */

public class BasePresenter<T, V extends IBaseView> implements Callback<T> {

    protected final String TAG = getClass().getSimpleName();

    private List<String> urls;

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
        urls = new ArrayList<>();
    }

    /**
     * 断开view，一般在onDestroy中调用
     */
    public void detachView() {
        this.mvpView = null;
        if (urls != null) {
            for (String url: urls) {
                OkHttpUtil.getInstance().cancelTag(url);
            }
            urls.clear();
        }
        urls = null;
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
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return params[index];
    }

    @Override
    public void onPrepare() {
        mvpView.showLoading();
    }

    @Override
    public void onSuccess(T data) {
        LogUtil.d(TAG, "请求成功：" + data);
        mvpView.showData(data);
    }

    @Override
    public void onFailure(String msg) {
        mvpView.showToast(msg);
    }

    @Override
    public void onError(String error) {
        mvpView.showErr(error);
    }

    @Override
    public void onComplete() {
        mvpView.hideLoading();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        onFailure(e.getMessage());
        onComplete();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
//        LogUtil.d(TAG, "返回数据为：" + response.body().string());
        int code = response.code();
        if (code == 200) {
            onSuccess((T) GsonUtil.json2Obj(response.body().string(), mType));
        }else {
            onError(response.message());
        }
        onComplete();
    }

    public void onLoadDataByGet(String url){
        if (isViewAttached()) {
            onPrepare();
            urls.add(url);
            OkHttpUtil.getInstance().requestGetAPI(url, this);
        }
    }

    public void onLoadDataByPost(String url, String params){
        if (isViewAttached()) {
            onPrepare();
            urls.add(url);
            OkHttpUtil.getInstance().requestPostAPI(url, params, this);
        }
    }
}