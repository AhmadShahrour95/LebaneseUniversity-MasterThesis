package com.appstechio.workyzo.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.appstechio.workyzo.R;
import com.appstechio.workyzo.activities.JobDetails_Activity;
import com.appstechio.workyzo.databinding.MyproposalsRcvBinding;
import com.appstechio.workyzo.fragments.MyProposals_Tab;
import com.appstechio.workyzo.models.Job;
import com.appstechio.workyzo.models.User;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MyProposalsAdapter extends RecyclerView.Adapter<MyProposalsAdapter.JobsViewHolder> {

    private ArrayList<Job> proposals_lists;
    private ArrayList<Job> MyUpdatedProposals_lists;



    public MyProposalsAdapter(ArrayList<Job> jobPosts) {
        this.proposals_lists = jobPosts;
    }


    @NonNull
    @Override
    public JobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JobsViewHolder(
                MyproposalsRcvBinding.inflate(LayoutInflater.from(parent.getContext()), parent
                        , false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull JobsViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        holder.setData(proposals_lists.get(position));

        if (holder.jobsRcvBinding.MyProposalSummary.length() > 80) {
            holder.jobsRcvBinding.textView2.setVisibility(View.VISIBLE);
        } else {
            holder.jobsRcvBinding.textView2.setVisibility(View.GONE);
        }

        holder.jobsRcvBinding.textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.jobsRcvBinding.MyProposalSummary.setMaxLines(100);
                holder.jobsRcvBinding.textView2.setVisibility(View.GONE);
            }
        });
        //Click on Card view
 /*       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.jobsRcvBinding.getRoot().getContext(), JobDetails_Activity.class);
                Job jobPost = proposals_lists.get(position);
                intent.putExtra(Constants.KEY_JOB_PAYMENT, holder.jobsRcvBinding.EmployerJobSalarypaid.getText().toString());
                intent.putExtra(Constants.KEY_JOB, jobPost);
                holder.jobsRcvBinding.getRoot().getContext().startActivity(intent);
            }
        });*/


        //CLICK ON ITEM MENU BUTTON

        holder.jobsRcvBinding.myproposalMenuBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                PopupMenu popup = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    popup = new PopupMenu(holder.jobsRcvBinding.myproposalMenuBtn.getContext(),
                            holder.jobsRcvBinding.myproposalMenuBtn, Gravity.NO_GRAVITY, R.attr.actionOverflowMenuStyle, 0);
                }

//                TextView Noexp = (TextView) ((Activity)holder.jobsRcvBinding.getRoot().getContext()).findViewById(R.id.Noexperiencelabel);
                //  TextView ViewMore_Experience = (TextView) ((Activity)holder.jobsRcvBinding.getRoot().getContext()).findViewById(R.id.Viewmore_Experiences);
                // RecyclerView Experience_RCV = (RecyclerView) ((Activity)holder.jobsRcvBinding.getRoot().getContext()).findViewById(R.id.Experienceprofile_RCV);
                // FragmentContainerView Experience_frame = (FragmentContainerView) ((Activity)holder.jobsRcvBinding.getRoot().getContext()).findViewById(R.id.addexperienceframe);
                Activity activity = unwrap(view.getContext());
                //AppCompatActivity appCompatActivity = unwrapCompat(view.getContext());
                ImageView no_data = (ImageView)activity.findViewById(R.id.Nodata_Image);
                RecyclerView Myproposals_RCV = (RecyclerView) activity.findViewById(R.id.MyProposals_RCV);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_edit:
                                //Constants.UPDATE_FLAG = true;
                                //holder.showCustomDialog(holder.itemView,proposals_lists.get(position));

                                FirebaseFirestore database;
                                PreferenceManager preferenceManager = new PreferenceManager(holder.jobsRcvBinding.getRoot().getContext());
                                final Dialog dialog = new Dialog(holder.jobsRcvBinding.getRoot().getContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(true);
                                dialog.setContentView(R.layout.writeproposal_dialog);

                                Button submitProposal_Btn = dialog.findViewById(R.id.Submit_Review_btn);
                                TextInputEditText Proposal_content = dialog.findViewById(R.id.inputProposaltxt);
                                TextInputEditText Freelancer_BidAmount = dialog.findViewById(R.id.InputBidtxt);

                                if(proposals_lists.get(position).getHired_Freelancer().equals("")){
                                    dialog.show();

                                    database = FirebaseFirestore.getInstance();
                                    String JobId_Target = proposals_lists.get(position).getJob_ID();
                                    int Clicked_position =  holder.getBindingAdapterPosition();

                                    String bid_price = proposals_lists.get(Clicked_position).getProposals().get(0).get(Constants.KEY_JOB_PROPOSAL_FREELANCER_BID_AMOUNT).toString();
                                    String proposal_description = proposals_lists.get(Clicked_position).getProposals().get(0).get(Constants.KEY_JOB_PROPOSAL_FREELANCER_CONTENT).toString();

                                    Freelancer_BidAmount.setText(bid_price);
                                    Proposal_content.setText(proposal_description);
                                    //Freelancer_BidAmount.setText();
                                    //Proposal_content.setText();


                                    submitProposal_Btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            Map<String, Object> payment = proposals_lists.get(position).getBudget();
                                            //Object MinBudget = null;
                                           // Object MaxBudget = null;

                                           // MinBudget = payment.get(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET);
                                            //MaxBudget = payment.get(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET);
                                           // Double Converted_MinBudget = convertDouble(MinBudget);
                                           // Double Converted_MaxBudget = convertDouble(MaxBudget);

                                            Double Converted_MinBudget = Double.parseDouble(String.valueOf(payment.get(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET)));
                                            //   Double Converted_MinBudget = convertDouble(MinBudget);
                                            Double Converted_MaxBudget =  Double.parseDouble(String.valueOf(payment.get(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET)));


                                            if(Freelancer_BidAmount.getText().toString().trim().isEmpty()){
                                                Freelancer_BidAmount.setError("Required field*");

                                            }else if (Proposal_content.getText().toString().trim().isEmpty()) {
                                                Proposal_content.setError("Required field*");

                                            }else if (Proposal_content.getText().toString().trim().isEmpty() &&
                                                    Freelancer_BidAmount.getText().toString().trim().isEmpty()) {

                                                Proposal_content.setError("Required field*");
                                                Freelancer_BidAmount.setError("Required field*");

                                            }else{
                                                Double BidAmount = Double.valueOf(Freelancer_BidAmount.getText().toString());
                                                if (BidAmount >= Converted_MinBudget &&
                                                        BidAmount <= Converted_MaxBudget ) {

                                                    //GET PROPOSAL MAP FROM JOBS COLLECTION and replace old values
                                                    database.collection(Constants.KEY_COLLECTION_JOBS)
                                                            .document(JobId_Target)
                                                            .get()
                                                            .addOnCompleteListener(task -> {
                                                                if (task.isSuccessful() && task.getResult() != null) {

                                                                    String freelancer_selected = (String) task.getResult().get(Constants.KEY_JOB_FREELANCER_HIRED);
                                                                    if(freelancer_selected.equals("")){

                                                                    }
                                                                    Constants.MyProposals = (ArrayList<HashMap>) task.getResult().get(Constants.KEY_JOB_PROPOSAL);


                                                                    for (int i = 0; i <  Constants.MyProposals.size(); i++) {

                                                                        if ( Constants.MyProposals.get(i).get(Constants.KEY_JOB_PROPOSAL_FREELANCER_USERNAME)
                                                                                .toString().equals(preferenceManager.getString(Constants.KEY_USERNAME))) {

                                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                                                Constants.MyProposals.get(i).replace(Constants.KEY_JOB_PROPOSAL_FREELANCER_BID_AMOUNT, BidAmount);
                                                                                Constants.MyProposals.get(i).replace(Constants.KEY_JOB_PROPOSAL_FREELANCER_CONTENT, Proposal_content.getText().toString());
                                                                            }

                                                                        }
                                                                    }

                                                                    DocumentReference documentReference =
                                                                            database.collection(Constants.KEY_COLLECTION_JOBS)
                                                                                    .document(proposals_lists.get(position).getJob_ID());

                                                                    documentReference.update(Constants.KEY_JOB_PROPOSAL, Constants.MyProposals )
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    Toast.makeText(holder.jobsRcvBinding.getRoot().getContext(),"Proposal updated successfully",Toast.LENGTH_LONG).show();

                                                                                    //Arrays

                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(holder.jobsRcvBinding.getRoot().getContext(),"Proposal not updated successfully",Toast.LENGTH_LONG).show();

                                                                                }
                                                                            });
                                                                }
                                                            });



                                               /*     preferenceManager.putMapArray(Constants.KEY_JOB_PROPOSAL, Constants.Freelancer_Proposal);
                                                    MyProposalsAdapter myProposalsAdapter;
                                                    myProposalsAdapter = new MyProposalsAdapter(MyUpdatedProposals_lists);
                                                    Myproposals_RCV.setLayoutManager(new LinearLayoutManager(holder.jobsRcvBinding.getRoot().getContext()));
                                                    Myproposals_RCV.setAdapter(myProposalsAdapter);*/
                                                    dialog.dismiss();


                                                }else {
                                                    Freelancer_BidAmount.setError("Bid amount out of range");
                                                }

                                            }

                                        }
                                    });
                                }else {
                                    Toast.makeText(holder.jobsRcvBinding.getRoot().getContext(),"Job awarded to someone else",Toast.LENGTH_LONG).show();
                                }

                                return true;

                            case R.id.action_delete:

                                AlertDialog.Builder builder = new AlertDialog.Builder(holder.jobsRcvBinding.getRoot().getContext());
                                builder.setTitle("Are you sure about this ?");


                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                     holder.DeleteProposal(holder.itemView,proposals_lists.get(position));

                                        //Reload The RecyclerView Adapter

                                        if (proposals_lists.size() > 0) {
                                            //holder.jobsRcvBinding..NodataImage.setVisibility(View.GONE);
                                            MyProposalsAdapter myProposalsAdapter;
                                            myProposalsAdapter = new MyProposalsAdapter(proposals_lists);
                                            Myproposals_RCV.setAdapter(myProposalsAdapter);

                                            //binding.MyJobsPostedRCV.setAdapter(myProposalsAdapter);
                                            //Jobs_loading(false);
                                            //binding.resultsRangeJobslabel.setText(new StringBuilder().append("Showing ").append(JobPostsList.size()).append(" results").toString());

                                        } else {
                                            //Jobs_loading(false);
                                            //binding.NodataImage.setVisibility(View.VISIBLE);
                                            //binding.resultsRangeJobslabel.setText(new StringBuilder().append("Showing ").append(JobPostsList.size()).append(" results").toString());

                                        }

                                    }

                                });

                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                       dialogInterface.dismiss();
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



    static double convertDouble(Object longValue){
        double valueTwo = -1; // whatever to state invalid!

        if(longValue instanceof Long)
            valueTwo = ((Long) longValue).doubleValue();

        System.out.println(valueTwo);
        return valueTwo;
    }

    public interface OnFreelancerUserClickListener {
        void OnFreelancerCLick(User user);

    }

    private static Activity unwrap(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        return (Activity) context;
    }

    public void filterList(ArrayList<Job> filteredlist) {
        proposals_lists = filteredlist;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return proposals_lists.size();
    }


    public static class JobsViewHolder extends RecyclerView.ViewHolder {

        private final MyproposalsRcvBinding jobsRcvBinding;
        private PreferenceManager preferenceManager;

        public JobsViewHolder(MyproposalsRcvBinding jobsRcvBinding1) {
            super(jobsRcvBinding1.getRoot());
            this.jobsRcvBinding = jobsRcvBinding1;
        }

        void setData(Job jobPost) {

            preferenceManager = new PreferenceManager(jobsRcvBinding.getRoot().getContext());

            jobsRcvBinding.EmployerJobTitle.setText(jobPost.getTitle());


                Map<String, Object> payment = jobPost.getBudget();
                String Type = null;

                Type = (String) payment.get(Constants.KEY_JOB_PAYMENT_TYPE);

                ArrayList<HashMap> MyProposals_Map = jobPost.getProposals();


                    for(int i = 0 ; i<MyProposals_Map.size();i++) {

                        if (MyProposals_Map.get(i).get(Constants.KEY_JOB_PROPOSAL_FREELANCER_USERNAME)
                                .toString().equals(preferenceManager.getString(Constants.KEY_USERNAME))) {
                            if (Type.equals("Fixed price")) {
                                jobsRcvBinding.MyproposalBidAmount.setText(new StringBuilder().append(MyProposals_Map.get(i).get(Constants.KEY_JOB_PROPOSAL_FREELANCER_BID_AMOUNT)).append(" USD").toString());

                            } else {
                                jobsRcvBinding.MyproposalBidAmount.setText(new StringBuilder().append(MyProposals_Map.get(i).get(Constants.KEY_JOB_PROPOSAL_FREELANCER_BID_AMOUNT)).append(" USD per hour").toString());

                            }

                            jobsRcvBinding.MyProposalSummary.setText(MyProposals_Map.get(i).get(Constants.KEY_JOB_PROPOSAL_FREELANCER_CONTENT).toString());

                        }
                    }

            }

        //public static ArrayList<HashMap> Freelancer_Proposal;

        FirebaseFirestore database;



        void DeleteProposal (View view,Job jobproposal) {
            preferenceManager = new PreferenceManager(jobsRcvBinding.getRoot().getContext());
            String JobId_Target = jobproposal.getJob_ID();
            database = FirebaseFirestore.getInstance();
            //GET PROPOSAL MAP FROM JOBS COLLECTION and delete it
            database.collection(Constants.KEY_COLLECTION_JOBS)
                    .document(JobId_Target)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Constants.MyProposals = (ArrayList<HashMap>) task.getResult().get(Constants.KEY_JOB_PROPOSAL);

                            for (int i = 0; i <  Constants.MyProposals.size(); i++) {

                                if ( Constants.MyProposals.get(i).get(Constants.KEY_JOB_PROPOSAL_FREELANCER_USERNAME)
                                        .toString().equals(preferenceManager.getString(Constants.KEY_USERNAME))) {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                                        Constants.MyProposals.get(i).clear();
                                        Constants.MyProposals.remove(i);
                                    }

                                }
                            }

                            DocumentReference documentReference =
                                    database.collection(Constants.KEY_COLLECTION_JOBS)
                                            .document(jobproposal.getJob_ID());

                            documentReference.update(Constants.KEY_JOB_PROPOSAL, Constants.MyProposals )
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(jobsRcvBinding.getRoot().getContext(),"Proposal deleted successfully",Toast.LENGTH_LONG).show();

                                            //Arrays
                                            preferenceManager.putMapArray(Constants.KEY_JOB_PROPOSAL, Constants.Freelancer_Proposal);

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(jobsRcvBinding.getRoot().getContext(),"Proposal not deleted successfully",Toast.LENGTH_LONG).show();

                                        }
                                    });
                        }
                    });
        }

        }



    }











