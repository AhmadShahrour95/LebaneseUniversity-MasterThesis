package com.appstechio.workyzo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.appstechio.workyzo.R;
import com.appstechio.workyzo.activities.Editprofile_Activity;
import com.appstechio.workyzo.adapters.EducationAdapter;
import com.appstechio.workyzo.adapters.ExperienceAdapter;
import com.appstechio.workyzo.adapters.ReviewAdapter;
import com.appstechio.workyzo.adapters.Selected_skillsAdapter;
import com.appstechio.workyzo.databinding.FragmentProfileBinding;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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


    private  FragmentProfileBinding binding;
    private PreferenceManager preferenceManager;
    private Selected_skillsAdapter selectedSkillsAdapter;
    private ExperienceAdapter experienceAdapter;
    private EducationAdapter educationAdapter;
    private ReviewAdapter reviewAdapter;
    private float value = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        preferenceManager = new PreferenceManager(getActivity());
        Editprofile_Click(view);
        //LoadUser_Data(view);
        return  view;
    }

    private void Editprofile_Click(View view){
        binding.editprofilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(getContext(), Editprofile_Activity.class);
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void LoadUser_Data (View view) {

        if (preferenceManager.getString(Constants.KEY_PROFILE_IMAGE) == null) {
            binding.ProfilepicView.setImageResource(R.drawable.avatar_man);
        } else {
            //CONVERT STRING BASE 64 TO BITMAP
            byte[] decodedString = Base64.decode(preferenceManager.getString(Constants.KEY_PROFILE_IMAGE), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            binding.ProfilepicView.setImageBitmap(decodedByte);
        }

        if(preferenceManager.getStringArray(Constants.KEY_POSTED_JOBS) != null){
            int jobs_posted_number = preferenceManager.getStringArray(Constants.KEY_POSTED_JOBS).size();
            binding.JobsPostedNumber.setText(String.valueOf(jobs_posted_number));
        }else{
            binding.JobsPostedNumber.setText(String.valueOf(0));
        }

            int jobs_hired_number = preferenceManager.getInt(Constants.KEY_HIRED_JOBS);
            binding.HiredJobsNumber.setText(String.valueOf(jobs_hired_number));

            int jobs_applied_number = preferenceManager.getInt(Constants.KEY_APPLIED_JOBS);
            binding.JobsAppliedNumber.setText(String.valueOf(jobs_applied_number));

        if(preferenceManager.getStringArray(Constants.KEY_COMPLETED_JOBS) != null){
            int jobs_completed_number = preferenceManager.getStringArray(Constants.KEY_COMPLETED_JOBS).size();
            binding.CompletedJobsNumber.setText(String.valueOf(jobs_completed_number));
        }else{
            binding.CompletedJobsNumber.setText(String.valueOf(0));
        }



        binding.ProfileCountryFlag.setImageResource(preferenceManager.getInt(Constants.KEY_COUNTRY_FLAG));
        binding.Fullnamelabel.setText(new StringBuilder().append(preferenceManager.getString(Constants.KEY_FIRSTNAME)).append(" ").append(preferenceManager.getString(Constants.KEY_LASTNAME)).toString());
        binding.usernamelabel.setText(preferenceManager.getString(Constants.KEY_USERNAME));
        binding.addresslabel.setText(new StringBuilder().append(preferenceManager.getString(Constants.KEY_CITY)).append(",").append(preferenceManager.getString(Constants.KEY_COUNTRY)).toString());

        if(preferenceManager.getString(Constants.KEY_SALARY_TYPE) == null &&
                preferenceManager.getString(Constants.KEY_SALARY_AMOUNT) == null){

        }else{
            if(preferenceManager.getString(Constants.KEY_SALARY_TYPE).equals("Fixed price")){
                binding.salarylabel.setText(new StringBuilder().append(preferenceManager.getString(Constants.KEY_SALARY_AMOUNT)).append("  ").append("USD").toString());
            }else{
                binding.salarylabel.setText(new StringBuilder().append(preferenceManager.getString(Constants.KEY_SALARY_AMOUNT)).append("  ").append("USD/hour").toString());
            }
        }

        binding.EmailaddressLabel.setText(preferenceManager.getString(Constants.KEY_EMAILADDRESS));
        binding.MobileNumberLabel.setText(preferenceManager.getString(Constants.KEY_MOBILE_NUMBER));
        binding.Jobtitilelabel.setText(preferenceManager.getString(Constants.KEY_PROFESSIONAL_HEADLINE));
        binding.summarylabel.setText(preferenceManager.getString(Constants.KEY_USER_SUMMARY));

        //Load User Skills Data
        if(preferenceManager.getStringArray(Constants.KEY_SKILLS).size() >0){
            binding.NoTopskillsprofilelabel.setVisibility(View.GONE);
            selectedSkillsAdapter =  new Selected_skillsAdapter(preferenceManager.getStringArray(Constants.KEY_SKILLS));
            binding.TopskillsprofileRCV.setLayoutManager(new LinearLayoutManager(this.getContext()));
            binding.TopskillsprofileRCV.setAdapter(selectedSkillsAdapter);

        }else {
            binding.NoTopskillsprofilelabel.setVisibility(View.VISIBLE);
        }

        if(preferenceManager.getStringArray(Constants.KEY_SKILLS).size() >= 6){
            binding.ViewmoreSkills.setVisibility(View.VISIBLE);
            ArrayList<String> skillsArray_new = new ArrayList<String>(preferenceManager.getStringArray(Constants.KEY_SKILLS).subList(0,5));
            selectedSkillsAdapter = new Selected_skillsAdapter(skillsArray_new);
            binding.TopskillsprofileRCV.setAdapter(selectedSkillsAdapter);
            selectedSkillsAdapter.notifyDataSetChanged();

        }else{
            selectedSkillsAdapter = new Selected_skillsAdapter(preferenceManager.getStringArray(Constants.KEY_SKILLS));
            binding.TopskillsprofileRCV.setAdapter(selectedSkillsAdapter);
            binding.ViewmoreSkills.setVisibility(View.GONE);
        }

        //int VisibleItems = getVisibleItemCount( binding.TopskillsprofileRCV);

        binding.ViewmoreSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ViewmoreSkills.setVisibility(View.INVISIBLE);
                selectedSkillsAdapter = new Selected_skillsAdapter(preferenceManager.getStringArray(Constants.KEY_SKILLS));
                binding.TopskillsprofileRCV.setAdapter(selectedSkillsAdapter);
                selectedSkillsAdapter.notifyDataSetChanged();
            }
        });

        //Load User Experience Data

        if(preferenceManager.getMapArray(Constants.KEY_EXPERIENCES).size() >0){
            binding.Noexperienceprofilelabel.setVisibility(View.GONE);
            experienceAdapter =  new ExperienceAdapter(preferenceManager.getMapArray(Constants.KEY_EXPERIENCES));
            experienceAdapter.activateButtons(false);
            DividerItemDecoration itemDecor = new DividerItemDecoration(binding.getRoot().getContext(), DividerItemDecoration.VERTICAL);
            binding.ExperienceprofileRCV.addItemDecoration(itemDecor);
            binding.ExperienceprofileRCV.setNestedScrollingEnabled(false);
            binding.ExperienceprofileRCV.setLayoutManager(new LinearLayoutManager(this.getContext()));
            binding.ExperienceprofileRCV.setAdapter(experienceAdapter);

        }else {
            binding.Noexperienceprofilelabel.setVisibility(View.VISIBLE);
        }

        if(preferenceManager.getMapArray(Constants.KEY_EXPERIENCES).size() >= 6){
            binding.ViewmoreExperience.setVisibility(View.VISIBLE);
            ArrayList<HashMap> ExperienceArray_new = new ArrayList<HashMap>(preferenceManager.getMapArray(Constants.KEY_LANGUAGES).subList(0,5));
            experienceAdapter = new ExperienceAdapter(ExperienceArray_new);
            binding.ExperienceprofileRCV.setAdapter(experienceAdapter);
            experienceAdapter.notifyDataSetChanged();

        }else{
            experienceAdapter = new ExperienceAdapter(preferenceManager.getMapArray(Constants.KEY_EXPERIENCES));
            binding.ExperienceprofileRCV.setAdapter(experienceAdapter);
            binding.ViewmoreExperience.setVisibility(View.GONE);
        }


        binding.ViewmoreExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ViewmoreExperience.setVisibility(View.INVISIBLE);
                experienceAdapter = new ExperienceAdapter(preferenceManager.getMapArray(Constants.KEY_EXPERIENCES));
                binding.ExperienceprofileRCV.setAdapter(experienceAdapter);
                experienceAdapter.notifyDataSetChanged();
            }
        });


        //Load User Education Data

        if(preferenceManager.getMapArray(Constants.KEY_EDUCATION).size() >0){

            binding.Noeducationprofilelabel.setVisibility(View.GONE);
            educationAdapter =  new EducationAdapter(preferenceManager.getMapArray(Constants.KEY_EDUCATION));
            educationAdapter.activateButtons(false);
            DividerItemDecoration itemDecor = new DividerItemDecoration(binding.getRoot().getContext(), DividerItemDecoration.VERTICAL);
            binding.EducationprofileRCV.addItemDecoration(itemDecor);
            binding.EducationprofileRCV.setNestedScrollingEnabled(false);
            binding.EducationprofileRCV.setLayoutManager(new LinearLayoutManager(this.getContext()));
            binding.EducationprofileRCV.setAdapter(educationAdapter);

        }else {
            binding.Noeducationprofilelabel.setVisibility(View.VISIBLE);
        }

        if(preferenceManager.getMapArray(Constants.KEY_EDUCATION).size() >= 6){
            binding.ViewmoreEducation.setVisibility(View.VISIBLE);
            ArrayList<HashMap> EducationArray_new = new ArrayList<HashMap>(preferenceManager.getMapArray(Constants.KEY_EDUCATION).subList(0,5));
            educationAdapter = new EducationAdapter(EducationArray_new);

            binding.EducationprofileRCV.setAdapter(educationAdapter);
            educationAdapter.notifyDataSetChanged();

        }else{
            educationAdapter = new EducationAdapter(preferenceManager.getMapArray(Constants.KEY_EDUCATION));
            binding.EducationprofileRCV.setAdapter(educationAdapter);
            binding.ViewmoreEducation.setVisibility(View.GONE);
        }


        binding.ViewmoreEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ViewmoreEducation.setVisibility(View.INVISIBLE);
                educationAdapter = new EducationAdapter(preferenceManager.getMapArray(Constants.KEY_EDUCATION));
                binding.EducationprofileRCV.setAdapter(educationAdapter);
                educationAdapter.notifyDataSetChanged();
            }
        });


        //Load User Languages Data

        if(preferenceManager.getStringArray(Constants.KEY_LANGUAGES).size() >0){
            binding.NoLanguagesprofilelabel.setVisibility(View.GONE);
            selectedSkillsAdapter =  new Selected_skillsAdapter(preferenceManager.getStringArray(Constants.KEY_LANGUAGES));
            binding.languagesrecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
            binding.languagesrecyclerView.setAdapter(selectedSkillsAdapter);

        }else {
            binding.NoLanguagesprofilelabel.setVisibility(View.VISIBLE);
        }

        if(preferenceManager.getStringArray(Constants.KEY_LANGUAGES).size() >= 6){
            binding.ViewmoreLanguages.setVisibility(View.VISIBLE);
            ArrayList<String> LanguagesArray_new = new ArrayList<String>(preferenceManager.getStringArray(Constants.KEY_LANGUAGES).subList(0,5));
            selectedSkillsAdapter = new Selected_skillsAdapter(LanguagesArray_new);
            binding.languagesrecyclerView.setAdapter(selectedSkillsAdapter);
            selectedSkillsAdapter.notifyDataSetChanged();

        }else{
            selectedSkillsAdapter = new Selected_skillsAdapter(preferenceManager.getStringArray(Constants.KEY_LANGUAGES));
            binding.languagesrecyclerView.setAdapter(selectedSkillsAdapter);
            binding.ViewmoreLanguages.setVisibility(View.GONE);
        }


        binding.ViewmoreLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ViewmoreLanguages.setVisibility(View.INVISIBLE);
                selectedSkillsAdapter = new Selected_skillsAdapter(preferenceManager.getStringArray(Constants.KEY_LANGUAGES));
                binding.languagesrecyclerView.setAdapter(selectedSkillsAdapter);
                selectedSkillsAdapter.notifyDataSetChanged();
            }
        });


        //Load User Reviews Data
       // FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAILADDRESS, binding.EmailaddressLabel.getText().toString())
                .get()
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful() && task1.getResult() != null &&
                            task1.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task1.getResult().getDocuments().get(0);
                        ArrayList<HashMap> Review_array = new ArrayList<>();
                        Review_array = (ArrayList<HashMap>) documentSnapshot.get(Constants.KEY_REVIEW);
                        if (Review_array != null) {
                            preferenceManager.putMapArray(Constants.KEY_REVIEW, Review_array);
                        } else {
                            preferenceManager.putMapArray(Constants.KEY_REVIEW, new ArrayList<>());

                        }

                        int reviews_count = preferenceManager.getMapArray(Constants.KEY_REVIEW).size();

                        for (int i=0;i<reviews_count;i++){
                            value = value + Float.parseFloat(preferenceManager.getMapArray(Constants.KEY_REVIEW).get(i).get(Constants.KEY_REVIEW_RATING).toString());
                        }


                        binding.ratingBar.setRating(value/reviews_count);
                        float rating_average = value/reviews_count;
                        System.out.println(preferenceManager.getMapArray(Constants.KEY_REVIEW).size());
                        if(preferenceManager.getMapArray(Constants.KEY_REVIEW).size() == 0){
                            binding.ratingvalue.setText("0.0");
                        }else{
                            binding.ratingvalue.setText(String.valueOf(rating_average));
                        }

                        binding.reviewsnumlabel.setText(new StringBuilder().append("(").append(String.valueOf(reviews_count)).append(" reviews)").toString());

                        rating_average = 0;
                        value = 0;

                        if(preferenceManager.getMapArray(Constants.KEY_REVIEW).size() >0){
                            binding.Noreviewsprofilelabel.setVisibility(View.GONE);
                            reviewAdapter =  new ReviewAdapter(preferenceManager.getMapArray(Constants.KEY_REVIEW));
                            DividerItemDecoration itemDecor = new DividerItemDecoration(binding.getRoot().getContext(), DividerItemDecoration.VERTICAL);
                            binding.ReviewsprofileRCV.addItemDecoration(itemDecor);
                            binding.ReviewsprofileRCV.setNestedScrollingEnabled(false);
                            binding.ReviewsprofileRCV.setLayoutManager(new LinearLayoutManager(this.getContext()));
                            binding.ReviewsprofileRCV.setAdapter(reviewAdapter);

                        }else {
                            binding.Noreviewsprofilelabel.setVisibility(View.VISIBLE);
                        }

                        if(preferenceManager.getMapArray(Constants.KEY_REVIEW).size() >= 6){
                            binding.ViewmoreReviews.setVisibility(View.VISIBLE);
                            ArrayList<HashMap> ReviewArray_new = new ArrayList<HashMap>(preferenceManager.getMapArray(Constants.KEY_REVIEW).subList(0,5));
                            reviewAdapter = new ReviewAdapter(ReviewArray_new);
                            binding.ReviewsprofileRCV.setAdapter(reviewAdapter);
                            reviewAdapter.notifyDataSetChanged();

                        }else{
                            reviewAdapter = new ReviewAdapter(preferenceManager.getMapArray(Constants.KEY_REVIEW));
                            binding.ReviewsprofileRCV.setAdapter(reviewAdapter);
                            binding.ViewmoreReviews.setVisibility(View.GONE);
                        }


                        binding.ViewmoreReviews.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                binding.ViewmoreReviews.setVisibility(View.INVISIBLE);
                                reviewAdapter = new ReviewAdapter(preferenceManager.getMapArray(Constants.KEY_REVIEW));
                                binding.ReviewsprofileRCV.setAdapter(reviewAdapter);
                                reviewAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });


    }


    public static int getVisibleItemCount(RecyclerView rv) {
        final int firstVisiblePos = getFirstVisiblePosition(rv);
        final int lastVisiblePos = getLastVisiblePosition(rv);
        return Math.max(0, lastVisiblePos - firstVisiblePos);
    }
    /**
     * get first visible position of recycler view
     *
     * @param rv
     * @return
     */
    public static int getFirstVisiblePosition(RecyclerView rv) {
        if (rv != null) {
            final RecyclerView.LayoutManager layoutManager = rv
                    .getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) layoutManager)
                        .findFirstVisibleItemPosition();
            }
        }
        return 0;
    }
    /**
     * get last visible position of recycler view
     *
     * @param rv
     * @return
     */
    public static int getLastVisiblePosition(RecyclerView rv) {
        if (rv != null) {
            final RecyclerView.LayoutManager layoutManager = rv
                    .getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) layoutManager)
                        .findLastVisibleItemPosition();
            }
        }
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();

        View view = binding.getRoot().getRootView();
        LoadUser_Data(view);

            Constants.MYJOBS_FLAG = false;
            Constants.PROFILE_FLAG = false;

    }
}