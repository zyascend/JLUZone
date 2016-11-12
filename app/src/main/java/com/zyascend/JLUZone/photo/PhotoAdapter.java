package com.zyascend.JLUZone.photo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zyascend.JLUZone.R;
import com.zyascend.JLUZone.base.BaseReAdapter;
import com.zyascend.JLUZone.entity.MyPhoto;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 *
 * Created by Administrator on 2016/8/6.
 */
public class PhotoAdapter extends BaseReAdapter {

    private List<MyPhoto> myPhotoList;

    public void setMyPhotoList(List<MyPhoto> myPhotoList) {
        this.myPhotoList = myPhotoList;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);

        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        PhotoViewHolder viewHolder = (PhotoViewHolder) holder;
        viewHolder.image.setImageResource(R.drawable.ic_jlu_100px);
//        viewHolder.title.setText("zyascend cool!!!");
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        ImageView image;
        @Bind(R.id.title)
        TextView title;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
