package com.zyascend.JLUZone.news;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zyascend.JLUZone.R;
import com.zyascend.JLUZone.base.BaseReAdapter;
import com.zyascend.JLUZone.entity.News;
import com.zyascend.JLUZone.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by Administrator on 2016/8/4.
 */
public class NewsAdapter extends BaseReAdapter {

    private List<News> mList = new ArrayList<>();

    public void setList(List<News> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final NewsViewHolder viewHolder = (NewsViewHolder) holder;
        if (ActivityUtils.NotNullOrEmpty(mList)){
            News news = mList.get(position);
            viewHolder.tvTitle.setText(news.getTitle());
            viewHolder.tvEditor.setText(news.getEditor());
            viewHolder.tvDate.setText(news.getDate());
        }

        if (null != mOnItemClickListener){
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
        return ActivityUtils.NotNullOrEmpty(mList)? mList.size() : 0 ;
    }


    class NewsViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_title)
        TextView tvTitle;

        @Bind(R.id.tv_editor)
        TextView tvEditor;
        @Bind(R.id.tv_date)
        TextView tvDate;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }
}
