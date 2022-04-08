package com.appstechio.workyzo.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.CompoundButton;
import android.widget.TextView;

import com.appstechio.workyzo.R;
import com.appstechio.workyzo.adapters.ExperienceAdapter;

import com.appstechio.workyzo.databinding.FragmentAddexperienceBinding;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addexperience#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addexperience extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public addexperience() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addexperience.
     */
    // TODO: Rename and change types and number of parameters
    public static addexperience newInstance(String param1, String param2) {
        addexperience fragment = new addexperience();
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

    String[] Months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    ArrayAdapter<String> adapteritems;
    private FragmentAddexperienceBinding binding;
    private PreferenceManager preferenceManager;
    private String Item_clickedPosition;
    private int start_month = 0;
    private int end_month = 0;
    private int start_year = 0;
    private int end_year = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddexperienceBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        saveexperience(view);
        Cancelexperience(view);
        populatemonth(view);
        populateyears(view);
        CheckIfCurrently_working(view);
        GetDataFromBundle(view);
        return view;
    }

    private void GetDataFromBundle (View view){
        if (getArguments() != null) {
            Item_clickedPosition = getArguments().getString("ITEM_CLICKED");
            String Job_Title = getArguments().getString(Constants.KEY_POSITION_TITLE);
            String Job_Company = getArguments().getString(Constants.KEY_COMPANY_NAME);
            String Job_Summary = getArguments().getString(Constants.KEY_WORK_SUMMARY);
            String Job_StartMonth = getArguments().getString("START_MONTH");
            String Job_StartYear = getArguments().getString("START_YEAR");
            String Job_ENDMonth = getArguments().getString("END_MONTH");
            String Job_ENDYear = getArguments().getString("END_YEAR");
            String Job_present = getArguments().getString(Constants.KEY_PRESENT_WORK);

            binding.inputpositiontitletxt.setText(Job_Title);
            binding.inputcompanytxt.setText(Job_Company);
            binding.autoCompletestartmonth.setText(Job_StartMonth);
            binding.autoCompletestartyear.setText(Job_StartYear);
            binding.presentjobcheckbox.setChecked(Boolean.parseBoolean(Job_present));
            if(binding.presentjobcheckbox.isChecked()){
                CheckIfCurrently_working(view);
            }
            binding.autoCompleteendmonth.setText(Job_ENDMonth);
            binding.autoCompleteendyear.setText(Job_ENDYear);
            binding.inputexpSummarytxt.setText(Job_Summary);

            populatemonth(view);
            populateyears(view);
        }
    }

    private void populateyears (View view){
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 1950; i--) {
            years.add(Integer.toString(i));
        }
        adapteritems = new ArrayAdapter<>(getActivity(),R.layout.list_item, years);
        binding.autoCompletestartyear.setAdapter(adapteritems);
        binding.autoCompletestartyear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();

            }
        });

        binding.autoCompleteendyear.setAdapter(adapteritems);
        binding.autoCompleteendyear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();

            }
        });
    }

    private void populatemonth(View view){

        adapteritems = new ArrayAdapter<>(getActivity(),R.layout.list_item, Months);
        binding.autoCompletestartmonth.setAdapter(adapteritems);
        binding.autoCompletestartmonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();

            }
        });

        binding.autoCompleteendmonth.setAdapter(adapteritems);
        binding.autoCompleteendmonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();

            }
        });
    }

    //Check if currently working in the same job
    private void CheckIfCurrently_working (View view) {
        binding.presentjobcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    binding.autoCompleteendmonth.getText().clear();
                    binding.autoCompleteendmonth.setEnabled(false);
                    binding.inputendmonth.setEnabled(false);

                    binding.autoCompleteendyear.getText().clear();
                    //binding.inputendyear.getEditText().getText().clear();
                    binding.autoCompleteendyear.setEnabled(false);
                    binding.inputendyear.setEnabled(false);
                }else{
                    binding.autoCompleteendmonth.setEnabled(true);
                    binding.autoCompleteendyear.setEnabled(true);
                    binding.inputendmonth.setEnabled(true);
                    binding.inputendyear.setEnabled(true);
                }
            }
        });
    }

    private void ClearFields(){
        binding.inputpositiontitletxt.getText().clear();
        binding.inputcompanytxt.getText().clear();
        binding.inputstartmonth.getEditText().getText().clear();
        binding.inputstarteduyear.getEditText().getText().clear();
        binding.presentjobcheckbox.setChecked(false);
        binding.inputendmonth.getEditText().getText().clear();
        binding.inputendyear.getEditText().getText().clear();
        binding.inputexpSummarytxt.getText().clear();
    }

    private  void saveexperience(View view){
        TextView Noexp = getActivity().findViewById(R.id.Noexperiencelabel);
        TextView ViewMore_Experience = getActivity().findViewById(R.id.Viewmore_Experiences);
        RecyclerView Experience_RCV = getActivity().findViewById(R.id.Experienceprofile_RCV);

        binding.expsavebtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                if (binding.inputstarteduyear.getEditText().getText().toString() == null || binding.inputstarteduyear.getEditText().getText().toString().isEmpty()){
                    start_year = 0;
                }else if (binding.inputendyear.getEditText().getText().toString() == null || binding.inputendyear.getEditText().getText().toString().isEmpty()){
                    end_year = 0;
                }else{
                    start_year = Integer.parseInt(binding.inputstarteduyear.getEditText().getText().toString());
                    end_year = Integer.parseInt(binding.inputendyear.getEditText().getText().toString());
                }

                if(binding.inputstartmonth.getEditText().getText().toString().equals("January")){
                    start_month =1;
                }else if (binding.inputstartmonth.getEditText().getText().toString().equals("February")){
                    start_month =2;
                } else if (binding.inputstartmonth.getEditText().getText().toString().equals("March")){
                    start_month =3;
                } else if (binding.inputstartmonth.getEditText().getText().toString().equals("April")){
                    start_month =4;
                } else if (binding.inputstartmonth.getEditText().getText().toString().equals("May")){
                    start_month =5;
                } else if (binding.inputstartmonth.getEditText().getText().toString().equals("June")){
                    start_month =6;
                } else if (binding.inputstartmonth.getEditText().getText().toString().equals("July")){
                    start_month =7;
                } else if (binding.inputstartmonth.getEditText().getText().toString().equals("August")){
                    start_month =8;
                } else if (binding.inputstartmonth.getEditText().getText().toString().equals("September")){
                    start_month =9;
                } else if (binding.inputstartmonth.getEditText().getText().toString().equals("October")){
                    start_month =10;
                } else if (binding.inputstartmonth.getEditText().getText().toString().equals("November")){
                    start_month =11;
                } else if (binding.inputstartmonth.getEditText().getText().toString().equals("December")){
                    start_month =12;
                }

                if(binding.presentjobcheckbox.isChecked()){
                    if(binding.inputendmonth.getEditText().getText().toString().equals("January")){
                        end_month =1;
                    }else if (binding.inputendmonth.getEditText().getText().toString().equals("February")){
                        end_month =2;
                    } else if (binding.inputendmonth.getEditText().getText().toString().equals("March")){
                        end_month =3;
                    } else if (binding.inputendmonth.getEditText().getText().toString().equals("April")){
                        end_month =4;
                    } else if (binding.inputendmonth.getEditText().getText().toString().equals("May")){
                        end_month =5;
                    } else if (binding.inputendmonth.getEditText().getText().toString().equals("June")){
                        end_month =6;
                    } else if (binding.inputendmonth.getEditText().getText().toString().equals("July")){
                        end_month =7;
                    } else if (binding.inputendmonth.getEditText().getText().toString().equals("August")){
                        end_month =8;
                    } else if (binding.inputendmonth.getEditText().getText().toString().equals("September")){
                        end_month =9;
                    } else if (binding.inputendmonth.getEditText().getText().toString().equals("October")){
                        end_month =10;
                    } else if (binding.inputendmonth.getEditText().getText().toString().equals("November")){
                        end_month =11;
                    } else if (binding.inputendmonth.getEditText().getText().toString().equals("December")){
                        end_month =12;
                    }
                }



                if( binding.inputpositiontitletxt.getText().toString().isEmpty()){
                    binding.inputpositiontitle.setError(getString(R.string.RequiredField_Error));
                }else if ( binding.inputcompanytxt.getText().toString().isEmpty()){
                    binding.inputpositiontitle.setErrorEnabled(false);
                    binding.inputcompany.setError(getString(R.string.RequiredField_Error));
                }else if ( binding.inputstartmonth.getEditText().getText().toString().isEmpty()){
                    binding.inputcompany.setErrorEnabled(false);
                    binding.inputstartmonth.setError(getString(R.string.RequiredField_Error));
                }else if ( binding.inputstarteduyear.getEditText().getText().toString().isEmpty()){
                    binding.inputstartmonth.setErrorEnabled(false);
                    binding.inputstarteduyear.setError(getString(R.string.RequiredField_Error));
                }else if (!binding.presentjobcheckbox.isChecked() && binding.inputendmonth.getEditText().getText().toString().isEmpty()){
                    System.out.println(binding.presentjobcheckbox.isChecked());
                    binding.inputstarteduyear.setErrorEnabled(false);
                    binding.inputendmonth.setError(getString(R.string.RequiredField_Error));
                }else if (!binding.presentjobcheckbox.isChecked() &&  binding.inputendyear.getEditText().getText().toString().isEmpty()){
                    System.out.println(binding.presentjobcheckbox.isChecked());
                    binding.inputendmonth.setErrorEnabled(false);
                    binding.inputendyear.setError(getString(R.string.RequiredField_Error));
                }else if ( binding.inputexpSummarytxt.getText().toString().isEmpty()){
                    binding.inputendyear.setErrorEnabled(false);
                    binding.inputexpSummary.setError(getString(R.string.RequiredField_Error));
                }else if (!binding.presentjobcheckbox.isChecked() && start_year > end_year){
                    System.out.println(binding.presentjobcheckbox.isChecked());
                    binding.inputexpSummary.setErrorEnabled(false);
                    binding.inputendyear.setError("Invalid date");
                }else if ( !binding.presentjobcheckbox.isChecked() && start_month > end_month){
                    binding.inputendyear.setErrorEnabled(false);
                    binding.inputendmonth.setError("Invalid date");
                }else{
                    binding.inputendmonth.setErrorEnabled(false);

                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    Fragment bottomFragment = manager.findFragmentById(R.id.addexperienceframe);
                    ft.hide(bottomFragment);
                    ft.commit();


                    preferenceManager = new PreferenceManager(getActivity());
                    Constants.Experience_Map = preferenceManager.getMapArray(Constants.KEY_EXPERIENCES);
                    if(Constants.UPDATE_FLAG){
                        int i = Integer.parseInt(Item_clickedPosition);
                        Constants.Experience_Map.get(i).put(Constants.KEY_POSITION_TITLE,binding.inputpositiontitletxt.getText());
                        Constants.Experience_Map.get(i).put(Constants.KEY_POSITION_TITLE, binding.inputpositiontitletxt.getText().toString());
                        Constants.Experience_Map.get(i).put(Constants.KEY_COMPANY_NAME, binding.inputcompanytxt.getText().toString());
                        Constants.Experience_Map.get(i).put(Constants.KEY_START_DATE, binding.autoCompletestartmonth.getText().subSequence(0, 3).toString() + "," + binding.autoCompletestartyear.getText().toString());
                        if (binding.presentjobcheckbox.isChecked()) {
                            Constants.Experience_Map.get(i).put(Constants.KEY_END_DATE,"");
                            Constants.Experience_Map.get(i).put(Constants.KEY_PRESENT_WORK, String.valueOf(true));
                            // Constants.Experience_Map.get(i).put(Constants.KEY_END_DATE, binding.autoCompleteendmonth.getText().subSequence(0, 3).toString() + "," + binding.autoCompleteendyear.getText().toString());

                        } else {

                            Constants.Experience_Map.get(i).put(Constants.KEY_END_DATE, binding.autoCompleteendmonth.getText().subSequence(0, 3).toString() + "," + binding.autoCompleteendyear.getText().toString());
                            Constants.Experience_Map.get(i).put(Constants.KEY_PRESENT_WORK, String.valueOf(false));
                        }
                        Constants.Experience_Map.get(i).put(Constants.KEY_WORK_SUMMARY, binding.inputexpSummarytxt.getText().toString());
                        Log.d("EXPERIENCE", String.valueOf(Constants.Experience_Map));

                        preferenceManager.putMapArray(Constants.KEY_EXPERIENCES, Constants.Experience_Map);
                        ClearFields();
                    }else {
                        //int i = Integer.parseInt(Item_clickedPosition);
                        HashMap<String, String> Experience = new HashMap<>();
                        Experience.put(Constants.KEY_POSITION_TITLE, binding.inputpositiontitletxt.getText().toString());
                        Experience.put(Constants.KEY_COMPANY_NAME, binding.inputcompanytxt.getText().toString());
                        Experience.put(Constants.KEY_START_DATE, binding.autoCompletestartmonth.getText().subSequence(0, 3).toString() + "," + binding.autoCompletestartyear.getText().toString());
                        if (binding.presentjobcheckbox.isChecked()) {
                            //Experience.put(Constants.KEY_START_DATE, binding.autoCompletestartmonth.getText().subSequence(0, 3).toString() + "," + binding.autoCompletestartyear.getText().toString());
                            Experience.put(Constants.KEY_PRESENT_WORK, String.valueOf(true));
                            Experience.put(Constants.KEY_END_DATE, "");

                            //Do Nothing
                        } else {
                            Experience.put(Constants.KEY_END_DATE, binding.autoCompleteendmonth.getText().subSequence(0, 3).toString() + "," + binding.autoCompleteendyear.getText().toString());
                            Experience.put(Constants.KEY_PRESENT_WORK, String.valueOf(false));
                        }
                        Experience.put(Constants.KEY_WORK_SUMMARY, binding.inputexpSummarytxt.getText().toString());

                        Constants.Experience_Map.add(Experience);
                        preferenceManager.putMapArray(Constants.KEY_EXPERIENCES, Constants.Experience_Map);
                        ClearFields();

                    }
                    //Reload The RecyclerView Adapter
                    ExperienceAdapter experienceAdapter;
                    experienceAdapter =  new ExperienceAdapter(preferenceManager.getMapArray(Constants.KEY_EXPERIENCES));
                    Experience_RCV.setAdapter(experienceAdapter);
                    experienceAdapter.activateButtons(true);
                    //Experience_RCV.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
                    Experience_RCV.setVisibility(View.VISIBLE);

                    ArrayList<HashMap> explist = new ArrayList<>();
                    explist = preferenceManager.getMapArray(Constants.KEY_EXPERIENCES);
                    if(explist.size() > 0){
                        Noexp.setVisibility(View.GONE);

                    }else {
                        Noexp.setVisibility(View.VISIBLE);
                    }

                    if(preferenceManager.getMapArray(Constants.KEY_EXPERIENCES).size() >= 6){
                        ViewMore_Experience.setVisibility(View.VISIBLE);
                        ArrayList<HashMap> ExperienceArray_new = new ArrayList<HashMap>(preferenceManager.getMapArray(Constants.KEY_EXPERIENCES).subList(0,5));
                        experienceAdapter = new ExperienceAdapter(ExperienceArray_new);
                        Experience_RCV.setAdapter(experienceAdapter);
                        experienceAdapter.notifyDataSetChanged();

                    }else{
                        ViewMore_Experience.setVisibility(View.GONE);
                    }

                    Constants.UPDATE_FLAG = false;
                }



            }
        });
    }

    private  void Cancelexperience(View view){
        RecyclerView Experience_RCV = getActivity().findViewById(R.id.Experienceprofile_RCV);
        TextView Noexp = getActivity().findViewById(R.id.Noexperiencelabel);
        binding.Expcancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferenceManager = new PreferenceManager(getActivity());
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                Fragment bottomFragment = manager.findFragmentById(R.id.addexperienceframe);
                ft.hide(bottomFragment);
                ft.commit();

                ArrayList<HashMap> Experience_List = new ArrayList<>();
                Experience_List = preferenceManager.getMapArray(Constants.KEY_EXPERIENCES);
                if(Experience_List.size() > 0){
                    Noexp.setVisibility(View.GONE);

                }else {
                    Noexp.setVisibility(View.VISIBLE);
                }

                Experience_RCV.setVisibility(View.VISIBLE);
                ClearFields();
            }


            });

    }
}