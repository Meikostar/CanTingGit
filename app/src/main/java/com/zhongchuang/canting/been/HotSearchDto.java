package com.zhongchuang.canting.been;


import java.io.Serializable;
import java.util.List;

/**
 * Created by rzb on 2019/7/18.
 */
public class HotSearchDto implements Serializable {
    private List<String> consume_index;
    private List<String> gc;
    private List<String> lm;
    private List<String> ax;
    private List<String> st;

    public List<String> getConsume_index() {
        return consume_index;
    }

    public void setConsume_index(List<String> consume_index) {
        this.consume_index = consume_index;
    }

    public List<String> getGc() {
        return gc;
    }

    public void setGc(List<String> gc) {
        this.gc = gc;
    }

    public List<String> getLm() {
        return lm;
    }

    public void setLm(List<String> lm) {
        this.lm = lm;
    }

    public List<String> getAx() {
        return ax;
    }

    public void setAx(List<String> ax) {
        this.ax = ax;
    }

    public List<String> getSt() {
        return st;
    }

    public void setSt(List<String> st) {
        this.st = st;
    }
}

