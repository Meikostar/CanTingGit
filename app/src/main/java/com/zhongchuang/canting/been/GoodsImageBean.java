package com.zhongchuang.canting.been;

import android.net.Uri;

import java.io.Serializable;

/***
 * 功能描述:今日新款Item中图片列表item
 * 作者:chenwei
 * 时间:2016/12/21
 * 版本:v1.0
 ***/
public class GoodsImageBean implements Serializable {

    //本地需要数据
    public Uri uri;

    public int w;

    public int h;

    public boolean isSelected;

    /////////////////////////

    public String create_time_;// (string, optional): 创建时间 ,

    public long goods_ ;//(integer, optional): 商品主键 ,

    public long id_ ;//(integer, optional): 主键 ,

    public int sort_;// (integer, optional): 序号 ,

    public int type_ ;//(integer, optional): 类型:1图片/2视频 ,

    public String url_;// (string, optional): url

}
