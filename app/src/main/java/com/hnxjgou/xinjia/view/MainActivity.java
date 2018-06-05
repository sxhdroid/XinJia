package com.hnxjgou.xinjia.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hnxjgou.xinjia.R;
import com.hnxjgou.xinjia.view.base.BaseActivity;
import com.hnxjgou.xinjia.widget.CustomViewPager;

public class MainActivity extends BaseActivity {

    private CustomViewPager viewPager; // fragment页容器
    private BottomNavigationBar bottomNavigationBar; // 底部tab控件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                if (position == 0) setActionBarTitle(R.string.classify);
                else if (position == 1) setActionBarTitle(R.string.shopping_cart);
                else setActionBarTitle(R.string.mine);
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
