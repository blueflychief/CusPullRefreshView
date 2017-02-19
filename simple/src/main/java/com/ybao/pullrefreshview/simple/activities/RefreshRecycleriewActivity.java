package com.ybao.pullrefreshview.simple.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ybao.pullrefreshview.layout.BaseFooterView;
import com.ybao.pullrefreshview.layout.BaseHeaderView;
import com.ybao.pullrefreshview.refreshview.BaseRecyclerViewHolder;
import com.ybao.pullrefreshview.refreshview.OnRecyclerItemClickListener;
import com.ybao.pullrefreshview.refreshview.RefreshAdapter;
import com.ybao.pullrefreshview.refreshview.RefreshRecyclerView;
import com.ybao.pullrefreshview.simple.R;
import com.ybao.pullrefreshview.layout.NormalFooterView;
import com.ybao.pullrefreshview.layout.NormalHeaderView;

import java.util.ArrayList;
import java.util.List;

public class RefreshRecycleriewActivity extends AppCompatActivity implements BaseHeaderView.OnRefreshListener, BaseFooterView.OnLoadListener {
    private static final String TAG = "RefreshRecycleriew";
    RefreshRecyclerView mRefreshRecyclerView;
    private MyRefreshAdapter mMyRefreshAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_recycleriew);
        mRefreshRecyclerView = (RefreshRecyclerView) findViewById(R.id.refresh_recycleriew);

//
//        NormalHeaderView refreshHeader = new NormalHeaderView(this);
//        refreshHeader.setOnRefreshListener(this);
//        NormalFooterView loadFooter = new NormalFooterView(this);
//        loadFooter.setOnLoadListener(this);
//        mRefreshRecyclerView.setPullHeader(refreshHeader);
//        mRefreshRecyclerView.setPullFooter(loadFooter);

        mRefreshRecyclerView.setDefaultFooter();
        mRefreshRecyclerView.setDefaultHeader();
        mRefreshRecyclerView.setOnLoadListener(this);
        mRefreshRecyclerView.setOnRefreshListener(this);
        mMyRefreshAdapter = new MyRefreshAdapter(getData(12), mRefreshRecyclerView);
        mRefreshRecyclerView.setAdapter(mMyRefreshAdapter);
        LinearLayout linearLayout = initViews();
        mRefreshRecyclerView.setHeader(linearLayout);
        mRefreshRecyclerView.setFooter(linearLayout);
        mMyRefreshAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onRecyclerItemClick(int dataPosition, View view, Object data) {
                Toast.makeText(RefreshRecycleriewActivity.this, dataPosition + "位置的：" + view.getId() + "被点击了", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    private LinearLayout initViews() {
        TextView textView = new TextView(this);
        textView.setText("Hello World");
        textView.setTextSize(20);
        textView.setId(1000);
        textView.setPadding(20, 20, 20, 20);
        textView.setClickable(true);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RefreshRecycleriewActivity.this, "textView1被点击了", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView2 = new TextView(this);
        textView2.setText("Hello World");
        textView2.setTextSize(20);
        textView2.setId(2000);
        textView2.setPadding(20, 20, 20, 20);
        textView2.setClickable(true);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RefreshRecycleriewActivity.this, "textView2被点击了", Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(textView);
        linearLayout.addView(textView2);
        return linearLayout;
    }

    @Override
    public void onRefresh(final BaseHeaderView baseHeaderView) {
        Log.i(TAG, "onRefresh: ");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                baseHeaderView.stopRefresh();
                mMyRefreshAdapter.addData(true, null);
            }
        }, 2000);
    }

    @Override
    public void onLoad(final BaseFooterView baseFooterView) {
        Log.i(TAG, "onLoad: ");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                baseFooterView.stopLoad();
                mMyRefreshAdapter.addData(true, getData(16));
            }
        }, 2000);
    }


    class MyRefreshAdapter extends RefreshAdapter {

        public MyRefreshAdapter(List list, RefreshRecyclerView refreshView) {
            super(list, refreshView);
        }

        @Override
        public BaseRecyclerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
            return new BaseRecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindHolder(BaseRecyclerViewHolder holder, int position, Object data) {
            ((TextView) holder.itemView).setText((String) data);
        }
    }


    int page = 1;

    private List<String> getData(int n) {
        List<String> datas = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            datas.add("第" + page + "页,第" + i + "条");
        }
        return datas;
    }
}
