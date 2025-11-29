package com.example.comp3074_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.comp3074_project.room.Restaurant;
import com.example.comp3074_project.room.RestaurantRepo;
import com.example.comp3074_project.room.RestaurantWithTags;
import com.example.comp3074_project.room.Tag;
import com.google.android.material.chip.Chip;

public class DetailsActivity extends AppCompatActivity {

    private RestaurantRepo restaurantRepo;
    private long restaurantId;
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);

        restaurantId = getIntent().getLongExtra("RESTAURANT_ID", -1);

        if (restaurantId == -1) {
            finish();
            return;
        }

        restaurantRepo = new RestaurantRepo(getApplication());
        loadRestaurantData();

        Button detailsBtnGetDirections = findViewById(R.id.detailsBtnGetDirections);
        detailsBtnGetDirections.setOnClickListener(v -> {
            if (restaurant != null) {
                Intent intent = new Intent(DetailsActivity.this, NavigationActivity.class);
                intent.putExtra(NavigationActivity.EXTRA_LATITUDE, restaurant.getLatitude());
                intent.putExtra(NavigationActivity.EXTRA_LONGITUDE, restaurant.getLongitude());
                intent.putExtra(NavigationActivity.EXTRA_LABEL, restaurant.getName());
                startActivity(intent);
            }
        });
    }

    private void loadRestaurantData() {
        RestaurantWithTags restaurantWithTags = restaurantRepo.getRestaurantWithTagsById(restaurantId);

        if (restaurantWithTags == null || restaurantWithTags.restaurant == null) {
            finish();
            return;
        }

        restaurant = restaurantWithTags.restaurant;
        TextView detailsName = findViewById(R.id.detailsRestaurantName);
        detailsName.setText(restaurant.getName());
        RatingBar ratingBar = findViewById(R.id.detailsRatingBar);
        ratingBar.setRating(restaurant.getRating());
        TextView detailsAddress = findViewById(R.id.detailsAddress);
        detailsAddress.setText(restaurant.getAddress());
        TextView detailsPhone = findViewById(R.id.detailsPhoneNumber);

        if (restaurant.getPhoneNumber() != null && !restaurant.getPhoneNumber().isEmpty()) {
            detailsPhone.setText(restaurant.getPhoneNumber());
        } else {
            detailsPhone.setText("Not provided");
        }

        TextView detailsDescription = findViewById(R.id.detailsDescription);
        if (restaurant.getDescription() != null && !restaurant.getDescription().isEmpty()) {
            detailsDescription.setText(restaurant.getDescription());
        } else {
            detailsDescription.setText("No description available");
        }

        LinearLayout tagsContainer = findViewById(R.id.detailsTags);
        tagsContainer.removeAllViews();

        if (restaurantWithTags.tags != null && !restaurantWithTags.tags.isEmpty()) {
            for (Tag tag : restaurantWithTags.tags) {
                Chip chip = new Chip(this);
                chip.setText(tag.getTagName());
                chip.setChipBackgroundColorResource(android.R.color.darker_gray);
                chip.setTextColor(ContextCompat.getColor(this, android.R.color.black));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 8, 8);
                chip.setLayoutParams(params);

                tagsContainer.addView(chip);
            }
        }
    }
}