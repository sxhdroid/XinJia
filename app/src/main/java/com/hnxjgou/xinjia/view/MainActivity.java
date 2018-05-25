package com.hnxjgou.xinjia.view;

import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hnxjgou.xinjia.R;
import com.hnxjgou.xinjia.view.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNavigationBar();
    }

    // 初始化底部导航栏
    private void initNavigationBar(){
        BottomNavigationBar bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);

        bottomNavigationBar.clearAll();
        //固定模式
        bottomNavigationBar
                .setMode(BottomNavigationBar.MODE_FIXED);
        //背景固定不变
//        bottomNavigationBar
//                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        bottomNavigationBar
                .setActiveColor(R.color.colorPrimary)// 选中的颜色
                .setInActiveColor(R.color.color_999999) // 未选中的颜色
                .setBarBackgroundColor(R.color.white); // 底部背景设

        BadgeItem numberBadgeItem = new BadgeItem()
                .setBorderWidth(4)
                .setBackgroundColorResource(R.color.color_1d802e)
                .setText("5")
                .setHideOnSelect(true); // 小标

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.icon_code, getString(R.string.classify)))
                .addItem(new BottomNavigationItem(R.drawable.icon_phone, getString(R.string.shopping_cart)).setBadgeItem(numberBadgeItem))
                .addItem(new BottomNavigationItem(R.drawable.icon_pwd, getString(R.string.mine)))
                .initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {

            }
            @Override
            public void onTabUnselected(int position) {
            }
            @Override
            public void onTabReselected(int position) {

            }
        });
    }


}
