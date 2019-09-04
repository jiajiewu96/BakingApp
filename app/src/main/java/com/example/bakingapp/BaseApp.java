package com.example.bakingapp;

import android.app.Application;
import com.example.bakingapp.data.FavoritesDatabase;
import com.example.bakingapp.data.RecipeRepository;

public class BaseApp extends Application {

    public FavoritesDatabase getDatabase(){
        return FavoritesDatabase.getInstance(this);
    }

    public RecipeRepository getRepository(){
        return RecipeRepository.getInstance(getDatabase());
    }
}
