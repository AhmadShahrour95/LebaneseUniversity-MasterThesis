package com.appstechio.workyzo.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appstechio.workyzo.adapters.FreelancerProposalsAdapter;
import com.appstechio.workyzo.adapters.FreelancerUsersAdapter;
import com.appstechio.workyzo.databinding.FragmentJobProposalsBinding;

import com.appstechio.workyzo.R;
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

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JobProposals#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobProposals extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public JobProposals() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JobProposals.
     */
    // TODO: Rename and change types and number of parameters
    public static JobProposals newInstance(String param1, String param2) {
        JobProposals fragment = new JobProposals();
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

    private boolean Userfound = false;
private ArrayList<HashMap> Freelancer_appliedjobs = new ArrayList<>();
   private HashMap<String, String> applied_job = new HashMap<>();
   //private ArrayList<HashMap> Freelancer = new ArrayList<>();
    private User FreelancerUser;
    private Job JobPosts;
    private FreelancerProposalsAdapter proposalsAdapter;
    private ArrayList<User> freelancersusersList;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    private FragmentJobProposalsBinding binding;
    private  ArrayList<String> jobs = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentJobProposalsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

       /* try {
            getProposals(view);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        Myproposalslistener(view);
        return view;
    }

    private void Myproposalslistener (View view){

        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity());

        database.collection(Constants.KEY_COLLECTION_JOBS)
                .whereEqualTo(Constants.KEY_COMPLETED,false)
                //.whereNotEqualTo(Constants.KEY_EMPLOYERID,preferenceManager.getString(Constants.KEY_USERID))
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
                                getProposals(view);
                            } catch (ParseException parseException) {
                                parseException.printStackTrace();
                            }
                        }else{

                        }
                    }
                });
    }

    // DISPLAY ALL PROPOSALS IN THE DATABASE
    public void getProposals(View view) throws ParseException {
        //Users_loading(true);
       // User user = new User();
        JobPosts = (Job) getActivity().getIntent().getSerializableExtra(Constants.KEY_JOB);
        Constants.JOB_INFO = JobPosts.getJob_ID();
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity());
        String JobId_Target = JobPosts.getJob_ID();


        //GET PROPOSAL MAP FROM JOBS COLLECTION
        database.collection(Constants.KEY_COLLECTION_JOBS)
                .document(JobId_Target)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {

                        Constants.Freelancer_Proposal = (ArrayList<HashMap>) task.getResult().get(Constants.KEY_JOB_PROPOSAL);

                    }
                });


        //LOAD USER DATA AFTER ENSURING THAT HE PLACES A BID
        database.collection(Constants.KEY_COLLECTION_USERS).get()
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful() && task1.getResult() != null) {
                        freelancersusersList = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot1 : task1.getResult()) {
                            if(Constants.Freelancer_Proposal != null){

                                for(int i = 0 ; i<Constants.Freelancer_Proposal.size();i++) {

                                    if(Constants.Freelancer_Proposal.get(i).get(Constants.KEY_JOB_PROPOSAL_FREELANCER_USERNAME)
                                            .toString().equals(queryDocumentSnapshot1.getString(Constants.KEY_USERNAME))){
                                        Userfound = true;

                                    }
                                }

                                if(Userfound){
                                    User user = new User();
                                    user.setUserId(queryDocumentSnapshot1.getId());
                                    user.setUsername(queryDocumentSnapshot1.getString(Constants.KEY_USERNAME));
                                    user.setFirst_name(queryDocumentSnapshot1.getString(Constants.KEY_FIRSTNAME));
                                    user.setLast_name(queryDocumentSnapshot1.getString(Constants.KEY_LASTNAME));
                                    user.setMobile_Number(queryDocumentSnapshot1.getString(Constants.KEY_MOBILE_NUMBER));
                                    user.setEmail_Address(queryDocumentSnapshot1.getString(Constants.KEY_EMAILADDRESS));
                                    user.setAddress((Map<String, String>) queryDocumentSnapshot1.get(Constants.KEY_ADDRESS));
                                    user.setProfessional_Headline(queryDocumentSnapshot1.getString(Constants.KEY_PROFESSIONAL_HEADLINE));
                                    user.setReviews((ArrayList<HashMap>) queryDocumentSnapshot1.get(Constants.KEY_REVIEW));
                                    user.setExperience((ArrayList<HashMap>) queryDocumentSnapshot1.get(Constants.KEY_EXPERIENCES));
                                    user.setEducation((ArrayList<HashMap>) queryDocumentSnapshot1.get(Constants.KEY_EDUCATION));
                                    user.setTop_Skills((ArrayList<String>) queryDocumentSnapshot1.get(Constants.KEY_SKILLS));
                                    user.setLanguages((ArrayList<String>) queryDocumentSnapshot1.get(Constants.KEY_LANGUAGES));
                                    user.setToken(queryDocumentSnapshot1.getString(Constants.KEY_FCM_TOKEN));

                                    if (queryDocumentSnapshot1.getString(Constants.KEY_PROFILE_IMAGE) == null) {
                                        Bitmap myLogo = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.avatar_man);
                                        String encodedimg = encodeImage(myLogo);
                                        user.setProfile_Image(encodedimg);
                                    } else {
                                        user.setProfile_Image(queryDocumentSnapshot1.getString(Constants.KEY_PROFILE_IMAGE));
                                    }
                                    //user.setApplied_Jobs(Constants.Freelancer_Proposal);
                                    freelancersusersList.add(user);
                                    Userfound = false;
                                }

                            }

                        }
                            }


                    if (freelancersusersList.size() > 0) {

                        binding.NodatafreelancerImage.setVisibility(View.GONE);
                        proposalsAdapter = new FreelancerProposalsAdapter(freelancersusersList);
                        DividerItemDecoration itemDecor = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
                        binding.JobsProposalsRCV.addItemDecoration(itemDecor);
                        binding.JobsProposalsRCV.setLayoutManager(new LinearLayoutManager(this.getContext()));
                        binding.JobsProposalsRCV.setAdapter(proposalsAdapter);
                        //Users_loading(false);

                    } else {
                        //Users_loading(false);
                        binding.NodatafreelancerImage.setVisibility(View.VISIBLE);
                    }

                });



    }


    @Override
    public void onResume() {
        super.onResume();
     /*   try {
            getProposals(binding.getRoot().getRootView());
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    private String encodeImage(Bitmap bitmap) {
        int previewwidth = 100;
        int previewHeight = bitmap.getHeight() * previewwidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewwidth, previewHeight, false);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}