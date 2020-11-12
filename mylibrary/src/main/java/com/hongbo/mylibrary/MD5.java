package com.hongbo.mylibrary;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	public static String pwdMD5(String pwdString)
	{
		if(pwdString==null)return"";
		 StringBuffer buf = new StringBuffer("");
		try 
		{ 
		   MessageDigest md = MessageDigest.getInstance("MD5");
		   md.update(pwdString.getBytes()); 
		   byte b[] = md.digest(); 
		   int i;   
		for (int offset = 0; offset < b.length; offset++)
		{ 
		  i = b[offset]; 
		  if(i<0) i+= 256; 
		  if(i<16) 
		  buf.append("0"); 
		  buf.append(Integer.toHexString(i));
		} 
		} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block 
			Log.i("wo", e.toString());
		e.printStackTrace(); 
		} 
		return  buf.toString();
	}
}