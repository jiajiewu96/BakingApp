package com.example.bakingapp.data;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.bakingapp.model.Recipe;

@Database(entities = {Recipe.class}, version = 3, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class FavoritesDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static FavoritesDatabase sInstance;
    private static final String DATABASE_NAME = "favorites_database";

    public static FavoritesDatabase getInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavoritesDatabase.class, FavoritesDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return sInstance;
    }

    public abstract FavoritesDao favoritesDao();
}
