package com.appstechio.workyzo.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.appstechio.workyzo.R;

import com.appstechio.workyzo.adapters.JobsAdapter;

import com.appstechio.workyzo.interfaces.Display_Toasts;
import com.appstechio.workyzo.models.Job;

import com.appstechio.workyzo.models.User;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.appstechio.workyzo.databinding.FragmentJobsTabBinding;
import com.google.firebase.firestore.QuerySnapshot;
import com.hbb20.CountryCodePicker;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Jobs_Tab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Jobs_Tab extends Fragment implements Display_Toasts {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Jobs_Tab() {
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
    public static Jobs_Tab newInstance(String param1, String param2) {
        Jobs_Tab fragment = new Jobs_Tab();
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
    private FragmentJobsTabBinding binding;
    ArrayAdapter<String> adapteritems;
    ArrayAdapter<String> adapteritemsskills;
    private  ArrayList<String> jobs_posted = new ArrayList<>();
    ArrayList<String> filterskills_list = new ArrayList<>();

    private int radius_value = 50;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=  FragmentJobsTabBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


      /*  try {
            getJobs(view);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        search_Job(view);
        Filter_Jobs(binding);

     /*   binding.RefreshJobsSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                try {
                    getJobs(view);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                binding.RefreshJobsSwipe.setRefreshing(false);
            }
        });*/

        Jobslistener(view);
        return view;

    }


    private void Jobslistener (View view){

        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity());
        String currentUserID = preferenceManager.getString(Constants.KEY_USERID);

        database.collection(Constants.KEY_COLLECTION_JOBS)
                .whereNotEqualTo(Constants.KEY_EMPLOYERID,currentUserID)
                .whereEqualTo(Constants.KEY_COMPLETED,false)
                .whereEqualTo(Constants.KEY_JOB_FREELANCER_HIRED,"")
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
                                jobs_posted.add(doc.getId());
                            }
                        }
                        Log.d(TAG, "Current Jobs " + jobs_posted.size());
//                        Log.d(TAG, "Current jobs " + JobPostsList.size());
                        if(jobs_posted.size() > 0 ){
                            try {
                                getJobs(view);
                            } catch (ParseException parseException) {
                                parseException.printStackTrace();
                            }
                        }else{
                            Jobs_loading(false);
                            binding.FilterjobLabel.setVisibility(View.INVISIBLE);
                            binding.FilterFreelancerBtn.setVisibility(View.INVISIBLE);
                            binding.resultsRangeJobslabel.setText(new StringBuilder().append("Showing ").append(jobs_posted.size()).append(" results").toString());
                        }
                    }
                });
    }

    // DISPLAY ALL JOBS IN THE DATABASE
    public void getJobs(View view) throws ParseException {
        Jobs_loading(true);
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity());
        String currentUserID_Post = preferenceManager.getString(Constants.KEY_USERID);
        JobPostsList = new ArrayList<>();
        database.collection(Constants.KEY_COLLECTION_JOBS)
                .whereNotEqualTo(Constants.KEY_EMPLOYERID,currentUserID_Post)
                .whereEqualTo(Constants.KEY_COMPLETED,false)
                .whereEqualTo(Constants.KEY_JOB_FREELANCER_HIRED,"")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                           // String JobDocumentId = queryDocumentSnapshot.getId().substring(0,28);

                           // if (currentUserID_Post.equals(JobDocumentId)) {
                            //    continue;
                            //}
                            Job jobPost = new Job();
                            //jobPost.setEmployer_ID(queryDocumentSnapshot.getId().substring(0,28));
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
                            //Log.d("JOBS", String.valueOf(jobPost.getUploadedFiles()));
                        }

                    }

                    if (JobPostsList.size() > 0) {
                        binding.NodataImage.setVisibility(View.GONE);
                        jobsAdapter = new JobsAdapter(JobPostsList);
                        DividerItemDecoration itemDecor = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
                        binding.JobsPostRCV.addItemDecoration(itemDecor);
                        binding.JobsPostRCV.setLayoutManager(new LinearLayoutManager(this.getContext()));
                        binding.JobsPostRCV.setAdapter(jobsAdapter);
                        Jobs_loading(false);
                        binding.FilterjobLabel.setVisibility(View.VISIBLE);
                        binding.FilterFreelancerBtn.setVisibility(View.VISIBLE);
                        if(JobPostsList.size() == 1){
                            binding.resultsRangeJobslabel.setText(new StringBuilder().append("Showing ").append(JobPostsList.size()).append(" result").toString());
                        }else {
                            binding.resultsRangeJobslabel.setText(new StringBuilder().append("Showing ").append(JobPostsList.size()).append(" results").toString());
                        }

                    } else {
                        jobsAdapter = new JobsAdapter(JobPostsList);
                        binding.JobsPostRCV.setAdapter(jobsAdapter);
                        Jobs_loading(false);
                        binding.NodataImage.setVisibility(View.VISIBLE);
                        binding.FilterjobLabel.setVisibility(View.INVISIBLE);
                        binding.FilterFreelancerBtn.setVisibility(View.INVISIBLE);
                        binding.resultsRangeJobslabel.setText(new StringBuilder().append("Showing ").append(JobPostsList.size()).append(" results").toString());

                    }
                });
    }


    void showFilterDialog(){

        final Dialog dialog = new Dialog(binding.getRoot().getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.filterjobs_dialog);

        Button Filter= dialog.findViewById(R.id.Filter_btn);

        CheckBox checkBox = dialog.findViewById(R.id.checkBox);
        CountryCodePicker countryCodePicker = dialog.findViewById(R.id.ccp_picker);

        CheckBox checkBox1 = dialog.findViewById(R.id.checkBox2);
       // Switch nearby_switch = dialog.findViewById(R.id.switch2);
        SeekBar seekBar = dialog.findViewById(R.id.seekBar);
        TextView radius_txt = dialog.findViewById(R.id.Radius_value);

        CheckBox checkBox2 = dialog.findViewById(R.id.checkBox3);
        TextInputLayout textInputLayout = dialog.findViewById(R.id.inputsalarytype);
        AutoCompleteTextView autoCompleteTextView = dialog.findViewById(R.id.autoCompletesalarytype);
        TextInputLayout textInputLayout1 = dialog.findViewById(R.id.inputminimumbudgetsalary);
        TextInputLayout textInputLayout2 = dialog.findViewById(R.id.inputmaximumbudgetsalary);

        CheckBox checkBox3 = dialog.findViewById(R.id.checkBox4);
        TextInputLayout textInputLayout3 = dialog.findViewById(R.id.inputJobSkillsRequired);
        AutoCompleteTextView autoCompleteskills = dialog.findViewById(R.id.autoCompleteJobSkillsRequired);
        ChipGroup chipGroup = dialog.findViewById(R.id.SkillrequiredChipgroup);

        if(checkBox.isChecked()){
            countryCodePicker.setVisibility(View.VISIBLE);
        }else{
            countryCodePicker.setVisibility(View.INVISIBLE);
        }

        if(checkBox1.isChecked()){
           // nearby_switch.setEnabled(true);
            seekBar.setEnabled(true);
        }else{
            seekBar.setEnabled(false);
            //nearby_switch.setEnabled(false);
        }



    /*    nearby_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    nearby_switch.setText("On");
                }else {
                    nearby_switch.setText("Off");
                }
            }
        });*/

        if(checkBox2.isChecked()){
            textInputLayout.setEnabled(true);
            textInputLayout1.setEnabled(true);
            textInputLayout2.setEnabled(true);
        }else{
            textInputLayout.setEnabled(false);
            textInputLayout1.setEnabled(false);
            textInputLayout2.setEnabled(false);
        }

        String[] salarytype = {"Fixed price", "Hourly rate"};

        adapteritems = new ArrayAdapter<String>(binding.getRoot().getContext(), R.layout.list_item, salarytype);
        autoCompleteTextView.setAdapter(adapteritems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();

                if (autoCompleteTextView.getText().toString().equals("Hourly rate")) {
                    textInputLayout1.setSuffixText("USD/hour");
                    textInputLayout2.setSuffixText("USD/hour");
                }else{
                    textInputLayout1.setSuffixText("USD");
                    textInputLayout2.setSuffixText("USD");
                }
            }
        });

        if(checkBox3.isChecked()){
            textInputLayout3.setEnabled(true);
        }else{
            textInputLayout3.setEnabled(false);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    countryCodePicker.setVisibility(View.VISIBLE);
                }else {
                    countryCodePicker.setVisibility(View.INVISIBLE);
                }
            }
        });

        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    seekBar.setEnabled(true);
                   // nearby_switch.setEnabled(true);
                }else {
                    seekBar.setEnabled(false);
                   // nearby_switch.setEnabled(false);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                radius_txt.setText(new StringBuilder().append(i).append(" Km").toString());
                radius_value = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    textInputLayout.setEnabled(true);
                    textInputLayout1.setEnabled(true);
                    textInputLayout2.setEnabled(true);

                }else {
                    autoCompleteTextView.clearListSelection();
                    textInputLayout.setEnabled(false);
                    textInputLayout1.setEnabled(false);
                    textInputLayout2.setEnabled(false);
                }
            }
        });


        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    textInputLayout3.setEnabled(true);

                }else {
                    textInputLayout3.setEnabled(false);

                }
            }
        });

        List<String> list = new ArrayList<>(Arrays.asList(Constants.computerskillslabels)); //returns a list view of an array
        //returns a list view of str2 and adds all elements of str2 into list
        list.addAll(Arrays.asList(Constants.Writingskillslabels));
        list.addAll(Arrays.asList(Constants.DesignMediaskillslabels));
        list.addAll(Arrays.asList(Constants.DataEntryskillslabels));
        list.addAll(Arrays.asList(Constants.Engineeringskillslabels));
        list.addAll(Arrays.asList(Constants.Salesskillslabels));
        list.addAll(Arrays.asList(Constants.Businessskillslabels));
        list.addAll(Arrays.asList(Constants.ProductSourcingskillslabels));
        list.addAll(Arrays.asList(Constants.Mobileskillslabels));
        list.addAll(Arrays.asList(Constants.Translationskillslabels));
        list.addAll(Arrays.asList(Constants.Servicesskillslabels));
        list.addAll(Arrays.asList(Constants.shipping_transportationskillslabels));
        list.addAll(Arrays.asList(Constants.Telecommunicationsskillslabels));
        list.addAll(Arrays.asList(Constants.Educationskillslabels));
        list.addAll(Arrays.asList(Constants.Otherskillslabels));

        Object[] Skills_list = list.toArray();


        adapteritemsskills = new ArrayAdapter(binding.getRoot().getContext(), R.layout.list_item, Skills_list);
        autoCompleteskills.setAdapter(adapteritemsskills);

        autoCompleteskills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();

                String userinput = autoCompleteskills.getText().toString();
                Chip chip = new Chip(getContext());
                chip.setText(userinput);
                chip.setSingleLine(true);
                chip.setTextSize(14);
                chip.setCloseIconVisible(true);
                chip.setClickable(true);

                if(filterskills_list.size() < 5){
                    if (filterskills_list.contains(userinput)){
                        // showToast("Skill already selected",1);
                    }else{
                        chipGroup.addView(chip);
                        chipGroup.setVisibility(View.VISIBLE);
                        filterskills_list.add(chip.getText().toString());
                        //Log.d("CHIP", String.valueOf(Constants.LanguageChip_list));
                    }
                }

                autoCompleteskills.getText().clear();
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        filterskills_list.remove(chip.getText().toString());
                        chipGroup.removeView(chip);
                    }
                });
            }
        });
        dialog.show();

        Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkBox.isChecked() && !checkBox1.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked()){
                    try {
                        getJobs(view);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(checkBox.isChecked()){
                    Filterbycountry(view,countryCodePicker);
                }else if(checkBox1.isChecked()){
                    FilterbyLocation(view);
                } else if(checkBox2.isChecked()) {
                    float min = Float.parseFloat(textInputLayout1.getEditText().getText().toString());
                    float max = Float.parseFloat(textInputLayout2.getEditText().getText().toString());
                    Filterbysalary(view,autoCompleteTextView,min,max);
                   // ArrayList<User> arrayList = Filterbysalary(view,autoCompleteTextView,min,max);
                }else if (checkBox3.isChecked()){
                    String[] skillslist = filterskills_list.toArray(new String[0]);
                   Filterbyskills(view,skillslist);
                }


                dialog.dismiss();
            }
        });
    }

    public void Filter_Jobs(FragmentJobsTabBinding binding) {
        binding.FilterFreelancerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterskills_list.clear();
                showFilterDialog();
            }
        });

    }


    public void Filterbyskills(View view,String[] skills){

        JobPostsList.clear();

        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity());
        String currentUserID = preferenceManager.getString(Constants.KEY_USERID);


        Jobs_loading(true);
        database.collection(Constants.KEY_COLLECTION_JOBS)
                .whereArrayContainsAny(Constants.KEY_JOB_SKILLS_REQUIRED,Arrays.asList(skills))
                .whereNotEqualTo(Constants.KEY_EMPLOYERID,currentUserID)
                .whereEqualTo(Constants.KEY_COMPLETED,false)
                .whereEqualTo(Constants.KEY_JOB_FREELANCER_HIRED,"")
                .get()
                .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null) {
                                JobPostsList = new ArrayList<>();
                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {


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

                    if (JobPostsList.size() > 0) {
                        binding.NodataImage.setVisibility(View.GONE);
                        jobsAdapter = new JobsAdapter(JobPostsList);
                        // DividerItemDecoration itemDecor = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
                        // binding.JobsPostRCV.addItemDecoration(itemDecor);
                        // binding.JobsPostRCV.setLayoutManager(new LinearLayoutManager(this.getContext()));
                        binding.JobsPostRCV.setAdapter(jobsAdapter);
                        Jobs_loading(false);
                        binding.resultsRangeJobslabel.setText(new StringBuilder().append("Showing ").append(JobPostsList.size()).append(" results").toString());

                    } else {
                        Jobs_loading(false);
                        jobsAdapter = new JobsAdapter(JobPostsList);
                        binding.JobsPostRCV.setAdapter(jobsAdapter);
                        binding.NodataImage.setVisibility(View.VISIBLE);
                        binding.resultsRangeJobslabel.setText(new StringBuilder().append("Showing ").append(JobPostsList.size()).append(" results").toString());

                    }
                        });

        Constants.JobRequiredSkills_Array.clear();
    }



    public void Filterbysalary(View view,AutoCompleteTextView autoCompleteTextView,float min ,float max ){
        JobPostsList.clear();
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity());
        String currentUserID = preferenceManager.getString(Constants.KEY_USERID);
        //CountryCodePicker countryCodePicker = (CountryCodePicker) view.findViewById(R.id.ccp_picker);

        Jobs_loading(true);
        database.collection(Constants.KEY_COLLECTION_JOBS)
                .whereNotEqualTo(Constants.KEY_EMPLOYERID,currentUserID)
                .whereEqualTo(Constants.KEY_COMPLETED,false)
                .whereEqualTo(Constants.KEY_JOB_FREELANCER_HIRED,"")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        //freelancersusersList = new ArrayList<>();

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {



                            Map<String, Object> Salary_map = (Map<String, Object>) queryDocumentSnapshot.get(Constants.KEY_JOB_PAYMENT);
                            float min_amount = Float.parseFloat(Salary_map.get(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET).toString());
                            float max_amount = Float.parseFloat(Salary_map.get(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET).toString());
                            //System.out.println(Address_map.get(Constants.KEY_COUNTRY).toString());
                            if (Salary_map.get(Constants.KEY_JOB_PAYMENT_TYPE).toString().equals(autoCompleteTextView.getText().toString())
                                    && min_amount == min  && max_amount == max) {

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
                    }

                    if (JobPostsList.size() > 0) {
                        binding.NodataImage.setVisibility(View.GONE);
                        jobsAdapter = new JobsAdapter(JobPostsList);
                        // DividerItemDecoration itemDecor = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
                        // binding.JobsPostRCV.addItemDecoration(itemDecor);
                        // binding.JobsPostRCV.setLayoutManager(new LinearLayoutManager(this.getContext()));
                        binding.JobsPostRCV.setAdapter(jobsAdapter);
                        Jobs_loading(false);
                        binding.resultsRangeJobslabel.setText(new StringBuilder().append("Showing ").append(JobPostsList.size()).append(" results").toString());

                    } else {
                        Jobs_loading(false);
                        jobsAdapter = new JobsAdapter(JobPostsList);
                        binding.JobsPostRCV.setAdapter(jobsAdapter);
                        binding.NodataImage.setVisibility(View.VISIBLE);
                        binding.resultsRangeJobslabel.setText(new StringBuilder().append("Showing ").append(JobPostsList.size()).append(" results").toString());

                    }
                });

    }



    public void FilterbyLocation(View view){
       // freelancersusersList.clear();
        JobPostsList.clear();
        ArrayList<String> freelancersusersList = new ArrayList<>();
        database = FirebaseFirestore.getInstance();
        // Find Users within 50km of the user signed in
        final GeoLocation center = new GeoLocation(preferenceManager.getDouble("Latitude"), preferenceManager.getDouble("Longtitude"));

        final double radiusInM = radius_value * 1000;


        // Each item in 'bounds' represents a startAt/endAt pair. We have to issue
        // a separate query for each pair. There can be up to 9 pairs of bounds
        // depending on overlap, but in most cases there are 4.
        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM);
        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (GeoQueryBounds b : bounds) {
            Query q = database.collection(Constants.KEY_COLLECTION_USERS)
                    .orderBy("geohash")
                    .startAt(b.startHash)
                    .endAt(b.endHash);

            tasks.add(q.get());
        }

// Collect all the query results together into a single list
        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> t) {
                        List<DocumentSnapshot> matchingDocs = new ArrayList<>();

                        for (Task<QuerySnapshot> task : tasks) {
                            QuerySnapshot snap = task.getResult();
                            for (DocumentSnapshot doc : snap.getDocuments()) {
                                double lat = doc.getDouble("Latitude");
                                double lng = doc.getDouble("Longtitude");

                                // We have to filter out a few false positives due to GeoHash
                                // accuracy, but most will match
                                GeoLocation docLocation = new GeoLocation(lat, lng);
                                double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
                                if (distanceInM <= radiusInM) {
                                    matchingDocs.add(doc);
                                }
                            }
                        }

                        // matchingDocs contains the results
                        // ...
                        System.out.println("Nearby Users: " + matchingDocs);
                        String currentUserID = preferenceManager.getString(Constants.KEY_USERID);
                        for (int i = 0; i < matchingDocs.size(); i++) {
                            if (currentUserID.equals(matchingDocs.get(i).getId())) {
                                continue;
                            }
                            User user = new User();
                            user.setUserId(matchingDocs.get(i).getId());

                            freelancersusersList.add(user.getUserId());
                        }


                        Jobs_loading(true);
                        database = FirebaseFirestore.getInstance();
                        preferenceManager = new PreferenceManager(getActivity());
                        String currentUserID_Post = preferenceManager.getString(Constants.KEY_USERID);

                        database.collection(Constants.KEY_COLLECTION_JOBS)
                                .whereNotEqualTo(Constants.KEY_EMPLOYERID,currentUserID_Post)
                                .whereEqualTo(Constants.KEY_COMPLETED,false)
                                .whereEqualTo(Constants.KEY_JOB_FREELANCER_HIRED,"")
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful() && task.getResult() != null) {
                                        JobPostsList = new ArrayList<>();
                                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                                            for(int i =0;i<freelancersusersList.size();i++){

                                                if(queryDocumentSnapshot.getString(Constants.KEY_EMPLOYERID).equals(freelancersusersList.get(i))){

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
                                            //Log.d("JOBS", String.valueOf(jobPost.getUploadedFiles()));
                                        }

                                    }

                                    if (JobPostsList.size() > 0) {
                                        binding.NodataImage.setVisibility(View.GONE);
                                        jobsAdapter = new JobsAdapter(JobPostsList);
                                        // DividerItemDecoration itemDecor = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
                                        // binding.JobsPostRCV.addItemDecoration(itemDecor);
                                        // binding.JobsPostRCV.setLayoutManager(new LinearLayoutManager(this.getContext()));
                                        binding.JobsPostRCV.setAdapter(jobsAdapter);
                                        Jobs_loading(false);
                                        binding.resultsRangeJobslabel.setText(new StringBuilder().append("Showing ").append(JobPostsList.size()).append(" results").toString());

                                    } else {
                                        Jobs_loading(false);
                                        jobsAdapter = new JobsAdapter(JobPostsList);
                                        binding.JobsPostRCV.setAdapter(jobsAdapter);
                                        binding.NodataImage.setVisibility(View.VISIBLE);
                                        binding.resultsRangeJobslabel.setText(new StringBuilder().append("Showing ").append(JobPostsList.size()).append(" results").toString());

                                    }
                                });


                    }


                });




    }


    public void Filterbycountry(View view,CountryCodePicker countryCodePicker){
        JobPostsList.clear();
        ArrayList<String> freelancersusersList = new ArrayList<>();
        freelancersusersList.clear();
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity());
        String currentUserID = preferenceManager.getString(Constants.KEY_USERID);
       // Jobs_loading(true);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereNotEqualTo(FieldPath.documentId(),currentUserID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                       // freelancersusersList = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                         /*   if (currentUserID.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }*/

                            Map<String, Object> Address_map = (Map<String, Object>) queryDocumentSnapshot.get(Constants.KEY_ADDRESS);
                           // System.out.println(Address_map.get(Constants.KEY_COUNTRY).toString());
                           // System.out.println("Address " + Address_map.toString());

                            if(Address_map != null){
                                if (Address_map.get(Constants.KEY_COUNTRY).toString().equals(countryCodePicker.getSelectedCountryName())) {

                                    User user = new User();
                                    user.setUserId(queryDocumentSnapshot.getId());

                                    freelancersusersList.add(user.getUserId());
                                }
                            }

                        }
                    }

                    Jobs_loading(true);
                    database = FirebaseFirestore.getInstance();
                    preferenceManager = new PreferenceManager(getActivity());
                    String currentUserID_Post = preferenceManager.getString(Constants.KEY_USERID);

                    database.collection(Constants.KEY_COLLECTION_JOBS)
                            .whereNotEqualTo(Constants.KEY_EMPLOYERID,currentUserID_Post)
                            .whereEqualTo(Constants.KEY_COMPLETED,false)
                            .whereEqualTo(Constants.KEY_JOB_FREELANCER_HIRED,"")
                            .get()
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful() && task1.getResult() != null) {
                                    JobPostsList = new ArrayList<>();
                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task1.getResult()) {

                                        for(int i =0;i<freelancersusersList.size();i++){

                                            if(queryDocumentSnapshot.getString(Constants.KEY_EMPLOYERID).equals(freelancersusersList.get(i))){

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
                                        //Log.d("JOBS", String.valueOf(jobPost.getUploadedFiles()));
                                    }

                                }

                                if (JobPostsList.size() > 0) {
                                    binding.NodataImage.setVisibility(View.GONE);
                                    jobsAdapter = new JobsAdapter(JobPostsList);
                                    binding.JobsPostRCV.setAdapter(jobsAdapter);
                                    Jobs_loading(false);
                                    binding.resultsRangeJobslabel.setText(new StringBuilder().append("Showing ").append(JobPostsList.size()).append(" results").toString());

                                } else {
                                    Jobs_loading(false);
                                    jobsAdapter = new JobsAdapter(JobPostsList);
                                    binding.JobsPostRCV.setAdapter(jobsAdapter);
                                    binding.NodataImage.setVisibility(View.VISIBLE);
                                    binding.resultsRangeJobslabel.setText(new StringBuilder().append("Showing ").append(JobPostsList.size()).append(" results").toString());

                                }
                            });

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
                binding.JobsPostRCV.setAdapter(jobsAdapter);
                if(binding.JobsPostRCV.getAdapter().getItemCount() == 0){
                    //do nothing
                }else{
                    filter(editable.toString());
                    binding.JobsPostRCV.setAdapter(jobsAdapter);

                    String searchinput = binding.searchboxJobstxt.getText().toString().trim();
                    if (searchinput.isEmpty()) {
                        jobsAdapter = new JobsAdapter(JobPostsList);
                        binding.JobsPostRCV.setAdapter(jobsAdapter);
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
            binding.JobsPostRCV.setVisibility(View.INVISIBLE);
            binding.searchboxJobs.setVisibility(View.INVISIBLE);
            binding.resultsRangeJobslabel.setVisibility(View.INVISIBLE);
            binding.FilterFreelancerBtn.setVisibility(View.INVISIBLE);
            binding.TopresultsJobslabel.setVisibility(View.INVISIBLE);
            binding.FilterjobLabel.setVisibility(View.INVISIBLE);

           binding.JobsProgressbar.setVisibility(View.VISIBLE);

        }else{
            binding.JobsPostRCV.setVisibility(View.VISIBLE);
            binding.searchboxJobs.setVisibility(View.VISIBLE);
            binding.resultsRangeJobslabel.setVisibility(View.VISIBLE);
            binding.FilterFreelancerBtn.setVisibility(View.VISIBLE);
            binding.TopresultsJobslabel.setVisibility(View.VISIBLE);
            binding.FilterjobLabel.setVisibility(View.VISIBLE);
            binding.NodataImage.setVisibility(View.VISIBLE);

            binding.JobsProgressbar.setVisibility(View.INVISIBLE);

        }
    }



    @Override
    public void onResume() {
        super.onResume();

        View view = binding.getRoot().getRootView();

    /*    try {
            getJobs(view);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void showToast(String message, int duration) {

    }
}