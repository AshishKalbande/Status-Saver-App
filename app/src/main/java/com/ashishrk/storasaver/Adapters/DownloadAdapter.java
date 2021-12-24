package com.ashishrk.storasaver.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ashishrk.storasaver.Models.DModel;
import com.ashishrk.storasaver.R;
import com.ashishrk.storasaver.databinding.DownloadedItemBinding;
import com.ashishrk.storasaver.sImageShow;
import com.ashishrk.storasaver.sVideoShow;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadHolder> {


    private ArrayList<DModel> Dlist;
    private Context context;

    public DownloadAdapter(ArrayList<DModel> list, Context context) {
        this.Dlist = list;
        this.context = context;
    }

    @NonNull
    @Override
    public DownloadHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        return new DownloadHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.downloaded_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull DownloadAdapter.DownloadHolder holder, int position) {
        DModel item = Dlist.get(position);


        //play button show code
        if (item.getUri().toString().endsWith(".mp4"))
            holder.binding.Dplay.setVisibility(View.VISIBLE);
        else holder.binding.Dplay.setVisibility(View.GONE);

        Glide.with(context)
                .load(item.getPath())
                .into(holder.binding.DownloadImg);

        holder.binding.share.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/jpg");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(item.getPath()));
            context.startActivity(Intent.createChooser(shareIntent, "Share image"));

        });

        holder.binding.DownloadImg.setOnClickListener(v -> {

            if (item.getPath().endsWith(".mp4")){
                context.startActivity(new Intent(context, sVideoShow.class)
                        .putExtra("VIDEO FILE",item.getPath()));
            }else {
                context.startActivity(new Intent(context, sImageShow.class)
                        .putExtra("file", item.getPath()));
            }

        });


        //start download code

        holder.binding.delete.setOnClickListener(v -> {
            final String path = item.getPath();
            final File file = new File(path);
            if (file.exists()) {
                boolean del = file.delete();
                Dlist.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, Dlist.size());
                notifyDataSetChanged();
                Toast.makeText(context, "File was Deleted", Toast.LENGTH_SHORT).show();
                if(del){
                    MediaScannerConnection.scanFile(
                            context,
                            new String[]{ path, path},
                            new String[]{ "image/jpg/png","video/mp4"},
                            new MediaScannerConnection.MediaScannerConnectionClient()
                            {
                                public void onMediaScannerConnected()
                                {
                                }
                                public void onScanCompleted(String path, Uri uri)
                                {
                                    Log.d("Video path: ",path);
                                }
                            });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return Dlist.size();
    }

    public class DownloadHolder extends RecyclerView.ViewHolder{

        DownloadedItemBinding binding;
        public DownloadHolder(DownloadedItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

}
