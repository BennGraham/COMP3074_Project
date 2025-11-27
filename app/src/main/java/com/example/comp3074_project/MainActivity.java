package com.example.comp3074_project;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

        // Load all restaurants
        loadRestaurants();
    }

    private void loadRestaurants() {
        List<Restaurant> restaurants = restaurantRepo.getAllRestaurants();
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
}