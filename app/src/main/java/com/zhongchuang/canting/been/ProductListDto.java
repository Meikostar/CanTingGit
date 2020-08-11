package com.zhongchuang.canting.been;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rzb on 2019/6/29
 */
public class ProductListDto implements Serializable {
    private List<ProductDto> data;

    public List<ProductDto> getData() {
        return data;
    }

    public void setData(List<ProductDto> data) {
        this.data = data;
    }
}
