package com.hnxjgou.xinjia.view.index;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.amap.api.location.AMapLocation;
import com.bumptech.glide.Glide;
import com.hnxjgou.xinjia.ApiConfig;
import com.hnxjgou.xinjia.R;
import com.hnxjgou.xinjia.model.entity.Business;
import com.hnxjgou.xinjia.utils.AMapLocationUtil;
import com.hnxjgou.xinjia.utils.LogUtil;
import com.hnxjgou.xinjia.view.adapter.CommonRecyclerAdapter;
import com.hnxjgou.xinjia.view.adapter.ViewHolder;
import com.hnxjgou.xinjia.view.base.BaseActivity;
import com.hnxjgou.xinjia.widget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家列表界面
 */
public class BusinessListActivity extends BaseActivity<List<Business>> implements AMapLocationUtil.OnLocationListener {

    private int categoryId; // 商家所属的类别
    private int pageSize = 20; // 分页加载的页大小
    private int currentPage = 0; // 分页加载当前页索引，从0开始

    private boolean hasMore = false; //  是否有更多数据需要加载

    private List<Business> businessList; // 商家列表

    private BusinessAdapter businessAdapter;

    private RecyclerView recyclerView;
    private LinearLayout ly_error; // 加载错误布局

    private Button btn_reload;// 重新加载

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_list);

        categoryId = getIntent().getIntExtra("categoryId", -1);
        if (categoryId == -1) {
            showToast(getString(R.string.data_error));
            return;
        }
        setActionBarTitle(getIntent().getStringExtra("categoryName"));

        initView();
        AMapLocationUtil.getInstance().addLocationListener(this);
        AMapLocationUtil.getInstance().startLocation();
        loadData();

    }

    private void initView(){
        recyclerView = findViewById(R.id.rv_common);
        ly_error = findViewById(R.id.ly_load_error);
        btn_reload = findViewById(R.id.btn_reload);

        ly_error.setVisibility(View.GONE);
        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 重新加载点击事件
                loadData();
            }
        });

        // 初始化适配器相关
        businessList = new ArrayList<>();
        businessAdapter = new BusinessAdapter(this, businessList, R.layout.item_business);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 自定义分割线
        RecycleViewDivider divider = new RecycleViewDivider(this, RecycleViewDivider.VERTICAL_LIST);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.shape_trans_divider));
        recyclerView.addItemDecoration(divider); // 添加分割线
        recyclerView.setAdapter(businessAdapter);
        businessAdapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                // 跳转到商家商品列表
                startActivity(new Intent(BusinessListActivity.this, GoodsByCategoryActivity.class)
                        .putExtra("businessId", businessAdapter.getItem(position).BusinessId)
                        .putExtra("userId", businessAdapter.getItem(position).UserId)
                        .putExtra("businessName", businessAdapter.getItem(position).BusinessName));
            }
        });

        // 设置滑动监听，用来实现分页加载
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 这里在加入判断，判断是否滑动到底部
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    // 滑动停止了
                    if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                            >= recyclerView.computeVerticalScrollRange()){
                        // 滑动到底部了
//                        LogUtil.e(TAG, "滑动到底部了");
                        if (hasMore) loadData();
                    }
                }
            }
        });
    }

    // 加载数据
    private void loadData() {
        Map<String, String> params = new HashMap<>();
        params.put("pageSize", pageSize + "");
        params.put("currentPage", currentPage + "");
        params.put("categoryId", categoryId + "");
        doPost(ApiConfig.business.build_url(ApiConfig.business.API_BUSINESS_LIST), null, params);
    }

    @Override
    public void onLocation(AMapLocation aMapLocation) {
//        LogUtil.e(TAG, "定位：" + aMapLocation.getAddress());
        businessAdapter.updateLocation(aMapLocation.getLongitude()
                , aMapLocation.getLatitude());
    }

    @Override
    public void showData(List<Business> data, Object tag) {
        if (currentPage == 0 && !businessList.isEmpty()) {
            businessList.clear();
        }
        businessList.addAll(data);
        businessAdapter.notifyDataSetChanged();
        currentPage += 1;
        // 判断是否还有更多数据需要加载
        if (data == null || data.isEmpty() || data.size() < pageSize) hasMore = false;
        else hasMore = true;
    }

    @Override
    public void showLoading(Object tag) {
        if (currentPage == 0) super.showLoading(tag);
    }

    @Override
    public void showErr(String err) {
        if (currentPage == 0) {
            // 加载第一页失败才显示加载失败的视图
            ly_error.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        AMapLocationUtil.getInstance().removeLocationListener(this);
        super.onDestroy();
    }

    public void onExtension(View view) {
        // 我要推广点击事件
        LogUtil.e(TAG, "我要推广");
    }

    private class BusinessAdapter extends CommonRecyclerAdapter<Business> {

        private double latitude, longitude;

        public BusinessAdapter(Context context, List<Business> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void convert(ViewHolder holder, int position, Business item) {
            // 设置商家名称
            holder.setText(R.id.tv_business_name, item.BusinessName);
            //设置商家logo
            Glide.with(BusinessListActivity.this)
                    .load(item.Logo)
                    .into((ImageView) holder.getView(R.id.iv_business_logo));
            // 设置商家评级
            RatingBar ratingBar = holder.getView(R.id.rb_business);
            ratingBar.setRating((float) item.Score);
            // 设置商家活动
            holder.setText(R.id.tv_discount, item.BusinessActivity);
            // 设置商家销售
            holder.setText(R.id.tv_sales, getString(R.string.sales, item.DayMarketing));
            holder.setText(R.id.tv_last_month_sales, getString(R.string.last_month_sales, item.MonthMarketing));
            // 设置商家地址
            holder.setText(R.id.tv_business_address, item.DetailedAddress);
            // 设置距离数据
            if (latitude != 0 && longitude != 0) {
                int distance_m = AMapLocationUtil.calculateDistance(latitude, longitude
                        , item.StoreLat, item.StoreLon);// 计算两点距离为m的数值
                if (distance_m / 1000 == 0) {
                    // 不足1000m，以m为单位
                    holder.setText(R.id.tv_distance,distance_m + "m");
                }else { // 以㎞为单位
                    holder.setText(R.id.tv_distance, getString(R.string.distance_km, distance_m / 1000.0));
                }
            }
        }

        /**
         * 更新距离数据
         * @param longitude 经度
         * @param latitude 维度
         */
        public void updateLocation(double longitude, double latitude) {
            if (AMapLocationUtil.calculateDistance(latitude, longitude, this.latitude, this.longitude) < 100) {
                // 如果新位置与上一次的位置变化不足100m，不更新界面
                return;
            }
            this.latitude = latitude;
            this.longitude = longitude;
            notifyDataSetChanged();
        }
    }

}
