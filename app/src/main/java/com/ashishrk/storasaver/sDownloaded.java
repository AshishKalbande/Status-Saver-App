package com.ashishrk.storasaver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import com.ashishrk.storasaver.Adapters.DownloadAdapter;
import com.ashishrk.storasaver.Models.DModel;
import com.ashishrk.storasaver.databinding.ActivitySdownloadedBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static android.os.Build.VERSION.SDK_INT;

public class sDownloaded extends AppCompatActivity {

    ActivitySdownloadedBinding binding;
    int requestCode = 358;

    private ArrayList<DModel> Dlist = new ArrayList<>();
    private DownloadAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sdownloaded);


        getData();
        chekpermision();
        binding.Swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.Swipe.setRefreshing(true);
                Dlist.clear();
                getData();{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.Swipe.setRefreshing(false);
                        }
                    },1000);
                }
            }
        });

        //admob
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        binding.adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                super.onAdFailedToLoad(adError);
                binding.adView.loadAd(adRequest);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                super.onAdClicked();
                //ad clicked visibility code likhe
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        // end admob code

        binding.bottomNav.setSelectedItemId(R.id.downloaded);
        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {

                switch (item.getItemId()){
                    case R.id.downloaded:
                        return true;

                    case R.id.StatusSaver:
                        startActivity(new Intent(getApplicationContext()
                                ,MainWtsp.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.iSaver:
                        startActivity(new Intent(getApplicationContext()
                                ,MainInsta.class));
                        overridePendingTransition(0,0);
                        return true;


                }
                return false;
            }
        });

        binding.ImgPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(sDownloaded.this,PrivacyPolicy.class));
//                Toast.makeText(sDownloaded.this, "Privcy", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chekpermision() {
        if (SDK_INT>23){
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
            {
                //maincode;
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){

            }
            else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.
                        permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},requestCode);
            }
        }
//        else {
//            Toast.makeText(getApplicationContext(),"Already",Toast.LENGTH_SHORT).show();
//        }
    }

    private void getData() {

        DModel model;

        // this is simple watsp getpath
        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.App_DIR;
        File targetDirector = new File(targetPath);
        File[] allFiles = targetDirector.listFiles();

        //sorting list Simple Watsp for img   // the 01 is simple wtsp and 02 is business watsup
        if(allFiles != null && allFiles.length>0) {
            Arrays.sort(allFiles, ((o1, o2) -> {
                if (o1.lastModified() > o2.lastModified()) return -1;
                else if (o1.lastModified() < o2.lastModified()) return +1;
                else return 0;
            }));

            for (int i = 0; i < allFiles.length; i++) {
                File file = allFiles[i];

                    model = new DModel("whats" + i,
                            Uri.fromFile(file),
                            allFiles[i].getAbsolutePath(),
                            file.getName());
                    Dlist.add(model);

            }
        }



        binding.recylerView.setLayoutManager(new GridLayoutManager(this,3));
        adapter = new DownloadAdapter(Dlist,sDownloaded.this);
        binding.recylerView.setAdapter(adapter);
    }
}