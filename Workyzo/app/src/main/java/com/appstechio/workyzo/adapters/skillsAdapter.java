package com.appstechio.workyzo.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appstechio.workyzo.R;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class skillsAdapter extends RecyclerView.Adapter<skillsAdapter.ViewHolder>{


    Context context;
    private ArrayList<String> Skills_titles = new ArrayList<String>();
    int Addskill_icon;
    public static  int i1 = 0;
    private PreferenceManager preferenceManager;
    private OnskillClickListener skillClickListener;



    public  skillsAdapter (Context c ,ArrayList<String > skills_titles, int addskill_icon, skillsAdapter.OnskillClickListener onskillClickListener){
        this.context = c;
        this.Skills_titles = skills_titles;
        this.Addskill_icon = addskill_icon;
        this.skillClickListener = onskillClickListener;
    }
    public  skillsAdapter (Context c ,ArrayList<String > skills_titles){
        this.context = c;
        this.Skills_titles = skills_titles;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.skillsitems_rcv , parent, false);
        return new skillsAdapter.ViewHolder(view,skillClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Skilltitle.setText(Skills_titles.get(position));
        holder.Addicon.setImageResource(R.drawable.ic_add);

        ArrayList<String> selectedskills_array = new ArrayList<>();
        preferenceManager = new PreferenceManager(context.getApplicationContext());
        selectedskills_array = preferenceManager.getStringArray(Constants.KEY_SKILLS);
        for(String skill : selectedskills_array){
            if(skill.equals(holder.Skilltitle.getText().toString())){
                Log.d("TAG","True");
                holder.itemView.setBackgroundColor(Color.GREEN);
                holder.Addicon.setVisibility(View.INVISIBLE);
            }else{
            }
        }


        TextView label = (TextView) ((Activity)context).findViewById(R.id.selected_skillstxt);

        if(!Constants.Skills_Array.contains(holder.Skilltitle.getText().toString())){

            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.Addicon.setVisibility(View.VISIBLE);

        }else{

            holder.itemView.setBackgroundColor(Color.GREEN);
            holder.Addicon.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int color = Color.TRANSPARENT;
                Drawable background = holder.itemView.getBackground();
                if (background instanceof ColorDrawable)
                    color = ((ColorDrawable) background).getColor();

                if (Constants.Skills_Array.size() < 20) {
                    if(!Constants.Skills_Array.contains(holder.Skilltitle.getText().toString())){
                        Constants.Skills_Array.add((holder.Skilltitle.getText().toString()));
                        //preferenceManager.putStringArray(Constants.KEY_SKILLS,skills_array);
                        label.setText(new StringBuilder().append(Constants.Skills_Array.size()).append(" out of 20 skills selected").toString());
                        holder.itemView.setBackgroundColor(Color.GREEN);
                        holder.Addicon.setVisibility(View.INVISIBLE);

                    }else{
                        Constants.Skills_Array.remove((holder.Skilltitle.getText().toString()));

                        label.setText(new StringBuilder().append(Constants.Skills_Array.size()).append(" out of 20 skills selected").toString());
                        holder.itemView.setBackgroundColor(Color.WHITE);
                        holder.Addicon.setVisibility(View.VISIBLE);
                    }


                }else{

                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return  Skills_titles.size();
    }

    //public void SetSkillsItemsnumber(ArrayList<String> newDataset) {
     //   skills_array = newDataset;
    //}

    public void filterList(ArrayList<String> filteredlist)
    {
        Skills_titles = filteredlist;
        notifyDataSetChanged();
    }
    public  interface OnskillClickListener {
       void onSkillClick (int position);

   }

    public static  class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        TextView Skilltitle;
        ImageView Addicon;
        private OnskillClickListener onskillClickListener;


        public ViewHolder(@NonNull View itemView, OnskillClickListener skillClickListener) {
            super(itemView);

            Skilltitle = itemView.findViewById(R.id.skill_title);
            Addicon = itemView.findViewById(R.id.addskill_icon);
            this.onskillClickListener = skillClickListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onskillClickListener.onSkillClick(getAdapterPosition());
        }
    }
}
