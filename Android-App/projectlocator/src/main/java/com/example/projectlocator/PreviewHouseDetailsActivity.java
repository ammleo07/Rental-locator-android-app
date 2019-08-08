package com.example.projectlocator;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import Model.HouseOwnerForm;
import Model.User;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitServiceHouseOwner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PreviewHouseDetailsActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;
    TextView houseInfo;
    HouseOwnerForm form;

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
    ProgressDialog progressDialog;
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

        setContentView(R.layout.activity_preview_house_details);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        houseInfo = (TextView) findViewById(R.id.preview_info_house);

        form = (HouseOwnerForm) (getIntent().getSerializableExtra("User"));
        //houseInfo.setText(data);
        // Set up the user interaction to manually show or hide the system UI.
        populateValues(form);
        toggle();
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.save_house_details).setOnTouchListener(mDelayHideTouchListener);

    }

    public void back(View view)
    {
        finish();
    }

    public void save(View view)
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Button save = (Button) findViewById(R.id.save_house_details);
        progressDialog= new ProgressDialog(PreviewHouseDetailsActivity.this);
        progressDialog.setMessage("Please wait, saving data is in progresss.. ");
        progressDialog.setTitle("Saving User");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        save.setEnabled(false);
        Toast.makeText(getApplicationContext(), "Saving new user..." , Toast.LENGTH_LONG).show();
        form.getUser().setTokenId(FirebaseInstanceId.getInstance().getToken());
        saveUser(form);
        progressDialog.dismiss();


    }

    public void populateValues(HouseOwnerForm houseOwnerForm)
    {
        String data = "*******************************************************"
                + "\n Username:" + form.getUser().getUsername() + "\t\t\t" + "Password:" + form.getUser().getPassword()
                + "\n Name: " + form.getUser().getFirstName() + " " + form.getUser().getMiddleName() + " " + form.getUser().getLastName()
                + "\n Contact Number: " + form.getHouseOwner().getContactNumber()
                + "\n\n\n******************House Details***********************"
                + "\n House Name:" + form.getHouse().getHouseName()
                + "\n House Type:" + form.getHouse().getHouseType()
                + "\n Montly Fee: " + form.getHouse().getMonthlyFee()
                + "\n Number of Slots:" + form.getHouse().getNumberOfSlots();
        TextView username = (TextView) findViewById(R.id.preview_house_owner_username);
        TextView password = (TextView) findViewById(R.id.preview_house_owner_password);
        TextView name = (TextView) findViewById(R.id.preview_house_owner_name);
        TextView contactNumber = (TextView) findViewById(R.id.preview_house_owner_contact_number);
        TextView houseName = (TextView) findViewById(R.id.preview_house_house_name);
        TextView houseType = (TextView) findViewById(R.id.preview_house_house_type);
        TextView boarderType = (TextView) findViewById(R.id.preview_house_boarder_type);
        TextView monthlyFees = (TextView) findViewById(R.id.preview_house_monthly_fee);
        TextView numberOfSlots = (TextView) findViewById(R.id.preview_house_number_of_slots);
        TextView houseNo = (TextView) findViewById(R.id.preview_address_house_number);
        TextView street = (TextView) findViewById(R.id.preview_house_address_street);
        TextView brgy = (TextView) findViewById(R.id.preview_address_brgy);
        TextView town = (TextView) findViewById(R.id.preview_address_town);
        TextView latitude = (TextView) findViewById(R.id.preview_address_latitude);
        TextView longitude = (TextView) findViewById(R.id.preview_address_longitude);



        username.setText(form.getUser().getUsername());
        password.setText(form.getUser().getPassword());
        name.setText(form.getUser().getFirstName() + " " + form.getUser().getMiddleName() + " " + form.getUser().getLastName());
        contactNumber.setText(": "+form.getHouseOwner().getContactNumber());
        houseName.setText(form.getHouse().getHouseName());
        houseType.setText(form.getHouse().getHouseType());
        boarderType.setText(form.getHouse().getBoarderType());
        monthlyFees.setText(form.getHouse().getMonthlyFee() + ":" + (form.getHouse().getIsNegotiable().equalsIgnoreCase("Y") ? "Negotiable" : "Non=t Negotiable"));
        numberOfSlots.setText(form.getHouse().getNumberOfSlots() + "");

        houseNo.setText(": "+form.getAddress().getHouseNo());
        street.setText(form.getAddress().getStreet());
        brgy.setText(form.getAddress().getBarangay());
        town.setText(form.getAddress().getTown());
        latitude.setText(form.getAddress().getLatitude() + "");
        longitude.setText(form.getAddress().getLongitude() + "");


    }

    public boolean saveUser(final HouseOwnerForm houseOwnerForm) {
        boolean result=false;
        RetrofitServiceHouseOwner mService;
        SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
        ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
        mService= ApiUtils.getHomeOwnerService();
        mService.saveUser(houseOwnerForm).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(response.isSuccessful()) {
                    if(response.body().equalsIgnoreCase("success"))
                    {
                        Toast.makeText(getApplicationContext(), "User has been saved" , Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),SuccessRegistrationActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Error has been occured on the server" , Toast.LENGTH_LONG).show();

                    }
                    Log.d("House", "new House owner");
                }else {
                    int statusCode  = response.code();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.d("RegistrationActivity", "error loading from API:" + t.getMessage());
                Toast.makeText(getApplicationContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return result;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
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
        //mControlsView.setVisibility(View.GONE);
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
