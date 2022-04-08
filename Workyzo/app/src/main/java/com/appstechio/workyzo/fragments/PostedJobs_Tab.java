package com.appstechio.workyzo.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appstechio.workyzo.R;
import com.appstechio.workyzo.adapters.FreelancerUsersAdapter;
import com.appstechio.workyzo.adapters.JobsAdapter;
import com.appstechio.workyzo.databinding.FragmentPostedjobsTabBinding;
import com.appstechio.workyzo.interfaces.Display_Toasts;
import com.appstechio.workyzo.models.Job;
import com.appstechio.workyzo.models.User;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostedJobs_Tab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostedJobs_Tab extends Fragment implements Display_Toasts {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostedJobs_Tab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Freelancers_Tab.
     */
    // TODO: Rename and change types and number of parameters
    public static PostedJobs_Tab newInstance(String param1, String param2) {
        PostedJobs_Tab fragment = new PostedJobs_Tab();
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
    private FragmentPostedjobsTabBinding binding;
    private  ArrayList<String> jobs = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPostedjobsTabBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Constants.POSTEDJOB_FLAG = true;
        //database = FirebaseFirestore.getInstance();
      /* try {
            getPostedJobs(view);
       } catch (ParseException e) {
           e.printStackTrace();
       }*/
        search_Job(view);
        //search_freelancer(view);

        Myjobslistener(view);
        return view;


    }

    private void Myjobslistener (View view){

        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity());

        database.collection(Constants.KEY_COLLECTION_JOBS)
                //.whereEqualTo(Constants.KEY_COMPLETED,false)
                .whereNotEqualTo(Constants.KEY_EMPLOYERID,preferenceManager.getString(Constants.KEY_USERID))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.getId() != null) {
                                jobs.add(doc.getId());
                            }
                        }
                        Log.d(TAG, "Current jobs " + jobs.size());
//                        Log.d(TAG, "Current jobs " + JobPostsList.size());
                        if(jobs.size() > 0 ){
                            try {
                                getPostedJobs(view);
                            } catch (ParseException parseException) {
                                parseException.printStackTrace();
                            }
                        }else{

                        }
                    }
                });
    }

    // DISPLAY ALL JOBS IN THE DATABASE
    public void getPostedJobs(View view) throws ParseException {
        Jobs_loading(true);
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity());
        String currentUserID_Post = preferenceManager.getString(Constants.KEY_USERID);

        JobPostsList = new ArrayList<>();
        database.collection(Constants.KEY_COLLECTION_JOBS)
                .whereEqualTo(Constants.KEY_EMPLOYERID,currentUserID_Post)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            //String JobEmployerId = queryDocumentSnapshot.getId().substring(0,28);

                            //if (currentUserID_Post.equals(JobEmployerId)) {

                                Job jobPost = new Job();
                                jobPost.setEmployer_ID(currentUserID_Post);
                                jobPost.setJob_ID(queryDocumentSnapshot.getId());
                                jobPost.setTitle(queryDocumentSnapshot.getString(Constants.KEY_JOB_TITLE));
                                jobPost.setBudget((Map<String, Object>) queryDocumentSnapshot.get(Constants.KEY_JOB_PAYMENT));
                                jobPost.setCreatedDate(queryDocumentSnapshot.getString(Constants.KEY_JOB_CREATED_DATE));
                                jobPost.setDescription(queryDocumentSnapshot.getString(Constants.KEY_JOB_DESCRIPTION));
                                jobPost.setSkillsRequired((ArrayList<String>) queryDocumentSnapshot.get(Constants.KEY_JOB_SKILLS_REQUIRED));
                                jobPost.setProposals((ArrayList<HashMap>) queryDocumentSnapshot.get(Constants.KEY_JOB_PROPOSAL));
                                jobPost.setUploadedFiles((ArrayList<HashMap>) queryDocumentSnapshot.get(Constants.KEY_JOB_UPLOADED_FILES));
                                jobPost.setHired_Freelancer(queryDocumentSnapshot.getString(Constants.KEY_JOB_FREELANCER_HIRED));
                                jobPost.setCompleted(queryDocumentSnapshot.getBoolean(Constants.KEY_COMPLETED));
                                //Log.d("HIRED",jobPost.getHired_Freelancer());
                                JobPostsList.add(jobPost);
                           // }

                        }

                    }

                    if (JobPostsList.size() > 0) {
                        binding.NodataImage.setVisibility(View.GONE);
                        jobsAdapter = new JobsAdapter(JobPostsList);
                        DividerItemDecoration itemDecor = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
                        binding.MyJobsPostedRCV.addItemDecoration(itemDecor);
                        binding.MyJobsPostedRCV.setLayoutManager(new LinearLayoutManager(this.getContext()));
                        binding.MyJobsPostedRCV.setAdapter(jobsAdapter);
                        Jobs_loading(false);
                        binding.resultsRangeJobslabel.setText(new StringBuilder().append("Showing ").append(JobPostsList.size()).append(" results").toString());

                    } else {
                        Jobs_loading(false);
                        jobsAdapter = new JobsAdapter(JobPostsList);
                        DividerItemDecoration itemDecor = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
                        binding.MyJobsPostedRCV.addItemDecoration(itemDecor);
                        binding.MyJobsPostedRCV.setLayoutManager(new LinearLayoutManager(this.getContext()));
                        binding.MyJobsPostedRCV.setAdapter(jobsAdapter);
                        binding.NodataImage.setVisibility(View.VISIBLE);
                        binding.resultsRangeJobslabel.setText(new StringBuilder().append("Showing ").append(JobPostsList.size()).append(" results").toString());

                    }
                });
    }


    private void search_Job(View view) {

        binding.searchboxJobstxt.addTextChangedListener(new TextWatcher() {
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
                binding.MyJobsPostedRCV.setAdapter(jobsAdapter);
                if(binding.MyJobsPostedRCV.getAdapter().getItemCount() == 0){
                    //do nothing
                }else{
                    filter(editable.toString());
                    binding.MyJobsPostedRCV.setAdapter(jobsAdapter);

                    String searchinput = binding.searchboxJobstxt.getText().toString().trim();
                    if (searchinput.isEmpty()) {
                        jobsAdapter = new JobsAdapter(JobPostsList);
                        binding.MyJobsPostedRCV.setAdapter(jobsAdapter);
                        binding.resultsRangeJobslabel.setText(new StringBuilder().append("Showing ").append(JobPostsList.size()).append(" results").toString());

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
        binding.resultsRangeJobslabel.setText(new StringBuilder().append("Showing ").append(filteredJobs.size()).append(" results").toString());

    }

    private void  Jobs_loading (Boolean isloading){
        if(isloading){
            binding.NodataImage.setVisibility(View.GONE);
            binding.MyJobsPostedRCV.setVisibility(View.INVISIBLE);
            binding.searchboxJobs.setVisibility(View.INVISIBLE);
            binding.resultsRangeJobslabel.setVisibility(View.INVISIBLE);

            binding.TopresultsJobslabel.setVisibility(View.INVISIBLE);


            binding.JobsProgressbar.setVisibility(View.VISIBLE);

        }else{
            binding.MyJobsPostedRCV.setVisibility(View.VISIBLE);
            binding.searchboxJobs.setVisibility(View.VISIBLE);
            binding.resultsRangeJobslabel.setVisibility(View.VISIBLE);
            binding.TopresultsJobslabel.setVisibility(View.VISIBLE);


            binding.JobsProgressbar.setVisibility(View.INVISIBLE);

        }
    }



    @Override
    public void onResume() {
        super.onResume();

        View view = binding.getRoot().getRootView();
        Constants.HIREDJOB_FLAG = false;
        Constants.POSTEDJOB_FLAG = true;
        try {
            getPostedJobs(view);
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