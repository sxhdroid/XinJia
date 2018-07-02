package com.hnxjgou.xinjia.view.index;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.hnxjgou.xinjia.ApiConfig;
import com.hnxjgou.xinjia.R;
import com.hnxjgou.xinjia.model.entity.Business;
import com.hnxjgou.xinjia.model.entity.Sales;
import com.hnxjgou.xinjia.view.base.BaseActivity;
import com.hnxjgou.xinjia.widget.MyMarkerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 商家详情界面
 */
public class BusinessDetailActivity extends BaseActivity<Business> {

    private int businessId; // 商家ID

    private LineChart lineChart; // 报表折线图
    private TextView tv_sales, tv_orders, tv_amounts; // 销量、订单、营业额
    private TextView tv_y_amount; // 年营业额
    private TextView tv_discount;// 优惠活动
    private TextView tv_address;// 商家地址
    private TextView tv_business_hours;// 营业时间

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
        setupLineChart();

        tv_sales = findViewById(R.id.tv_sales);
        tv_orders = findViewById(R.id.tv_orders);
        tv_amounts = findViewById(R.id.tv_amounts);
        tv_y_amount = findViewById(R.id.y_amount);
        tv_discount = findViewById(R.id.tv_discount);
        tv_address = findViewById(R.id.tv_address);
        tv_business_hours = findViewById(R.id.tv_business_hours);
    }

    // 配置表格基本属性
    private void setupLineChart() {

        // no description text
        lineChart.getDescription().setEnabled(false);
        lineChart.setNoDataText(null);// 设置没有数据时显示文本

        // enable touch gestures
        lineChart.setTouchEnabled(true);
        lineChart.setDrawGridBackground(false); // 绘制图表背景

        // enable scaling and dragging
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);

        // Y轴
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setTextSize(12f);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(false);  // 设置是否绘制网格线
        leftAxis.setAxisLineColor(getResources().getColor(R.color.color_f2f2f2)); // 设置轴线颜色
//        leftAxis.setValueFormatter(new PercentFormatter()); // 设置数据显示格式

        // X轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceMax(1);
        xAxis.setGranularity(1f); // 设置间隔尺寸，影响轴值得浮点计算。
        xAxis.setAxisLineColor(getResources().getColor(R.color.color_f2f2f2)); // 设置轴线颜色
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return getString(R.string.x_month, (int)value);
            }
        });

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getLegend().setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT); // 设置图例位置
        lineChart.getLegend().setTextColor(Color.WHITE); // 设置图例文字颜色
        // 设置弹出标注
        MarkerView markerView = new MyMarkerView(this, R.layout.layout_chart_markerview);
        markerView.setChartView(lineChart);
        lineChart.setMarker(markerView);
    }

    // 获取商家详情
    private void loadDetailInfo(){
        Map<String, String> params = new HashMap<>();
        params.put("businessId", businessId + "");
        doPost(ApiConfig.business.build_url(ApiConfig.business.API_BUSINESS_DETAIL), null, params);
    }

    @Override
    public void showData(Business data, Object tag) {
        if (data == null) return;
        setChartData(data);

        tv_sales.setText(getString(R.string.t_sales, data.MonthMarketing));
        tv_orders.setText(getString(R.string.orders, data.OrderNumber));

        // 销售额以分为单位返回
        float turnover = data.Turnover / 100f; // 转换为元
        if (turnover > 10000) {
            // 超过1万元，用万作为单位显示
            tv_amounts.setText(getString(R.string.amounts_wan, turnover / 10000f));
        }else {
            tv_amounts.setText(getString(R.string.amounts, turnover));
        }

        //设置年销售额
        String amount = null;
        float y_turnover = data.YearTurnover / 100f; // 转换为元
        if (y_turnover > 10000) {
            amount = getString(R.string.y_amounts_wan, y_turnover / 10000f);
        }else {
            amount = getString(R.string.y_amounts, y_turnover);
        }
        SpannableString spannableString = new SpannableString(amount);
        // 改变颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.color_f26716));
        spannableString.setSpan(foregroundColorSpan, 8, spannableString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        // 改变尺寸
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(1.5f);
        spannableString.setSpan(sizeSpan, 8, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_y_amount.setText(spannableString);

//        actionBar.setDisplayUseLogoEnabled(true);//设置logo可以显示
//        actionBar.setDisplayShowHomeEnabled(true);
//        Glide.with(this).load(data.Logo).into(new SimpleTarget<GlideDrawable>(40, 30) {
//            @Override
//            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                actionBar.setLogo(resource);//设置logo的图标
//            }
//        });
        // 设置店铺优惠活动
        tv_discount.setText(data.BusinessActivity);
        // 商家地址
        tv_address.setText(data.DetailedAddress);
        // 营业时间
        tv_business_hours.setText(data.StartTime + "~" + data.ClosingTime);
    }

    @Override
    protected void onDestroy() {
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayUseLogoEnabled(false);
        super.onDestroy();
    }

    // 初始化表格数据并显示
    private void setChartData(Business business){

        List<Entry> entries = new ArrayList<>();

        if (business.BusinessSales == null) return;
        Random random = new Random();
        for (Sales data : business.BusinessSales) {
            entries.add(new Entry(data.Month, data.Amount + random.nextInt(100)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "月销售额（千）"); // add entries to dataset
        dataSet.setColor(getResources().getColor(R.color.white));
        dataSet.setValueTextColor(getResources().getColor(R.color.white)); // 设置曲线上的文字颜色
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawCircleHole(true);
        dataSet.setCircleColor(getResources().getColor(R.color.white));
        dataSet.setLineWidth(1.8f);
        dataSet.setCircleRadius(3.6f);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.animateXY(1500, 1500);
    }

    // 更多点击事件
    public void onMore(View view) {

    }
}
