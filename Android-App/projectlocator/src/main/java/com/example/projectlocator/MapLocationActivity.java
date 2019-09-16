package com.example.projectlocator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

import Model.HouseOwnerForm;


public class MapLocationActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    double lat,lng;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    HouseOwnerForm ownerForm;
    Double oldTransportation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        ownerForm = (HouseOwnerForm) (getIntent().getSerializableExtra("House"));
        oldTransportation = getIntent().getDoubleExtra("Transporation", 0.0);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }else {

            mMap.setMyLocationEnabled(true);

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            Criteria criteria = new Criteria();

            String provider = locationManager.getBestProvider(criteria, true);

            Location location = locationManager.getLastKnownLocation(provider);

            if(location!=null){
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(provider, 20000, 5, this);
        }
        // Add a marker in Sydney and move the camera
        //mMap.setMyLocationEnabled(true);

        //String latLong = getLocation();
        //LatLng currentLod = new LatLng(lng, lat);
        //Toast.makeText(getApplicationContext(), "location:" + mMap.getMyLocation().getLatitude(), Toast.LENGTH_LONG).show();
        //mMap.addMarker(new MarkerOptions().position(currentLod).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLod));

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition position) {
                LatLng latvalue = position.target;
                double lat = latvalue.latitude;
                double lng = latvalue.longitude;
                LatLng loc = new LatLng(lat, lng);
                //mMap.clear();

                //mMap.addMarker(new MarkerOptions().position(loc).title(" Your location"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                //Toast.makeText(getApplicationContext(), "location:" + lat + ":" + lng, Toast.LENGTH_LONG).show();
            }
        });
    }

    public String getLocation(){
        String result="";
        GpsTracker gpsTracker = new GpsTracker(MapLocationActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            lat= latitude;
            lng =longitude;
            result=latitude + ";" + longitude;
        }else{
            gpsTracker.showSettingsAlert();
        }

        return result;
    }


    @Override
    public void onLocationChanged(Location location) {
        mMap.clear();
        double latitude = location.getLatitude();

        double longitude = location.getLongitude();

        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions renteeMarker = new MarkerOptions().position(
                               new LatLng(latitude, longitude))
                              .title("Rentee Location");
        renteeMarker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        MarkerOptions houseMarker = new MarkerOptions().position(
                new LatLng(ownerForm.getAddress().getLatitude(), ownerForm.getAddress().getLongitude()))
                .title("House Location");

//        MarkerOptions houseMarker = new MarkerOptions().position(
//                new LatLng(14.5705, 121.0272))
//                .title("House Location");

        //mMap.addMarker(renteeMarker);
        //mMap.addMarker(houseMarker);

//        houseMarker.icon(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
//
        mMap.addMarker(renteeMarker);
        mMap.addMarker(houseMarker);
//
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(renteeMarker.getPosition());
        builder.include(houseMarker.getPosition());
        LatLngBounds bounds = builder.build();
//
//
//        //mMap.addMarker(new MarkerOptions().position(latLng).title("Current Position"));
        int padding = 150; // offset from edges of the map in pixels
        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(cu);
                //map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30));
            }
        });
        //mMap.animateCamera(cu);
        //mMap.moveCamera(cu);

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        //LatLng SYDNEY = new LatLng(latitude,longitude);
        //LatLng MOUNTAIN_VIEW = new LatLng(14.577733, 121.033659);


// Obtain the map from a MapFragment or MapView.

// Move the camera instantly to Sydney with a zoom of 15.
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, 15));

// Zoom in, animating the camera.
        //mMap.animateCamera(CameraUpdateFactory.zoomIn());

// Zoom out to zoom level 10, animating with a duration of 2 seconds.
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 5000, null);

// Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(MOUNTAIN_VIEW)      // Sets the center of the map to Mountain View
//                .zoom(17)                   // Sets the zoom
//                .bearing(90)                // Sets the orientation of the camera to east
//                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
//                .build();                   // Creates a CameraPosition from the builder
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        float results[] =new float[19];
        Location.distanceBetween(latitude,longitude,ownerForm.getAddress().getLatitude(),ownerForm.getAddress().getLongitude(),results);
        //Location.distanceBetween(latitude,longitude,14.5925,121.0282,results);
        Toast.makeText(getApplicationContext(), "Distance:" + ((results[0] / 1000) + 2) + "\nOld Transporation:" + oldTransportation  +"\nEstimated taxi fare w/o traffic: PHP. " + computeFare((results[0] / 1000) + 2) + "\nEstimated taxi fare for 1 hour waiting with traffic: PHP. " + (computeFare((results[0] / 1000) + 2) + 120) + "\nNew Transportation w/o Traffic Savings: PHP. " + computeTotal(computeFare((results[0] / 1000) + 2)) + "\nNew Transportation 1 Hour Waiting With Traffic Savings: PHP. " + computeTotal(computeFare((results[0] / 1000) + 2) + 120) , Toast.LENGTH_LONG).show();

    }

    public double computeFare(float distance)
    {
        double fare=40.00;
        fare = fare + (Math.floor(distance) * 13.5);
        return fare;
    }

    public double computeTotal(double computedFare)
    {

        return oldTransportation - computedFare;
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
