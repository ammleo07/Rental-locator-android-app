package Util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.example.projectlocator.R;
import com.example.projectlocator.ViewNotificationActivity;

import Model.Transaction;
import Model.User;

/**
 * Created by alber on 26/05/2019.
 */

public class NotificationHelper {

    public void displayNotification(Context context, String title, String content, Transaction transaction)
    {


        NotificationCompat.Builder builder = new  NotificationCompat.Builder(context,"")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_menu_send)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 3000, 3000)
                .setContentText(content);

        Intent notificationIntent = new Intent(context, ViewNotificationActivity.class);
        notificationIntent.putExtra("content", content);
        notificationIntent.putExtra("transaction", transaction);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManagerCompat managerCompat= NotificationManagerCompat.from(context);
        managerCompat.notify(1,builder.build());

    }
}
