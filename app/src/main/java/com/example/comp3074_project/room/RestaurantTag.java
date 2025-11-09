package com.example.comp3074_project.room;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "restaurant_tag_table",
        primaryKeys = {"restaurant_id", "tag_id" },
        foreignKeys = {
                @ForeignKey(entity = Restaurant.class,
                        parentColumns = "restaurant_id",
                        childColumns = "restaurant_id",
                        // don't forget to add this to join tables to prevent leaving orphans
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Tag.class,
                        parentColumns = "tag_id",
                        childColumns = "tag_id",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index(value = {"tag_id"})
        }
)
public class RestaurantTag {
    private long restaurant_id;
    private long tag_id;

    public RestaurantTag(long restaurant_id, long tag_id){
        this.restaurant_id = restaurant_id;
        this.tag_id = tag_id;
    }


    public long getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(long restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public long getTag_id() {
        return tag_id;
    }

    public void setTag_id(long tag_id) {
        this.tag_id = tag_id;
    }
}
