package com.zhongchuang.canting.widget;


public enum LoadMoreMode {
    /**
     * 点击加载更多
     */
    CLICK,
    /**
     * 滑动到底部加载跟多
     */
    SCROLL;

    static LoadMoreMode mapIntToValue(int modeInt) {
        switch (modeInt) {
            case 0x0:
            default:
                return CLICK;
            case 0x1:
                return SCROLL;
        }
    }
}
