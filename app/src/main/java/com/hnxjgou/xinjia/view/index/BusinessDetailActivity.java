package com.hnxjgou.xinjia.view.index;

import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.hnxjgou.xinjia.ApiConfig;
import com.hnxjgou.xinjia.R;
import com.hnxjgou.xinjia.model.entity.Business;
import com.hnxjgou.xinjia.view.base.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 商家详情界面
 */
public class BusinessDetailActivity extends BaseActivity<Business> {

    private int businessId; // 商家ID

    private Business business; // 商家实体类

    private LineChart lineChart; // 报表折线图

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_detail);

        businessId = getIntent().getIntExtra("businessId", -1);
        if (businessId == -1) {
            showToast(getString(R.string.data_error));
            return;
        }
//        LogUtil.e(TAG, "商家ID ： " + businessId);
        setActionBarTitle(getIntent().getStringExtra("businessName"));

        initViews();
        loadDetailInfo();

    }

    private void initViews(){
        lineChart = findViewById(R.id.sales_chart);
    }

    // 获取商家详情
    private void loadDetailInfo(){
        Map<String, String> params = new HashMap<>();
        params.put("businessId", businessId + "");
        doPost(ApiConfig.business.build_url(ApiConfig.business.API_BUSINESS_DETAIL), null, params);
    }

    @Override
    public void showData(Business data, Object tag) {

    }
}
