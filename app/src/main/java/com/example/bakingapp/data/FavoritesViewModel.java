package com.example.bakingapp.data;

import android.app.Application;
import android.graphics.Movie;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.bakingapp.BaseApp;

import com.example.bakingapp.model.Recipe;

public class FavoritesViewModel extends ViewModel {

    private static RecipeRepository mRecipeRepository;

    public FavoritesViewModel(@NonNull Application application, RecipeRepository recipeRepository){
        mRecipeRepository = recipeRepository;
    }

    public void addFavorite(Recipe recipe){
        mRecipeRepository.addRecipeToFavorites(recipe);
    }

    public void removeFavorite(Recipe recipe){
        mRecipeRepository.removeRecipeToFavorites(recipe);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory{
        @NonNull
        private final Application mApplication;

        private final RecipeRepository mRepository;

        public Factory(@NonNull Application application){
            mApplication = application;
            mRepository = ((BaseApp) application).getRepository();
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new FavoritesViewModel(mApplication, mRepository);
        }
    }
}
