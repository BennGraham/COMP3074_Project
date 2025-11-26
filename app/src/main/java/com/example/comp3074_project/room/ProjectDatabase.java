package com.example.comp3074_project.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Restaurant.class, Tag.class, RestaurantTag.class, Rating.class}, version = 3, exportSchema = false)
public abstract class ProjectDatabase extends RoomDatabase {
    private static final String DB_NAME = "ProjectDatabase";

    public abstract RestaurantDao restaurantDao();
    public abstract TagDao tagDao();
    public abstract RatingDao ratingDao();

    private static volatile ProjectDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ProjectDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    ProjectDatabase.class,
                    DB_NAME
            )
                    .addCallback(callback)
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback callback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NotNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseExecutor.execute(() -> {
                // what to execute when database is created
            });
        }
    };
}
