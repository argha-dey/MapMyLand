package com.cyberswift.facebookintegrate.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cyberswift.facebookintegrate.R;
import com.cyberswift.facebookintegrate.custom.CustomTextView;
import com.cyberswift.facebookintegrate.model.LatitudeLongitude;
import com.cyberswift.facebookintegrate.model.MySaveShape;
import com.cyberswift.facebookintegrate.model.MySingleShape;
import com.cyberswift.facebookintegrate.model.SaveListModel;
import com.cyberswift.facebookintegrate.utility.PermissionUtils;
import com.cyberswift.facebookintegrate.utility.Util;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;


/**
 * Created by User-129-pc on 20-09-2018.
 */

public class MultiShapeMapActivity extends AppCompatActivity implements OnMyLocationButtonClickListener,
        OnMyLocationClickListener, GoogleApiClient.ConnectionCallbacks,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, View.OnClickListener {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;
    // The entry points to the Places API.
    public Location mLastKnownLocation;
    // The entry point to the Fused Location Provider.
    private boolean mLocationPermissionGranted;
    private Button btn_polySquare, btn_clear, btn_polyLine, btn_polygon, btn_add_text, btn_circle, btn_save;
    private ProgressDialog mProDialog;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private ArrayList<PolygonOptions> squareList;
    int numberOfShape = 0;
    int numberOfPolyMarker = 0;
    private Marker polyLineMarker;
    private Marker polygonMarker;
    private Marker currentLocation;
    private ArrayList<MapShape> storeShapes = new ArrayList<>();

    private Marker TL_Marker, TR_Marker, BL_Marker, BR_Marker;
    private Marker centerMarker, circleDragableMarker;
    private Realm myRealm;
    String fileName;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity_layout);
        squareList = new ArrayList<>();
        mContext = MultiShapeMapActivity.this;
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    public void findPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete
                            .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());
                ((CustomTextView) findViewById(R.id.findPlace)).setText(place.getAddress());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 18.0f));

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // Prompt the user for permission.
        getLocationPermission();
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
        btn_polySquare = findViewById(R.id.btn_polySquare);
        btn_polySquare.setOnClickListener(this);
        btn_clear = findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);
        btn_polyLine = findViewById(R.id.btn_polyLine);
        btn_polyLine.setOnClickListener(this);
        btn_polygon = findViewById(R.id.btn_polygon);
        btn_polygon.setOnClickListener(this);
        btn_add_text = findViewById(R.id.btn_add_text);
        btn_add_text.setOnClickListener(this);
        btn_circle = findViewById(R.id.btn_circle);
        btn_circle.setOnClickListener(this);
        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        // Setting a custom info window adapter for the google map
    /*    CustomMarkerInfoWindowAdapter markerInfoWindowAdapter = new CustomMarkerInfoWindowAdapter(getApplicationContext());
        googleMap.setInfoWindowAdapter(markerInfoWindowAdapter);*/
        //  getDeviceLocation();

/**
 *   Marker Draggable on Click Listener  *****/

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

                String tag = String.valueOf(marker.getTag());
                String arrayPosition[] = tag.split(":");
                int pos = Integer.valueOf(arrayPosition[0]);
                int markerPos = Integer.valueOf(arrayPosition[1]);
                System.out.println("Shape position : " + pos);
                System.out.println("Marker Position : " + markerPos);


                // polyline Draggable Marker ....
                if (storeShapes.get(pos).getShape_type().equals("polyline")) {
                    System.out.println("@@ ----- Draw Polyline ----- @@");
                    storeShapes.get(pos).arrayOfPolyLineMarker.get(markerPos).setPosition(marker.getPosition());
                    storeShapes.get(pos).drawShape(mMap);
                }

                // polygon Draggable Marker....
                else if (storeShapes.get(pos).getShape_type().equals("polygon")) {
                    System.out.println("@@ ----- Draw polygon ----- @@");
                    storeShapes.get(pos).arrayOfPolyLineMarker.get(markerPos).setPosition(marker.getPosition());
                    storeShapes.get(pos).drawShape(mMap);
                }

                // circle Draggable Marker....
                else if (storeShapes.get(pos).getShape_type().equals("circle")) {
                    System.out.println("@@ ----- Draw circle ----- @@");
                    ArrayList<Marker> circleMarkerList = storeShapes.get(pos).arrayOfPolyLineMarker;
                    circleMarkerList.get(1).setPosition(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
                    storeShapes.get(pos).arrayOfPolyLineMarker = circleMarkerList;
                    storeShapes.get(pos).drawShape(mMap);
                }

                // Square Draggable Marker .....
                else if (storeShapes.get(pos).getShape_type().equals("square")) {
                    System.out.println("@@ ----- Draw square ----- @@");
                    ArrayList<Marker> squarePoints = storeShapes.get(pos).arrayOfPolyLineMarker;
                    squarePoints.get(1).setPosition(new LatLng(squarePoints.get(0).getPosition().latitude, marker.getPosition().longitude));
                    squarePoints.get(2).setPosition(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
                    squarePoints.get(3).setPosition(new LatLng(marker.getPosition().latitude, squarePoints.get(3).getPosition().longitude));
                    storeShapes.get(pos).arrayOfPolyLineMarker = squarePoints;
                    storeShapes.get(pos).drawShape(mMap);
                }

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });

         /***
 *  Draw Shape From Save Data  *****/

        drawShapeFromSaveData();

    }


    private void drawShapeFromSaveData() {
        mMap.clear();
        ArrayList<SaveListModel> saveMysaveShapes = Util.fetcharraylist(mContext);
        if(saveMysaveShapes.size()>0) {
            for (int pos = 0; pos < saveMysaveShapes.size(); pos++) {
                ArrayList<Marker> tempMarkerList = new ArrayList<Marker>();
                for (int i = 0; i < saveMysaveShapes.get(pos).getLatitudeLongitudes().size(); i++) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    Marker marker = mMap.addMarker(markerOptions.position(new LatLng(saveMysaveShapes.get(pos).getLatitudeLongitudes().get(i).getLatitude(), saveMysaveShapes.get(pos).getLatitudeLongitudes().get(i).getLongitude())));
                    tempMarkerList.add(marker);
                }
                MapShape shape = new MapShape(saveMysaveShapes.get(pos).getShapeType(), saveMysaveShapes.get(pos).getShapId(), tempMarkerList);
                shape.drawShape(mMap);
            }
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }


    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(this);
                mMap.setOnMyLocationClickListener(this);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setAllGesturesEnabled(true);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    @Override
    public boolean onMyLocationButtonClick() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getLocationPermission();
        } else {
            showProgressDialog();
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        hideProgressDialog();
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        currentLocation = mMap.addMarker(new MarkerOptions().position(latLng).title("Your Current Location"));
                        currentLocation.setTag(String.valueOf("CurrentLocation"));
                        mMap.setMinZoomPreference(15.0f);
                    } else {
                        hideProgressDialog();
                        showGpsServiceNotActive();
                    }
                }


            });

        }
        return false;
    }

    private void showGpsServiceNotActive() {
        PermissionUtils.ShowOkDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        //  Toast.makeText(this, "Current location:\n" + location., Toast.LENGTH_LONG).show();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_polySquare:
                if (storeShapes.size() > numberOfShape) {
                    numberOfShape = numberOfShape + 1;
                }
                numberOfPolyMarker = 0;

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        if (numberOfShape >= storeShapes.size()) {
                            MarkerOptions markerOptions1 = new MarkerOptions();
                            TL_Marker = mMap.addMarker(markerOptions1.position(latLng));
                            TL_Marker.setTag("" + numberOfShape + ":" + (numberOfPolyMarker));

                            numberOfPolyMarker = numberOfPolyMarker + 1;
                            MarkerOptions markerOptions2 = new MarkerOptions();
                            TR_Marker = mMap.addMarker(markerOptions2.position(new LatLng(latLng.latitude, latLng.longitude + 0.005)));
                            TR_Marker.setVisible(false);
                            TR_Marker.setTag("" + numberOfShape + ":" + (numberOfPolyMarker));

                            numberOfPolyMarker = numberOfPolyMarker + 1;
                            MarkerOptions markerOptions4 = new MarkerOptions();
                            BR_Marker = mMap.addMarker(markerOptions4.position(new LatLng(latLng.latitude - 0.005, latLng.longitude + 0.005)).draggable(true));
                            BR_Marker.setTag("" + numberOfShape + ":" + (numberOfPolyMarker));

                            numberOfPolyMarker = numberOfPolyMarker + 1;
                            MarkerOptions markerOptions3 = new MarkerOptions();
                            BL_Marker = mMap.addMarker(markerOptions3.position(new LatLng(latLng.latitude - 0.005, latLng.longitude)).draggable(true));
                            BL_Marker.setTag("" + numberOfShape + ":" + (numberOfPolyMarker));
                            BL_Marker.setVisible(false);

                            ArrayList<Marker> temp = new ArrayList<Marker>();
                            temp.add(TL_Marker);
                            temp.add(TR_Marker);
                            temp.add(BR_Marker);
                            temp.add(BL_Marker);
                            MapShape shape = new MapShape("square", numberOfShape, temp);
                            shape.drawShape(mMap);
                            storeShapes.add(shape);
                        }
                    }
                });
                break;


            case R.id.btn_circle:
                if (storeShapes.size() > numberOfShape) {
                    numberOfShape = numberOfShape + 1;
                }
                numberOfPolyMarker = 0;
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        if (numberOfShape >= storeShapes.size()) {
                            MarkerOptions markerOptions5 = new MarkerOptions();
                            centerMarker = mMap.addMarker(markerOptions5.position(latLng));
                            centerMarker.setTag("" + numberOfShape + ":" + (numberOfPolyMarker));

                            numberOfPolyMarker = numberOfPolyMarker + 1;
                            MarkerOptions markerOptions6 = new MarkerOptions();
                            circleDragableMarker = mMap.addMarker(markerOptions6.position(new LatLng(latLng.latitude + 0.005, latLng.longitude + 0.005)));
                            circleDragableMarker.setDraggable(true);
                            circleDragableMarker.setTag("" + numberOfShape + ":" + (numberOfPolyMarker));


                            ArrayList<Marker> tempCircleMarkerList = new ArrayList<Marker>();
                            tempCircleMarkerList.add(centerMarker);
                            tempCircleMarkerList.add(circleDragableMarker);
                            MapShape shape = new MapShape("circle", numberOfShape, tempCircleMarkerList);
                            shape.drawShape(mMap);
                            storeShapes.add(shape);

                        }

                    }
                });


                break;


            case R.id.btn_polyLine:
                if (storeShapes.size() > numberOfShape) {
                    numberOfShape = numberOfShape + 1;
                }
                numberOfPolyMarker = 0;
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()

                {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        polyLineMarker = mMap.addMarker(markerOptions.position(latLng).draggable(true));
                        polyLineMarker.setTag("" + numberOfShape + ":" + (numberOfPolyMarker));
                        numberOfPolyMarker = numberOfPolyMarker + 1;

                        if (numberOfShape >= storeShapes.size()) {
                            ArrayList<Marker> temp = new ArrayList<Marker>();
                            temp.add(polyLineMarker);
                            MapShape shape = new MapShape("polyline", numberOfShape, temp);
                            shape.drawShape(mMap);
                            storeShapes.add(shape);
                        } else {
                            MapShape shape = storeShapes.get(numberOfShape);
                            shape.arrayOfPolyLineMarker.add(polyLineMarker);
                            shape.drawShape(mMap);
                            storeShapes.set(numberOfShape, shape);
                        }
                    }
                });


                break;
            case R.id.btn_clear:
                mMap.clear();
                break;

            case R.id.btn_polygon:
                if (storeShapes.size() > numberOfShape) {
                    numberOfShape = numberOfShape + 1;
                }
                numberOfPolyMarker = 0;
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        MarkerOptions markerPolygon = new MarkerOptions();
                        markerPolygon.position(latLng);
                        polygonMarker = mMap.addMarker(markerPolygon.position(latLng).draggable(true));
                        polygonMarker.setTag("" + numberOfShape + ":" + (numberOfPolyMarker));
                        numberOfPolyMarker = numberOfPolyMarker + 1;
                        if (numberOfShape >= storeShapes.size()) {
                            ArrayList<Marker> temp = new ArrayList<Marker>();
                            temp.add(polygonMarker);
                            MapShape shape = new MapShape("polygon", numberOfShape, temp);
                            shape.drawShape(mMap);
                            storeShapes.add(shape);
                        } else {
                            MapShape shape = storeShapes.get(numberOfShape);
                            shape.arrayOfPolyLineMarker.add(polygonMarker);
                            shape.drawShape(mMap);
                            storeShapes.set(numberOfShape, shape);
                        }
                    }
                });


                break;
            case R.id.btn_add_text:
                break;

            case R.id.btn_save:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MultiShapeMapActivity.this);
                alertDialog.setTitle("SAVE FILE");
                alertDialog.setMessage("Enter a file name");
                final EditText input = new EditText(MultiShapeMapActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (input.getText().toString().compareTo("") != 0) {
                                    myRealm = Realm.getDefaultInstance();
                                    myRealm.beginTransaction();
                                    MySaveShape mySaveShapeObject = myRealm.createObject(MySaveShape.class);
                                    mySaveShapeObject.setFileName(input.getText().toString());
                                    RealmList<MySingleShape> tempMySingleShape = new RealmList<>();
                                    for (int i = 0; i < storeShapes.size(); i++) {
                                        System.out.println("number of shape  : " + storeShapes.size());
                                        MySingleShape mySingleShapeObject = myRealm.createObject(MySingleShape.class);
                                        mySingleShapeObject.setShapeName(storeShapes.get(i).getShape_type());
                                        mySingleShapeObject.setShapeId(storeShapes.get(i).getShape_id());
                                        RealmList<LatitudeLongitude> tempLatLong = new RealmList<>();
                                        for (int j = 0; j < storeShapes.get(i).arrayOfPolyLineMarker.size(); j++) {
                                            System.out.println("size of marker for any one shape  : " + storeShapes.get(i).arrayOfPolyLineMarker.size());
                                            LatitudeLongitude latitudeLongitudeObject = myRealm.createObject(LatitudeLongitude.class);
                                            latitudeLongitudeObject.setLatitude(storeShapes.get(i).arrayOfPolyLineMarker.get(j).getPosition().latitude);
                                            latitudeLongitudeObject.setLongitude(storeShapes.get(i).arrayOfPolyLineMarker.get(j).getPosition().longitude);
                                            tempLatLong.add(latitudeLongitudeObject);
                                        }
                                        mySingleShapeObject.setLatitudeLongitudes(tempLatLong);
                                        tempMySingleShape.add(mySingleShapeObject);
                                    }
                                    mySaveShapeObject.setMySingleShapes(tempMySingleShape);
                                    myRealm.commitTransaction();


                                    Intent intent = new Intent(mContext, ShapeListActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    Activity activity = (Activity) mContext;
                                    mContext.startActivity(intent);
                                    activity.overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
                                    Toast.makeText(getApplicationContext(), " File saved", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            " Please enter a name to save your file.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();

                break;

            default:
                // TODO Auto-generated method stub
                break;
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void showProgressDialog() {
        if (mProDialog == null) {
            mProDialog = new ProgressDialog(this);
            mProDialog.setMessage("Fetching your location...");
            mProDialog.setIndeterminate(true);
        }

        mProDialog.show();
    }

    private void hideProgressDialog() {
        if (mProDialog != null && mProDialog.isShowing()) {
            mProDialog.hide();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideProgressDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
    }

}
