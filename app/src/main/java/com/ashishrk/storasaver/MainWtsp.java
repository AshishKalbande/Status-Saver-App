package com.ashishrk.storasaver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ashishrk.storasaver.Fragements.wImageFragment;
import com.ashishrk.storasaver.Fragements.wVideoFragment;
import com.ashishrk.storasaver.databinding.ActivityMainWtspBinding;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION.SDK_INT;

public class MainWtsp extends AppCompatActivity {

    private ActivityMainWtspBinding binding;
    private MainWtsp activity;
    private ViewpagerAdapter adapter;
    int requestCode = 358;
    private final int UPDATE_REQUEST_CODE = 55;
    Dialog dialog;
    String TAG = "TAG";
    private AppUpdateManager mAppUdateManager;

    @Override
    protected void onResume() {
        InAppUpdate();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main_wtsp);


        activity = this;

        // in app update
        InAppUpdate();


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
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

        Constant.CheckFolder();
        binding.StoraTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(MainWtsp.this);
                dialog.setContentView(R.layout.custome_stora_tutorial);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                dialog.show();

                dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

            }
        });




        // bottom navigation
        binding.bottomNav.setSelectedItemId(R.id.home);
        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {

                switch (item.getItemId()){
                    case R.id.StatusSaver:
                        return true;

                    case R.id.iSaver:
                        startActivity(new Intent(getApplicationContext()
                                ,MainInsta.class));
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
        // bottom navigation

        chekpermision();
        initView();

        binding.ShareStoraApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Stora Saver");
                    String shareLink = "https://play.google.com/store/app/details?id="+BuildConfig.APPLICATION_ID+"\n\n";
                    intent.putExtra(Intent.EXTRA_TEXT,shareLink);
                    startActivity(Intent.createChooser(intent,"share by"));
                }catch (Exception e){
                    Toast.makeText(activity, "Error occured ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void chekpermision() {
        if (SDK_INT>23){
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
            {

            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){


            }
            else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.
                        permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},requestCode);
            }
        }
        else {
           //
            }
    }

    private void initView() {
        adapter = new ViewpagerAdapter(activity.getSupportFragmentManager(),activity.getLifecycle());
        adapter.addFragment(new wImageFragment(),"Images");
        adapter.addFragment(new wVideoFragment(),"Video");

        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(1);

        new TabLayoutMediator(binding.tabLayout,binding.viewPager,
                (tab, position) -> {
                    tab.setText(adapter.fragmentsTitleList.get(position));
                }).attach();

        for (int i= 0; i<binding.tabLayout.getTabCount(); i++){
            TextView tv = (TextView) LayoutInflater.from(activity)
                    .inflate(R.layout.custom_tab, null);

            binding.tabLayout.getTabAt(i).setCustomView(tv);
        }
    }

    class ViewpagerAdapter extends FragmentStateAdapter {

        private final List<Fragment> fragmentsList = new ArrayList<>();
        private final List<String> fragmentsTitleList = new ArrayList<>();

        public ViewpagerAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        public void addFragment(Fragment fragment, String title){
            fragmentsList.add(fragment);
            fragmentsTitleList.add(title);
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return  fragmentsList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentsList.size();
        }
    }


    // in app update

    private void InAppUpdate(){
        mAppUdateManager = AppUpdateManagerFactory.create(this);
        mAppUdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && result.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE))
                {
                    try {
                        mAppUdateManager.startUpdateFlowForResult(result,AppUpdateType.FLEXIBLE,MainWtsp.this,UPDATE_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mAppUdateManager.registerListener(installStateUpdatedListener);

    }

    private InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState state) {
            if (state.installStatus() == InstallStatus.DOWNLOADED){
                showCompleteUpdate();
            }
        }
    };

    @Override
    protected void onStop() {
        if (mAppUdateManager!=null) mAppUdateManager.unregisterListener(installStateUpdatedListener);
        super.onStop();
    }



    private void showCompleteUpdate() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"New Update Available!",
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Install", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAppUdateManager.completeUpdate();
            }
        });
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == UPDATE_REQUEST_CODE && resultCode != RESULT_OK){
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}