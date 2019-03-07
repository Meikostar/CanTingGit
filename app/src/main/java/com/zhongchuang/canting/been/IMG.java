package com.zhongchuang.canting.been;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by honghouyang on 16/11/24.
 */
public class IMG implements Serializable {
    public static final int IMG_URL = 0;
    public static final int IMG_RESOURCE = 1;

    public String img_url;
    public int img_resource;
    public int type;

    public ArrayList<IMG> child_list=new ArrayList<>();


}
