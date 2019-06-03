package com.example.projectlocator.Util.Job;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import Model.Transaction;
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

            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            //remoteMessage.getData().get("username");
            Log.i("notif","sending notif with username:" + remoteMessage.getData().get("username"));
            Transaction transaction = new Transaction();
            transaction.setOwnerTokenId(remoteMessage.getData().get("ownerTokenId"));
            transaction.setRenteeTokenId(remoteMessage.getData().get("renteeTokenId"));
            transaction.setRentee(remoteMessage.getData().get("rentee"));
            transaction.setHouseOwner(remoteMessage.getData().get("houseOwner"));
            //transaction.setStatus(remoteMessage.getData().get("rentee"));
            transaction.setRenteeContactNumber(remoteMessage.getData().get("renteeContactNumber"));

            new NotificationHelper().displayNotification(getApplicationContext(),title,body,transaction);
        }
    }

}
