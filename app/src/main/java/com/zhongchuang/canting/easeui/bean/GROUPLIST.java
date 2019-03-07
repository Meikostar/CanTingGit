package com.zhongchuang.canting.easeui.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by syj on 2016/12/5.
 */
public class GROUPLIST implements Serializable {
    private ArrayList<GROUPS> group;

    public ArrayList<GROUPS> getGroup() {
        return group;
    }

    public void setGroup(ArrayList<GROUPS> group) {
        this.group = group;
    }
}
