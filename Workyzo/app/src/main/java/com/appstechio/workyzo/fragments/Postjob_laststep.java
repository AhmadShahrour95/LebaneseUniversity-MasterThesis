package com.appstechio.workyzo.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appstechio.workyzo.R;
import com.appstechio.workyzo.activities.Home_Activity;
import com.appstechio.workyzo.databinding.FragmentPostjobLaststepBinding;
import com.appstechio.workyzo.interfaces.Display_Toasts;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import kr.co.prnd.StepProgressBar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Postjob_laststep#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Postjob_laststep extends Fragment implements Display_Toasts {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Postjob_laststep() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Postjob_firststep.
     */
    // TODO: Rename and change types and number of parameters
    public static Postjob_laststep newInstance(String param1, String param2) {
        Postjob_laststep fragment = new Postjob_laststep();
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

    private static int  a = 0;
    private PreferenceManager preferenceManager;
    private String filename = null;
    private static  int Salarytye_Flag = 0 ;
    private FragmentPostjobLaststepBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      binding =FragmentPostjobLaststepBinding.inflate(inflater, container, false);
      View view = binding.getRoot();
        Click_SubmitPostJob_Btn(view);
        ClickBack_Btn(view);
        //Load_JobDetailsForConfirmation(view);
      return view;
    }



    private void Load_JobDetailsForConfirmation(View view) {
        preferenceManager = new PreferenceManager(getActivity());
        binding.EmployerJobTitle.setText(preferenceManager.getString(Constants.KEY_JOB_TITLE));
        binding.EmployerjobTasksdescription.setText(preferenceManager.getString(Constants.KEY_JOB_DESCRIPTION));
        if (preferenceManager.getString(Constants.KEY_JOB_PAYMENT_TYPE).equals("Fixed price")) {
            binding.EmployerJobSalarypaid.setText(new StringBuilder().append(preferenceManager.getString(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET)).append(" - ").append(preferenceManager.getString(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET)).append(" USD").toString());
        } else {
            binding.EmployerJobSalarypaid.setText(new StringBuilder().append(preferenceManager.getString(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET)).append(" - ").append(preferenceManager.getString(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET)).append(" USD per hour").toString());
        }

        ArrayList<String> skills = preferenceManager.getStringArray(Constants.KEY_JOB_SKILLS_REQUIRED);


        if (skills == null) {
            // jobsRcvBinding.employerjobRequiredSkills.setVisibility(View.GONE);
        } else {
            // jobsRcvBinding.employerjobRequiredSkills.setVisibility(View.VISIBLE);

            for (int a = 0; a < skills.size(); a++) {
                Chip chip = new Chip(binding.getRoot().getContext());
                chip.setText(skills.get(a).toString());
                chip.setSingleLine(true);
                chip.setTextSize(14);
                binding.SkillsChipGroup.addView(chip);

            }
        }
    }

    private void Click_SubmitPostJob_Btn (View view){
        binding.SubmitPostJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.JobRequiredSkills_Array.clear();
                preferenceManager = new PreferenceManager(getActivity());

                if(Constants.EDITPOST_FLAG){

                    //No Files
                    if(Constants.Mediafiles_Uploaded == null || Constants.Mediafiles_Uploaded.size() == 0 ){

                        FirebaseFirestore database = FirebaseFirestore.getInstance();
                        // database.collection(Constants.KEY_COLLECTION_JOBS).document(preferenceManager.getString(Constants.KEY_USERID)+"@"+uniqueJobID)

                        HashMap<String,Object> Budget = new HashMap<String,Object>();
                        Budget.put(Constants.KEY_JOB_PAYMENT_TYPE,preferenceManager.getString(Constants.KEY_JOB_PAYMENT_TYPE));
                        Budget.put(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET,preferenceManager.getString(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET));
                        Budget.put(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET,preferenceManager.getString(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET));


                        SimpleDateFormat formatter = new SimpleDateFormat("MMMM,dd,yyyy - hh:mm a");
                        Date date = new Date();
                        // format date to string
                        String Post_createdDate = formatter.format(date);

                        DocumentReference documentReference =
                                database.collection(Constants.KEY_COLLECTION_JOBS).document(
                                        preferenceManager.getString(Constants.KEY_JOB_ID_UPDATE));

                        documentReference
                                .update(Constants.KEY_EMPLOYERID,preferenceManager.getString(Constants.KEY_USERID),
                                        Constants.KEY_JOB_TITLE,preferenceManager.getString(Constants.KEY_JOB_TITLE),
                                        Constants.KEY_JOB_DESCRIPTION,preferenceManager.getString(Constants.KEY_JOB_DESCRIPTION),
                                        Constants.KEY_JOB_CREATED_DATE,Post_createdDate,
                                        Constants.KEY_JOB_SKILLS_REQUIRED,preferenceManager.getStringArray(Constants.KEY_JOB_SKILLS_REQUIRED),
                                        Constants.KEY_JOB_PAYMENT,Budget
                                        //Constants.KEY_JOB_UPLOADED_FILES,preferenceManager.getMapArray("ATTACHMENTS")
                                )
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        //Constants.Files_Map.clear();
                                        showToast("Job is updated successfully",1);
                                        Context context = view.getContext();
                                        Intent intent = new Intent(context, Home_Activity.class);
                                        startActivity(intent);
                                        //getActivity().finish();


                                    }
                                })
                                .addOnFailureListener(exception -> {
                                    // Signup_loading(false);
                                    Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                    }else{
                            //With Files
                        for (int i = 0; i<  Constants.Mediafiles_Uploaded.size();i++){
                            filename =  Constants.Mediafiles_Uploaded.get(i).getName();
                            Uri Fileuri =  Constants.Mediafiles_Uploaded.get(i).getUri();

                            StorageReference Folder = FirebaseStorage.getInstance().getReference().child(preferenceManager.getString(Constants.KEY_USERNAME) + " Files");
                            StorageReference File = Folder.child(filename);
                            int file_sequence = i;
                            File.putFile(Fileuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    File.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            Constants.Files_Map.get(file_sequence).put("File_url",uri.toString());
                                            System.out.println(Constants.Files_Map);
                                            preferenceManager.putMapArray("ATTACHMENTS", Constants.Files_Map);

                                            if(Constants.Mediafiles_Uploaded.size() - file_sequence == 1){
                                                HashMap<String,Object> Budget = new HashMap<String,Object>();
                                                Budget.put(Constants.KEY_JOB_PAYMENT_TYPE,preferenceManager.getString(Constants.KEY_JOB_PAYMENT_TYPE));
                                                Budget.put(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET,preferenceManager.getString(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET));
                                                Budget.put(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET,preferenceManager.getString(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET));

                                                SimpleDateFormat formatter = new SimpleDateFormat("MMMM,dd,yyyy - hh:mm a");
                                                Date date = new Date();
                                                // format date to string
                                                String Post_createdDate = formatter.format(date);

                                                FirebaseFirestore database = FirebaseFirestore.getInstance();

                                                DocumentReference documentReference =
                                                        database.collection(Constants.KEY_COLLECTION_JOBS).document(
                                                                preferenceManager.getString(Constants.KEY_JOB_ID_UPDATE));

                                                documentReference
                                                        .update(Constants.KEY_EMPLOYERID,preferenceManager.getString(Constants.KEY_USERID),
                                                                Constants.KEY_JOB_TITLE,preferenceManager.getString(Constants.KEY_JOB_TITLE),
                                                                Constants.KEY_JOB_DESCRIPTION,preferenceManager.getString(Constants.KEY_JOB_DESCRIPTION),
                                                                Constants.KEY_JOB_CREATED_DATE,Post_createdDate,
                                                                Constants.KEY_JOB_SKILLS_REQUIRED,preferenceManager.getStringArray(Constants.KEY_JOB_SKILLS_REQUIRED),
                                                                Constants.KEY_JOB_PAYMENT,Budget,
                                                                Constants.KEY_JOB_UPLOADED_FILES,preferenceManager.getMapArray("ATTACHMENTS")
                                                        )
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                //Constants.Files_Map.clear();
                                                                showToast("Job is updated successfully",1);
                                                                Context context = view.getContext();
                                                                Intent intent = new Intent(context, Home_Activity.class);
                                                                startActivity(intent);
                                                                //getActivity().finish();


                                                            }
                                                        })
                                                        .addOnFailureListener(exception -> {
                                                            // Signup_loading(false);
                                                            Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                                                        });

                                            }
                                        }

                                    });
                                }
                            });

                        }
                    }


                }else {

                    if(Constants.Mediafiles_Uploaded == null || Constants.Mediafiles_Uploaded.size() == 0 ){
                        HashMap<String,Object> Budget = new HashMap<String,Object>();

                        Double Converted_MinBudget = Double.parseDouble(preferenceManager.getString(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET));
                        Double Converted_MaxBudget = Double.parseDouble(preferenceManager.getString(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET));
                        Budget.put(Constants.KEY_JOB_PAYMENT_TYPE,preferenceManager.getString(Constants.KEY_JOB_PAYMENT_TYPE));
                        Budget.put(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET,Converted_MinBudget);
                        Budget.put(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET,Converted_MaxBudget);

                        HashMap<String, Object> Job_Post = new HashMap<>();
                        Job_Post.put(Constants.KEY_JOB_TITLE,preferenceManager.getString(Constants.KEY_JOB_TITLE));
                        Job_Post.put(Constants.KEY_JOB_DESCRIPTION,preferenceManager.getString(Constants.KEY_JOB_DESCRIPTION));
                        SimpleDateFormat formatter = new SimpleDateFormat("MMMM,dd,yyyy - hh:mm a");
                        Date date = new Date();
                        // format date to string
                        String Post_createdDate = formatter.format(date);
                        Job_Post.put(Constants.KEY_EMPLOYERID,preferenceManager.getString(Constants.KEY_USERID));
                        Job_Post.put(Constants.KEY_JOB_CREATED_DATE,Post_createdDate);
                        Job_Post.put(Constants.KEY_JOB_SKILLS_REQUIRED,preferenceManager.getStringArray(Constants.KEY_JOB_SKILLS_REQUIRED));
                        Job_Post.put(Constants.KEY_JOB_PAYMENT,Budget);
                        Job_Post.put(Constants.KEY_JOB_PROPOSAL,new ArrayList<HashMap>());

                       // Job_Post.put(Constants.KEY_JOB_UPLOADED_FILES,preferenceManager.getMapArray("ATTACHMENTS"));
                        Job_Post.put(Constants.KEY_COMPLETED,false);
                        Job_Post.put(Constants.KEY_JOB_FREELANCER_HIRED,"");

                        FirebaseFirestore database = FirebaseFirestore.getInstance();
                        database.collection(Constants.KEY_COLLECTION_JOBS)
                                .document()
                                .set(Job_Post)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        //Constants.Files_Map.clear();
                                        Toast.makeText(getActivity(), "Job is added successfully", Toast.LENGTH_SHORT).show();
                                        Context context = view.getContext();
                                        Intent intent = new Intent(context, Home_Activity.class);
                                        startActivity(intent);
                                        //getActivity().finish();

                                    }
                                })
                                .addOnFailureListener(exception -> {
                                    // Signup_loading(false);
                                    showToast(exception.getMessage(),1);
                                });
                    }else{
                        for (int i = 0; i<  Constants.Mediafiles_Uploaded.size();i++){
                            filename =  Constants.Mediafiles_Uploaded.get(i).getName();
                            Uri Fileuri =  Constants.Mediafiles_Uploaded.get(i).getUri();

                            StorageReference Folder = FirebaseStorage.getInstance().getReference().child(preferenceManager.getString(Constants.KEY_USERNAME) + " Files");
                            StorageReference File = Folder.child(filename);
                            System.out.println("Row= "+ i);
                            int file_sequence = i;
                            File.putFile(Fileuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    File.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            System.out.println("Row= "+ file_sequence);
                                            Constants.Files_Map.get(file_sequence).put("File_url",uri.toString());
                                            System.out.println(Constants.Files_Map);


                                            if(Constants.Mediafiles_Uploaded.size() - file_sequence == 1){
                                                preferenceManager.putMapArray("ATTACHMENTS", Constants.Files_Map);
                                                HashMap<String,Object> Budget = new HashMap<String,Object>();

                                                Double Converted_MinBudget = Double.parseDouble(preferenceManager.getString(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET));
                                                Double Converted_MaxBudget = Double.parseDouble(preferenceManager.getString(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET));
                                                Budget.put(Constants.KEY_JOB_PAYMENT_TYPE,preferenceManager.getString(Constants.KEY_JOB_PAYMENT_TYPE));
                                                Budget.put(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET,Converted_MinBudget);
                                                Budget.put(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET,Converted_MaxBudget);


                                                HashMap<String, Object> Job_Post = new HashMap<>();
                                                //user.put(Constants.KEY_USERID,Firebase_user.getUid().toString());
                                                Job_Post.put(Constants.KEY_JOB_TITLE,preferenceManager.getString(Constants.KEY_JOB_TITLE));
                                                Job_Post.put(Constants.KEY_JOB_DESCRIPTION,preferenceManager.getString(Constants.KEY_JOB_DESCRIPTION));

                                                SimpleDateFormat formatter = new SimpleDateFormat("MMMM,dd,yyyy - hh:mm a");
                                                Date date = new Date();
                                                // format date to string
                                                String Post_createdDate = formatter.format(date);
                                                //String uniqueJobID = UUID.randomUUID().toString();
                                                Job_Post.put(Constants.KEY_EMPLOYERID,preferenceManager.getString(Constants.KEY_USERID));
                                                Job_Post.put(Constants.KEY_JOB_CREATED_DATE,Post_createdDate);
                                                Job_Post.put(Constants.KEY_JOB_SKILLS_REQUIRED,preferenceManager.getStringArray(Constants.KEY_JOB_SKILLS_REQUIRED));
                                                Job_Post.put(Constants.KEY_JOB_PAYMENT,Budget);
                                                Job_Post.put(Constants.KEY_JOB_PROPOSAL,new ArrayList<HashMap>());

                                                Job_Post.put(Constants.KEY_JOB_UPLOADED_FILES,preferenceManager.getMapArray("ATTACHMENTS"));
                                                Job_Post.put(Constants.KEY_COMPLETED,false);
                                                Job_Post.put(Constants.KEY_JOB_FREELANCER_HIRED,"");
                                                FirebaseFirestore database = FirebaseFirestore.getInstance();
                                                // database.collection(Constants.KEY_COLLECTION_JOBS).document(preferenceManager.getString(Constants.KEY_USERID)+"@"+uniqueJobID)
                                                database.collection(Constants.KEY_COLLECTION_JOBS)
                                                        .document()
                                                        .set(Job_Post)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                //Constants.Files_Map.clear();
                                                                Toast.makeText(getActivity(), "Job is posted successfully", Toast.LENGTH_LONG).show();
                                                                Context context = view.getContext();
                                                                Intent intent = new Intent(context, Home_Activity.class);
                                                                startActivity(intent);
                                                                //getActivity().finish();

                                                            }
                                                        })
                                                        .addOnFailureListener(exception -> {
                                                            // Signup_loading(false);
                                                            showToast(exception.getMessage(),1);
                                                        });

                                            }
                                        }

                                    });
                                }
                            });

                        }
                    }

                }

            }

        });
    }




    private  void ClickBack_Btn(View view){

        ViewPager2 viewPager2 = getActivity().findViewById(R.id.PostjobViewPager);
        StepProgressBar stepProgressBar = getActivity().findViewById(R.id.StepProgressBar);
        binding.BacktopreviousStep4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.step = Constants.step - 1;

                if(Constants.step == 0){
                    viewPager2.setCurrentItem(0);
                }else if (Constants.step == 1) {
                    viewPager2.setCurrentItem(1);
                } else if (Constants.step == 2) {
                    viewPager2.setCurrentItem(2);
                } else if (Constants.step == 3){
                    viewPager2.setCurrentItem(3);
                } else {
                    viewPager2.setCurrentItem(4);
                }

                stepProgressBar.setStep(Constants.step);

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

    static double convertDouble(Object longValue){
        double valueTwo = -1; // whatever to state invalid!

        if(longValue instanceof Long)
            valueTwo = ((Long) longValue).doubleValue();

        System.out.println(valueTwo);
        return valueTwo;
    }

    @Override
    public void onResume() {
        super.onResume();

        View view = binding.getRoot().getRootView();
        Load_JobDetailsForConfirmation(view);
    }
}