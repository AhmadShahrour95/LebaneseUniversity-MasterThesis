package com.appstechio.workyzo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.appstechio.workyzo.R;
import com.appstechio.workyzo.databinding.ActivityForgotpasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class ForgotPassword_Activity extends AppCompatActivity {
    private ActivityForgotpasswordBinding Forgotpasslayout;
    public  int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Forgotpasslayout = ActivityForgotpasswordBinding.inflate(getLayoutInflater());
        View view = Forgotpasslayout.getRoot();
        setContentView(view);
        BacktoLogin();
        SendResetemailClick();

    }


    private  void BacktoLogin(){
        Forgotpasslayout.ResetPassbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void  SendEmail_loading (Boolean isloading){
        if(isloading){
            Forgotpasslayout.ForgotpasswordFrame.setVisibility(View.INVISIBLE);
            Forgotpasslayout.progressBar.setVisibility(View.VISIBLE);
        }else{
            Forgotpasslayout.progressBar.setVisibility(View.INVISIBLE);
            Forgotpasslayout.ResetPasswordSentFrame.setVisibility(View.VISIBLE);
          // String email =  Forgotpasslayout.textView7.setPaintFlags(Paint.Bo);
            Forgotpasslayout.textView7.setText("A password reset email has been sent to " + Forgotpasslayout.inputresetemailtxt.getText().toString() +".Follow the instructions in the email sent to reset your password ");
        }
    }

    private  void SendResetemailClick(){
            Forgotpasslayout.Sendresetemailbtn.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String reset_email =Forgotpasslayout.inputresetemailtxt.getText().toString();
                    SendEmail_loading(true);
                    SendEmail_loading(false);

                    new CountDownTimer(10000,1000){

                        @Override
                        public void onTick(long l) {
                            count++;
                        }
                        public void onFinish(){
                            finish();

                        }
                    }.start();

                    if(!android.util.Patterns.EMAIL_ADDRESS.matcher(reset_email).matches() && !reset_email.trim().isEmpty()){
                        Forgotpasslayout.Inputresetemail.setError(getString(R.string.ValidEmail_Error));
                    }else if (reset_email.trim().isEmpty()){
                        Forgotpasslayout.Inputresetemail.setError(getString(R.string.RequiredField_Error));
                    }else{
                        SendEmail_loading(true);
                        //SEND A PASSWORD RESET EMAIL
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        FirebaseUser user = auth.getCurrentUser();

                        auth.sendPasswordResetEmail(reset_email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            SendEmail_loading(false);

                                            new CountDownTimer(5000,1000){

                                                @Override
                                                public void onTick(long l) {
                                                    count++;
                                                }
                                                public void onFinish(){
                                                    BacktoLogin();

                                                }
                                            }.start();
                                        }else {
                                            Forgotpasslayout.progressBar.setVisibility(View.INVISIBLE);
                                            Forgotpasslayout.Sendresetemailbtn.setVisibility(View.VISIBLE);
                                            Toast.makeText(getApplicationContext(), "Sending email failed please try again", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                         }
                    }
                  }));
             }
    }



