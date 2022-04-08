package com.appstechio.workyzo.adapters;

import com.appstechio.workyzo.fragments.Freelancers_Tab;
import com.appstechio.workyzo.fragments.Jobs_Tab;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HomePagerAdapter extends FragmentStateAdapter {

    public HomePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return  new Freelancers_Tab();
            default:
                return new Jobs_Tab();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
