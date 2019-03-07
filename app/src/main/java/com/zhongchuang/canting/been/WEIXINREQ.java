package com.zhongchuang.canting.been;


import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/23.
 */

public class WEIXINREQ extends BaseResponse implements Serializable {
    public String sign;
    public String timestamp;
    public String noncestr;
    public String nonce_str;
    public String prepayid;
    public String mch_id;
    public WEIXINREQ data;
    public String packages;//="Sign=WXPay"
    public String appid;
    public String partnerid;
    public String orderId;

//    "package": "Sign=WXPay",
//            "orderId": "1000001275057876",
//            "appid": "wxed0453751545a174",
//            "sign": "5049D38F222F7421C31197D2F174FBC3",
//            "partnerid": "1496042602",
//            "prepayid": "wx181737247031626a567e36550344247913",
//            "noncestr": "wHqh4BiNmzGWkNarREGe8vc3WCBKJuhz",
//            "timestamp": "1531906628"
//        "package": "Sign=WXPay",
//                "appid": "wxed0453751545a174",
//                "sign": "A2A0D71D3EAB1FE1F8216EE940FCC25F",
//                "partnerid": "1496042602",
//                "prepayid": "wx2018020212015946b9238c500089992428",
//                "noncestr": "2ZKiLOzWDC6rooI91KMSYis5wi6xynzg",
//                "timestamp": "1517544131"


}
