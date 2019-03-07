package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2018/7/6.
 */

public class ProductBuy extends BaseResponse{

//               "firstSpeci": "颜色",
//                       "firstSpeciValue": "灰色",
//                       "product_sku_id": "45861",
//                       "secondSpeciValue": "36码",
//                       "id": "13",
//                       "product_platform_id": "4586",
//                       "secondSpeci": "尺寸"
    public boolean sku_enable;
    public String firstSpeci;
    public String secondSpeci;
    public String threeSpeci;
    public String fourSpeci;
    public String fiveSpeci;
    public String firstSpeciValue;
    public String picture_sku_url;
    public String threeSpeciValue;
    public String fourSpeciValue;
    public String fiveSpeciValue;
    public String pro_stock;
    public String secondSpeciValue;
    public String firstValue;
    public String product_platform_id;
    public String product_sku_id;
    public String strValue;
    public String picture_url ;
    public String  integral_price ;
    public String   pro_price ;
    public int pro_site ;
    public int tierNumber ;

//    mer_name //店铺名称  ,ship_address //发货地址
    public ProductBuy data;
    public List<ProductBuy> listResult;

}
