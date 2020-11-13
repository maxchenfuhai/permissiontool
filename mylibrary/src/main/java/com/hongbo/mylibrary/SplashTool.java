package com.hongbo.mylibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static android.webkit.WebSettings.LOAD_NO_CACHE;

public abstract class SplashTool implements View.OnClickListener {
    Activity context;

    public SplashTool(Activity context) {
        this.context = context;
    }

    Dialog permissiondialog;
    public void buildonPermissionsDialog(JsOnClickListenter jsOnClickListenter,int type){
        int laoutid=R.layout.permission_layout_dialog;
        if(type==1){
            laoutid=R.layout.permission_layout_dialog1;
        }
        View permissionLayout = LayoutInflater.from(context).inflate(laoutid, null);
        permissionLayout.findViewById(R.id.disagree).setOnClickListener(this);
        permissionLayout.findViewById(R.id.agree).setOnClickListener(this);
        final WebView webView = permissionLayout.findViewById(R.id.webView);
        webView.addJavascriptInterface(jsOnClickListenter, "alistner");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.getSettings().setCacheMode(LOAD_NO_CACHE);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                //注入Js代码
                webView.loadUrl("javascript:(function(){" +
                        "var objs = document.getElementsByTagName(\"u\"); " +
                        "for(var i=0;i<objs.length;i++)  " +
                        "{"
                        + "    objs[i].onclick=function()  " +
                        "    {  "
                        + "        window.alistner.aclick(this.id);  " +
                        "    }  " +
                        "}" +
                        "})()");
            }
        });
        webView.loadUrl("file:////android_asset/agree.html");
        permissiondialog = new Dialog(context, R.style.mydialog);
        permissiondialog.setContentView(permissionLayout);
        permissiondialog.setCancelable(false);
        permissiondialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.disagree){
            permissiondialog.cancel();
            new AlertDialog.Builder(context).setTitle("提示").setMessage("确定退出吗？")
                    .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            context.finish();
                        }
                    }).setNegativeButton("继续使用", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    permissiondialog.show();
                }
            }).show();
        }else if(v.getId()==R.id.agree){
            permissiondialog.cancel();
            afertAgree();
        }
    }

    public abstract void afertAgree();
}
