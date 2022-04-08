package com.appstechio.workyzo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.viewpager2.widget.ViewPager2;
import kr.co.prnd.StepProgressBar;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.appstechio.workyzo.databinding.FragmentPostjobFirststepBinding;

import com.appstechio.workyzo.R;

import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Postjob_firststep#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Postjob_firststep extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Postjob_firststep() {
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
    public static Postjob_firststep newInstance(String param1, String param2) {
        Postjob_firststep fragment = new Postjob_firststep();
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

    private PreferenceManager preferenceManager;
    private  FragmentPostjobFirststepBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPostjobFirststepBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Click_NextBtn(view);
        binding.inputJobDescriptionPosttxt.setMovementMethod(new ScrollingMovementMethod());
        charactercount(view);
        preferenceManager = new PreferenceManager(getActivity());

        if(Constants.EDITPOST_FLAG){
            binding.InputJobTitlePosttxt.setText(preferenceManager.getString(Constants.KEY_JOB_TITLE_UPDATE));
            binding.inputJobDescriptionPosttxt.setText(preferenceManager.getString(Constants.KEY_JOB_DESCRIPTION_UPDATE));
        }

        return view;
    }


    private void Click_NextBtn(View view){

        ViewPager2 viewPager2 = getActivity().findViewById(R.id.PostjobViewPager);
        StepProgressBar stepProgressBar = getActivity().findViewById(R.id.StepProgressBar);


        binding.NextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                preferenceManager = new PreferenceManager(getActivity());

                //check empty fields
                if(Check_NoEmptyFields()) {
                    Constants.step = Constants.step + 1;
                    preferenceManager.putString(Constants.KEY_JOB_TITLE, binding.InputJobTitlePosttxt.getText().toString());
                    preferenceManager.putString(Constants.KEY_JOB_DESCRIPTION, binding.inputJobDescriptionPosttxt.getText().toString());

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

                }


            }
        });


    }




    private boolean Check_NoEmptyFields(){
        if (binding.InputJobTitlePosttxt.getText().toString().trim().isEmpty() && binding.inputJobDescriptionPosttxt.getText().toString().trim().isEmpty() ){
            binding.InputJobTitlePosttxt.setError(getString(R.string.RequiredField_Error));
            binding.inputJobDescriptionPosttxt.setError(getString(R.string.RequiredField_Error));
            return  false;
        }else if (binding.InputJobTitlePosttxt.getText().toString().trim().isEmpty()) {
            binding.InputJobTitlePosttxt.setError(getString(R.string.RequiredField_Error));
            return  false;
        }else if (binding.inputJobDescriptionPosttxt.getText().toString().trim().isEmpty()) {
            binding.inputJobDescriptionPosttxt.setError(getString(R.string.RequiredField_Error));
            return  false;
        }else {
            return true;
        }

    }

    private static int  Max = 4000;
    private void charactercount (View view){

        binding.inputJobDescriptionPosttxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Max = 4000;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int count = charSequence.toString().length();

                int result = Max - count;
                binding.JobDescriptionCharcounter.setText(String.valueOf(result));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}