package com.hnxjgou.xinjia.view.index;

import android.content.Context;
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
import com.hnxjgou.xinjia.view.adapter.CommonRecyclerAdapter;
import com.hnxjgou.xinjia.view.adapter.ViewHolder;
import com.hnxjgou.xinjia.widget.SwipeMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 根据类别查询所有商品列表界面。
 */
public class GoodsListActivity extends GoodsListBaseActivity<List<Goods>> {

    private int pageSize = 20; // 分页加载的页大小
    private int currentPage = 0; // 分页加载当前页索引，从0开始
    private int businessCategoryId; // 分类id

    private boolean hasMore = false; //  是否有更多数据需要加载

    private GoodsAdapter adapter;
    private List<Goods> goodsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        businessCategoryId = getIntent().getIntExtra("categoryId", 0);

        loadGoods();
    }

    @Override
    protected void initViews() {
        super.initViews();

        adapter = new GoodsAdapter(this, goodsList, R.layout.item_goods);
        adapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {

            }
        });
        goodsView.setAdapter(adapter);

        // 设置滑动监听，用来实现分页加载
        goodsView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Glide.with(getContext()).pauseRequests(); // 列表滚动暂停图片加载
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Glide.with(getContext()).resumeRequests(); // 列表停止滚动启动图片加载
                // 这里在加入判断，判断是否滑动到底部
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    // 滑动停止了
                    if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                            >= recyclerView.computeVerticalScrollRange()){
                        // 滑动到底部了
                        if (hasMore) loadGoods();
                    }
                }
            }
        });
    }

    // 获取商家商品列表
    @Override
    protected void loadGoods() {
        Map<String, String> params = new HashMap<>();
        params.put("pageSize", pageSize + "");
        params.put("currentPage", currentPage + "");
        params.put("businessCategoryId", businessCategoryId + "");
        doPost(ApiConfig.business.build_url(ApiConfig.business.API_GOODS), null, params);
    }

    @Override
    public void showData(List<Goods> data, Object tag) {
        if (currentPage == 0 && !goodsList.isEmpty()) {
            goodsList.clear();
        }
        goodsList.addAll(data);
        adapter.notifyDataSetChanged();
        currentPage += 1;
        // 判断是否还有更多数据需要加载
        if (data == null || data.isEmpty() || data.size() < pageSize) hasMore = false;
        else hasMore = true;
    }

    @Override
    public void showErr(String err) {
        if (currentPage == 0) {
            // 加载第一页失败才显示加载失败的视图
            super.showErr(err);
        }
    }

    // 商品列表适配器
    private class GoodsAdapter extends CommonRecyclerAdapter<Goods> {


        public GoodsAdapter(Context context, List<Goods> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void convert(final ViewHolder holder, final int position, final Goods goods) {
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

                        } else {
                            // 显示的是加入购物车按钮,执行加入购物车逻辑
                            addGoodsToCart((ImageView) holder.getView(R.id.iv_goods_logo), goods.CommodityId);
                            //关闭侧滑菜单
                            LinearLayoutManager layoutManager = (LinearLayoutManager) goodsView.getLayoutManager();
                            SwipeMenu swipeMenu = (SwipeMenu) layoutManager.findViewByPosition(position);
                            swipeMenu.close();
                        }
                    } else {
                        // 没有登录跳转登录界面
                        UserManager.getInstance().toLoginActivity(GoodsListActivity.this, 1);
                    }
                }
            });

        }
    }
}
