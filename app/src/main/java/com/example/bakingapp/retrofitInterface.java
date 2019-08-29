package com.example.bakingapp;

import com.example.bakingapp.model.RecipeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface retrofitInterface {
    @GET
    Call<RecipeResponse> getRecipeResponse();
}
