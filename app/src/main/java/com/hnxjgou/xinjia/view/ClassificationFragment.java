package com.hnxjgou.xinjia.view;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnxjgou.xinjia.ApiConfig;
import com.hnxjgou.xinjia.R;
import com.hnxjgou.xinjia.model.entity.Business;
import com.hnxjgou.xinjia.model.entity.Category;
import com.hnxjgou.xinjia.utils.GsonUtil;
import com.hnxjgou.xinjia.utils.LogUtil;
import com.hnxjgou.xinjia.view.adapter.CommonRecyclerAdapter;
import com.hnxjgou.xinjia.view.adapter.MultiTypeSupport;
import com.hnxjgou.xinjia.view.adapter.ViewHolder;
import com.hnxjgou.xinjia.view.base.LazyLoadFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家分类页
 */
public class ClassificationFragment extends LazyLoadFragment<String> {

    private RecyclerView rv_left; // 左侧类型列表
    private RecyclerView rv_right; // 右侧指定类别下的数据列表

    private LinearLayout ly_load_error; // 数据加载失败时显示

    private Button btn_reload; // 重新加载数据按钮

    private List<Category> categories; // 左侧类别实体集合
    private List<Category> childCategories; // 右侧类别实体集合

    private CategoryAdapter categoryAdapter; // 左侧类别适配器对象
    private ChildCategoryAdapter childCategoryAdapter; // 右侧类别适配器

    public ClassificationFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categories = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), categories, R.layout.item_category);
        categoryAdapter.setSelectPotision(0); // 默认第一项为选中状态

        childCategories = new ArrayList<>();
        childCategoryAdapter = new ChildCategoryAdapter(getContext(), childCategories, new MultiTypeSupport<Category>() {
            @Override
            public int getLayoutId(int position) {
                return childCategoryAdapter.getViewType(position) == childCategoryAdapter.viewTypeGroup
                        ? R.layout.item_category_lable : R.layout.item_category;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_classification, container, false);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

        ly_load_error = view.findViewById(R.id.ly_load_error);
        ly_load_error.setVisibility(categories.isEmpty() ? View.VISIBLE : View.GONE);

        btn_reload = view.findViewById(R.id.btn_reload);

        rv_left = view.findViewById(R.id.rv_left);
        rv_right = view.findViewById(R.id.rv_right);

        categoryAdapter.setSelectPotision(categoryAdapter.getSelectPotision());//设置选中状态
        // 左侧类别列表项点击事件
        categoryAdapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                // 左侧点击后同步显示右侧数据
                childCategories.clear();
                childCategories.addAll(categoryAdapter.getItem(categoryAdapter.getSelectPotision()).NodeList);
                childCategoryAdapter.notifyChanged();
            }
        });
        rv_left.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_left.setAdapter(categoryAdapter);

        childCategoryAdapter.setOnExpandableItemClick(new CommonRecyclerAdapter.OnExpandableItemClick() {
            @Override
            public void onGroupClick(RecyclerView.ViewHolder holder, int position) {
                // 右侧类别点击事件
                LogUtil.e(TAG, "点击的是类别 : " + childCategoryAdapter.getGroupItem(position).CategoryName);
            }

            @Override
            public void onChildrenClick(RecyclerView.ViewHolder holder, int position) {
                // 右侧商家点击事件
                LogUtil.e(TAG, "点击的是商家 ： " + childCategoryAdapter.getChildItem(position).BusinessName);
            }
        });
        rv_right.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_right.setAdapter(childCategoryAdapter);

        //重新加载数据按钮事件
        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 如果类别列表为空，那么重新加载
                requestData();
            }
        });
    }

    @Override
    public void requestData() {
        //请求类别
        doPost(ApiConfig.category.build_url(ApiConfig.category.API_CATEGORY), R.id.rv_left, null);
    }

    @Override
    public void showErr(String err) {
        // 不管是请求所有类别出错还是请求指定的类别详情出错，都显示请求出错的界面
        ly_load_error.setVisibility(View.VISIBLE);
    }

    @Override
    public void showData(String data, Object tag) {
        if ((int)tag == R.id.rv_left) {
            // 请求类别返回
            ly_load_error.setVisibility(View.GONE);
            categories.addAll(GsonUtil.<Category>jsonArray2List(data, "Data", Category.class));
            categoryAdapter.notifyDataSetChanged();
            // 左侧类别加载完成后，默认显示第一个类别的子类数据
            childCategories.addAll(categoryAdapter.getItem(categoryAdapter.getSelectPotision()).NodeList);
            childCategoryAdapter.notifyDataSetChanged();
        }
    }

    // 左侧类别列表适配器
    private class CategoryAdapter extends CommonRecyclerAdapter<Category> {

        public CategoryAdapter(Context context, List<Category> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void convert(ViewHolder holder, int position, Category item) {
            // 正式的绑定数据
            CheckedTextView view = holder.getView(R.id.tv_category);
            view.setText(item.CategoryName);
            view.setChecked(selectPotision == position);
            holder.itemView.setBackgroundResource(view.isChecked() ? R.drawable.item_category_checked : R.drawable.item_category_normal);
        }
    }

    // 右侧类别列表适配器
    private class ChildCategoryAdapter extends CommonRecyclerAdapter<Category> {

        public final int viewTypeGroup = 1; // item的viewType为1，显示子类标签
        public final int viewTypeChild = 2; // item的viewType为2，显示子类标签

        private Map<Integer, Category> groupItem = new HashMap<>();

        public ChildCategoryAdapter(Context context, List<Category> data, MultiTypeSupport<Category> multiTypeSupport) {
            super(context, data, multiTypeSupport);
        }

        public void notifyChanged(){
            groupItem.clear();
            notifyDataSetChanged();
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
        public int getItemCount() {
            int count = 0;
            for (int i = 0; i < mData.size(); i ++){
                groupItem.put(count, mData.get(i)); // 记录类标签的位置，便于实现二级列表的效果
                // 统计每个类别下商家的个数
                count += getChildItemCount(i) + 1; // 如果有商家，那么总项数为商家数+类别数
            }
            return count;
        }

        // 根据位置获取group
        private Category getGroupItem(int position) {
            return groupItem.get(position);
        }

        // 根据位置获取children
        private Business getChildItem(int position) {
            int group = -1;
            for (int key : groupItem.keySet()) {
                // 遍历找到子类的父类信息
                if (position > key) {
                    group = key;
                    break;
                }
            }
            if (group == -1) return null;
            return getGroupItem(group).Business.get(position - group - 1);
        }

        private int getChildItemCount(int groupPosition) {
            if (mData.get(groupPosition).Business == null) {
                return 0;
            }
            return mData.get(groupPosition).Business.size();
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
        public void convert(ViewHolder holder, int position, Category item) {
            TextView textView = holder.getView(R.id.tv_category);
            // 正式的绑定数据
            if (getViewType(position) == viewTypeGroup) { // 类别item
                textView.setText(getGroupItem(position).CategoryName);
            }else {
                // 商家item
                Business business = getChildItem(position);
                textView.setText(business.BusinessName);
            }
        }
    }


}
