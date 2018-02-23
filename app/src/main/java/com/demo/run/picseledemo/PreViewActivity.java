package com.demo.run.picseledemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.demo.run.picseledemo.databinding.ActivityPreviewBinding;

import java.util.ArrayList;
import java.util.List;

import me.panpf.sketch.SketchImageView;

/**
 * Created by MAC on 2018/2/23.
 */

public class PreViewActivity extends Activity {

    private ActivityPreviewBinding binding;
    private Context context;
    private LinearLayoutManager linearLayoutManager;
    private PreViewPhotoListAdapter adapter1;
    private PreViewAdapter adapter;
    private List<String> list;

    public static void jumpPreViewActivity(Context context, ArrayList<String> list) {
        Intent intent = new Intent(context, PreViewActivity.class);
        intent.putStringArrayListExtra("photoList", list);
        ((Activity) context).startActivityForResult(intent, 101);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_preview);
        init();
        onclick();
    }

    private void init() {
        list = getIntent().getStringArrayListExtra("photoList");

        binding.previewSend.setText("发送" + list.size() + "/" + list.size());

        if (list != null) {
            List<SketchImageView> imageViewList = new ArrayList<>();
            List<PhotoBean> photoBeans = new ArrayList<>();
            for (String s : list) {
                SketchImageView sketchImageView = new SketchImageView(context);
                sketchImageView.displayImage(s);
                imageViewList.add(sketchImageView);

                PhotoBean photoBean = new PhotoBean();
                photoBean.setPath(s);
                photoBeans.add(photoBean);
            }
            adapter = new PreViewAdapter(context, imageViewList);
            binding.previewPhoto.setAdapter(adapter);
            binding.previewPhoto.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    adapter1.showBorder(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            binding.previewPhotoList.setLayoutManager(linearLayoutManager);
            adapter1 = new PreViewPhotoListAdapter(context);
            binding.previewPhotoList.setAdapter(adapter1);
            photoBeans.get(0).setCheck(true);
            adapter1.setList(photoBeans);
            adapter1.notifyDataSetChanged();
            adapter1.setmOnClick(new PreViewPhotoListAdapter.OnClick() {
                @Override
                public void itemOnClick(int position) {
                    controller(position);
                }
            });
        }
    }

    private void controller(int i) {
        binding.previewPhoto.setCurrentItem(i);
    }

    private void onclick() {
        binding.previewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.previewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
