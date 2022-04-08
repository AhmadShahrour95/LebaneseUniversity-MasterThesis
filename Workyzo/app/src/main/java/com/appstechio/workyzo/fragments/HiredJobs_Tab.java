package com.appstechio.workyzo.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appstechio.workyzo.adapters.JobsAdapter;
import com.appstechio.workyzo.databinding.FragmentHiredjobsTabBinding;
import com.appstechio.workyzo.interfaces.Display_Toasts;
import com.appstechio.workyzo.models.Job;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HiredJobs_Tab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HiredJobs_Tab extends Fragment implements Display_Toasts {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HiredJobs_Tab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Jobs_Tab.
     */
    // TODO: Rename and change types and number of parameters
    public static HiredJobs_Tab newInstance(String param1, String param2) {
        HiredJobs_Tab fragment = new HiredJobs_Tab();
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

    private JobsAdapter jobsAdapter;
    private ArrayList<Job> JobPostsList;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    private ActivityResultLauncher<Intent> startchat_activity;
    private FragmentHiredjobsTabBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=  FragmentHiredjobsTabBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Constants.HIREDJOB_FLAG = true;

     /*   try {
            getHiredJobs(view);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        search_Job(view);
        return view;

    }



    // DISPLAY ALL JOBS IN THE DATABASE
    public void getHiredJobs(View view) throws ParseException {
        Jobs_loading(true);
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity());
        String currentUsername_Post = preferenceManager.getString(Constants.KEY_USERNAME);
        JobPostsList = new ArrayList<>();
        database.collection(Constants.KEY_COLLECTION_JOBS)
                .whereEqualTo(Constants.KEY_JOB_FREELANCER_HIRED,currentUsername_Post)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            String Hired_FreelancerId = queryDocumentSnapshot.getString(Constants.KEY_JOB_FREELANCER_HIRED);

                            //if (currentUsername_Post.equals(Hired_FreelancerId)) {
                                Job jobPost = new Job();
                                jobPost.setEmployer_ID(queryDocumentSnapshot.getString(Constants.KEY_EMPLOYERID));
                                jobPost.setJob_ID(queryDocumentSnapshot.getId());
                                jobPost.setTitle(queryDocumentSnapshot.getString(Constants.KEY_JOB_TITLE));
                                jobPost.setBudget((Map<String, Object>) queryDocumentSnapshot.get(Constants.KEY_JOB_PAYMENT));
                                jobPost.setCreatedDate(queryDocumentSnapshot.getString(Constants.KEY_JOB_CREATED_DATE));
                                jobPost.setDescription(queryDocumentSnapshot.getString(Constants.KEY_JOB_DESCRIPTION));
                                jobPost.setSkillsRequired((ArrayList<String>) queryDocumentSnapshot.get(Constants.KEY_JOB_SKILLS_REQUIRED));
                                jobPost.setProposals((ArrayList<HashMap>) queryDocumentSnapshot.get(Constants.KEY_JOB_PROPOSAL));
                                jobPost.setUploadedFiles((ArrayList<HashMap>) queryDocumentSnapshot.get(Constants.KEY_JOB_UPLOADED_FILES));

                                JobPostsList.add(jobPost);

                            }

                        }

                    //}

                    if (JobPostsList.size() > 0) {
                        Jobs_loading(false);
                        binding.NodatafreelancerImage.setVisibility(View.GONE);
                        jobsAdapter = new JobsAdapter(JobPostsList);
                        DividerItemDecoration itemDecor = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
                        binding.HiredjobsRCV.addItemDecoration(itemDecor);
                        binding.HiredjobsRCV.setLayoutManager(new LinearLayoutManager(this.getContext()));
                        binding.HiredjobsRCV.setAdapter(jobsAdapter);
                        binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(JobPostsList.size()).append(" results").toString());

                    } else {
                        Jobs_loading(false);
                        jobsAdapter = new JobsAdapter(JobPostsList);
                        DividerItemDecoration itemDecor = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
                        binding.HiredjobsRCV.addItemDecoration(itemDecor);
                        binding.HiredjobsRCV.setLayoutManager(new LinearLayoutManager(this.getContext()));
                        binding.HiredjobsRCV.setAdapter(jobsAdapter);
                        binding.NodatafreelancerImage.setVisibility(View.VISIBLE);
                        binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(JobPostsList.size()).append(" results").toString());

                    }
                });
    }


    private void search_Job(View view) {

        binding.searchboxFreelancerstxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //binding.resultsRangeLabel.setText(new StringBuilder().append("Search results for").append(" '").append(charSequence).append("'").toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

                jobsAdapter = new JobsAdapter(JobPostsList);
                binding.HiredjobsRCV.setAdapter(jobsAdapter);
                if(binding.HiredjobsRCV.getAdapter().getItemCount() == 0){
                    //do nothing
                }else{
                    filter(editable.toString());
                    binding.HiredjobsRCV.setAdapter(jobsAdapter);

                    String searchinput = binding.searchboxFreelancerstxt.getText().toString().trim();
                    if (searchinput.isEmpty()) {
                        jobsAdapter = new JobsAdapter(JobPostsList);
                        binding.HiredjobsRCV.setAdapter(jobsAdapter);
                        binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(JobPostsList.size()).append(" results").toString());

                    }
                }

            }
        });
    }

    private void filter(String text) {
        ArrayList<Job> filteredJobs = new ArrayList<Job>();

        for (int i = 0; i < JobPostsList.size(); i++) {
            String st = JobPostsList.get(i).getTitle().toLowerCase();
            if (st.contains(text.toLowerCase())) {
                filteredJobs.add(JobPostsList.get(i));
            }
        }
        jobsAdapter.filterList(filteredJobs);
        binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(filteredJobs.size()).append(" results").toString());

    }

    private void  Jobs_loading (Boolean isloading){
        if(isloading){

            if ( binding.NodatafreelancerImage.getVisibility() == View.VISIBLE) {
                // Its visible
            } else {
                binding.NodatafreelancerImage.setVisibility(View.GONE);
                binding.HiredjobsRCV.setVisibility(View.INVISIBLE);
                binding.searchboxFreelancers.setVisibility(View.INVISIBLE);
                binding.resultsRangeLabel.setVisibility(View.INVISIBLE);

                binding.TopresultsLabel.setVisibility(View.INVISIBLE);

                binding.UsersProgressbar.setVisibility(View.VISIBLE);
            }


        }else{

            if ( binding.NodatafreelancerImage.getVisibility() == View.VISIBLE) {
                // Its visible
            }else {
                binding.UsersProgressbar.setVisibility(View.GONE);
                binding.HiredjobsRCV.setVisibility(View.VISIBLE);
                binding.searchboxFreelancers.setVisibility(View.VISIBLE);
                binding.resultsRangeLabel.setVisibility(View.VISIBLE);

                binding.TopresultsLabel.setVisibility(View.VISIBLE);

            }

        }
    }



    @Override
    public void onResume() {
        super.onResume();

        View view = binding.getRoot().getRootView();
      Constants.HIREDJOB_FLAG = true;
      Constants.POSTEDJOB_FLAG = false;
        try {
            getHiredJobs(view);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void showToast(String message, int duration) {

    }
}