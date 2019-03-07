package com.zhongchuang.canting.been;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mykar on 17/4/12.
 */

public class UploadFileBean implements Serializable {
    public static final int STATUS_COMMON = 0;
    public static final int STATUS_WAIT_UPLOAD = 1;
    public static final int STATUS_BEGIN_UPLOAD = 2;
    public static final int STATUS_UPLOADING = 3;

   // private String type;//类型 视频:video  图片:image 添加更多:other
    //private String path;//地址
    private int status;//状态 0:默认 1:待上传 2:开始上传  3:上传中 4:上传失败
    private boolean finishDelete;//上传完成是否删除
    private double process;
    private int sort;
    private String forderPath;
    protected String url_;
    public String img_name;
    public boolean ishow;

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }

    public boolean ishow() {
        return ishow;
    }

    public void setIshow(boolean ishow) {
        this.ishow = ishow;
    }

    protected int type_;//1图片2视频

    public String getUrl_() {
        return url_;
    }

    public void setUrl_(String url_) {
        this.url_ = url_;
    }

    public int getType_() {
        return type_;
    }

    public void setType_(int type_) {
        this.type_ = type_;
    }
    public String getForderPath() {
        return forderPath;
    }

    public void setForderPath(String forderPath) {
        this.forderPath = forderPath;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public UploadFileBean(int type) {
        this.type_ = type;
    }

    public UploadFileBean(int type, String path) {
        this.type_ = type;
        this.url_ = path;
    }

    public UploadFileBean(int type, String path, boolean finishDelete) {
        this.type_ = type;
        this.url_ = path;
        this.finishDelete  = finishDelete;
    }

    public static ArrayList<UploadFileBean> create(List<String> paths){
        ArrayList<UploadFileBean> beans = new ArrayList<>();
        if (paths!=null && paths.size()>0){
            for (String path : paths){
                beans.add(new UploadFileBean(1,path));
            }
        }
        return beans;
    }

    public static ArrayList<String> from(List<UploadFileBean> paths){
        ArrayList<String> beans = new ArrayList<>();
        if (paths!=null && paths.size()>0){
            for (UploadFileBean path : paths){
                beans.add(path.getPath());
            }
        }
        return beans;
    }

    public static boolean haveVideo(List<UploadFileBean> paths){
        ArrayList<String> beans = new ArrayList<>();
        if (paths!=null && paths.size()>0){
            for (UploadFileBean path : paths){
               if (path.getType()==2){
                   return true;
               }
            }
        }
        return false;
    }

    public int getType() {
        return type_;
    }

    public void setType(int type) {
        this.type_ = type;
    }

    public String getPath() {
        return url_;
    }

    public void setPath(String path) {
        this.url_ = path;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isFinishDelete() {
        return finishDelete;
    }

    public void setFinishDelete(boolean finishDelete) {
        this.finishDelete = finishDelete;
    }

    public double getProcess() {
        return process;
    }




}
