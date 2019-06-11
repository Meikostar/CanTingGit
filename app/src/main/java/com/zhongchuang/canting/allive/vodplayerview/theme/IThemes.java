package com.zhongchuang.canting.allive.vodplayerview.theme;

import com.zhongchuang.canting.allive.vodplayerview.view.control.ControlView;
import com.zhongchuang.canting.allive.vodplayerview.view.guide.GuideView;
import com.zhongchuang.canting.allive.vodplayerview.view.quality.QualityView;
import com.zhongchuang.canting.allive.vodplayerview.view.speed.SpeedView;
import com.zhongchuang.canting.allive.vodplayerview.view.tipsview.ErrorView;
import com.zhongchuang.canting.allive.vodplayerview.view.tipsview.NetChangeView;
import com.zhongchuang.canting.allive.vodplayerview.view.tipsview.ReplayView;
import com.zhongchuang.canting.allive.vodplayerview.view.tipsview.TipsView;
import com.zhongchuang.canting.allive.vodplayerview.widget.AliyunVodPlayerView;
import com.zhongchuang.canting.allive.vodplayerview.widget.AliyunVodPlayerViews;

/*
 * Copyright (C) 2010-2018 Alibaba Group Holding Limited.
 */

/**
 * 主题的接口。用于变换UI的主题。
 * 实现类有{@link ErrorView}，{@link NetChangeView} , {@link ReplayView} ,{@link ControlView},
 * {@link GuideView} , {@link QualityView}, {@link SpeedView} , {@link TipsView},
 * {@link AliyunVodPlayerView}
 */

public interface IThemes {
    /**
     * 设置主题
     * @param theme 支持的主题
     */
    void setTheme(AliyunVodPlayerViews.Theme theme);
}
