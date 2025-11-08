package com.example.comp3074_project.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "rating_table")
public class Rating {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rating_id")
    private long id;

    @ColumnInfo(name = "restaurant_id")
    private long restaurantId;

    @NotNull
    @ColumnInfo(name = "rating_value")
    private float ratingValue;

    @ColumnInfo(name = "updated_at")
    private long updatedAt;

    public Rating(long restaurantId, @NotNull float ratingValue, long updatedAt) {
        this.restaurantId = restaurantId;
        this.ratingValue = ratingValue;
        this.updatedAt = updatedAt;
    }

    public Rating() {
        this.updatedAt = System.currentTimeMillis();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(float ratingValue) {
        if (ratingValue > 5 || ratingValue < 1) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.ratingValue = ratingValue;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }
}
