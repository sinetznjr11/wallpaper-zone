package com.sinetcodes.wallpaperzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.Locale;

import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    /*
    todo: folder not created in internal storage.
     */
    private static final String TAG = "TestActivity";
    private static final int WRITE_STORAGE_CODE=001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        String url="https://images.unsplash.com/photo-1562886889-0d7ec2bc333e?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb";

        if (ContextCompat.checkSelfPermission(TestActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(TestActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_CODE);
        } else {
            createFolder();
        }








        //String dirPath = getRootDirPath(getApplicationContext());
        //Log.d(TAG, "onCreate: "+dirPath);
       /* int downloadId = PRDownloader.download(url, dirPath, "wmaze_file_id.jpg")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        Log.d(TAG, "onStartOrResume: ");
                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        //Log.d(TAG, "onProgress: "+progress.currentBytes+" of "+progress.totalBytes);
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                    }
                    @Override
                    public void onError(Error error) {

                    }

                });

        Status status = PRDownloader.getStatus(downloadId);
        if(status==Status.COMPLETED){
            Log.d(TAG, "onCreate: Completed.");
        }
*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case WRITE_STORAGE_CODE:
                //createFolder();
                break;
            default:
                break;
        }
    }

    public void createFolder(){
        //String path = Environment.getDataDirectory().getAbsolutePath() + "/storage/emulated/0";
        File wmazeFolder=new File(Environment.getExternalStorageDirectory().toString()+"/wmaze");
        if(!wmazeFolder.exists()){
            if(wmazeFolder.mkdir())
                Log.d(TAG, "onCreate: Directory created "+wmazeFolder.getAbsolutePath());
            else
                Log.d(TAG, "onCreate: Directory not created "+wmazeFolder.getAbsolutePath());


        }else{
            Log.d(TAG, "createFolder: Directory already exists."+wmazeFolder.getAbsolutePath());
        }

    }

    public static String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(),
                    null)[0];
            return file.getAbsolutePath();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }

    public static String getProgressDisplayLine(long currentBytes, long totalBytes) {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes);
    }

    private static String getBytesToMBString(long bytes){
        return String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00));
    }
}
