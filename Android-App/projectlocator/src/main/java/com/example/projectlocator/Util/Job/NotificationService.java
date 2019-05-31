package com.example.projectlocator.Util.Job;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import Model.User;
import Util.NotificationHelper;

/**
 * Created by alber on 26/05/2019.
 */

public class NotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getNotification() != null) {
            Log.i("notif","sending notif with content:" + remoteMessage.getData().get("detail"));
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            //User user = remoteMessage.getData().
            new NotificationHelper().displayNotification(getApplicationContext(),title,body);
        }
    }
}
