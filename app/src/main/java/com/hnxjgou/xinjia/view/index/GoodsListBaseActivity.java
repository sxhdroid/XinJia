package com.hnxjgou.xinjia.view.index;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.hnxjgou.xinjia.R;
import com.hnxjgou.xinjia.model.UserManager;
import com.hnxjgou.xinjia.utils.LogUtil;
import com.hnxjgou.xinjia.view.base.BaseActivity;
import com.hnxjgou.xinjia.widget.RecycleViewDivider;
import com.hnxjgou.xinjia.widget.SwipeMenu;


/**
 * 商品列表界面基类
 */
public abstract class GoodsListBaseActivity<T> extends BaseActivity<T> {

    protected int businessId; // 商家ID
    protected int userId;// 商家所属的用户id
    private String businessName; // 商家名称

    protected RecyclerView goodsView; // 商品列表展示控件

    private LinearLayout ly_load_error; // 数据加载失败时显示
    private RelativeLayout mRootRl; // 界面根布局节点

    private Button btn_add_goods; // 添加商品
    private ImageView iv_to_top; // 滚动到顶部
    private ImageView iv_cart; // 购物车

    private PathMeasure mPathMeasure;

    protected abstract void loadGoods();

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
        setActionBarTitle(businessName = getIntent().getStringExtra("businessName"));
        userId = getIntent().getIntExtra("userId", 0);

        initViews();
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
                        .putExtra("businessName", businessName));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void initViews(){

        mRootRl = findViewById(R.id.rly_root);

        btn_add_goods = findViewById(R.id.btn_add_goods);
        // 如果当前登录的用户与该商家所属的用户是同一人，则显示添加商品按钮
        btn_add_goods.setVisibility(userId == UserManager.getInstance().getUserId() ? View.VISIBLE : View.GONE);
        iv_to_top = findViewById(R.id.iv_to_top);
        final Animation in = AnimationUtils.loadAnimation(this, R.anim.push_bottom_in);
        final Animation out = AnimationUtils.loadAnimation(this, R.anim.push_bottom_out);

        iv_cart = findViewById(R.id.iv_cart);

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
                Glide.with(getContext()).resumeRequests(); // 列表停止滚动启动图片加载
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Glide.with(getContext()).pauseRequests(); // 列表滚动暂停图片加载
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

        goodsView.addOnItemTouchListener(new SwipeMenu.OnSwipeItemTouchListener(getContext()));

        goodsView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (goodsView.getAdapter().getItemCount() == 0) return;
                // 停止滚动了
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                    //获取最后一个可见view的位置
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    // 总共显示的项数
                    int showItems = lastItemPosition - firstItemPosition;
                    if (firstItemPosition / showItems >= 2) {
                        if (iv_to_top.getVisibility() == View.VISIBLE) return; // 防止重复启动动画
                        iv_to_top.startAnimation(in);
                        iv_to_top.setVisibility(View.VISIBLE);
                    }else {
                        if (iv_to_top.getVisibility() == View.INVISIBLE) return; // 防止重复启动动画
                        iv_to_top.startAnimation(out);
                        iv_to_top.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

    }

    @Override
    public void showErr(String err) {
        ly_load_error.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // 登录成功后，需要根据权限判断显示界面内容，所以更新列表数据
            goodsView.getAdapter().notifyDataSetChanged();
            btn_add_goods.setVisibility(userId == UserManager.getInstance().getUserId() ? View.VISIBLE : View.GONE);
        }
    }

    // 添加商品按钮点击事件
    public void toAddGoods(View view) {

    }

    // 查看购物车按钮点击事件
    public void toCart(View view) {
        LogUtil.e(TAG, "=====cart");
    }

    // 滚动到顶部按钮事件
    public void toTop(View view) {
        goodsView.smoothScrollToPosition(0);
    }

    // 将货物添加至购物车
    protected void addGoodsToCart(ImageView imageView, int goodsId){

        final ImageView view = new ImageView(this);
        view.setImageDrawable(imageView.getDrawable());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100, 100);
        mRootRl.addView(view, layoutParams);

        //二、计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLoc = new int[2];
        mRootRl.getLocationInWindow(parentLoc);

        //得到商品图片的坐标（用于计算动画开始的坐标）
        int startLoc[] = new int[2];
        imageView.getLocationInWindow(startLoc);

        //得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        iv_cart.getLocationInWindow(endLoc);

        float startX = startLoc[0] - parentLoc[0] + imageView.getWidth()/2;
        float startY = startLoc[1] - parentLoc[1] + imageView.getHeight()/2;

        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLoc[0] + iv_cart.getWidth() / 5;
        float toY = endLoc[1] - parentLoc[1];

        //开始绘制贝塞尔曲线
        Path path = new Path();
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        mPathMeasure = new PathMeasure(path, false);

        //属性动画
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float)animation.getAnimatedValue();
                float[] mCurrentPosition = new float[2];
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                view.setTranslationX(mCurrentPosition[0]);
                view.setTranslationY(mCurrentPosition[1]);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 购物车的数量加1
//                mCount++;
//                mCountTv.setText(String.valueOf(mCount));
                // 把移动的图片imageview从父布局里移除
                mRootRl.removeView(view);

                //shopImg 开始一个放大动画
                Animation scaleAnim = AnimationUtils.loadAnimation(GoodsListBaseActivity.this, R.anim.scale);
                iv_cart.startAnimation(scaleAnim);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }
}
