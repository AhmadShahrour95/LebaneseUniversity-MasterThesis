package com.appstechio.workyzo.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Base64;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;


import com.appstechio.workyzo.activities.Freelancerprofile_Activity;
import com.appstechio.workyzo.models.User;
import com.appstechio.workyzo.activities.Chatmessages_Activity;
import com.appstechio.workyzo.databinding.FreelancerusersRcvBinding;
import com.appstechio.workyzo.utilities.Constants;
import com.google.android.material.chip.Chip;


import java.util.ArrayList;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


public class FreelancerUsersAdapter extends RecyclerView.Adapter<FreelancerUsersAdapter.FreelancerViewHolder> {

private ArrayList<User> freelancerusers;


    public FreelancerUsersAdapter(ArrayList<User> freelancerusers) {
        this.freelancerusers = freelancerusers;
    }



    @NonNull
    @Override
    public FreelancerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FreelancerViewHolder(
                    FreelancerusersRcvBinding.inflate(LayoutInflater.from(parent.getContext()),parent
                    ,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull FreelancerViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        holder.setData(freelancerusers.get(position));

        //Click on Contact button
        holder.freelancerUsersBinding.ContactUserBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.freelancerUsersBinding.getRoot().getContext(), Chatmessages_Activity.class);
                //String usernametxt = users.get(position).getUsername();
                User user = freelancerusers.get(position);
                intent.putExtra(Constants.KEY_USER,user);
                holder.freelancerUsersBinding.getRoot().getContext().startActivity(intent);
            }
        });

        //Click on Card view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.freelancerUsersBinding.getRoot().getContext(), Freelancerprofile_Activity.class);
                //String usernametxt = users.get(position).getUsername();
                User user = freelancerusers.get(position);
                intent.putExtra(Constants.KEY_USER,user);
                intent.putExtra(Constants.KEY_ADDRESS,holder.freelancerUsersBinding.FreelancerAddress.getText().toString());
                intent.putExtra(Constants.KEY_SALARY,holder.freelancerUsersBinding.FreelancerSalary.getText().toString());

                BitmapDrawable bitmapDrawable = ((BitmapDrawable) holder.freelancerUsersBinding.freelancerCountryFlag.getDrawable());
                Bitmap bitmap = bitmapDrawable .getBitmap();
                intent.putExtra(Constants.KEY_COUNTRY_FLAG,bitmap);
                holder.freelancerUsersBinding.getRoot().getContext().startActivity(intent);
            }
        });
    }

    public  interface OnFreelancerUserClickListener {
        void OnFreelancerCLick (User user);

    }
    public void filterList(ArrayList<User> filteredlist)
    {
        freelancerusers = filteredlist;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return freelancerusers.size();
    }


    public static class FreelancerViewHolder extends RecyclerView.ViewHolder {

        private final FreelancerusersRcvBinding freelancerUsersBinding;
        private float value ;

        public FreelancerViewHolder(FreelancerusersRcvBinding freelancerUsersBinding) {
            super(freelancerUsersBinding.getRoot());
            this.freelancerUsersBinding = freelancerUsersBinding;
        }

        void setData(User user) {


            freelancerUsersBinding.FreelancerFullName.setText(new StringBuilder().append(user.getFirst_name()).append(" ").append(user.getLast_name()).toString());
            freelancerUsersBinding.freelancerUsername.setText(new StringBuilder().append("@").append(user.getUsername()));
            freelancerUsersBinding.FreelancerProfileImage.setImageBitmap(getUserImage(user.getProfile_Image()));
            freelancerUsersBinding.freelancerProHeadline.setText(user.getProfessional_Headline());
            freelancerUsersBinding.freelancerSummary.setText(user.getUser_Summary());

            if (user.getReviews() != null){
                int reviews_count = user.getReviews().size();

                for (int i=0;i<reviews_count;i++){
                    value = value + Float.parseFloat(user.getReviews().get(i).get(Constants.KEY_REVIEW_RATING).toString());
                }

                float rating_average = value/reviews_count;
                if(rating_average == 0){
                    freelancerUsersBinding.FreelancerRatingValue.setText(String.valueOf(0));
                }else{
                    freelancerUsersBinding.FreelancerRatingValue.setText(String.valueOf(rating_average));
                }
                freelancerUsersBinding.freelancerReviewsValue.setText(String.valueOf(reviews_count));
            }



            if (freelancerUsersBinding.freelancerSummary.length() > 80) {
                freelancerUsersBinding.textView2.setVisibility(View.VISIBLE);
            } else {
                freelancerUsersBinding.textView2.setVisibility(View.GONE);
            }

            freelancerUsersBinding.textView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    freelancerUsersBinding.freelancerSummary.setMaxLines(100);
                    freelancerUsersBinding.textView2.setVisibility(View.GONE);
                }
            });


            ArrayList<String> skills = user.getTop_Skills();
            int i = 0;
            if (skills == null || skills.size() == 0) {
                freelancerUsersBinding.freelancerSkills.setVisibility(View.GONE);
            } else {
                freelancerUsersBinding.freelancerSkills.setVisibility(View.VISIBLE);
                if (skills.size() == 1) {
                    Chip chip = new Chip(freelancerUsersBinding.getRoot().getContext());
                    chip.setText(skills.get(i).toString());
                    chip.setSingleLine(true);
                    chip.setTextSize(16);
                    freelancerUsersBinding.FreelancerSkillsChips.addView(chip);
                    // freelancerUsersBinding.freelancerSkills.setText(new StringBuilder().append(skills.get(i)).toString());

                } else if (skills.size() == 2) {
                    for (int a = 0; a < 2; a++) {
                        Chip chip = new Chip(freelancerUsersBinding.getRoot().getContext());
                        chip.setText(skills.get(a).toString());
                        chip.setSingleLine(true);
                        chip.setTextSize(14);
                        freelancerUsersBinding.FreelancerSkillsChips.addView(chip);
                    }

                    // freelancerUsersBinding.freelancerSkills.setText(new StringBuilder().append(skills.get(i)).append(" - ").append(skills.get(i + 1)).toString());

                } else {
                    for (int a = 0; a < 3; a++) {
                        Chip chip = new Chip(freelancerUsersBinding.getRoot().getContext());
                        chip.setText(skills.get(a).toString());
                        chip.setSingleLine(true);
                        chip.setTextSize(14);
                        freelancerUsersBinding.FreelancerSkillsChips.addView(chip);
                        // freelancerUsersBinding.freelancerSkills.setText(new StringBuilder().append(skills.get(i)).append(" - ").append(skills.get(i + 1)).append(" - ").append(skills.get(i + 2)).toString());

                    }
                }


                Map<String, String> address = user.getAddress();
                String City = null;
                String Country = null;

                Map<String, Object> salary = user.getSalary();
                String Type = null;
                Object Amount = null;

                if (address != null) {
                    City = (String) address.get("City");
                    Country = (String) address.get("Country");
                    Type = (String) salary.get("Type");
                    Amount =  salary.get("Amount");
                }

                if (Country.equals("United States")) {
                    String country_flag = "flag_united_states_of_america";
                    int resID = itemView.getContext().getResources().getIdentifier(country_flag, "drawable", itemView.getContext().getPackageName());
                    freelancerUsersBinding.freelancerCountryFlag.setImageResource(resID);

                } else {
                    String country_flag = "flag_" + Country.toLowerCase().replace(" ", "_");
                    int resID = itemView.getContext().getResources().getIdentifier(country_flag, "drawable", itemView.getContext().getPackageName());
                    freelancerUsersBinding.freelancerCountryFlag.setImageResource(resID);
                }
                //Flag = (String) address.get("Country_flag");
                //freelancerUsersBinding.freelancerCountryFlag.setImageBitmap(getUserImage(Flag));
                // Log.d("HASH",String.valueOf(City + Country + " "+ getUserImage(Flag)));
                //Log.d("HASH",String.valueOf(country_flag + " "+ itemView.getContext().getPackageName()));


                if (Type.equals("Fixed price")) {
                    freelancerUsersBinding.FreelancerSalary.setText(new StringBuilder().append(Amount).append(" USD"));
                } else {
                    freelancerUsersBinding.FreelancerSalary.setText(new StringBuilder().append(Amount).append(" USD/hour"));
                }

                freelancerUsersBinding.FreelancerAddress.setText(new StringBuilder().append(City).append(",").append(Country).toString());
            }
        }

        private Bitmap getUserImage (String encodedImage){
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        }


    }
}







