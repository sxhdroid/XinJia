package com.hnxjgou.xinjia.utils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.DPoint;

import java.util.ArrayList;
import java.util.List;

import static com.amap.api.location.CoordinateConverter.calculateLineDistance;

/**
 * 高德定位SDK工具类
 */

public final class AMapLocationUtil {

    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    private static AMapLocationUtil instance = null;
    // 自定义位置监听器
    private List<OnLocationListener> locationListeners;


    private AMapLocationUtil(){
        locationListeners = new ArrayList<>();
    }

    public static AMapLocationUtil getInstance() {
        if (instance == null) {
            instance = new AMapLocationUtil();
        }
        return instance;
    }

    public void addLocationListener(OnLocationListener listener){
        if (listener == null || mLocationClient == null || locationListeners.contains(listener)) return;
        locationListeners.add(listener);
    }

    public void removeLocationListener(OnLocationListener listener){
        if (listener == null) return;
        locationListeners.remove(listener);
    }

    public interface OnLocationListener {
        void onLocation(AMapLocation aMapLocation);
    }

    // 初始化定位客户端
    public void initLocationClient(Context context) {
        if (mLocationClient == null) {
            //初始化client
            mLocationClient = new AMapLocationClient(context);
            //设置定位参数
            mLocationClient.setLocationOption(getDefaultOption());
            // 设置定位监听
            mLocationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    for (OnLocationListener listener: locationListeners) {
                        listener.onLocation(aMapLocation);
                    }
                }
            });
        }
    }

    public void setLocationOption(AMapLocationClientOption option) {
        if (mLocationClient != null) {
            mLocationClient.setLocationOption(option);
        }
    }

    /**
     * 默认的定位参数
     * @since 2.8.0
     *
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(5 * 60 * 1000);//可选，设置定位间隔。单位为秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 开始定位
     *
     * @since 2.8.0
     *
     */
    public void startLocation(){
        if (mLocationClient != null) {
            // 启动定位
            mLocationClient.startLocation();
        }
    }

    /**
     * 最终退出程序时调用此方法停止定位，可多次启动和停止。
     *
     * @since 2.8.0
     *
     */
    public void stopLocation(){
        if (mLocationClient != null) {
            // 停止定位
            mLocationClient.stopLocation();
        }
    }

    /**
     * 销毁定位
     *
     * @since 2.8.0
     *
     */
    public void destroyLocation(){
        if (null != mLocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.onDestroy();
            mLocationClient = null;
            locationListeners.clear();
            locationListeners = null;
        }
    }

    public static int calculateDistance(double startLat, double startLng, double endLat, double endLng){
        return (int) calculateLineDistance(new DPoint(startLat, startLng), new DPoint(endLat, endLng));
    }
}
