package com.appstechio.workyzo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appstechio.workyzo.databinding.SelectedfilesitemsRcvBinding;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Selected_filesAdapter extends RecyclerView.Adapter<Selected_filesAdapter.SelectedFilesViewHolder> {


    private ArrayList<HashMap> SelectedFiles_list;

    private PreferenceManager preferenceManager;

    public Selected_filesAdapter(ArrayList<HashMap> selectedFiles_list) {
        SelectedFiles_list = selectedFiles_list;
    }

    @NonNull
    @Override
    public SelectedFilesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SelectedFilesViewHolder(SelectedfilesitemsRcvBinding.inflate(LayoutInflater.from(parent.getContext()),parent
                ,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedFilesViewHolder holder, int position) {

        holder.selectedfilesitemsRcvBinding.SelectedFileName.setText(SelectedFiles_list.get(position).get("FileName").toString());
        holder.selectedfilesitemsRcvBinding.SelectedFileSize.setText(SelectedFiles_list.get(position).get("FileSize").toString());
        holder.selectedfilesitemsRcvBinding.DeleteFileSelectedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              int Clicked_position =  holder.getBindingAdapterPosition();
              notifyItemRemoved(Clicked_position);
                Constants.Files_Map = SelectedFiles_list;
                Constants.Files_Map.remove(Clicked_position);
              notifyItemRangeChanged(Clicked_position,SelectedFiles_list.size());
            }
        });

    }



    @Override
    public int getItemCount() {
        return SelectedFiles_list.size();
    }


    public static class SelectedFilesViewHolder extends RecyclerView.ViewHolder{

        private final SelectedfilesitemsRcvBinding selectedfilesitemsRcvBinding;


        public SelectedFilesViewHolder(SelectedfilesitemsRcvBinding selectedfilesitemsRcvBinding) {
            super(selectedfilesitemsRcvBinding.getRoot());
            this.selectedfilesitemsRcvBinding = selectedfilesitemsRcvBinding;
        }



    }
}
