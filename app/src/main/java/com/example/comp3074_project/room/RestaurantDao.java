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

    @Query("SELECT * FROM restaurant_table WHERE restaurant_id = :restaurantId")
    Restaurant getRestaurantById(int restaurantId);

    @Query("SELECT * FROM restaurant_table WHERE name LIKE :searchQuery ORDER BY name ASC")
    List<Restaurant> searchRestaurantsByNameQuery(String searchQuery);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRestaurantTag(RestaurantTag restaurantTag);

    @Query("DELETE FROM restaurant_tag_table WHERE restaurant_id = :restaurantId AND tag_id = :tagId")
    void deleteRestaurantTag(long restaurantId, long tagId);


}
