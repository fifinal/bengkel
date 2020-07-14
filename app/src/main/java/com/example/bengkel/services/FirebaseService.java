package com.example.bengkel.services;

import android.app.NotificationManager;
import android.app.Service;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.bengkel.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title=remoteMessage.getNotification().getTitle();

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,getString(R.string.default_notification_client_id));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("");
        builder.setContentText("");

        int time =(int) System.currentTimeMillis();
        NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(time,builder.build());
    }
}
