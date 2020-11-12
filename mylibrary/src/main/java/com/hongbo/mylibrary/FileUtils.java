package com.hongbo.mylibrary;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

public class FileUtils {

    //建立一个文件类型与文件后缀名的匹配表
    private static final String[][] MATCH_ARRAY = {
            //{后缀名，    文件类型}
            {
                    ".3gp", "video/3gpp"
            },


            {
                    ".apk", "application/vnd.android.package-archive"
            },


            {
                    ".asf", "video/x-ms-asf"
            },


            {
                    ".avi", "video/x-msvideo"
            },


            {
                    ".bin", "application/octet-stream"
            },


            {
                    ".bmp", "image/bmp"
            },


            {
                    ".c", "text/plain"
            },


            {
                    ".class", "application/octet-stream"
            },


            {
                    ".conf", "text/plain"
            },


            {
                    ".cpp", "text/plain"
            },


            {
                    ".doc", "application/msword"
            },


            {
                    ".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"//application/vnd.openxmlformats-officedocument.wordprocessingml.document
            },

            {
                    ".xls", "application/vnd.ms-excel"
            },


            {
                    ".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"// application/x-excel
            },


            {
                    ".exe", "application/octet-stream"
            },


            {
                    ".gif", "image/gif"
            },


            {
                    ".gtar", "application/x-gtar"
            },


            {
                    ".gz", "application/x-gzip"
            },


            {
                    ".h", "text/plain"
            },


            {
                    ".htm", "text/html"
            },


            {
                    ".html", "text/html"
            },


            {
                    ".jar", "application/java-archive"
            },


            {
                    ".java", "text/plain"
            },


            {
                    ".jpeg", "image/jpeg"
            },


            {
                    ".jpg", "image/jpeg"
            },


            {
                    ".js", "application/x-javascript"
            },


            {
                    ".log", "text/plain"
            },


            {
                    ".m3u", "audio/x-mpegurl"
            },


            {
                    ".m4a", "audio/mp4a-latm"
            },


            {
                    ".m4b", "audio/mp4a-latm"
            },


            {
                    ".m4p", "audio/mp4a-latm"
            },


            {
                    ".m4u", "video/vnd.mpegurl"
            },


            {
                    ".m4v", "video/x-m4v"
            },


            {
                    ".mov", "video/quicktime"
            },


            {
                    ".mp2", "audio/x-mpeg"
            },


            {
                    ".mp3", "audio/x-mpeg"
            },


            {
                    ".mp4", "video/mp4"
            },


            {
                    ".mpc", "application/vnd.mpohun.certificate"
            },


            {
                    ".mpe", "video/mpeg"
            },


            {
                    ".mpeg", "video/mpeg"
            },


            {
                    ".mpg", "video/mpeg"
            },


            {
                    ".mpg4", "video/mp4"
            },


            {
                    ".mpga", "audio/mpeg"
            },


            {
                    ".msg", "application/vnd.ms-outlook"
            },


            {
                    ".ogg", "audio/ogg"
            },


            {
                    ".pdf", "application/pdf"
            },


            {
                    ".png", "image/png"
            },


            {
                    ".pps", "application/vnd.ms-powerpoint"
            },


            {
                    ".ppt", "application/vnd.ms-powerpoint"
            },


            {
                    ".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"
            },


            {
                    ".prop", "text/plain"
            },


            {
                    ".rc", "text/plain"
            },


            {
                    ".rmvb", "audio/x-pn-realaudio"
            },


            {
                    ".rtf", "application/rtf"
            },


            {
                    ".sh", "text/plain"
            },


            {
                    ".tar", "application/x-tar"
            },


            {
                    ".tgz", "application/x-compressed"
            },


            {
                    ".txt", "text/plain"
            },


            {
                    ".wav", "audio/x-wav"
            },


            {
                    ".wma", "audio/x-ms-wma"
            },


            {
                    ".wmv", "audio/x-ms-wmv"
            },


            {
                    ".wps", "application/vnd.ms-works"
            },


            {
                    ".xml", "text/plain"
            },


            {
                    ".z", "application/x-compress"
            },


            {
                    ".zip", "application/x-zip-compressed"
            },


            {
                    "", "*/*"
            }
    };
    public final static int INSERT_IMAGE=1;
    public final static int INSERT_FILE=2;

    public static String getReadFileName(String fileName){
        if(fileName.contains(".")) fileName=fileName.substring(0,fileName.lastIndexOf("."));
        if(fileName.contains("("))fileName=fileName.substring(0,fileName.indexOf("("));
        return fileName;
    }
    public static Uri insertDocument(String name, long length, Context context, String relatePath, int type) {
        deleteFileInDbRecord(context, MediaStore.Files.FileColumns.DISPLAY_NAME + " like '"+getReadFileName(name)+"%'",type);
        ContentValues map = new ContentValues();
        ContentResolver contentResolver = context.getContentResolver();
        Uri  external = MediaStore.Files.getContentUri("external");
        if(type==INSERT_IMAGE){
            external =  MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        long timeSave = System.currentTimeMillis();
        map.put(MediaStore.Files.FileColumns.DISPLAY_NAME, name);
        map.put(MediaStore.Files.FileColumns.DATE_TAKEN, timeSave+"");
        map.put(MediaStore.Files.FileColumns.DATE_ADDED, timeSave+"");
        if (Build.VERSION.SDK_INT >= 29) {
            map.put(MediaStore.Files.FileColumns.RELATIVE_PATH, relatePath);
        } else {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/" + relatePath);
            if (!file.exists()) file.mkdirs();
            map.put(MediaStore.Files.FileColumns.DATA, Environment.getExternalStorageDirectory().getAbsolutePath()+"/" + relatePath + "/" + name);
        }
        map.put(MediaStore.Files.FileColumns.SIZE,length);
        map.put(MediaStore.Files.FileColumns.MIME_TYPE, getMine(name));
        return contentResolver.insert(external, map);
    }


    public static String getMine(String name){
        String mine = "";
        for (int i = 0; i < MATCH_ARRAY.length; i++) {
            if (name.toLowerCase().endsWith(MATCH_ARRAY[i][0])) {
                mine = MATCH_ARRAY[i][1];
                break;
            }
        }
        return mine;
    }


    public static Cursor getDbCursor(Context context,Uri uri,String[] projection,String selection,String[] selectionArgs,String sortOrder){
        ContentResolver mContentResolver = context.getContentResolver();
        Cursor mCursor = mContentResolver.query(uri,
                projection, selection, selectionArgs,
                sortOrder);
        return mCursor;
    }

    public static void deleteFileInDbRecord(Context context,String selection,int type){
        Cursor cursor= null;
        try{
            Uri external = MediaStore.Files.getContentUri("external");
            if(type==INSERT_IMAGE){
                external =  MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            }
            cursor=getDbCursor(context,external,null,selection,null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    long fileId = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
                    String uri = external
                            .buildUpon()
                            .appendPath(String.valueOf(fileId)).build().toString();
                    context.getContentResolver().delete(Uri.parse(uri), null, null);

                }
            }
            cursor.close();
        }catch (Exception e){

        }finally {
            if(cursor!=null)cursor.close();
        }

    }
}
