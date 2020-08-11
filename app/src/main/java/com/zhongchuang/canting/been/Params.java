package com.zhongchuang.canting.been;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/10.
 */

public class Params implements Serializable{


    public String proSite;
    public String payType;
    public String transactionId;
    public String id;

    public List<String> shop_category;
    public List<String> shop_platform;
    public Params       children;
    public String title;
    public String parent_id;
    public List<Params> data;




}
