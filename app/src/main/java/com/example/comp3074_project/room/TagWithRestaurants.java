package com.example.comp3074_project.room;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class TagWithRestaurants {
    @Embedded
    public Tag tag;

    @Relation(
            parentColumn = "tag_id",
            entityColumn = "restaurant_id",
            associateBy = @Junction(
                    value = RestaurantTag.class,
                    parentColumn = "tag_id",
                    entityColumn = "restaurant_id"
            )
    )
    public List<Restaurant> restaurants;

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}
