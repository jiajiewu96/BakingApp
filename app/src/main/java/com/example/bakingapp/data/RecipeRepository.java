package com.example.bakingapp.data;

import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.utils.RetroFitUtils;

import java.util.List;

import retrofit2.Call;

public class RecipeRepository {

    private static final Object LOCK = new Object();
    private static RecipeRepository sInstance;

    public synchronized static RecipeRepository getInstance(){
        if(sInstance == null){
            synchronized (LOCK){
                if(sInstance == null){
                    sInstance = new RecipeRepository();
                }
            }
        }
        return sInstance;
    }

    public Call<List<Recipe>> getRecipiesFromJSON(){
        return RetroFitUtils.loadRecipies();
    }


}
