package com.zhongchuang.canting.been;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/27.
 */

public class CateCaBean implements Serializable {
//    shop_gre_set_id   //菜设置id
//            gre_cate_id   //菜类别id
//    greens_info_id //菜信息id
//            greens_price   //价格
//    serial_number  //序号（序号越大，排序越前）
//            explain_conten  //说明内容
//    greens_image_url  //菜图片地址
//            create_time  //创建时间
//    modify_time  //修改时间
//            create_by  //创建人


    public String gre_cate_id;
    public int  count;
    public String greens_info_id;
    public String greens_name;
    public double greens_price;
    public String shopImageUrl;
    public String serial_number;
    public String greens_image_url;
    public String create_time;
    public String modify_time;
    public String create_by;
    public String explain_conten;
    public String greensInfoId;
    public String shopCartId;
    public String shop_gre_set_id;

    public CateCaBean data;

}
