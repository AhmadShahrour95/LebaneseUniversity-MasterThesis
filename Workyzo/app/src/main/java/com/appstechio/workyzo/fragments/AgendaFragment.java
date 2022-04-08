package com.appstechio.workyzo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appstechio.workyzo.adapters.AgendaPagerAdapter;
import com.appstechio.workyzo.adapters.HomePagerAdapter;
import com.appstechio.workyzo.databinding.FragmentMyagendaBinding;
import com.appstechio.workyzo.utilities.Constants;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AgendaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgendaFragment extends Fragment {
    private FragmentMyagendaBinding binding;
    TabLayout tabLayout;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AgendaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgendaFragment newInstance(String param1, String param2) {
        AgendaFragment fragment = new AgendaFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyagendaBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        // View view = inflater.inflate(R.layout.fragment_home, container, false);
        // addfragment(view);
        addfragment(view);
        return view;

    }

    private void addfragment(View view) {
        binding.ViewPager.setAdapter(new AgendaPagerAdapter(getActivity()));

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                binding.tabLayoutBrowse, binding.ViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0: {
                        tab.setText("Hired Jobs");
                        break;
                    }
                    case 1: {
                        tab.setText("Posted Jobs");
                        break;
                    }

                    case 2: {
                        tab.setText("My Proposals");
                        break;
                    }
                }
            }
        });
        tabLayoutMediator.attach();
    }

    @Override
    public void onResume() {
        super.onResume();

        View view = binding.getRoot().getRootView();
        Constants.MYJOBS_FLAG = true;
        Constants.PROFILE_FLAG = true;
        Log.d("FLAG", String.valueOf(Constants.MYJOBS_FLAG));

    }
}