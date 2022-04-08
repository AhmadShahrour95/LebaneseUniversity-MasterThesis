package com.appstechio.workyzo.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appstechio.workyzo.adapters.BottomNav_PagerAdapter;
import com.appstechio.workyzo.R;
import com.appstechio.workyzo.databinding.ActivityHomeBinding;
import com.appstechio.workyzo.interfaces.Display_Toasts;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Home_Activity extends BaseActivity implements Display_Toasts {
    private ActivityHomeBinding Homelayout;
    private PreferenceManager preferenceManager;
    private static final  int GET_LOCATION_TIMEOUT = 100000;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private FirebaseFirestore database;
    private  ArrayList<String> jobs = new ArrayList<>();
    static int count = 0;
    private   BadgeDrawable badgeDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Homelayout = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = Homelayout.getRoot();
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(view);
        Click_AddJobPost();
        Click_Notification();



        Logout_Click();
        BottomNavigationIcon_Click();
        getToken();
        //showCustomDialog();

        //MainView Pager
        Homelayout.MainViewPager.setUserInputEnabled(false);
        Homelayout.MainViewPager.setAdapter(new BottomNav_PagerAdapter(this));

        //toolbar
        setSupportActionBar(Homelayout.maintoolbar);

        //Bottom Navigation
        Homelayout.bottomNavigation.setBackground(null);
        Homelayout.bottomNavigation.getMenu().getItem(2).setEnabled(false);

        // as soon as the application opens the first
        // fragment should be shown to the user
        // in this case it is algorithm fragment
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        if(Constants.FROMPROPOSAL_FLAG || Constants.FROMEDITPROFILE_FLAG){
            Homelayout.MainViewPager.setCurrentItem(3);
            Constants.FROMPROPOSAL_FLAG=false;
            Constants.FROMEDITPROFILE_FLAG =false;
        }else {
            Constants.FROMPROPOSAL_FLAG=false;
            Constants.FROMEDITPROFILE_FLAG =false;
           //do nothing
        }

        JobNotificationlistener();
    }


   /* public  void Chat_Notification(){
        badgeDrawable =  Homelayout.bottomNavigation.getOrCreateBadge(Homelayout.bottomNavigation.getMenu().getItem(3).getItemId());

        database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_CHAT).whereEqualTo(Constants.KEY_RECEIVER_ID,preferenceManager.getString(Constants.KEY_USERID))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            //Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                       // int i =0;
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            switch (dc.getType()) {

                                case ADDED:
                                    Constants.notificationchat_count++;
                                    System.out.println(Constants.notificationchat_count);
                                    badgeDrawable.setVisible(false);
                                        if( Constants.notificationchat_count >= 2){
                                            int count = Constants.notificationchat_count - value.getDocumentChanges().size();
                                            if(count != 0){
                                                System.out.println("Count+ "+ count);
                                                badgeDrawable.setVisible(true);
                                                //badgeDrawable.setNumber(Constants.notificationchat_count );
                                            }
                                        }
                                    break;
                            }

                            }
                        }

                    });
                }*/




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GetCurrentLocation();

                //finish();
                //startActivity(getIntent());
            } else {
                showToast("Permission denied!", 1);
            }
        }
    }

    //GET CURRENT LOCATION
    @SuppressLint("MissingPermission")
    private void GetCurrentLocation() {
        LocationRequest locationRequest =  LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(Home_Activity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        preferenceManager = new PreferenceManager(getApplicationContext());
                        LocationServices.getFusedLocationProviderClient(Home_Activity.this)
                                .removeLocationUpdates(this);
                        if ( locationResult != null && locationResult.getLocations().size() > 0 ){
                            int LatestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(LatestLocationIndex).getLatitude();
                            double Longtitude = locationResult.getLocations().get(LatestLocationIndex).getLongitude();

                            // Compute the GeoHash for a lat/lng point
                            //double lat = 51.5074;
                            //double lng = 0.1278;
                            String hash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(latitude, Longtitude));

                            // Add the hash and the lat/lng to the document. We will use the hash
                            // for queries and the lat/lng for distance comparisons.
                            HashMap<String, Object> location = new HashMap<>();
                            location.put("geohash", hash);
                            location.put("latitude", latitude);
                            location.put("Longtitude", Longtitude);

                            database = FirebaseFirestore.getInstance();
                            DocumentReference UserRef = database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USERID));
                            UserRef.update(location)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // ...
                                            preferenceManager.putString("geohash",hash);
                                            preferenceManager.putDouble("latitude", Float.valueOf(String.valueOf(latitude)));
                                            preferenceManager.putDouble("Longtitude", Float.valueOf(String.valueOf(Longtitude)));

                                        }
                                    });



                            Geocoder geocoder = new Geocoder(Home_Activity.this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(latitude, Longtitude, 1);
                                setUpLocationdata(addresses);


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Location location_user = new Location("providerNA");
                            location_user.setLatitude(latitude);
                            location_user.setLongitude(Longtitude);
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
        if(city == null){
            preferenceManager.putString(Constants.KEY_ADDRESS,state+","+Countryname);
        }else if (state == null) {
            preferenceManager.putString(Constants.KEY_ADDRESS,city+","+Countryname);
        }else{
            preferenceManager.putString(Constants.KEY_ADDRESS,add);
        }

        preferenceManager.putString(Constants.KEY_COUNTRY_CODE,Country_Code);
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




    private void JobNotificationlistener (){

        database = FirebaseFirestore.getInstance();

        database.collection(Constants.KEY_COLLECTION_JOBS)
                .whereEqualTo(Constants.KEY_COMPLETED,false)
                .whereNotEqualTo(Constants.KEY_EMPLOYERID,preferenceManager.getString(Constants.KEY_USERID))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        List<String> cities = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.getId() != null) {
                                jobs.add(doc.getId());
                            }
                        }
                        Log.d(TAG, "Current jobs " + jobs.size());

                        if(jobs.size() > 0 ){
                            Homelayout.notificationBadgecounter.setVisibility(View.VISIBLE);
                            Homelayout.notificationCounter.setText(String.valueOf(jobs.size()));
                            jobs.clear();
                        }else{
                            Homelayout.notificationBadgecounter.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }




    private void Click_Notification(){
        Homelayout.Notificationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Homelayout.bottomNavigation.setSelectedItemId(R.id.home);
               Homelayout.MainViewPager.setCurrentItem(0);
               Homelayout.notificationBadgecounter.setVisibility(View.INVISIBLE);
               jobs.clear();

            }
        });
    }
    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updatetoken);
    }

    private void updatetoken(String token) {
        preferenceManager.putString(Constants.KEY_FCM_TOKEN,token);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USERID)
                );

        documentReference.update(Constants.KEY_FCM_TOKEN,token)
                .addOnSuccessListener(unused -> Log.d("Token","Token updated successfully"))
                .addOnFailureListener(e -> Log.d("Token","Unable to update token"));
    }


    //CLICK ON LOGOUT BUTTON
    private void Logout_Click(){
        Homelayout.Logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                DocumentReference documentReference =
                        database.collection(Constants.KEY_COLLECTION_USERS).document(
                                preferenceManager.getString(Constants.KEY_USERID)
                        );
                HashMap<String,Object> updates = new HashMap<>();
                updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
                documentReference.update(updates)
                        .addOnSuccessListener(unused -> {
                            preferenceManager.Clear();
                            FirebaseAuth.getInstance().signOut();
                            finish();
                        })
                        .addOnFailureListener(e -> showToast("Unable to sign out",0));
            }
        });
    }

    //CHANGE FRAGMENT WHEN CLICKING ON THE ICON WITH THE BOTTOM NAVIGATION
    private void BottomNavigationIcon_Click(){
        Homelayout.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // By using switch we can easily get
                // the selected fragment
                // by using there id.
                switch (item.getItemId()) {

                    case R.id.home:
                        Homelayout.maintoolbar.setTitle("Home");
                        Homelayout.MainViewPager.setCurrentItem(0);
                       // selectedFragment = new HomeFragment();
                        break;

                    case R.id.jobs:
                        Homelayout.maintoolbar.setTitle("My Agenda");
                        Homelayout.MainViewPager.setCurrentItem(1);
                        // selectedFragment = new HomeFragment();
                        break;

                    case R.id.inbox:
                        Homelayout.maintoolbar.setTitle("Chat");
                        Homelayout.MainViewPager.setCurrentItem(2);
                        //selectedFragment = new Chat();
                        break;
                    case R.id.profile:
                        Homelayout.maintoolbar.setTitle("Profile");
                        Homelayout.MainViewPager.setCurrentItem(3);
                        //selectedFragment = new ProfileFragment();

                        break;
                }
                // It will help to replace the
                // one fragment to other.
               // getSupportFragmentManager()
                       // .beginTransaction()
                       // .replace(R.id.fragment_container, selectedFragment)
                      //  .commit();
                return true;
            }
        });


       Homelayout.MainViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);


            }

           @Override
           public void onPageSelected(int position) {
               super.onPageSelected(position);
               showToast(String.valueOf(position),1);
               if (position == 0) {
                   Homelayout.bottomNavigation.setSelectedItemId(R.id.home);
               }else if (position == 1) {
                   Homelayout.bottomNavigation.setSelectedItemId(R.id.jobs);
               }else if (position == 2) {
                   Homelayout.bottomNavigation.setSelectedItemId(R.id.inbox);
               }else if (position == 3) {
                   Homelayout.bottomNavigation.setSelectedItemId(R.id.profile);
               }
           }


       });
    }


    private void Click_AddJobPost(){
        Homelayout.AddPostFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.step = 0;
                Constants.JobRequiredSkills_Array.clear();
                Constants.Mediafiles_Uploaded.clear();
                if(Constants.Files_Map != null){
                    Constants.Files_Map.clear();
                }
                Intent intent = new Intent(getApplicationContext(),Postjob_Activity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Chat_Notification();
    }
}