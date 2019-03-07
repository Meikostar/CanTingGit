package com.zhongchuang.canting.allive.editor.msg;

public interface MessageHandler {
    <T> int onHandleMessage(T message);
}
