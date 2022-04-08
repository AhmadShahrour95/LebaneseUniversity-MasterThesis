package com.appstechio.workyzo.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.appstechio.workyzo.activities.Freelancerprofile_Activity;
import com.appstechio.workyzo.adapters.JobsAdapter;
import com.appstechio.workyzo.models.User;
import com.appstechio.workyzo.adapters.FreelancerUsersAdapter;
import com.appstechio.workyzo.R;

import com.appstechio.workyzo.databinding.FragmentFreelancersTabBinding;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;
import com.hbb20.CountryCodePicker;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Freelancers_Tab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Freelancers_Tab extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Freelancers_Tab() {
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
    public static Freelancers_Tab newInstance(String param1, String param2) {
        Freelancers_Tab fragment = new Freelancers_Tab();
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


    private FreelancerUsersAdapter userAdapter;
    private ArrayList<User> freelancersusersList;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    private FragmentFreelancersTabBinding binding;
    ArrayAdapter<String> adapteritems;
    ArrayAdapter<String> adapteritemsskills;
    private  ArrayList<String> userslist = new ArrayList<>();
    private int radius_value = 50;
    ArrayList<String> filterskills_list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFreelancersTabBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //database = FirebaseFirestore.getInstance();
       /* try {
            getUsers(binding);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        Filter_Freelancers(binding);

        search_freelancer(view);

    /*    binding.RefreshUsersSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                try {
                    getUsers(view);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                binding.RefreshUsersSwipe.setRefreshing(false);
            }
        });*/

        MyUserslistener(view);
        return view;

    }

    void showFilterDialog(){

        final Dialog dialog = new Dialog(binding.getRoot().getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.filterfreelancer_dialog);

        Button Filter= dialog.findViewById(R.id.Filter_btn);

        CheckBox checkBox = dialog.findViewById(R.id.checkBox);
        CountryCodePicker countryCodePicker = dialog.findViewById(R.id.ccp_picker);

        CheckBox checkBox1 = dialog.findViewById(R.id.checkBox2);
       // Switch nearby_switch = dialog.findViewById(R.id.switch1);
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
            //nearby_switch.setEnabled(true);
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
                   // nearby_switch.setEnabled(true);
                    seekBar.setEnabled(true);
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
                        getUsers(view);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(checkBox.isChecked()){
                    ArrayList<User> arrayList = Filterbycountry(view,countryCodePicker);
                }else if(checkBox1.isChecked()){
                    FilterbyLocation(view);
                } else if(checkBox2.isChecked()) {
                    float min = Float.parseFloat(textInputLayout1.getEditText().getText().toString());
                    float max = Float.parseFloat(textInputLayout2.getEditText().getText().toString());
                    ArrayList<User> arrayList = Filterbysalary(view,autoCompleteTextView,min,max);
                }else if (checkBox3.isChecked()){
                    String[] skillslist = filterskills_list.toArray(new String[0]);
                    ArrayList<User> arrayList = Filterbyskills(view,skillslist);
                }


                dialog.dismiss();
            }
        });
    }

    public void Filter_Freelancers(FragmentFreelancersTabBinding binding) {
        binding.FilterFreelancerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterskills_list.clear();
                showFilterDialog();
            }
        });

    }

    public ArrayList<User> Filterbyskills(View view,String[] skills){
        freelancersusersList.clear();
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity());
        String currentUserID = preferenceManager.getString(Constants.KEY_USERID);


        Users_loading(true);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereArrayContainsAny(Constants.KEY_SKILLS,Arrays.asList(skills))
                .whereNotEqualTo(FieldPath.documentId(),currentUserID)
                .whereEqualTo(Constants.KEY_VISIBLE_ASFREELANCER,true)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        freelancersusersList = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                          /*  if (currentUserID.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }*/

                            //ArrayList<String> skills_map = (ArrayList<String>) queryDocumentSnapshot.get(Constants.KEY_SKILLS);
                          //  if (Address_map.get(Constants.KEY_COUNTRY).toString().equals(countryCodePicker.getSelectedCountryName())) {

                                User user = new User();
                                user.setUserId(queryDocumentSnapshot.getId());
                                user.setUsername(queryDocumentSnapshot.getString(Constants.KEY_USERNAME));
                                user.setFirst_name(queryDocumentSnapshot.getString(Constants.KEY_FIRSTNAME));
                                user.setLast_name(queryDocumentSnapshot.getString(Constants.KEY_LASTNAME));
                                user.setMobile_Number(queryDocumentSnapshot.getString(Constants.KEY_MOBILE_NUMBER));
                                user.setEmail_Address(queryDocumentSnapshot.getString(Constants.KEY_EMAILADDRESS));
                                user.setAddress((Map<String, String>) queryDocumentSnapshot.get(Constants.KEY_ADDRESS));
                                user.setSalary((Map<String, Object>) queryDocumentSnapshot.get(Constants.KEY_SALARY));
                                user.setProfessional_Headline(queryDocumentSnapshot.getString(Constants.KEY_PROFESSIONAL_HEADLINE));
                                user.setUser_Summary(queryDocumentSnapshot.getString(Constants.KEY_USER_SUMMARY));
                                user.setKey(queryDocumentSnapshot.getString("Key"));
                                user.setTop_Skills((ArrayList<String>) queryDocumentSnapshot.get(Constants.KEY_SKILLS));
                                user.setExperience((ArrayList<HashMap>) queryDocumentSnapshot.get(Constants.KEY_EXPERIENCES));
                                user.setEducation((ArrayList<HashMap>) queryDocumentSnapshot.get(Constants.KEY_EDUCATION));
                                user.setLanguages((ArrayList<String>) queryDocumentSnapshot.get(Constants.KEY_LANGUAGES));
                                user.setReviews((ArrayList<HashMap>) queryDocumentSnapshot.get(Constants.KEY_REVIEW));


                                if (queryDocumentSnapshot.getString(Constants.KEY_PROFILE_IMAGE) == null) {
                                    Bitmap myLogo = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.avatar_man);
                                    String encodedimg = encodeImage(myLogo);
                                    user.setProfile_Image(encodedimg);
                                } else {
                                    user.setProfile_Image(queryDocumentSnapshot.getString(Constants.KEY_PROFILE_IMAGE));
                                }

                                user.setToken(queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN));
                            if(queryDocumentSnapshot.getString(Constants.KEY_USERNAME) != "" && queryDocumentSnapshot.getString(Constants.KEY_FIRSTNAME) != "" && queryDocumentSnapshot.getString(Constants.KEY_LASTNAME) != ""
                                    && queryDocumentSnapshot.getString(Constants.KEY_EMAILADDRESS) != "" &&
                                    queryDocumentSnapshot.getString(Constants.KEY_MOBILE_NUMBER)!= null && queryDocumentSnapshot.getString(Constants.KEY_MOBILE_NUMBER) != "" && (Map<String, String>) queryDocumentSnapshot.get(Constants.KEY_ADDRESS) != null && user.getAddress().get(Constants.KEY_CITY).toString() != "" && user.getAddress().get(Constants.KEY_COUNTRY).toString() != ""
                                    && (Map<String, Object>) queryDocumentSnapshot.get(Constants.KEY_SALARY) != null && user.getSalary().get(Constants.KEY_SALARY_AMOUNT).toString() != "" && user.getSalary().get(Constants.KEY_SALARY_TYPE).toString() != ""
                                    && queryDocumentSnapshot.getString(Constants.KEY_PROFESSIONAL_HEADLINE) != null && queryDocumentSnapshot.getString(Constants.KEY_PROFESSIONAL_HEADLINE) != ""
                                    &&  queryDocumentSnapshot.getString(Constants.KEY_USER_SUMMARY) != null && queryDocumentSnapshot.getString(Constants.KEY_USER_SUMMARY) != "" &&
                                    (ArrayList<String>) queryDocumentSnapshot.get(Constants.KEY_SKILLS) != null &&   !((ArrayList<?>) queryDocumentSnapshot.get(Constants.KEY_SKILLS)).isEmpty() && queryDocumentSnapshot.getString(Constants.KEY_PROFILE_IMAGE) != null){

                                freelancersusersList.add(user);
                            }
                            }
                       // }
                    }

                    if (freelancersusersList.size() > 0) {

                        userAdapter = new FreelancerUsersAdapter(freelancersusersList);
                        binding.FreelancersUsersRCV.setAdapter(userAdapter);
                        Users_loading(false);
                        binding.NodatafreelancerImage.setVisibility(View.GONE);

                        if(freelancersusersList.size() == 1){
                            binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(freelancersusersList.size()).append(" result").toString());
                        }else {
                            binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(freelancersusersList.size()).append(" results").toString());
                        }

                    } else {
                        Users_loading(false);
                        userAdapter = new FreelancerUsersAdapter(freelancersusersList);
                        //userAdapter.filterList(freelancersusersList);
                        binding.FreelancersUsersRCV.setAdapter(userAdapter);
                        binding.NodatafreelancerImage.setVisibility(View.VISIBLE);
                        binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(freelancersusersList.size()).append(" results").toString());
                    }

                });
        //Constants.JobRequiredSkills_Array.clear();
        return  freelancersusersList;
    }

    public ArrayList<User> Filterbysalary(View view,AutoCompleteTextView autoCompleteTextView,float min ,float max ){
        freelancersusersList.clear();
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity());
        String currentUserID = preferenceManager.getString(Constants.KEY_USERID);
        //CountryCodePicker countryCodePicker = (CountryCodePicker) view.findViewById(R.id.ccp_picker);

        Users_loading(true);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereNotEqualTo(FieldPath.documentId(),currentUserID)
                .whereEqualTo(Constants.KEY_VISIBLE_ASFREELANCER,true)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        freelancersusersList = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {


                            Map<String, Object> Salary_map = (Map<String, Object>) queryDocumentSnapshot.get(Constants.KEY_SALARY);
                            if(Salary_map != null){
                                float amount = Float.parseFloat(Salary_map.get(Constants.KEY_SALARY_AMOUNT).toString());
                                //System.out.println(Address_map.get(Constants.KEY_COUNTRY).toString());
                                if (Salary_map.get(Constants.KEY_SALARY_TYPE).toString().equals(autoCompleteTextView.getText().toString())
                                        && amount >= min  && amount <= max) {

                                    User user = new User();
                                    user.setUserId(queryDocumentSnapshot.getId());
                                    user.setUsername(queryDocumentSnapshot.getString(Constants.KEY_USERNAME));
                                    user.setFirst_name(queryDocumentSnapshot.getString(Constants.KEY_FIRSTNAME));
                                    user.setLast_name(queryDocumentSnapshot.getString(Constants.KEY_LASTNAME));
                                    user.setMobile_Number(queryDocumentSnapshot.getString(Constants.KEY_MOBILE_NUMBER));
                                    user.setEmail_Address(queryDocumentSnapshot.getString(Constants.KEY_EMAILADDRESS));
                                    user.setAddress((Map<String, String>) queryDocumentSnapshot.get(Constants.KEY_ADDRESS));
                                    user.setSalary((Map<String, Object>) queryDocumentSnapshot.get(Constants.KEY_SALARY));
                                    user.setProfessional_Headline(queryDocumentSnapshot.getString(Constants.KEY_PROFESSIONAL_HEADLINE));
                                    user.setUser_Summary(queryDocumentSnapshot.getString(Constants.KEY_USER_SUMMARY));
                                    user.setKey(queryDocumentSnapshot.getString("Key"));
                                    user.setTop_Skills((ArrayList<String>) queryDocumentSnapshot.get(Constants.KEY_SKILLS));
                                    user.setExperience((ArrayList<HashMap>) queryDocumentSnapshot.get(Constants.KEY_EXPERIENCES));
                                    user.setEducation((ArrayList<HashMap>) queryDocumentSnapshot.get(Constants.KEY_EDUCATION));
                                    user.setLanguages((ArrayList<String>) queryDocumentSnapshot.get(Constants.KEY_LANGUAGES));
                                    user.setReviews((ArrayList<HashMap>) queryDocumentSnapshot.get(Constants.KEY_REVIEW));


                                    if (queryDocumentSnapshot.getString(Constants.KEY_PROFILE_IMAGE) == null) {
                                        Bitmap myLogo = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.avatar_man);
                                        String encodedimg = encodeImage(myLogo);
                                        user.setProfile_Image(encodedimg);
                                    } else {
                                        user.setProfile_Image(queryDocumentSnapshot.getString(Constants.KEY_PROFILE_IMAGE));
                                    }

                                    user.setToken(queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN));
                                    if(queryDocumentSnapshot.getString(Constants.KEY_USERNAME) != "" && queryDocumentSnapshot.getString(Constants.KEY_FIRSTNAME) != "" && queryDocumentSnapshot.getString(Constants.KEY_LASTNAME) != ""
                                            && queryDocumentSnapshot.getString(Constants.KEY_EMAILADDRESS) != "" &&
                                            queryDocumentSnapshot.getString(Constants.KEY_MOBILE_NUMBER)!= null && queryDocumentSnapshot.getString(Constants.KEY_MOBILE_NUMBER) != "" && (Map<String, String>) queryDocumentSnapshot.get(Constants.KEY_ADDRESS) != null && user.getAddress().get(Constants.KEY_CITY).toString() != "" && user.getAddress().get(Constants.KEY_COUNTRY).toString() != ""
                                            && (Map<String, Object>) queryDocumentSnapshot.get(Constants.KEY_SALARY) != null && user.getSalary().get(Constants.KEY_SALARY_AMOUNT).toString() != "" && user.getSalary().get(Constants.KEY_SALARY_TYPE).toString() != ""
                                            && queryDocumentSnapshot.getString(Constants.KEY_PROFESSIONAL_HEADLINE) != null && queryDocumentSnapshot.getString(Constants.KEY_PROFESSIONAL_HEADLINE) != ""
                                            &&  queryDocumentSnapshot.getString(Constants.KEY_USER_SUMMARY) != null && queryDocumentSnapshot.getString(Constants.KEY_USER_SUMMARY) != "" &&
                                            (ArrayList<String>) queryDocumentSnapshot.get(Constants.KEY_SKILLS) != null &&   !((ArrayList<?>) queryDocumentSnapshot.get(Constants.KEY_SKILLS)).isEmpty() && queryDocumentSnapshot.getString(Constants.KEY_PROFILE_IMAGE) != null){

                                        freelancersusersList.add(user);
                                    }
                            }

                            }
                        }
                    }

                    if (freelancersusersList.size() > 0) {

                        userAdapter = new FreelancerUsersAdapter(freelancersusersList);
                        //userAdapter.filterList(freelancersusersList);
                        //DividerItemDecoration itemDecor = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
                        // binding.FreelancersUsersRCV.addItemDecoration(itemDecor);
                        // binding.FreelancersUsersRCV.setLayoutManager(new LinearLayoutManager(this.getContext()));
                        binding.FreelancersUsersRCV.setAdapter(userAdapter);
                        Users_loading(false);
                        binding.NodatafreelancerImage.setVisibility(View.GONE);
                        //binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(freelancersusersList.size()).append(" results").toString());
                        if(freelancersusersList.size() == 1){
                            binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(freelancersusersList.size()).append(" result").toString());
                        }else {
                            binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(freelancersusersList.size()).append(" results").toString());
                        }
                    } else {
                        Users_loading(false);
                        userAdapter = new FreelancerUsersAdapter(freelancersusersList);
                        //userAdapter.filterList(freelancersusersList);
                        binding.FreelancersUsersRCV.setAdapter(userAdapter);
                        binding.NodatafreelancerImage.setVisibility(View.VISIBLE);
                        binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(freelancersusersList.size()).append(" results").toString());
                    }

                });

        return  freelancersusersList;
    }

    public void FilterbyLocation(View view){
        freelancersusersList.clear();
        database = FirebaseFirestore.getInstance();


    // Find Users within 50km of the user signed in
        final GeoLocation center = new GeoLocation(preferenceManager.getDouble("Latitude"), preferenceManager.getDouble("Longtitude"));
        final double radiusInM = radius_value * 1000;

        System.out.println(preferenceManager.getDouble("Latitude") + " "+ preferenceManager.getDouble("Longtitude"));

    // Each item in 'bounds' represents a startAt/endAt pair. We have to issue
    // a separate query for each pair. There can be up to 9 pairs of bounds
    // depending on overlap, but in most cases there are 4.
        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM);
        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (GeoQueryBounds b : bounds) {

            Query q = database.collection(Constants.KEY_COLLECTION_USERS)
                    //.whereEqualTo(Constants.KEY_VISIBLE_ASFREELANCER,true)
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
                        System.out.println(tasks.get(0).getResult().getDocuments().toString());
                        for (Task<QuerySnapshot> task : tasks) {
                            QuerySnapshot snap = task.getResult();
                            for (DocumentSnapshot doc : snap.getDocuments()) {
                                double lat = doc.getDouble("Latitude");
                                double lng = doc.getDouble("Longtitude");

                                System.out.println(lat + "  "+ lng);
                                // We have to filter out a few false positives due to GeoHash
                                // accuracy, but most will match
                                GeoLocation docLocation = new GeoLocation(lat, lng);
                                double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);

                                System.out.println("Dist= " + distanceInM);
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
                            user.setVisible_as_freelancer(matchingDocs.get(i).getBoolean(Constants.KEY_VISIBLE_ASFREELANCER));
                            user.setUsername(matchingDocs.get(i).getString(Constants.KEY_USERNAME));
                            user.setFirst_name(matchingDocs.get(i).getString(Constants.KEY_FIRSTNAME));
                            user.setLast_name(matchingDocs.get(i).getString(Constants.KEY_LASTNAME));
                            user.setMobile_Number(matchingDocs.get(i).getString(Constants.KEY_MOBILE_NUMBER));
                            user.setEmail_Address(matchingDocs.get(i).getString(Constants.KEY_EMAILADDRESS));
                            user.setAddress((Map<String, String>) matchingDocs.get(i).get(Constants.KEY_ADDRESS));
                            user.setSalary((Map<String, Object>) matchingDocs.get(i).get(Constants.KEY_SALARY));
                            user.setProfessional_Headline(matchingDocs.get(i).getString(Constants.KEY_PROFESSIONAL_HEADLINE));
                            user.setUser_Summary(matchingDocs.get(i).getString(Constants.KEY_USER_SUMMARY));
                            user.setKey(matchingDocs.get(i).getString("Key"));
                            user.setTop_Skills((ArrayList<String>) matchingDocs.get(i).get(Constants.KEY_SKILLS));
                            user.setExperience((ArrayList<HashMap>) matchingDocs.get(i).get(Constants.KEY_EXPERIENCES));
                            user.setEducation((ArrayList<HashMap>) matchingDocs.get(i).get(Constants.KEY_EDUCATION));
                            user.setLanguages((ArrayList<String>) matchingDocs.get(i).get(Constants.KEY_LANGUAGES));
                            user.setReviews((ArrayList<HashMap>) matchingDocs.get(i).get(Constants.KEY_REVIEW));
                            // user.setApplied_Jobs(queryDocumentSnapshot.getString(Constants.KEY_APPLIED_JOBS));
                            // user.setCompleted_Jobs(queryDocumentSnapshot.getString(Constants.KEY_COMPLETED_JOBS));

                            if (matchingDocs.get(i).getString(Constants.KEY_PROFILE_IMAGE) == null) {
                                Bitmap myLogo = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.avatar_man);
                                String encodedimg = encodeImage(myLogo);
                                user.setProfile_Image(encodedimg);
                            } else {
                                user.setProfile_Image(matchingDocs.get(i).getString(Constants.KEY_PROFILE_IMAGE));
                            }

                            user.setToken(matchingDocs.get(i).getString(Constants.KEY_FCM_TOKEN));

                            if(user.getVisible_as_freelancer()){
                                freelancersusersList.add(user);
                            }

                        }


                        if (freelancersusersList.size() > 0) {

                            userAdapter = new FreelancerUsersAdapter(freelancersusersList);

                            binding.FreelancersUsersRCV.setAdapter(userAdapter);
                            Users_loading(false);
                            binding.NodatafreelancerImage.setVisibility(View.GONE);
                            //binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(freelancersusersList.size()).append(" results").toString());
                            if(freelancersusersList.size() == 1){
                                binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(freelancersusersList.size()).append(" result").toString());
                            }else {
                                binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(freelancersusersList.size()).append(" results").toString());
                            }
                        } else {
                            Users_loading(false);
                            userAdapter = new FreelancerUsersAdapter(freelancersusersList);
                            binding.FreelancersUsersRCV.setAdapter(userAdapter);
                            binding.NodatafreelancerImage.setVisibility(View.VISIBLE);
                            binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(freelancersusersList.size()).append(" results").toString());
                        }
                    }
                });

    }

    public ArrayList<User> Filterbycountry(View view,CountryCodePicker countryCodePicker){
        freelancersusersList.clear();
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity());
        String currentUserID = preferenceManager.getString(Constants.KEY_USERID);
        System.out.println(countryCodePicker.getSelectedCountryName().toString());
        Users_loading(true);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereNotEqualTo(FieldPath.documentId(),currentUserID)
                .whereEqualTo(Constants.KEY_VISIBLE_ASFREELANCER,true)
                .get()
                .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null) {
                                freelancersusersList = new ArrayList<>();
                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {


                                    if(queryDocumentSnapshot.get(Constants.KEY_ADDRESS) != null){
                                        Map<String, Object> Address_map = (Map<String, Object>) queryDocumentSnapshot.get(Constants.KEY_ADDRESS);

                                        if (Address_map.get(Constants.KEY_COUNTRY).toString().equals(countryCodePicker.getSelectedCountryName())) {

                                            User user = new User();
                                            user.setUserId(queryDocumentSnapshot.getId());
                                            user.setVisible_as_freelancer(queryDocumentSnapshot.getBoolean(Constants.KEY_VISIBLE_ASFREELANCER));
                                            user.setUsername(queryDocumentSnapshot.getString(Constants.KEY_USERNAME));
                                            user.setFirst_name(queryDocumentSnapshot.getString(Constants.KEY_FIRSTNAME));
                                            user.setLast_name(queryDocumentSnapshot.getString(Constants.KEY_LASTNAME));
                                            user.setMobile_Number(queryDocumentSnapshot.getString(Constants.KEY_MOBILE_NUMBER));
                                            user.setEmail_Address(queryDocumentSnapshot.getString(Constants.KEY_EMAILADDRESS));
                                            user.setAddress((Map<String, String>) queryDocumentSnapshot.get(Constants.KEY_ADDRESS));
                                            user.setSalary((Map<String, Object>) queryDocumentSnapshot.get(Constants.KEY_SALARY));
                                            user.setProfessional_Headline(queryDocumentSnapshot.getString(Constants.KEY_PROFESSIONAL_HEADLINE));
                                            user.setUser_Summary(queryDocumentSnapshot.getString(Constants.KEY_USER_SUMMARY));
                                            user.setKey(queryDocumentSnapshot.getString("Key"));
                                            user.setTop_Skills((ArrayList<String>) queryDocumentSnapshot.get(Constants.KEY_SKILLS));
                                            user.setExperience((ArrayList<HashMap>) queryDocumentSnapshot.get(Constants.KEY_EXPERIENCES));
                                            user.setEducation((ArrayList<HashMap>) queryDocumentSnapshot.get(Constants.KEY_EDUCATION));
                                            user.setLanguages((ArrayList<String>) queryDocumentSnapshot.get(Constants.KEY_LANGUAGES));
                                            user.setReviews((ArrayList<HashMap>) queryDocumentSnapshot.get(Constants.KEY_REVIEW));


                                            if (queryDocumentSnapshot.getString(Constants.KEY_PROFILE_IMAGE) == null) {
                                                Bitmap myLogo = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.avatar_man);
                                                String encodedimg = encodeImage(myLogo);
                                                user.setProfile_Image(encodedimg);
                                            } else {
                                                user.setProfile_Image(queryDocumentSnapshot.getString(Constants.KEY_PROFILE_IMAGE));
                                            }

                                            user.setToken(queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN));

                                            if(queryDocumentSnapshot.getString(Constants.KEY_USERNAME) != "" && queryDocumentSnapshot.getString(Constants.KEY_FIRSTNAME) != "" && queryDocumentSnapshot.getString(Constants.KEY_LASTNAME) != ""
                                                    && queryDocumentSnapshot.getString(Constants.KEY_EMAILADDRESS) != "" &&
                                                    queryDocumentSnapshot.getString(Constants.KEY_MOBILE_NUMBER)!= null && queryDocumentSnapshot.getString(Constants.KEY_MOBILE_NUMBER) != "" && (Map<String, String>) queryDocumentSnapshot.get(Constants.KEY_ADDRESS) != null && user.getAddress().get(Constants.KEY_CITY).toString() != "" && user.getAddress().get(Constants.KEY_COUNTRY).toString() != ""
                                                    && (Map<String, Object>) queryDocumentSnapshot.get(Constants.KEY_SALARY) != null && user.getSalary().get(Constants.KEY_SALARY_AMOUNT).toString() != "" && user.getSalary().get(Constants.KEY_SALARY_TYPE).toString() != ""
                                                    && queryDocumentSnapshot.getString(Constants.KEY_PROFESSIONAL_HEADLINE) != null && queryDocumentSnapshot.getString(Constants.KEY_PROFESSIONAL_HEADLINE) != ""
                                                    &&  queryDocumentSnapshot.getString(Constants.KEY_USER_SUMMARY) != null && queryDocumentSnapshot.getString(Constants.KEY_USER_SUMMARY) != "" &&
                                                    (ArrayList<String>) queryDocumentSnapshot.get(Constants.KEY_SKILLS) != null &&   !((ArrayList<?>) queryDocumentSnapshot.get(Constants.KEY_SKILLS)).isEmpty() && queryDocumentSnapshot.getString(Constants.KEY_PROFILE_IMAGE) != null){

                                                freelancersusersList.add(user);
                                            }
                                        }
                                    }

                                        }
                                 }

                    if (freelancersusersList.size() > 0) {

                        userAdapter = new FreelancerUsersAdapter(freelancersusersList);
                        //userAdapter.filterList(freelancersusersList);
                        //DividerItemDecoration itemDecor = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
                       // binding.FreelancersUsersRCV.addItemDecoration(itemDecor);
                       // binding.FreelancersUsersRCV.setLayoutManager(new LinearLayoutManager(this.getContext()));
                        binding.FreelancersUsersRCV.setAdapter(userAdapter);
                        Users_loading(false);
                        binding.NodatafreelancerImage.setVisibility(View.GONE);
                       // binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(freelancersusersList.size()).append(" results").toString());
                        if(freelancersusersList.size() == 1){
                            binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(freelancersusersList.size()).append(" result").toString());
                        }else {
                            binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(freelancersusersList.size()).append(" results").toString());
                        }
                    } else {
                        Users_loading(false);
                        userAdapter = new FreelancerUsersAdapter(freelancersusersList);
                        //userAdapter.filterList(freelancersusersList);
                        binding.FreelancersUsersRCV.setAdapter(userAdapter);
                        binding.NodatafreelancerImage.setVisibility(View.VISIBLE);
                        binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(freelancersusersList.size()).append(" results").toString());
                    }

                     });

                         return  freelancersusersList;
    }

    private void MyUserslistener (View view){

        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity());
        String currentUserID = preferenceManager.getString(Constants.KEY_USERID);

        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereNotEqualTo(FieldPath.documentId(),currentUserID)
                .whereEqualTo(Constants.KEY_VISIBLE_ASFREELANCER,true)
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
                                userslist.add(doc.getId());
                            }
                        }
                        Log.d(TAG, "Current users " + userslist.size());
//                        Log.d(TAG, "Current jobs " + JobPostsList.size());
                        if(userslist.size() > 0 ){
                            try {
                                getUsers(view);
                            } catch (ParseException parseException) {
                                parseException.printStackTrace();
                            }
                        }else{
                            Users_loading(false);
                            binding.FilterFreelancerBtn.setVisibility(View.INVISIBLE);
                            binding.FilterLabel.setVisibility(View.INVISIBLE);
                            binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(userslist.size()).append(" results").toString());
                        }
                    }
                });
    }

    // DISPLAY ALL USERS IN THE DATABASE
    public void getUsers(View view) throws ParseException {
        Users_loading(true);
        database = FirebaseFirestore.getInstance();
                                preferenceManager = new PreferenceManager(getActivity());
                                String currentUserID = preferenceManager.getString(Constants.KEY_USERID);

                                database.collection(Constants.KEY_COLLECTION_USERS)
                                        .whereNotEqualTo(FieldPath.documentId(),currentUserID)
                                        .whereEqualTo(Constants.KEY_VISIBLE_ASFREELANCER,true)
                                        .get()
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful() && task.getResult() != null) {
                                                freelancersusersList = new ArrayList<>();
                                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                    if (currentUserID.equals(queryDocumentSnapshot.getId())) {
                                                        continue;
                                                    }
                                                    User user = new User();
                                                    user.setUserId(queryDocumentSnapshot.getId());
                                                    user.setVisible_as_freelancer(queryDocumentSnapshot.getBoolean(Constants.KEY_VISIBLE_ASFREELANCER));
                                                    user.setUsername(queryDocumentSnapshot.getString(Constants.KEY_USERNAME));
                                                    user.setFirst_name(queryDocumentSnapshot.getString(Constants.KEY_FIRSTNAME));
                                                    user.setLast_name(queryDocumentSnapshot.getString(Constants.KEY_LASTNAME));
                                                    user.setMobile_Number(queryDocumentSnapshot.getString(Constants.KEY_MOBILE_NUMBER));
                                                    user.setEmail_Address(queryDocumentSnapshot.getString(Constants.KEY_EMAILADDRESS));
                                                    user.setAddress((Map<String, String>) queryDocumentSnapshot.get(Constants.KEY_ADDRESS));
                                                    user.setSalary((Map<String, Object>) queryDocumentSnapshot.get(Constants.KEY_SALARY));
                                                    user.setProfessional_Headline(queryDocumentSnapshot.getString(Constants.KEY_PROFESSIONAL_HEADLINE));
                                                    user.setUser_Summary(queryDocumentSnapshot.getString(Constants.KEY_USER_SUMMARY));
                                                    user.setKey(queryDocumentSnapshot.getString("Key"));
                                                    user.setTop_Skills((ArrayList<String>) queryDocumentSnapshot.get(Constants.KEY_SKILLS));
                                                    user.setExperience((ArrayList<HashMap>) queryDocumentSnapshot.get(Constants.KEY_EXPERIENCES));
                                                    user.setEducation((ArrayList<HashMap>) queryDocumentSnapshot.get(Constants.KEY_EDUCATION));
                                                    user.setLanguages((ArrayList<String>) queryDocumentSnapshot.get(Constants.KEY_LANGUAGES));
                                                    user.setReviews((ArrayList<HashMap>) queryDocumentSnapshot.get(Constants.KEY_REVIEW));
                                                    // user.setApplied_Jobs(queryDocumentSnapshot.getString(Constants.KEY_APPLIED_JOBS));
                                                    // user.setCompleted_Jobs(queryDocumentSnapshot.getString(Constants.KEY_COMPLETED_JOBS));
                                                    //user.setPosted_Jobs(queryDocumentSnapshot.getString(Constants.KEY_POSTED_JOBS));
                                                    // user.setHired_Jobs(queryDocumentSnapshot.getString(Constants.KEY_HIRED_JOBS));


                                                    if (queryDocumentSnapshot.getString(Constants.KEY_PROFILE_IMAGE) == null) {
                                                        Bitmap myLogo = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.avatar_man);
                                                        String encodedimg = encodeImage(myLogo);
                                                        user.setProfile_Image(encodedimg);
                                                    } else {
                                                        user.setProfile_Image(queryDocumentSnapshot.getString(Constants.KEY_PROFILE_IMAGE));
                                                    }

                                                    user.setToken(queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN));


                                                    if(queryDocumentSnapshot.getString(Constants.KEY_USERNAME) != "" && queryDocumentSnapshot.getString(Constants.KEY_FIRSTNAME) != "" && queryDocumentSnapshot.getString(Constants.KEY_LASTNAME) != ""
                                                            && queryDocumentSnapshot.getString(Constants.KEY_EMAILADDRESS) != "" &&
                                                            queryDocumentSnapshot.getString(Constants.KEY_MOBILE_NUMBER)!= null && queryDocumentSnapshot.getString(Constants.KEY_MOBILE_NUMBER) != "" && (Map<String, String>) queryDocumentSnapshot.get(Constants.KEY_ADDRESS) != null && user.getAddress().get(Constants.KEY_CITY).toString() != "" && user.getAddress().get(Constants.KEY_COUNTRY).toString() != ""
                                                            && (Map<String, Object>) queryDocumentSnapshot.get(Constants.KEY_SALARY) != null && user.getSalary().get(Constants.KEY_SALARY_AMOUNT).toString() != "" && user.getSalary().get(Constants.KEY_SALARY_TYPE).toString() != ""
                                                            && queryDocumentSnapshot.getString(Constants.KEY_PROFESSIONAL_HEADLINE) != null && queryDocumentSnapshot.getString(Constants.KEY_PROFESSIONAL_HEADLINE) != ""
                                                            &&  queryDocumentSnapshot.getString(Constants.KEY_USER_SUMMARY) != null && queryDocumentSnapshot.getString(Constants.KEY_USER_SUMMARY) != "" &&
                                                            (ArrayList<String>) queryDocumentSnapshot.get(Constants.KEY_SKILLS) != null &&   !((ArrayList<?>) queryDocumentSnapshot.get(Constants.KEY_SKILLS)).isEmpty() && queryDocumentSnapshot.getString(Constants.KEY_PROFILE_IMAGE) != null){

                                                        freelancersusersList.add(user);
                                                    }

                                                }

                                            }
                    if (freelancersusersList.size() > 0) {
                        binding.NodatafreelancerImage.setVisibility(View.GONE);
                        userAdapter = new FreelancerUsersAdapter(freelancersusersList);
                        DividerItemDecoration itemDecor = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
                        binding.FreelancersUsersRCV.addItemDecoration(itemDecor);
                        binding.FreelancersUsersRCV.setLayoutManager(new LinearLayoutManager(this.getContext()));
                        binding.FreelancersUsersRCV.setAdapter(userAdapter);
                        Users_loading(false);
                        //binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(freelancersusersList.size()).append(" results").toString());
                        if(freelancersusersList.size() == 1){
                            binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(freelancersusersList.size()).append(" result").toString());
                        }else {
                            binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(freelancersusersList.size()).append(" results").toString());
                        }
                    } else {
                        Users_loading(false);
                        binding.NodatafreelancerImage.setVisibility(View.VISIBLE);
                        userAdapter = new FreelancerUsersAdapter(freelancersusersList);
                        binding.FreelancersUsersRCV.setAdapter(userAdapter);
                        binding.FilterFreelancerBtn.setVisibility(View.INVISIBLE);
                        binding.FilterLabel.setVisibility(View.INVISIBLE);
                        binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(freelancersusersList.size()).append(" results").toString());

                    }
                });
    }


    private void search_freelancer(View view) {

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
                userAdapter = new FreelancerUsersAdapter(freelancersusersList);
                binding.FreelancersUsersRCV.setAdapter(userAdapter);
                if( binding.FreelancersUsersRCV.getAdapter().getItemCount() == 0){
                    //do nothing
                }else {
                    filter(editable.toString());
                    binding.FreelancersUsersRCV.setAdapter(userAdapter);

                    String searchinput = binding.searchboxFreelancerstxt.getText().toString().trim();


                    if (searchinput.isEmpty()) {
                        userAdapter = new FreelancerUsersAdapter(freelancersusersList);
                        binding.FreelancersUsersRCV.setAdapter(userAdapter);
                        binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(freelancersusersList.size()).append(" results").toString());

                    }
                }
            }
        });
    }

    private void filter(String text) {
        ArrayList<User> filteredFreelancers = new ArrayList<User>();

        for (int i = 0; i < freelancersusersList.size(); i++) {

            if (freelancersusersList.get(i).getUsername().contains(text.toLowerCase())) {
                filteredFreelancers.add(freelancersusersList.get(i));
            }
        }
        userAdapter.filterList(filteredFreelancers);
        binding.resultsRangeLabel.setText(new StringBuilder().append("Showing ").append(filteredFreelancers.size()).append(" results").toString());

    }


    private void  Users_loading (Boolean isloading){
        if(isloading){
            binding.NodatafreelancerImage.setVisibility(View.GONE);
            binding.FreelancersUsersRCV.setVisibility(View.INVISIBLE);
            binding.searchboxFreelancers.setVisibility(View.INVISIBLE);
            binding.resultsRangeLabel.setVisibility(View.INVISIBLE);
            binding.FilterFreelancerBtn.setVisibility(View.INVISIBLE);
            binding.TopresultsLabel.setVisibility(View.INVISIBLE);
            binding.FilterLabel.setVisibility(View.INVISIBLE);

            binding.UsersProgressbar.setVisibility(View.VISIBLE);

        }else{
           // binding.NodatafreelancerImage.setVisibility(View.GONE);
            binding.FreelancersUsersRCV.setVisibility(View.VISIBLE);
            binding.searchboxFreelancers.setVisibility(View.VISIBLE);
            binding.resultsRangeLabel.setVisibility(View.VISIBLE);
            binding.FilterFreelancerBtn.setVisibility(View.VISIBLE);
            binding.TopresultsLabel.setVisibility(View.VISIBLE);
            binding.FilterLabel.setVisibility(View.VISIBLE);

            binding.UsersProgressbar.setVisibility(View.INVISIBLE);

        }
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

    @Override
    public void onResume() {
        super.onResume();
        View view = binding.getRoot().getRootView();
     /*   try {
            getUsers(view);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }


}