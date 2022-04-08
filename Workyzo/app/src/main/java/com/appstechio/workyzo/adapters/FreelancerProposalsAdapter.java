package com.appstechio.workyzo.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appstechio.workyzo.activities.Chatmessages_Activity;
import com.appstechio.workyzo.activities.Freelancerprofile_Activity;
import com.appstechio.workyzo.activities.Home_Activity;
import com.appstechio.workyzo.databinding.FreelancerproposalsRcvBinding;
import com.appstechio.workyzo.fragments.ProfileFragment;
import com.appstechio.workyzo.models.Job;
import com.appstechio.workyzo.models.User;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class FreelancerProposalsAdapter extends RecyclerView.Adapter<FreelancerProposalsAdapter.FreelancerProposalViewHolder> {

private ArrayList<User> freelancerusers;
    private ArrayList<Job> Jobs_lists;
    private ArrayList<HashMap> Proposals_list= new ArrayList<HashMap>();
    private Job JobPosts;
    private PreferenceManager preferenceManager;

    public FreelancerProposalsAdapter(ArrayList<User> freelancerusers) {
        this.freelancerusers = freelancerusers;
    }



    @NonNull
    @Override
    public FreelancerProposalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FreelancerProposalViewHolder(
                    FreelancerproposalsRcvBinding.inflate(LayoutInflater.from(parent.getContext()),parent
                    ,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull FreelancerProposalViewHolder holder, int position) {

        holder.setData(freelancerusers.get(position));
        if(Constants.FIXED_PRICE_FLAG){
            holder.freelancerproposalsRcvBinding.FreelancerBidAmount.setText(new StringBuilder().append(Constants.Freelancer_Proposal.get(position).get(Constants.KEY_JOB_PROPOSAL_FREELANCER_BID_AMOUNT)).append(" USD").toString());
        }else {
            holder.freelancerproposalsRcvBinding.FreelancerBidAmount.setText(new StringBuilder().append(Constants.Freelancer_Proposal.get(position).get(Constants.KEY_JOB_PROPOSAL_FREELANCER_BID_AMOUNT)).append(" USD per hour").toString());
        }

        holder.freelancerproposalsRcvBinding.freelancerProposalSummary.setText(Constants.Freelancer_Proposal.get(position).get(Constants.KEY_JOB_PROPOSAL_FREELANCER_CONTENT).toString());

        if (holder.freelancerproposalsRcvBinding.freelancerProposalSummary.length() > 80) {
            holder.freelancerproposalsRcvBinding.textView2.setVisibility(View.VISIBLE);
        } else {
            holder.freelancerproposalsRcvBinding.textView2.setVisibility(View.GONE);
        }

        holder.freelancerproposalsRcvBinding.textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.freelancerproposalsRcvBinding.freelancerProposalSummary.setMaxLines(100);
                holder.freelancerproposalsRcvBinding.textView2.setVisibility(View.GONE);
            }
        });

        //Click on Contact button
/*        holder.freelancerUsersBinding.ContactUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.freelancerUsersBinding.getRoot().getContext(), Chatmessages_Activity.class);
                //String usernametxt = users.get(position).getUsername();
                User user = freelancerusers.get(position);
                intent.putExtra(Constants.KEY_USER,user);
                holder.freelancerUsersBinding.getRoot().getContext().startActivity(intent);
            }
        });*/

        //Click on Card view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferenceManager = new PreferenceManager(holder.freelancerproposalsRcvBinding.getRoot().getContext());
                if(freelancerusers.get(position).getEmail_Address().equals(preferenceManager.getString(Constants.KEY_EMAILADDRESS))){

                    Intent intent = new Intent(holder.freelancerproposalsRcvBinding.getRoot().getContext(), Home_Activity.class);
                    holder.freelancerproposalsRcvBinding.getRoot().getContext().startActivity(intent);
                    Constants.FROMPROPOSAL_FLAG = true;

                }else{
                    Constants.FROMPROPOSAL_FLAG = false;
                    Intent intent = new Intent(holder.freelancerproposalsRcvBinding.getRoot().getContext(), Freelancerprofile_Activity.class);
                    //String usernametxt = users.get(position).getUsername();
                    User user = freelancerusers.get(position);
                    intent.putExtra(Constants.KEY_USER,user);

                    //intent.putExtra(Constants.KEY_ADDRESS,holder.freelancerproposalsRcvBinding.FreelancerAddress.getText().toString());
                    //intent.putExtra(Constants.KEY_SALARY,holder.freelancerproposalsRcvBinding.FreelancerBidAmount.getText().toString());

                    BitmapDrawable bitmapDrawable = ((BitmapDrawable) holder.freelancerproposalsRcvBinding.freelancerCountryFlag.getDrawable());
                    Bitmap bitmap = bitmapDrawable .getBitmap();
                    intent.putExtra(Constants.KEY_COUNTRY_FLAG,bitmap);
                    holder.freelancerproposalsRcvBinding.getRoot().getContext().startActivity(intent);
                }

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


    public class FreelancerProposalViewHolder extends RecyclerView.ViewHolder {

        private final FreelancerproposalsRcvBinding freelancerproposalsRcvBinding;


        public FreelancerProposalViewHolder(FreelancerproposalsRcvBinding freelancerUsersBinding) {
            super(freelancerUsersBinding.getRoot());
            this.freelancerproposalsRcvBinding = freelancerUsersBinding;
        }

        void setData(User user) {

            freelancerproposalsRcvBinding.FreelancerFullName.setText(new StringBuilder().append(user.getFirst_name()).append(" ").append(user.getLast_name()).toString());
            freelancerproposalsRcvBinding.freelancerUsername.setText(new StringBuilder().append("@").append(user.getUsername()));
            freelancerproposalsRcvBinding.FreelancerProfileImage.setImageBitmap(getUserImage(user.getProfile_Image()));
            freelancerproposalsRcvBinding.freelancerProHeadline.setText(user.getProfessional_Headline());




           Map<String, String> address = user.getAddress();
                String City = null;
                String Country = null;

                Map<String, Object> salary = user.getSalary();
                String Type = null;
                Object Amount = null;

                if (address != null) {
                    City = (String) address.get("City");
                    Country = (String) address.get("Country");
                }

                if (Country.equals("United States")) {
                    String country_flag = "flag_united_states_of_america";
                    int resID = itemView.getContext().getResources().getIdentifier(country_flag, "drawable", itemView.getContext().getPackageName());
                    freelancerproposalsRcvBinding.freelancerCountryFlag.setImageResource(resID);

                } else {
                    String country_flag = "flag_" + Country.toLowerCase().replace(" ", "_");
                    int resID = itemView.getContext().getResources().getIdentifier(country_flag, "drawable", itemView.getContext().getPackageName());
                    freelancerproposalsRcvBinding.freelancerCountryFlag.setImageResource(resID);
                }

            }
        }

        private Bitmap getUserImage (String encodedImage){
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        }


    }








