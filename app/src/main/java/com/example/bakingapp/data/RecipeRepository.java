package com.example.bakingapp.data;

import androidx.lifecycle.LiveData;

import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.utils.RetroFitUtils;

import java.util.List;

import retrofit2.Call;

public class RecipeRepository {

    private static final Object LOCK = new Object();
    private static RecipeRepository sInstance;
    private final FavoritesDatabase mDatabase;

    private RecipeRepository(final FavoritesDatabase recipeDatabase) {
        mDatabase = recipeDatabase;
    }

    public synchronized static RecipeRepository getInstance(FavoritesDatabase favoritesDatabase) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new RecipeRepository(favoritesDatabase);
                }
            }
        }
        return sInstance;
    }

    public Call<List<Recipe>> getRecipiesFromJSON() {
        return RetroFitUtils.loadRecipies();
    }

    public LiveData<List<Recipe>> getRecipesFromFavorites() {
        return mDatabase.favoritesDao().loadAllRecipes();
    }

    public void addRecipeToFavorites(Recipe recipe) {
        mDatabase.favoritesDao().insertRecipe(recipe);
    }

    public void removeRecipeToFavorites(Recipe recipe) {
        mDatabase.favoritesDao().deleteRecipe(recipe);
    }

    public boolean checkForRecipeInDb(Recipe recipe) {
        return mDatabase.favoritesDao().checkForRecipe(recipe.getId()) != null;
    }
    public List<Recipe> loadRecipesFromWidget(){
        return mDatabase.favoritesDao().loadRecipesForWidget();
    }
}
