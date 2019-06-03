package com.example.projectlocator;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import Model.Transaction;

public class ViewNotificationActivity extends AppCompatActivity {

    Transaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_view_notification);
        String contentText =getIntent().getStringExtra("content");
        transaction = (Transaction) getIntent().getSerializableExtra("transaction");
        Toast.makeText(this, "content: " + contentText, Toast.LENGTH_LONG).show();
        TextView contentView = (TextView) findViewById(R.id.notification_content);
        Button callBtn = (Button) findViewById(R.id.call_btn);
        callBtn.setText("Call " + transaction.getRenteeContactNumber());
        contentView.setText(contentText);
    }

    public void makeCall(View v)
    {
        try {

            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + transaction.getRenteeContactNumber()));

            if (ActivityCompat.checkSelfPermission(ViewNotificationActivity.this,
                    android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);

        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Error:" + ex.getMessage() , Toast.LENGTH_LONG).show();

        }

    }

    public void gotoLogIn(View v)
    {
        try {
            Intent intent;
            SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
            if(sharedpreferences == null)
            {
                intent = new Intent(ViewNotificationActivity.this,LoginActivity.class);
            }
            else {
                if(sharedpreferences.getString("userType",null).equalsIgnoreCase("rentee")) {
                    intent = new Intent(ViewNotificationActivity.this, RenteePortalActivity.class);
                    intent.putExtra("username", sharedpreferences.getString("username",null));
                }
                else {
                    intent = new Intent(ViewNotificationActivity.this, HouseOwnerPortalActivity.class);
                    intent.putExtra("username", sharedpreferences.getString("username",null));
                }
            }

            startActivity(intent);

        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Error:" + ex.getMessage() , Toast.LENGTH_LONG).show();

        }

    }
}
