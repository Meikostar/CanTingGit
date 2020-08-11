package com.zhongchuang.canting.been;


import com.zhongchuang.canting.been.pay.ExtDto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rzb on 2019/6/20.
 */
public class RecommendListDto implements Serializable {
    private String       shop_name;
    private String       type;
    private String       category;
    private String       industry;
    private String       name;
    private String       area_id;
    private String       address;
    private String       lat;
    private String       lng;
    private String       phone;
    private String       logo;
    private String photos;
    public List<String> service_arr;

    private ExtDto ext;
    private String       created_at;
    private String       updated_at;
    private String       comment_count;
    private String       comment_count_rate;
    private String       comment_rate;
    private String       site_recommend;
    private String       slug;
    private String       brief;
    private String       background_img;
    private String       tips;
    private List<String> labels;
    private String       order_column;
    public String       new_goods_count;
    public String       per_consumption;
    private String       comment_good_count;
    private String       comment_average_score;
    public String       distance;
    private String       follow_count;
    private boolean       is_follow;
    private String       id;
    private List<String> area;
    private String       new_product;
    private List<String> services;
    private ProductListDto  products;

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public ExtDto getExt() {
        return ext;
    }

    public void setExt(ExtDto ext) {
        this.ext = ext;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getComment_count_rate() {
        return comment_count_rate;
    }

    public void setComment_count_rate(String comment_count_rate) {
        this.comment_count_rate = comment_count_rate;
    }

    public String getComment_rate() {
        return comment_rate;
    }

    public void setComment_rate(String comment_rate) {
        this.comment_rate = comment_rate;
    }

    public String getSite_recommend() {
        return site_recommend;
    }

    public void setSite_recommend(String site_recommend) {
        this.site_recommend = site_recommend;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getBackground_img() {
        return background_img;
    }

    public void setBackground_img(String background_img) {
        this.background_img = background_img;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getOrder_column() {
        return order_column;
    }

    public void setOrder_column(String order_column) {
        this.order_column = order_column;
    }

    public String getComment_good_count() {
        return comment_good_count;
    }

    public void setComment_good_count(String comment_good_count) {
        this.comment_good_count = comment_good_count;
    }

    public String getComment_average_score() {
        return comment_average_score;
    }

    public void setComment_average_score(String comment_average_score) {
        this.comment_average_score = comment_average_score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNew_product() {
        return new_product;
    }

    public void setNew_product(String new_product) {
        this.new_product = new_product;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public ProductListDto getProducts() {
        return products;
    }

    public void setProducts(ProductListDto products) {
        this.products = products;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getFollow_count() {
        return follow_count;
    }

    public void setFollow_count(String follow_count) {
        this.follow_count = follow_count;
    }

    public boolean isIs_follow() {
        return is_follow;
    }

    public List<String> getArea() {
        return area;
    }

    public void setArea(List<String> area) {
        this.area = area;
    }
}
