package com.appstechio.workyzo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.appstechio.workyzo.adapters.CategoriesAdapter;
import com.appstechio.workyzo.R;
import com.appstechio.workyzo.databinding.ActivityAddskillsBinding;
import com.appstechio.workyzo.adapters.skillsAdapter;
import com.appstechio.workyzo.interfaces.Display_Toasts;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;

public class Addskills_Activity extends AppCompatActivity implements CategoriesAdapter.OnCategoryClickListener, skillsAdapter.OnskillClickListener, Display_Toasts {

    private ActivityAddskillsBinding Skills_layout;
    public static  int i = 0;
    skillsAdapter skills_Adapter;




    private ArrayList<String> categorylist = new ArrayList<>(Arrays.asList(Constants.Categorylabels));

    private ArrayList<String> computerskillslist = new ArrayList<>(Arrays.asList(Constants.computerskillslabels));
    private ArrayList<String> writingskillslist = new ArrayList<>(Arrays.asList(Constants.Writingskillslabels));
    private ArrayList<String> designskillslist = new ArrayList<>(Arrays.asList(Constants.DesignMediaskillslabels));
    private ArrayList<String> dataentryskillslist = new ArrayList<>(Arrays.asList(Constants.DataEntryskillslabels));
    private ArrayList<String> engineeringskillslist = new ArrayList<>(Arrays.asList(Constants.Engineeringskillslabels));
    private ArrayList<String> salesskillslist = new ArrayList<>(Arrays.asList(Constants.Salesskillslabels));
    private ArrayList<String> businessskillslist = new ArrayList<>(Arrays.asList(Constants.Businessskillslabels));
    private ArrayList<String> productsourcingskillslist = new ArrayList<>(Arrays.asList(Constants.ProductSourcingskillslabels));
    private ArrayList<String> mobileskillslist = new ArrayList<>(Arrays.asList(Constants.Mobileskillslabels));
    private ArrayList<String> translationskillslist = new ArrayList<>(Arrays.asList(Constants.Translationskillslabels));
    private ArrayList<String> servicesskillslist = new ArrayList<>(Arrays.asList(Constants.Servicesskillslabels));
    private ArrayList<String> shippingskillslist = new ArrayList<>(Arrays.asList(Constants.shipping_transportationskillslabels));
    private ArrayList<String> telecommunicationskillslist = new ArrayList<>(Arrays.asList(Constants.Telecommunicationsskillslabels));
    private ArrayList<String> educationskillslist = new ArrayList<>(Arrays.asList(Constants.Educationskillslabels));
    private ArrayList<String> otherskillslist = new ArrayList<>(Arrays.asList(Constants.Otherskillslabels));

    private ArrayList<ArrayList<String>> SkillsArrays = new ArrayList<>(Arrays.asList(computerskillslist,writingskillslist,designskillslist,dataentryskillslist,
            engineeringskillslist,salesskillslist,businessskillslist,productsourcingskillslist,mobileskillslist,translationskillslist,servicesskillslist,
            shippingskillslist,telecommunicationskillslist,educationskillslist,otherskillslist));

    CategoriesAdapter categoriesadapter = new CategoriesAdapter(this, categorylist, Constants.Categoryicons, R.drawable.ic_arrow_right,this);

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        Skills_layout = ActivityAddskillsBinding.inflate(getLayoutInflater());
        View view = Skills_layout.getRoot();
        setContentView(view);
        populatecategory_list();
        Backbtn_pressed();
        BacktocategoryRCVbtn_pressed();
        search_skill();
        populate_skills();
        populate_selectedSkills();
        Save_SelectedSkills();
        Constants.Skills_Array = preferenceManager.getStringArray(Constants.KEY_SKILLS);
        System.out.println(preferenceManager.getStringArray(Constants.KEY_SKILLS).toString());
        Skills_layout.selectedSkillstxt.setText(new StringBuilder().append(Constants.Skills_Array.size()).append(" out of 20 skills selected").toString());
    }

    private void populate_skills(){
        ArrayList<String> AllSkillslist = new ArrayList<String>();
        for (int i = 0; i < SkillsArrays.size(); i++) {
            for (int j = 0; j < SkillsArrays.get(i).size(); j++) {
              AllSkillslist.add(SkillsArrays.get(i).get(j));
            }
        }
        skills_Adapter = new skillsAdapter(this, AllSkillslist, R.drawable.ic_add,this);
    }

    private void populate_selectedSkills(){
        Skills_layout.selectedSkillstxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skills_Adapter = new skillsAdapter(Skills_layout.getRoot().getContext(),Constants.Skills_Array);
                Skills_layout.CategoriesRCV.setAdapter(skills_Adapter);
                Skills_layout.SelectcategoryLabel.setText("Selected Skills");
                Skills_layout.backtocategoryRCVBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    private  void Save_SelectedSkills(){
        Skills_layout.SaveSkillsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferenceManager.putStringArray(Constants.KEY_SKILLS,Constants.Skills_Array);
                showToast("Skills are added successfully",1);
                finish();
            }
        });
    }
    private  void search_skill(){

        Skills_layout.searchboxSkillstxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Skills_layout.backtocategoryRCVBtn.setVisibility(View.VISIBLE);
                Skills_layout.SelectcategoryLabel.setText(new StringBuilder().append("Search results for").append(" '").append(charSequence).append("'").toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

                filter(editable.toString());
                Skills_layout.CategoriesRCV.setAdapter(skills_Adapter);

                String searchinput = Skills_layout.searchboxSkillstxt.getText().toString().trim();
                if (searchinput.isEmpty()){
                    Skills_layout.CategoriesRCV.setAdapter(categoriesadapter);
                    Skills_layout.SelectcategoryLabel.setText("Select a category");
                    Skills_layout.backtocategoryRCVBtn.setVisibility(View.INVISIBLE);

                }

            }
        });
    }

    private  void filter (String text){
        ArrayList<String> filteredskills = new ArrayList<String>();

        for (int i = 0; i < SkillsArrays.size(); i++) {
            for (int j = 0; j < SkillsArrays.get(i).size(); j++) {
               if(SkillsArrays.get(i).get(j).toLowerCase().contains(text.toLowerCase())){
                 filteredskills.add(SkillsArrays.get(i).get(j));
               }
            }
        }

        skills_Adapter.filterList(filteredskills);
    }
    private void populatecategory_list() {

            Skills_layout.CategoriesRCV.setAdapter(categoriesadapter);
            DividerItemDecoration itemDecor = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
            Skills_layout.CategoriesRCV.addItemDecoration(itemDecor);
            Skills_layout.CategoriesRCV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            Skills_layout.CategoriesRCV.setHasFixedSize(true);

    }

    @Override
    public void onCategoryClick(int position) {

                skills_Adapter = new skillsAdapter(this, SkillsArrays.get(position), R.drawable.ic_add,this);
                Skills_layout.CategoriesRCV.setAdapter(skills_Adapter);
                Skills_layout.SelectcategoryLabel.setText(categorylist.get(position));
                Skills_layout.backtocategoryRCVBtn.setVisibility(View.VISIBLE);

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Skills_layout.SkillsScroll.fullScroll(ScrollView.FOCUS_UP);
                    }
                };
                Skills_layout.SkillsScroll.post(runnable);




    }

    @Override
    public void onSkillClick(int position) {
        skills_Adapter.notifyItemChanged(position);
    }


    private void BacktocategoryRCVbtn_pressed(){
        Skills_layout.backtocategoryRCVBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Skills_layout.CategoriesRCV.setAdapter(categoriesadapter);
                Skills_layout.SelectcategoryLabel.setText("Select a category");
                Skills_layout.backtocategoryRCVBtn.setVisibility(View.INVISIBLE);

            }
        });
    }

    private  void Backbtn_pressed(){
        Skills_layout.AddskillsBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.BACKFROMSKILLS_FLAG = true;
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Constants.BACKFROMSKILLS_FLAG = true;
        finish();
    }

    @Override
    protected  void onStart(){

        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }



}

