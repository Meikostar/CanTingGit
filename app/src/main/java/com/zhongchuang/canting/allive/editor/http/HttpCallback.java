/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.zhongchuang.canting.allive.editor.http;

public interface HttpCallback<T> {
    void onSuccess(T result);
    void onFailure(Throwable e);
}
