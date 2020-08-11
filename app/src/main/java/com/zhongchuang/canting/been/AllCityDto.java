package com.zhongchuang.canting.been;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rzb on 2019/7/9.
 */
public class AllCityDto implements Serializable {
    private String                   id;
    private String                   name;
    public String                   cat_name;
    public String                   title;
    private String                   parent_id;
    private String                   _lft;
    private String                   _rgt;
    private String                   code;
    private List<SecondLevelCityCto> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }


    public String get_rgt() {
        return _rgt;
    }

    public void set_rgt(String _rgt) {
        this._rgt = _rgt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String get_lft() {
        return _lft;
    }

    public void set_lft(String _lft) {
        this._lft = _lft;
    }

    public List<SecondLevelCityCto> getChildren() {
        return children;
    }

    public void setChildren(List<SecondLevelCityCto> children) {
        this.children = children;
    }
}

