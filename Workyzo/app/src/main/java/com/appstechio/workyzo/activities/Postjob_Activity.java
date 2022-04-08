package com.appstechio.workyzo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.appstechio.workyzo.R;
import com.appstechio.workyzo.adapters.PostJobProcess_PagerAdapter;
import com.appstechio.workyzo.databinding.ActivityPostjobBinding;
import com.appstechio.workyzo.fragments.Postjob_firststep;
import com.appstechio.workyzo.interfaces.Display_Toasts;
import com.appstechio.workyzo.models.Job;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;

import com.google.android.material.chip.Chip;

import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Postjob_Activity extends AppCompatActivity implements Display_Toasts {

    private ActivityPostjobBinding postjob_layout;
    private PreferenceManager preferenceManager;
    ArrayAdapter<String> adapteritems;
    private Job JobPosts;

    private static int step = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postjob_layout = ActivityPostjobBinding.inflate(getLayoutInflater());
        View view = postjob_layout.getRoot();
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(view);

        Click_BacktoHome();


        postjob_layout.inputJobDescriptionPosttxt.setMovementMethod(new ScrollingMovementMethod());


        //MainView Pager
        postjob_layout.PostjobViewPager.setUserInputEnabled(false);
        postjob_layout.PostjobViewPager.setCurrentItem(step);
        postjob_layout.PostjobViewPager.setAdapter(new PostJobProcess_PagerAdapter(this));

    }



    private void Click_BacktoHome(){
        postjob_layout.BackbtnToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.step = 0;
                    step = 0;
                    Constants.EDITPOST_FLAG =false;
                    finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Constants.step = 0;
        step = 0;
        Constants.EDITPOST_FLAG =false;
        finish();
    }

    private void Load_EditJobData(){

        JobPosts = (Job) getIntent().getSerializableExtra(Constants.KEY_JOB);
        preferenceManager.putString(Constants.KEY_JOB_ID_UPDATE,JobPosts.getJob_ID());
        preferenceManager.putString(Constants.KEY_JOB_TITLE_UPDATE,JobPosts.getTitle());
        preferenceManager.putString(Constants.KEY_JOB_DESCRIPTION_UPDATE,JobPosts.getDescription());
        preferenceManager.putStringArray(Constants.KEY_JOB_SKILLS_REQUIRED_UPDATE,JobPosts.getSkillsRequired());
        preferenceManager.putString(Constants.KEY_JOB_PAYMENT_UPDATE,JobPosts.getBudget().get(Constants.KEY_JOB_PAYMENT_TYPE).toString());
        preferenceManager.putString(Constants.KEY_JOB_PAYMENT_TYPE_UPDATE,JobPosts.getBudget().get(Constants.KEY_JOB_PAYMENT_TYPE).toString());
        preferenceManager.putString(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET_UPDATE,JobPosts.getBudget().get(Constants.KEY_JOB_PAYMENT_MINIMUM_BUDGET).toString());
        preferenceManager.putString(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET_UPDATE,JobPosts.getBudget().get(Constants.KEY_JOB_PAYMENT_MAXIMUM_BUDGET).toString());
        preferenceManager.putMapArray(Constants.KEY_JOB_UPLOADED_FILES_UPDATE,JobPosts.getUploadedFiles());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Constants.EDITPOST_FLAG){
            Load_EditJobData();

            //System.out.println("Editpost: "+    preferenceManager.getString(Constants.KEY_JOB_TITLE_UPDATE));
        }


    }
}