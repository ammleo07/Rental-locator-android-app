package com.example.projectlocator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ViewNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_notification);
        String contentText =getIntent().getStringExtra("content");
        Toast.makeText(this, "content: " + contentText, Toast.LENGTH_LONG).show();
        TextView contentView = (TextView) findViewById(R.id.notification_content);
        contentView.setText(contentText);
    }
}
