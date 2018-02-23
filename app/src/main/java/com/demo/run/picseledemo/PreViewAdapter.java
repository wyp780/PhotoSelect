package com.demo.run.picseledemo;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.panpf.sketch.SketchImageView;

/**
 * Created by MAC on 2018/2/23.
 */

public class PreViewAdapter extends PagerAdapter {

    private Context context;
    private List<SketchImageView> list = new ArrayList<>();

    public PreViewAdapter(Context context, List<SketchImageView> list) {
        this.context = context;
        this.list.addAll(list);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(list.get(position), 0);
        return list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position ));
    }
}
