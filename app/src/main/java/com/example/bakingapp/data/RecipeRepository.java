package com.example.bakingapp.data;

import com.example.bakingapp.model.RecipeResponse;
import com.example.bakingapp.utils.RetroFitUtils;

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

    public Call<RecipeResponse> getRecipiesFromJSON(){
        return RetroFitUtils.loadRecipies();
    }


}
