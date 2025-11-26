package com.example.comp3074_project.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RatingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Rating rating);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Rating> ratings);

    @Update
    void update(Rating rating);

    @Delete
    void delete(Rating rating);

    @Query("SELECT * FROM rating_table WHERE restaurant_id = :restaurantId ORDER BY updated_at DESC")
    List<Rating> getRatingsByRestaurantId(long restaurantId);

    @Query("SELECT AVG(rating_value) FROM rating_table WHERE restaurant_id = :restaurantId")
    Float getAverageRatingByRestaurantId(long restaurantId);

    @Query("SELECT * FROM rating_table WHERE rating_id = :ratingId")
    Rating getRatingById(long ratingId);

    @Query("SELECT * FROM rating_table ORDER BY updated_at DESC")
    List<Rating> getAllRatings();

    @Query("DELETE FROM rating_table WHERE restaurant_id = :restaurantId")
    void deleteRatingsByRestaurantId(long restaurantId);

    @Query("DELETE FROM rating_table")
    void deleteAllRatings();
}