package com.appstechio.workyzo.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.appstechio.workyzo.databinding.ActivitySplashBinding;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding Splashlayout;
    private static final  int SPLASH_SCREEN_TIMEOUT = 2000;
    private PreferenceManager preferenceManager;
    private FirebaseAuth mAuth;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Splashlayout = ActivitySplashBinding.inflate(getLayoutInflater());
        View splashview = Splashlayout.getRoot();
        mAuth = FirebaseAuth.getInstance();
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(splashview);



        //Delay Timeout to Navigate to Login Activity
        new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            // Check if user is signed in (non-null) and update UI accordingly.
            //FirebaseAuth.getInstance().signOut();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null) {

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                database.collection(Constants.KEY_COLLECTION_USERS)
                        .whereEqualTo(Constants.KEY_EMAILADDRESS, currentUser.getEmail())
                        .get()
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful() && task1.getResult() != null &&
                                    task1.getResult().getDocuments().size() > 0) {
                                DocumentSnapshot documentSnapshot = task1.getResult().getDocuments().get(0);
                                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                preferenceManager.putString(Constants.KEY_USERID, documentSnapshot.getId());
                                preferenceManager.putBoolean(Constants.KEY_VISIBLE_ASFREELANCER, documentSnapshot.getBoolean(Constants.KEY_VISIBLE_ASFREELANCER));
                                preferenceManager.putString(Constants.KEY_USERNAME, documentSnapshot.getString(Constants.KEY_USERNAME));
                                preferenceManager.putString(Constants.KEY_EMAILADDRESS, documentSnapshot.getString(Constants.KEY_EMAILADDRESS));
                                preferenceManager.putString(Constants.KEY_FIRSTNAME, documentSnapshot.getString(Constants.KEY_FIRSTNAME));
                                preferenceManager.putString(Constants.KEY_LASTNAME, documentSnapshot.getString(Constants.KEY_LASTNAME));
                                preferenceManager.putString(Constants.KEY_MOBILE_NUMBER, documentSnapshot.getString(Constants.KEY_MOBILE_NUMBER));
                                // preferenceManager.putString(Constants.KEY_GENDER,documentSnapshot.getString(Constants.KEY_GENDER));
                                preferenceManager.putString("MyKey", documentSnapshot.getString("Key"));

                                int i = 1;

                                if (preferenceManager.getString(Constants.KEY_FIRSTNAME) != null && !preferenceManager.getString(Constants.KEY_FIRSTNAME).isEmpty()) {
                                    preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE, i++);

                                }

                                if (preferenceManager.getString(Constants.KEY_LASTNAME) != null && !preferenceManager.getString(Constants.KEY_LASTNAME).isEmpty()) {
                                    preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE, i++);

                                }

                                if (preferenceManager.getString(Constants.KEY_USERNAME) != null && !preferenceManager.getString(Constants.KEY_USERNAME).isEmpty()) {
                                    preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE, i++);

                                }
                                if (preferenceManager.getString(Constants.KEY_EMAILADDRESS) != null && !preferenceManager.getString(Constants.KEY_EMAILADDRESS).isEmpty()) {
                                    preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE, i++);

                                }


                                if (preferenceManager.getString(Constants.KEY_MOBILE_NUMBER) != null && !preferenceManager.getString(Constants.KEY_MOBILE_NUMBER).isEmpty()) {
                                    preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE, i++);

                                }
                                preferenceManager.putString(Constants.KEY_PROFESSIONAL_HEADLINE, documentSnapshot.getString(Constants.KEY_PROFESSIONAL_HEADLINE));
                                if (preferenceManager.getString(Constants.KEY_PROFESSIONAL_HEADLINE) != null && !preferenceManager.getString(Constants.KEY_PROFESSIONAL_HEADLINE).isEmpty()) {
                                    preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE, i++);


                                }
                                preferenceManager.putString(Constants.KEY_USER_SUMMARY, documentSnapshot.getString(Constants.KEY_USER_SUMMARY));
                                if (preferenceManager.getString(Constants.KEY_USER_SUMMARY) != null && !preferenceManager.getString(Constants.KEY_USER_SUMMARY).isEmpty()) {
                                    preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE, i++);


                                }

                                Map<String, Object> Address_map = (Map<String, Object>) documentSnapshot.get(Constants.KEY_ADDRESS);
                                if (Address_map != null) {
                                    for (Map.Entry<String, Object> dataEntry : Address_map.entrySet()) {
                                        if (dataEntry.getKey().equals(Constants.KEY_COUNTRY)) {
                                            preferenceManager.putString(Constants.KEY_COUNTRY, dataEntry.getValue().toString());
                                        } else {
                                            preferenceManager.putString(Constants.KEY_CITY, dataEntry.getValue().toString());
                                        }
                                    }
                                }
                                if (preferenceManager.getString(Constants.KEY_COUNTRY) != null && !preferenceManager.getString(Constants.KEY_COUNTRY).isEmpty()) {
                                    preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE, i++);


                                }
                                if (preferenceManager.getString(Constants.KEY_CITY) != null && !preferenceManager.getString(Constants.KEY_CITY).isEmpty()) {
                                    preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE, i++);


                                }
                                if (preferenceManager.getString(Constants.KEY_COUNTRY) != null) {
                                    if (preferenceManager.getString(Constants.KEY_COUNTRY).equals("United States")) {
                                        String country_flag = "flag_united_states_of_america";
                                        int resID = getApplicationContext().getResources().getIdentifier(country_flag, "drawable", getApplicationContext().getPackageName());
                                        preferenceManager.putInt(Constants.KEY_COUNTRY_FLAG, resID);
                                    } else {
                                        String country_flag = "flag_" + preferenceManager.getString(Constants.KEY_COUNTRY).toLowerCase().replace(" ", "_");
                                        int resID = getApplicationContext().getResources().getIdentifier(country_flag, "drawable", getApplicationContext().getPackageName());
                                        preferenceManager.putInt(Constants.KEY_COUNTRY_FLAG, resID);
                                    }
                                }


                                Map<String, Object> Salary_map = (Map<String, Object>) documentSnapshot.get(Constants.KEY_SALARY);
                                if (Salary_map != null) {
                                    for (Map.Entry<String, Object> dataEntry : Salary_map.entrySet()) {
                                        if (dataEntry.getKey().equals("Type")) {
                                            preferenceManager.putString(Constants.KEY_SALARY_TYPE, dataEntry.getValue().toString());
                                        } else {
                                            preferenceManager.putString(Constants.KEY_SALARY_AMOUNT, dataEntry.getValue().toString());
                                        }
                                    }
                                } else {
                                    preferenceManager.putStringArray(Constants.KEY_SALARY, new ArrayList<>());
                                }

                                if (preferenceManager.getString(Constants.KEY_SALARY_TYPE) != null && !preferenceManager.getString(Constants.KEY_SALARY_TYPE).isEmpty()) {
                                    preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE, i++);

                                }
                                if (preferenceManager.getString(Constants.KEY_SALARY_AMOUNT) != null && !preferenceManager.getString(Constants.KEY_SALARY_AMOUNT).isEmpty()) {
                                    preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE, i++);

                                }

                                ArrayList<String> topskills_array = new ArrayList<>();
                                topskills_array = (ArrayList<String>) documentSnapshot.get(Constants.KEY_SKILLS);
                                if (topskills_array != null || !preferenceManager.getStringArray(Constants.KEY_SKILLS).isEmpty()) {
                                    preferenceManager.putStringArray(Constants.KEY_SKILLS, topskills_array);
                                    preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE, i++);
                                } else {
                                    preferenceManager.putStringArray(Constants.KEY_SKILLS, new ArrayList<>());


                                }

                                ArrayList<HashMap> Experience_array = new ArrayList<>();
                                Experience_array = (ArrayList<HashMap>) documentSnapshot.get(Constants.KEY_EXPERIENCES);
                                if (Experience_array != null) {
                                    preferenceManager.putMapArray(Constants.KEY_EXPERIENCES, Experience_array);

                                } else {
                                    preferenceManager.putMapArray(Constants.KEY_EXPERIENCES, new ArrayList<>());


                                }

                                ArrayList<HashMap> Education_array = new ArrayList<>();
                                Education_array = (ArrayList<HashMap>) documentSnapshot.get(Constants.KEY_EDUCATION);
                                if (Education_array != null) {
                                    preferenceManager.putMapArray(Constants.KEY_EDUCATION, Education_array);

                                } else {
                                    preferenceManager.putMapArray(Constants.KEY_EDUCATION, new ArrayList<>());


                                }

                                ArrayList<HashMap> Review_array = new ArrayList<>();
                                Review_array = (ArrayList<HashMap>) documentSnapshot.get(Constants.KEY_REVIEW);
                                if (Review_array != null) {
                                    preferenceManager.putMapArray(Constants.KEY_REVIEW, Review_array);
                                } else {
                                    preferenceManager.putMapArray(Constants.KEY_REVIEW, new ArrayList<>());

                                }

                                ArrayList<String> Languages_array = new ArrayList<>();
                                Languages_array = (ArrayList<String>) documentSnapshot.get(Constants.KEY_LANGUAGES);
                                if (Languages_array != null) {
                                    preferenceManager.putStringArray(Constants.KEY_LANGUAGES, Languages_array);
                                    //Log.d("skills", String.valueOf(preferenceManager.getStringArray(Constants.KEY_SKILLS)));
                                } else {
                                    preferenceManager.putStringArray(Constants.KEY_LANGUAGES, new ArrayList<>());


                                }

                                if (documentSnapshot.getString(Constants.KEY_PROFILE_IMAGE) == null) {
                                    //do nothing
                                } else {
                                    preferenceManager.putString(Constants.KEY_PROFILE_IMAGE, documentSnapshot.getString(Constants.KEY_PROFILE_IMAGE));
                                    if (preferenceManager.getString(Constants.KEY_PROFILE_IMAGE) != null || !preferenceManager.getString(Constants.KEY_PROFILE_IMAGE).isEmpty()) {
                                        preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE, i++);
                                    }
                                }


                                //GET JOBS posted
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection(Constants.KEY_COLLECTION_JOBS)
                                        .whereNotEqualTo(Constants.KEY_JOB_FREELANCER_HIRED, preferenceManager.getString(Constants.KEY_USERNAME))
                                        .get()
                                        .addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful() && task2.getResult() != null &&
                                                    task2.getResult().getDocuments().size() > 0) {
                                                ArrayList<String> Postedjobs_array = new ArrayList<>();
                                                for (int x = 0; x < task2.getResult().getDocuments().size(); x++) {
                                                    DocumentSnapshot documentSnapshot1 = task2.getResult().getDocuments().get(x);
                                                    //Check if job posted by the user signed in
                                                    if (documentSnapshot1.getId().contains(preferenceManager.getString(Constants.KEY_USERID))) {
                                                        Postedjobs_array.add(documentSnapshot1.getId());
                                                    }
                                                }
                                                preferenceManager.putStringArray(Constants.KEY_POSTED_JOBS, Postedjobs_array);
                                                // Log.d("JOBS", String.valueOf(preferenceManager.getStringArray(Constants.KEY_POSTED_JOBS).size()));

                                            }
                                        });

                                //GET JOBS Applied
                                db.collection(Constants.KEY_COLLECTION_JOBS)
                                        .whereNotEqualTo(Constants.KEY_EMPLOYERID,preferenceManager.getString(Constants.KEY_USERID))
                                        .get()
                                        .addOnCompleteListener(task4 -> {
                                            if (task4.isSuccessful()) {
                                                int count = 0;

                                                for (DocumentSnapshot document : task4.getResult()) {
                                                    if (!document.getId().contains(preferenceManager.getString(Constants.KEY_USERID))) {
                                                        ArrayList<String> arrayList = new ArrayList<>();
                                                        if (document.getData().get(Constants.KEY_JOB_PROPOSAL) != null) {
                                                            arrayList.add(document.getData().get(Constants.KEY_JOB_PROPOSAL).toString());

                                                            if (arrayList.get(0).contains(preferenceManager.getString(Constants.KEY_USERNAME))) {
                                                                count = count + 1;
                                                                arrayList.clear();
                                                            }
                                                        }

                                                    }
                                                }
                                                preferenceManager.putInt(Constants.KEY_APPLIED_JOBS, count);
                                            } else {
                                                preferenceManager.putInt(Constants.KEY_APPLIED_JOBS, 0);
                                            }

                                        });


                                //GET HIRED JOBS (ACCEPTED)
                                db.collection(Constants.KEY_COLLECTION_JOBS)
                                        .whereEqualTo(Constants.KEY_JOB_FREELANCER_HIRED, preferenceManager.getString(Constants.KEY_USERNAME))
                                        .get()
                                        .addOnCompleteListener(task3 -> {
                                            if (task3.isSuccessful() && task3.getResult() != null &&
                                                    task3.getResult().getDocuments().size() > 0) {

                                                preferenceManager.putInt(Constants.KEY_HIRED_JOBS, task3.getResult().getDocuments().size());
                                                // Log.d("JOBS", String.valueOf(preferenceManager.getInt(Constants.KEY_HIRED_JOBS)));

                                            } else {
                                                preferenceManager.putInt(Constants.KEY_HIRED_JOBS, 0);
                                            }
                                        });

                                //GET COMPLETED JOBS
                                db.collection(Constants.KEY_COLLECTION_JOBS)
                                        .whereEqualTo(Constants.KEY_JOB_FREELANCER_HIRED, preferenceManager.getString(Constants.KEY_USERNAME))
                                        .get()
                                        .addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful() && task2.getResult() != null &&
                                                    task2.getResult().getDocuments().size() > 0) {
                                                ArrayList<String> Completedjobs_array = new ArrayList<>();
                                                for (int x = 0; x < task2.getResult().getDocuments().size(); x++) {
                                                    DocumentSnapshot documentSnapshot1 = task2.getResult().getDocuments().get(x);
                                                    //Check if job is completed
                                                    if (documentSnapshot1.getBoolean(Constants.KEY_COMPLETED)) {
                                                        Completedjobs_array.add(documentSnapshot1.getId());
                                                    }
                                                }
                                                preferenceManager.putStringArray(Constants.KEY_COMPLETED_JOBS, Completedjobs_array);
                                                // Log.d("JOBS", String.valueOf(preferenceManager.getStringArray(Constants.KEY_POSTED_JOBS).size()));

                                            }
                                        });

                                int value = preferenceManager.getInt(Constants.KEY_PROFILECOMPLETION_VALUE) * 7;
                                if(value < 91 ){
                                    Intent HomeAct = new Intent(getApplicationContext(),Home_Activity.class);
                                    HomeAct.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(HomeAct);

                                    Intent EditprofileAct = new Intent(getApplicationContext(),Editprofile_Activity.class);
                                    startActivity(EditprofileAct);
                                }else {

                                    Intent LoginAct = new Intent(getApplicationContext(),MainActivity.class);
                                    LoginAct.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(LoginAct);

                                    Intent HomeAct = new Intent(getApplicationContext(),Home_Activity.class);
                                    //HomeAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                    startActivity(HomeAct);
                                }

                                // Intent Homeactivity = new Intent(getApplicationContext(), Home_Activity.class);
                                //startActivity(Homeactivity);
                                // showToast("WELCOME BACK", 1);
                            } else {

                            }
                        });
            }else{
                Intent LoginInt = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(LoginInt);
                finish();
            }

        }
    },SPLASH_SCREEN_TIMEOUT);
    }
}