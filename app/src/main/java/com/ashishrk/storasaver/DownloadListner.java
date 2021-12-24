package com.ashishrk.storasaver;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;
import java.util.logging.SimpleFormatter;

public class DownloadListner {



    public static View.OnClickListener downloadItem(final File sourceFile) {
        Constant.CheckFolder();

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Runnable() {
                    Dialog dialog;

                    @Override
                    public void run() {
                        try {

                            //set date formate
                            long dateandTime = System.currentTimeMillis();
                            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm_a", Locale.getDefault());
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("-ddMMMyyyy-", Locale.getDefault());
                            String date = simpleDateFormat.format(dateandTime);
                            String time = simpleTimeFormat.format(dateandTime);

//                            File destpath  = new File(Environment.getExternalStorageDirectory().toString() + Constant.App_DIR+ "StoraSaver"+date+" "+time +sourceFile.getName());

                            final int min = 0;
                            final int max =1000;
                            final int random = new Random().nextInt((max-min)+1)+min;
                            final String randomString = String.valueOf(random);
                             File destpath  = new File(Environment.getExternalStorageDirectory().toString() + Constant.App_DIR+ "StoraSaver-"+sourceFile.getName());

                            copyFile(sourceFile, destpath);
                            dialog = new Dialog(v.getContext());
                            dialog.setContentView(R.layout.down_sucessfull);
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            dialog.setCancelable(true);
                            dialog.show();

                            MediaScannerConnection.scanFile(v.getContext(), new String[]{String.valueOf(destpath),"StoraSaver-"+sourceFile.getName()},
                                    new String[]{"*/*"}, new MediaScannerConnection.MediaScannerConnectionClient() {
                                        @Override
                                        public void onMediaScannerConnected() {

                                        }

                                        @Override
                                        public void onScanCompleted(String path, Uri uri) {

                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("Recycler", "onClick: Error:" + e.getMessage());


                        }

                    }

                }.run();

                            }


        };

    }


    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }

    }

}
