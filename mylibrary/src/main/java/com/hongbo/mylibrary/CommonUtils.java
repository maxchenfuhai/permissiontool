package com.hongbo.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class CommonUtils {
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            int versionCode = info.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static void toast(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    public static int dip2px(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    public static String convertStorage(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    //获取窗口截图
    public static Bitmap snapShotWithStatusBar(Activity activity,int screenHeight) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        Bitmap bp = Bitmap.createBitmap(bmp, 0, 0, width, screenHeight);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 压缩图片
     * @param reqWidth 控件宽度
     * @param reqHeight 控件高度
     * @param path  图片文件路径
     * @return
     */
    public static Bitmap filterImage(int reqWidth, int reqHeight, String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int inSampleSize = 1;
        if (reqWidth < options.outWidth || reqHeight < options.outHeight) {
            float bi1 = (float) options.outWidth / (float) reqWidth;
            float bi2 = (float) options.outHeight / (float) reqHeight;
            inSampleSize = (int) Math.ceil(Math.max(bi1, bi2));
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }
}
