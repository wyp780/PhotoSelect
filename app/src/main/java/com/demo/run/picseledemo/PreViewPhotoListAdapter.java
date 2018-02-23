package com.demo.run.picseledemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MAC on 2018/2/23.
 */

public class PreViewPhotoListAdapter extends RecyclerView.Adapter<PreViewPhotoListAdapter.ViewHolder> {

    private Context context;
    private List<PhotoBean> list = new ArrayList<>();
    private OnClick mOnClick;
    private int border_type = 0;

    interface OnClick {
        void itemOnClick(int position);
    }

    public PreViewPhotoListAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<PhotoBean> list) {
        this.list.clear();
        this.list.addAll(list);
    }

    public void setmOnClick(OnClick onClick) {
        this.mOnClick = onClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_preview, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context)
                .load(list.get(position).getPath())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClick != null) {
                    mOnClick.itemOnClick(position);
                    showBorder(position);
                }
            }
        });
        if (list.get(position).isCheck()) {
            holder.border.setVisibility(View.VISIBLE);
        } else {
            holder.border.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private View border;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.itempreview_image);
            border = (View) itemView.findViewById(R.id.itempreview_border);
        }
    }

    public void showBorder(int i){
        list.get(border_type).setCheck(false);
        list.get(i).setCheck(true);
        border_type = i;
        notifyDataSetChanged();
    }

}
