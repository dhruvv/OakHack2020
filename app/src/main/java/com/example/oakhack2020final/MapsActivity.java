package com.example.oakhack2020final;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnPolylineClickListener {
    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    LocationManager locationManager;
    LocationListener locationListener;
    long maxid;
    int flag = 0;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;




    public void centreMapOnLocation(final Location location, String title){

        Log.d(TAG, "centreMapOnLocation: ran");
        LatLng userLocation = new LatLng(37.78911, -122.40723);//CHANGE THIS FOR FINAL PRESENTATION

        CameraPosition cameraPosition = new CameraPosition.Builder().
                target(userLocation).
                tilt(40).
                zoom((float) 20.6).
                bearing(-30).
                build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        DatabaseReference ref = database.getInstance().getReference();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    maxid = dataSnapshot.getChildrenCount();
                    Log.d(TAG, "onDataChange: "+maxid);
                        parseData(maxid);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    private void parseData(long noOfChildren){
        Log.d(TAG, "parseData: "+noOfChildren);
        for(int i = 1; i<=noOfChildren; i++) {
            DatabaseReference mRef = database.getInstance().getReference().child(String.valueOf(i));

            final int finalI = i;
            mRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String dataSnapshotStr = dataSnapshot.getValue().toString();
                    dataSnapshotStr.replace("{", "");
                    dataSnapshotStr.replace("}", "");
                    String[] dataSnapshotSplitted = dataSnapshotStr.split("\\s+");
                    String LAT = dataSnapshotSplitted[0];
                    String LONG = dataSnapshotSplitted[1];

                    addMarker(LAT, LONG, finalI);



                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });
        }
    }


    private void addMarker(String LAT, String LONG, int i){

        int height = 230;
        int width = 230;
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.man);
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        BitmapDescriptor smallMarkerIconMan = BitmapDescriptorFactory.fromBitmap(smallMarker);


        Bitmap a = BitmapFactory.decodeResource(getResources(), R.drawable.stickman);
        Bitmap smallMarkerManTwo = Bitmap.createScaledBitmap(a, width, height, false);
        BitmapDescriptor smallMarkerIconManTwo = BitmapDescriptorFactory.fromBitmap(smallMarkerManTwo);



        if(i%2 == 0) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.valueOf(LAT), Double.valueOf(LONG))).icon(smallMarkerIconMan));
        }else{
            if(i%2 != 0){
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.valueOf(LAT), Double.valueOf(LONG))).icon(smallMarkerIconManTwo));
            }
        }

        int heightHome = 150;
        int widthHome = 150;

        Bitmap c = BitmapFactory.decodeResource(getResources(), R.drawable.home);
        Bitmap homeMarker = Bitmap.createScaledBitmap(c, widthHome, heightHome, false);
        BitmapDescriptor homeMarkerIcon = BitmapDescriptorFactory.fromBitmap(homeMarker);
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.789007, -122.407308)).icon(homeMarkerIcon).title("Lounge Google Home"));

        Bitmap d = BitmapFactory.decodeResource(getResources(), R.drawable.home);
        Bitmap homeMarkerTwo = Bitmap.createScaledBitmap(c, widthHome, heightHome, false);
        BitmapDescriptor homeMarkerIconTwo = BitmapDescriptorFactory.fromBitmap(homeMarker);
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.789277, -122.407125)).icon(homeMarkerIcon).title("Entrance Google Home"));

        Bitmap e = BitmapFactory.decodeResource(getResources(), R.drawable.roomba);
        Bitmap roombaMarker = Bitmap.createScaledBitmap(e, widthHome, heightHome, false);
        BitmapDescriptor roombaMarkerIcon = BitmapDescriptorFactory.fromBitmap(roombaMarker);
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.789268, -122.407332)).icon(roombaMarkerIcon).title("Roomba"));



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centreMapOnLocation(lastKnownLocation,"Your Location");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }



    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        googleMap.setIndoorEnabled(true);
        Intent intent = getIntent();


        if (intent.getIntExtra("Place Number",0) == 0 ){

            // Zoom into users location

            mMap.setOnMarkerClickListener(this);

            locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    centreMapOnLocation(location,"Your Location");
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
            };

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centreMapOnLocation(lastKnownLocation,"Your Location");
            } else {

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }

        List<LatLng> list = null;

        // Get the data: latitude/longitude positions of police stations.
        try {
            list = readItems(R.raw.police_stations);
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of locations.", Toast.LENGTH_LONG).show();
        }

        int[] colors = {
                Color.rgb(102, 225, 0), // green
                Color.rgb(255, 0, 0)    // red
        };

        float[] startPoints = {
                0.7f, 1f
        };

        Gradient gradient = new Gradient(colors, startPoints);

        mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();

        mProvider.setGradient(gradient);
        mProvider.setOpacity(0.6);

        mProvider.setRadius(100);
        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

    }

    private ArrayList<LatLng> readItems(int resource) throws JSONException {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        InputStream inputStream = getResources().openRawResource(resource);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            list.add(new LatLng(lat, lng));
        }
        return list;
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTitle().equals("Lounge Google Home")) {
            Log.d(TAG, "onMarkerClick: marker has been clicked");

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getInstance().getReference().child("LoungeRoomHome");
            ref.child("Clicked").setValue("true");
        }else{
            if(marker.getTitle().equals("Entrance Google Home")){
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getInstance().getReference().child("EntranceGoogleHome");
                ref.child("Clicked").setValue("true");
            }else{
                if(marker.getTitle().equals("Roomba")){
                    Polyline polyline1 = mMap.addPolyline((new PolylineOptions())
                            .clickable(true)
                            .add(new LatLng(37.789292, -122.407318),
                                    new LatLng(37.789218, -122.407255),
                                    new LatLng(37.789203, -122.407253),
                                    new LatLng(37.789189, -122.407254),
                                    new LatLng(37.789153, -122.407247),
                                    new LatLng(37.789095, -122.407226),
                                    new LatLng(37.789067, -122.40722),
                                    new LatLng( 37.789059, -122.407262)));



//                    lat" : 37.78922, "lng" : -122.407336 } ,
//                    {"lat" : 37.789221, "lng" : -122.407377 } ,
//                    {"lat" : 37.7892, "lng" : -122.407379 } ,
//                    {"lat" : 37.789178, "lng" : -122.407383 } ,
//                    {"lat" : 37.788997 , "lng" : -122.4071 },
//                    {"lat" : 37.788985, "lng" : -122.407151 }
                    // Set listeners for click events.
                    mMap.setOnPolylineClickListener(this);
                }
            }
        }
        return false;

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }
}
