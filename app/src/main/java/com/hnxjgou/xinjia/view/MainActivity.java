package com.hnxjgou.xinjia.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hnxjgou.xinjia.R;
import com.hnxjgou.xinjia.utils.AMapLocationUtil;
import com.hnxjgou.xinjia.view.base.BaseActivity;
import com.hnxjgou.xinjia.view.cart.CartFragment;
import com.hnxjgou.xinjia.view.index.ClassificationFragment;
import com.hnxjgou.xinjia.view.user.MineFragment;
import com.hnxjgou.xinjia.widget.CustomViewPager;

public class MainActivity extends BaseActivity {

    private static final int PERMISSION_REQUEST_CODE = 0;//申请权限请求码

    private CustomViewPager viewPager; // fragment页容器
    private BottomNavigationBar bottomNavigationBar; // 底部tab控件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        initPager();
        initNavigationBar();
    }

    //初始化tab页
    private void initPager(){
        viewPager = findViewById(R.id.cvp_fragment_container);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        viewPager.setScanScroll(true);
        viewPager.setCurrentItem(0);
        setActionBarTitle(R.string.classify);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationBar.selectTab(position, false);
                // 设置标题随tab页变化
                if (position == 0) {
                    setActionBarTitle(R.string.classify);
                    showActionBar();
                } else if (position == 1) {
                    setActionBarTitle(R.string.shopping_cart);
                    showActionBar();
                } else {
                    setActionBarTitle(R.string.mine);
                    hideActionBar();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // 初始化底部导航栏
    private void initNavigationBar(){
        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);

        bottomNavigationBar.clearAll();
        //固定模式
        bottomNavigationBar
                .setMode(BottomNavigationBar.MODE_FIXED);
        //背景固定不变
//        bottomNavigationBar
//                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        bottomNavigationBar
                .setActiveColor(R.color.colorPrimary)// 选中的颜色
                .setInActiveColor(R.color.color_333333) // 未选中的颜色
                .setBarBackgroundColor(R.color.white); // 底部背景设

//        BadgeItem numberBadgeItem = new BadgeItem()
//                .setBorderWidth(4)
//                .setBackgroundColorResource(R.color.color_1d802e)
//                .setText("5")
//                .setHideOnSelect(true); // 小标

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.icon_classify_normal, getString(R.string.classify)))
                .addItem(new BottomNavigationItem(R.drawable.icon_cart_normal, getString(R.string.shopping_cart)))
                .addItem(new BottomNavigationItem(R.drawable.icon_center_normal, getString(R.string.mine)))
                .initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                viewPager.setCurrentItem(position, false);
            }

            @Override
            public void onTabUnselected(int position) {
            }
            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AMapLocationUtil.getInstance().destroyLocation();
    }

    @SuppressLint("NewApi")
    private void checkPermissions(){
        if (isMarshmallow()) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Permission has not been granted and must be requested.
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    // Provide an additional rationale to the user if the permission was not granted
                    // and the user would benefit from additional context for the use of the permission.
                }
                // Request the permission. The result will be received in onRequestPermissionResult()
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_REQUEST_CODE);
            } else {
                // Permission is already available

            }
        }
    }

    // 判断是否是Android 6.0及以上系统
    private static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= 23;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                // Permission request was denied.
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter{

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0) {
                fragment = new ClassificationFragment();
            }else if (position == 1){
                fragment = new CartFragment();
            }else if (position == 2) {
                fragment = new MineFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
