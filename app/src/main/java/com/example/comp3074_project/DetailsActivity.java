package com.example.comp3074_project;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.comp3074_project.room.Rating;
import com.example.comp3074_project.room.Restaurant;
import com.example.comp3074_project.room.RestaurantRepo;
import com.example.comp3074_project.room.RestaurantWithTags;
import com.example.comp3074_project.room.Tag;
import com.google.android.material.chip.Chip;

public class DetailsActivity extends AppCompatActivity {

    private RestaurantRepo restaurantRepo;
    private long restaurantId;
    private Restaurant restaurant;
    private RestaurantWithTags restaurantWithTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.header_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

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

        Button detailsBtnFavourite = findViewById(R.id.detailsBtnFavourite);
        updateFavouriteButton(detailsBtnFavourite, restaurant.getFavourite());

        detailsBtnFavourite.setOnClickListener(v -> {
            boolean newFavorite = !restaurant.getFavourite();
            restaurant.setFavourite(newFavorite);
            restaurantRepo.toggleFavourite(restaurantId, newFavorite);
            updateFavouriteButton(detailsBtnFavourite, newFavorite);
        });

        Button detailsBtnEdit = findViewById(R.id.detailsBtnEdit);
        detailsBtnEdit.setOnClickListener(v -> showEditRestaurantDialog());
    }


    private void loadRestaurantData() {
        restaurantWithTags = restaurantRepo.getRestaurantWithTagsById(restaurantId);

        if (restaurantWithTags == null || restaurantWithTags.restaurant == null) {
            finish();
            return;
        }

        restaurant = restaurantWithTags.restaurant;
        TextView detailsName = findViewById(R.id.detailsRestaurantName);
        detailsName.setText(restaurant.getName());
        RatingBar ratingBar = findViewById(R.id.detailsRatingBar);
        ratingBar.setRating(restaurant.getRating());

        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            if (fromUser) {
                Rating newRating = new Rating(
                        restaurantId,
                        rating,
                        System.currentTimeMillis()
                );
                restaurantRepo.insertRating(newRating);

                restaurantRepo.updateRestaurantRating(restaurantId);
            }
        });

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
                chip.setCloseIconVisible(true);
                chip.setOnCloseIconClickListener(v -> {
                    restaurantRepo.removeTagFromRestaurant(restaurantId, tag.getId());
                    tagsContainer.removeView(chip);
                    restaurantWithTags.tags.remove(tag);
                });

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 8, 8);
                chip.setLayoutParams(params);

                tagsContainer.addView(chip);
            }
        }

        Button addTagButton = findViewById(R.id.detailsBtnAddTag);
        addTagButton.setOnClickListener(v -> showAddTagDialog());

        Button shareButton = findViewById(R.id.detailsBtnShare);
        shareButton.setOnClickListener(v -> {
            String shareText = "Check out this restaurant!\n\n"
                    + "Name: " + restaurant.getName() + "\n"
                    + "Address: " + restaurant.getAddress() + "\n"
                    + "Phone: " + restaurant.getPhoneNumber() + "\n"
                    + "Rating: " + restaurant.getRating() + "/5\n\n"
                    + "Description: " + restaurant.getDescription();

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this restaurant: " + restaurant.getName());
            intent.putExtra(Intent.EXTRA_TEXT, shareText);

            startActivity(Intent.createChooser(intent, "Share via"));
        });
    }

    private void updateFavouriteButton(Button button, boolean isFavourite) {
        if (isFavourite) {
            button.setText("Unfavourite");
        } else {
            button.setText("Favourite");
        }
    }

    private void showEditRestaurantDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_restaurant_dialog, null);
        builder.setView(dialogView);

        EditText etName = dialogView.findViewById(R.id.dialogName);
        EditText etAddress = dialogView.findViewById(R.id.etAddress);
        EditText etPhone = dialogView.findViewById(R.id.etPhone);
        EditText etDescription = dialogView.findViewById(R.id.etDescription);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        dialogView.findViewById(R.id.dialogTags).setVisibility(View.GONE);
        dialogView.findViewById(R.id.tagsLabel).setVisibility(View.GONE);

        etName.setText(restaurant.getName());
        etAddress.setText(restaurant.getAddress());
        etPhone.setText(restaurant.getPhoneNumber());
        etDescription.setText(restaurant.getDescription());
        ratingBar.setRating(restaurant.getRating());

        String originalAddress = restaurant.getAddress();

        builder.setTitle("Edit Restaurant");
        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String address = etAddress.getText().toString().trim();

            if (name.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Name and Address are required", Toast.LENGTH_SHORT).show();
                return;
            }

            double latitude = restaurant.getLatitude();
            double longitude = restaurant.getLongitude();

            if (!address.equals(originalAddress)) {
                Geocoder geocoder = new Geocoder(this);
                try {
                    List<Address> addresses = geocoder.getFromLocationName(address, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        latitude = addresses.get(0).getLatitude();
                        longitude = addresses.get(0).getLongitude();
                    }
                } catch (IOException e) {
                    // todo: fallback logic?
                }
            }

            String phone = etPhone.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            float rating = ratingBar.getRating();

            restaurant.setName(name);
            restaurant.setAddress(address);
            restaurant.setPhoneNumber(phone);
            restaurant.setDescription(description);
            restaurant.setRating(rating);
            restaurant.setLatitude(latitude);
            restaurant.setLongitude(longitude);

            restaurantRepo.update(restaurant);

            Toast.makeText(this, "Restaurant updated", Toast.LENGTH_SHORT).show();

            TextView detailsName = findViewById(R.id.detailsRestaurantName);
            detailsName.setText(name);
            TextView detailsAddress = findViewById(R.id.detailsAddress);
            detailsAddress.setText(address);
            TextView detailsPhone = findViewById(R.id.detailsPhoneNumber);
            detailsPhone.setText(phone.isEmpty() ? "Not provided" : phone);
            TextView detailsDescription = findViewById(R.id.detailsDescription);
            detailsDescription.setText(description.isEmpty() ? "No description available" : description);
            RatingBar detailsRatingBar = findViewById(R.id.detailsRatingBar);
            detailsRatingBar.setRating(rating);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAddTagDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Tag");

        EditText input = new EditText(this);
        input.setHint("Enter tag name");
        input.setInputType(android.text.InputType.TYPE_CLASS_TEXT);

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(48, 16, 48, 0);
        container.addView(input);

        builder.setView(container);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String tagName = input.getText().toString().trim();
            if (!tagName.isEmpty()) {
                restaurantRepo.addTagToRestaurant(restaurantId, tagName);

                LinearLayout tagsContainer = findViewById(R.id.detailsTags);
                Tag newTag = new Tag(tagName);
                Chip chip = new Chip(this);
                chip.setText(tagName);
                chip.setChipBackgroundColorResource(android.R.color.darker_gray);
                chip.setTextColor(ContextCompat.getColor(this, android.R.color.black));
                chip.setCloseIconVisible(true);
                chip.setOnCloseIconClickListener(v -> {
                    restaurantRepo.removeTagFromRestaurant(restaurantId, newTag.getId());
                    tagsContainer.removeView(chip);
                });

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 8, 8);
                chip.setLayoutParams(params);

                tagsContainer.addView(chip);

                Toast.makeText(this, "Tag added", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}