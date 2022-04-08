package com.appstechio.workyzo.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.appstechio.workyzo.R;
import com.appstechio.workyzo.databinding.FragmentPostjobThirdstepBinding;
import com.appstechio.workyzo.interfaces.Display_Toasts;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import kr.co.prnd.StepProgressBar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Postjob_thirdstep#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Postjob_thirdstep extends Fragment implements Display_Toasts {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Postjob_thirdstep() {
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
    public static Postjob_thirdstep newInstance(String param1, String param2) {
        Postjob_thirdstep fragment = new Postjob_thirdstep();
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
    private FragmentPostjobThirdstepBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      binding =FragmentPostjobThirdstepBinding.inflate(inflater, container, false);
      View view = binding.getRoot();
        Click_PaymenttypeCard(view);
        Click_NextBtn(view);
        ClickBack_Btn(view);
        preferenceManager = new PreferenceManager(getActivity());

        if(Constants.EDITPOST_FLAG){
           if(preferenceManager.getString(Constants.KEY_JOB_PAYMENT_TYPE_UPDATE).equals("Fixed price")) {
               binding.FixedpriceCard.setChecked(true);
               binding.HourlyRateCard.setChecked(false);
               binding.EstimatedBudgetTitle.setVisibility(View.VISIBLE);
               binding.inputminimumbudgetsalary.setVisibility(View.VISIBLE);
               binding.inputmaximumbudgetsalary.setVisibility(View.VISIBLE);
               binding.inputminimumbudgetsalary.setSuffixText("USD");
               binding.inputmaximumbudgetsalary.setSuffixText("USD");

               binding.inputminimumbudgetsalarytxt.setText(preferenceManager.getString(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET_UPDATE));
               binding.inputmaximumbudgetsalarytxt.setText(preferenceManager.getString(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET_UPDATE));
           }else {
               binding.FixedpriceCard.setChecked(true);
               binding.HourlyRateCard.setChecked(false);
               binding.EstimatedBudgetTitle.setVisibility(View.VISIBLE);
               binding.inputminimumbudgetsalary.setVisibility(View.VISIBLE);
               binding.inputmaximumbudgetsalary.setVisibility(View.VISIBLE);

               binding.inputminimumbudgetsalary.setSuffixText("USD/hour");
               binding.inputmaximumbudgetsalary.setSuffixText("USD/hour");

               binding.inputminimumbudgetsalarytxt.setText(preferenceManager.getString(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET_UPDATE));
               binding.inputmaximumbudgetsalarytxt.setText(preferenceManager.getString(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET_UPDATE));
           }
        }

      return view;
    }


    private void Click_PaymenttypeCard (View view){
        binding.HourlyRateCard.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                binding.HourlyRateCard.setChecked(true);
                binding.FixedpriceCard.setChecked(false);
                binding.HourlyRateCard.setCardBackgroundColor(Color.GRAY);
                binding.FixedpriceCard.setCardBackgroundColor(Color.WHITE);

                binding.EstimatedBudgetTitle.setVisibility(View.VISIBLE);
                binding.inputminimumbudgetsalary.setVisibility(View.VISIBLE);
                binding.inputmaximumbudgetsalary.setVisibility(View.VISIBLE);

                binding.inputminimumbudgetsalary.setSuffixText("USD/hour");
                binding.inputmaximumbudgetsalary.setSuffixText("USD/hour");
            }
        });

        binding.FixedpriceCard.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                binding.FixedpriceCard.setChecked(true);
                binding.HourlyRateCard.setChecked(false);
                binding.FixedpriceCard.setCardBackgroundColor(Color.GRAY);
                binding.HourlyRateCard.setCardBackgroundColor(Color.WHITE);

                binding.EstimatedBudgetTitle.setVisibility(View.VISIBLE);
                binding.inputminimumbudgetsalary.setVisibility(View.VISIBLE);
                binding.inputmaximumbudgetsalary.setVisibility(View.VISIBLE);

                binding.inputminimumbudgetsalary.setSuffixText("USD");
                binding.inputmaximumbudgetsalary.setSuffixText("USD");
            }
        });

    }

    private void Click_NextBtn(View view){

        ViewPager2 viewPager2 = getActivity().findViewById(R.id.PostjobViewPager);
        StepProgressBar stepProgressBar = getActivity().findViewById(R.id.StepProgressBar);


        binding.NextStep2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                preferenceManager = new PreferenceManager(getActivity());
                if(binding.FixedpriceCard.isChecked()){
                    preferenceManager.putString(Constants.KEY_JOB_PAYMENT_TYPE, "Fixed price");
                   if(Check_NoEmptyFields()){
                       String minbudget = binding.inputminimumbudgetsalarytxt.getText().toString();
                       String maxbudget = binding.inputmaximumbudgetsalarytxt.getText().toString();
                       minbudget = minbudget.contains(".") ? minbudget.replaceAll("0*$","").replaceAll("\\.$","") : minbudget;
                       maxbudget = maxbudget.contains(".") ? maxbudget.replaceAll("0*$","").replaceAll("\\.$","") : maxbudget;

                       preferenceManager.putString(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET,minbudget);
                        preferenceManager.putString(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET,maxbudget);

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
                }
            }else {
                    preferenceManager.putString(Constants.KEY_JOB_PAYMENT_TYPE, "Hourly rate");
                    if (Check_NoEmptyFields()) {

                        String minbudget = binding.inputminimumbudgetsalarytxt.getText().toString();
                        String maxbudget = binding.inputmaximumbudgetsalarytxt.getText().toString();
                        minbudget = minbudget.contains(".") ? minbudget.replaceAll("0*$","").replaceAll("\\.$","") : minbudget;
                        maxbudget = maxbudget.contains(".") ? maxbudget.replaceAll("0*$","").replaceAll("\\.$","") : maxbudget;

                        preferenceManager.putString(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET, minbudget);
                        preferenceManager.putString(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET, maxbudget);
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
                    }
                }
                }


        });

     }


    private  void ClickBack_Btn(View view){

        ViewPager2 viewPager2 = getActivity().findViewById(R.id.PostjobViewPager);
        StepProgressBar stepProgressBar = getActivity().findViewById(R.id.StepProgressBar);
        binding.BacktopreviousStep2Btn.setOnClickListener(new View.OnClickListener() {
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

    private  boolean checkif_ValidBudget(){

        if(!binding.inputminimumbudgetsalarytxt.getText().toString().trim().isEmpty() && !binding.inputmaximumbudgetsalarytxt.getText().toString().trim().isEmpty()){
            double minbudget = Double.parseDouble(binding.inputminimumbudgetsalarytxt.getText().toString());
            double maxbudget = Double.parseDouble(binding.inputmaximumbudgetsalarytxt.getText().toString());
            if(minbudget > maxbudget){
                return false;
            }else {
                return true;
            }
        }else {
            return false;
        }
    }

    private boolean Check_NoEmptyFields(){

        if (binding.inputminimumbudgetsalarytxt.getText().toString().trim().isEmpty() && binding.inputmaximumbudgetsalarytxt.getText().toString().trim().isEmpty() ){
            binding.inputminimumbudgetsalary.setError(getString(R.string.RequiredField_Error));
            binding.inputmaximumbudgetsalary.setError(getString(R.string.RequiredField_Error));
            return  false;
        }else if (binding.inputminimumbudgetsalarytxt.getText().toString().trim().isEmpty()) {
            binding.inputminimumbudgetsalary.setError(getString(R.string.RequiredField_Error));
            return  false;
        }else if (binding.inputmaximumbudgetsalarytxt.getText().toString().trim().isEmpty()) {
            binding.inputminimumbudgetsalary.setError(getString(R.string.RequiredField_Error));
            return false;
        }else if(!checkif_ValidBudget()){
            binding.inputminimumbudgetsalary.setError("should be less than the maximum");
            return  false;
        }else {
            return true;
        }

    }

    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void showToast(String message, int duration) {

    }
}