package com.appstechio.workyzo.adapters;

import com.appstechio.workyzo.fragments.Freelancers_Tab;
import com.appstechio.workyzo.fragments.HiredJobs_Tab;
import com.appstechio.workyzo.fragments.Jobs_Tab;
import com.appstechio.workyzo.fragments.MyProposals_Tab;
import com.appstechio.workyzo.fragments.PostedJobs_Tab;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AgendaPagerAdapter extends FragmentStateAdapter {

    public AgendaPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return  new HiredJobs_Tab();
            case 1:
                return  new PostedJobs_Tab();
            default:
                return new MyProposals_Tab();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
