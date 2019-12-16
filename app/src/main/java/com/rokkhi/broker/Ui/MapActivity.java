package com.rokkhi.broker.Ui;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rokkhi.broker.Model.FBuildings;
import com.rokkhi.broker.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Context context;
    FBuildings fBuildings;

    GoogleApiClient mGoogleApiClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        context=this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        fBuildings = (FBuildings) getIntent().getSerializableExtra("fbuildings");

        Log.e("TAG", "onCreate: " + fBuildings.getLatitude().toString());
        Log.e("TAG", "onCreate: " + fBuildings.getLongitude().toString());
//        Toast.makeText(this, ""+fBuildings.getLatitude(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, ""+fBuildings.getLongitude(), Toast.LENGTH_SHORT).show();
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


        mMap.setMyLocationEnabled(true);
        mMap.setBuildingsEnabled(false);

        LatLng loacation = new LatLng(fBuildings.getLatitude(), fBuildings.getLongitude());

        Marker marker = mMap.addMarker(new MarkerOptions().position(loacation).title(fBuildings.getHousename())
                .snippet("House Number: "+fBuildings.getB_houseno()+",Road Number: "+fBuildings.getB_roadno()));

        marker.showInfoWindow();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                showInformationDialog(context);


                return false;
            }
        });

        Log.e("TAG", "onMapReady: "+fBuildings.getB_imageUrl().get(0) );


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loacation,16.0f));

    }

    private void showInformationDialog(Context context) {


        AlertDialog.Builder alert = new AlertDialog.Builder(MapActivity.this);
        View view = getLayoutInflater().inflate(R.layout.show_building_information_alert, null);

        Button okButton = view.findViewById(R.id.okButtonAlert);
        ImageView imageView = view.findViewById(R.id.imageviewAlert);
        ProgressBar progressBar=view.findViewById(R.id.progressbarAlert);
        TextView houseNameAlert = view.findViewById(R.id.houseNameAlert);
        TextView housenumberAlert = view.findViewById(R.id.housenumberAlert);
        TextView roadNameAlert = view.findViewById(R.id.roadNameAlert);
        TextView roadNumberAlert = view.findViewById(R.id.roadNumberAlert);
        alert.setView(view);

        alert.setCancelable(false);

        Picasso.get().load(fBuildings.getB_imageUrl().get(0))
                .fit()
                .error(R.drawable.noimage)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        imageView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        imageView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                    }
                });

        houseNameAlert.setText(": "+fBuildings.getHousename());
        housenumberAlert.setText(": "+fBuildings.getB_houseno());
        roadNumberAlert.setText(": "+fBuildings.getB_roadno());
        roadNameAlert.setText(": "+fBuildings.getB_roadName());


        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });


    }

    public void cancelBtn(View view) {

        finish();
    }

    public void reportToAdmin(View view) {

        Intent intent = new Intent(MapActivity.this, ReportActivity.class);
        intent.putExtra("fbuildings", fBuildings);
        startActivity(intent);

    }
}
