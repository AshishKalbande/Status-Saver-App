package com.ashishrk.storasaver;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class Constant {


    public static String W_SourceDir = "/WhatsApp/Media/.statuses";
    public static String WB_SourceDir = "/WhatsApp Business/Media/.statuses";

    public static String App_DIR = "/Download/Insta Video Downloader/Stora/";







    public static void CheckFolder(){
        String AppPath = Environment.getExternalStorageDirectory().getAbsolutePath() + App_DIR;
        File dir = new File(AppPath);

        boolean isDirectoryCreated = dir.exists();
        if(!isDirectoryCreated){
            isDirectoryCreated = dir.mkdir();
        }
        else {
            Log.d("Folder","Already Created");
        }
    }



}
