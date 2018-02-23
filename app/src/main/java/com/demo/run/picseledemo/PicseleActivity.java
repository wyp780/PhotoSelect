package com.demo.run.picseledemo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.demo.run.picseledemo.databinding.ActivityPicseleBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MAC on 2018/2/7.
 */

public class PicseleActivity extends Activity {

    public static int w;//屏幕宽度，adapter加载图片时要把边框留出来(还不确定)
    private ActivityPicseleBinding binding;
    private Context context;
    private List<PhotoAlbumBean> photoAlbumBeans = new ArrayList<>();
    private List<PhotoBean> photoBeanList = new ArrayList<>();
    private ArrayList<String> resultList = new ArrayList<>();
    private PicseleAdapter adapter;
    private int MAX_NUM = 9;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_picsele);
        w = getResources().getDisplayMetrics().widthPixels;
        binding.picselePop.setBinding(binding);
        init();
    }

    private void init() {
        adapter = new PicseleAdapter(context);
        binding.picseleList.setLayoutManager(new GridLayoutManager(this, 4));
        binding.picseleList.addItemDecoration(new DividerGridItemDecoration(context, DividerGridItemDecoration.GRID_DIVIDER_VERTICAL));
        binding.picseleList.setAdapter(adapter);
        permissionOpen();

        binding.picseleSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("imageList", resultList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        binding.picseleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("imageList", new ArrayList<String>());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        binding.picselePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreViewActivity.jumpPreViewActivity(context, resultList);
            }
        });

        binding.picseleDocumentationListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.picselePop.setMAX_H(binding.picseleList.getHeight());
                binding.picselePop.showView();
                binding.picselePop.adapter.setmOnClick(new PopWindowAdapter.OnClick() {
                    @Override
                    public void itemOnClick(int position) {
                        setData(photoAlbumBeans.get(position).getImageList());
                    }
                });
            }
        });
    }

    //是否开启了读写权限
    private boolean permissionOpen() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    101);
        } else {
            getImages();
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getImages();
            } else {
                Toast.makeText(this, "权限申请失败,请重试", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //获取手机所有图片
    private void getImages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = context.getContentResolver();

                Cursor cursor = contentResolver.query(imageUri, null, MediaStore.Images.Media.MIME_TYPE
                        + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg"}, MediaStore.Images.Media.DATE_MODIFIED);

                if (cursor == null) {
                    return;
                }

                List<PhotoBean> pathList = new ArrayList<>();
                String mParentPath = "";
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File file = new File(path).getParentFile();
                    String parentPath = file.getAbsolutePath();

                    if (cursor.isFirst()) {
                        PhotoBean imgeBean = new PhotoBean();
                        imgeBean.setPath(path);
                        pathList.add(imgeBean);
                        mParentPath = parentPath;
                    } else if (mParentPath.equals(parentPath)) {
                        PhotoBean imgeBean = new PhotoBean();
                        imgeBean.setPath(path);
                        pathList.add(imgeBean);
                    } else {
                        List<PhotoBean> list = new ArrayList<>();
                        list.addAll(pathList);
                        pathList.clear();
                        PhotoBean imgeBean = new PhotoBean();
                        imgeBean.setPath(path);
                        pathList.add(imgeBean);
                        PhotoAlbumBean bean = new PhotoAlbumBean();
                        bean.setNumber(list.size());
                        bean.setFirstImageUrl(list.get(0).getPath());
                        bean.setImageList(list);
                        bean.setParentPath(mParentPath);
                        photoAlbumBeans.add(bean);
                        mParentPath = parentPath;
                    }
                }
                handler.sendEmptyMessage(0x110);
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            initPop();
            for (PhotoAlbumBean pictureBean : photoAlbumBeans) {
                for (PhotoBean b : pictureBean.getImageList()) {
                    photoBeanList.add(b);
                }
            }
            adapter.setList(photoBeanList);
            adapter.notifyDataSetChanged();
            adapter.setmOnClick(new PicseleAdapter.OnClick() {
                @Override
                public void itemOnClick(int position) {
                    if (photoBeanList.get(position).isCheck()) {
                        photoBeanList.get(position).setCheck(false);
                        for (int i = 0; i < resultList.size(); i++) {
                            if (resultList.get(i).equals(photoBeanList.get(position).getPath())) {
                                resultList.remove(i);
                            }
                        }
                    } else {
                        if (resultList.size() >= MAX_NUM) {
                            Toast.makeText(context, "不能超过" + MAX_NUM + "张图片", Toast.LENGTH_LONG).show();
                        } else {
                            photoBeanList.get(position).setCheck(true);
                            resultList.add(photoBeanList.get(position).getPath());
                        }
                    }
                    changeSendText(resultList.size(), MAX_NUM);
                    if (resultList.size() > 0) {
                        binding.picselePreview.setTextColor(getResources().getColor(R.color.colorWhite));
                        binding.picselePreview.setEnabled(true);
                    } else {
                        binding.picselePreview.setTextColor(getResources().getColor(R.color.color5fWhite));
                        binding.picselePreview.setEnabled(false);
                    }
                }
            });
        }
    };

    //改变发送按钮的背景和字体颜色
    private void changeSendText(int i, int max) {
        if (i > 0) {
            binding.picseleSend.setTextColor(getResources().getColor(R.color.colorWhite));
            binding.picseleSend.setBackgroundResource(R.drawable.bg_picsele_send_l);
        } else {
            binding.picseleSend.setTextColor(getResources().getColor(R.color.color5fWhite));
            binding.picseleSend.setBackgroundResource(R.drawable.bg_picsele_send_d);
        }
        binding.picseleSend.setText("发送" + i + "/" + max);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra("imageList", new ArrayList<String>());
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initPop() {
        binding.picselePop.setData(photoAlbumBeans);
    }

    public void setData(List<PhotoBean> list) {
        photoBeanList.clear();
        photoBeanList.addAll(list);
        adapter.setList(list);
        adapter.notifyDataSetChanged();
        binding.picselePop.showView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra("imageList", resultList);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
