package com.appstechio.workyzo;
import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.widget.Toast;

import com.appstechio.workyzo.utilities.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.annotation.Nullable;

public class FetchAddressIntentService extends IntentService {

    private ResultReceiver resultReceiver;

    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent){
            if(intent != null){
                String errorMessage ="";
                resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
                Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
                if(location != null){
                    return;
                }
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null ;
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                }catch (Exception exception){
                    errorMessage = exception.getMessage();
                }
                if(addresses == null || addresses.isEmpty()){
                    deliverResulttoReceiver(Constants.FAILURE_RESULT,errorMessage);
                }else{
                    Address address = addresses.get(0);
                    ArrayList<String> addressFragments = new ArrayList<>();
                    if(address.getMaxAddressLineIndex() > 0){
                        for (int i=0; i < address.getMaxAddressLineIndex(); i++){
                            addressFragments.add(address.getAddressLine(i));
                            deliverResulttoReceiver(Constants.SUCCESS_RESULT,addressFragments+"");
                         }

                    }else{
                        try {
                            addressFragments.add(address.getAddressLine(0));
                            deliverResulttoReceiver(Constants.SUCCESS_RESULT,addressFragments+""        //change your code here
                            );
                        } catch (Exception ignored) {
                            Toast.makeText(this,ignored.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }

                }
            }


    }

    private void deliverResulttoReceiver (int resultCode, String addressMessage){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY,addressMessage);
        resultReceiver.send(resultCode,bundle);

    }
}
