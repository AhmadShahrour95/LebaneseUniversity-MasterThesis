package com.appstechio.workyzo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.appstechio.workyzo.adapters.HomePagerAdapter;
import com.appstechio.workyzo.adapters.JobDetailsPagerAdapter;
import com.appstechio.workyzo.databinding.ActivityJobDetailsBinding;
import com.appstechio.workyzo.models.Job;
import com.appstechio.workyzo.utilities.Constants;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class JobDetails_Activity extends BaseActivity {

    private ActivityJobDetailsBinding JobsDetails_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JobsDetails_layout = ActivityJobDetailsBinding.inflate(getLayoutInflater());
        View view = JobsDetails_layout.getRoot();


        addfragment();
        backbtn_pressed();
        setContentView(view);
    }




    private void addfragment() {

        JobsDetails_layout.JobDetailsViewPager.setAdapter(new JobDetailsPagerAdapter(this));

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                JobsDetails_layout.tabs, JobsDetails_layout.JobDetailsViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0: {
                        tab.setText("Details");
                        break;
                    }
                    case 1: {
                        tab.setText("Proposals");
                        break;
                    }
                }
            }
        });
        tabLayoutMediator.attach();
    }


    private  void backbtn_pressed(){
        JobsDetails_layout.backtoBrowseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.JOB_INFO = null;
                 finish();

            }
        });
    }
}