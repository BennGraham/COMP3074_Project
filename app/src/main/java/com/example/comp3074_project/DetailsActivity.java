package com.example.comp3074_project;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.comp3074_project.room.Restaurant;
import com.example.comp3074_project.room.RestaurantRepo;
import com.example.comp3074_project.room.RestaurantWithTags;
import com.example.comp3074_project.room.Tag;

public class DetailsActivity extends AppCompatActivity {

    private RestaurantRepo restaurantRepo;
    private long restaurantId;

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
    }

    private void loadRestaurantData() {
        RestaurantWithTags restaurantWithTags = restaurantRepo.getRestaurantWithTagsById(restaurantId);

        if (restaurantWithTags == null || restaurantWithTags.restaurant == null) {
            finish();
            return;
        }

        Restaurant restaurant = restaurantWithTags.restaurant;
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
                TextView tagView = new TextView(this);
                tagView.setText(tag.getTagName());

                tagsContainer.addView(tagView);
            }
        }
    }
}