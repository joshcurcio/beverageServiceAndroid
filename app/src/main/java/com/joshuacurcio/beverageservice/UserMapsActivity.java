package com.joshuacurcio.beverageservice;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.joshuacurcio.beverageservice.Objects.Course;
import com.joshuacurcio.beverageservice.Objects.OrderItem;
import com.joshuacurcio.beverageservice.Objects.UserOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class UserMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HashMap<String, Marker> courseOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        courseOrders = new HashMap<>();

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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new MyLocationListener();

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, locationListener);

            Singleton.mDatabase.child("courses").child(Singleton.selectedCourse).child("orders").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    for( DataSnapshot child : dataSnapshot.getChildren())
                    {
                        Marker tempMarker;
                        UserOrder temp = dataSnapshot.getValue(UserOrder.class);
                        LatLng tempLatLng = new LatLng(temp.getLattitude(), temp.getLongitude());
                        if(temp.getUser().equalsIgnoreCase(Singleton.mAuth.getCurrentUser().getUid()))
                        {
                            tempMarker = mMap.addMarker(new MarkerOptions().position(tempLatLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                        } else {
                            tempMarker = mMap.addMarker(new MarkerOptions().position(tempLatLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                        }
                        courseOrders.put(child.getKey(), tempMarker);
                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    for( DataSnapshot child : dataSnapshot.getChildren())
                    {
                        UserOrder temp = dataSnapshot.getValue(UserOrder.class);
                        LatLng tempLatLng = new LatLng(temp.getLattitude(), temp.getLongitude());
                        Marker tempMarker;
                        if (courseOrders.get(child.getKey()) != null)
                        {
                            tempMarker = courseOrders.get(child.getKey());
                            tempMarker.remove();
                        }

                        if(temp.getUser().equalsIgnoreCase(Singleton.mAuth.getCurrentUser().getUid()))
                        {
                            tempMarker = mMap.addMarker(new MarkerOptions().position(tempLatLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                        } else {
                            tempMarker = mMap.addMarker(new MarkerOptions().position(tempLatLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                        }


                        courseOrders.remove(child.getKey());
                        courseOrders.put(child.getKey(), tempMarker);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot)
                {
                    for( DataSnapshot child : dataSnapshot.getChildren())
                    {
                        UserOrder temp = dataSnapshot.getValue(UserOrder.class);
                        LatLng tempLatLng = new LatLng(temp.getLattitude(), temp.getLongitude());
                        Marker tempMarker;
                        if (courseOrders.get(child.getKey()) != null)
                        {
                            tempMarker = courseOrders.get(child.getKey());
                            tempMarker.remove();
                        }
                        courseOrders.remove(child.getKey());
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            // Show rationale and request permission.

        }
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            Singleton.userLocation = new LatLng(loc.getLatitude(),loc.getLongitude());
            Toast.makeText(UserMapsActivity.this, "LAT: " + Singleton.userLocation.latitude, Toast.LENGTH_SHORT).show();
            Singleton.mDatabase.child("courses").child(Singleton.selectedCourse).child("orders").child(Singleton.userOrderID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Singleton.userOrder = dataSnapshot.getValue(UserOrder.class);
                    Singleton.userOrder.setLongitude(Singleton.userLocation.longitude);
                    Singleton.userOrder.setLattitude(Singleton.userLocation.latitude);

                    Singleton.mDatabase.child("courses").child(Singleton.selectedCourse).child("orders").child(Singleton.userOrderID).setValue(Singleton.userOrder);
                    Singleton.userOrder = null;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }
}
