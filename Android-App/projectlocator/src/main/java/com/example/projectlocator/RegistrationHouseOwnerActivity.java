package com.example.projectlocator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectlocator.Fragments.HouseOwner.Tab1;
import com.example.projectlocator.Fragments.HouseOwner.Tab2;
import com.example.projectlocator.Fragments.HouseOwner.Tab3;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Model.Address;
import Model.House;
import Model.HouseOwner;
import Model.HouseOwnerForm;
import Model.RenteeForm;
import Model.User;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitService;
import Util.Retrofit.RetrofitServiceHouseOwner;
import Util.Validation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistrationHouseOwnerActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    Button btnPickImage;
    public EditText houseOwnerUsername, houseOwnerPassword,houseOwnerFirstName,houseOwnerMiddleName,houseOwnerLastName,houseOwnerContactNumber;
    public EditText houseName,monthlyFee,description;
    public EditText town,houseNo;
    public CheckBox isNegotiable;
    Spinner houseType,street,brgy, noOfSlots,boarderType;
    View formTab1,formTab2,progressBar,appBar;
    ProgressDialog progressDialog;
    public Fragment fragmentTab1, fragmentTab2;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_house_owner);
        //btnPickImage = (Button) findViewById(R.id.btnChooseImage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.owner_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.house_owner_tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        appBar = findViewById(R.id.appbar);
        progressBar = findViewById(R.id.house_owner_progress_bar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                houseOwnerUsername = (EditText) findViewById(R.id.register_house_owner_username);
                houseOwnerPassword = (EditText) findViewById(R.id.register_house_owner_password);
                houseOwnerFirstName = (EditText) findViewById(R.id.register_house_owner_first_name);
                houseOwnerMiddleName = (EditText) findViewById(R.id.register_house_owner_middle_name);
                houseOwnerLastName = (EditText) findViewById(R.id.register_house_owner_last_name);
                houseOwnerContactNumber = (EditText) findViewById(R.id.register_house_owner_contact_number);
                houseName = (EditText) findViewById(R.id.register_house_house_name);
                houseType = (Spinner) findViewById(R.id.houseType_list);
                monthlyFee = (EditText) findViewById(R.id.register_house_monthly_fee);
                isNegotiable = (CheckBox) findViewById(R.id.register_house_is_negotiable);
                noOfSlots = (Spinner) findViewById(R.id.register_house_number_of_slots);
                houseNo = (EditText) findViewById(R.id.register_house_address_number);
                street = (Spinner) findViewById(R.id.street_list);
                brgy = (Spinner) findViewById(R.id.barangay_list);
                boarderType = (Spinner) findViewById(R.id.boarderType_list);
                town = (EditText) findViewById(R.id.register_house_address_town);
                EditText latitude = (EditText) findViewById(R.id.register_house_address_latitude);
                EditText longitude = (EditText) findViewById(R.id.register_house_address_longitude);
                description = (EditText) findViewById(R.id.register_house_house_description);
                List<String> requiredFields = new ArrayList<>();

                User user = new User();
                user.setUsername(houseOwnerUsername.getText().toString());
                user.setPassword(houseOwnerPassword.getText().toString());
                user.setFirstName(houseOwnerFirstName.getText().toString());
                user.setMiddleName(houseOwnerMiddleName.getText().toString());
                user.setLastName(houseOwnerLastName.getText().toString());
                user.setUserType("House Owner");

                requiredFields.add(user.getUsername());
                requiredFields.add(user.getPassword());
                requiredFields.add(user.getFirstName());
                requiredFields.add(user.getLastName());

                HouseOwner houseOwner = new HouseOwner();
                houseOwner.setUsername(user.getUsername());
                houseOwner.setContactNumber(houseOwnerContactNumber.getText().toString());
                requiredFields.add(houseOwner.getContactNumber());

                House house = new House();
                house.setHouseType(houseType.getSelectedItem().toString());
                house.setNumberOfSlots(Integer.parseInt(noOfSlots.getSelectedItem().toString()));
                house.setMonthlyFee(Double.parseDouble(monthlyFee.getText().toString()));
                house.setHouseName(houseName.getText().toString());
                house.setIsNegotiable(isNegotiable.isChecked() ? "Y": "N");
                house.setBoarderType(boarderType.getSelectedItem().toString().equalsIgnoreCase("Select Boarder Type") ? "Both" : boarderType.getSelectedItem().toString());
                house.setDescription(description.getText().toString());
                requiredFields.add(house.getHouseType());
                requiredFields.add(house.getHouseName());
                requiredFields.add(house.getNumberOfSlots() + "");
                requiredFields.add(house.getMonthlyFee() + "");

                Address address = new Address();
                address.setHouseNo("");
                address.setStreet(street.getSelectedItem().toString());
                address.setBarangay(brgy.getSelectedItem().toString());
                address.setTown(town.getText().toString());
                address.setHouseNo(houseNo.getText().toString());
                address.setLatitude(Double.parseDouble(latitude.getText().toString()));
                address.setLongitude(Double.parseDouble(longitude.getText().toString()));
                address.setProvince("");

                requiredFields.add(address.getLatitude() + "");
                requiredFields.add(address.getLongitude() + "");
                requiredFields.add(address.getBarangay());
                requiredFields.add(address.getStreet());
                requiredFields.add(address.getTown());


                Validation validation = new Validation();
                if(validation.isRequiredFieldsCompleted(requiredFields)) {
                    HouseOwnerForm houseOwnerForm = new HouseOwnerForm();
                    houseOwnerForm.setUser(user);
                    houseOwnerForm.setHouseOwner(houseOwner);
                    houseOwnerForm.setHouse(house);
                    houseOwnerForm.setAddress(address);
                    if(houseOwnerForm.getHouse().getMonthlyFee() > 0) {
                        Intent intent = new Intent(RegistrationHouseOwnerActivity.this, PreviewHouseDetailsActivity.class);
                        intent.putExtra("User", houseOwnerForm);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Rental Fee must not be 0" , Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please complete all required fields. Note: All number type must not be 0" , Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    public void viewMap(View view)
    {
        Intent intent = new Intent(RegistrationHouseOwnerActivity.this, MapTestActivity.class);
        startActivityForResult(intent, 999);

    }

    public String getAddressFromLatLng(double lat,double lng) throws Exception
    {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<android.location.Address> addresses = geocoder.getFromLocation(lat, lng, 1);
        String cityName = addresses.get(0).getLocality();
        String stateName = addresses.get(0).getCountryName();
        String streetName = addresses.get(0).getThoroughfare();
        //Toast.makeText(getApplicationContext(), cityName + ":" + stateName + ":" + addresses.get(0).getThoroughfare() , Toast.LENGTH_LONG).show();
        return cityName;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 999) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                LatLng latLng = (LatLng) data.getParcelableExtra("picked_point");
                Toast.makeText(this, "Point Chosen: " + latLng.latitude + " " + latLng.longitude, Toast.LENGTH_LONG).show();
                EditText latitude = (EditText) findViewById(R.id.register_house_address_latitude);
                EditText longitude = (EditText) findViewById(R.id.register_house_address_longitude);
                EditText town = (EditText) findViewById(R.id.register_house_address_town);

                latitude.setText(latLng.latitude + "");
                longitude.setText(latLng.longitude + "");

                try {
                    town.setText(getAddressFromLatLng(latLng.latitude, latLng.longitude));
                }
                catch (Exception ex)
                {

                }

            }
        }
    }


    public void chooseImage(View view)
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration_house_owner, menu);
        return true;
    }









    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            //formTab2.setVisibility(show ? View.GONE : View.VISIBLE);
            formTab1.setVisibility(show ? View.GONE : View.VISIBLE);
            formTab1.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    formTab1.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            //logoView.setVisibility(show ? View.GONE : View.VISIBLE);
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            formTab1.setVisibility(show ? View.GONE : View.VISIBLE);

        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        public PlaceholderFragment() {
//        }
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_registration_house_owner, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            return rootView;
//        }
//    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //Toast.makeText(getApplicationContext(), position + "", Toast.LENGTH_LONG).show();
            switch (position) {
                case 0:
                    fragmentTab1 =new Tab1();
                    return fragmentTab1;
                case 1:
                    fragmentTab2 = new Tab2();
                    return fragmentTab2;
                case 2:
                    return new Tab3();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
