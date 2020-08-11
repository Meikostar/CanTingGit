package com.zhongchuang.canting.been;


import com.zhongchuang.canting.been.pay.ExtDto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Lenovo on 2019/7/5.
 */
public class ProductDto implements Serializable {
    private String       id;
    private String       type;
    private String       shop_id;
    private String       title;
    private String       product_no;
    public String       save_money;
    private String       cover;
    private List<String> imgs;
    private String       market_price;
    private String       price;
    private String       score;
    private String       category_id;
    private String       content;
    private String       stock;
    private String       order_column;
    private String       model_id;
    private Map<String,String> parameter;
    private ExtDto       ext;
    private String       slug;
    private String       sales_count;
    private String       template_id;
    private String       weight;
    private String       is_virtual;
    private String       comment_count;
    private String       comment_good_rate;
    private String       favorite_count;
    private String       on_sale;
    private String       is_new;
    private String       is_hot;
    private String       is_recommend;
    private String       is_shop_recommend;
    private String       created_at;
    private String       updated_at;
    private List<String> labels;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProduct_no() {
        return product_no;
    }

    public void setProduct_no(String product_no) {
        this.product_no = product_no;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getOrder_column() {
        return order_column;
    }

    public void setOrder_column(String order_column) {
        this.order_column = order_column;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public Map<String, String> getParameter() {
        return parameter;
    }

    public ExtDto getExt() {
        return ext;
    }

    public void setExt(ExtDto ext) {
        this.ext = ext;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSales_count() {
        return sales_count;
    }

    public void setSales_count(String sales_count) {
        this.sales_count = sales_count;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getIs_virtual() {
        return is_virtual;
    }

    public void setIs_virtual(String is_virtual) {
        this.is_virtual = is_virtual;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getComment_good_rate() {
        return comment_good_rate;
    }

    public void setComment_good_rate(String comment_good_rate) {
        this.comment_good_rate = comment_good_rate;
    }

    public String getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(String favorite_count) {
        this.favorite_count = favorite_count;
    }

    public String getOn_sale() {
        return on_sale;
    }

    public void setOn_sale(String on_sale) {
        this.on_sale = on_sale;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public String getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(String is_hot) {
        this.is_hot = is_hot;
    }

    public String getIs_recommend() {
        return is_recommend;
    }

    public void setIs_recommend(String is_recommend) {
        this.is_recommend = is_recommend;
    }

    public String getIs_shop_recommend() {
        return is_shop_recommend;
    }

    public void setIs_shop_recommend(String is_shop_recommend) {
        this.is_shop_recommend = is_shop_recommend;
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

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
}
