package com.demo.run.picseledemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MAC on 2018/2/7.
 */

public class PicseleAdapter extends RecyclerView.Adapter<PicseleAdapter.ViewHolder> {

    private Context context;
    private List<PhotoBean> list;
    private OnClick mOnClick;

    interface OnClick {
        void itemOnClick(int position);
    }

    public PicseleAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setList(List<PhotoBean> list) {
        this.list.clear();
        this.list.addAll(list);
    }

    public void setmOnClick(OnClick onClick) {
        this.mOnClick = onClick;
    }

    @Override
    public PicseleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PicseleAdapter.ViewHolder holder, final int position) {
        Glide.with(context)
                .load(list.get(position).getPath())
                .override((PicseleActivity.w - 160) / 4, (PicseleActivity.w - 160) / 4)
                .thumbnail(0.2f)
                .centerCrop()
                .into(holder.photo);
        if(list.get(position).isCheck()){
            holder.shadow.setVisibility(View.VISIBLE);
            holder.check.setBackgroundResource(R.drawable.bg_picsele_check_ture);
        }
        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClick != null) {
                    mOnClick.itemOnClick(position);
                    if (!list.get(position).isCheck()) {
                        holder.shadow.setVisibility(View.GONE);
                        holder.check.setBackgroundResource(R.drawable.bg_picsele_check_false);
                    } else if (list.get(position).isCheck()) {
                        holder.shadow.setVisibility(View.VISIBLE);
                        holder.check.setBackgroundResource(R.drawable.bg_picsele_check_ture);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView photo, shadow;
        private TextView check;

        public ViewHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.itemphoto_image);
            shadow = (ImageView) itemView.findViewById(R.id.itemphoto_shadow);
            check = (TextView) itemView.findViewById(R.id.itemphoto_check);
        }
    }
}
