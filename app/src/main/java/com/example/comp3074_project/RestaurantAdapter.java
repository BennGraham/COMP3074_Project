package com.example.comp3074_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.comp3074_project.room.Restaurant;
import com.example.comp3074_project.room.RestaurantWithTags;
import com.example.comp3074_project.room.Tag;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends BaseAdapter {
    private List<RestaurantWithTags> restaurants;
    private final Context context;
    private final LayoutInflater inflater;

    public RestaurantAdapter(Context context) {
        this.context = context;
        this.restaurants = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return restaurants.size();
    }

    @Override
    public RestaurantWithTags getItem(int position) {
        return restaurants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return restaurants.get(position).getRestaurant().getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.restaurant_list_item, parent, false);
            holder = new ViewHolder();
            holder.restaurantName = convertView.findViewById(R.id.restaurantName);
            holder.ratingBar = convertView.findViewById(R.id.ratingBar);
            holder.restaurantAddress = convertView.findViewById(R.id.restaurantAddress);
            holder.tagsContainer = convertView.findViewById(R.id.tagsContainer);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RestaurantWithTags item = restaurants.get(position);

        holder.restaurantName.setText(item.restaurant.getName());
        holder.ratingBar.setRating(item.restaurant.getRating());
        holder.restaurantAddress.setText(item.restaurant.getAddress());

        holder.tagsContainer.removeAllViews();
        if (item.tags != null) {
            for (Tag tag : item.tags) {
                Chip chip = new Chip(context);
                chip.setText(tag.getTagName());
                chip.setChipBackgroundColorResource(android.R.color.darker_gray);
                chip.setClickable(false);
                chip.setTextSize(12);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMarginEnd(8);
                chip.setLayoutParams(params);

                holder.tagsContainer.addView(chip);
            }
        }

        return convertView;
    }

    public void setRestaurants(List<RestaurantWithTags> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        ImageView restaurantImage;
        TextView restaurantName;
        RatingBar ratingBar;
        TextView restaurantAddress;
        LinearLayout tagsContainer;
    }
}