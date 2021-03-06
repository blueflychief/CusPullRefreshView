package com.ybao.pullrefreshview.recycler;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public abstract class RefreshAdapter<VH extends BaseRecyclerViewHolder, T> extends RecyclerView.Adapter
        implements BaseRecyclerViewHolder.ItemClickListener,
        BaseRecyclerViewHolder.ItemLongClickListener {
    private RefreshRecyclerView mRecycleView;
    protected OnRecyclerItemClickListener<T> mOnRecyclerViewItemClickListener;
    protected OnRecyclerItemLongClickListener<T> mOnRecyclerItemLongClickListener;

    private final int TYPE_CONTENT = -1; // 内容类型
    private final int TYPE_FOOTER = -2; // 底部加载更多
    private final int TYPE_HEADER = -3; // 头部
    private final int TYPE_NULL = -4; // 数据为空
    private List<Integer> mViewTypes; // 子视图类型
    protected List<T> mDataContainer;  //数据容器
    private int mPageSize = 20;  //默认分页大小
    private boolean isDataNull = false;  //数据为空


    public RefreshAdapter(List<T> list, RefreshRecyclerView refreshView) {
        this(list, refreshView, 20);
    }

    /**
     * @param list        不能为空！！！
     * @param refreshView RefreshRecycleView
     * @param pageSize    分页大小，默认12
     */
    public RefreshAdapter(List<T> list, RefreshRecyclerView refreshView, int pageSize) {
        this.mRecycleView = refreshView;
        mDataContainer = list;
        mPageSize = pageSize;
        mViewTypes = new ArrayList<>();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CONTENT || mViewTypes.contains(viewType)) {
            VH vh = onCreateHolder(parent, viewType);
            vh.setItemClickListener(this);
            vh.setItemLongClickListener(this);
            return vh;
        } else if (viewType == TYPE_NULL) {
            mRecycleView.setFullSpan(mRecycleView.getNullPage());
            return new BaseRecyclerViewHolder(mRecycleView.getNullPage());
        } else if (viewType == TYPE_HEADER && mRecycleView.isHeaderVisible()) {
            mRecycleView.setFullSpan(mRecycleView.getHeader());
            return new BaseRecyclerViewHolder(mRecycleView.getHeader());
        } else if (viewType == TYPE_FOOTER && mRecycleView.isFooterVisible()) {
            mRecycleView.setFullSpan(mRecycleView.getFooter());
            return new BaseRecyclerViewHolder(mRecycleView.getFooter());
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemCount() > 0) {
            int viewType = this.getItemViewType(position);
            if (viewType == TYPE_CONTENT || mViewTypes.contains(viewType)) {
                int p = mRecycleView.isHeaderVisible() ? (holder.getLayoutPosition() - 1) : holder.getLayoutPosition();
                onBindHolder((VH) holder, p, mDataContainer.get(p));
            }
        }
    }

    /**
     * 没有特殊情况时子类请勿重写此方法！！！
     *
     * @return
     */
    @Override
    public int getItemCount() {
        int count = mDataContainer.size();
        if (mRecycleView.isHeaderVisible()) {
            count += 1;
        }
        if (mRecycleView.isFooterVisible()) {
            count += 1;
        }
        if (isDataNull) {
            count += 1;
        }
        return count;
    }

    /**
     * 没有特殊情况时子类请勿重写此方法！！！
     *
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mRecycleView.isHeaderVisible()) {
            return TYPE_HEADER;
        } else if ((position == (getItemCount() - 1)) && mRecycleView.isFooterVisible()) {
            return TYPE_FOOTER;
        } else if (isDataNull) {
            return TYPE_NULL;
        } else {
            return getItemType(mRecycleView.isHeaderVisible() ? (position - 1) : position);
        }
    }


    /**
     * 设置分页大小
     *
     * @param pageSize 默认12
     */
    public void setPageSize(int pageSize) {
        mPageSize = pageSize;
    }

    /**
     * 设置子视图类型, 如果有新的子视图类型, 直接往参数viewTypes中添加即可, 每个类型的值都要>3, 且不能重复
     *
     * @param viewTypes 子视图类型列表
     */
    public void setItemTypes(List<Integer> viewTypes) {
        if (viewTypes != null) {
            this.mViewTypes.addAll(viewTypes);
        }
    }

    /**
     * 自定义获取子视图类型的方法，如果有多种itemType，请重写此方法，在onBindHolder使用getItemType(int position)方法获得itemType
     * <p>
     * !!!!!!!!!!!!!!!!!!!!!请勿在子类中调用getItemViewType方法获取!!!
     */
    public int getItemType(int position) {
        return TYPE_CONTENT;
    }


    public void addData(List<T> newDataList) {
        addData(true, newDataList);
    }

    /**
     * 添加数据
     *
     * @param isRefresh   是否是刷新
     * @param newDataList 新数据
     */
    public void addData(boolean isRefresh, List<T> newDataList) {
        if (mDataContainer == null) {
            throw new NullPointerException("the mDataContainer not allowed null!!!");
        }
        if (mRecycleView.isRefreshing()) {
            mRecycleView.stopRefresh();
        }
        if (isRefresh) {
            mDataContainer.clear();
        }
        if (newDataList != null && newDataList.size() > 0) {
            mDataContainer.addAll(newDataList);
            isDataNull = false;
        } else {  //增加空白显示item
            isDataNull = true;
        }

        if (isRefresh) {
            notifyDataSetChanged();
        } else {
            notifyItemsInserted(newDataList);
        }
        if (mRecycleView.isLoadMoreEnable()) {
            mRecycleView.setFooterHasMore(newDataList != null && newDataList.size() == mPageSize);
        }
    }

    public T getItemData(int position) {
        return mDataContainer.get(position);
    }

    /**
     * 获取所有的数据
     *
     * @return
     */
    public List<T> getDataContainer() {
        return mDataContainer;
    }

    /**
     * 批量插入更新
     *
     * @param list
     */
    public void notifyItemsInserted(List list) {
        if (list == null) {
            return;
        }
        notifyItemRangeInserted(mDataContainer.size() - list.size() + (mRecycleView.isHeaderVisible() ? 2 : 1), list.size());
    }

    /**
     * Item的点击事件
     *
     * @param onRecyclerViewItemClickListener
     */
    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener<T> onRecyclerViewItemClickListener) {
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public void setOnRecyclerItemLongClickListener(OnRecyclerItemLongClickListener<T> onRecyclerItemLongClickListener) {
        mOnRecyclerItemLongClickListener = onRecyclerItemLongClickListener;
    }

    public abstract VH onCreateHolder(ViewGroup parent, int viewType);

    public abstract void onBindHolder(VH holder, int position, T data);

    public boolean isFooter(int position) {
        return TYPE_FOOTER == getItemViewType(position);
    }

    public boolean isHeader(int position) {
        return TYPE_HEADER == getItemViewType(position);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (null != mOnRecyclerViewItemClickListener) {
            int p = getDataPosition(position);
            mOnRecyclerViewItemClickListener.onRecyclerItemClick(p, view, mDataContainer.get(p));
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {
        if (null != mOnRecyclerItemLongClickListener) {
            int p = getDataPosition(position);
            mOnRecyclerItemLongClickListener.onRecyclerItemLongClick(p, view, mDataContainer.get(p));
        }
    }

    /*是否是数据项*/
    public boolean isDataItem(int layoutPosition) {
        if (mRecycleView.isHeaderVisible()) {
            if (layoutPosition == 0 || layoutPosition > mDataContainer.size()) {
                return false;
            }
        } else if (mRecycleView.isFooterVisible()) {
            if (layoutPosition >= mDataContainer.size()) {
                return false;
            }
        }
        return true;
    }


    /*获取data的position*/
    public int getDataPosition(int layoutPosition) {
        return mRecycleView.isHeaderVisible() ? (layoutPosition - 1) : layoutPosition;
    }
}


