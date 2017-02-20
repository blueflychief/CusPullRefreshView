package com.ybao.pullrefreshview.recycler;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.ybao.pullrefreshview.R;
import com.ybao.pullrefreshview.layout.PullRefreshLayout;

/**
 * Created by lsq on 2/13/2017.
 */

public class RefreshRecyclerView extends PullRefreshLayout {
    private Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private View mHeader;             // 头部
    private View mFooter;             // 脚部
    private View mNullPage;           // 无数据页

    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initRecycler(context);
    }

    private void initRecycler(Context context) {
        mRecyclerView = new RecyclerView(context);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        addView(mRecyclerView, -1, new LayoutParams(-1, -1));
        setNullPage(R.layout.view_null_data);
    }

    public boolean isLoadMoreEnable() {
        return false;
    }

    public View getHeader() {
        return mHeader;
    }

    public void setNullPage(int resId) {
        mNullPage = LayoutInflater.from(mContext).inflate(resId, this, false);
    }

    public View getNullPage() {
        return mNullPage;
    }

    public View getFooter() {
        return mFooter;
    }

    public void setHeader(View header) {
        mHeader = header;
    }

    public void setFooter(View footer) {
        mFooter = footer;
    }


    // TODO: 2/18/2017 这个需要重新考虑
    public void setFooterHasMore(boolean hasMore) {

    }

    /*获取RecyclerView*/
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /*获取RecyclerView的Adapter*/
    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    /*设置RecyclerView的Adapter*/
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException("adapter is null!!!");
        }
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
    }

    /*获取RecyclerView的LayoutManager*/
    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    /*设置RecyclerView的LayoutManager*/
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mLayoutManager = layoutManager;
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    /*header是否存在或者是否显示了*/
    public boolean isHeaderVisible() {
        return (getHeader() != null) && (getHeader().getVisibility() == View.VISIBLE);
    }

    /*footer是否存在或者是否显示了*/
    public boolean isFooterVisible() {
        return getFooter() != null && getFooter().getVisibility() == View.VISIBLE;
    }

    /**
     * 设置子视图充满一行
     *
     * @param view 子视图
     */
    public void setFullSpan(View view) {
        RecyclerView.LayoutManager manager = getLayoutManager();
        if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(
                    StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT, -2);
            params.setFullSpan(true);
            view.setLayoutParams(params);
        }

    }
}
