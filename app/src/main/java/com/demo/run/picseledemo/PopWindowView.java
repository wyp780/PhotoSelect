package com.demo.run.picseledemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.demo.run.picseledemo.databinding.ActivityPicseleBinding;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by MAC on 2018/2/8.
 */

public class PopWindowView extends LinearLayout {

    private Context context;
    private View popView;
    private LinearLayout linearLayout;
    private RecyclerView recyclerView;
    public PopWindowAdapter adapter;
    private boolean isShow = false;
    private int MAX_H = 0;
    private ActivityPicseleBinding binding;

    public PopWindowView(Context context) {
        super(context);
        init(context);
    }

    public PopWindowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PopWindowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        adapter = new PopWindowAdapter(context);
        popView = LayoutInflater.from(context).inflate(R.layout.pop_photoalbum, this);
        recyclerView = (RecyclerView) popView.findViewById(R.id.pop_list);
        linearLayout = (LinearLayout) popView.findViewById(R.id.pop_lilayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        linearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showView();
            }
        });
    }

    public void setData(List<PhotoAlbumBean> list) {
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    public void setMAX_H(int i){
        this.MAX_H = i;
    }

    public void setBinding(ActivityPicseleBinding binding){
        this.binding = binding;
    }

    public void showView() {
        //View是否显示的标志
        isShow = !isShow;
        //属性动画对象
        ValueAnimator va;
        if (isShow) {
            //显示view，高度从0变到height值
            binding.picseleMasks.setVisibility(View.VISIBLE);
            va = ValueAnimator.ofInt(0, MAX_H);
        } else {
            //隐藏view，高度从height变为0
            binding.picseleMasks.setVisibility(View.GONE);
            va = ValueAnimator.ofInt(MAX_H, 0);
        }
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //获取当前的height值
                int h = (Integer) valueAnimator.getAnimatedValue();
                //动态更新view的高度
                getLayoutParams().height = h;
                requestLayout();

                double alpah = new BigDecimal((float)h / MAX_H).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                int a = (int) ((alpah) * 177);
                binding.picseleMasks.setBackgroundColor(Color.argb(a,0,0,0));
            }
        });
        va.setDuration(200);
        //开始动画
        va.start();
    }

}
