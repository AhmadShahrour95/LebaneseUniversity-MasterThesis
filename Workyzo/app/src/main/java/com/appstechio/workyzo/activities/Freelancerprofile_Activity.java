package com.appstechio.workyzo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appstechio.workyzo.adapters.FreelancerUsersAdapter;
import com.appstechio.workyzo.adapters.ReviewAdapter;
import com.appstechio.workyzo.models.User;
import com.appstechio.workyzo.adapters.EducationAdapter;
import com.appstechio.workyzo.adapters.ExperienceAdapter;
import com.appstechio.workyzo.adapters.Selected_skillsAdapter;
import com.appstechio.workyzo.databinding.ActivityFreelancerprofileBinding;
import com.appstechio.workyzo.R;
import com.appstechio.workyzo.interfaces.Display_Toasts;
import com.appstechio.workyzo.network.ApiClient;
import com.appstechio.workyzo.network.ApiService;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Freelancerprofile_Activity extends AppCompatActivity implements Display_Toasts {

    private ActivityFreelancerprofileBinding freelancerprofileBinding;
    private PreferenceManager preferenceManager;
    private User FreelancerUser;
    private String encodedImage;
    private float value = 0;
    private Selected_skillsAdapter freelancerskillsadapter;
    private ExperienceAdapter freelancerexperienceadapter;
    private EducationAdapter freelancereducationadapter;
    private ReviewAdapter reviewAdapter;
    private Selected_skillsAdapter  freelancerlanguagesadapter;
    private  ArrayList<HashMap> Experience_array = new ArrayList<>();
    private  ArrayList<HashMap> Education_array = new ArrayList<>();
    private  ArrayList<HashMap> Review_array = new ArrayList<>();
    private  ArrayList<String> Languages_array = new ArrayList<>();
    private  ArrayList<String> Skills_array = new ArrayList<>();
    private boolean User_RatedBefore = false;
    private boolean User_ComplaintBefore = false;
    private boolean User_HiredBefore = false;

    private boolean User_ComplaintBefore1 = false;
    private Boolean isReceiverAvailable = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        freelancerprofileBinding = ActivityFreelancerprofileBinding.inflate(getLayoutInflater());
        View view = freelancerprofileBinding.getRoot();
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(view);
        BacktoFreelancersUser();
        Click_FloatingButtons();
        //loadFreelancer_ProfileDetails();



    }



    private void LoadJobs_Profile(){

        //GET JOBS posted
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_JOBS)
                .whereNotEqualTo(Constants.KEY_JOB_FREELANCER_HIRED,FreelancerUser.getUsername())
                .get()
                .addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful() && task2.getResult() != null &&
                            task2.getResult().getDocuments().size() > 0) {
                        ArrayList<String> Postedjobs_array = new ArrayList<>();
                        for (int x = 0 ;x< task2.getResult().getDocuments().size();x++){
                            DocumentSnapshot documentSnapshot1 = task2.getResult().getDocuments().get(x);
                            //Check if job posted by the user signed in
                            if(documentSnapshot1.getId().contains(FreelancerUser.getUserId())){
                                Postedjobs_array.add(documentSnapshot1.getId());
                            }
                        }
                        freelancerprofileBinding.JobsPostedNumber.setText(String.valueOf(Postedjobs_array.size()));
                        // Log.d("JOBS", String.valueOf(preferenceManager.getStringArray(Constants.KEY_POSTED_JOBS).size()));

                    }else{
                        freelancerprofileBinding.JobsPostedNumber.setText(String.valueOf(0));

                    }
                });

        //GET JOBS Applied
        db.collection(Constants.KEY_COLLECTION_JOBS)
                .get()
                .addOnCompleteListener(task4 -> {
                    if (task4.isSuccessful()) {
                        int count = 0;

                        for (DocumentSnapshot document : task4.getResult()) {
                            if(!document.getId().contains(FreelancerUser.getUserId())){
                                ArrayList<String> arrayList = new ArrayList<>();
                                arrayList.add(document.getData().get(Constants.KEY_JOB_PROPOSAL).toString());

                                if(arrayList.get(0).contains(FreelancerUser.getUsername())){
                                    count = count + 1;
                                    arrayList.clear();
                                }
                            }
                        }
                        freelancerprofileBinding.JobsAppliedNumber.setText(String.valueOf(count));
                    } else {
                        freelancerprofileBinding.JobsAppliedNumber.setText(String.valueOf(0));
                    }

                });


        //GET HIRED JOBS (ACCEPTED)
        db.collection(Constants.KEY_COLLECTION_JOBS)
                .whereEqualTo(Constants.KEY_JOB_FREELANCER_HIRED,FreelancerUser.getUsername())
                .get()
                .addOnCompleteListener(task3 -> {
                    if (task3.isSuccessful() && task3.getResult() != null &&
                            task3.getResult().getDocuments().size() > 0) {

                        freelancerprofileBinding.HiredJobsNumber.setText(String.valueOf(task3.getResult().getDocuments().size()));

                    }else{

                        freelancerprofileBinding.HiredJobsNumber.setText(String.valueOf(0));

                    }
                });

        //GET COMPLETED JOBS
        db.collection(Constants.KEY_COLLECTION_JOBS)
                .whereEqualTo(Constants.KEY_JOB_FREELANCER_HIRED,FreelancerUser.getUsername())
                .get()
                .addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful() && task2.getResult() != null &&
                            task2.getResult().getDocuments().size() > 0) {
                        ArrayList<String> Completedjobs_array = new ArrayList<>();
                        for (int x = 0 ;x< task2.getResult().getDocuments().size();x++){
                            DocumentSnapshot documentSnapshot1 = task2.getResult().getDocuments().get(x);
                            //Check if job is completed
                            if(documentSnapshot1.getBoolean(Constants.KEY_COMPLETED)){
                                Completedjobs_array.add(documentSnapshot1.getId());
                            }
                        }
                        freelancerprofileBinding.CompletedJobsNumber.setText(String.valueOf(Completedjobs_array.size()));

                    }else{
                        freelancerprofileBinding.CompletedJobsNumber.setText(String.valueOf(0));
                    }
                });
    }

    private void loadFreelancer_ProfileDetails(){

        FreelancerUser =(User) getIntent().getSerializableExtra(Constants.KEY_USER);

        //MY AGENDA IS CLICKED
        if(Constants.PROFILE_FLAG){
            freelancerprofileBinding.HireFloatingBtn.setVisibility(View.VISIBLE);
            freelancerprofileBinding.freelancerusernamelabel.setText(FreelancerUser.getUsername());
            freelancerprofileBinding.freelancerFullnamelabel.setText(new StringBuilder().append(FreelancerUser.getFirst_name()).append(" ").append(FreelancerUser.getLast_name()).toString());
            freelancerprofileBinding.freelancerEmailaddressLabel.setText(FreelancerUser.getEmail_Address());
            freelancerprofileBinding.freelancerMobileNumberLabel.setText(FreelancerUser.getMobile_Number());

            freelancerprofileBinding.freelancerJobtitilelabel.setText(FreelancerUser.getProfessional_Headline());

            LoadJobs_Profile();

            if(FreelancerUser.getReviews() != null){
                int reviews_count = FreelancerUser.getReviews().size();

                for (int i=0;i<reviews_count;i++){
                    value = value + Float.parseFloat(FreelancerUser.getReviews().get(i).get(Constants.KEY_REVIEW_RATING).toString());
                }

                freelancerprofileBinding.freelancerratingBar.setRating(value/reviews_count);
                System.out.println("Value: "+value);
                float rating_average = value/reviews_count;
                if(rating_average == 0){
                    freelancerprofileBinding.freelancerratingvalue.setText(String.valueOf(0));
                }else{
                    freelancerprofileBinding.freelancerratingvalue.setText(String.valueOf(rating_average));
                }

                freelancerprofileBinding.freelancerreviewsnumlabel.setText(new StringBuilder().append("(").append(String.valueOf(reviews_count)).append(" reviews)").toString());

                rating_average = 0;
                value = 0;
            }


            Bundle extras = getIntent().getExtras();
            if (extras != null) {
              //  freelancerprofileBinding.freelancersalarylabel.setText(extras.getString(Constants.KEY_SALARY).toString());
               // freelancerprofileBinding.freelanceraddresslabel.setText(extras.getString(Constants.KEY_ADDRESS).toString());
            }

            freelancerprofileBinding.freelancerProfileCountryFlag.setImageBitmap((Bitmap) extras.get(Constants.KEY_COUNTRY_FLAG));



            //Get from firebase Other data
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection(Constants.KEY_COLLECTION_USERS)
                    .whereEqualTo(Constants.KEY_USERNAME,FreelancerUser.getUsername())
                    .get()
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful() && task1.getResult() != null &&
                                task1.getResult().getDocuments().size() > 0) {
                            DocumentSnapshot documentSnapshot = task1.getResult().getDocuments().get(0);
                           // freelancerprofileBinding.freelancerEmailaddressLabel.setText(documentSnapshot.getString(Constants.KEY_EMAILADDRESS));
                           // freelancerprofileBinding.freelancerMobileNumberLabel.setText(documentSnapshot.getString(Constants.KEY_MOBILE_NUMBER));
                            freelancerprofileBinding.freelancersummarylabel.setText(documentSnapshot.getString(Constants.KEY_USER_SUMMARY));

                            HashMap<String,String> Freelancer_address = new HashMap<>();
                            Freelancer_address = (HashMap<String, String>) documentSnapshot.get(Constants.KEY_ADDRESS);
                            freelancerprofileBinding.freelanceraddresslabel.setText(new StringBuilder()
                                    .append(Freelancer_address.get(Constants.KEY_CITY)).append(",").append(Freelancer_address.get(Constants.KEY_COUNTRY)).toString());

                            HashMap<String,String> Freelancer_salary = new HashMap<>();
                            Freelancer_salary = (HashMap<String, String>) documentSnapshot.get(Constants.KEY_SALARY);

                            if(Freelancer_salary.get(Constants.KEY_SALARY_TYPE).equals("Fixed price")){
                                freelancerprofileBinding.freelancersalarylabel.setText(new StringBuilder().append(Freelancer_salary.get(Constants.KEY_SALARY_AMOUNT)).append("  ").append("USD").toString());
                            }else{
                                freelancerprofileBinding.freelancersalarylabel.setText(new StringBuilder().append(Freelancer_salary.get(Constants.KEY_SALARY_AMOUNT)).append("  ").append("USD/hour").toString());
                            }


                            Experience_array = (ArrayList<HashMap>) documentSnapshot.get(Constants.KEY_EXPERIENCES);
                            Education_array = (ArrayList<HashMap>) documentSnapshot.get(Constants.KEY_EDUCATION);
                            Languages_array = (ArrayList<String>) documentSnapshot.get(Constants.KEY_LANGUAGES);
                            Skills_array = (ArrayList<String>) documentSnapshot.get(Constants.KEY_SKILLS);
                            Review_array = (ArrayList<HashMap>) documentSnapshot.get(Constants.KEY_REVIEW);

                            //Load User Skills Data

                            Log.d("SKILLZ", String.valueOf(Skills_array));
                            if(Skills_array.size() >0){
                                freelancerprofileBinding.freelancerNoTopskillsprofilelabel.setVisibility(View.GONE);
                                freelancerskillsadapter = new Selected_skillsAdapter(Skills_array);
                                freelancerprofileBinding.freelancerTopskillsprofileRCV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                freelancerprofileBinding.freelancerTopskillsprofileRCV.setAdapter(freelancerskillsadapter);

                            }else {
                                freelancerprofileBinding.freelancerNoTopskillsprofilelabel.setVisibility(View.VISIBLE);
                            }

                            if(Skills_array.size()>= 6){
                                freelancerprofileBinding.freelancerViewmoreSkills.setVisibility(View.VISIBLE);
                                ArrayList<String> skillsArray_new = new ArrayList<String>(Skills_array.subList(0,5));
                                freelancerskillsadapter = new Selected_skillsAdapter(skillsArray_new);
                                freelancerprofileBinding.freelancerTopskillsprofileRCV.setAdapter(freelancerskillsadapter);
                                freelancerskillsadapter.notifyDataSetChanged();

                            }else{
                                freelancerskillsadapter = new Selected_skillsAdapter(Skills_array);
                                freelancerprofileBinding.freelancerTopskillsprofileRCV.setAdapter(freelancerskillsadapter);
                                freelancerprofileBinding.freelancerViewmoreSkills.setVisibility(View.GONE);
                            }

                            freelancerprofileBinding.freelancerViewmoreSkills.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    freelancerprofileBinding.freelancerViewmoreSkills.setVisibility(View.INVISIBLE);
                                    freelancerskillsadapter = new Selected_skillsAdapter(Skills_array);
                                    freelancerprofileBinding.freelancerTopskillsprofileRCV.setAdapter(freelancerskillsadapter);
                                    freelancerskillsadapter.notifyDataSetChanged();
                                }
                            });

                            //Load User Experience Data

                            if(Experience_array.size() >0){
                                freelancerprofileBinding.freelancerNoexperienceprofilelabel.setVisibility(View.GONE);
                                freelancerexperienceadapter =  new ExperienceAdapter(Experience_array);
                                freelancerexperienceadapter.activateButtons(false);
                                DividerItemDecoration itemDecor = new DividerItemDecoration(freelancerprofileBinding.getRoot().getContext(), DividerItemDecoration.VERTICAL);
                                freelancerprofileBinding.freelancerExperienceprofileRCV.addItemDecoration(itemDecor);
                                freelancerprofileBinding.freelancerExperienceprofileRCV.setNestedScrollingEnabled(false);
                                freelancerprofileBinding.freelancerExperienceprofileRCV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                freelancerprofileBinding.freelancerExperienceprofileRCV.setAdapter(freelancerexperienceadapter);

                            }else {
                                freelancerprofileBinding.freelancerNoexperienceprofilelabel.setVisibility(View.VISIBLE);
                            }

                            if(Experience_array.size() >= 6){
                                freelancerprofileBinding.freelancerViewmoreExperience.setVisibility(View.VISIBLE);
                                ArrayList<HashMap> ExperienceArray_new = new ArrayList<HashMap>(Experience_array.subList(0,5));
                                freelancerexperienceadapter = new ExperienceAdapter(ExperienceArray_new);
                                freelancerprofileBinding.freelancerExperienceprofileRCV.setAdapter(freelancerexperienceadapter);
                                freelancerexperienceadapter.notifyDataSetChanged();

                            }else{
                                freelancerexperienceadapter = new ExperienceAdapter(Experience_array);
                                freelancerprofileBinding.freelancerExperienceprofileRCV.setAdapter(freelancerexperienceadapter);
                                freelancerprofileBinding.freelancerViewmoreExperience.setVisibility(View.GONE);
                            }


                            freelancerprofileBinding.freelancerViewmoreExperience.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    freelancerprofileBinding.freelancerViewmoreExperience.setVisibility(View.INVISIBLE);
                                    freelancerexperienceadapter = new ExperienceAdapter(Experience_array);
                                    freelancerprofileBinding.freelancerExperienceprofileRCV.setAdapter(freelancerexperienceadapter);
                                    freelancerexperienceadapter.notifyDataSetChanged();
                                }
                            });


                            //Load User Education Data

                            if(Education_array.size() >0){
                                freelancerprofileBinding.Noeducationprofilelabel.setVisibility(View.GONE);
                                freelancereducationadapter =  new EducationAdapter(Education_array);
                                freelancereducationadapter.activateButtons(false);
                                DividerItemDecoration itemDecor = new DividerItemDecoration(freelancerprofileBinding.getRoot().getContext(), DividerItemDecoration.VERTICAL);
                                freelancerprofileBinding.freelancerEducationprofileRCV.addItemDecoration(itemDecor);
                                freelancerprofileBinding.freelancerEducationprofileRCV.setNestedScrollingEnabled(false);
                                freelancerprofileBinding.freelancerEducationprofileRCV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                freelancerprofileBinding.freelancerEducationprofileRCV.setAdapter(freelancereducationadapter);

                            }else {
                                freelancerprofileBinding.Noeducationprofilelabel.setVisibility(View.VISIBLE);
                            }

                            if(Education_array.size() >= 6){
                                freelancerprofileBinding.freelancerViewmoreEducation.setVisibility(View.VISIBLE);
                                ArrayList<HashMap> EducationArray_new = new ArrayList<HashMap>(Education_array.subList(0,5));
                                freelancereducationadapter = new EducationAdapter(EducationArray_new);

                                freelancerprofileBinding.freelancerEducationprofileRCV.setAdapter(freelancereducationadapter);
                                freelancereducationadapter.notifyDataSetChanged();

                            }else{
                                freelancereducationadapter = new EducationAdapter(Education_array);
                                freelancerprofileBinding.freelancerEducationprofileRCV.setAdapter(freelancereducationadapter);
                                freelancerprofileBinding.freelancerViewmoreEducation.setVisibility(View.GONE);
                            }

                            freelancerprofileBinding.freelancerViewmoreEducation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    freelancerprofileBinding.freelancerViewmoreEducation.setVisibility(View.INVISIBLE);
                                    freelancereducationadapter = new EducationAdapter(Education_array);
                                    freelancerprofileBinding.freelancerEducationprofileRCV.setAdapter(freelancereducationadapter);
                                    freelancereducationadapter.notifyDataSetChanged();
                                }
                            });

                            //Load User Languages Data
                            Log.d("Lang", String.valueOf(Languages_array));
                            if(Languages_array.size() >0){
                                freelancerprofileBinding.freelancerNoLanguagesprofilelabel.setVisibility(View.GONE);
                                freelancerlanguagesadapter =  new Selected_skillsAdapter(Languages_array);
                                freelancerprofileBinding.freelanceranguagesrecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                freelancerprofileBinding.freelanceranguagesrecyclerView.setAdapter(freelancerlanguagesadapter);

                            }else {
                                freelancerprofileBinding.freelancerNoLanguagesprofilelabel.setVisibility(View.VISIBLE);
                            }

                            if(Languages_array.size() >= 6){
                                freelancerprofileBinding.freelancerViewmoreLanguages.setVisibility(View.VISIBLE);
                                ArrayList<String> LanguagesArray_new = new ArrayList<String>(Languages_array.subList(0,5));
                                freelancerlanguagesadapter = new Selected_skillsAdapter(LanguagesArray_new);
                                freelancerprofileBinding.freelanceranguagesrecyclerView.setAdapter(freelancerlanguagesadapter);
                                freelancerlanguagesadapter.notifyDataSetChanged();

                            }else{
                                freelancerlanguagesadapter = new Selected_skillsAdapter(Languages_array);
                                freelancerprofileBinding.freelanceranguagesrecyclerView.setAdapter(freelancerlanguagesadapter);
                                freelancerprofileBinding.freelancerViewmoreLanguages.setVisibility(View.GONE);
                            }

                            freelancerprofileBinding.freelancerViewmoreLanguages.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    freelancerprofileBinding.freelancerViewmoreLanguages.setVisibility(View.INVISIBLE);
                                    freelancerlanguagesadapter = new Selected_skillsAdapter(Languages_array);
                                    freelancerprofileBinding.freelanceranguagesrecyclerView.setAdapter(freelancerlanguagesadapter);
                                    freelancerlanguagesadapter.notifyDataSetChanged();
                                }
                            });

                            //LOAD USER REVIEWS

                            //Load User Reviews
                               if(Review_array != null){
                                   if(Review_array.size() >0){
                                       freelancerprofileBinding.freelancerNoreviewsprofilelabel.setVisibility(View.GONE);
                                       reviewAdapter =  new ReviewAdapter(Review_array);
                                       DividerItemDecoration itemDecor = new DividerItemDecoration(freelancerprofileBinding.getRoot().getContext(), DividerItemDecoration.VERTICAL);
                                       freelancerprofileBinding.ReviewsprofileRCV.addItemDecoration(itemDecor);
                                       freelancerprofileBinding.ReviewsprofileRCV.setNestedScrollingEnabled(false);
                                       freelancerprofileBinding.ReviewsprofileRCV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                       freelancerprofileBinding.ReviewsprofileRCV.setAdapter(reviewAdapter);

                                   }else {
                                       freelancerprofileBinding.freelancerNoreviewsprofilelabel.setVisibility(View.VISIBLE);
                                   }

                                   if(Review_array.size() >= 6){
                                       freelancerprofileBinding.freelancerViewmoreReviews.setVisibility(View.VISIBLE);
                                       ArrayList<HashMap> ReviewArray_new = new ArrayList<HashMap>(Review_array.subList(0,5));
                                       reviewAdapter = new ReviewAdapter(ReviewArray_new);

                                       freelancerprofileBinding.ReviewsprofileRCV.setAdapter(reviewAdapter);
                                       reviewAdapter.notifyDataSetChanged();

                                   }else{
                                       reviewAdapter = new ReviewAdapter(Review_array);
                                       freelancerprofileBinding.ReviewsprofileRCV.setAdapter(reviewAdapter);
                                       freelancerprofileBinding.freelancerViewmoreReviews.setVisibility(View.GONE);
                                   }

                                   freelancerprofileBinding.freelancerViewmoreReviews.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           freelancerprofileBinding.freelancerViewmoreReviews.setVisibility(View.INVISIBLE);
                                           reviewAdapter = new ReviewAdapter(Review_array);
                                           freelancerprofileBinding.ReviewsprofileRCV.setAdapter(reviewAdapter);
                                           reviewAdapter.notifyDataSetChanged();
                                       }
                                   });
                               }



                            //PROFILE PICTURE
                            String FreelancerUser_Image = FreelancerUser.getProfile_Image();
                            //CONVERT STRING BASE 64 TO BITMAP
                            byte[] decodedString = Base64.decode(FreelancerUser_Image, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            freelancerprofileBinding.freelancerProfilepicView.setImageBitmap(decodedByte);


                        }
                    });



        //ELSE NOT PROFILE FLAG TRUE
        }else{
            freelancerprofileBinding.HireFloatingBtn.setVisibility(View.GONE);
            freelancerprofileBinding.freelancerusernamelabel.setText(FreelancerUser.getUsername());
            freelancerprofileBinding.freelancerFullnamelabel.setText(new StringBuilder().append(FreelancerUser.getFirst_name()).append(" ").append(FreelancerUser.getLast_name()).toString());
            freelancerprofileBinding.freelancerEmailaddressLabel.setText(FreelancerUser.getEmail_Address());
            freelancerprofileBinding.freelancerJobtitilelabel.setText(FreelancerUser.getProfessional_Headline());
            freelancerprofileBinding.freelancerMobileNumberLabel.setText(FreelancerUser.getMobile_Number());

            LoadJobs_Profile();

            if(FreelancerUser.getReviews() != null){
                int reviews_count = FreelancerUser.getReviews().size();

                for (int i=0;i<reviews_count;i++){
                    value = value + Float.parseFloat(FreelancerUser.getReviews().get(i).get(Constants.KEY_REVIEW_RATING).toString());
                }

                freelancerprofileBinding.freelancerratingBar.setRating(value/reviews_count);
                System.out.println("Value: "+value);
                float rating_average = value/reviews_count;
                if(rating_average == 0){
                    freelancerprofileBinding.freelancerratingvalue.setText(String.valueOf(0));
                }else{
                    freelancerprofileBinding.freelancerratingvalue.setText(String.valueOf(rating_average));
                }

                freelancerprofileBinding.freelancerreviewsnumlabel.setText(new StringBuilder().append("(").append(String.valueOf(reviews_count)).append(" reviews)").toString());

                rating_average = 0;
                value = 0;
            }



           Bundle extras = getIntent().getExtras();
            if (extras != null) {
                freelancerprofileBinding.freelancersalarylabel.setText(extras.getString(Constants.KEY_SALARY).toString());
                freelancerprofileBinding.freelanceraddresslabel.setText(extras.getString(Constants.KEY_ADDRESS).toString());
            }

            freelancerprofileBinding.freelancerProfileCountryFlag.setImageBitmap((Bitmap) extras.get(Constants.KEY_COUNTRY_FLAG));

            freelancerprofileBinding.freelancersummarylabel.setText(FreelancerUser.getUser_Summary());


                            //Load User Skills Data

                            if(FreelancerUser.getTop_Skills().size() >0){
                                freelancerprofileBinding.freelancerNoTopskillsprofilelabel.setVisibility(View.GONE);
                                freelancerskillsadapter = new Selected_skillsAdapter(FreelancerUser.getTop_Skills());
                                freelancerprofileBinding.freelancerTopskillsprofileRCV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                freelancerprofileBinding.freelancerTopskillsprofileRCV.setAdapter(freelancerskillsadapter);

                            }else {
                                freelancerprofileBinding.freelancerNoTopskillsprofilelabel.setVisibility(View.VISIBLE);
                            }

                            if(FreelancerUser.getTop_Skills().size()>= 6){
                                freelancerprofileBinding.freelancerViewmoreSkills.setVisibility(View.VISIBLE);
                                ArrayList<String> skillsArray_new = new ArrayList<String>(FreelancerUser.getTop_Skills().subList(0,5));
                                freelancerskillsadapter = new Selected_skillsAdapter(skillsArray_new);
                                freelancerprofileBinding.freelancerTopskillsprofileRCV.setAdapter(freelancerskillsadapter);
                                freelancerskillsadapter.notifyDataSetChanged();

                            }else{
                                freelancerskillsadapter = new Selected_skillsAdapter(FreelancerUser.getTop_Skills());
                                freelancerprofileBinding.freelancerTopskillsprofileRCV.setAdapter(freelancerskillsadapter);
                                freelancerprofileBinding.freelancerViewmoreSkills.setVisibility(View.GONE);
                            }

                            freelancerprofileBinding.freelancerViewmoreSkills.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    freelancerprofileBinding.freelancerViewmoreSkills.setVisibility(View.INVISIBLE);
                                    freelancerskillsadapter = new Selected_skillsAdapter(FreelancerUser.getTop_Skills());
                                    freelancerprofileBinding.freelancerTopskillsprofileRCV.setAdapter(freelancerskillsadapter);
                                    freelancerskillsadapter.notifyDataSetChanged();
                                }
                            });

                            //Load User Experience Data
            if(FreelancerUser.getExperience()!= null){


                            if(FreelancerUser.getExperience().size() >0){
                                freelancerprofileBinding.freelancerNoexperienceprofilelabel.setVisibility(View.GONE);
                                freelancerexperienceadapter =  new ExperienceAdapter(FreelancerUser.getExperience());
                                freelancerexperienceadapter.activateButtons(false);
                                DividerItemDecoration itemDecor = new DividerItemDecoration(freelancerprofileBinding.getRoot().getContext(), DividerItemDecoration.VERTICAL);
                                freelancerprofileBinding.freelancerExperienceprofileRCV.addItemDecoration(itemDecor);
                                freelancerprofileBinding.freelancerExperienceprofileRCV.setNestedScrollingEnabled(false);
                                freelancerprofileBinding.freelancerExperienceprofileRCV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                freelancerprofileBinding.freelancerExperienceprofileRCV.setAdapter(freelancerexperienceadapter);

                            }else {
                                freelancerprofileBinding.freelancerNoexperienceprofilelabel.setVisibility(View.VISIBLE);
                            }

                            if(FreelancerUser.getExperience().size() >= 6){
                                freelancerprofileBinding.freelancerViewmoreExperience.setVisibility(View.VISIBLE);
                                ArrayList<HashMap> ExperienceArray_new = new ArrayList<HashMap>(FreelancerUser.getExperience().subList(0,5));
                                freelancerexperienceadapter = new ExperienceAdapter(ExperienceArray_new);
                                freelancerprofileBinding.freelancerExperienceprofileRCV.setAdapter(freelancerexperienceadapter);
                                freelancerexperienceadapter.notifyDataSetChanged();

                            }else{
                                freelancerexperienceadapter = new ExperienceAdapter(FreelancerUser.getExperience());
                                freelancerprofileBinding.freelancerExperienceprofileRCV.setAdapter(freelancerexperienceadapter);
                                freelancerprofileBinding.freelancerViewmoreExperience.setVisibility(View.GONE);
                            }


                            freelancerprofileBinding.freelancerViewmoreExperience.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    freelancerprofileBinding.freelancerViewmoreExperience.setVisibility(View.INVISIBLE);
                                    freelancerexperienceadapter = new ExperienceAdapter(FreelancerUser.getExperience());
                                    freelancerprofileBinding.freelancerExperienceprofileRCV.setAdapter(freelancerexperienceadapter);
                                    freelancerexperienceadapter.notifyDataSetChanged();
                                }
                            });

            }
                            //Load User Education Data

            if(FreelancerUser.getEducation()!= null){


                            if(FreelancerUser.getEducation().size() >0){
                                freelancerprofileBinding.Noeducationprofilelabel.setVisibility(View.GONE);
                                freelancereducationadapter =  new EducationAdapter(FreelancerUser.getEducation());
                                freelancereducationadapter.activateButtons(false);
                                DividerItemDecoration itemDecor = new DividerItemDecoration(freelancerprofileBinding.getRoot().getContext(), DividerItemDecoration.VERTICAL);
                                freelancerprofileBinding.freelancerEducationprofileRCV.addItemDecoration(itemDecor);
                                freelancerprofileBinding.freelancerEducationprofileRCV.setNestedScrollingEnabled(false);
                                freelancerprofileBinding.freelancerEducationprofileRCV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                freelancerprofileBinding.freelancerEducationprofileRCV.setAdapter(freelancereducationadapter);

                            }else {
                                freelancerprofileBinding.Noeducationprofilelabel.setVisibility(View.VISIBLE);
                            }

                            if(FreelancerUser.getEducation().size() >= 6){
                                freelancerprofileBinding.freelancerViewmoreEducation.setVisibility(View.VISIBLE);
                                ArrayList<HashMap> EducationArray_new = new ArrayList<HashMap>(FreelancerUser.getEducation().subList(0,5));
                                freelancereducationadapter = new EducationAdapter(EducationArray_new);

                                freelancerprofileBinding.freelancerEducationprofileRCV.setAdapter(freelancereducationadapter);
                                freelancereducationadapter.notifyDataSetChanged();

                            }else{
                                freelancereducationadapter = new EducationAdapter(FreelancerUser.getEducation());
                                freelancerprofileBinding.freelancerEducationprofileRCV.setAdapter(freelancereducationadapter);
                                freelancerprofileBinding.freelancerViewmoreEducation.setVisibility(View.GONE);
                            }

                            freelancerprofileBinding.freelancerViewmoreEducation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    freelancerprofileBinding.freelancerViewmoreEducation.setVisibility(View.INVISIBLE);
                                    freelancereducationadapter = new EducationAdapter(FreelancerUser.getEducation());
                                    freelancerprofileBinding.freelancerEducationprofileRCV.setAdapter(freelancereducationadapter);
                                    freelancereducationadapter.notifyDataSetChanged();
                                }
                            });
            }
                            //Load User Languages Data

            if(FreelancerUser.getLanguages()!= null){


                            if(FreelancerUser.getLanguages().size() >0){
                                freelancerprofileBinding.freelancerNoLanguagesprofilelabel.setVisibility(View.GONE);
                                freelancerlanguagesadapter =  new Selected_skillsAdapter(FreelancerUser.getLanguages());
                                freelancerprofileBinding.freelanceranguagesrecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                freelancerprofileBinding.freelanceranguagesrecyclerView.setAdapter(freelancerlanguagesadapter);

                            }else {
                                freelancerprofileBinding.freelancerNoLanguagesprofilelabel.setVisibility(View.VISIBLE);
                            }

                            if(FreelancerUser.getLanguages().size() >= 6){
                                freelancerprofileBinding.freelancerViewmoreLanguages.setVisibility(View.VISIBLE);
                                ArrayList<String> LanguagesArray_new = new ArrayList<String>(FreelancerUser.getLanguages().subList(0,5));
                                freelancerlanguagesadapter = new Selected_skillsAdapter(LanguagesArray_new);
                                freelancerprofileBinding.freelanceranguagesrecyclerView.setAdapter(freelancerlanguagesadapter);
                                freelancerlanguagesadapter.notifyDataSetChanged();

                            }else{
                                freelancerlanguagesadapter = new Selected_skillsAdapter(FreelancerUser.getLanguages());
                                freelancerprofileBinding.freelanceranguagesrecyclerView.setAdapter(freelancerlanguagesadapter);
                                freelancerprofileBinding.freelancerViewmoreLanguages.setVisibility(View.GONE);
                            }

                            freelancerprofileBinding.freelancerViewmoreLanguages.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    freelancerprofileBinding.freelancerViewmoreLanguages.setVisibility(View.INVISIBLE);
                                    freelancerlanguagesadapter = new Selected_skillsAdapter(FreelancerUser.getLanguages());
                                    freelancerprofileBinding.freelanceranguagesrecyclerView.setAdapter(freelancerlanguagesadapter);
                                    freelancerlanguagesadapter.notifyDataSetChanged();
                                }
                            });
            }

                            //Load User Reviews
                        if(FreelancerUser.getReviews()!= null){

                            if(FreelancerUser.getReviews().size() >0){
                                freelancerprofileBinding.freelancerNoreviewsprofilelabel.setVisibility(View.GONE);
                                reviewAdapter =  new ReviewAdapter(FreelancerUser.getReviews());
                                DividerItemDecoration itemDecor = new DividerItemDecoration(freelancerprofileBinding.getRoot().getContext(), DividerItemDecoration.VERTICAL);
                                freelancerprofileBinding.ReviewsprofileRCV.addItemDecoration(itemDecor);
                                freelancerprofileBinding.ReviewsprofileRCV.setNestedScrollingEnabled(false);
                                freelancerprofileBinding.ReviewsprofileRCV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                freelancerprofileBinding.ReviewsprofileRCV.setAdapter(reviewAdapter);

                            }else {
                                freelancerprofileBinding.freelancerNoreviewsprofilelabel.setVisibility(View.VISIBLE);
                            }

                            if(FreelancerUser.getReviews().size() >= 6){
                                freelancerprofileBinding.freelancerViewmoreReviews.setVisibility(View.VISIBLE);
                                ArrayList<HashMap> ReviewArray_new = new ArrayList<HashMap>(FreelancerUser.getReviews().subList(0,5));
                                reviewAdapter = new ReviewAdapter(ReviewArray_new);

                                freelancerprofileBinding.ReviewsprofileRCV.setAdapter(reviewAdapter);
                                reviewAdapter.notifyDataSetChanged();

                            }else{
                                reviewAdapter = new ReviewAdapter(FreelancerUser.getReviews());
                                freelancerprofileBinding.ReviewsprofileRCV.setAdapter(reviewAdapter);
                                freelancerprofileBinding.freelancerViewmoreReviews.setVisibility(View.GONE);
                            }

                            freelancerprofileBinding.freelancerViewmoreReviews.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    freelancerprofileBinding.freelancerViewmoreReviews.setVisibility(View.INVISIBLE);
                                    reviewAdapter = new ReviewAdapter(FreelancerUser.getReviews());
                                    freelancerprofileBinding.ReviewsprofileRCV.setAdapter(reviewAdapter);
                                    reviewAdapter.notifyDataSetChanged();
                                }
                            });
                        }

                            //PROFILE PICTURE
                            String FreelancerUser_Image = FreelancerUser.getProfile_Image();
                            //CONVERT STRING BASE 64 TO BITMAP
                            byte[] decodedString = Base64.decode(FreelancerUser_Image, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            freelancerprofileBinding.freelancerProfilepicView.setImageBitmap(decodedByte);
                        }
                    }


    private String encodeImage(Bitmap bitmap){
        int previewwidth = 100;
        int previewHeight = bitmap.getHeight() * previewwidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewwidth,previewHeight,false);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte [] bytes = stream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void BacktoFreelancersUser() {
        freelancerprofileBinding.freelancerprofilebackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Constants.Skills_Array.clear();
                finish();
            }
        });
    }

    private void Click_FloatingButtons() {


        //CLICK HIRE FLOATING BUTTON
        freelancerprofileBinding.HireFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFreelancer_ProfileDetails();

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                preferenceManager = new PreferenceManager(getApplicationContext());
                String currentUserID = preferenceManager.getString(Constants.KEY_USERID);

                database.collection(Constants.KEY_COLLECTION_JOBS)
                        .document( Constants.JOB_INFO)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                       if (task.isSuccessful()) {
                                                           DocumentSnapshot document = task.getResult();
                                                           if (document.exists()) {
                                                              if(document.getData().get(Constants.KEY_JOB_FREELANCER_HIRED).toString() !=""){
                                                                  User_HiredBefore = true;
                                                              }else {
                                                                  User_HiredBefore = false;
                                                              }
                                                           } else {
                                                               User_HiredBefore = false;
                                                           }
                                                       }

                                                       System.out.println("HIRED BEFORE "+User_HiredBefore);

                                                       if(!User_HiredBefore){
                                                           AlertDialog.Builder builder = new AlertDialog.Builder(Freelancerprofile_Activity.this);
                                                           builder.setTitle("Are you sure you want to hire " + freelancerprofileBinding.freelancerFullnamelabel.getText().toString() + " for this job ?");

                                                           builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                               @Override
                                                               public void onClick(DialogInterface dialogInterface, int i) {
                                                                   FirebaseFirestore database = FirebaseFirestore.getInstance();
                                                                   DocumentReference documentReference =
                                                                           database.collection(Constants.KEY_COLLECTION_JOBS)
                                                                                   .document( Constants.JOB_INFO);

                                                                   documentReference.update(Constants.KEY_JOB_FREELANCER_HIRED,
                                                                           FreelancerUser.getUsername())

                                                                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                               @Override
                                                                               public void onSuccess(Void unused) {
                                                                                   showToast( freelancerprofileBinding.freelancerFullnamelabel.getText().toString() + " is hired successfully",1);
                                                                                   try {
                                                                                       JSONArray tokens = new JSONArray();
                                                                                       System.out.println("Receiver Id: "+FreelancerUser.getToken());
                                                                                       tokens.put(FreelancerUser.getToken());
                                                                                       JSONObject data = new JSONObject();
                                                                                       data.put(Constants.KEY_USERID,preferenceManager.getString(Constants.KEY_USERID));
                                                                                       data.put(Constants.KEY_USERNAME,preferenceManager.getString(Constants.KEY_USERNAME));
                                                                                       data.put(Constants.KEY_FCM_TOKEN,preferenceManager.getString(Constants.KEY_FCM_TOKEN));
                                                                                       data.put(Constants.NOTIFICATION_TYPE,"job_message");
                                                                                       //data.put(Constants.KEY_MESSAGE,preferenceManager.getString(Constants.KEY_MESSAGE));
                                                                                       data.put(Constants.KEY_MESSAGE,"You are hired by "+preferenceManager.getString(Constants.KEY_USERNAME));

                                                                                       JSONObject body = new JSONObject();
                                                                                       body.put(Constants.REMOTE_MSG_DATA,data);
                                                                                       body.put(Constants.REMOTE_MSG_REGISTRATION_IDS,tokens);
                                                                                       SendNotification(body.toString());


                                                                                   } catch (Exception e){
                                                                                       showToast(e.getMessage(),1);
                                                                                   }
                                                                                   finish();
                                                                                   //preferenceManager.putString(Constants.KEY_PROFILE_IMAGE,encodedImage);





                                                                               }
                                                                           });
                                                               }

                                                           });

                                                           builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                               @Override
                                                               public void onClick(DialogInterface dialogInterface, int i) {
                                                                   // ad.dismiss();
                                                                   finish();

                                                               }

                                                           });

                                                           AlertDialog ad = builder.create();
                                                           ad.show();
                                                           //showToast("ACCEPT FREELANCER",1);
                                                       }else{
                                                           Toast.makeText(Freelancerprofile_Activity.this, "Freelancer is hired for this job", Toast.LENGTH_LONG).show();
                                                       }
                                                   }
                        });

            }
       });



        //CLICK COMPLAINT FLOATING BUTTON
        freelancerprofileBinding.ReportFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // showToast("REPORT FREELANCER",1);

                FirebaseFirestore database = FirebaseFirestore.getInstance();
               database.collection(Constants.KEY_COLLECTION_COMPLAINTS)
                        .whereEqualTo(Constants.KEY_COMPLAINTS_FROM,preferenceManager.getString(Constants.KEY_EMAILADDRESS))
                        .get()
                       .addOnCompleteListener(task1 -> {
                           if (task1.isSuccessful() && task1.getResult() != null &&
                                   task1.getResult().getDocuments().size() > 0) {
                               for(int i=0 ; i<  task1.getResult().getDocuments().size();i++){
                                  String User_Complaint_On =  task1.getResult().getDocuments().get(i).getString(Constants.KEY_COMPLAINTS_ON);
                                   if (User_Complaint_On.equals(FreelancerUser.getEmail_Address())){
                                       User_ComplaintBefore = true;
                                   }else {
                                       User_ComplaintBefore = false;
                                   }

                               }

                               if(User_ComplaintBefore){
                                   showToast("Already Complained",1);
                               }else{
                                   showComplaintDialog();
                               }
                           }else {
                               showComplaintDialog();

                           }
                       });

            }
        });

        //CLICK RATE FLOATING BUTTON
        freelancerprofileBinding.RateFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFreelancer_ProfileDetails();

                if (FreelancerUser.getReviews() != null){
                    ArrayList<HashMap> Reviews_arraymap = new ArrayList<>();
                    Reviews_arraymap = FreelancerUser.getReviews();
                    for(int i1 = 0 ; i1<Reviews_arraymap.size();i1++) {

                        if(Reviews_arraymap.get(i1).get(Constants.KEY_REVIEW_USERNAME)
                                .toString().toLowerCase().equals(preferenceManager.getString(Constants.KEY_USERNAME).toLowerCase())){
                            User_RatedBefore = true;

                        }else{
                            User_RatedBefore = false;

                        }
                    }
                }


           if(User_RatedBefore){
               showToast("Already Rated",1);
           }else{
               showWriteReviewDialog();
           }
            }
        });


    }

    void showWriteReviewDialog(){
        final Dialog dialog = new Dialog(Freelancerprofile_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.writereview_dialog);

        Button Submitbtn = dialog.findViewById(R.id.Submit_Review_btn);
        TextInputEditText Feedback_content = dialog.findViewById(R.id.inputReviewtxt);
        RatingBar ratingBar = dialog.findViewById(R.id.Freelancer_Rating);

       // preferenceManager.putString("RATING_VALUE", String.valueOf(rating_value));
        dialog.show();

        Submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> review = new HashMap<>();
                review.put(Constants.KEY_REVIEW_USERNAME,preferenceManager.getString(Constants.KEY_USERNAME));
                review.put(Constants.KEY_REVIEW_CONTENT,Feedback_content.getText().toString());
                review.put(Constants.KEY_REVIEW_PROFILE_PIC,preferenceManager.getString(Constants.KEY_PROFILE_IMAGE));
                float  rating_value = ratingBar.getRating();
                review.put(Constants.KEY_REVIEW_RATING, String.valueOf(rating_value));

                Constants.Review_Map.add(review);
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                DocumentReference documentReference =
                        database.collection(Constants.KEY_COLLECTION_USERS).document(
                               FreelancerUser.getUserId()
                        );

                documentReference.update(Constants.KEY_REVIEW,  Constants.Review_Map)
                      .addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void unused) {
                              showToast("Review Added Successfully",1);
                          }
                      });


                //EditprofileAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //startActivity(EditprofileAct);
                dialog.cancel();
            }
        });
    }


    void showComplaintDialog(){
        final Dialog dialog = new Dialog(Freelancerprofile_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.writecomplaint_dialog);

        Button Submitbtn = dialog.findViewById(R.id.Submit_Complaintbtn);
        TextInputEditText Complaint_content = dialog.findViewById(R.id.inputComplainttxt);

        dialog.show();

        Submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> Complaint = new HashMap<>();
                Complaint.put(Constants.KEY_COMPLAINTS_CONTENT,Complaint_content.getText().toString());
                Complaint.put(Constants.KEY_COMPLAINTS_FROM,preferenceManager.getString(Constants.KEY_EMAILADDRESS));
                Complaint.put(Constants.KEY_COMPLAINTS_ON,FreelancerUser.getEmail_Address());

                FirebaseFirestore database = FirebaseFirestore.getInstance();

                database.collection(Constants.KEY_COLLECTION_COMPLAINTS).document()
                        .set(Complaint)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                showToast("Complaint submitted successfully",1);
                            }
                        })
                        .addOnFailureListener(exception -> {
                            showToast(exception.getMessage(),1);
                        });

                dialog.cancel();
            }
        });
    }

    private void SendNotification(String messageBody){
        ApiClient.getClient().create(ApiService.class).sendMessage(
                Constants.getremoteMsgHeaders(),
                messageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    try {
                        if(response.body() != null){
                            JSONObject responseJson = new JSONObject(response.body());
                            JSONArray results = responseJson.getJSONArray("results");
                            if(responseJson.getInt("failure") == 1) {
                                JSONObject error =(JSONObject)  results.get(0);
                                showToast(error.getString("error"),1);
                                return;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showToast("Notification sent successfully",1);
                }else{
                    showToast("Error"+response.code(),1);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast(t.getMessage(),1);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFreelancer_ProfileDetails();
    }
}