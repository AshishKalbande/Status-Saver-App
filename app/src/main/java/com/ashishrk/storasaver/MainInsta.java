package com.ashishrk.storasaver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ashishrk.storasaver.databinding.ActivityMainInstaBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hcr2bot.instagramvideosdownloader.InstaVideo;

public class MainInsta extends AppCompatActivity {


    ActivityMainInstaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_insta);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_insta);

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        binding.IgPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);
                String output = item.getText().toString();
                if (!output.isEmpty()){
                    binding.IgUrl.setText(output);
                }else {
                    Toast.makeText(MainInsta.this,"No Text",Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.IgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                InstaVideo.downloadVideo(MainInsta.this, binding.IgUrl.getText().toString());
                InstaVideo.downloadVideo(MainInsta.this, binding.IgUrl.getText().toString(), "/Stora/");


            }
        });


        binding.bottomNav.setSelectedItemId(R.id.iSaver);
        // perform ItemSelectedListner
        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {

                switch (item.getItemId()){
                    case R.id.iSaver:
                        return true;

                    case R.id.StatusSaver:
                        startActivity(new Intent(getApplicationContext()
                                ,MainWtsp.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.downloaded:
                        startActivity(new Intent(getApplicationContext()
                                ,sDownloaded.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

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



    }
}