package com.zhongchuang.canting.widget;

import java.util.List;


public class TagBean  {

    private String title;
    private String url;
    private double price;
    private int amount;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private List<TagBean> tagBean=null;

    public TagBean(){}
    public  TagBean(String title ,double price,int amount,String url){
        this.url=url;
        this.title=title;
        this.price=price;
        this.amount=amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setTagBean(List<TagBean> tagBean) {
        this.tagBean = tagBean;
    }
    public List<TagBean> getTagBean(){
        return  tagBean;
    }
}
