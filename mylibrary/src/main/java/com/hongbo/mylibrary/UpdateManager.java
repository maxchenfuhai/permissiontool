package com.hongbo.mylibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateManager extends AsyncTask<Void, Integer, String> implements View.OnClickListener {
    private long totallen;
    private long length;
    private int progress;
    private ProgressBar progressBar;
    private Activity context;
    private String filepath;
    private TextView progressTextView;
    private String fileName, downloadUrl;
    private File saveDir;
    Dialog updatedialog;
    View head, foot;

    public UpdateManager(Activity context, File saveDir, String downloadUrl, String fileName) {
        this.context = context;
        this.saveDir = saveDir;
        this.downloadUrl = downloadUrl;
        this.fileName = fileName;

    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(downloadUrl)
                    .openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                if (!saveDir.exists())
                    saveDir.mkdirs();
                filepath = new File(saveDir, fileName).getAbsolutePath();
                FileOutputStream outStream = new FileOutputStream(filepath);
                InputStream inputStream = conn.getInputStream();
                length = conn.getContentLength();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                    totallen += len;
                    progress = (int) (totallen * 100 / length);
                    publishProgress(progress);
                }
                inputStream.close();
                outStream.close();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            return "error";
        }
        return progress + "";
    }

    @Override
    protected void onPostExecute(String s) {
        if (updatedialog != null) updatedialog.cancel();
        if ("error".equals(s)) {
            if (!context.isFinishing())
                new AlertDialog.Builder(context).setTitle("提示").setMessage("网络异常！请检查网络配置是否正确？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        context.finish();
                    }
                }).show();
        } else {
            installApk();
            context.finish();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressBar.setProgress(values[0]);
        progressTextView.setText(values[0] + "%");
    }

    private void installApk() {
        File apkfile = new File(filepath);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        Uri apkUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            apkUri = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".fileprovider",
                    apkfile);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            apkUri = Uri.parse("file://" + apkfile.toString());
        }
        i.setDataAndType(apkUri, "application/vnd.android.package-archive");
        context.startActivity(i);
    }

    public void buildUpdateDialog(String verName, String updateContent) {
        View mFloatLayout = context.getLayoutInflater().inflate(R.layout.update_dialog, null);
        TextView updatelog = mFloatLayout.findViewById(R.id.updatelog);
        mFloatLayout.findViewById(R.id.btn_update).setOnClickListener(this);
        mFloatLayout.findViewById(R.id.btn_cancle).setOnClickListener(this);
        head = mFloatLayout.findViewById(R.id.update_head);
        foot = mFloatLayout.findViewById(R.id.update_foot);
        TextView versionname = (TextView) mFloatLayout.findViewById(R.id.versionname);
        versionname.setText(verName);

        progressBar = mFloatLayout.findViewById(R.id.updateprogressBar);
        progressTextView = mFloatLayout.findViewById(R.id.progress);
        updatelog.setText(updateContent);

        updatedialog = new Dialog(context, R.style.mydialog);
        updatedialog.setContentView(mFloatLayout);
        updatedialog.setCancelable(false);
        updatedialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_update) {
            head.setVisibility(View.GONE);
            foot.setVisibility(View.VISIBLE);
            execute();
        } else if (v.getId() == R.id.btn_cancle) {
            updatedialog.cancel();
            context.finish();
        }
    }
}
