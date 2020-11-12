package com.hongbo.mylibrary;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;


public class BaseActivity extends FragmentActivity implements View.OnClickListener {
    public List<String> targetPermission=new ArrayList<>();
    public static final int STATUS1=1;//欢迎页面模式
    public static final int STATUS2=2;//获取地理位置模式
    public int status;
    public OnRequestPermissionsResultListenter onRequestPermissionsResultListenter;
    public void setOnRequestPermissionsResultListenter(OnRequestPermissionsResultListenter onRequestPermissionsResultListenter){
        //利用动态代理实现切面编程
        MyProxy myProxy=new MyProxy();
        myProxy.setOnRequestPermissionsResultListenter(onRequestPermissionsResultListenter);
        myProxy.setBaseActivity(this);
        this.onRequestPermissionsResultListenter=(OnRequestPermissionsResultListenter)myProxy.getProxy();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(onRequestPermissionsResultListenter!=null){
                onRequestPermissionsResultListenter.onRequestPermissionsResultListen(true);
            }
        }else {
            if(onRequestPermissionsResultListenter!=null){
                onRequestPermissionsResultListenter.onRequestPermissionsResultListen(false);
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(targetPermission.size()>0&&requestCode==100){
            if(!PermissionUtils.checkPermissionAndListen(this)){
                if(onRequestPermissionsResultListenter!=null)onRequestPermissionsResultListenter.onRequestPermissionsResultListen(false);
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
}
