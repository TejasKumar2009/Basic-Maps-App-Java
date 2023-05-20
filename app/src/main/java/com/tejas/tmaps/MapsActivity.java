package com.tejas.tmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.tejas.tmaps.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    public boolean isFirstLocation = true;

    String [] mapTypes = {"Normal", "Satellite", "Terrain", "Hybrid"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Using Spinner
        Spinner mapTypeSpinner = findViewById(R.id.mapType_dropdown);
        mapTypeSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) MapsActivity.this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mapTypes);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapTypeSpinner.setAdapter(arrayAdapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng nd_latLng = new LatLng(28.6139, 77.2090);
        LatLng mumbai_latLng = new LatLng(19.0760, 72.8777);
        LatLng chennai_latLng = new LatLng(13.0827, 80.2707);

//        MarkerOptions markerOptions = new MarkerOptions().position(nd_latLng).title("New Delhi");
//        mMap.addMarker(markerOptions);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(nd_latLng));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(nd_latLng, 16f)); // This is for zooming into the location

        // Circle
        mMap.addCircle(new CircleOptions()
                .center(mumbai_latLng)
                .radius(1000)
                .fillColor(Color.BLUE)
                .strokeColor(Color.rgb(107, 144, 255)));

        // I can also add polygon but i am not doing it currently

        // Image
        mMap.addGroundOverlay(new GroundOverlayOptions()
                .position(chennai_latLng, 1000f, 1000f)
                .image(BitmapDescriptorFactory.fromResource(R.drawable.mannu_image)));

        // Geocoder
//        try {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        ArrayList<Address> addressArray = (ArrayList<Address>) geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        String placeName = addressArray.get(0).getFeatureName();
                        if (isFirstLocation){
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(placeName);
                            mMap.addMarker(markerOptions);
                            isFirstLocation = false;
                        }
                        else{
                            mMap.clear();
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(placeName);
                            mMap.addMarker(markerOptions);
                        }


                        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                        builder.setTitle("Details");
                        builder.setMessage("Do you want to see the full details of the selected location ?");

                        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {

                            if (addressArray.get(0).getAddressLine(0) == null)
                                addressArray.get(0).setAddressLine(0, "Not Known");
                            if (addressArray.get(0).getFeatureName() == null)
                                addressArray.get(0).setFeatureName("Not Known");
                            if (addressArray.get(0).getLocality() == null)
                                addressArray.get(0).setLocality("Not Known");
                            if (addressArray.get(0).getCountryName() == null)
                                addressArray.get(0).setCountryName("Not Known");
                            if (addressArray.get(0).getPostalCode() == null)
                                addressArray.get(0).setPostalCode("Not Known");
                            if (addressArray.get(0).getPhone() == null)
                                addressArray.get(0).setPhone("Phone No. Not Available");
                            if (addressArray.get(0).getUrl() == null)
                                addressArray.get(0).setUrl("No Website Url Available");


                            Intent intent = new Intent(MapsActivity.this, DetailsActivity.class);
                            String[] detailsInfo2 = {addressArray.get(0).getAddressLine(0), addressArray.get(0).getFeatureName(), addressArray.get(0).getLocality(), addressArray.get(0).getCountryName(), addressArray.get(0).getPostalCode(), addressArray.get(0).getLatitude() + "", addressArray.get(0).getLongitude() + "", addressArray.get(0).getPhone(), addressArray.get(0).getUrl()};
                            intent.putExtra("detailsInfo2", detailsInfo2);
                            startActivity(intent);
                        });

                        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                            dialog.cancel();
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();


                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MapsActivity.this, "Location Not Found !", Toast.LENGTH_SHORT).show();
                    }

                }
            });
//
//        } catch(Exception e){
//            Toast.makeText(this, "Location not found !", Toast.LENGTH_SHORT).show();
        }
   

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String currentMapType = mapTypes[position];
        if (Objects.equals(currentMapType, "Normal"))
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        else if (Objects.equals(currentMapType, "Satellite"))
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        else if (Objects.equals(currentMapType, "Terrain"))
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        else if (Objects.equals(currentMapType, "Hybrid"))
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}