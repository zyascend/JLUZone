package com.zyascend.JLUZone.map;

import android.content.Context;
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
 * Created by Administrator on 2017/3/21.
 */
public class DestinationAdapter extends BaseReAdapter {


    private final Context mContext;
    private int[] pics = {R.drawable.map_nanling, R.drawable.map_nanqu, R.drawable.map_nanhu,
            R.drawable.map_heping, R.drawable.map_xinming, R.drawable.map_chaoyang,};

    public DestinationAdapter(Context context){
        mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_destination, parent, false);
        return new DesHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DesHolder desHolder = (DesHolder) holder;
        desHolder.pic.setImageResource(pics[position]);
        desHolder.title.setText(mContext.getResources().getTextArray(R.array.map_list)[position]);
        desHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(desHolder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return pics.length;
    }

    class DesHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.pic)
        ImageView pic;
        @Bind(R.id.title)
        TextView title;
        public DesHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
        }
    }
}
