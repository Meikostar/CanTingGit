package com.zhongchuang.canting.been;

import java.io.Serializable;
import java.util.List;

public class GoodsAttrDto implements Serializable {
    private String key;
    private List<String> attrList;
    public List<BaseDto> data;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public List<String> getAttrList() {
        return attrList;
    }

    public void setAttrList(List<String> attrList) {
        this.attrList = attrList;
    }
}
