package com.hongbo.mylibrary;

import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

public abstract class JsOnClickListenter {
    @JavascriptInterface
    public void aclick(String id) {
        fun(id);
    }
    public abstract void fun(String id);
    public abstract void scrollToBottom();
    @JavascriptInterface
    public void log(String log) {
        LogUtils.showLog(log);
    }
    @JavascriptInterface
    public void scrollToWebBottom() {
        scrollToBottom();
    }
}
