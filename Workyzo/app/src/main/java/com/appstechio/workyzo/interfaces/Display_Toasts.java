package com.appstechio.workyzo.interfaces;

import android.content.Context;
import android.widget.Toast;

public interface Display_Toasts {

    Context getApplicationContext();

    default void showToast(String message, int duration){
        if (duration == 0) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }


}
