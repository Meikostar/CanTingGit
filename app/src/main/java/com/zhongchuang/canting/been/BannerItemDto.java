package com.zhongchuang.canting.been;


import java.io.Serializable;

/**
 * Created by Lenovo on 2019/6/28.
 */
public class BannerItemDto implements Serializable {
    private String id;
    private String path;
    private String click_event_type;
    private String click_event_value;
    private String description;
    private String sort;
    private ExtendDto extend;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getClick_event_type() {
        return click_event_type;
    }

    public void setClick_event_type(String click_event_type) {
        this.click_event_type = click_event_type;
    }

    public String getClick_event_value() {
        return click_event_value;
    }

    public void setClick_event_value(String click_event_value) {
        this.click_event_value = click_event_value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public ExtendDto getExtend() {
        return extend;
    }

    public void setExtend(ExtendDto extend) {
        this.extend = extend;
    }
}
