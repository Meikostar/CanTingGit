package com.zhongchuang.canting.been;


import java.io.Serializable;
import java.util.List;

/**
 * Created by rzb on 2019/7/8
 */
public class ConfigDto extends BaseResponse implements Serializable {
    private HotSearchDto hot_search;
    public ConfigDto rights_pull_new;
    public ConfigDto apk;
    public ConfigDto data;
    public ConfigDto exchange;
    public ConfigDto system;
    public ConfigDto order;
    public ConfigDto wallet;
    public String level_1;
    public int unread_count;
    public String new_pull_end_at;
    public String level_2;
    public String gold_money;
    public String update_msg;
    public String update_time;
    public String download_url;
    public String version;
    public String today_order_count;
    public String week_order_count;
    public String month_order_count;
    public String yesterday_order_count;
    public String all_order_count;
    public String created_order_count;
    public String paid_order_count;
    public String completed_order_count;
    public String refund_order_count;
    public String buy_product_count;
    public String sale_product_count;
    public String no_sale_product_count;

    private List<String> st_search_distance;
    public List<String> search_distance;
    public List<AreaDto> shipment;

    public HotSearchDto getHot_search() {
        return hot_search;
    }

    public void setHot_search(HotSearchDto hot_search) {
        this.hot_search = hot_search;
    }

    public List<String> getSt_search_distance() {
        return st_search_distance;
    }

    public void setSt_search_distance(List<String> st_search_distance) {
        this.st_search_distance = st_search_distance;
    }
}
