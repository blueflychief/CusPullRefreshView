package com.ybao.pullrefreshview.refreshview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.ybao.pullrefreshview.layout.PullRefreshLayout;
import com.ybao.pullrefreshview.support.impl.Loadable;
import com.ybao.pullrefreshview.support.impl.Refreshable;

/**
 * Created by lsq on 2/13/2017.
 */

public class RefreshRecyclerView extends PullRefreshLayout {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Refreshable mRefreshHeader;
    private Loadable mLoadFooter;
    private View mHeader;             // 头部和脚部
    private View mFooter;             // 头部和脚部

    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUp(context);
    }

    private void setUp(Context context) {
        mRecyclerView = new RecyclerView(context);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        addView(mRecyclerView, -1, new LayoutParams(-1, -1));
    }

    public void setPullHeader(Refreshable pullHeader) {
        if (pullHeader != null) {
            mRefreshHeader = pullHeader;
            addView((View) mRefreshHeader, -1, new LayoutParams(-1, -2));
        }
    }

    public void setPullFooter(Loadable loadFooter) {
        if (loadFooter != null) {
            mLoadFooter = loadFooter;
            addView((View) mLoadFooter, -1, new LayoutParams(-1, -2));
        }
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException("adapter is null!!!");
        }
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mLayoutManager = layoutManager;
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    /**
     * 设置子视图充满一行
     *
     * @param view 子视图
     */
    public void setFullSpan(View view) {
        RecyclerView.LayoutManager manager = getLayoutManager();
        // 根据布局设置参数, 使"加载更多"的布局充满一行
        if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(
                    StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT, StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT);
            params.setFullSpan(true);
            view.setLayoutParams(params);
        }
    }

    public void setRefreshing(boolean refreshing) {

    }

    public boolean isRefreshing() {
        if (getRefreshHeader()!=null) {
            getRefreshHeader().stopRefresh();
        }
        return false;
    }

    public boolean isLoading() {
        return false;
    }

    public boolean isLoadMoreEnable() {
        return mLoadFooter != null;
    }

    public View getHeader() {
        return mHeader;
    }

    public View getFooter() {
        return mFooter;
    }

    public void setHeader(View header) {
        mHeader = header;
    }

    public Refreshable getRefreshHeader() {
        return mRefreshHeader;
    }

    public Loadable getLoadFooter() {
        return mLoadFooter;
    }

    public void setFooter(View footer) {
        mFooter = footer;
    }

    public void setFooterHasMore(boolean hasMore) {

    }
}
