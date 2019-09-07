package com.example.bakingapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bakingapp.model.Ingredients;
import com.example.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface FavoritesDao {
    @Query("SELECT * FROM recipes")
    LiveData<List<Recipe>> loadAllRecipes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(Recipe recipe);

    @Delete
    void deleteRecipe(Recipe recipe);

    @Query("SELECT * FROM recipes WHERE id = :queryId")
    Recipe checkForRecipe(int queryId);

}
