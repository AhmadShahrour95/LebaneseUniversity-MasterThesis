package com.appstechio.workyzo.adapters;

import com.appstechio.workyzo.fragments.Freelancers_Tab;
import com.appstechio.workyzo.fragments.JobProposals;
import com.appstechio.workyzo.fragments.Jobdetails;
import com.appstechio.workyzo.fragments.Jobs_Tab;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class JobDetailsPagerAdapter extends FragmentStateAdapter {

    public JobDetailsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return  new JobProposals();
            default:
                return new Jobdetails();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
