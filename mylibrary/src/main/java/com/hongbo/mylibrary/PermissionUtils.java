package com.hongbo.mylibrary;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {

    public static void requstPermission(final BaseActivity baseActivity ) {
        SharedPreferences sharedPreferences= baseActivity.getSharedPreferences("user",  Context.MODE_PRIVATE);
        String str=baseActivity.targetPermission.get(0);
        boolean isfirst=sharedPreferences.getBoolean(str,true);
        if(isfirst){
            sharedPreferences.edit().putBoolean(str,false).commit();
            ActivityCompat.requestPermissions(baseActivity,
                    new String[]{str}, 1);
        }else if (ActivityCompat.shouldShowRequestPermissionRationale(baseActivity, str)) {
            ActivityCompat.requestPermissions(baseActivity,
                    new String[]{str}, 1);
        } else {
            String s = "";
            if (str.equals(Manifest.permission.READ_PHONE_STATE)) s = "读取手机信息权限";
            if (str.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) s = "存储权限";
            if (str.equals(Manifest.permission.CAMERA)) s = "摄像头权限";
            if (str.equals(Manifest.permission.RECORD_AUDIO)) s = "麦克风权限";
            if (str.equals(Manifest.permission.ACCESS_FINE_LOCATION)) s = "GPS权限";
            if (str.equals(Manifest.permission.ACCESS_COARSE_LOCATION)) s = "网络定位权限";
            if(baseActivity.status==BaseActivity.STATUS1){
                if (baseActivity.onRequestPermissionsResultListenter != null){
                    baseActivity.onRequestPermissionsResultListenter.onRequestPermissionsResultListen(false);
                }
               return;
            }
            showRequestPermissionDialog(baseActivity,str);
        }
    }

    public static boolean checkPermissionAndListen( BaseActivity baseActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(baseActivity, baseActivity.targetPermission.get(0))
                == PackageManager.PERMISSION_GRANTED) {
            if (baseActivity.onRequestPermissionsResultListenter != null){
                baseActivity.onRequestPermissionsResultListenter.onRequestPermissionsResultListen(true);

            }
            return true;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (baseActivity.onRequestPermissionsResultListenter != null)
                baseActivity.onRequestPermissionsResultListenter.onRequestPermissionsResultListen(true);
            return true;
        }
        return false;
    }


    public static void showRequestPermissionDialog(final BaseActivity baseActivity,String str){
        String s = "";
        if (str.equals(Manifest.permission.READ_PHONE_STATE)) s = "读取手机信息权限";
        if (str.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) s = "存储权限";
        if (str.equals(Manifest.permission.CAMERA)) s = "摄像头权限";
        if (str.equals(Manifest.permission.RECORD_AUDIO)) s = "麦克风权限";
        if (str.equals(Manifest.permission.ACCESS_FINE_LOCATION)) s = "GPS权限";
        if (str.equals(Manifest.permission.ACCESS_COARSE_LOCATION)) s = "网络定位权限";
        new AlertDialog.Builder(baseActivity).setTitle("提示").setMessage("此功能需要获取" + s + "，是否授权？")
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(baseActivity.status==BaseActivity.STATUS2){
                            baseActivity.targetPermission.clear();
                        }else {
                            if (baseActivity.onRequestPermissionsResultListenter != null){
                                baseActivity.onRequestPermissionsResultListenter.onRequestPermissionsResultListen(false);
                            }
                        }
                    }
                }).setNegativeButton("授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", baseActivity.getPackageName(), null);
                intent.setData(uri);
                baseActivity.startActivityForResult(intent, 100);
            }
        }).show();
    }

    public static void openLocationInfo(final BaseActivity baseActivity){
        new AlertDialog.Builder(baseActivity).setTitle("提示").setMessage("此功能需要打开位置信息，是否跳转？")
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
                baseActivity.startActivityForResult(intent, 100);
            }
        }).show();
    }
    public static void checkPermissionAndListenAndrequstPermission(BaseActivity baseActivity) {
        if( baseActivity.targetPermission.size()>0){
            if (!checkPermissionAndListen(baseActivity)) {
                requstPermission(baseActivity);
            }
        }

    }
}
