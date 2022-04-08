package com.appstechio.workyzo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appstechio.workyzo.adapters.FreelancerUsersAdapter;
import com.appstechio.workyzo.interfaces.Display_Toasts;
import com.appstechio.workyzo.R;
import com.appstechio.workyzo.databinding.ActivitySignUpBinding;
import com.appstechio.workyzo.models.User;
import com.appstechio.workyzo.security.DBHandler;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUp_Activity extends AppCompatActivity implements Display_Toasts {

    private  ActivitySignUpBinding Signuplayout;
    private PreferenceManager preferenceManager;
    private DBHandler dbHandler;

    ArrayAdapter<String> adapteritems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Signuplayout = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = Signuplayout.getRoot();
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(view);
        backtoLogin();
       // populate_Gender();
        SignUp_Click();
        LoginNow_Click();
        ResendLink_Click();
        // creating a new dbhandler class
        // and passing our context to it.
        dbHandler = new DBHandler(SignUp_Activity.this);

        Signuplayout.inputemailtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(Signuplayout.inputemailtxt.getText().toString()).matches())
                {
                    Signuplayout.inputSignupEmail.setError(getString(R.string.ValidEmail_Error));
                    //valid = false;
                } else {
                    Signuplayout.inputSignupEmail.setErrorEnabled(false);
                    //valid = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
    }

    private  void backtoLogin(){
            Signuplayout.Signupbackbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        Signuplayout.Signupbackbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                finish();
            }
        });
        }


    private void  Signup_loading (Boolean isloading){
            if(isloading){
                Signuplayout.Signupbtn.setVisibility(View.INVISIBLE);
                Signuplayout.SignupProgressBar.setVisibility(View.VISIBLE);
            }else{
                Signuplayout.Signupbtn.setVisibility(View.VISIBLE);
                Signuplayout.SignupProgressBar.setVisibility(View.INVISIBLE);
            }
        }

    private  void showSignup_Toast(){

            // Retrieve the Layout Inflater and inflate the layout from xml
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.signupsuccess_toast,
                    (ViewGroup) findViewById(R.id.toast_layout_root));
            // get the reference of TextView and ImageVIew from inflated layout
            TextView toastTextView = (TextView) layout.findViewById(R.id.toastTextView);
            ImageView toastImageView = (ImageView) layout.findViewById(R.id.toastImageView);
            // set the text in the TextView
            toastTextView.setText(getString(R.string.Signup_Success));
            // set the Image in the ImageView
            toastImageView.setImageResource(R.drawable.ic_check_circle);
            // create a new Toast using context
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG); // set the duration for the Toast
            toast.setView(layout); // set the inflated layout
            toast.setGravity(Gravity.CENTER |Gravity.CENTER_HORIZONTAL,0,0);
            toast.show(); // display the custom Toast

        }


    public  int counter = 0 ;
    private  void  showVerification_Frame(){

             Signuplayout.verificationLabel.setText(getString(R.string.Verify_Email1) + " "+
                    Signuplayout.inputemailtxt.getText().toString() + getString(R.string.Veridy_Email2));
             Signuplayout.SignUpFrame.setVisibility(View.GONE);
             Signuplayout.EmailVerificationFrame.setVisibility(View.VISIBLE);

         }


    private void SignUp_Click() {
        Signuplayout.Signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int valid = 0;
                if (Signuplayout.inputFirstNametxt.getText().toString().isEmpty()) {
                    Signuplayout.inputFirstName.setError(getString(R.string.RequiredField_Error));
                } else {
                    Signuplayout.inputFirstName.setErrorEnabled(false);
                    valid = valid + 1;
                }
                if (Signuplayout.inputLastNametxt.getText().toString().isEmpty()) {
                    Signuplayout.inputLastName.setError(getString(R.string.RequiredField_Error));
                } else {
                    Signuplayout.inputLastName.setErrorEnabled(false);
                    valid = valid + 1;
                }
                if (Signuplayout.inputUserNametxt.getText().toString().isEmpty()) {
                    Signuplayout.inputUserName.setError(getString(R.string.RequiredField_Error));
                } else {
                    Signuplayout.inputUserName.setErrorEnabled(false);
                    valid = valid + 1;
                }
                if (Signuplayout.inputemailtxt.getText().toString().isEmpty()) {
                    Signuplayout.inputSignupEmail.setError(getString(R.string.RequiredField_Error));
                } else {
                    Signuplayout.inputSignupEmail.setErrorEnabled(false);
                    valid = valid + 1;
                }
                if (Signuplayout.inputSignupPasswordtxt.getText().toString().isEmpty()) {
                    Signuplayout.inputSignupPassword.setError(getString(R.string.RequiredField_Error));
                } else {
                    Signuplayout.inputSignupEmail.setErrorEnabled(false);
                    valid = valid + 1;
                }
                if (Signuplayout.inputConfirmPasswordtxt.getText().toString().isEmpty()) {
                    Signuplayout.inputConfirmPassword.setError(getString(R.string.RequiredField_Error));
                } else {
                    Signuplayout.inputConfirmPassword.setErrorEnabled(false);
                    valid = valid + 1;
                }
                if (Signuplayout.inputSignupPasswordtxt.getText().toString().length() < 6) {
                    Signuplayout.inputSignupPassword.setError("Minimum password length 6 characters");
                } else {
                    Signuplayout.inputSignupPassword.setErrorEnabled(false);
                    valid = valid + 1;
                }
                if (!Signuplayout.inputSignupPasswordtxt.getText().toString().equals
                        (Signuplayout.inputConfirmPasswordtxt.getText().toString())) {
                    Signuplayout.inputConfirmPassword.setError(getString(R.string.PasswordConfirm_Error));
                } else {
                    Signuplayout.inputConfirmPassword.setErrorEnabled(false);
                    valid = valid + 1;
                }

                if (valid == 8){
                    SignUp();
                }

            }


        });
    }



    //REGISTER A USER IN THE DATABSE
    private FirebaseAuth mAuth;

    private void SignUp() {
            Signup_loading(true);

        String email = Signuplayout.inputemailtxt.getText().toString();
        String password = Signuplayout.inputSignupPasswordtxt.getText().toString();
        String firstname = Signuplayout.inputFirstNametxt.getText().toString();
        String lastname = Signuplayout.inputLastNametxt.getText().toString();
        String username = Signuplayout.inputUserNametxt.getText().toString().toLowerCase();


                mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Signuplayout.inputSignupEmail.setErrorEnabled(false);
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser Firebase_user = mAuth.getCurrentUser();
                                    //ADD DATA TO THE DATABASE
                                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                                    HashMap<String, Object> user = new HashMap<>();
                                    //user.put(Constants.KEY_USERID,Firebase_user.getUid().toString());
                                    user.put(Constants.KEY_FIRSTNAME,firstname);
                                    user.put(Constants.KEY_LASTNAME,lastname);
                                    user.put(Constants.KEY_USERNAME,username);
                                    // user.put(Constants.KEY_GENDER,gender);
                                    user.put(Constants.KEY_EMAILADDRESS,email);
                                    user.put(Constants.KEY_VISIBLE_ASFREELANCER,false);

                                    System.out.println(user.get(Constants.KEY_USERNAME));
                                    //Check Username if exists
                                    database.collection(Constants.KEY_COLLECTION_USERS)
                                            .whereEqualTo(Constants.KEY_USERNAME,user.get(Constants.KEY_USERNAME))
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful() && task.getResult() != null) {
                                                       if(task.getResult().size() > 0 ){
                                                           for (QueryDocumentSnapshot document : task.getResult()) {
                                                               if(document.exists()){
                                                                   Firebase_user.delete();
                                                                   Signuplayout.inputUserName.setError("Username already exists");
                                                                   Signup_loading(false);
                                                               }
                                                           }
                                                       }else{
                                                           Signuplayout.inputUserName.setErrorEnabled(false);
                                                           //GENERATE KEYS !

                                                           // generate a RSA key pair
                                                           KeyPairGenerator keygen = null;
                                                           try {
                                                               keygen = KeyPairGenerator.getInstance("RSA");
                                                           } catch (NoSuchAlgorithmException e1) {
                                                               e1.printStackTrace();
                                                           }
                                                           keygen.initialize(2048, new SecureRandom());
                                                           KeyPair keyPair = keygen.generateKeyPair();

                                                           //PRIVATE KEY
                                                           PrivateKey privateKey = keyPair.getPrivate();
                                                           byte[] PRIVATEKeyByte = privateKey.getEncoded();

                                                           String PRIVATEkeystring = Base64.encodeToString(PRIVATEKeyByte, Base64.NO_WRAP);
                                                           //preferenceManager.putString("PRIVATE",PRIVATEkeystring);

                                                           //PUBLIC KEY
                                                           PublicKey publicKey = keyPair.getPublic();

                                                           // get encoded form (byte array)
                                                           byte[] publicKeyByte = publicKey.getEncoded();
                                                           // Base64 encoded string
                                                           String publicKeyString = Base64.encodeToString(publicKeyByte, Base64.NO_WRAP);
                                                           //preferenceManager.putString("PUBKEY",publicKeyString);


                                                           user.put("Key",publicKeyString);
                                                           dbHandler.addNewKey(Firebase_user.getUid(),PRIVATEkeystring);


                                                           database.collection(Constants.KEY_COLLECTION_USERS).document(Firebase_user.getUid().toString())
                                                                   .set(user)
                                                                   .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                       @Override
                                                                       public void onSuccess(Void unused) {
                                                                           preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                                                                           //preferenceManager.putString(Constants.KEY_USERID,documentReference.getId());
                                                                           preferenceManager.putInt(Constants.KEY_PROFILECOMPLETION_VALUE,40);
                                                                           preferenceManager.putString(Constants.KEY_EMAILADDRESS,Signuplayout.inputemailtxt.getText().toString());
                                                                           //dbHandler.addNewKey(Firebase_user.getUid(),PRIVATEkeystring);
                                                                           showSignup_Toast();
                                                                           showVerification_Frame();
                                                                           SendEmailVerification();
                                                                           Signup_loading(false);
                                                                       }
                                                                   })
                                                                   .addOnFailureListener(exception -> {
                                                                       Signup_loading(false);
                                                                       showToast(exception.getMessage(),1);
                                                                   });

                                                       }



                                                    }

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(SignUp_Activity.this, "Error", Toast.LENGTH_LONG).show();
                                                }
                                            });

                                } else {
                                    Signup_loading(false);
                                    // If sign in fails, display a message to the user.
                                    Signuplayout.inputSignupEmail.setError("Email address already exists");
                                    //showToast("Failed to create an account. Email address already exists",1);
                                }
                            }
            });

    }




    private  void LoginNow_Click() {
        Signuplayout.LoginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LoginAct = new Intent(getApplicationContext(), MainActivity.class);
                LoginAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(LoginAct);
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

    private void ResendLink_Click(){
        Signuplayout.ResendLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendEmailVerification();
            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
    }
}