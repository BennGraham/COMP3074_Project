package com.example.comp3074_project;

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

import com.example.comp3074_project.room.Restaurant;
import com.example.comp3074_project.room.RestaurantRepo;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private RestaurantAdapter adapter;
    private RestaurantRepo restaurantRepo;
    private EditText searchBar;
    private Button addRestaurantButton;

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
                // TODO: open detials on press
                Toast.makeText(MainActivity.this, "Clicked: " + restaurant.getName(), Toast.LENGTH_SHORT).show();
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

        loadRestaurants();
    }

    private void loadRestaurants() {
        List<Restaurant> restaurants = restaurantRepo.restaurantDao.getAllRestaurants();
        adapter.setRestaurants(restaurants);
    }

    private void filterRestaurants(String query) {
        if (query.isEmpty()) {
            loadRestaurants();
        } else {
            String searchQuery = "%" + query + "%";
            List<Restaurant> filtered = restaurantRepo.searchRestaurantsByName(searchQuery);
            adapter.setRestaurants(filtered);
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
                    0.0,
                    0.0
            );

            long restaurantId = restaurantRepo.insert(restaurant);

            if (restaurantId != -1) {
                if (!tagsInput.isEmpty()) {
                    String[] tags = tagsInput.split(",");
                    for (String tag : tags) {
                        String tagName = tag.trim();
                        if (!tagName.isEmpty()) {
                            restaurantRepo.addTagToRestaurant(restaurantId, tagName);
                        }
                    }
                }

                loadRestaurants();
                Toast.makeText(this, "Restaurant added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add restaurant", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}