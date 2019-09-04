package com.example.bakingapp.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bakingapp.BaseApp;
import com.example.bakingapp.model.Recipe;

import java.util.List;

public class FavoriteListViewModel extends AndroidViewModel {
    private static RecipeRepository mRecipeRepository;
    private static LiveData<List<Recipe>> mFavorites;
    public FavoriteListViewModel(@NonNull Application application) {
        super(application);
        mRecipeRepository = ((BaseApp) application).getRepository();
        mFavorites = mRecipeRepository.getRecipesFromFavorites();
    }
    public LiveData<List<Recipe>> getfavorites(){
        return mFavorites;
    }
}
