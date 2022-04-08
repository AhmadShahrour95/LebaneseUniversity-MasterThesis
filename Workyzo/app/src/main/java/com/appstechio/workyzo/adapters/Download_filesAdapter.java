package com.appstechio.workyzo.adapters;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appstechio.workyzo.R;
import com.appstechio.workyzo.databinding.DownloadfilesitemsRcvBinding;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.google.type.Color;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class Download_filesAdapter extends RecyclerView.Adapter<Download_filesAdapter.DownloadFilesViewHolder> {


    private ArrayList<HashMap> SelectedFiles_list;

    private PreferenceManager preferenceManager;

    public Download_filesAdapter(ArrayList<HashMap> selectedFiles_list) {
        SelectedFiles_list = selectedFiles_list;
    }

    @NonNull
    @Override
    public DownloadFilesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DownloadFilesViewHolder(DownloadfilesitemsRcvBinding.inflate(LayoutInflater.from(parent.getContext()),parent
                ,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadFilesViewHolder holder, int position) {

        holder.downloadfilesitemsRcvBinding.SelectedFileName.setText(SelectedFiles_list.get(position).get("FileName").toString());
        holder.downloadfilesitemsRcvBinding.SelectedFileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              int Clicked_position =  holder.getBindingAdapterPosition();

                File direct = new File(Environment.getExternalStorageDirectory()
                        + "/Workyzo_files");

                if (!direct.exists()) {
                    direct.mkdirs();
                }

                DownloadManager manager = (DownloadManager) holder.downloadfilesitemsRcvBinding.getRoot().getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(SelectedFiles_list.get(position).get("File_url").toString());
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setDescription("Downloading a file...");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setTitle(holder.downloadfilesitemsRcvBinding.SelectedFileName.getText().toString());
                request.setVisibleInDownloadsUi(true);
                request.allowScanningByMediaScanner();
                request.setDestinationInExternalPublicDir("/Workyzo_files",holder.downloadfilesitemsRcvBinding.SelectedFileName.getText().toString());
                long reference = manager.enqueue(request);
            }
        });

    }



    @Override
    public int getItemCount() {
        return SelectedFiles_list.size();
    }


    public static class DownloadFilesViewHolder extends RecyclerView.ViewHolder{

        private final DownloadfilesitemsRcvBinding downloadfilesitemsRcvBinding;


        public DownloadFilesViewHolder(DownloadfilesitemsRcvBinding downloadfilesitemsRcvBinding) {
            super(downloadfilesitemsRcvBinding.getRoot());
            this.downloadfilesitemsRcvBinding = downloadfilesitemsRcvBinding;
        }



    }
}
