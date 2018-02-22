package com.demo.run.picseledemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.demo.run.picseledemo.databinding.ActivityHomeBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MAC on 2018/2/7.
 */

public class HomeActivity extends Activity {

    private ActivityHomeBinding binding;
    private Context context;
    private HomeAdapter adapter;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        init();
    }

    private void init() {

        adapter = new HomeAdapter(context);
        binding.homeList.setLayoutManager(new LinearLayoutManager(context));
        binding.homeList.setAdapter(adapter);

        binding.homeOpenPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PicseleActivity.class);
                startActivityForResult(intent, 101);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            list = data.getStringArrayListExtra("imageList");
            if (list.size() > 0) {
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
