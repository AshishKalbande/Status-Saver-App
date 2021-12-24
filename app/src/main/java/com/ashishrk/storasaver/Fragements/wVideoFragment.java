package com.ashishrk.storasaver.Fragements;

import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashishrk.storasaver.Adapters.wVideoAdapter;
import com.ashishrk.storasaver.Constant;
import com.ashishrk.storasaver.Models.WModel;
import com.ashishrk.storasaver.R;
import com.ashishrk.storasaver.databinding.FragmentWVideoBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class wVideoFragment extends Fragment {

    private FragmentWVideoBinding binding;
    private ArrayList<WModel> list = new ArrayList<>();;
    private wVideoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_w_video,container,false);



        list.clear();
        getData();
        binding.refresh2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.refresh2.setRefreshing(true);
                list.clear();
                getData();{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.refresh2.setRefreshing(false);
                        }
                    },1000);
                }
            }
        });

        return binding.getRoot();
    }



    private void getData() {

        WModel model;

        // this is simple watsp getpath
        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                Constant.W_SourceDir;
        File targetDirector = new File(targetPath);
        File[] allFiles = targetDirector.listFiles();

        //Business watsp get path
        String targetPathBusiness = Environment.getExternalStorageDirectory().getAbsolutePath() +
                Constant.WB_SourceDir;
        File targetDirectorBusiness = new File(targetPathBusiness);
        File[] allFilesBisiness = targetDirectorBusiness.listFiles();

        if (allFiles != null && allFiles.length > 0) {
            Arrays.sort(allFiles, ((o1, o2) -> {
                if (o1.lastModified() > o2.lastModified()) return -1;
                else if (o1.lastModified() < o2.lastModified()) return +1;
                else return 0;
            }));
            for (int i = 0; i < allFiles.length; i++) {
                File file = allFiles[i];
                //chekc img or video
                if (Uri.fromFile(file).toString().endsWith(".mp4")) {
                    model = new WModel("whats" + i,
                            Uri.fromFile(file),
                            allFiles[i].getAbsolutePath(),
                            file.getName());
                    list.add(model);
                }
            }
        }


        if (allFilesBisiness != null && allFilesBisiness.length > 0) {
            Arrays.sort(allFilesBisiness, ((o1, o2) -> {
                if (o1.lastModified() > o2.lastModified()) return -1;
                else if (o1.lastModified() < o2.lastModified()) return +1;
                else return 0;
            }));



            for (int i = 0; i < allFilesBisiness.length; i++) {
                File file = allFilesBisiness[i];
                if (Uri.fromFile(file).toString().endsWith(".mp4")) {
                    model = new WModel("whatsBusiness" + i,
                            Uri.fromFile(file),
                            allFilesBisiness[i].getAbsolutePath(),
                            file.getName());
                    list.add(model);
                }
            }
        }

        adapter = new wVideoAdapter(list, getActivity());
        binding.wtspRecyclerView.setAdapter(adapter);

    }

}
