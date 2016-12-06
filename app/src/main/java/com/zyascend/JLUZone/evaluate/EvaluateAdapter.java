package com.zyascend.JLUZone.evaluate;

import android.graphics.PointF;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zyascend.JLUZone.R;
import com.zyascend.JLUZone.base.BaseReAdapter;
import com.zyascend.JLUZone.entity.EvaluateItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by Administrator on 2016/12/5.
 */

public class EvaluateAdapter extends BaseReAdapter {
    private static final String TAG = "EvaluateAdapter";
    private List<EvaluateItem> mList = new ArrayList<>();
    private boolean canClick = true;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evaluate, parent, false);
        return new EvaluateHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final EvaluateHolder viewHolder = (EvaluateHolder) holder;
        if (mList == null || mList.isEmpty())return;

        EvaluateItem item = mList.get(position);
        viewHolder.className.setText(item.getClassName());
        viewHolder.teacherName.setText(item.getTeacher());

        if (item.isEvaluated()){
            viewHolder.btnEvaluate.setText("已评价");
            viewHolder.btnEvaluate.setClickable(false);
        }

        viewHolder.btnEvaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (canClick){
                    viewHolder.btnEvaluate.setText("评价中");
                    canClick = false;
                    if (mOnItemClickListener != null){
                        mOnItemClickListener.onItemClick(viewHolder.getAdapterPosition());
                    }
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(List<EvaluateItem> list) {
        mList.clear();
        mList = list;
        notifyDataSetChanged();
    }

    public void updateItem(int position){
        //http://blog.csdn.net/jdsjlzx/article/details/50445424
        Log.d(TAG, "updateItem: ");
        EvaluateItem item = mList.get(position);
        item.setEvaluated(true);
        mList.remove(position);
        mList.add(position,item);
        canClick = true;
        notifyItemChanged(position);
    }

    class EvaluateHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.className)
        TextView className;
        @Bind(R.id.teacherName)
        TextView teacherName;
        @Bind(R.id.btn_evaluate)
        Button btnEvaluate;
        public EvaluateHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
