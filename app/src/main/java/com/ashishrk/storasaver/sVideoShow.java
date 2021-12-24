package com.ashishrk.storasaver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;

import com.ashishrk.storasaver.databinding.ActivitySvideoShowBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class sVideoShow extends AppCompatActivity {

    ActivitySvideoShowBinding binding;
    private InterstitialAd mInterstitialAd;
    private String TAG = "TAG";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mInterstitialAd != null) {
            mInterstitialAd.show(sVideoShow.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svideo_show);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_svideo_show);


        Intent intent = getIntent();
        String uri= intent.getStringExtra("VIDEO FILE");
        binding.videoViews.setVideoPath(uri);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(binding.videoViews);
        Uri uri1 = Uri.parse(uri);
        binding.videoViews.setMediaController(mediaController);
        binding.videoViews.setVideoURI(uri1);
        binding.videoViews.requestFocus();
        binding.videoViews.start();




        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-9422323438961177/9834166683", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });


    }


}
