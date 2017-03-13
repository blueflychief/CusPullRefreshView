/**
 * Copyright 2015 Pengyuan-Jiang
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Author：Ybao on 2015/11/5 ‏‎17:53
 * <p>
 * QQ: 392579823
 * <p>
 * Email：392579823@qq.com
 */
package com.ybao.pullrefreshview.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.ybao.pullrefreshview.extras.NormalFooterView;
import com.ybao.pullrefreshview.extras.NormalHeaderView;
import com.ybao.pullrefreshview.iable.Loadable;
import com.ybao.pullrefreshview.iable.Refreshable;


/**
 * 经典下拉刷新，上拉加载的通用控件（可用于任意控件 如 ListView GridView WebView ScrollView）
 * <p>
 * 弹性下（上）拉，滑倒顶（低）部无需松开即可继续拉动
 */
public class PullRefreshLayout extends FlingLayout {
    public final static int LAYOUT_NORMAL = 0x00;
    public final static int LAYOUT_DRAWER = 0x01;
    public final static int LAYOUT_SCROLLER = 0x10;

    private Context mContext;
    private BaseHeaderView mRefreshHeader;
    private BaseFooterView mLoadFooter;

    public PullRefreshLayout(Context context) {
        this(context, null);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    @Override
    protected boolean onScroll(float y) {
        if (mRefreshHeader != null && y >= 0) {
            boolean intercept = mRefreshHeader.onScroll(y);
            if (y != 0) {
                return intercept;
            }
        }
        if (mLoadFooter != null && y <= 0) {
            boolean intercept = mLoadFooter.onScroll(y);
            if (y != 0) {
                return intercept;
            }
        }
        return false;
    }

    @Override
    protected void onScrollChange(int stateType) {
        if (mRefreshHeader != null) {
            mRefreshHeader.onScrollChange(stateType);
            if (stateType == BaseHeaderView.REFRESHING) {
                if (mLoadFooter != null) {
                    // TODO: 2/19/2017  禁止上拉
                }
            }
        }
        if (mLoadFooter != null) {
            mLoadFooter.onScrollChange(stateType);
            if (stateType == BaseFooterView.LOADING) {
                if (mRefreshHeader != null) {
                    // TODO: 2/19/2017  禁止下拉
                }
            }
        }

    }

    @Override
    protected boolean onStartFling(float nowY) {
        if (mRefreshHeader != null && nowY > 0) {
            return mRefreshHeader.onStartFling(nowY);
        } else if (mLoadFooter != null && nowY < 0) {
            return mLoadFooter.onStartFling(nowY);
        }
        return false;
    }

    /*设置默认的下拉刷新头*/
    public void setDefaultHeader() {
        setPullHeader(new NormalHeaderView(mContext));
    }

    /*设置默认的上拉加载Footer*/
    public void setDefaultFooter() {
        setPullFooter(new NormalFooterView(mContext));
    }

    /*设置下拉刷新的监听*/
    public void setOnRefreshListener(BaseHeaderView.OnRefreshListener onRefreshListener) {
        if (onRefreshListener != null && mRefreshHeader != null) {
            mRefreshHeader.setOnRefreshListener(onRefreshListener);
        }
    }

    /*设置上拉加载更多的监听*/
    public void setOnLoadListener(BaseFooterView.OnLoadListener onLoadListener) {
        if (onLoadListener != null && mLoadFooter != null) {
            mLoadFooter.setOnLoadListener(onLoadListener);
        }
    }

    /*设置下拉刷新的头部*/
    public void setPullHeader(BaseHeaderView pullHeader) {
        if (pullHeader != null) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                if (getChildAt(i) instanceof Refreshable) {  //移除已有的刷新Header
                    removeViewAt(i);
                    break;
                }
            }
            mRefreshHeader = pullHeader;
            addView(mRefreshHeader, -1, new LayoutParams(-1, -2));
        }
    }

    /*设置上拉加载的底部*/
    public void setPullFooter(BaseFooterView loadFooter) {
        if (loadFooter != null) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                if (getChildAt(i) instanceof Loadable) {  //移除已有的加载Footer
                    removeViewAt(i);
                    break;
                }
            }
            mLoadFooter = loadFooter;
            addView(mLoadFooter, -1, new LayoutParams(-1, -2));
        }
    }

    /*是否正在刷新*/
    public boolean isRefreshing() {
        return mRefreshHeader != null && mRefreshHeader.isRefreshing();
    }

    /*是否正在加载*/
    public boolean isLoading() {
        return mLoadFooter != null && mLoadFooter.isLoading();
    }

    /*开始下拉刷新*/
    public void startRefresh() {
        if (mRefreshHeader != null) {
            mRefreshHeader.startRefresh();
        }
    }

    /*停止下拉刷新*/
    public void stopRefresh() {
        if (mRefreshHeader != null) {
            mRefreshHeader.stopRefresh();
        }
    }

    /*开始加载更多*/
    public void startLoad() {
        if (mLoadFooter != null) {
            mLoadFooter.startLoad();
        }
    }

    /*停止加载更多*/
    public void stopLoad() {
        if (mLoadFooter != null) {
            mLoadFooter.stopLoad();
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof BaseHeaderView) {
            mRefreshHeader = (BaseHeaderView) child;
            mRefreshHeader.setPullRefreshLayout(this);
        } else if (child instanceof BaseFooterView) {
            mLoadFooter = (BaseFooterView) child;
            mLoadFooter.setPullRefreshLayout(this);
        }
        super.addView(child, index, params);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int height = getHeight();
        if (mRefreshHeader != null) {
            View mHeaderView = mRefreshHeader;
            mHeaderView.layout(mHeaderView.getLeft(), -mHeaderView.getMeasuredHeight(), mHeaderView.getRight(), 0);
        }
        if (mLoadFooter != null) {
            View mFooterView = mLoadFooter;
            mFooterView.layout(mFooterView.getLeft(), height, mFooterView.getRight(), height + mFooterView.getMeasuredHeight());
        }
    }
}