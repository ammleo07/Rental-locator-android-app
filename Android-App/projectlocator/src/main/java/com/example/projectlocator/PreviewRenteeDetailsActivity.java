package com.example.projectlocator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import Model.HouseOwnerForm;
import Model.RenteeForm;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PreviewRenteeDetailsActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    RenteeForm form;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preview_rentee_details);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        form = (RenteeForm) (getIntent().getSerializableExtra("User"));

        populateValues(form);
        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.save_rentee_details).setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    public void back(View view)
    {
        finish();
    }

    public void acceptTerms(final View view){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(PreviewRenteeDetailsActivity.this);
        View mView = layoutInflaterAndroid.inflate(R.layout.terms_and_condition_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(PreviewRenteeDetailsActivity.this);
        alertDialogBuilderUserInput.setView(mView);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        save(view);
                    }
                })

                .setNegativeButton("Decline",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
        alertDialogAndroid.getWindow().setLayout(600, 800);
    }

    public void save(View view)
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Button save = (Button) findViewById(R.id.save_rentee_details);
        save.setEnabled(false);
        Toast.makeText(getApplicationContext(), "Saving new user..." , Toast.LENGTH_LONG).show();
        form.getUser().setTokenId(FirebaseInstanceId.getInstance().getToken());
        saveUser(form);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


    }


    public void populateValues(RenteeForm form)
    {

        TextView username = (TextView) findViewById(R.id.preview_rentee_username);
        TextView password = (TextView) findViewById(R.id.preview_rentee_password);
        TextView name = (TextView) findViewById(R.id.preview_rentee_name);
        TextView contactNumber = (TextView) findViewById(R.id.preview_rentee_contact_number);
        TextView houseType = (TextView) findViewById(R.id.preview_rentee_house_type);
        TextView priceRange = (TextView) findViewById(R.id.preview_rentee_price_range);
        TextView renteeType = (TextView) findViewById(R.id.preview_rentee_type);
        TextView fundType = (TextView) findViewById(R.id.preview_rentee_fund_type);
        username.setText(form.getUser().getUsername());
        password.setText(form.getUser().getPassword());
        name.setText(form.getUser().getFirstName() + " " + form.getUser().getMiddleName() + " " + form.getUser().getLastName());
        contactNumber.setText(": " +form.getRentee().getContactNumber());
        houseType.setText(form.getRentee().getHouseType());
        priceRange.setText(": Php. " + form.getRentee().getMinPriceRange()  + " to Php. " + form.getRentee().getMaxPriceRange());
        renteeType.setText(form.getRentee().getRenteeType());
        fundType.setText(form.getRentee().getFundType());
    }

    public boolean saveUser(final RenteeForm rentee) {
        boolean result=false;
        RetrofitService mService;
        SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
        ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
        mService= ApiUtils.getSOService();
        mService.saveUser(rentee).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(response.isSuccessful()) {
                    //showProgress(false);
                    if(!response.body().equalsIgnoreCase("failed"))
                    {
                        Toast.makeText(getApplicationContext(), "User has been saved" , Toast.LENGTH_LONG).show();
                        //Intent intent = new Intent(getApplicationContext(),SuccessRegistrationActivity.class);
                        Intent intent = new Intent(getApplicationContext(),UploadDocumentRentee.class);
                        intent.putExtra("id", response.body());
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Error has been occured on the server" , Toast.LENGTH_LONG).show();

                    }
                    Log.d("Rentee Activity", "new Rentee");
                }else {
                    int statusCode  = response.code();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //           showErrorMessage();
                //showProgress(false);
                Log.d("RegistrationActivity", "error loading from API:" + t.getMessage());
                //Toast.makeText(getApplicationContext(), "Data:" + rentee.getUser().getUsername() + ":" + rentee.getUser().getUserType()+ "-" +rentee.getUser().getLastName() + ":" + rentee.getRentee().getContactNumber() , Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return result;
    }


    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
