package com.example.comp3074_project;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import com.example.comp3074_project.room.Restaurant;
import com.example.comp3074_project.room.RestaurantRepo;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private RestaurantAdapter adapter;
    private RestaurantRepo restaurantRepo;
    private EditText searchBar;
    private Button addRestaurantButton;
    private Button aboutButton;
    private LiveData<List<Restaurant>> restaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        restaurantRepo = new RestaurantRepo(getApplication());

        listView = findViewById(R.id.restaurantList);
        adapter = new RestaurantAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant restaurant = (Restaurant) parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("RESTAURANT_ID", restaurant.getId());
                startActivity(intent);
            }
        });

        searchBar = findViewById(R.id.searchBar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRestaurants(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        addRestaurantButton = findViewById(R.id.addRestaurantButton);
        addRestaurantButton.setOnClickListener(v -> showAddRestaurantDialog());

        aboutButton = findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        loadRestaurants();
    }

    private void loadRestaurants() {
        if (restaurantList != null) {
            restaurantList.removeObservers(this);
        }
        restaurantList = restaurantRepo.getAllRestaurants();
        restaurantList.observe(this, restaurants -> adapter.setRestaurants(restaurants));
    }

    private void filterRestaurants(String query) {
        if (restaurantList != null) {
            restaurantList.removeObservers(this);
        }

        if (query.isEmpty()) {
            loadRestaurants();
        } else {
            String searchQuery = "%" + query + "%";
            restaurantList = restaurantRepo.searchRestaurantsByName(searchQuery);
            restaurantList.observe(this, restaurants -> adapter.setRestaurants(restaurants));
        }
    }

    private void showAddRestaurantDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_restaurant_dialog, null);
        builder.setView(dialogView);

        EditText etName = dialogView.findViewById(R.id.dialogName);
        EditText etAddress = dialogView.findViewById(R.id.etAddress);
        EditText etPhone = dialogView.findViewById(R.id.etPhone);
        EditText etDescription = dialogView.findViewById(R.id.etDescription);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        EditText etTags = dialogView.findViewById(R.id.dialogTags);

        builder.setTitle("Add Restaurant");
        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String address = etAddress.getText().toString().trim();

            if (name.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Name and Address are required", Toast.LENGTH_SHORT).show();
                return;
            }

            Geocoder geocoder = new Geocoder(this);
            double latitude = 0.0;
            double longitude = 0.0;

            try {
                List<Address> addresses = geocoder.getFromLocationName(address, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    latitude = addresses.get(0).getLatitude();
                    longitude = addresses.get(0).getLongitude();
                }
            } catch (IOException e) {
                // todo
            }

            String phone = etPhone.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            float rating = ratingBar.getRating();
            String tagsInput = etTags.getText().toString().trim();

            Restaurant restaurant = new Restaurant(
                    name,
                    description,
                    address,
                    phone,
                    rating,
                    latitude,
                    longitude
            );

            restaurantRepo.insert(restaurant, tagsInput);
            Toast.makeText(this, "Restaurant added", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
