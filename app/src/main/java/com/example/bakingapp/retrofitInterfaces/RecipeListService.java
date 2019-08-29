package com.example.bakingapp.retrofitInterfaces;

import com.example.bakingapp.model.RecipeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RecipeListService {
    @GET
    Call<RecipeResponse> getRecipeResponse();
}
