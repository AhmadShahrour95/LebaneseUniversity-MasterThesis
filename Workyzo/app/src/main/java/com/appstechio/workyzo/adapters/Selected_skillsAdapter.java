package com.appstechio.workyzo.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.appstechio.workyzo.utilities.PreferenceManager;
import com.appstechio.workyzo.databinding.SelectedskillsitemsRcvBinding;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Selected_skillsAdapter extends RecyclerView.Adapter<Selected_skillsAdapter.SelectedSkillsViewHolder> {


    private ArrayList<String> SelectedSkills_titles = new ArrayList<String>();

    private PreferenceManager preferenceManager;

    public Selected_skillsAdapter(ArrayList<String> selectedSkills_titles) {
        SelectedSkills_titles = selectedSkills_titles;
    }

    @NonNull
    @Override
    public SelectedSkillsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SelectedSkillsViewHolder(SelectedskillsitemsRcvBinding.inflate(LayoutInflater.from(parent.getContext()),parent
                ,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedSkillsViewHolder holder, int position) {
        holder.selectedskillsitemsRcvBinding.SelectedSkillTitle.setText(SelectedSkills_titles.get(position));

    }



    @Override
    public int getItemCount() {
        return SelectedSkills_titles.size();
    }


    public static class SelectedSkillsViewHolder extends RecyclerView.ViewHolder{

        private final SelectedskillsitemsRcvBinding selectedskillsitemsRcvBinding;


        public SelectedSkillsViewHolder(SelectedskillsitemsRcvBinding selectedskillsitemsRcvBinding) {
            super(selectedskillsitemsRcvBinding.getRoot());
            this.selectedskillsitemsRcvBinding = selectedskillsitemsRcvBinding;
        }



    }
}
