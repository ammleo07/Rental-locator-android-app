package com.example.projectlocator;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.evernote.android.job.JobRequest;
import com.example.projectlocator.Service.LocationBroadcastService;
import com.example.projectlocator.Util.Job.LocatorService;
import com.example.projectlocator.Util.Job.TestService;
import com.example.projectlocator.Util.Realm.RealmController;
import com.example.projectlocator.Util.Retrofit.ApiUtils;
import com.example.projectlocator.Util.Retrofit.RetrofitService;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import Model.UserLocation;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    private GpsTracker gpsTracker;
    TextView txtLoc,txtUsername,txtCurrentUser;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    private Realm realm;
    String currentUser;
    private RetrofitService mService;
    private List<EventDay> mEventDays;
    CalendarView mCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);


        Intent intent = getIntent();
        currentUser=intent.getStringExtra("username");
        txtCurrentUser=header.findViewById(R.id.txtCurrentUser);
        txtLoc = (TextView) findViewById(R.id.txtLocation);

        txtCurrentUser.setText(currentUser);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, UserActivity.this);

        //saveLocation(getLocation());
        UserLocation userLocation=null;
        try {
            String location=getLocation();
            userLocation = new UserLocation();
            userLocation.setUsername(currentUser);
            userLocation.setLatitude(location.split(";")[0]);
            userLocation.setLongitude(location.split(";")[1]);
            userLocation.setDateCaptured("" + new Date());
            writeLocationTOXML(userLocation);
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),"error:" + ex.getMessage(),Toast.LENGTH_LONG).show();
        }
        txtLoc.setText("Latitude:" + userLocation.getLatitude() + "\nLongitude:" + userLocation.getLongitude() + "\nDate:" + userLocation.getDateCaptured() );

        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        cancelAllJob();
        //scheduleJob();

        try {
            List<String> schedules=new ArrayList<>();
            schedules.add("06/12/2018 12:43:28");
            schedules.add("05/12/2018 12:43:28");
            schedules.add("04/12/2018 12:43:28");
            setEvents(schedules);

        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),"error:" + ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    public void setEvents(List<String> dateInString) throws Exception
    {
        //MyEventDay myEventDay;
        mEventDays = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date;
        Calendar calendar;
        for(String scheduleDate:dateInString)
        {
            date = sdf.parse(scheduleDate);
            calendar= Calendar.getInstance();
            calendar.setTime(date);
            EventDay eventDay = new EventDay(calendar,R.mipmap.ama);
            mEventDays.add(eventDay);

        }
        mCalendarView.setEvents(mEventDays);
    }

    public void scheduleJob()
    {
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(this, LocatorService.class);
        JobInfo.Builder builder = new JobInfo.Builder(1, componentName);
        //builder.setMinimumLatency(5000);//Start after 15 seconds
        builder.setPeriodic(5000);//min time is 15 minutes only
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("Title", "Locator " + 1);
        builder.setExtras(bundle);
        if (scheduler != null) {
            int result = scheduler.schedule(builder.build());
            if (result == JobScheduler.RESULT_SUCCESS) {
                Log.d("Login act", "job success");
            } else {
                Log.e("Login act", "job failed");
            }
        }


    }

    public void cancelAllJob()
    {
        JobScheduler schedulers = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        schedulers.cancelAll();
    }



    public String getLocation(){
        String result="";
        GpsTracker gpsTracker = new GpsTracker(UserActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            result=latitude + ";" + longitude;
        }else{
            gpsTracker.showSettingsAlert();
        }

        return result;
    }

    public void saveLocation(String location)
    {
        this.realm = RealmController.with(this).getRealm();
        UserLocation userLocation = new UserLocation();
        //book.setId(RealmController.getInstance().getBooks().size() + 1);
        userLocation.setId((RealmController.getInstance().getUserLocations() == null) ? 1 + + System.currentTimeMillis() : RealmController.getInstance().getUserLocations().size() + System.currentTimeMillis());
        userLocation.setLatitude(location.split(";")[0]);
        userLocation.setLongitude(location.split(";")[1]);
        userLocation.setUsername(currentUser);
        userLocation.setDateCaptured("" + new Date());

        RealmController.with(this).refresh();
        realm.beginTransaction();
        realm.copyToRealm(userLocation);
        realm.commitTransaction();
        Toast.makeText(getApplicationContext(),"new location saved for:" + userLocation.getUsername(),Toast.LENGTH_LONG).show();

    }

    public UserLocation getLastSavedLocation()
    {
        this.realm = RealmController.with(this).getRealm();
        UserLocation userLocation = RealmController.with(this).getUserLocations().sort("id").last();
        RealmController.with(this).refresh();
        return userLocation;
    }

    public void writeLocationTOXML(UserLocation userLocation) throws Exception
    {
        XmlSerializer xmlSerializer= Xml.newSerializer();
        StringWriter writer= new StringWriter();
        xmlSerializer.setOutput(writer);
        xmlSerializer.startDocument("UTF-8",true);
        xmlSerializer.startTag("","locations");
        xmlSerializer.startTag("","location");
        xmlSerializer.startTag("","latitude");
        xmlSerializer.text(userLocation.getLatitude());
        xmlSerializer.endTag("","latitude");

        xmlSerializer.startTag("","longitude");
        xmlSerializer.text(userLocation.getLongitude());
        xmlSerializer.endTag("","longitude");

        xmlSerializer.startTag("","username");
        xmlSerializer.text(userLocation.getUsername());
        xmlSerializer.endTag("","username");

        xmlSerializer.startTag("","dateCaptured");
        xmlSerializer.text(userLocation.getDateCaptured());
        xmlSerializer.endTag("","dateCaptured");


        xmlSerializer.endTag("","location");
        xmlSerializer.endTag("","locations");
        xmlSerializer.endDocument();

        FileOutputStream outputStream=getApplicationContext().openFileOutput("location.xml",getApplicationContext().MODE_PRIVATE);
        outputStream.write(writer.toString().getBytes(),0,writer.toString().length());
        outputStream.close();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //startService(new Intent(this, LocationBroadcastService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stopService(new Intent(this, LocationBroadcastService.class));
    }

    @Override
    public void onLocationChanged(Location location) {
        try
        {
            txtLoc.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
            UserLocation userLocation = new UserLocation();
            userLocation.setUsername(currentUser);
            userLocation.setLongitude("" + location.getLongitude());
            userLocation.setLatitude("" + location.getLongitude());
            userLocation.setDateCaptured("" + new Date());
            writeLocationTOXML(userLocation);
        }
        catch (Exception ex)
        {
            Log.e("Error on writeXML",ex.getMessage());
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}
