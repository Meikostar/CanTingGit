package com.zhongchuang.canting.presenter;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/6.
 */

public interface LiveStreamPresenter {

    void getLiveRoomData(Map<String,String> map,int type);
    void getLatestVideoList(Map<String, String> map, int loadtype);


}
