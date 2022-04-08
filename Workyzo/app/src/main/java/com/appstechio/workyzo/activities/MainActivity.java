package com.appstechio.workyzo.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appstechio.workyzo.R;
import com.appstechio.workyzo.interfaces.Display_Toasts;
import com.appstechio.workyzo.databinding.ActivityMainBinding;
import com.appstechio.workyzo.security.DBHandler;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements Display_Toasts {
private ActivityMainBinding LoginLayout;
private FirebaseAuth mAuth;
private PreferenceManager preferenceManager;
    private DBHandler dbHandler;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginLayout = ActivityMainBinding.inflate(getLayoutInflater());
        View view = LoginLayout.getRoot();
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(view);
        LoginClick();
        SignupClick();
        ForgotpasswordClick();
        //showCustomDialog();
        mAuth = FirebaseAuth.getInstance();
        preferenceManager.Clear();

      /*  if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
             ActivityCompat.requestPermissions(Editprofile_Activity.this, new String[]
             {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }*/

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




    private boolean isValidLoginDetails() {
        if(LoginLayout.inputemailtxt.getText().toString().trim().isEmpty()){
            LoginLayout.Inputemail.setError(getString(R.string.RequiredField_Error));
            return false;
        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(LoginLayout.inputemailtxt.getText().toString()).matches()){
            LoginLayout.Inputemail.setError(getString(R.string.ValidEmail_Error));
            return false;
        }else if (LoginLayout.inputPasswordtxt.getText().toString().trim().isEmpty()){
            LoginLayout.inputPassword.setError(getString(R.string.RequiredField_Error));
            return false;
        }
        return true;
    }

    private void  Login_loading (Boolean isloading){
        if(isloading){
            LoginLayout.Loginbtn.setVisibility(View.INVISIBLE);
            LoginLayout.Inputemail.setVisibility(View.INVISIBLE);
            LoginLayout.inputPassword.setVisibility(View.INVISIBLE);
            LoginLayout.ForgotPasswordLink.setVisibility(View.INVISIBLE);
            LoginLayout.Signuplink.setVisibility(View.INVISIBLE);
            LoginLayout.accountquestionLabel.setVisibility(View.INVISIBLE);
            LoginLayout.LoginProgressBar.setVisibility(View.VISIBLE);
        }else{
            LoginLayout.Loginbtn.setVisibility(View.VISIBLE);
            LoginLayout.Inputemail.setVisibility(View.VISIBLE);
            LoginLayout.inputPassword.setVisibility(View.VISIBLE);
            LoginLayout.ForgotPasswordLink.setVisibility(View.VISIBLE);
            LoginLayout.Signuplink.setVisibility(View.VISIBLE);
            LoginLayout.accountquestionLabel.setVisibility(View.VISIBLE);
            LoginLayout.LoginProgressBar.setVisibility(View.INVISIBLE);

        }
    }

/*    private  void showLoginFailed_Toast(){

        // Retrieve the Layout Inflater and inflate the layout from xml
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.loginfailed_toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        // get the reference of TextView and ImageVIew from inflated layout
        TextView toastTextView = (TextView) layout.findViewById(R.id.toastTextView);
        // set the text in the TextView
        toastTextView.setText(getString(R.string.Login_Failed));
        // set the Image in the ImageView

        //toastImageView.setImageResource(R.drawable.ic_check_circle);
        // create a new Toast using context
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG); // set the duration for the Toast
        toast.setView(layout); // set the inflated layout
        toast.setGravity(Gravity.BOTTOM |Gravity.CENTER_HORIZONTAL,0,0);
        toast.show(); // display the custom Toast

    }*/

    private void LoginClick(){
        LoginLayout.Loginbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                try {
                    //RSAEncryption ();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(isValidLoginDetails()){
                    SignIn();
                };

            }
        });
    }

    private void ForgotpasswordClick(){

        LoginLayout.ForgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent Forgotpassactivity = new Intent(getApplicationContext(),ForgotPassword_Activity.class);
                startActivity(Forgotpassactivity);
            }
        });
    }

    private void SignupClick(){
            LoginLayout.Signuplink.setOnClickListener(new View.OnClickListener() {
                //
                @Override
                public void onClick(View view) {
                    Intent Signupactivity = new Intent(getApplicationContext(), SignUp_Activity.class);
                    startActivity(Signupactivity);
                }
            });
        }

    private  void SignIn (){
            Login_loading(true);
            String login_email = LoginLayout.inputemailtxt.getText().toString();
            String login_password = LoginLayout.inputPasswordtxt.getText().toString();

            mAuth.signInWithEmailAndPassword(login_email, login_password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                //CHECK IF EMAIL VERIFIED

                                    if (user.isEmailVerified()){
                                        //user.reload();
                                        FirebaseFirestore database = FirebaseFirestore.getInstance();
                                        database.collection(Constants.KEY_COLLECTION_USERS)
                                                .whereEqualTo(Constants.KEY_EMAILADDRESS,login_email)
                                                .get()
                                                .addOnCompleteListener(task1 -> {
                                                    if(task1.isSuccessful() && task1.getResult()!=null &&
                                                    task1.getResult().getDocuments().size()>0){
                                                        DocumentSnapshot documentSnapshot = task1.getResult().getDocuments().get(0);
                                                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                                                        preferenceManager.putBoolean(Constants.KEY_VISIBLE_ASFREELANCER, documentSnapshot.getBoolean(Constants.KEY_VISIBLE_ASFREELANCER));
                                                        preferenceManager.putString(Constants.KEY_USERID,documentSnapshot.getId());
                                                        preferenceManager.putString(Constants.KEY_USERNAME,documentSnapshot.getString(Constants.KEY_USERNAME));
                                                        preferenceManager.putString(Constants.KEY_EMAILADDRESS,documentSnapshot.getString(Constants.KEY_EMAILADDRESS));
                                                        preferenceManager.putString(Constants.KEY_FIRSTNAME,documentSnapshot.getString(Constants.KEY_FIRSTNAME));
                                                        preferenceManager.putString(Constants.KEY_LASTNAME,documentSnapshot.getString(Constants.KEY_LASTNAME));
                                                        preferenceManager.putString(Constants.KEY_MOBILE_NUMBER,documentSnapshot.getString(Constants.KEY_MOBILE_NUMBER));
                                                       // preferenceManager.putString(Constants.KEY_GENDER,documentSnapshot.getString(Constants.KEY_GENDER));
                                                        preferenceManager.putString("MyKey",documentSnapshot.getString("Key"));

                                                        int i = 1;

                                                        if(preferenceManager.getString(Constants.KEY_FIRSTNAME) != null && !preferenceManager.getString(Constants.KEY_FIRSTNAME).isEmpty()){
                                                            preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE,i++);

                                                        }

                                                        if(preferenceManager.getString(Constants.KEY_LASTNAME) != null && !preferenceManager.getString(Constants.KEY_LASTNAME).isEmpty()){
                                                            preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE,i++);

                                                        }

                                                        if(preferenceManager.getString(Constants.KEY_USERNAME) != null && !preferenceManager.getString(Constants.KEY_USERNAME).isEmpty()){
                                                            preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE,i++);

                                                        }
                                                        if(preferenceManager.getString(Constants.KEY_EMAILADDRESS) != null && !preferenceManager.getString(Constants.KEY_EMAILADDRESS).isEmpty()){
                                                            preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE,i++);

                                                        }


                                                        if(preferenceManager.getString(Constants.KEY_MOBILE_NUMBER) != null && !preferenceManager.getString(Constants.KEY_MOBILE_NUMBER).isEmpty()){
                                                            preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE,i++);

                                                        }
                                                        preferenceManager.putString(Constants.KEY_PROFESSIONAL_HEADLINE,documentSnapshot.getString(Constants.KEY_PROFESSIONAL_HEADLINE));
                                                        if(preferenceManager.getString(Constants.KEY_PROFESSIONAL_HEADLINE) != null && !preferenceManager.getString(Constants.KEY_PROFESSIONAL_HEADLINE).isEmpty()){
                                                            preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE,i++);


                                                        }
                                                        preferenceManager.putString(Constants.KEY_USER_SUMMARY,documentSnapshot.getString(Constants.KEY_USER_SUMMARY));
                                                        if(preferenceManager.getString(Constants.KEY_USER_SUMMARY) != null && !preferenceManager.getString(Constants.KEY_USER_SUMMARY).isEmpty()){
                                                            preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE,i++);

                                                        }

                                                        Map<String, Object> Address_map = (Map<String, Object>) documentSnapshot.get(Constants.KEY_ADDRESS);
                                                        if(Address_map != null){
                                                            for (Map.Entry<String, Object> dataEntry : Address_map.entrySet()) {
                                                                if (dataEntry.getKey().equals(Constants.KEY_COUNTRY)) {
                                                                    preferenceManager.putString(Constants.KEY_COUNTRY,dataEntry.getValue().toString());
                                                                }else{
                                                                    preferenceManager.putString(Constants.KEY_CITY,dataEntry.getValue().toString());
                                                                }
                                                            }
                                                        }
                                                        if(preferenceManager.getString(Constants.KEY_COUNTRY) != null && !preferenceManager.getString(Constants.KEY_COUNTRY).isEmpty()){
                                                            preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE,i++);

                                                        }
                                                        if(preferenceManager.getString(Constants.KEY_CITY) != null && !preferenceManager.getString(Constants.KEY_CITY).isEmpty()){
                                                            preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE,i++);

                                                        }
                                                        if(preferenceManager.getString(Constants.KEY_COUNTRY) != null ){
                                                            if(preferenceManager.getString(Constants.KEY_COUNTRY).equals("United States")){
                                                                String country_flag = "flag_united_states_of_america";
                                                                int resID = getApplicationContext().getResources().getIdentifier(country_flag , "drawable", getApplicationContext().getPackageName());
                                                                preferenceManager.putInt(Constants.KEY_COUNTRY_FLAG,resID);
                                                            }else{
                                                                String country_flag = "flag_"+preferenceManager.getString(Constants.KEY_COUNTRY).toLowerCase().replace(" ","_");
                                                                int resID = getApplicationContext().getResources().getIdentifier(country_flag , "drawable", getApplicationContext().getPackageName());
                                                                preferenceManager.putInt(Constants.KEY_COUNTRY_FLAG,resID);
                                                            }
                                                        }


                                                        Map<String, Object> Salary_map = (Map<String, Object>) documentSnapshot.get(Constants.KEY_SALARY);
                                                        if(Salary_map != null){
                                                            for (Map.Entry<String, Object> dataEntry : Salary_map.entrySet()) {
                                                                if (dataEntry.getKey().equals("Type")) {
                                                                    preferenceManager.putString(Constants.KEY_SALARY_TYPE,dataEntry.getValue().toString());
                                                                }else{
                                                                    preferenceManager.putString(Constants.KEY_SALARY_AMOUNT,dataEntry.getValue().toString());
                                                                }
                                                            }
                                                        }else{
                                                            preferenceManager.putStringArray(Constants.KEY_SALARY,new ArrayList<>());
                                                        }

                                                        if(preferenceManager.getString(Constants.KEY_SALARY_TYPE) != null && !preferenceManager.getString(Constants.KEY_SALARY_TYPE).isEmpty()){
                                                            preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE,i++);
                                                        }
                                                        if(preferenceManager.getString(Constants.KEY_SALARY_AMOUNT) != null && !preferenceManager.getString(Constants.KEY_SALARY_AMOUNT).isEmpty()){
                                                            preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE,i++);
                                                        }

                                                        ArrayList<String> topskills_array = new ArrayList<>();
                                                         topskills_array = (ArrayList<String>) documentSnapshot.get(Constants.KEY_SKILLS);
                                                        if(topskills_array != null || preferenceManager.getStringArray(Constants.KEY_SKILLS) != null){
                                                            preferenceManager.putStringArray(Constants.KEY_SKILLS,topskills_array);
                                                            preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE,i++);
                                                        }else{
                                                            preferenceManager.putStringArray(Constants.KEY_SKILLS,new ArrayList<>());


                                                        }

                                                        ArrayList<HashMap> Experience_array = new ArrayList<>();
                                                        Experience_array = (ArrayList<HashMap>) documentSnapshot.get(Constants.KEY_EXPERIENCES);
                                                        if(Experience_array != null){
                                                            preferenceManager.putMapArray(Constants.KEY_EXPERIENCES,Experience_array);

                                                        }else{
                                                            preferenceManager.putMapArray(Constants.KEY_EXPERIENCES,new ArrayList<>());


                                                        }

                                                        ArrayList<HashMap> Education_array = new ArrayList<>();
                                                        Education_array = (ArrayList<HashMap>) documentSnapshot.get(Constants.KEY_EDUCATION);
                                                        if(Education_array != null){
                                                            preferenceManager.putMapArray(Constants.KEY_EDUCATION,Education_array);

                                                        }else{
                                                            preferenceManager.putMapArray(Constants.KEY_EDUCATION,new ArrayList<>());


                                                        }

                                                        ArrayList<HashMap> Review_array = new ArrayList<>();
                                                        Review_array = (ArrayList<HashMap>) documentSnapshot.get(Constants.KEY_REVIEW);
                                                        if(Review_array != null){
                                                            preferenceManager.putMapArray(Constants.KEY_REVIEW,Review_array);
                                                        }else{
                                                            preferenceManager.putMapArray(Constants.KEY_REVIEW,new ArrayList<>());

                                                        }

                                                        ArrayList<String> Languages_array = new ArrayList<>();
                                                         Languages_array = (ArrayList<String>) documentSnapshot.get(Constants.KEY_LANGUAGES);
                                                        if(Languages_array != null){
                                                            preferenceManager.putStringArray(Constants.KEY_LANGUAGES,Languages_array);
                                                            //Log.d("skills", String.valueOf(preferenceManager.getStringArray(Constants.KEY_SKILLS)));
                                                        }else{
                                                            preferenceManager.putStringArray(Constants.KEY_LANGUAGES,new ArrayList<>());


                                                        }

                                                        if(documentSnapshot.getString(Constants.KEY_PROFILE_IMAGE) == null){
                                                            //do nothing
                                                        }else {
                                                            preferenceManager.putString(Constants.KEY_PROFILE_IMAGE,documentSnapshot.getString(Constants.KEY_PROFILE_IMAGE));
                                                            if(preferenceManager.getString(Constants.KEY_PROFILE_IMAGE) != null || !preferenceManager.getString(Constants.KEY_PROFILE_IMAGE).isEmpty()){
                                                                preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE,i++);
                                                            }
                                                        }



                                                        //GET JOBS posted
                                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                        db.collection(Constants.KEY_COLLECTION_JOBS)
                                                                .whereNotEqualTo(Constants.KEY_JOB_FREELANCER_HIRED,preferenceManager.getString(Constants.KEY_USERNAME))
                                                                .get()
                                                                .addOnCompleteListener(task2 -> {
                                                                            if (task2.isSuccessful() && task2.getResult() != null &&
                                                                                    task2.getResult().getDocuments().size() > 0) {
                                                                                ArrayList<String> Postedjobs_array = new ArrayList<>();
                                                                                for (int x = 0 ;x< task2.getResult().getDocuments().size();x++){
                                                                                    DocumentSnapshot documentSnapshot1 = task2.getResult().getDocuments().get(x);
                                                                                    //Check if job posted by the user signed in
                                                                                    if(documentSnapshot1.getId().contains(preferenceManager.getString(Constants.KEY_USERID))){
                                                                                        Postedjobs_array.add(documentSnapshot1.getId());
                                                                                    }
                                                                                }
                                                                                preferenceManager.putStringArray(Constants.KEY_POSTED_JOBS,Postedjobs_array);
                                                                               // Log.d("JOBS", String.valueOf(preferenceManager.getStringArray(Constants.KEY_POSTED_JOBS).size()));

                                                                            }
                                                                        });

                                                        //GET JOBS Applied
                                                        db.collection(Constants.KEY_COLLECTION_JOBS)
                                                                .whereNotEqualTo(Constants.KEY_EMPLOYERID,preferenceManager.getString(Constants.KEY_USERID))
                                                                .get()
                                                                .addOnCompleteListener(task4 -> {
                                                                    if (task4.isSuccessful()) {
                                                                        int count = 0;

                                                                        for (DocumentSnapshot document : task4.getResult()) {
                                                                            if(!document.getId().contains(preferenceManager.getString(Constants.KEY_USERID))){
                                                                                ArrayList<String> arrayList = new ArrayList<>();
                                                                                if(document.getData().get(Constants.KEY_JOB_PROPOSAL) != null){
                                                                                    arrayList.add(document.getData().get(Constants.KEY_JOB_PROPOSAL).toString());

                                                                                    if(arrayList.get(0).contains(preferenceManager.getString(Constants.KEY_USERNAME))){
                                                                                        count = count + 1;
                                                                                        arrayList.clear();
                                                                                    }
                                                                                }

                                                                            }
                                                                        }
                                                                        preferenceManager.putInt(Constants.KEY_APPLIED_JOBS,count);
                                                                    } else {
                                                                        preferenceManager.putInt(Constants.KEY_APPLIED_JOBS,0);
                                                                    }

                                                                });


                                                        //GET HIRED JOBS (ACCEPTED)
                                                        db.collection(Constants.KEY_COLLECTION_JOBS)
                                                                .whereEqualTo(Constants.KEY_JOB_FREELANCER_HIRED,preferenceManager.getString(Constants.KEY_USERNAME))
                                                                .get()
                                                                .addOnCompleteListener(task3 -> {
                                                                    if (task3.isSuccessful() && task3.getResult() != null &&
                                                                            task3.getResult().getDocuments().size() > 0) {

                                                                        preferenceManager.putInt(Constants.KEY_HIRED_JOBS,task3.getResult().getDocuments().size());
                                                                       // Log.d("JOBS", String.valueOf(preferenceManager.getInt(Constants.KEY_HIRED_JOBS)));

                                                                    }else{
                                                                        preferenceManager.putInt(Constants.KEY_HIRED_JOBS,0);
                                                                    }
                                                                });

                                                        //GET COMPLETED JOBS
                                                        db.collection(Constants.KEY_COLLECTION_JOBS)
                                                                .whereEqualTo(Constants.KEY_JOB_FREELANCER_HIRED,preferenceManager.getString(Constants.KEY_USERNAME))
                                                                .get()
                                                                .addOnCompleteListener(task2 -> {
                                                                    if (task2.isSuccessful() && task2.getResult() != null &&
                                                                            task2.getResult().getDocuments().size() > 0) {
                                                                        ArrayList<String> Completedjobs_array = new ArrayList<>();
                                                                        for (int x = 0 ;x< task2.getResult().getDocuments().size();x++){
                                                                            DocumentSnapshot documentSnapshot1 = task2.getResult().getDocuments().get(x);
                                                                            //Check if job is completed
                                                                            if(documentSnapshot1.getBoolean(Constants.KEY_COMPLETED)){
                                                                                Completedjobs_array.add(documentSnapshot1.getId());
                                                                            }
                                                                        }
                                                                        preferenceManager.putStringArray(Constants.KEY_COMPLETED_JOBS,Completedjobs_array);
                                                                        // Log.d("JOBS", String.valueOf(preferenceManager.getStringArray(Constants.KEY_POSTED_JOBS).size()));

                                                                    }
                                                                });


                                                        int value = preferenceManager.getInt(Constants.KEY_PROFILECOMPLETION_VALUE) * 7;
                                                        Login_loading(false);

                                                        if(value < 91 ){
                                                            Intent HomeAct = new Intent(getApplicationContext(),Home_Activity.class);
                                                            HomeAct.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                            startActivity(HomeAct);

                                                            Intent EditprofileAct = new Intent(getApplicationContext(),Editprofile_Activity.class);
                                                            startActivity(EditprofileAct);
                                                        }else {
                                                            Intent HomeAct = new Intent(getApplicationContext(),Home_Activity.class);
                                                            //HomeAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(HomeAct);
                                                        }

                                                    }
                                                });


                                    }else {
                                        SendEmailVerification();
                                        Login_loading(false);
                                        showToast("Your account is not verified. Please verify the account",1);
                                    }
                                // If sign in fails, display a message to the user.
                            }else{

                                Login_loading(false);
                                showToast("Login failed",0);
                            }
                            }

                        });
                    }

    private void SendEmailVerification() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user_email = mAuth.getCurrentUser();
        if (user_email != null) {
            user_email.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            } else {
                                showToast("Sending email verification failed. Please try to resend", 1);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@androidx.annotation.NonNull Exception e) {
                            showToast(e.getMessage(),1);
                        }
                    });

        }else{
            showToast("Sending email verification failed. Please try to resend", 1);
        }
    }



    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check for Internet Connection
        if (isConnected()) {
          // showToast("Internet Connected",1);
        } else {
            showToast("No Internet Connection",1);
        }

        preferenceManager.Clear();
    }

}





