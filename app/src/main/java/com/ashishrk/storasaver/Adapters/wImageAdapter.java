package com.ashishrk.storasaver.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ashishrk.storasaver.Models.WModel;
import com.ashishrk.storasaver.R;
import com.ashishrk.storasaver.DownloadListner;
import com.ashishrk.storasaver.databinding.WItemsBinding;
import com.ashishrk.storasaver.sImageShow;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;


public class wImageAdapter extends RecyclerView.Adapter<wImageAdapter.ImgHolder> {

    private ArrayList<WModel> Imglist;
    private Context context;
    private LayoutInflater inflater;

    public wImageAdapter(ArrayList<WModel> imglist, Context context) {
        Imglist = imglist;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ImgHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        return new ImgHolder(DataBindingUtil.inflate(inflater, R.layout.w_items, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull wImageAdapter.ImgHolder holder, int position) {
        final WModel item = Imglist.get(position);
        if (item.getUri().toString().endsWith(".mp4"))
            holder.binding.playButton.setVisibility(View.VISIBLE);
        else holder.binding.playButton.setVisibility(View.GONE);
        Glide.with(context)
                .load(item.getPath())
                .into(holder.binding.statusImg);


        final String path = Imglist.get(position).getPath();
        final File file = new File(path);

        holder.binding.download.setOnClickListener(
                DownloadListner.downloadItem(file));

        holder.binding.share.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/jpg");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(item.getPath()));
            context.startActivity(Intent.createChooser(shareIntent, "Share image"));
        });

        holder.binding.statusImg.setOnClickListener(v -> {
            context.startActivity(new Intent(context, sImageShow.class)
                    .putExtra("file",item.getPath()));
        });


    }

    @Override
    public int getItemCount() {
        return Imglist.size();
    }

    public class ImgHolder extends RecyclerView.ViewHolder{

        WItemsBinding binding;
        public ImgHolder(WItemsBinding binding) {
            super(binding.getRoot());

            this.binding =binding ;
        }
    }
}
