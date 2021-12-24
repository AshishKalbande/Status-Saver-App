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
import com.ashishrk.storasaver.sVideoShow;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class wVideoAdapter extends RecyclerView.Adapter<wVideoAdapter.wHolder> {

    private ArrayList<WModel> list;
    private Context context;
    private LayoutInflater inflater;

    public wVideoAdapter(ArrayList<WModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public wHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        return new wHolder(DataBindingUtil.inflate(inflater, R.layout.w_items, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull  wVideoAdapter.wHolder holder, int position) {
        final WModel item = list.get(position);
        if (item.getUri().toString().endsWith(".mp4"))
            holder.binding.playButton.setVisibility(View.VISIBLE);
        else holder.binding.playButton.setVisibility(View.GONE);
        Glide.with(context)
                .load(item.getPath())
                .into(holder.binding.statusImg);


        final String path = list.get(position).getPath();
        final File file = new File(path);
        holder.binding.download.setOnClickListener(DownloadListner.downloadItem(file));

        holder.binding.share.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/jpg");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(item.getPath()));
            context.startActivity(Intent.createChooser(shareIntent, "Share image"));

        });

        holder.binding.statusImg.setOnClickListener(v -> {
            context.startActivity(new Intent(context, sVideoShow.class)
                    .putExtra("VIDEO FILE",item.getPath()));
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class wHolder extends RecyclerView.ViewHolder{

        WItemsBinding binding;

        public wHolder(WItemsBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

    }
}
