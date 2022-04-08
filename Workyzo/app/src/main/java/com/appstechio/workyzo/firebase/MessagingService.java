package com.appstechio.workyzo.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.appstechio.workyzo.R;
import com.appstechio.workyzo.activities.Home_Activity;
import com.appstechio.workyzo.models.User;
import com.appstechio.workyzo.activities.Chatmessages_Activity;
import com.appstechio.workyzo.utilities.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCM","Token: " +token);


    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // Log.d("FCM","Message: " + remoteMessage.getNotification().getBody());
        if(remoteMessage.getData().get(Constants.NOTIFICATION_TYPE).equals("chat_message")){
            User user = new User();
            user.setUserId(remoteMessage.getData().get(Constants.KEY_USERID));
            user.setUsername(remoteMessage.getData().get(Constants.KEY_USERNAME));
            user.setToken(remoteMessage.getData().get(Constants.KEY_FCM_TOKEN));
            //user.setProfile_Image(remoteMessage.getData().get(Constants.KEY_PROFILE_IMAGE));

            int notificationId = new Random().nextInt();
            String channelId = "chat_message";

            Intent intent = new Intent(this, Chatmessages_Activity.class);
            intent.putExtra(Constants.KEY_USER, user);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
            builder.setSmallIcon(R.drawable.ic_notifications);
            builder.setContentTitle(user.getUsername());
            builder.setContentText(remoteMessage.getData().get(Constants.KEY_MESSAGE));
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(
                    remoteMessage.getData().get(Constants.KEY_MESSAGE)
            ));
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(true);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                CharSequence channelName = "Chat Message";
                String channelDescription = "This notification channel is used for chat messages notifications";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel notificationChannel = new NotificationChannel(channelId,channelName,importance);
                notificationChannel.setDescription(channelDescription);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(notificationId, builder.build());

        }else{

            int notificationId1 = new Random().nextInt();
            String channelId1 = "Job_message";

            Intent intent1 = new Intent(this, Home_Activity.class);
            //intent.putExtra(Constants.KEY_USER, user);
            PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 0, intent1, 0);
            NotificationCompat.Builder builder1 = new NotificationCompat.Builder(this, channelId1);
            builder1.setSmallIcon(R.drawable.ic_notifications);
            builder1.setContentTitle("Job");
            builder1.setContentText(remoteMessage.getData().get(Constants.KEY_MESSAGE));
            builder1.setStyle(new NotificationCompat.BigTextStyle().bigText(
                    remoteMessage.getData().get(Constants.KEY_MESSAGE)
            ));
            builder1.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            builder1.setContentIntent(pendingIntent1);
            builder1.setAutoCancel(true);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                CharSequence channelName1 = "Job hired";
                String channelDescription1 = "This notification channel is used for hired jobs notifications";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel notificationChannel = new NotificationChannel(channelId1,channelName1,importance);
                notificationChannel.setDescription(channelDescription1);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            NotificationManagerCompat notificationManagerCompat1 = NotificationManagerCompat.from(this);
            notificationManagerCompat1.notify(notificationId1, builder1.build());
        }





    }
}
