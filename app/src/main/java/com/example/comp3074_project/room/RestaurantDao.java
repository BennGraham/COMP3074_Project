package com.example.comp3074_project.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Restaurant restaurant);

    @Update
    void update(Restaurant restaurant);

    @Delete
    void delete(Restaurant restaurant);

    @Query("DELETE FROM restaurant_table")
    void deleteAllRestaurants();

    @Query("SELECT * FROM restaurant_table")
    List<Restaurant> getAllRestaurants();

}
