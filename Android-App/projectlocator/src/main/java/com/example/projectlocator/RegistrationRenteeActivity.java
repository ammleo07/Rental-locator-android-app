package com.example.projectlocator;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
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

import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectlocator.Fragments.Rentee.Tab1;
import com.example.projectlocator.Fragments.Rentee.Tab2;
import com.example.projectlocator.Fragments.Rentee.Tab3;

import java.util.ArrayList;
import java.util.List;

import Model.Rentee;
import Model.RenteeForm;
import Model.User;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitService;
import Util.Validation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationRenteeActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public EditText username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        //getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_registration_rentee);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        //username = (EditText) findViewById(R.id.register_rentee_username);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = (EditText) findViewById(R.id.register_rentee_username);
                EditText password = (EditText) findViewById(R.id.register_rentee_password);
                EditText firstName = (EditText) findViewById(R.id.register_rentee_first_name);
                EditText middleName = (EditText) findViewById(R.id.register_rentee_middle_name);
                EditText lastName = (EditText) findViewById(R.id.register_rentee_last_name);
                EditText contactNumber = (EditText) findViewById(R.id.register_rentee_contact_number);
                Spinner houseType = (Spinner) findViewById(R.id.houseType_list);
                EditText minPrice = (EditText) findViewById(R.id.register_rentee_min_price);
                EditText maxPrice = (EditText) findViewById(R.id.register_rentee_max_price);

                User user = new User();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());
                user.setFirstName(firstName.getText().toString());
                user.setMiddleName(middleName.getText().toString());
                user.setLastName(lastName.getText().toString());
                user.setUserType("Rentee");

                List<String> requiredFields = new ArrayList<>();
                requiredFields.add(user.getUsername());
                requiredFields.add(user.getFirstName());
                requiredFields.add(user.getLastName());

                Rentee rentee = new Rentee();
                rentee.setContactNumber(contactNumber.getText().toString());
                rentee.setUsername(user.getUsername());
                rentee.setHouseType(houseType.getSelectedItem().toString());
                rentee.setMinPriceRange(Double.parseDouble(minPrice.getText().toString()));
                rentee.setMaxPriceRange(Double.parseDouble(maxPrice.getText().toString()));

                requiredFields.add(rentee.getHouseType());
                requiredFields.add(rentee.getContactNumber());
                requiredFields.add(rentee.getMaxPriceRange() + "");
                requiredFields.add(rentee.getMinPriceRange() + "");

                Validation validation = new Validation();
                if(validation.isRequiredFieldsCompleted(requiredFields)) {
                    RenteeForm form = new RenteeForm();
                    form.setRentee(rentee);
                    form.setUser(user);
                    Intent intent = new Intent(RegistrationRenteeActivity.this, PreviewRenteeDetailsActivity.class);
                    intent.putExtra("User", form);
                    startActivity(intent);
                    //saveUser(form);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please complete all required fields" , Toast.LENGTH_LONG).show();
                }




            }
        });


    }

    public boolean saveUser(final RenteeForm rentee) {
        boolean result=false;
        RetrofitService mService;
        mService= ApiUtils.getSOService();
        mService.saveUser(rentee).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(response.isSuccessful()) {
                    //showProgress(false);
                    if(response.body().equalsIgnoreCase("success"))
                    {
                        Toast.makeText(getApplicationContext(), "User has been saved" , Toast.LENGTH_LONG).show();
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration_rentee, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_registration_rentee, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

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
            //return PlaceholderFragment.newInstance(position + 1);
            switch (position) {
                case 0:
                    return new Tab1();
                case 1:
                    return new Tab2();
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
