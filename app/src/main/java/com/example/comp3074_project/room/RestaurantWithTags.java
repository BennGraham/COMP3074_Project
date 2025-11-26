package com.example.comp3074_project.room;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class RestaurantWithTags {
    @Embedded
    public Restaurant restaurant;

    @Relation(
            parentColumn = "restaurant_id",
            entityColumn = "tag_id",
            associateBy = @Junction(
                    value = RestaurantTag.class,
                    parentColumn = "restaurant_id",
                    entityColumn = "tag_id"
            )
    )
    public List<Tag> tags;

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
