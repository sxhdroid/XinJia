package com.hnxjgou.xinjia.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 通用RecyclerAdapter，支持多布局
 */

public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    protected WeakReference<Context> mContext;
    protected LayoutInflater mInflater;
    //数据
    protected List<T> mData;
    // 布局
    private int mLayoutId;

    // 多布局支持
    private MultiTypeSupport mMultiTypeSupport;

    protected int selectPotision = -1;

    public CommonRecyclerAdapter(Context context, List<T> data, int layoutId) {
        mContext = new WeakReference<>(context);
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mLayoutId = layoutId;
    }

    /**
     * 多布局支持
     */
    public CommonRecyclerAdapter(Context context, List<T> data, MultiTypeSupport<T> multiTypeSupport) {
        this(context, data, -1);
        this.mMultiTypeSupport = multiTypeSupport;
    }

    /**
     * 根据当前位置获取不同的viewType
     */
    @Override
    public int getItemViewType(int position) {
        // 多布局支持
        if (mMultiTypeSupport != null) {
            return mMultiTypeSupport.getLayoutId(position);
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 多布局支持
        if (mMultiTypeSupport != null) {
            mLayoutId = viewType;
        }
        // 先inflate数据
        View itemView = mInflater.inflate(mLayoutId, parent, false);
        // 返回ViewHolder
        ViewHolder holder = new ViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        // 设置点击和长按事件
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectPotision = position;
                    mItemClickListener.onItemClick(holder, position);
                    notifyDataSetChanged();
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mLongClickListener.onLongClick(holder, position);
                }
            });
        }

        convert((ViewHolder) holder, position, mData.get(position));
    }

    /**
     * 利用抽象方法回传出去，每个不一样的Adapter去设置
     *
     * @param item 当前的数据
     */
    public abstract void convert(ViewHolder holder, int position, T item);

    /**
     * 获取指定位置的数据
     * @param position
     * @return
     */
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setSelectPotision(int selectPotision) {
        this.selectPotision = selectPotision;
        notifyDataSetChanged();
    }

    public int getSelectPotision() {
        return selectPotision;
    }

    /***************
     * 设置条目点击和长按事件
     *********************/
    protected OnItemClickListener mItemClickListener;
    protected OnLongClickListener mLongClickListener;
    // 实现为二级列表时使用
    protected OnExpandableItemClick onExpandableItemClick;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;
    }

    /**实现为二级列表点击事件*/
    public void setOnExpandableItemClick(OnExpandableItemClick onExpandableItemClick) {
        this.onExpandableItemClick = onExpandableItemClick;
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder holder, int position);
    }

    public interface OnLongClickListener {
        boolean onLongClick(RecyclerView.ViewHolder holder, int position);
    }

    public interface OnExpandableItemClick {
        void onGroupClick(RecyclerView.ViewHolder holder, int position);
        void onChildrenClick(RecyclerView.ViewHolder holder, int position);
    }

}
