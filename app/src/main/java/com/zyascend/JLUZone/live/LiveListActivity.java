package com.zyascend.JLUZone.live;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.zyascend.JLUZone.R;
import com.zyascend.JLUZone.base.BaseActivity;
import com.zyascend.JLUZone.base.BaseReAdapter;
import com.zyascend.JLUZone.entity.LiveChannel;
import com.zyascend.JLUZone.utils.CacheCleanUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/22.
 */

public class LiveListActivity extends BaseActivity<LiveContract.View, LivePresenter>
        implements LiveContract.View, SwipeRefreshLayout.OnRefreshListener, BaseReAdapter.OnItemClickListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private LiveAdapter adapter;
    private List<LiveChannel> mList = new ArrayList<>();

    @Override
    protected void doOnCreate() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });
    }

    @Override
    protected void initView() {

        setToolbarTitle("IPTV");
        adapter = new LiveAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter.setOnItemClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recycler;
    }

    @Override
    protected LivePresenter getPresenter() {
        return new LivePresenter();
    }

    @Override
    protected void loadFragment() {

    }

    @Override
    public void onGetChannelList(List<LiveChannel> channels) {
        if (channels != null && !channels.isEmpty()){
            mList = channels;
            adapter.clear();
            adapter.addAll(channels);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFail() {
        Toast.makeText(this, "加载失败", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onRefresh() {
        mPresenter.fetchChannel();
    }

    @Override
    public void onItemClick(int position) {
        // TODO: 2017/3/22
    }
}
