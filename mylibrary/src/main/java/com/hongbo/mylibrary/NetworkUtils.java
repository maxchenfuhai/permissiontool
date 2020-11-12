package com.hongbo.mylibrary;

import android.app.Activity;
import android.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

public class NetworkUtils {

	public static void access(String url, final Map<String, String> param, final Activity context, final OnResponse onResponse, int method){
		RequestQueue mQueue=Volley.newRequestQueue(context);
		StringRequest stringRequest = new StringRequest(method,
				url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						onResponse.onSuccess(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						onResponse.onFail();
						String errorStr="网络异常，请检查网络是否连接！";
						if(error!=null&&error.networkResponse!=null&&error.networkResponse.statusCode==500){
							errorStr="服务端出错，请联系客服！";
						}
						if(!context.isFinishing())new AlertDialog.Builder(context).setTitle("提示").setMessage(errorStr).setPositiveButton("确定",null).show();
					}
				}){
			 @Override  
			    protected Map<String, String> getParams() throws AuthFailureError {  
			        return param;  
			    }  
		};
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(90000,0,1f));
		stringRequest.setShouldCache(false);
		mQueue.add(stringRequest);
	}
	
	
	public interface OnResponse{
		void onSuccess(String response);
		void onFail();
	}
}
