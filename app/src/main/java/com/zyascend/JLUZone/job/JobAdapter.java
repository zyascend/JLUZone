package com.zyascend.JLUZone.job;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zyascend.JLUZone.R;
import com.zyascend.JLUZone.base.BaseReAdapter;
import com.zyascend.JLUZone.entity.Job;
import com.zyascend.JLUZone.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by Administrator on 2016/10/20.
 */

public class JobAdapter extends BaseReAdapter {


    private List<Job> mList = new ArrayList<>();

    public void setList(List<Job> List) {
        mList.addAll(List);
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final JobViewHolder viewHolder = (JobViewHolder) holder;
        if (ActivityUtils.NotNullOrEmpty(mList)) {
            Job job = mList.get(position);
            viewHolder.tvDate.setText(job.getDate().split(" ")[0]);
            viewHolder.tvHits.setText(String.valueOf(job.getHits()));
            if (TextUtils.equals(job.getType(), "3")){
                viewHolder.tvPlace.setVisibility(View.INVISIBLE);
            }else {
                viewHolder.tvPlace.setText(job.getAddress());
            }
            viewHolder.tvTitle.setText(job.getTitle());
        }

        if (null != mOnItemClickListener) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(viewHolder.getAdapterPosition());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return ActivityUtils.NotNullOrEmpty(mList) ? mList.size() : 0;
    }


    class JobViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_place)
        TextView tvPlace;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_hits)
        TextView tvHits;
        @Bind(R.id.tv_date)
        TextView tvDate;
        public JobViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
