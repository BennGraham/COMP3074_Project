package com.example.comp3074_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.comp3074_project.room.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends BaseAdapter {
    private List<Restaurant> restaurants;
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
    public Restaurant getItem(int position) {
        return restaurants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return restaurants.get(position).getId();
    }

   @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.restaurant_list_item, parent, false);
            holder = new ViewHolder();
            holder.restaurantImage = convertView.findViewById(R.id.restaurantImage);
            holder.restaurantName = convertView.findViewById(R.id.restaurantName);
            holder.ratingBar = convertView.findViewById(R.id.ratingBar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Restaurant restaurant = restaurants.get(position);

        holder.restaurantName.setText(restaurant.getName());
        holder.ratingBar.setRating(restaurant.getRating());

        // todo: use real image instead of placeholder
        holder.restaurantImage.setImageResource(android.R.drawable.ic_menu_gallery);

        return convertView;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        ImageView restaurantImage;
        TextView restaurantName;
        RatingBar ratingBar;
    }
}