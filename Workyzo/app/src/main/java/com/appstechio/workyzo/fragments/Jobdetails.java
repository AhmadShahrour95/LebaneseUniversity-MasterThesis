package com.appstechio.workyzo.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.appstechio.workyzo.R;
import com.appstechio.workyzo.activities.Freelancerprofile_Activity;
import com.appstechio.workyzo.adapters.Download_filesAdapter;
import com.appstechio.workyzo.adapters.Selected_filesAdapter;

import com.appstechio.workyzo.databinding.FragmentJobdetailsBinding;

import com.appstechio.workyzo.interfaces.Display_Toasts;
import com.appstechio.workyzo.models.Job;
import com.appstechio.workyzo.models.User;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Jobdetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Jobdetails extends Fragment implements Display_Toasts {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Jobdetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Jobdetails.
     */
    // TODO: Rename and change types and number of parameters
    public static Jobdetails newInstance(String param1, String param2) {
        Jobdetails fragment = new Jobdetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private User FreelancerUser;
    private Job JobPosts;
    private FragmentJobdetailsBinding binding;
    private Download_filesAdapter download_filesAdapter;
    private ArrayList<HashMap> Attachments = new ArrayList<>();
    private PreferenceManager preferenceManager;
    FirebaseFirestore database;
    public static ArrayList<HashMap> Freelancer_Proposal;
    Bundle extras;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentJobdetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        //loadJobDetails(view);
        showCustomDialog(view);
        extras = getActivity().getIntent().getExtras();
        if (extras == null) {

        }

        return view;
    }


    private void loadJobDetails(View view) {

        JobPosts = (Job) getActivity().getIntent().getSerializableExtra(Constants.KEY_JOB);
        preferenceManager = new PreferenceManager(getActivity());

        if(Constants.MYJOBS_FLAG){
            binding.PlaceBidBtn.setVisibility(View.GONE);
        }else {
            binding.PlaceBidBtn.setVisibility(View.VISIBLE);
        }

        HashMap<String,Object> payment = new HashMap<>();
        payment = (HashMap<String, Object>) JobPosts.getBudget();
        if(payment.get(Constants.KEY_JOB_PAYMENT_TYPE).equals("Fixed price")){
            Constants.FIXED_PRICE_FLAG = true;
            Log.d("FIXED", String.valueOf(Constants.FIXED_PRICE_FLAG));
        }else {
            Constants.FIXED_PRICE_FLAG = false;
            Log.d("FIXED", String.valueOf(Constants.FIXED_PRICE_FLAG));
        }

        String budgetlabel = extras.getString(Constants.KEY_JOB_PAYMENT);
        binding.jobDetailsBudgetlabel.setText(budgetlabel);
        binding.Jobtitilelabel.setText(JobPosts.getTitle());
        binding.EmployerJobPoststarttime.setText(JobPosts.getCreatedDate());
        binding.summarylabel.setText(JobPosts.getDescription());
        ArrayList<String> skills = new ArrayList<>();
        skills = JobPosts.getSkillsRequired();

            binding.SkillsChipGroup.removeAllViews();

            for (int a = 0; a < skills.size(); a++) {
                Chip chip = new Chip(binding.getRoot().getContext());
                chip.setText(skills.get(a).toString());
                chip.setSingleLine(true);
                chip.setTextSize(16);
                binding.SkillsChipGroup.addView(chip);

              /*  Bundle extras = getActivity().getIntent().getExtras();
                if (extras != null) {
                    binding.jobDetailsBudgetlabel.setText(extras.getString(Constants.KEY_JOB_PAYMENT).toString());
                }*/

            }



        //Get Job Uploaded files from firebase Other data
       database = FirebaseFirestore.getInstance();
        Attachments.clear();
        //Load Job Attachments Data
        if(JobPosts.getUploadedFiles() != null){

            binding.Attachments.setVisibility(View.VISIBLE);

            for (int i = 0 ; i <JobPosts.getUploadedFiles().size();i++){
                HashMap<String,String> File_Description = new HashMap<>();
                File_Description.put("FileName",JobPosts.getUploadedFiles().get(i).get("FileName").toString());
                File_Description.put("File_url",JobPosts.getUploadedFiles().get(i).get("File_url").toString());
                Attachments.add(File_Description);
            }


            download_filesAdapter = new Download_filesAdapter(Attachments);
            DividerItemDecoration itemDecor = new DividerItemDecoration(binding.getRoot().getContext(), DividerItemDecoration.VERTICAL);
            binding.AttachmentsRCV.addItemDecoration(itemDecor);
            binding.AttachmentsRCV.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
            binding.AttachmentsRCV.setAdapter(download_filesAdapter);

        }else{
            binding.Attachments.setVisibility(View.GONE);
            binding.AttachmentsRCV.setVisibility(View.GONE);
        }

        //Get Employer Country from firebase Other data

        database.collection(Constants.KEY_COLLECTION_USERS)
                .document(JobPosts.getEmployer_ID())
                .get()
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful() && task1.getResult() != null) {
                        HashMap<String,String> addr = new HashMap<>();
                        addr = (HashMap<String, String>) task1.getResult().get(Constants.KEY_ADDRESS);
                        binding.employerCountrytxt.setText(addr.get(Constants.KEY_COUNTRY));
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

    Boolean proposal_before = false;
    void showCustomDialog(View view) {
        binding.PlaceBidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(binding.getRoot().getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.writeproposal_dialog);

                Button submitProposal_Btn = dialog.findViewById(R.id.Submit_Review_btn);
                TextInputEditText Proposal_content = dialog.findViewById(R.id.inputProposaltxt);
                TextInputEditText Freelancer_BidAmount = dialog.findViewById(R.id.InputBidtxt);
                TextInputLayout Freelancer_BidAmount_layout = dialog.findViewById(R.id.InputBid_post);

                Freelancer_Proposal = new ArrayList<>();
                database = FirebaseFirestore.getInstance();
                String JobId_Target = JobPosts.getJob_ID();

                if(JobPosts.getBudget().get("Type").toString().equals("Hourly rate")){
                    Freelancer_BidAmount_layout.setSuffixText("USD/hour");
                }else{
                    Freelancer_BidAmount_layout.setSuffixText("USD");
                }


                //GET PROPOSAL MAP FROM JOBS COLLECTION
                database.collection(Constants.KEY_COLLECTION_JOBS)
                        .document(JobId_Target)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null) {
                                Freelancer_Proposal = (ArrayList<HashMap>) task.getResult().get(Constants.KEY_JOB_PROPOSAL);

                                if (Freelancer_Proposal != null) {

                                    for (int i = 0; i < Freelancer_Proposal.size(); i++) {

                                        if (Freelancer_Proposal.get(i).get(Constants.KEY_JOB_PROPOSAL_FREELANCER_USERNAME)
                                                .toString().equals(preferenceManager.getString(Constants.KEY_USERNAME))) {

                                            Toast.makeText(getActivity(), "You have sent a proposal", Toast.LENGTH_LONG).show();
                                            proposal_before = true;
                                        }
                                    }
                                }

                                if (Freelancer_Proposal.size() == 0 || !proposal_before) {
                                    dialog.show();
                                }


                            }
                        });


                submitProposal_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Map<String, Object> payment = JobPosts.getBudget();
                        /*Object MinBudget = null;
                        Object MaxBudget = null;

                        MinBudget =  payment.get(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET);
                        MaxBudget =  payment.get(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET);*/

                        Double Converted_MinBudget = Double.parseDouble(String.valueOf(payment.get(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET)));
                     //   Double Converted_MinBudget = convertDouble(MinBudget);
                        Double Converted_MaxBudget =  Double.parseDouble(String.valueOf(payment.get(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET)));



                        if (Freelancer_BidAmount.getText().toString().trim().isEmpty()) {
                            Freelancer_BidAmount.setError(getString(R.string.RequiredField_Error));

                        } else if (Proposal_content.getText().toString().trim().isEmpty()) {
                            Proposal_content.setError(getString(R.string.RequiredField_Error));

                        } else if (Proposal_content.getText().toString().trim().isEmpty() &&
                                Freelancer_BidAmount.getText().toString().trim().isEmpty()) {

                            Proposal_content.setError(getString(R.string.RequiredField_Error));
                            Freelancer_BidAmount.setError(getString(R.string.RequiredField_Error));

                        } else {
                            Double BidAmount = Double.valueOf((Freelancer_BidAmount.getText().toString()));

                            if (BidAmount >=  Converted_MinBudget &&
                                    BidAmount <=  Converted_MaxBudget) {

                                preferenceManager = new PreferenceManager(getActivity());

                                //GET PROPOSAL MAP FROM JOBS COLLECTION and replace old values
                                database.collection(Constants.KEY_COLLECTION_JOBS)
                                        .document(JobId_Target)
                                        .get()
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful() && task.getResult() != null) {
                                                Constants.MyProposals = (ArrayList<HashMap>) task.getResult().get(Constants.KEY_JOB_PROPOSAL);


                                                HashMap<String, Object> Proposals = new HashMap<String, Object>();
                                                Proposals.put(Constants.KEY_JOB_PROPOSAL_FREELANCER_USERNAME, preferenceManager.getString(Constants.KEY_USERNAME));
                                                Proposals.put(Constants.KEY_JOB_PROPOSAL_FREELANCER_BID_AMOUNT, BidAmount);
                                                Proposals.put(Constants.KEY_JOB_PROPOSAL_FREELANCER_CONTENT, Proposal_content.getText().toString());
                                                Constants.Proposals_Map.add(Proposals);


                                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                DocumentReference documentReference =
                                                        db.collection(Constants.KEY_COLLECTION_JOBS)
                                                                .document(JobPosts.getJob_ID());

                                                documentReference.update(Constants.KEY_JOB_PROPOSAL, Constants.Proposals_Map)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(getActivity(), "Proposal added successfully", Toast.LENGTH_LONG).show();

                                                                //Arrays
                                                                preferenceManager.putMapArray(Constants.KEY_JOB_PROPOSAL, Constants.Proposals_Map);

                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(getActivity(), "Proposal not added successfully", Toast.LENGTH_LONG).show();


                                                            }
                                                        });
                                            }
                                        });

                                dialog.dismiss();
                            } else {
                                Freelancer_BidAmount_layout.setError(getString(R.string.BidAmountRange_Error));
                            }

                        }

                    }
                });
            }
        });
    }

    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void showToast(String message, int duration) {

    }

    @Override
    public void onResume() {
        super.onResume();
        View view = binding.getRoot().getRootView();
        loadJobDetails(view);
    }
}