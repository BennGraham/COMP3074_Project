package com.example.comp3074_project;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.comp3074_project.databinding.ActivityNavigationBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class NavigationActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";
    public static final String EXTRA_LABEL = "extra_label";
    private static final float MAP_ZOOM = 15f;
    private static final LatLng DEFAULT_LOCATION = new LatLng(43.67615, -79.41051);
    private static final String DEFAULT_LABEL = "Selected location";

    private GoogleMap map;
    private ActivityNavigationBinding binding;
    private LatLng target;
    private String markerLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        resolveTarget();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void resolveTarget() {
        double lat = getIntent().getDoubleExtra(EXTRA_LATITUDE, Double.NaN);
        double lng = getIntent().getDoubleExtra(EXTRA_LONGITUDE, Double.NaN);
        markerLabel = getIntent().getStringExtra(EXTRA_LABEL);

        if (!Double.isNaN(lat) && !Double.isNaN(lng)) {
            target = new LatLng(lat, lng);
        } else {
            target = DEFAULT_LOCATION;
        }

        if (TextUtils.isEmpty(markerLabel)) {
            markerLabel = DEFAULT_LABEL;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        updateMarker();
    }

    private void updateMarker() {
        if (map == null || target == null) {
            return;
        }
        map.clear();
        map.addMarker(new MarkerOptions().position(target).title(markerLabel));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(target, MAP_ZOOM));
    }
}