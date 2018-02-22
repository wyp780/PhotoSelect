package com.demo.run.picseledemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MAC on 2018/2/7.
 */

public class PopWindowAdapter extends RecyclerView.Adapter<PopWindowAdapter.ViewHolder> {

    private Context context;
    private List<PhotoAlbumBean> list;
    private int check_type = 0;
    private OnClick mOnClick;

    interface OnClick {
        void itemOnClick(int position);
    }

    public PopWindowAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setList(List<PhotoAlbumBean> list) {
        this.list.clear();
        this.list.addAll(list);
    }

    public void setmOnClick(OnClick onClick) {
        this.mOnClick = onClick;
    }


    @Override
    public PopWindowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.item_popwindow, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PopWindowAdapter.ViewHolder holder, final int position) {
        Glide.with(context)
                .load(list.get(position).getFirstImageUrl())
                .into(holder.photo);
        holder.name.setText(list.get(position).getName());
        holder.num.setText(list.get(position).getNumber() + "张");
        if (list.get(position).isIscheck()) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClick != null){
                    mOnClick.itemOnClick(position);
                    list.get(check_type).setIscheck(false);
                    list.get(position).setIscheck(true);
                    check_type = position;
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView photo;
        private TextView name, num;
        private CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.itempopwindow_image);
            name = (TextView) itemView.findViewById(R.id.itempopwindow_name);
            num = (TextView) itemView.findViewById(R.id.itempopwindow_num);
            checkBox = (CheckBox) itemView.findViewById(R.id.itempopwindow_check);
        }
    }
}
