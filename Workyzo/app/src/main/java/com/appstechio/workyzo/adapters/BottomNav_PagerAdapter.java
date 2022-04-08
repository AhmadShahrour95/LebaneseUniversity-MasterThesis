package com.appstechio.workyzo.adapters;

import android.os.Build;

import com.appstechio.workyzo.fragments.AgendaFragment;
import com.appstechio.workyzo.fragments.Chat;
import com.appstechio.workyzo.fragments.HomeFragment;
import com.appstechio.workyzo.fragments.ProfileFragment;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class BottomNav_PagerAdapter extends FragmentStateAdapter {

    public BottomNav_PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:

                return  new AgendaFragment();
            case 2:

                return  new Chat();
            case 3:

                return  new ProfileFragment();
            default:

                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
