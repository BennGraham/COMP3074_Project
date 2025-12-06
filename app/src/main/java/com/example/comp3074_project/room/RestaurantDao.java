package com.example.comp3074_project.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Restaurant restaurant);

    @Update
    void update(Restaurant restaurant);

    @Delete
    void delete(Restaurant restaurant);

    @Query("DELETE FROM restaurant_table")
    void deleteAllRestaurants();

    @Query("SELECT * FROM restaurant_table ORDER BY name ASC")
    LiveData<List<Restaurant>> getAllRestaurants();

    @Transaction
    @Query("SELECT * FROM restaurant_table ORDER BY name ASC")
    LiveData<List<RestaurantWithTags>> getAllRestaurantsWithTagsLive();

    @Query("SELECT * FROM restaurant_table WHERE restaurant_id = :restaurantId")
    Restaurant getRestaurantById(long restaurantId);

    @Query("SELECT * FROM restaurant_table WHERE name LIKE :searchQuery ORDER BY name ASC")
    LiveData<List<Restaurant>> searchRestaurantsByName(String searchQuery);

    @Transaction
    @Query("SELECT DISTINCT r.* FROM restaurant_table r " +
            "LEFT JOIN restaurant_tag_table rt ON r.restaurant_id = rt.restaurant_id " +
            "LEFT JOIN tag_table t ON rt.tag_id = t.tag_id " +
            "WHERE r.name LIKE :query OR t.tag_name LIKE :query " +
            "ORDER BY r.name ASC")
    LiveData<List<RestaurantWithTags>> searchRestaurantsWithTagsByNameOrTag(String query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRestaurantTag(RestaurantTag restaurantTag);

    @Query("DELETE FROM restaurant_tag_table WHERE restaurant_id = :restaurantId AND tag_id = :tagId")
    void deleteRestaurantTag(long restaurantId, long tagId);

    @Transaction // need to remember that this is required for querying relations
    @Query("SELECT * FROM restaurant_table WHERE restaurant_id = :restaurantId")
    RestaurantWithTags getRestaurantWithTags(long restaurantId);

    @Transaction
    @Query("SELECT * FROM restaurant_table ORDER BY name ASC")
    List<RestaurantWithTags> getAllRestaurantsWithTags();

    @Query("SELECT DISTINCT r.* FROM restaurant_table r " +
            "LEFT JOIN restaurant_tag_table rt ON r.restaurant_id = rt.restaurant_id " +
            "LEFT JOIN tag_table t ON rt.tag_id = t.tag_id " +
            "WHERE r.name LIKE :query OR t.tag_name LIKE :query " +
            "ORDER BY r.name ASC")
    LiveData<List<Restaurant>> searchRestaurantsByNameOrTag(String query);
}
