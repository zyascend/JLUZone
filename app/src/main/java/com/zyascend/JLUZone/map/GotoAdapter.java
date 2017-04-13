package com.zyascend.JLUZone.map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zyascend.JLUZone.R;
import com.zyascend.JLUZone.base.BaseReAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by Administrator on 2016/12/5.
 */

public class GotoAdapter extends BaseReAdapter {


    private Context mContext;
    private int[] drawables = new int[]{R.drawable.ic_teach_building
            ,R.drawable.ic_apartment,R.drawable.ic_gym,R.drawable.ic_dinninghall
            ,R.drawable.ic_labrary,R.drawable.ic_work_building,R.drawable.ic_bank};
    private String[] texts;

    public GotoAdapter(Context context) {
        this.mContext = context;
        texts = context.getResources().getStringArray(R.array.goto_list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goto, parent, false);
        return new GoToHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final GoToHolder viewHolder = (GoToHolder) holder;
        viewHolder.imageView.setImageResource(drawables[position]);
        viewHolder.text.setText(texts[position]);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(viewHolder.getAdapterPosition());
                }
            }
        });
    }



    private Drawable getDrawable(int position) {
        return ContextCompat.getDrawable(mContext,drawables[position]);
    }

    @Override
    public int getItemCount() {
        return drawables.length;
    }


    class GoToHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.text)
        TextView text;
        @Bind(R.id.image)
        ImageView imageView;

        public GoToHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
