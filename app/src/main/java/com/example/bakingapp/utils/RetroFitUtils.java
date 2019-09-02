package com.example.bakingapp.utils;

import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.retrofitInterfaces.RecipeListService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitUtils {

    private static final String RECIPE_URL =
            "https://d17h27t6h515a5.cloudfront.net/";

    private static Retrofit buildRecipeUrl(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RECIPE_URL)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static Call<List<Recipe>> loadRecipies(){
        Retrofit retrofit = buildRecipeUrl();
        RecipeListService recipeListService = retrofit.create(RecipeListService.class);
        return recipeListService.getRecipeResponse();
    }

    private static OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        okhttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);
        return okhttpClientBuilder.build();
    }
}
