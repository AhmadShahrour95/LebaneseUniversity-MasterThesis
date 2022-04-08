package com.appstechio.workyzo.adapters;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.PopupMenu;
import android.widget.TextView;

import com.appstechio.workyzo.R;

import com.appstechio.workyzo.databinding.ExperienceitemsRcvBinding;
import com.appstechio.workyzo.fragments.addexperience;

import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.ExperienceViewHolder> {


    private ArrayList<HashMap> Experience_list = new ArrayList<HashMap>();
    private PreferenceManager preferenceManager;
    private boolean activate;

    public ExperienceAdapter(ArrayList<HashMap> experience_list) {
        Experience_list = experience_list;
    }





    @NonNull
    @Override
    public ExperienceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new ExperienceViewHolder(ExperienceitemsRcvBinding.inflate(LayoutInflater.from(parent.getContext()),parent
                ,false));
    }


    @Override
    public void onBindViewHolder(@NonNull ExperienceViewHolder holder, int position) {
        preferenceManager = new PreferenceManager(holder.experienceitemsRcvBinding.getRoot().getContext());
        //Experience_list = preferenceManager.getMapArray(Constants.KEY_EXPERIENCES);

        //Show Menu button in recyclerview
        if (activate) {
            holder.experienceitemsRcvBinding.ExpMenuBtn.setVisibility(View.VISIBLE);
        } else {
            holder.experienceitemsRcvBinding.ExpMenuBtn.setVisibility(View.INVISIBLE);
        }

        holder.experienceitemsRcvBinding.CompanynameTxt.setText(Experience_list.get(position).get(Constants.KEY_COMPANY_NAME).toString());
        holder.experienceitemsRcvBinding.PositionTitletxt.setText(Experience_list.get(position).get(Constants.KEY_POSITION_TITLE).toString());
        if(Experience_list.get(position).get(Constants.KEY_END_DATE).equals("") ){
            holder.experienceitemsRcvBinding.PresentTxt.setText("Present");
            holder.experienceitemsRcvBinding.StartDatetxt.setText(Experience_list.get(position).get(Constants.KEY_START_DATE).toString());
        }else {

            holder.experienceitemsRcvBinding.StartDatetxt.setText(Experience_list.get(position).get(Constants.KEY_START_DATE).toString());
            holder.experienceitemsRcvBinding.EndDateTxt.setText(Experience_list.get(position).get(Constants.KEY_END_DATE).toString());
            String sDate = Experience_list.get(position).get(Constants.KEY_START_DATE).toString();
            String eDate = Experience_list.get(position).get(Constants.KEY_END_DATE).toString();

            SimpleDateFormat obj = new SimpleDateFormat("MMM,yyyy");
            try {
                Date date1 = obj.parse(sDate);
                Date date2 = obj.parse(eDate);

                // Calucalte time difference in milliseconds
                long time_difference = date2.getTime() - date1.getTime();
                long dy =  TimeUnit.MILLISECONDS.toDays(time_difference);
                // Calculate time difference in years using TimeUnit class
                long years_difference = dy / 365l;
                dy %= 365;
                // Calculate time difference in months using TimeUnit class
                long months_difference = dy / 30;

                // Calucalte time difference in days using TimeUnit class
                //long days_difference = TimeUnit.MILLISECONDS.toDays(time_difference) % 365;

                if (years_difference > 1 && months_difference > 1){
                    holder.experienceitemsRcvBinding.DateDiffTxt.setText(new StringBuilder().append("(").append(years_difference).append(" years, ").append(months_difference).append(" months)").toString());
                }else if(years_difference < 1 && months_difference < 1){
                    //DO NOTHING EMPTY
                }else if(years_difference == 1 && months_difference == 1){
                    holder.experienceitemsRcvBinding.DateDiffTxt.setText(new StringBuilder().append("(").append(years_difference).append(" year, ").append(months_difference).append(" month)").toString());
                }else if(years_difference > 1 && months_difference < 1){
                    holder.experienceitemsRcvBinding.DateDiffTxt.setText(new StringBuilder().append("(").append(years_difference).append(" years)").toString());
                }else if(years_difference < 1 && months_difference > 1){
                    holder.experienceitemsRcvBinding.DateDiffTxt.setText(new StringBuilder().append("(").append(months_difference).append(" months)").toString());
                }else if(years_difference == 1 && months_difference < 1) {
                    holder.experienceitemsRcvBinding.DateDiffTxt.setText(new StringBuilder().append("(").append(years_difference).append(" year)").toString());
                }else if(years_difference < 1 && months_difference == 1){
                    holder.experienceitemsRcvBinding.DateDiffTxt.setText(new StringBuilder().append("(").append(months_difference).append(" month)").toString());
                }else if(years_difference == 1 && months_difference > 1){
                    holder.experienceitemsRcvBinding.DateDiffTxt.setText(new StringBuilder().append("(").append(years_difference).append(" year, ").append(months_difference).append(" months)").toString());
                }else{
                    holder.experienceitemsRcvBinding.DateDiffTxt.setText(new StringBuilder().append("(").append(years_difference).append(" years, ").append(months_difference).append(" month)").toString());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }

        holder.experienceitemsRcvBinding.WorkSummarytxt.setText(Experience_list.get(position).get(Constants.KEY_WORK_SUMMARY).toString());

        //CLICK ON ITEM MENU BUTTON

        holder.experienceitemsRcvBinding.ExpMenuBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                PopupMenu popup = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    popup = new PopupMenu(holder.experienceitemsRcvBinding.ExpMenuBtn.getContext(),
                            holder.experienceitemsRcvBinding.ExpMenuBtn, Gravity.NO_GRAVITY,R.attr.actionOverflowMenuStyle,0);
                }

                Activity activity = unwrap(view.getContext());
                AppCompatActivity appCompatActivity = unwrapCompat(view.getContext());
                TextView Noexp = (TextView) activity.findViewById(R.id.Noexperiencelabel);
                TextView ViewMore_Experience = (TextView) activity.findViewById(R.id.Viewmore_Experiences);
                RecyclerView Experience_RCV = (RecyclerView) activity.findViewById(R.id.Experienceprofile_RCV);
                FragmentContainerView Experience_frame = (FragmentContainerView) activity.findViewById(R.id.addexperienceframe);


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_edit:
                                Constants.UPDATE_FLAG = true;

                                int Clicked_position =  holder.getBindingAdapterPosition();
                                String Job_title = preferenceManager.getMapArray(Constants.KEY_EXPERIENCES).get(Clicked_position).get(Constants.KEY_POSITION_TITLE).toString();
                                String Job_Company = preferenceManager.getMapArray(Constants.KEY_EXPERIENCES).get(Clicked_position).get(Constants.KEY_COMPANY_NAME).toString();
                                String Job_Summary = preferenceManager.getMapArray(Constants.KEY_EXPERIENCES).get(Clicked_position).get(Constants.KEY_WORK_SUMMARY).toString();

                                //Job Start Date and End Date
                                String Job_StartDate = preferenceManager.getMapArray(Constants.KEY_EXPERIENCES).get(Clicked_position).get(Constants.KEY_START_DATE).toString();
                                String Job_Present = preferenceManager.getMapArray(Constants.KEY_EXPERIENCES).get(Clicked_position).get(Constants.KEY_PRESENT_WORK).toString();
                                String Job_EndDate = "";
                                if(Job_Present.equals("true")){
                                    //do nothing
                                     Job_EndDate = "";
                                }else {
                                     Job_EndDate = preferenceManager.getMapArray(Constants.KEY_EXPERIENCES).get(Clicked_position).get(Constants.KEY_END_DATE).toString();
                                }

                                //Split START DATE
                                String[] res = Job_StartDate.split("[,]", 0);
                                String Start_Month = res[0];
                                String St_Year = res[1];


                                String End_Month = "";
                                String EN_Year= "" ;
                                //Split END DATE
                                if(!Job_EndDate.isEmpty()){
                                    String[] end_res = Job_EndDate.split("[,]", 0);
                                     End_Month = end_res[0];
                                    EN_Year = end_res[1];
                                }


                                switch (Start_Month) {
                                    case "Jan": Start_Month = "January"; break;
                                    case "Feb": Start_Month = "February"; break;
                                    case "Mar": Start_Month = "March"; break;
                                    case "Apr": Start_Month = "April"; break;
                                    case "May": Start_Month = "May"; break;
                                    case "Jun": Start_Month = "June"; break;
                                    case "Jul": Start_Month = "July"; break;
                                    case "Aug": Start_Month = "August"; break;
                                    case "Sep": Start_Month = "September"; break;
                                    case "Oct": Start_Month = "October"; break;
                                    case "Nov": Start_Month = "November"; break;
                                    case "Dec": Start_Month = "December"; break;
                                }

                                switch (End_Month) {
                                    case "Jan": End_Month = "January"; break;
                                    case "Feb": End_Month = "February"; break;
                                    case "Mar": End_Month = "March"; break;
                                    case "Apr": End_Month = "April"; break;
                                    case "May": End_Month = "May"; break;
                                    case "Jun": End_Month = "June"; break;
                                    case "Jul": End_Month = "July"; break;
                                    case "Aug": End_Month = "August"; break;
                                    case "Sep": End_Month = "September"; break;
                                    case "Oct": End_Month = "October"; break;
                                    case "Nov": End_Month = "November"; break;
                                    case "Dec": End_Month = "December"; break;
                                }

                                FragmentManager manager = appCompatActivity.getSupportFragmentManager();
                                FragmentTransaction ft = manager.beginTransaction();
                                Fragment bottomFragment = manager.findFragmentById(R.id.addexperienceframe);
                                ft.show(bottomFragment);
                                ft.commit();
                                Experience_frame.setVisibility(View.VISIBLE);
                                Noexp.setVisibility(View.INVISIBLE);
                                Experience_RCV.setVisibility(View.GONE);

                                //Passing Data from Activity to Fragment

                                Bundle bundle = new Bundle();
                                bundle.putString(Constants.KEY_POSITION_TITLE,Job_title);
                                bundle.putString(Constants.KEY_COMPANY_NAME,Job_Company);
                                bundle.putString("START_MONTH",Start_Month);
                                bundle.putString("START_YEAR",St_Year);
                                bundle.putString("END_MONTH",End_Month);
                                bundle.putString("END_YEAR",EN_Year);
                                bundle.putString(Constants.KEY_WORK_SUMMARY,Job_Summary);
                                bundle.putString(Constants.KEY_PRESENT_WORK,Job_Present);
                                bundle.putString("ITEM_CLICKED", String.valueOf(Clicked_position));

                                addexperience exp_fragment = new addexperience();
                                exp_fragment.setArguments(bundle);
                                ft.replace(R.id.addexperienceframe, exp_fragment);

                                return true;

                            case R.id.action_delete:

                                AlertDialog .Builder builder = new AlertDialog.Builder(holder.experienceitemsRcvBinding.getRoot().getContext());
                                builder.setTitle("Are you sure about this ?");


                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        int Clicked_position =  holder.getBindingAdapterPosition();
                                        Constants.Experience_Map =  Experience_list;
                                        Constants.Experience_Map.remove(Clicked_position);
                                        //Experience_list.remove(Clicked_position);
                                        notifyItemRemoved(Clicked_position);
                                        preferenceManager.putMapArray(Constants.KEY_EXPERIENCES,Constants.Experience_Map);

                                        notifyItemRangeChanged(Clicked_position,Constants.Experience_Map.size());


                                        //Reload The RecyclerView Adapter
                                        ExperienceAdapter experienceAdapter;
                                        experienceAdapter =  new ExperienceAdapter(preferenceManager.getMapArray(Constants.KEY_EXPERIENCES));
                                        Experience_RCV.setAdapter(experienceAdapter);
                                        experienceAdapter.activateButtons(true);
                                        //Experience_RCV.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
                                        Experience_RCV.setVisibility(View.VISIBLE);

                                        ArrayList<HashMap> explist = new ArrayList<>();
                                        explist = preferenceManager.getMapArray(Constants.KEY_EXPERIENCES);
                                        if(explist.size() > 0){
                                            Noexp.setVisibility(View.GONE);

                                        }else {
                                            Noexp.setVisibility(View.VISIBLE);
                                        }

                                        if(preferenceManager.getMapArray(Constants.KEY_EXPERIENCES).size() >= 6){
                                            ViewMore_Experience.setVisibility(View.VISIBLE);
                                            ArrayList<HashMap> ExperienceArray_new = new ArrayList<HashMap>(preferenceManager.getMapArray(Constants.KEY_EXPERIENCES).subList(0,5));
                                            experienceAdapter = new ExperienceAdapter(ExperienceArray_new);
                                            Experience_RCV.setAdapter(experienceAdapter);
                                            experienceAdapter.notifyDataSetChanged();
                                            Log.d("KRAT", String.valueOf(ExperienceArray_new));
                                        }else{
                                            ViewMore_Experience.setVisibility(View.GONE);
                                        }
                                        }

                                });
                                AlertDialog ad = builder.create();
                                ad.show();

                                return true;

                            default:
                                return false;
                        }
                    }
                });

                /*  The below code in try catch is responsible to display icons*/
                try {
                    Field[] fields = popup.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popup);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // here you can inflate your menu
                popup.inflate(R.menu.recyclerview_menu);



                popup.show();
            }
        });
    }

/*    public void DeleteExperience(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USERID));
         documentReference.update(Constants.KEY_EXPERIENCES, FieldValue.arrayRemove("0"));



    }*/


    private static Activity unwrap(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        return (Activity) context;
    }


    private static AppCompatActivity unwrapCompat(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        return (AppCompatActivity) context;
    }

    public void activateButtons(boolean activate) {
        this.activate = activate;
        notifyDataSetChanged(); //need to call it for the child views to be re-created with buttons.
    }

    @Override
    public int getItemCount() {
        return Experience_list.size();
    }


    public static class ExperienceViewHolder extends RecyclerView.ViewHolder{

        private final ExperienceitemsRcvBinding experienceitemsRcvBinding;


        public ExperienceViewHolder(ExperienceitemsRcvBinding experienceitemsRcvBinding) {
            super(experienceitemsRcvBinding.getRoot());
            this.experienceitemsRcvBinding = experienceitemsRcvBinding;
        }
    }
}
