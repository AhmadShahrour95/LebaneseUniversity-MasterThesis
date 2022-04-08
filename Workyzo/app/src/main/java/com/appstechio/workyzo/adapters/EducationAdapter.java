package com.appstechio.workyzo.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.os.Build;
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
import com.appstechio.workyzo.databinding.EducationitemsRcvBinding;
import com.appstechio.workyzo.fragments.addeducation;
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

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.EducationViewHolder> {


    private ArrayList<HashMap> Education_list;
    private PreferenceManager preferenceManager;
    private boolean activate;


    public EducationAdapter(ArrayList<HashMap> education_list) {
        Education_list = education_list;
    }





    @NonNull
    @Override
    public EducationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new EducationViewHolder(EducationitemsRcvBinding.inflate(LayoutInflater.from(parent.getContext()),parent
                ,false));
    }



    @Override
    public void onBindViewHolder(@NonNull EducationViewHolder holder, int position) {
        preferenceManager = new PreferenceManager(holder.educationitemsRcvBinding.getRoot().getContext());
        //Education_list = preferenceManager.getMapArray(Constants.KEY_EDUCATION);

        //show Menu btn in recyclerview
        if (activate) {
            holder.educationitemsRcvBinding.EduMenuBtn.setVisibility(View.VISIBLE);
        } else {
            holder.educationitemsRcvBinding.EduMenuBtn.setVisibility(View.INVISIBLE);
        }

        holder.educationitemsRcvBinding.DegreeTxt.setText(new StringBuilder().append(Education_list.get(position).get(Constants.KEY_DEGREE).toString()).append(",").toString());
        holder.educationitemsRcvBinding.MajorTxt.setText(Education_list.get(position).get(Constants.KEY_MAJOR).toString());
        holder.educationitemsRcvBinding.UniversityTxt.setText(new StringBuilder().append(Education_list.get(position).get(Constants.KEY_UNIVERSITY_NAME).toString()).append(",").toString());
        holder.educationitemsRcvBinding.CountryeduTxt.setText(Education_list.get(position).get(Constants.KEY_EDUCATION_COUNTRY).toString());
        holder.educationitemsRcvBinding.StartDateEdu.setText(Education_list.get(position).get(Constants.KEY_START_YEAR).toString() + " -");
        holder.educationitemsRcvBinding.endDateEdu.setText(Education_list.get(position).get(Constants.KEY_END_YEAR).toString());




            String sDate = Education_list.get(position).get(Constants.KEY_START_YEAR).toString();
            String eDate = Education_list.get(position).get(Constants.KEY_END_YEAR).toString();

            SimpleDateFormat obj = new SimpleDateFormat("yyyy");
            try {
                Date date1 = obj.parse(sDate);
                Date date2 = obj.parse(eDate);

                // Calucalte time difference in milliseconds
                long time_difference = date2.getTime() - date1.getTime();
                long dy =  TimeUnit.MILLISECONDS.toDays(time_difference);
                // Calculate time difference in years using TimeUnit class
                long years_difference = dy / 365l;
                dy %= 365;

                holder.educationitemsRcvBinding.YearsDiffTxt.setText(new StringBuilder().append("( ").append(years_difference).append(" years )").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }



        holder.educationitemsRcvBinding.EduMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    popup = new PopupMenu(holder.educationitemsRcvBinding.EduMenuBtn.getContext(),
                            holder.educationitemsRcvBinding.EduMenuBtn, Gravity.NO_GRAVITY,R.attr.actionOverflowMenuStyle,0);
                }

                Activity activity = unwrap(view.getContext());
                AppCompatActivity appCompatActivity = unwrapCompat(view.getContext());
                TextView Noedu = (TextView) activity.findViewById(R.id.Noeducationlabel);
                TextView ViewMore_Education = (TextView) activity.findViewById(R.id.Viewmore_Education);
                RecyclerView Education_RCV = (RecyclerView) activity.findViewById(R.id.educationrecyclerView);
                FragmentContainerView Education_frame = (FragmentContainerView) activity.findViewById(R.id.addeducationframe);


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_edit:
                                Constants.UPDATE_FLAG = true;
                                int Clicked_position =  holder.getBindingAdapterPosition();
                                String Education_country = preferenceManager.getMapArray(Constants.KEY_EDUCATION).get(Clicked_position).get(Constants.KEY_EDUCATION_COUNTRY).toString();
                                String University_name = preferenceManager.getMapArray(Constants.KEY_EDUCATION).get(Clicked_position).get(Constants.KEY_UNIVERSITY_NAME).toString();
                                String Degree = preferenceManager.getMapArray(Constants.KEY_EDUCATION).get(Clicked_position).get(Constants.KEY_DEGREE).toString();
                                String Major = preferenceManager.getMapArray(Constants.KEY_EDUCATION).get(Clicked_position).get(Constants.KEY_MAJOR).toString();
                                String StartYear =preferenceManager.getMapArray(Constants.KEY_EDUCATION).get(Clicked_position).get(Constants.KEY_START_YEAR).toString();
                                String LastYear = preferenceManager.getMapArray(Constants.KEY_EDUCATION).get(Clicked_position).get(Constants.KEY_END_YEAR).toString();


                                FragmentManager manager = appCompatActivity.getSupportFragmentManager();
                                FragmentTransaction ft = manager.beginTransaction();
                                Fragment bottomFragment = manager.findFragmentById(R.id.addeducationframe);
                                ft.show(bottomFragment);
                                ft.commit();
                                Education_frame.setVisibility(View.VISIBLE);
                                Noedu.setVisibility(View.INVISIBLE);
                                Education_RCV.setVisibility(View.GONE);

                                //Passing Data from Activity to Fragment

                                Bundle bundle = new Bundle();
                                bundle.putString(Constants.KEY_EDUCATION_COUNTRY,Education_country);
                                bundle.putString(Constants.KEY_UNIVERSITY_NAME,University_name);
                                bundle.putString(Constants.KEY_DEGREE,Degree);
                                bundle.putString(Constants.KEY_MAJOR,Major);
                                bundle.putString(Constants.KEY_START_YEAR,StartYear);
                                bundle.putString(Constants.KEY_END_YEAR,LastYear);
                                bundle.putString("ITEM_CLICKED", String.valueOf(Clicked_position));

                                addeducation edu_fragment = new addeducation();
                                edu_fragment.setArguments(bundle);
                                ft.replace(R.id.addeducationframe, edu_fragment);

                                return true;

                            case R.id.action_delete:

                                AlertDialog.Builder builder = new AlertDialog.Builder(holder.educationitemsRcvBinding.getRoot().getContext());
                                builder.setTitle("Are you sure about this ?");


                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        int Clicked_position =  holder.getBindingAdapterPosition();
                                        Constants.Education_Map =  Education_list;
                                        Constants.Education_Map.remove(Clicked_position);
                                        notifyItemRemoved(Clicked_position);
                                        preferenceManager.putMapArray(Constants.KEY_EDUCATION,Constants.Education_Map);
                                        notifyItemRangeChanged(Clicked_position,Constants.Education_Map.size());


                                        //Reload The RecyclerView Adapter
                                        EducationAdapter educationAdapter;
                                        educationAdapter =  new EducationAdapter(preferenceManager.getMapArray(Constants.KEY_EDUCATION));
                                        Education_RCV.setAdapter(educationAdapter);
                                        educationAdapter.activateButtons(true);
                                        //Experience_RCV.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
                                        Education_RCV.setVisibility(View.VISIBLE);

                                        ArrayList<HashMap> edulist = new ArrayList<>();
                                        edulist = preferenceManager.getMapArray(Constants.KEY_EDUCATION);
                                        if(edulist.size() > 0){
                                            Noedu.setVisibility(View.GONE);

                                        }else {
                                            Noedu.setVisibility(View.VISIBLE);
                                        }

                                        if(preferenceManager.getMapArray(Constants.KEY_EDUCATION).size() >= 6){
                                            ViewMore_Education.setVisibility(View.VISIBLE);
                                            ArrayList<HashMap> EducationArray_new = new ArrayList<HashMap>(preferenceManager.getMapArray(Constants.KEY_EDUCATION).subList(0,5));
                                            educationAdapter = new EducationAdapter(EducationArray_new);
                                            Education_RCV.setAdapter(educationAdapter);
                                            educationAdapter.notifyDataSetChanged();

                                        }else{
                                            ViewMore_Education.setVisibility(View.GONE);
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



    public void activateButtons(boolean activate) {
        this.activate = activate;
        notifyDataSetChanged(); //need to call it for the child views to be re-created with buttons.
    }

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
    @Override
    public int getItemCount() {
        return Education_list.size();
    }


    public static class EducationViewHolder extends RecyclerView.ViewHolder{

        private final EducationitemsRcvBinding educationitemsRcvBinding;


        public EducationViewHolder(EducationitemsRcvBinding educationitemsRcvBinding) {
            super(educationitemsRcvBinding.getRoot());
            this.educationitemsRcvBinding = educationitemsRcvBinding;
        }
    }
}
