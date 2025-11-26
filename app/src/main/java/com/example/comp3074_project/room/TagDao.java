package com.example.comp3074_project.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Tag tag);

    @Query("SELECT * FROM tag_table")
    List<Tag> getAllTags();

    @Query("SELECT * FROM tag_table WHERE tag_id = :tagId")
    Tag getTagById(int tagId);

    @Query("SELECT * FROM tag_table WHERE tag_name = :tagName")
    long getTagIdByName(String tagName);

    @Transaction
    @Query("SELECT * from tag_table WHERE tag_id = :tagId")
    TagWithRestaurants getTagWithRestaurants(long tagId);

    @Transaction
    @Query("SELECT * from tag_table ORDER BY tag_name ASC")
    List<TagWithRestaurants> getAllTagsWithRestaurants();

    @Transaction
    @Query("SELECT T.* FROM tag_table AS T WHERE T.tag_name = :tagName")
    TagWithRestaurants getTagWithRestaurantsByTagName(String tagName);
}
