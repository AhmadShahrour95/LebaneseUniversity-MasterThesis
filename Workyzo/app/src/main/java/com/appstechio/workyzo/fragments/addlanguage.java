package com.appstechio.workyzo.fragments;

import android.content.Context;
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
import com.appstechio.workyzo.adapters.Selected_skillsAdapter;
import com.appstechio.workyzo.interfaces.Display_Toasts;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.appstechio.workyzo.databinding.FragmentAddlanguageBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addlanguage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addlanguage extends Fragment implements Display_Toasts {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public addlanguage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addlanguage.
     */
    // TODO: Rename and change types and number of parameters
    public static addlanguage newInstance(String param1, String param2) {
        addlanguage fragment = new addlanguage();
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

    private FragmentAddlanguageBinding binding;
    ArrayAdapter<String> adapteritems;
    private PreferenceManager preferenceManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       binding = FragmentAddlanguageBinding.inflate(inflater, container, false);
       View view = binding.getRoot();

        Cancellanguage(view);
        savelanguage(view);
        populatelanguages(view);
        AddandDelete_LanguagesChips(view);

        return view;

    }


    private void AddandDelete_LanguagesChips (View view){
        binding.inputlanguage.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.autoCompletelanguage.getText().clear();

            }
        });
    }


    private void populatelanguages (View view) {
        // Create a collection of all available countries
        List<String> languageslist = new ArrayList<String>();

        // Map ISO countries to custom country object
        String[] LanguageCodes = Locale.getISOLanguages();
        for (String languagecodes : LanguageCodes){

           Locale locale = new Locale(languagecodes);
            //String iso = locale.getISO3Country();
            //String code = locale.getCountry();
            String name = locale.getDisplayLanguage();
            languageslist.add(name);
        }
        Collections.sort(languageslist);
        adapteritems = new ArrayAdapter<>(getActivity(),R.layout.list_item,languageslist);
        binding.autoCompletelanguage.setAdapter(adapteritems);
        binding.autoCompletelanguage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();

                String userinput = binding.autoCompletelanguage.getText().toString();
                Chip chip = new Chip(getContext());
                chip.setText(userinput);
                chip.setSingleLine(true);
                chip.setTextSize(16);
                chip.setCloseIconVisible(true);
                chip.setClickable(true);
                if (Constants.LanguageChip_list.contains(userinput)){
                    showToast("Language already selected",1);
                }else{
                    binding.languageChipgroup.addView(chip);
                    binding.languageChipgroup.setVisibility(View.VISIBLE);
                    Constants.LanguageChip_list.add(chip.getText().toString());
                    binding.autoCompletelanguage.getText().clear();
                    Log.d("CHIP", String.valueOf(Constants.LanguageChip_list));
                }

                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Constants.LanguageChip_list.remove(chip.getText().toString());
                        binding.languageChipgroup.removeView(chip);
                    }
                });
            }


        });
    }

    private  void savelanguage(View view){
        TextView Nolang = getActivity().findViewById(R.id.NoLanguageslabel);
        TextView ViewMore_Language = getActivity().findViewById(R.id.Viewmore_EditLanguages);
        RecyclerView Language_RCV = getActivity().findViewById(R.id.languagesrecyclerView);
        binding.Langsavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                preferenceManager = new PreferenceManager(getActivity());
                preferenceManager.putStringArray(Constants.KEY_LANGUAGES,Constants.LanguageChip_list);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                Fragment bottomFragment = manager.findFragmentById(R.id.addlanguageframe);
                ft.hide(bottomFragment);
                ft.commit();

                //Reload The RecyclerView Adapter
                Selected_skillsAdapter selectedLanguagesAdapter;
                selectedLanguagesAdapter =  new Selected_skillsAdapter(preferenceManager.getStringArray(Constants.KEY_LANGUAGES));
                Language_RCV.setAdapter(selectedLanguagesAdapter);
                Language_RCV.setVisibility(View.VISIBLE);

                ArrayList<String> Languagelist = new ArrayList<>();
                Languagelist = preferenceManager.getStringArray(Constants.KEY_LANGUAGES);
                if(Languagelist.size() > 0){
                    Nolang.setVisibility(View.GONE);

                }else {
                    Nolang.setVisibility(View.VISIBLE);
                }

                if(preferenceManager.getStringArray(Constants.KEY_LANGUAGES).size() >= 6){
                    ViewMore_Language.setVisibility(View.VISIBLE);
                    ArrayList<String> LanguagesArray_new = new ArrayList<String>(preferenceManager.getStringArray(Constants.KEY_LANGUAGES).subList(0,5));
                    selectedLanguagesAdapter = new Selected_skillsAdapter(LanguagesArray_new);
                    Language_RCV.setAdapter(selectedLanguagesAdapter);
                    selectedLanguagesAdapter.notifyDataSetChanged();
                }else{
                    ViewMore_Language.setVisibility(View.GONE);
                }

            }
        });
    }

    private  void Cancellanguage(View view){
        TextView Nolang = getActivity().findViewById(R.id.NoLanguageslabel);
        RecyclerView Language_RCV = getActivity().findViewById(R.id.languagesrecyclerView);
        binding.Langcancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferenceManager = new PreferenceManager(getActivity());
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                Fragment bottomFragment = manager.findFragmentById(R.id.addlanguageframe);
                ft.hide(bottomFragment);
                ft.commit();

                ArrayList<String> Languagelist = new ArrayList<>();
                Languagelist = preferenceManager.getStringArray(Constants.KEY_LANGUAGES);
                if(Languagelist.size() > 0){
                    Nolang.setVisibility(View.GONE);

                }else {
                    Nolang.setVisibility(View.VISIBLE);
                }

                Language_RCV.setVisibility(View.VISIBLE);
                //Nolang.setVisibility(View.VISIBLE);
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