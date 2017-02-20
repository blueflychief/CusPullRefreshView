package com.ybao.pullrefreshview.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.ybao.pullrefreshview.support.utils.ViewScrollUtil;

/**
 * Created by lsq on 2/20/2017.
 */

public class StretchHeaderLayout extends FrameLayout {
    private final String TAG = "StretchHeaderLayout";
    private PullRefreshLayout mPullRefreshLayout;
    private View mStretchHeaderView;
    private BaseFooterView mFooterView;
    private View mPullView;
    private ViewScrollUtil.ScrollGeter scrollGeter;


    public StretchHeaderLayout(Context context) {
        this(context, null);
    }

    public StretchHeaderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StretchHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        mPullRefreshLayout = new PullRefreshLayout(context);
        addView(mPullRefreshLayout, -1, new LayoutParams(-1, -1));
    }

    /*添加弹性头*/
    public void addStretchHeaderView(View view) {
        if (view != null) {
            mStretchHeaderView = view;
            addView(mStretchHeaderView, -1, new LayoutParams(-1, -2));
            post(new Runnable() {

                @Override
                public void run() {
                    int height = mStretchHeaderView.getMeasuredHeight();
                    Log.i(TAG, "height: " + height);
                    mPullView.setPadding(getPaddingLeft(), getPaddingTop() + height, getPaddingRight(), getPaddingBottom());
                    mPullRefreshLayout.addView(mPullView, -1, new LayoutParams(-1, -1));
                    if (mPullView instanceof ViewGroup) {
                        ((ListView) mPullView).setClipToPadding(false);
                        scrollGeter = ViewScrollUtil.getScrollGeter(mPullView);
                    }
                }
            });
        }
    }

    /*添加PullView*/
    public void addPullView(View view) {
        if (view != null) {
            mPullView = view;
        }
    }

    /*添加Footer*/
    public void setPullFooter(BaseFooterView baseFooterView) {
        if (baseFooterView != null) {
            mFooterView = baseFooterView;
            mPullRefreshLayout.setPullFooter(mFooterView);
        }
    }

    public ViewScrollUtil.ScrollGeter getScrollGeter() {
        return scrollGeter;
    }

    public PullRefreshLayout getPullRefreshLayout() {
        return mPullRefreshLayout;
    }
}
