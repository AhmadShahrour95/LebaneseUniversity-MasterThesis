package com.appstechio.workyzo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.appstechio.workyzo.activities.Postjob_Activity;
import com.appstechio.workyzo.databinding.FragmentPostjobSecondstepBinding;

import com.appstechio.workyzo.R;
import com.appstechio.workyzo.interfaces.Display_Toasts;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import kr.co.prnd.StepProgressBar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Postjob_secondstep#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Postjob_secondstep extends Fragment implements Display_Toasts {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Postjob_secondstep() {
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
    public static Postjob_secondstep newInstance(String param1, String param2) {
        Postjob_secondstep fragment = new Postjob_secondstep();
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

    ArrayAdapter<String> adapteritems;
    private FragmentPostjobSecondstepBinding binding;
    private PreferenceManager preferenceManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      binding =FragmentPostjobSecondstepBinding.inflate(inflater, container, false);
      View view = binding.getRoot();
        populateskills_required(view);
        Click_NextBtn(view);
        ClickBack_Btn(view);
        preferenceManager = new PreferenceManager(getActivity());

        if(Constants.EDITPOST_FLAG){
            for(int i=0;i<preferenceManager.getStringArray(Constants.KEY_JOB_SKILLS_REQUIRED_UPDATE).size();i++){

                Chip chip = new Chip(getContext());
                chip.setText(preferenceManager.getStringArray(Constants.KEY_JOB_SKILLS_REQUIRED_UPDATE).get(i));
                chip.setSingleLine(true);
                chip.setTextSize(16);
                chip.setCloseIconVisible(true);
                chip.setClickable(true);

               binding.SkillrequiredChipgroup.addView(chip);
               binding.SkillrequiredChipgroup.setVisibility(View.VISIBLE);
               Constants.JobRequiredSkills_Array.add(chip.getText().toString());

                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Constants.JobRequiredSkills_Array.remove(chip.getText().toString());
                        binding.SkillrequiredChipgroup.removeView(chip);
                    }
                });

            }


        }
      return view;
    }

    private  void populateskills_required(View view){

        List<String> list = new ArrayList<>(Arrays.asList(Constants.computerskillslabels)); //returns a list view of an array

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


        adapteritems = new ArrayAdapter(binding.getRoot().getContext(), R.layout.list_item, Skills_list);
        binding.autoCompleteJobSkillsRequired.setAdapter(adapteritems);

        binding.autoCompleteJobSkillsRequired.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();

                String userinput = binding.autoCompleteJobSkillsRequired.getText().toString();
                Chip chip = new Chip(getContext());
                chip.setText(userinput);
                chip.setSingleLine(true);
                chip.setTextSize(16);
                chip.setCloseIconVisible(true);
                chip.setClickable(true);
                if(Constants.JobRequiredSkills_Array.size() < 5){
                    if (Constants.JobRequiredSkills_Array.contains(userinput)){
                        Toast.makeText(getActivity(),"Skill already selected",Toast.LENGTH_LONG).show();
                    }else{
                        binding.SkillrequiredChipgroup.addView(chip);
                        binding.SkillrequiredChipgroup.setVisibility(View.VISIBLE);
                        Constants.JobRequiredSkills_Array.add(chip.getText().toString());

                    }
                }

                binding.autoCompleteJobSkillsRequired.getText().clear();
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Constants.JobRequiredSkills_Array.remove(chip.getText().toString());
                        binding.SkillrequiredChipgroup.removeView(chip);
                    }
                });
            }
        });
    }

    private void Click_NextBtn(View view){

        ViewPager2 viewPager2 = getActivity().findViewById(R.id.PostjobViewPager);
        StepProgressBar stepProgressBar = getActivity().findViewById(R.id.StepProgressBar);


        binding.NextStep1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Constants.JobRequiredSkills_Array.size() == 5){
                    preferenceManager = new PreferenceManager(getActivity());

                    preferenceManager.putStringArray(Constants.KEY_JOB_SKILLS_REQUIRED,  Constants.JobRequiredSkills_Array);
                    //Constants.JobRequiredSkills_Array.clear();
                    Constants.step = Constants.step + 1;
                    if (Constants.step == 0) {
                        viewPager2.setCurrentItem(0);
                    } else if (Constants.step == 1) {
                        viewPager2.setCurrentItem(1);
                    } else if (Constants.step == 2) {
                        viewPager2.setCurrentItem(2);
                    } else if (Constants.step == 3) {
                        viewPager2.setCurrentItem(3);
                    } else {
                        viewPager2.setCurrentItem(4);
                    }

                    stepProgressBar.setStep(Constants.step);
                } else {
                    Toast.makeText(getActivity(), "You should select up to 5 skills to proceed", Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    private  void ClickBack_Btn(View view){

        ViewPager2 viewPager2 = getActivity().findViewById(R.id.PostjobViewPager);
        StepProgressBar stepProgressBar = getActivity().findViewById(R.id.StepProgressBar);
        binding.BacktopreviousStepBtn.setOnClickListener(new View.OnClickListener() {
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
}