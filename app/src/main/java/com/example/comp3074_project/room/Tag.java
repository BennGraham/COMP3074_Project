package com.example.comp3074_project.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "tag_table")
public class Tag {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tag_id")
    private long id;

    @NotNull
    @ColumnInfo(name = "tag_name")
    private String tagName;

    public Tag(@NotNull String tagName) {
        this.tagName = tagName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public @NotNull String getTagName() {
        return tagName;
    }

    public void setTagName(@NotNull String tagName) {
        this.tagName = tagName;
    }
}
