package com.appstechio.workyzo.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class BaseActivity extends AppCompatActivity {

    private DocumentReference documentReference;
    private FirebaseFirestore database;
    private PreferenceManager preferenceManager;
    Runnable mToastRunnable;
    Handler mHandler ;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private static final  int GET_LOCATION_TIMEOUT = 100000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       preferenceManager = new PreferenceManager(getApplicationContext());
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USERID));

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BaseActivity.this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            GetCurrentLocation();

            //Runnable mToastRunnable;
            mHandler = new Handler();

            //Delay Timeout to Get Current Location
            mToastRunnable = new Runnable() {
                @Override
                public void run() {
                    //showToast("Location Updated",1);
                    mHandler.postDelayed(this,GET_LOCATION_TIMEOUT );
                   // preferenceManager = new PreferenceManager(getApplicationContext());
                    GetCurrentLocation();
                }
            };
            //start
            mToastRunnable.run();

            //stop
            //mHandler.removeCallbacks(mToastRunnable);*/
        }
    }

    //GET CURRENT LOCATION
    @SuppressLint("MissingPermission")
    private void GetCurrentLocation() {
        LocationRequest locationRequest =  LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(BaseActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        preferenceManager = new PreferenceManager(BaseActivity.this);
                        LocationServices.getFusedLocationProviderClient(BaseActivity.this)
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
                        /*    HashMap<String, Object> location = new HashMap<>();
                            location.put("geohash", hash);
                            location.put("Latitude", latitude);
                            location.put("Longtitude", Longtitude);*/


                            database = FirebaseFirestore.getInstance();
                            DocumentReference UserRef = database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USERID));
                            UserRef.update("geohash",hash,"Latitude",latitude,"Longtitude",Longtitude)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // ...
                                            preferenceManager.putString("geohash",hash);
                                            preferenceManager.putDouble("Latitude", Float.valueOf(String.valueOf(latitude)));
                                            preferenceManager.putDouble("Longtitude", Float.valueOf(String.valueOf(Longtitude)));

                                        }
                                    });



                            Geocoder geocoder = new Geocoder(BaseActivity.this, Locale.getDefault());
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
    @Override
    protected void onPause() {
        super.onPause();
        documentReference.update(Constants.KEY_STATUS,false);
        System.out.println("PAUSE");
        mHandler.removeCallbacks(mToastRunnable);
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check for Internet Connection
        if (isConnected()) {
            //Toast.makeText(this, "Internet Connected", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
       // preferenceManager = new PreferenceManager(getApplicationContext());
        documentReference.update(Constants.KEY_STATUS,true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //preferenceManager.Clear();
        System.out.println("STOP");
        mHandler.removeCallbacks(mToastRunnable);
       // Constants.notificationchat_count = 0;
    }
}
