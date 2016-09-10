package com.efithealth.app.Photo;

/**
 * Created by zhuang on 2015/9/17.
 */
public class ImageModel {

    private String id;//图片id
    public String path;//路径
//    private Boolean isChecked = false;//是否被选中
    public boolean isSelected=false;

    public ImageModel(String id, String path, Boolean isSelected) {
        this.id = id;
        this.path = path;
        this.isSelected = isSelected;
    }

    public ImageModel(String id, String path) {
        this.id = id;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getisSelected() {
        return isSelected;
    }

    public void setisSelected(Boolean isChecked) {
        this.isSelected = isChecked;
    }
}
