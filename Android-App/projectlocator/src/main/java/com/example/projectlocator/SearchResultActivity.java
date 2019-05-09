package com.example.projectlocator;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Model.House;

public class SearchResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        List<House> houses = (List<House>) (getIntent().getSerializableExtra("Houses"));
        final TableLayout tableLayout = (TableLayout)findViewById(R.id.result_table);
        for(House house:houses) {
            TableRow tableRow = new TableRow(getApplicationContext());
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,0,10.0f);
            tableRow.setLayoutParams(layoutParams);
            tableRow.setBackgroundResource(R.drawable.border);

            // Add a TextView in the first column.

            TextView id = new TextView(getApplicationContext());
            id.setText(house.getId() + "");
            id.setTextColor(Color.BLACK);
            id.setBackgroundResource(R.drawable.border);
            id.setGravity(Gravity.CENTER);
            id.setVisibility(View.INVISIBLE);
            tableRow.addView(id, 0);

            TextView textView = new TextView(getApplicationContext());
            textView.setText((house.getHouseName() == null) ? "none" : house.getHouseName());
//            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            textView.setTextColor(Color.BLACK);
            textView.setBackgroundResource(R.drawable.border);
            textView.setGravity(Gravity.CENTER);
            tableRow.addView(textView, 1);

            TextView textView2 = new TextView(getApplicationContext());
            textView2.setText(house.getHouseType());
            textView2.setTextColor(Color.BLACK);
            textView2.setBackgroundResource(R.drawable.border);
            textView2.setGravity(Gravity.CENTER);
            tableRow.addView(textView2, 2);


            TextView textView3 = new TextView(getApplicationContext());
            textView3.setText(house.getMonthlyFee() + "");
            textView3.setTextColor(Color.BLACK);
            textView3.setBackgroundResource(R.drawable.border);
            textView3.setGravity(Gravity.CENTER);
            tableRow.addView(textView3, 3);

            tableRow.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    TableRow t = (TableRow) view;
                    TextView firstTextView = (TextView) t.getChildAt(0);
                    TextView secondTextView = (TextView) t.getChildAt(1);
                    Toast.makeText(getApplicationContext(), firstTextView.getText().toString() , Toast.LENGTH_LONG).show();
                    //String firstText = firstTextView.getText().toString();
                    //String secondText = secondTextView.getText().toString();

                    return false;
                }
            });

            tableRow.setOnHoverListener(new View.OnHoverListener() {
                @Override
                public boolean onHover(View view, MotionEvent motionEvent) {
                    TableRow t = (TableRow) view;
                    TextView firstTextView = (TextView) t.getChildAt(0);
                    TextView secondTextView = (TextView) t.getChildAt(1);
                    t.setBackgroundColor(Color.CYAN);
                    return false;
                }


            });

            tableLayout.addView(tableRow);


        }
    }
}
