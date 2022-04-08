package com.appstechio.workyzo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.appstechio.workyzo.R;
import com.appstechio.workyzo.adapters.EducationAdapter;
import com.appstechio.workyzo.adapters.ExperienceAdapter;
import com.appstechio.workyzo.databinding.FragmentAddeducationBinding;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addeducation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addeducation extends Fragment {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public addeducation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addeducation.
     */
    // TODO: Rename and change types and number of parameters
    public static addeducation newInstance(String param1, String param2) {
        addeducation fragment = new addeducation();
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

    private String Item_clickedPosition;
    private  FragmentAddeducationBinding binding;
    ArrayAdapter<String> adapteritems;
    private PreferenceManager preferenceManager;
    private int start_year = 0;
    private int end_year = 0;
    String[] Degree_list = {"High School diploma","Associate degree","Bachelor's degree","Master's degree","Doctoral degree"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddeducationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        populateyears(view);
        saveeducation(view);
        Canceleducation(view);
        populateDegree(view);

        binding.autoCompleteeducountries.setTextIsSelectable(true);
        binding.autoCompleteeducountries.setFocusable(false);
        binding.autoCompleteeducountries.setFocusableInTouchMode(false);
        binding.autoCompleteeducountries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ccpEducation.launchCountrySelectionDialog();
                String country_selected = binding.ccpEducation.getSelectedCountryName().toString();
                //showToast(country_selected,1);
                if(country_selected != null){
                    binding.ccpEducation.setVisibility(View.VISIBLE);
                    binding.autoCompleteeducountries.setText("");
                }

            }
        });
       // populateeducountries(view);
        GetDataFromBundle(view);
        return view;
    }

    public String getCountryCode(String countryName) {

        // Get all country codes in a string array.
        String[] isoCountryCodes = Locale.getISOCountries();
        String countryCode = "";
        // Iterate through all country codes:
        for (String code : isoCountryCodes) {
            // Create a locale using each country code
            Locale locale = new Locale("", code);
            // Get country name for each code.
            String name = locale.getDisplayCountry();
            if(name.equals(countryName))
            {
                countryCode = code;
                break;
            }
        }
        return countryCode;
    }

    public static String capitalizeWord(String str){
        String words[]=str.split("\\s");
        String capitalizeWord="";
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord+=first.toUpperCase()+afterfirst+" ";
        }
        return capitalizeWord.trim();
    }
    private void GetDataFromBundle (View view){
        if (getArguments() != null) {
            Item_clickedPosition = getArguments().getString("ITEM_CLICKED");
            String Education_country = getArguments().getString(Constants.KEY_EDUCATION_COUNTRY);
            String University_name = getArguments().getString(Constants.KEY_UNIVERSITY_NAME);
            String Degree = getArguments().getString(Constants.KEY_DEGREE);
            String Major = getArguments().getString(Constants.KEY_MAJOR);
            String StartYear = getArguments().getString(Constants.KEY_START_YEAR);
            String LastYear = getArguments().getString(Constants.KEY_END_YEAR);

            String Countryname = capitalizeWord(Education_country);
            String Country_Code = getCountryCode(Countryname);

            binding.ccpEducation.setCountryForNameCode(Country_Code);
            binding.inputUniversitytxt.setText(University_name);
            binding.autoCompletedegree.setText(Degree);
            binding.inputMajortxt.setText(Major);
            binding.autoCompletestarteduyear.setText(StartYear);
            binding.autoCompleteEndeduyear.setText(LastYear);

            populateDegree(view);
            populateyears(view);

        }
    }



    private void populateDegree(View view){

        adapteritems = new ArrayAdapter<>(getActivity(),R.layout.list_item, Degree_list);
        binding.autoCompletedegree.setAdapter(adapteritems);
        binding.autoCompletedegree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();

            }
        });

    }

    private void populateyears (View view){
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 1950; i--) {
            years.add(Integer.toString(i));
        }
        adapteritems = new ArrayAdapter<>(getActivity(),R.layout.list_item, years);
        binding.autoCompletestarteduyear.setAdapter(adapteritems);
        binding.autoCompletestarteduyear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();

            }
        });

        binding.autoCompleteEndeduyear.setAdapter(adapteritems);
        binding.autoCompleteEndeduyear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();

            }
        });
    }

    private void ClearFields(){
        binding.inputUniversitytxt.getText().clear();
        binding.autoCompletedegree.getText().clear();
        binding.inputMajortxt.getText().clear();
        binding.inputstarteduyear.getEditText().getText().clear();
        binding.inputendeduyear.getEditText().getText().clear();


    }


    private void saveeducation(View view){

        TextView Noedu = getActivity().findViewById(R.id.Noeducationlabel);
        TextView ViewMore_Education = getActivity().findViewById(R.id.Viewmore_Education);
        RecyclerView Education_RCV = getActivity().findViewById(R.id.educationrecyclerView);

        binding.expsavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.inputstarteduyear.getEditText().getText().toString() == null || binding.inputstarteduyear.getEditText().getText().toString().isEmpty()){
                    start_year = 0;
                }else if (binding.inputendeduyear.getEditText().getText().toString() == null || binding.inputendeduyear.getEditText().getText().toString().isEmpty()){
                    end_year = 0;
                }else{
                    start_year = Integer.parseInt(binding.inputstarteduyear.getEditText().getText().toString());
                    end_year = Integer.parseInt(binding.inputendeduyear.getEditText().getText().toString());
                }


                if( binding.inputUniversitytxt.getText().toString().isEmpty()){
                    binding.inputUniversity.setError(getString(R.string.RequiredField_Error));
                }else if ( binding.autoCompletedegree.getText().toString().isEmpty()){
                    binding.inputUniversity.setErrorEnabled(false);
                    binding.inputDegree.setError(getString(R.string.RequiredField_Error));
                }else if ( binding.inputMajortxt.getText().toString().isEmpty()){
                    binding.inputDegree.setErrorEnabled(false);
                    binding.inputMajor.setError(getString(R.string.RequiredField_Error));
                }else if ( binding.inputstarteduyear.getEditText().getText().toString().isEmpty()){
                    binding.inputMajor.setErrorEnabled(false);
                    binding.inputstarteduyear.setError(getString(R.string.RequiredField_Error));
                }else if (binding.inputendeduyear.getEditText().getText().toString().isEmpty()){
                    binding.inputstarteduyear.setErrorEnabled(false);
                    binding.inputendeduyear.setError(getString(R.string.RequiredField_Error));
                }else if ( start_year > end_year){
                    binding.inputendeduyear.setError("Invalid date");
                }else {
                    binding.inputendeduyear.setErrorEnabled(false);

                    preferenceManager = new PreferenceManager(getActivity());
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    Fragment bottomFragment = manager.findFragmentById(R.id.addeducationframe);
                    ft.hide(bottomFragment);
                    ft.commit();

                    preferenceManager = new PreferenceManager(getActivity());
                    Constants.Education_Map = preferenceManager.getMapArray(Constants.KEY_EDUCATION);
                    if(Constants.UPDATE_FLAG) {
                        int i = Integer.parseInt(Item_clickedPosition);
                        Constants.Education_Map.get(i).put(Constants.KEY_EDUCATION_COUNTRY, binding.ccpEducation.getSelectedCountryName().toString());
                        Constants.Education_Map.get(i).put(Constants.KEY_UNIVERSITY_NAME, binding.inputUniversitytxt.getText().toString());
                        Constants.Education_Map.get(i).put(Constants.KEY_DEGREE, binding.autoCompletedegree.getText().toString());
                        Constants.Education_Map.get(i).put(Constants.KEY_MAJOR, binding.inputMajortxt.getText().toString());

                        Constants.Education_Map.get(i).put(Constants.KEY_START_YEAR, binding.autoCompletestarteduyear.getText().toString());
                        Constants.Education_Map.get(i).put(Constants.KEY_END_YEAR, binding.autoCompleteEndeduyear.getText().toString());

                        preferenceManager.putMapArray(Constants.KEY_EDUCATION, Constants.Education_Map);
                        ClearFields();
                    }else{
                        HashMap<String ,String> Education = new HashMap<>();

                        Education.put(Constants.KEY_EDUCATION_COUNTRY,binding.ccpEducation.getSelectedCountryName().toString());
                        Education.put(Constants.KEY_DEGREE,binding.autoCompletedegree.getText().toString());
                        Education.put(Constants.KEY_MAJOR,binding.inputMajortxt.getText().toString());
                        Education.put(Constants.KEY_UNIVERSITY_NAME,binding.inputUniversitytxt.getText().toString());
                        Education.put(Constants.KEY_START_YEAR,binding.autoCompletestarteduyear.getText().toString());
                        Education.put(Constants.KEY_END_YEAR,binding.autoCompleteEndeduyear.getText().toString());


                        Constants.Education_Map.add(Education);

                        preferenceManager.putMapArray(Constants.KEY_EDUCATION,Constants.Education_Map);
                        ClearFields();
                    }

                    //Reload The RecyclerView Adapter
                    EducationAdapter educationAdapter;
                    educationAdapter =  new EducationAdapter(preferenceManager.getMapArray(Constants.KEY_EDUCATION));
                    Education_RCV.setAdapter(educationAdapter);
                    educationAdapter.activateButtons(true);
                    //Experience_RCV.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
                    Education_RCV.setVisibility(View.VISIBLE);

                    ArrayList<HashMap> edulist = new ArrayList<>();
                    edulist = preferenceManager.getMapArray(Constants.KEY_EDUCATION);
                    if(edulist.size() > 0){
                        Noedu.setVisibility(View.GONE);

                    }else {
                        Noedu.setVisibility(View.VISIBLE);
                    }

                    if(preferenceManager.getMapArray(Constants.KEY_EDUCATION).size() >= 6){
                        ViewMore_Education.setVisibility(View.VISIBLE);
                        ArrayList<HashMap> EducationArray_new = new ArrayList<HashMap>(preferenceManager.getMapArray(Constants.KEY_EDUCATION).subList(0,5));
                        educationAdapter = new EducationAdapter(EducationArray_new);
                        Education_RCV.setAdapter(educationAdapter);
                        educationAdapter.notifyDataSetChanged();

                    }else{
                        ViewMore_Education.setVisibility(View.GONE);
                    }

                    Constants.UPDATE_FLAG = false;
                }


            }
        });
    }

    private void  Canceleducation(View view){
        RecyclerView Education_RCV = getActivity().findViewById(R.id.educationrecyclerView);
        TextView Noedu = getActivity().findViewById(R.id.Noeducationlabel);
        binding.Educancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferenceManager = new PreferenceManager(getActivity());
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                Fragment bottomFragment = manager.findFragmentById(R.id.addeducationframe);
                ft.hide(bottomFragment);
                ft.commit();

                Noedu.setVisibility(View.VISIBLE);


            ArrayList<HashMap> Education_List = new ArrayList<>();
            Education_List = preferenceManager.getMapArray(Constants.KEY_EDUCATION);
                if(Education_List.size() > 0){
                    Noedu.setVisibility(View.GONE);

            }else {
                    Noedu.setVisibility(View.VISIBLE);
            }

                Education_RCV.setVisibility(View.VISIBLE);
                ClearFields();
        }
        });
    }
}
