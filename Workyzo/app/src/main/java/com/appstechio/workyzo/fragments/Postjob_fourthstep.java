package com.appstechio.workyzo.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appstechio.workyzo.R;
import com.appstechio.workyzo.adapters.FreelancerUsersAdapter;
import com.appstechio.workyzo.adapters.Selected_filesAdapter;
import com.appstechio.workyzo.databinding.FragmentPostjobFourthstepBinding;
import com.appstechio.workyzo.interfaces.Display_Toasts;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.DuplicateMap;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;
import kr.co.prnd.StepProgressBar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Postjob_fourthstep#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Postjob_fourthstep extends Fragment implements Display_Toasts {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Postjob_fourthstep() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Postjob_firststep.
     */
    // TODO: Rename and change types and number of parameters
    public static Postjob_fourthstep newInstance(String param1, String param2) {
        Postjob_fourthstep fragment = new Postjob_fourthstep();
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

    private static  int Salarytye_Flag = 0 ;
    private FragmentPostjobFourthstepBinding binding;
    private ActivityResultLauncher<Intent> Openfile_picker;
    private Selected_filesAdapter FilesAdapter;
    private String size = null;
    private String filename = null;
    private  PreferenceManager preferenceManager;



    private ArrayList<MediaFile> Mediafiles;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      binding =FragmentPostjobFourthstepBinding.inflate(inflater, container, false);
      View view = binding.getRoot();

        Click_UploadFiles(view);
        ClickBack_Btn(view);
        Click_NextBtn(view);
        preferenceManager = new PreferenceManager(getActivity());

        Openfile_picker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            preferenceManager = new PreferenceManager(getActivity());
                             Mediafiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);

                            //Do something with files
                            //Get file name
                            for (int i = 0; i<Mediafiles.size();i++){

                                Constants.Mediafiles_Uploaded.add(Mediafiles.get(i));
                                 filename = Mediafiles.get(i).getName();
                                Uri Fileuri = Mediafiles.get(i).getUri();
                                long filesize = Mediafiles.get(i).getSize();
                                int unitsize = String.valueOf(filesize).length();
                                if (unitsize <= 3){
                                    size = filesize+" byte";
                                }else if (unitsize <= 6){
                                    size = filesize/1024+" kb";
                                }else {
                                    size = filesize/(1024*1024)+" mb";
                                }
                                     HashMap<String,String> File_Description = new HashMap<>();
                                     File_Description.put("FileName",filename);
                                     File_Description.put("FileSize",size);

                                //Add hashmap to arraylist
                                Constants.Files_Map.add(File_Description);

                                }


                        }
                            FilesAdapter = new Selected_filesAdapter(Constants.Files_Map);
                            DividerItemDecoration itemDecor = new DividerItemDecoration(binding.getRoot().getContext(), DividerItemDecoration.VERTICAL);
                            binding.SelectedFilesRCV.addItemDecoration(itemDecor);
                            binding.SelectedFilesRCV.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
                            binding.SelectedFilesRCV.setAdapter(FilesAdapter);
                        }

                });

        if(Constants.EDITPOST_FLAG){

           Constants.Files_Map = preferenceManager.getMapArray(Constants.KEY_JOB_UPLOADED_FILES_UPDATE);
           if(Constants.Files_Map != null){
               if(Constants.Files_Map.size() >0){
                   FilesAdapter = new Selected_filesAdapter(Constants.Files_Map);
                   DividerItemDecoration itemDecor = new DividerItemDecoration(binding.getRoot().getContext(), DividerItemDecoration.VERTICAL);
                   binding.SelectedFilesRCV.addItemDecoration(itemDecor);
                   binding.SelectedFilesRCV.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
                   binding.SelectedFilesRCV.setAdapter(FilesAdapter);
               }

           }else{

           }

        }

      return view;
    }

    private void Click_NextBtn(View view){

        ViewPager2 viewPager2 = getActivity().findViewById(R.id.PostjobViewPager);
        StepProgressBar stepProgressBar = getActivity().findViewById(R.id.StepProgressBar);


        binding.NextStep3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                preferenceManager = new PreferenceManager(getActivity());
                        Constants.step = Constants.step + 1;
                        if (Constants.step == 0) {
                            viewPager2.setCurrentItem(0);
                        } else if (Constants.step == 1) {
                            viewPager2.setCurrentItem(1);
                        } else if (Constants.step == 2) {
                            viewPager2.setCurrentItem(2);
                        } else if (Constants.step == 3) {
                            viewPager2.setCurrentItem(3);
                        } else if (Constants.step == 4) {
                            viewPager2.setCurrentItem(4);
                        }
                        stepProgressBar.setStep(Constants.step);
                    }

                });
        }



    private  void ClickBack_Btn(View view){

        ViewPager2 viewPager2 = getActivity().findViewById(R.id.PostjobViewPager);
        StepProgressBar stepProgressBar = getActivity().findViewById(R.id.StepProgressBar);
        binding.BacktopreviousStep3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.step = Constants.step - 1;

                if(Constants.step == 0){
                    viewPager2.setCurrentItem(0);
                }else if (Constants.step == 1) {
                    viewPager2.setCurrentItem(1);
                } else if (Constants.step == 2) {
                    viewPager2.setCurrentItem(2);
                } else if (Constants.step == 3){
                    viewPager2.setCurrentItem(3);
                } else {
                    viewPager2.setCurrentItem(4);
                }

                stepProgressBar.setStep(Constants.step);

            }
        });

    }
    private void Click_UploadFiles (View view){
        binding.ImagepickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(!checkCameraPermission()){
                requestCameraPermission();
            }else {
                ImagePicker();
            }
            }
        });

        binding.FilepickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    FilePicker();
            }
        });

        binding.VideopickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkCameraPermission()){
                    requestCameraPermission();
                }else {
                    VideoPicker();
                }
            }
        });

        binding.AudiopickerBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    AudioPicker();
            }
        });


    }


    private static HashMap<String, String>  convertArrayListToHashMap(ArrayList<String> arrayList)
    {

        HashMap<String, String> hashMap = new HashMap<>();

        for (int i = 0;i<arrayList.size();i=i+2) {

            hashMap.put("FileName", arrayList.get(i));
            hashMap.put("FileSize", arrayList.get(i + 1));

            Constants.Files_Map.add(hashMap);

        }

        return hashMap;
    }

    private void requestCameraPermission() {
        requestPermissions(new String[] {Manifest.permission.CAMERA},1);
    }

    private Boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(binding.getRoot().getContext(), Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        //boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void ImagePicker(){
        Intent intent = new Intent(binding.getRoot().getContext(), FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowImages(true)
                .enableImageCapture(true)
                .setMaxSelection(10)
                .setSkipZeroSizeFiles(true)
                .build());

        Openfile_picker.launch(intent);
    }

    private void VideoPicker(){
        Intent intent = new Intent(binding.getRoot().getContext(), FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowImages(false)
                .setShowVideos(true)
                .enableVideoCapture(true)
                .setMaxSelection(10)
                .setSkipZeroSizeFiles(true)
                .build());

        Openfile_picker.launch(intent);
    }

    private void AudioPicker(){
        Intent intent = new Intent(binding.getRoot().getContext(), FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowAudios(true)
                .setShowImages(false)
                .setShowVideos(false)
                .setMaxSelection(10)
                .setSkipZeroSizeFiles(true)
                .build());

        Openfile_picker.launch(intent);
    }

    private void FilePicker(){
        Intent intent = new Intent(binding.getRoot().getContext(), FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowFiles(true)
                .setShowImages(false)
                .setShowVideos(false)
                .setSuffixes("txt", "pdf", "html", "rtf", "csv", "xml",
                        "zip", "tar", "gz", "rar", "7z","torrent",
                        "doc", "docx", "odt", "ott",
                        "ppt", "pptx", "pps",
                        "xls", "xlsx", "ods", "ots")
                .setMaxSelection(10)
                .setSkipZeroSizeFiles(true)
                .build());

        Openfile_picker.launch(intent);
    }



    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void showToast(String message, int duration) {

    }
}