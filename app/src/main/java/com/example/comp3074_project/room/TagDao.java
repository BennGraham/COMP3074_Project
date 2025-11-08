package com.example.comp3074_project.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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
    Tag getTagByName(String tagName);
}
