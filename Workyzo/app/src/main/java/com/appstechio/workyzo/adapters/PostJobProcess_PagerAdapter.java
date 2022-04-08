package com.appstechio.workyzo.adapters;

import android.os.Build;

import com.appstechio.workyzo.fragments.Postjob_firststep;
import com.appstechio.workyzo.fragments.Chat;
import com.appstechio.workyzo.fragments.HomeFragment;
import com.appstechio.workyzo.fragments.Postjob_fourthstep;
import com.appstechio.workyzo.fragments.Postjob_laststep;
import com.appstechio.workyzo.fragments.Postjob_secondstep;
import com.appstechio.workyzo.fragments.Postjob_thirdstep;
import com.appstechio.workyzo.fragments.ProfileFragment;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PostJobProcess_PagerAdapter extends FragmentStateAdapter {

    public PostJobProcess_PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return  new Postjob_secondstep();
            case 2:
                return  new Postjob_thirdstep();
            case 3:
               return  new Postjob_fourthstep();
            case 4:
                return new Postjob_laststep();
            default:
                return new Postjob_firststep();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
