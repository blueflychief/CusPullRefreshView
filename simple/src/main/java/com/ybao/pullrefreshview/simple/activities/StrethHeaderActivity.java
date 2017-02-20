package com.ybao.pullrefreshview.simple.activities;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ybao.pullrefreshview.extras.NormalFooterView;
import com.ybao.pullrefreshview.layout.FlingLayout;
import com.ybao.pullrefreshview.layout.StretchHeaderLayout;
import com.ybao.pullrefreshview.simple.R;

import java.util.ArrayList;
import java.util.List;

public class StrethHeaderActivity extends AppCompatActivity implements AbsListView.OnScrollListener, FlingLayout.OnScrollListener {
    private StretchHeaderLayout shStretchHeader;
    private View mHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streth_header);
        shStretchHeader = (StretchHeaderLayout) findViewById(R.id.shStretchHeader);
        ListView listView = new ListView(this);
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.view_streth_header, shStretchHeader, false);
        shStretchHeader.addStretchHeaderView(mHeaderView);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item, getData(12));
        listView.setAdapter(adapter);
        shStretchHeader.addPullView(listView);
        listView.setOnScrollListener(this);
        shStretchHeader.getPullRefreshLayout().setOnScrollListener(this);
        shStretchHeader.setPullFooter(new NormalFooterView(this));
    }


    int page = 1;

    private List<String> getData(int n) {
        List<String> datas = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            datas.add("第" + page + "页,第" + i + "条");
        }
        return datas;
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        onMove();
    }

    private void onMove() {
        int oy = (int) shStretchHeader.getPullRefreshLayout().getMoveY() - (shStretchHeader.getScrollGeter() != null ? shStretchHeader.getScrollGeter().getScrollY() : 0);
        if (oy > 0) {
            int imgHeaderHeigth = mHeaderView.getMeasuredHeight();
            float ph = 1 + (float) oy / (float) imgHeaderHeigth;
            ViewCompat.setPivotY(mHeaderView, 0);
            ViewCompat.setScaleX(mHeaderView, ph);
            ViewCompat.setScaleY(mHeaderView, ph);

        } else {
            ViewCompat.setScaleX(mHeaderView, 1);
            ViewCompat.setScaleY(mHeaderView, 1);
            ViewCompat.setTranslationY(mHeaderView, oy);
        }
    }

    @Override
    public void onScroll(FlingLayout flingLayout, float y) {
        onMove();
    }

    @Override
    public void onScrollChange(FlingLayout flingLayout, int state) {

    }
}
