package com.demo.run.picseledemo;

import java.util.List;

/**
 * Created by MAC on 2018/2/2.
 */

public class PhotoAlbumBean {

    private String name;
    private int number;
    private String firstImageUrl;
    private List<PhotoBean> imageList;
    private String parentPath;
    private boolean ischeck = false;

    public boolean isIscheck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }
    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getFirstImageUrl() {
        return firstImageUrl;
    }

    public void setFirstImageUrl(String firstImageUrl) {
        this.firstImageUrl = firstImageUrl;
    }

    public List<PhotoBean> getImageList() {
        return imageList;
    }

    public void setImageList(List<PhotoBean> imageList) {
        this.imageList = imageList;
    }

}
