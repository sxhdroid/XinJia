package com.hnxjgou.xinjia.view.index;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hnxjgou.xinjia.ApiConfig;
import com.hnxjgou.xinjia.R;
import com.hnxjgou.xinjia.model.UserManager;
import com.hnxjgou.xinjia.model.entity.Goods;
import com.hnxjgou.xinjia.model.entity.GoodsCategory;
import com.hnxjgou.xinjia.view.adapter.CommonRecyclerAdapter;
import com.hnxjgou.xinjia.view.adapter.MultiTypeSupport;
import com.hnxjgou.xinjia.view.adapter.ViewHolder;
import com.hnxjgou.xinjia.widget.SwipeMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 按类别分类的商品列表界面。
 */
public class GoodsByCategoryActivity extends GoodsListBaseActivity<List<GoodsCategory>> {


    private GoodsByCategoryAdapter adapter;
    private List<GoodsCategory> goodsCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadGoods();
    }

    @Override
    protected void initViews(){
        super.initViews();

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
                startActivity(new Intent(GoodsByCategoryActivity.this, GoodsListActivity.class)
                        .putExtras(getIntent().getExtras())
                        .putExtra("categoryId", adapter.getGroupItem(position).BusinessCategoryId));
            }

            @Override
            public void onChildrenClick(RecyclerView.ViewHolder holder, int position) {
                // 商品项点击事件
            }
        });
        goodsView.setAdapter(adapter);
    }

    // 获取商家商品列表
    @Override
    protected void loadGoods(){
        Map<String, String> params = new HashMap<>();
        params.put("businessId", businessId + "");
        doPost(ApiConfig.business.build_url(ApiConfig.business.API_BUSINESS_CATEGORY), null, params);
    }

    @Override
    public void showData(List<GoodsCategory> data, Object tag) {
        goodsCategories.addAll(data);
        adapter.notifyDataSetChanged();
        ly_load_error.setVisibility(View.GONE);
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
        public void convert(final ViewHolder holder, final int position, GoodsCategory item) {
            if (getViewType(position) == viewTypeGroup) {
                holder.setText(R.id.tv_category_name, getGroupItem(position).Name);
                Glide.with(getContext())
                        .load(getGroupItem(position).TitleImage)
                        .error(R.mipmap.ic_launcher)
                        .placeholder(R.mipmap.ic_launcher)
                        .into((ImageView) holder.getView(R.id.iv_category));
            }else {
                final Goods goods = getChildItem(position);
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

                Button btn_opt = holder.getView(R.id.btn_opt);
                btn_opt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UserManager.getInstance().isLogin()) {
                            if (userId == UserManager.getInstance().getUserId()) {
                                // 用户是该商家的主人

                            }else {
                                // 显示的是加入购物车按钮,执行加入购物车逻辑
                                addGoodsToCart((ImageView) holder.getView(R.id.iv_goods_logo), goods);
                                //关闭侧滑菜单
                                LinearLayoutManager layoutManager = (LinearLayoutManager) goodsView.getLayoutManager();
                                SwipeMenu swipeMenu = (SwipeMenu) layoutManager.findViewByPosition(position);
                                swipeMenu.close();
                            }
                        }else {
                            // 没有登录跳转登录界面
                            UserManager.getInstance().toLoginActivity(GoodsByCategoryActivity.this, 1);
                        }
                    }
                });

            }
        }
    }
}
