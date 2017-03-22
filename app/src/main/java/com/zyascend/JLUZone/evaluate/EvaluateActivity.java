package com.zyascend.JLUZone.evaluate;

import android.content.DialogInterface;
import android.graphics.Color;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.zyascend.JLUZone.R;
import com.zyascend.JLUZone.base.BaseActivity;
import com.zyascend.JLUZone.base.BaseReAdapter;
import com.zyascend.JLUZone.entity.EvaluateItem;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by Administrator on 2016/12/5.
 */

public class EvaluateActivity extends BaseActivity<EvaluateContract.View, EvaluatePresenter>
        implements EvaluateContract.View, BaseReAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private EvaluateAdapter adapter;
    private List<EvaluateItem> mList;
    private int mPosition;
    private boolean canEvaluate = true;


    @Override
    protected void doOnCreate() {
        Log.v("system", "doOnCreate: ");
        System.out.print('I'+'T');
        android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle("注意事项")
                .setMessage(getString(R.string.evaluate_alarm))
                .setCancelable(false)
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        swipeRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(true);
                                onRefresh();
                            }
                        });
                    }
                }).create();

        dialog.show();
    }

    @Override
    protected void initView() {
        swipeRefreshLayout.setColorSchemeColors(Color.RED,Color.GREEN);
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new EvaluateAdapter();
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recycler;
    }

    @Override
    protected EvaluatePresenter getPresenter() {
        return new EvaluatePresenter();
    }

    @Override
    protected void loadFragment() {

    }

    @Override
    public void onItemClick(int position) {
        if (!canEvaluate){
            Toast.makeText(this, "评价中，请稍候再试", Toast.LENGTH_SHORT).show();
            return;
        }
        this.mPosition = position;
        showEvaluateDialog();
    }

    private void showEvaluateDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("选择评价内容")
                .setSingleChoiceItems(R.array.evaluate_list, 0,null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        canEvaluate = false;
                        mPresenter.evaluate(mList.get(mPosition).getItemId(),which,"");
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    @Override
    public void onRefresh() {
        mPresenter.loadEvaluateList();
    }

    @Override
    public void onLoadList(List<EvaluateItem> list) {
        if (list == null || list.isEmpty())return;
        mList = list;
        adapter.setList(list);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onEvaluated() {
        adapter.updateItem(mPosition);
        canEvaluate = true;
    }

    @Override
    public void onFail() {
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this, "出了点问题", Toast.LENGTH_SHORT).show();
    }
}
