package com.hongbo.mylibrary;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyProxy implements InvocationHandler {
    private OnRequestPermissionsResultListenter onRequestPermissionsResultListenter;
    private BaseActivity baseActivity;

    public void setBaseActivity(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public void setOnRequestPermissionsResultListenter(OnRequestPermissionsResultListenter onRequestPermissionsResultListenter) {
        this.onRequestPermissionsResultListenter = onRequestPermissionsResultListenter;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         Boolean a1=(Boolean)args[0];
         if(a1){
             baseActivity.targetPermission.remove(0);
             if(baseActivity.targetPermission.size()>0){
                 PermissionUtils.checkPermissionAndListenAndrequstPermission(baseActivity);
             }else {
                 return method.invoke(onRequestPermissionsResultListenter,args);
             }
         }else {
            if(baseActivity.status==BaseActivity.STATUS1){
                baseActivity.targetPermission.remove(0);
                if(baseActivity.targetPermission.size()>0){
                    PermissionUtils.checkPermissionAndListenAndrequstPermission(baseActivity);
                }else {
                    return method.invoke(onRequestPermissionsResultListenter,args);
                }
            }else{
                baseActivity.targetPermission.clear();
                return method.invoke(onRequestPermissionsResultListenter,args);
            }
         }
        return null;
    }

    public Object getProxy(){
       return Proxy.newProxyInstance(onRequestPermissionsResultListenter.getClass().getClassLoader(),onRequestPermissionsResultListenter.getClass().getInterfaces(),this);
    }
}
