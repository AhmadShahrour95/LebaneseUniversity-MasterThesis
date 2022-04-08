package com.appstechio.workyzo.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.ResultReceiver;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.appstechio.workyzo.R;
import com.appstechio.workyzo.adapters.EducationAdapter;
import com.appstechio.workyzo.adapters.ExperienceAdapter;
import com.appstechio.workyzo.adapters.Selected_skillsAdapter;
import com.appstechio.workyzo.adapters.skillsAdapter;
import com.appstechio.workyzo.databinding.ActivityEditprofileBinding;
import com.appstechio.workyzo.interfaces.Display_Toasts;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class Editprofile_Activity extends AppCompatActivity implements Display_Toasts,skillsAdapter.OnskillClickListener {

    private FirebaseAuth mAuth;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private ActivityEditprofileBinding Editlayout;
    ArrayAdapter<String> adapteritems;
    Selected_skillsAdapter selectedSkillsAdapter;
    Selected_skillsAdapter selectedLanguagesAdapter;
    ExperienceAdapter experienceAdapter;
    EducationAdapter educationAdapter;
    private PreferenceManager preferenceManager;
    private String encodedImage;
    public static final int CAMERA_REQUEST = 100;
    public static final int STORAGE_REQUEST = 101;
    String cameraPermission[];
    String storagePermission[];
    private ResultReceiver resultReceiver;
    private boolean User_RequestDeleteBefore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Editlayout = ActivityEditprofileBinding.inflate(getLayoutInflater());
        View view = Editlayout.getRoot();
        setContentView(view);



       // resultReceiver = new AddressResultReceiver(new Handler());
        preferenceManager = new PreferenceManager(getApplicationContext());
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
           // ActivityCompat.requestPermissions(Editprofile_Activity.this, new String[]
                  //  {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            GetCurrentLocation();
        }


       // Editlayout.inputAddresstxt.setText(preferenceManager.getString(Constants.KEY_ADDRESS));
        Editlayout.autoCompletecountries.setTextIsSelectable(true);
        Editlayout.autoCompletecountries.setFocusable(false);
        Editlayout.autoCompletecountries.setFocusableInTouchMode(false);
           Editlayout.autoCompletecountries.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Editlayout.ccp.launchCountrySelectionDialog();
                   String country_selected = Editlayout.ccp.getSelectedCountryName().toString();
                   //showToast(country_selected,1);
                   if(country_selected != null){
                       Editlayout.ccp.setVisibility(View.VISIBLE);
                       Editlayout.autoCompletecountries.setText("");
                   }

               }
           });


        Changeprofilepic();
        Backtoprofile();
        Showaddskills();
        Showaddexperience();
        Showaddeducation();
        Showaddlanguage();
        populatesalarytype();
        LoadUserdata_Tofields();
        SaveProfile_Click();
        MobileNumberFocusListener();
        //GetCurrentLocation_Click();
        DeleteAccount();
        ShowChangePasswordDialog();
        Visibility_listener();
        //GetCurrentLocation();

    }


/*    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GetCurrentLocation();
                finish();
                startActivity(getIntent());
            } else {
                showToast("Permission denied!", 1);
            }
        }
    }*/

    private void DeleteAccount(){
        Editlayout.DeleteAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                database.collection(Constants.KEY_COLLECTION_ACCOUNT_DELETION)
                        .whereEqualTo(Constants.KEY_ACCOUNT_DELETION_FROM,preferenceManager.getString(Constants.KEY_EMAILADDRESS))
                        .get()
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful() && task1.getResult() != null &&
                                    task1.getResult().getDocuments().size() > 0) {
                               // User_RequestDeleteBefore = true;
                               // if(User_RequestDeleteBefore){
                                    showToast("Already Requested",1);
                               // }else{
                                // }
                            }else {
                                showAccountDeletionDialog();
                            }
                        });

            }

        });
    }

    private void ShowChangePasswordDialog(){
        Editlayout.ChangePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Editprofile_Activity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.changepassword_dialog);

                Button Changebtn = dialog.findViewById(R.id.Change_btn);
                TextInputLayout New_passwordtxt = dialog.findViewById(R.id.inputnewPassword);

                dialog.show();

                Changebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String newPassword = New_passwordtxt.getEditText().getText().toString();

                        if(newPassword.length() < 6){
                            New_passwordtxt.setErrorEnabled(true);
                            New_passwordtxt.setError("Minimum length 6 characters");
                        }else if (newPassword.isEmpty()){
                            New_passwordtxt.setErrorEnabled(true);
                            New_passwordtxt.setError(getString(R.string.RequiredField_Error));
                        }else{
                            New_passwordtxt.setErrorEnabled(false);

                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                showToast(" Password changed successfully",1);
                                            }
                                        }
                                    });

                            dialog.cancel();
                        }


                    }
                });

            }

        });
    }

    void showAccountDeletionDialog(){
        final Dialog dialog = new Dialog(Editprofile_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.accountdeletion_dialog);

        Button Submitbtn = dialog.findViewById(R.id.SubmitRequest_btn);
        TextInputEditText Reason_content = dialog.findViewById(R.id.inputReasontxt);

        dialog.show();

        Submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> Deletion_request = new HashMap<>();
                Deletion_request.put(Constants.KEY_DELETION_REASON,Reason_content.getText().toString());
                Deletion_request.put(Constants.KEY_ACCOUNT_DELETION_FROM,preferenceManager.getString(Constants.KEY_EMAILADDRESS));

                FirebaseFirestore database = FirebaseFirestore.getInstance();

                database.collection(Constants.KEY_COLLECTION_ACCOUNT_DELETION).document()
                        .set(Deletion_request)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                showToast("Your account will be deleted in 5 days",1);
                            }
                        })
                        .addOnFailureListener(exception -> {
                            showToast(exception.getMessage(),1);
                        });

                dialog.cancel();
            }
        });
    }


    private void MobileNumberFocusListener(){
        if(!Editlayout.inputMobilenumbertxt.getText().toString().isEmpty()){
            Editlayout.ccpMobile.setVisibility(View.VISIBLE);
        }

        Editlayout.ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Editlayout.ccpMobile.setCountryForNameCode(Editlayout.ccp.getSelectedCountryNameCode().toString());
            }
        });

        Editlayout.inputMobilenumbertxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    Editlayout.ccpMobile.setVisibility(View.VISIBLE);

                }else{
                    Editlayout.ccpMobile.setVisibility(View.VISIBLE);
                    if (!Editlayout.inputMobilenumbertxt.getText().toString().isEmpty()){
                        Editlayout.ccpMobile.setVisibility(View.VISIBLE);
                    }else{
                        Editlayout.ccpMobile.setVisibility(View.INVISIBLE);
                    }

                }
            }
        });


    }



    void showCustomDialog(){
        final Dialog dialog = new Dialog(Editprofile_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.profilecompletion_dialog);

        ProgressBar PRG_Bar = dialog.findViewById(R.id.profileDialog_progressbar);
        Button gotoeditbtn = dialog.findViewById(R.id.Gotoeditprofile_btn);
        TextView valuetxt = dialog.findViewById(R.id.dialogprogress_value);
        TextView Usernametxt = dialog.findViewById(R.id.dialog_title);
        Usernametxt.setText(new StringBuilder().append("Hello, ").append(preferenceManager.getString(Constants.KEY_FIRSTNAME).toString()));
       // int value = preferenceManager.getInt(Constants.KEY_PROFILECOMPLETION_VALUE) * 7;
       // int PERCENT = value + 9;
        System.out.println("Value percent" + Editlayout.progressBarCompleteProfile.getProgress());
        PRG_Bar.setProgress(Editlayout.progressBarCompleteProfile.getProgress());
        valuetxt.setText(Editlayout.ProgressbarPercentageLabel.getText().toString());

        gotoeditbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent EditprofileAct = new Intent(getApplicationContext(),Editprofile_Activity.class);
                //EditprofileAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //startActivity(EditprofileAct);
                dialog.cancel();
            }
        });

        if(Editlayout.progressBarCompleteProfile.getProgress() < 91){
            dialog.show();
       }else{
            //Don't Show Dialog
       }

    }

    //GET CURRENT LOCATION
    @SuppressLint("MissingPermission")
    private void GetCurrentLocation() {
        LocationRequest locationRequest =  LocationRequest.create()
        .setInterval(10000)
        .setFastestInterval(3000)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(Editprofile_Activity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(Editprofile_Activity.this)
                                .removeLocationUpdates(this);
                        if ( locationResult != null && locationResult.getLocations().size() > 0 ){
                            int LatestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(LatestLocationIndex).getLatitude();
                            double Longtitude = locationResult.getLocations().get(LatestLocationIndex).getLongitude();
                            Geocoder geocoder = new Geocoder(Editprofile_Activity.this, Locale.getDefault());
                            //Editlayout.inputAddresstxt.setText(String.valueOf(latitude) +","+String.valueOf(Longtitude));
                            try {
                                List<Address> addresses = geocoder.getFromLocation(latitude, Longtitude, 1);
                                setUpLocationdata(addresses);


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Location location = new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(Longtitude);
                            //fetchAdddressFromLocation(location);
                        }

                    }
                }, Looper.getMainLooper());

    }

    private void setUpLocationdata(List<Address> addresses) {
        String add = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String zip = addresses.get(0).getPostalCode();
       String Country_Name = addresses.get(0).getCountryName().toString();
        // get First letter of the string
       // String firstLetStr = Country_Name.substring(0, 1);

        // Get remaining letter using substring
        //String remLetStr = Country_Name.substring(1);
       // remLetStr = remLetStr.toLowerCase();
        //String Countryname = firstLetStr+remLetStr;
        String Countryname = capitalizeWord(Country_Name);
        if(Countryname.equals("Usa")){
            Countryname = "United States";
        }else if(Countryname.equals("Uk")){
            Countryname = "United Kingdom";
        }
        String Country_Code = getCountryCode(Countryname);

        //preferenceManager.putString(Constants.KEY_COUNTRY_CODE,Country_Code);

        if(preferenceManager.getString(Constants.KEY_COUNTRY) == null || preferenceManager.getString(Constants.KEY_COUNTRY) == ""){
            //Editlayout.ccp.setAutoDetectedCountry(true);
            Editlayout.ccp.setCountryForNameCode(Country_Code);
        }else {

            String Country_Code_Server = getCountryCode(preferenceManager.getString(Constants.KEY_COUNTRY));
           Editlayout.ccp.setCountryForNameCode(Country_Code_Server);
        }
        final String[] names = {"City.:-" + city, "State.:-" + state, "Address:-" + add, "Zip:-" + zip};

    }


    public static String capitalizeWord(String str){
        String words[]=str.split("\\s");
        String capitalizeWord="";
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord+=first.toUpperCase()+afterfirst+" ";
        }
        return capitalizeWord.trim();
    }

    public String getCountryCode(String countryName) {

        // Get all country codes in a string array.
        String[] isoCountryCodes = Locale.getISOCountries();
        String countryCode = "";
        // Iterate through all country codes:
        for (String code : isoCountryCodes) {
            // Create a locale using each country code
            Locale locale = new Locale("", code);
            // Get country name for each code.
            String name = locale.getDisplayCountry();
            if(name.equals(countryName))
            {
                countryCode = code;
                break;
            }
        }
        return countryCode;
    }



private  void Visibility_listener(){
        Editlayout.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Constants.VISIBLEASFREELANCER_FLAG =  true;
                }else {
                    Constants.VISIBLEASFREELANCER_FLAG =  false;
                }
            }
        });
}

    //LOAD USER DATA IN THE FIELDS
    private void LoadUserdata_Tofields () {


        if (preferenceManager.getString(Constants.KEY_PROFILE_IMAGE) == null ||preferenceManager.getString(Constants.KEY_PROFILE_IMAGE) == "" ) {
            Editlayout.ProfilepicViewedit.setImageResource(R.drawable.avatar_man);
        } else {
            //CONVERT STRING BASE 64 TO BITMAP
            byte[] decodedString = Base64.decode(preferenceManager.getString(Constants.KEY_PROFILE_IMAGE), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Editlayout.ProfilepicViewedit.setImageBitmap(decodedByte);

            Bitmap bitmap = ((BitmapDrawable)Editlayout.ProfilepicViewedit.getDrawable()).getBitmap();
            encodedImage = encodeImage(bitmap);
        }

    }

    private String amount;
    private void SaveProfile_Click(){
        Editlayout.SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ImageView flag = Editlayout.ccp.getImageViewFlag();
                Bitmap flag_image=((BitmapDrawable)flag.getDrawable()).getBitmap();
                String country_flag = encodeImage(flag_image);

                HashMap<String,String> Address = new HashMap<String,String>();
                Address.put(Constants.KEY_COUNTRY,Editlayout.ccp.getSelectedCountryName());
                Address.put(Constants.KEY_CITY,Editlayout.inputCitytxt.getText().toString());


                HashMap<String,Object> Salary = new HashMap<String,Object>();
                 amount =Editlayout.inputsalarytxt.getText().toString();
                //Remove trailing zero
                amount = amount.contains(".") ? amount.replaceAll("0*$","").replaceAll("\\.$","") : amount;

                Salary.put(Constants.KEY_SALARY_TYPE,Editlayout.autoCompletesalarytype.getText().toString());
                Salary.put(Constants.KEY_SALARY_AMOUNT,amount);


                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                    DocumentReference documentReference =
                            database.collection(Constants.KEY_COLLECTION_USERS).document(
                                    preferenceManager.getString(Constants.KEY_USERID)
                            );

                    String mobilenum = "";
                    if(Editlayout.inputMobilenumbertxt.getText().toString().isEmpty()){
                        mobilenum ="";
                    }else{
                        mobilenum = Editlayout.ccpMobile.getFormattedFullNumber();
                    }

                    documentReference.update(Constants.KEY_PROFILE_IMAGE,encodedImage,
                            Constants.KEY_VISIBLE_ASFREELANCER,Constants.VISIBLEASFREELANCER_FLAG,
                            Constants.KEY_FIRSTNAME,Editlayout.inputFirstNametxt.getText().toString(),
                            Constants.KEY_LASTNAME,Editlayout.inputLastNametxt.getText().toString(),
                            Constants.KEY_USERNAME,Editlayout.inputUserNametxt.getText().toString(),
                            Constants.KEY_EMAILADDRESS,Editlayout.inputemailtxt.getText().toString(),
                            Constants.KEY_ADDRESS,Address,
                            Constants.KEY_MOBILE_NUMBER,mobilenum,
                            Constants.KEY_SALARY,Salary,
                            Constants.KEY_PROFESSIONAL_HEADLINE,Editlayout.inputProheadlinetxt.getText().toString(),
                            Constants.KEY_USER_SUMMARY,Editlayout.inputSummarytxt.getText().toString(),
                            Constants.KEY_SKILLS,preferenceManager.getStringArray(Constants.KEY_SKILLS),
                            Constants.KEY_EXPERIENCES,preferenceManager.getMapArray(Constants.KEY_EXPERIENCES),
                            Constants.KEY_EDUCATION,preferenceManager.getMapArray(Constants.KEY_EDUCATION),
                            Constants.KEY_LANGUAGES,preferenceManager.getStringArray(Constants.KEY_LANGUAGES))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    showToast("Profile updated successfully",1);
                                    preferenceManager.putString(Constants.KEY_PROFILE_IMAGE,encodedImage);
                                    preferenceManager.putBoolean(Constants.KEY_VISIBLE_ASFREELANCER,Constants.VISIBLEASFREELANCER_FLAG);
                                    preferenceManager.putString(Constants.KEY_FIRSTNAME,Editlayout.inputFirstNametxt.getText().toString());
                                    preferenceManager.putString(Constants.KEY_LASTNAME,Editlayout.inputLastNametxt.getText().toString());
                                    preferenceManager.putString(Constants.KEY_USERNAME,Editlayout.inputUserNametxt.getText().toString());
                                   // preferenceManager.putString(Constants.KEY_EMAILADDRESS,Editlayout.inputemailtxt.getText().toString());
                                    preferenceManager.putString(Constants.KEY_COUNTRY,Editlayout.ccp.getSelectedCountryName());
                                    preferenceManager.putString(Constants.KEY_MOBILE_NUMBER,Editlayout.inputMobilenumbertxt.getText().toString());
                                    preferenceManager.putString(Constants.KEY_CITY,Editlayout.inputCitytxt.getText().toString());
                                    preferenceManager.putString(Constants.KEY_SALARY_TYPE,Editlayout.autoCompletesalarytype.getText().toString());
                                    preferenceManager.putString(Constants.KEY_SALARY_AMOUNT,amount);
                                    preferenceManager.putString(Constants.KEY_PROFESSIONAL_HEADLINE,Editlayout.inputProheadlinetxt.getText().toString());
                                    preferenceManager.putString(Constants.KEY_USER_SUMMARY,Editlayout.inputSummarytxt.getText().toString());
                                    //Arrays
                                    preferenceManager.putStringArray(Constants.KEY_SKILLS,preferenceManager.getStringArray(Constants.KEY_SKILLS));
                                    preferenceManager.putMapArray(Constants.KEY_EXPERIENCES,preferenceManager.getMapArray(Constants.KEY_EXPERIENCES));
                                    preferenceManager.putMapArray(Constants.KEY_EDUCATION,preferenceManager.getMapArray(Constants.KEY_EDUCATION));
                                    preferenceManager.putStringArray(Constants.KEY_LANGUAGES,preferenceManager.getStringArray(Constants.KEY_LANGUAGES));

                                    String country_flag = "flag_"+Editlayout.ccp.getSelectedCountryName().toLowerCase().replace(" ","_");
                                    int resID = getApplicationContext().getResources().getIdentifier(country_flag , "drawable", getApplicationContext().getPackageName());
                                    preferenceManager.putInt(Constants.KEY_COUNTRY_FLAG,resID);


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showToast("Unable to update Profile",1);
                                }
                            });

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //Update email address
                if(!Editlayout.inputemailtxt.getText().toString().equals(preferenceManager.getString(Constants.KEY_EMAILADDRESS))){
                    user.updateEmail(Editlayout.inputemailtxt.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        preferenceManager.putString(Constants.KEY_EMAILADDRESS,Editlayout.inputemailtxt.getText().toString());
                                        finish();
                                        Intent MainAct = new Intent(getApplicationContext(),MainActivity.class);
                                        MainAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(MainAct);
                                        SendEmailVerification();
                                        preferenceManager.Clear();
                                        FirebaseAuth.getInstance().signOut();
                                    }
                                }
                            });
                }

            }

        });
    }

    ArrayList<TextInputLayout> validationList = new ArrayList();
    private void init_Inputs() {
        validationList.add(Editlayout.inputFirstName);
        validationList.add(Editlayout.inputLastName);
        validationList.add(Editlayout.inputUserName);
        validationList.add(Editlayout.inputSignupEmail);
        validationList.add(Editlayout.inputMobilenumber);
        //validationList.add(Editlayout.inputCountry);
        validationList.add(Editlayout.inputAddress);
        validationList.add(Editlayout.inputCity);
        validationList.add(Editlayout.inputsalarytype);
        validationList.add(Editlayout.inputsalary);
        validationList.add(Editlayout.inputProheadline);
        validationList.add(Editlayout.inputSummary);
    }

    private boolean isValid(TextInputLayout et) {
        return et.getEditText().getText().toString().trim().isEmpty();
    }


    private int Checkinput_completion() {
        //return validate();
        init_Inputs();
        int complete_value = 0;

        //Check if the image Empty
        if (Editlayout.ProfilepicViewedit.getDrawable().getConstantState().equals(getResources().getDrawable( R.drawable.avatar_man).getConstantState()))
        {
            //Do nothing
        }else{
            complete_value++;
        }


        if(Editlayout.TopSkillsRCV.getAdapter().getItemCount() == 0){
            //Do nothing
        }else{
            complete_value++;
        }

        //Check Empty fields
        for (TextInputLayout e : validationList.subList(0,11)) {
            if(!isValid(e)){
                complete_value++;


            }
        }
        if(Editlayout.ccp.getSelectedCountryName() != null){
            complete_value++;
        }
        int result = complete_value * 7;
        return  result ;
    }

    private void CheckIfAllTextchanged_Fields() {

        for (TextInputLayout e : validationList.subList(0, 11)) {

            e.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() > 0) {
                        Checkinput_completion();
                        Update_progressbar();
                    } else {
                        Checkinput_completion();
                        Update_progressbar();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


        }
    }

    private  void Update_progressbar(){
        int progress_value = Checkinput_completion();
        System.out.println(progress_value);
        int progress_percent = progress_value + 9;
        Editlayout.ProgressbarPercentageLabel.setText(String.valueOf(progress_percent) +"%");
        Editlayout.progressBarCompleteProfile.setProgress(progress_value);
        preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE,progress_percent);
    }



    private   void Backtoprofile (){
        Editlayout.Editprofilebackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(preferenceManager.getInt(Constants.KEY_PROFILECOMPLETION_VALUE) == 100 && Editlayout.ccpMobile.isValidFullNumber()
                        && preferenceManager.getString(Constants.KEY_FIRSTNAME) != "" && preferenceManager.getString(Constants.KEY_LASTNAME) != ""
                        && preferenceManager.getString(Constants.KEY_USERNAME) != ""  && preferenceManager.getString(Constants.KEY_EMAILADDRESS) != ""
                        && preferenceManager.getString(Constants.KEY_MOBILE_NUMBER) != "" && preferenceManager.getString(Constants.KEY_CITY) != ""
                        && preferenceManager.getString(Constants.KEY_SALARY_TYPE) != ""  && preferenceManager.getString(Constants.KEY_SALARY_AMOUNT) != ""
                        && preferenceManager.getString(Constants.KEY_PROFESSIONAL_HEADLINE) != "" && preferenceManager.getString(Constants.KEY_USER_SUMMARY) != ""
                        && preferenceManager.getString(Constants.KEY_PROFILE_IMAGE) != null && preferenceManager.getString(Constants.KEY_EMAILADDRESS) != ""
                        && preferenceManager.getStringArray(Constants.KEY_SKILLS).size() > 0 && preferenceManager.getStringArray(Constants.KEY_SKILLS) != null){
                    Constants.Skills_Array.clear();
                    Constants.FROMEDITPROFILE_FLAG =true;
                    finish();

                }else{
                    showToast("Please your profile should be fully completed",1);
                }


            }
        });
    }


    //SEND EMAIL VERIFICATION
    private void SendEmailVerification() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user_email = mAuth.getCurrentUser();
        if (user_email != null) {
            user_email.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            } else {
                                showToast("Sending email verification failed. Please try to resend", 1);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast(e.getMessage(),1);
                        }
                    });

        }else{
            showToast("Sending email verification failed. Please try to resend", 1);
        }
    }
    private String encodeImage(Bitmap bitmap){
        int previewwidth = 100;
        int previewHeight = bitmap.getHeight() * previewwidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewwidth,previewHeight,false);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte [] bytes = stream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    //CHANGE PROFILE PICTURE -- FROM GALLERY OR CAMERA
    private void Changeprofilepic() {
        Editlayout.changeprofilepic.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }else {
                        ImagePicker.with(Editprofile_Activity.this)
                                .crop()	    			//Crop image(Optional), Check Customization for more option
                                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .start();
                    }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Editlayout.ProfilepicViewedit.setImageBitmap(bitmap);
                encodedImage = encodeImage(bitmap);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getApplicationContext(), Editlayout.ProfilepicViewedit.getDrawable().getConstantState().toString(), Toast.LENGTH_SHORT).show();


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
           // Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void populatesalarytype() {
        String[] salarytype = {"Fixed price", "Hourly rate",};

        adapteritems = new ArrayAdapter<>(this, R.layout.list_item, salarytype);
        Editlayout.autoCompletesalarytype.setAdapter(adapteritems);
        Editlayout.autoCompletesalarytype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();

                if(Editlayout.autoCompletesalarytype.getText().toString().equals("Hourly rate")){

                }

                if (Editlayout.autoCompletesalarytype.getText().toString().equals("Hourly rate")) {
                    Editlayout.inputsalary.setSuffixText("USD/hour");
                }else{
                    Editlayout.inputsalary.setSuffixText("USD");
                }
            }
        });


    }

    private void Showaddskills(){
        Editlayout.Addtopskillsebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent skillactivity = new Intent(getApplicationContext(), Addskills_Activity.class);
                startActivity(skillactivity);

            }
        });
    }

    private void Showaddexperience(){
        Editlayout.Addexperiencebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                Fragment bottomFragment = manager.findFragmentById(R.id.addexperienceframe);
                ft.show(bottomFragment);
                ft.commit();
                Editlayout.addexperienceframe.setVisibility(View.VISIBLE);
                Editlayout.Noexperiencelabel.setVisibility(View.INVISIBLE);
                Editlayout.ExperienceprofileRCV.setVisibility(View.GONE);
            }
        });
    }

    private void Showaddeducation(){
        Editlayout.Addeducationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                Fragment bottomFragment = manager.findFragmentById(R.id.addeducationframe);
                ft.show(bottomFragment);
                ft.commit();
                Editlayout.addeducationframe.setVisibility(View.VISIBLE);
                Editlayout.Noeducationlabel.setVisibility(View.INVISIBLE);
                Editlayout.educationrecyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void Showaddlanguage(){
        Editlayout.Addlanguagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                Fragment bottomFragment = manager.findFragmentById(R.id.addlanguageframe);
                ft.show(bottomFragment);
                ft.commit();
                Editlayout.addlanguageframe.setVisibility(View.VISIBLE);
                Editlayout.NoLanguageslabel.setVisibility(View.INVISIBLE);
                Editlayout.languagesrecyclerView.setVisibility(View.GONE);

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Editlayout.editprofileScroll.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                };
                Editlayout.editprofileScroll.post(runnable);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(storagePermission,STORAGE_REQUEST);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return  result;
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(cameraPermission,CAMERA_REQUEST);
    }

    private Boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
    return result && result1;
    }





    @Override
    protected  void onStart(){
        super.onStart();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Editlayout.editprofileScroll.fullScroll(ScrollView.FOCUS_UP);
            }
        };
        Editlayout.editprofileScroll.post(runnable);
    }
    public String getFirstWordUsingSubString(String input) {
        return input.substring(0, input.indexOf(" "));
    }

    @Override
    protected void onResume() {
        super.onResume();
       //Toast.makeText(getApplicationContext(),"Resume",Toast.LENGTH_SHORT).show();
        preferenceManager = new PreferenceManager(getApplicationContext());

        Editlayout.switch1.setChecked(preferenceManager.getBoolean(Constants.KEY_VISIBLE_ASFREELANCER));


        Editlayout.inputFirstNametxt.setText(preferenceManager.getString(Constants.KEY_FIRSTNAME));
        Editlayout.inputLastNametxt.setText(preferenceManager.getString(Constants.KEY_LASTNAME));
        Editlayout.inputUserNametxt.setText(preferenceManager.getString(Constants.KEY_USERNAME));
        Editlayout.inputemailtxt.setText(preferenceManager.getString(Constants.KEY_EMAILADDRESS));

        Editlayout.inputMobilenumbertxt.setText(preferenceManager.getString(Constants.KEY_MOBILE_NUMBER));
        Editlayout.ccpMobile.setCountryForNameCode(Editlayout.ccp.getSelectedCountryNameCode().toString());
        if(Editlayout.inputMobilenumbertxt.getText().toString().isEmpty()){
            Editlayout.ccpMobile.setVisibility(View.INVISIBLE);
        }else{
            Editlayout.ccpMobile.setVisibility(View.VISIBLE);
        }


        String newmobile="";
        String mobilecode ="";

        if (preferenceManager.getString(Constants.KEY_MOBILE_NUMBER) == null || preferenceManager.getString(Constants.KEY_MOBILE_NUMBER) == ""){

        }else {
            System.out.println(preferenceManager.getString(Constants.KEY_MOBILE_NUMBER));
            mobilecode = getFirstWordUsingSubString(preferenceManager.getString(Constants.KEY_MOBILE_NUMBER));
            System.out.println(mobilecode);
            if(preferenceManager.getString(Constants.KEY_MOBILE_NUMBER).contains(mobilecode)){
                newmobile =  preferenceManager.getString(Constants.KEY_MOBILE_NUMBER).replace(mobilecode,"");
                Editlayout.inputMobilenumbertxt.setText(newmobile);
            }
        }

        Editlayout.ccpMobile.registerCarrierNumberEditText(Editlayout.inputMobilenumbertxt);

        Editlayout.ccpMobile.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                // your code
                if(Editlayout.inputMobilenumbertxt.getText().toString() == ""){

                }else{
                    if(!isValidNumber){
                        Editlayout.inputMobilenumber.setErrorEnabled(true);
                        Editlayout.inputMobilenumber.setError("Invalid Mobile Number");
                    }else{
                        Editlayout.inputMobilenumber.setErrorEnabled(false);
                    }
                }


            }
        });

        Editlayout.inputCitytxt.setText(preferenceManager.getString(Constants.KEY_CITY));

        Editlayout.autoCompletesalarytype.setText(preferenceManager.getString(Constants.KEY_SALARY_TYPE));
        Editlayout.inputsalarytxt.setText(preferenceManager.getString(Constants.KEY_SALARY_AMOUNT));
        Editlayout.inputProheadlinetxt.setText(preferenceManager.getString(Constants.KEY_PROFESSIONAL_HEADLINE));
        Editlayout.inputSummarytxt.setText(preferenceManager.getString(Constants.KEY_USER_SUMMARY));


        //Load Skills Data
        selectedSkillsAdapter =  new Selected_skillsAdapter(preferenceManager.getStringArray(Constants.KEY_SKILLS));
        Editlayout.TopSkillsRCV.setAdapter(selectedSkillsAdapter);
        Editlayout.TopSkillsRCV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Editlayout.TopSkillsRCV.setHasFixedSize(false);

        if(preferenceManager.getStringArray(Constants.KEY_SKILLS).size() >0){
            Editlayout.NoTopskillslabel.setVisibility(View.GONE);

        }else {
            Editlayout.NoTopskillslabel.setVisibility(View.VISIBLE);
        }

        if(preferenceManager.getStringArray(Constants.KEY_SKILLS).size() >= 6){
            Editlayout.ViewmoreEditskills.setVisibility(View.VISIBLE);
            ArrayList<String> skillsArray_new = new ArrayList<String>(preferenceManager.getStringArray(Constants.KEY_SKILLS).subList(0,5));
            selectedSkillsAdapter = new Selected_skillsAdapter(skillsArray_new);
            Editlayout.TopSkillsRCV.setAdapter(selectedSkillsAdapter);
            selectedSkillsAdapter.notifyDataSetChanged();
        }else{
            Editlayout.ViewmoreEditskills.setVisibility(View.GONE);
        }

        Editlayout.ViewmoreEditskills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editlayout.ViewmoreEditskills.setVisibility(View.INVISIBLE);
                selectedSkillsAdapter = new Selected_skillsAdapter(preferenceManager.getStringArray(Constants.KEY_SKILLS));
                Editlayout.TopSkillsRCV.setAdapter(selectedSkillsAdapter);
                selectedSkillsAdapter.notifyDataSetChanged();

                if(Constants.Skills_Array.size() >0){
                    ArrayList<String> skillsArray_new = new ArrayList<String>(Constants.Skills_Array);
                    selectedSkillsAdapter = new Selected_skillsAdapter(skillsArray_new);
                    Editlayout.TopSkillsRCV.setAdapter(selectedSkillsAdapter);
                    selectedSkillsAdapter.notifyDataSetChanged();
                }

            }
        });


        //Load User Skills data
    /*    selectedSkillsAdapter = new Selected_skillsAdapter(preferenceManager.getStringArray(Constants.KEY_SKILLS));
        Editlayout.TopSkillsRCV.setAdapter(selectedSkillsAdapter);
        selectedSkillsAdapter.notifyDataSetChanged();

        if(preferenceManager.getStringArray(Constants.KEY_SKILLS).size() >0){
            Editlayout.NoTopskillslabel.setVisibility(View.GONE);
        }else {
            Editlayout.NoTopskillslabel.setVisibility(View.VISIBLE);
        }

        if(preferenceManager.getStringArray(Constants.KEY_SKILLS).size() >= 6){
            Editlayout.ViewmoreEditskills.setVisibility(View.VISIBLE);
            //skillsAdapter.SetSkillsItemsnumber(new ArrayList<String>(preferenceManager.getStringArray(Constants.KEY_SKILLS).subList(0,5)));
            ArrayList<String> skillsArray_new = new ArrayList<String>(preferenceManager.getStringArray(Constants.KEY_SKILLS).subList(0,5));
            selectedSkillsAdapter = new Selected_skillsAdapter(skillsArray_new);
            Editlayout.TopSkillsRCV.setAdapter(selectedSkillsAdapter);
            selectedSkillsAdapter.notifyDataSetChanged();
        }else{
            Editlayout.ViewmoreEditskills.setVisibility(View.GONE);
        }*/


        //Load User Experience Data

        experienceAdapter =  new ExperienceAdapter(preferenceManager.getMapArray(Constants.KEY_EXPERIENCES));
        DividerItemDecoration itemDecor = new DividerItemDecoration(Editlayout.getRoot().getContext(), DividerItemDecoration.VERTICAL);
        Editlayout.ExperienceprofileRCV.addItemDecoration(itemDecor);
        Editlayout.ExperienceprofileRCV.setAdapter(experienceAdapter);
        experienceAdapter.activateButtons(true);
        Editlayout.ExperienceprofileRCV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Editlayout.ExperienceprofileRCV.setHasFixedSize(false);


        ArrayList<HashMap> experience_map = new ArrayList<>();
        experience_map = preferenceManager.getMapArray(Constants.KEY_EXPERIENCES);
        if(experience_map.size() > 0){
            Editlayout.Noexperiencelabel.setVisibility(View.GONE);

        }else {
            Editlayout.Noexperiencelabel.setVisibility(View.VISIBLE);
        }

        if(preferenceManager.getMapArray(Constants.KEY_EXPERIENCES).size() >= 6){
            Editlayout.ViewmoreExperiences.setVisibility(View.VISIBLE);
            ArrayList<HashMap> ExperiencesArray_new = new ArrayList<HashMap>(preferenceManager.getMapArray(Constants.KEY_EXPERIENCES).subList(0,5));
            experienceAdapter = new ExperienceAdapter(ExperiencesArray_new);
            Editlayout.ExperienceprofileRCV.setAdapter(experienceAdapter);
            experienceAdapter.notifyDataSetChanged();
        }else{
            Editlayout.ViewmoreExperiences.setVisibility(View.GONE);
        }

        //Load User Education Data

        educationAdapter =  new EducationAdapter(preferenceManager.getMapArray(Constants.KEY_EDUCATION));
        DividerItemDecoration itemDecor_edu = new DividerItemDecoration(Editlayout.getRoot().getContext(), DividerItemDecoration.VERTICAL);
        Editlayout.educationrecyclerView.addItemDecoration(itemDecor_edu);
        Editlayout.educationrecyclerView.setNestedScrollingEnabled(false);
        Editlayout.educationrecyclerView.setAdapter(educationAdapter);
        educationAdapter.activateButtons(true);
        Editlayout.educationrecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Editlayout.educationrecyclerView.setHasFixedSize(false);

        ArrayList<HashMap> education_map = new ArrayList<>();
        education_map = preferenceManager.getMapArray(Constants.KEY_EDUCATION);
        if(education_map.size() > 0){
            Editlayout.Noeducationlabel.setVisibility(View.GONE);

        }else {
            Editlayout.Noeducationlabel.setVisibility(View.VISIBLE);
        }

        if(preferenceManager.getMapArray(Constants.KEY_EDUCATION).size() >= 6){
            Editlayout.ViewmoreEducation.setVisibility(View.VISIBLE);
            ArrayList<HashMap> EducationArray_new = new ArrayList<HashMap>(preferenceManager.getMapArray(Constants.KEY_EDUCATION).subList(0,5));
            educationAdapter = new EducationAdapter(EducationArray_new);
            Editlayout.educationrecyclerView.setAdapter(educationAdapter);
            educationAdapter.notifyDataSetChanged();
        }else{
            Editlayout.ViewmoreEducation.setVisibility(View.GONE);
        }

        //load User languages Data
        selectedLanguagesAdapter =  new Selected_skillsAdapter(preferenceManager.getStringArray(Constants.KEY_LANGUAGES));
        Editlayout.languagesrecyclerView.setAdapter(selectedLanguagesAdapter);
        Editlayout.languagesrecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Editlayout.languagesrecyclerView.setHasFixedSize(false);

        ArrayList<String> Languagelist = new ArrayList<>();
        Languagelist = preferenceManager.getStringArray(Constants.KEY_LANGUAGES);
        if(Languagelist.size() > 0){
            Editlayout.NoLanguageslabel.setVisibility(View.GONE);

        }else {
            Editlayout.NoLanguageslabel.setVisibility(View.VISIBLE);
        }

        if(preferenceManager.getStringArray(Constants.KEY_LANGUAGES).size() >= 6){
            Editlayout.ViewmoreEditLanguages.setVisibility(View.VISIBLE);
            ArrayList<String> LanguagesArray_new = new ArrayList<String>(preferenceManager.getStringArray(Constants.KEY_LANGUAGES).subList(0,5));
            selectedLanguagesAdapter = new Selected_skillsAdapter(LanguagesArray_new);
            Editlayout.languagesrecyclerView.setAdapter(selectedLanguagesAdapter);
            selectedLanguagesAdapter.notifyDataSetChanged();
        }else{
            Editlayout.ViewmoreEditLanguages.setVisibility(View.GONE);
        }

        populatesalarytype();
        Log.d("Crypto", String.valueOf(Constants.Skills_Array.size()));
        Checkinput_completion();
        Update_progressbar();
        CheckIfAllTextchanged_Fields();

        if(!Constants.BACKFROMSKILLS_FLAG){
            showCustomDialog();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
       // Toast.makeText(getApplicationContext(),"Pause",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSkillClick(int position) {

    }

    @Override
    public void onBackPressed() {
        System.out.println("City " +preferenceManager.getString(Constants.KEY_CITY));
        if(preferenceManager.getInt(Constants.KEY_PROFILECOMPLETION_VALUE) == 100 && Editlayout.ccpMobile.isValidFullNumber()
        && preferenceManager.getString(Constants.KEY_FIRSTNAME) != "" && preferenceManager.getString(Constants.KEY_LASTNAME) != ""
                && preferenceManager.getString(Constants.KEY_USERNAME) != ""  && preferenceManager.getString(Constants.KEY_EMAILADDRESS) != ""
                && preferenceManager.getString(Constants.KEY_MOBILE_NUMBER) != "" && preferenceManager.getString(Constants.KEY_CITY) != ""
                && preferenceManager.getString(Constants.KEY_SALARY_TYPE) != ""  && preferenceManager.getString(Constants.KEY_SALARY_AMOUNT) != ""
                && preferenceManager.getString(Constants.KEY_PROFESSIONAL_HEADLINE) != "" && preferenceManager.getString(Constants.KEY_USER_SUMMARY) != ""
                && preferenceManager.getString(Constants.KEY_PROFILE_IMAGE) != null && preferenceManager.getString(Constants.KEY_EMAILADDRESS) != ""
                && preferenceManager.getStringArray(Constants.KEY_SKILLS).size() > 0 && preferenceManager.getStringArray(Constants.KEY_SKILLS) != null){
            Constants.Skills_Array.clear();
            Constants.FROMEDITPROFILE_FLAG =true;
            finish();

        }else{
            showToast("Please your profile should be fully completed",1);
        }
    }
}