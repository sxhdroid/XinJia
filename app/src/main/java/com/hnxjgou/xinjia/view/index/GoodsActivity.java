package com.hnxjgou.xinjia.view.index;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hnxjgou.xinjia.ApiConfig;
import com.hnxjgou.xinjia.R;
import com.hnxjgou.xinjia.model.entity.Goods;
import com.hnxjgou.xinjia.model.entity.GoodsCategory;
import com.hnxjgou.xinjia.view.adapter.CommonRecyclerAdapter;
import com.hnxjgou.xinjia.view.adapter.MultiTypeSupport;
import com.hnxjgou.xinjia.view.adapter.ViewHolder;
import com.hnxjgou.xinjia.view.base.BaseActivity;
import com.hnxjgou.xinjia.widget.RecycleViewDivider;
import com.hnxjgou.xinjia.widget.SwipeMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家商品列表界面。按类别分
 */
public class GoodsActivity extends BaseActivity<List<GoodsCategory>> {

    private int businessId; // 商家ID
    private long userId;// 商家所属的用户id
    private RecyclerView goodsView; // 商品列表展示控件

    private LinearLayout ly_load_error; // 数据加载失败时显示

    private GoodsByCategoryAdapter adapter;
    private List<GoodsCategory> goodsCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        businessId = getIntent().getIntExtra("businessId", -1);
        if (businessId == -1) {
            showToast(getString(R.string.data_error));
            return;
        }
//        LogUtil.e(TAG, "商家ID ： " + businessId);
        setActionBarTitle(getIntent().getStringExtra("businessName"));
        userId = getIntent().getLongExtra("userId", 0);

        initViews();
        loadGoods();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.business_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_detail:
                startActivity(new Intent(this, BusinessDetailActivity.class)
                        .putExtra("businessId", businessId)
                        .putExtra("businessName", getTitle()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initViews(){
        goodsView = findViewById(R.id.rv_common);
        goodsView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecycleViewDivider divider = new RecycleViewDivider(getContext(), RecycleViewDivider.VERTICAL_LIST);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shape_trans_divider));
        goodsView.addItemDecoration(divider);

        // 列表滚动时优化图片加载
        goodsView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Glide.with(getContext()).pauseRequests(); // 列表滚动暂停图片加载
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Glide.with(getContext()).resumeRequests(); // 列表停止滚动启动图片加载
            }
        });

        ly_load_error = findViewById(R.id.ly_load_error);
        ly_load_error.setVisibility(View.GONE);
        // 重新加载按钮点击事件
        findViewById(R.id.btn_reload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGoods();
            }
        });

        adapter = new GoodsByCategoryAdapter(this, goodsCategories, new MultiTypeSupport() {
            @Override
            public int getLayoutId(int position) {
                return adapter.getViewType(position) == adapter.viewTypeGroup
                        ? R.layout.item_goods_category_lable : R.layout.item_goods;
            }
        });
        adapter.setOnExpandableItemClick(new CommonRecyclerAdapter.OnExpandableItemClick() {
            @Override
            public void onGroupClick(RecyclerView.ViewHolder holder, int position) {
                // 类别项点击事件
            }

            @Override
            public void onChildrenClick(RecyclerView.ViewHolder holder, int position) {
                // 商品项点击事件
            }
        });
        goodsView.setAdapter(adapter);
        goodsView.addOnItemTouchListener(new SwipeMenu.OnSwipeItemTouchListener(getContext()));

    }

    // 获取商家商品列表
    private void loadGoods(){
        Map<String, String> params = new HashMap<>();
        params.put("businessId", businessId + "");
        doPost(ApiConfig.business.build_url(ApiConfig.business.API_BUSINESS_CATEGORY), null, params);
    }

    @Override
    public void showData(List<GoodsCategory> data, Object tag) {
        goodsCategories.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showErr(String err) {
        ly_load_error.setVisibility(View.VISIBLE);
    }

    // 商品列表适配器
    private class GoodsByCategoryAdapter extends CommonRecyclerAdapter<GoodsCategory> {

        public final int viewTypeGroup = 1; // item的viewType为1，显示子类标签
        public final int viewTypeChild = 2; // item的viewType为2，显示子类标签

        private Map<Integer, Integer> groupItem = new LinkedHashMap<>();

        public GoodsByCategoryAdapter(Context context, List<GoodsCategory> data, MultiTypeSupport multiTypeSupport){
            super(context, data, multiTypeSupport);
        }

        @Override
        public int getItemCount() {
            int count = 0;
            for (int i = 0; i < mData.size(); i ++){
                groupItem.put(count, i); // 记录类标签的位置，便于实现二级列表的效果
                // 统计每个类别下商家的个数
                count += getChildItemCount(i) + 1; // 如果有商家，那么总项数为商家数+类别数
            }
            return count;
        }

        public GoodsCategory getGroupItem(int position) {
            return mData.get(groupItem.get(position));
        }

        public Goods getChildItem(int position){
            int group = -1;
            for (int key : groupItem.keySet()) {
                // 遍历找到子类的父类信息位置
                if (position < key) {
                    break;
                }
                group = key;
            }
            if (group == -1) return null;
            return getGroupItem(group).Commoditys.get(position - group - 1);
        }

        // 子列表长度
        private int getChildItemCount(int groupPosition) {
            if (mData.get(groupPosition).Commoditys == null) {
                return 0;
            }
            return mData.get(groupPosition).Commoditys.size();
        }

        /**
         * 获取指定位置的视图类型
         * @param position
         * @return
         */
        public int getViewType(int position){
            if (groupItem.get(position) == null) return viewTypeChild;
            return viewTypeGroup;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if (onExpandableItemClick != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getViewType(position) == viewTypeGroup) {
                            onExpandableItemClick.onGroupClick(holder, position);
                        }else {
                            onExpandableItemClick.onChildrenClick(holder, position);
                        }
                    }
                });
            }
            convert((ViewHolder) holder, position, null);
        }

        @Override
        public void convert(ViewHolder holder, int position, GoodsCategory item) {
            if (getViewType(position) == viewTypeGroup) {
                holder.setText(R.id.tv_category_name, getGroupItem(position).Name);
                Glide.with(getContext())
                        .load(getGroupItem(position).TitleImage)
                        .error(R.mipmap.ic_launcher)
                        .placeholder(R.mipmap.ic_launcher)
                        .into((ImageView) holder.getView(R.id.iv_category));
            }else {
                Goods goods = getChildItem(position);
                Glide.with(getContext())
                        .load(goods.Commoditylogo)
                        .error(R.mipmap.ic_launcher)
                        .placeholder(R.mipmap.ic_launcher)
                        .into((ImageView) holder.getView(R.id.iv_goods_logo));  // 设置商品图标
                holder.setText(R.id.tv_goods_name, goods.CommodityName); // 设置名称
                holder.setText(R.id.tv_discription, goods.Advertisement); // 设置描述语
                // 设置价格
                holder.setText(R.id.tv_price, getString(R.string.price, goods.ConcessionalPrice / 100f));
                // 设置原价格
                TextView textView = holder.getView(R.id.tv_original_price);
                textView.setText(getString(R.string.price, goods.OriginalPrice / 100f));
                textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 设置中划线

            }
        }
    }
}
