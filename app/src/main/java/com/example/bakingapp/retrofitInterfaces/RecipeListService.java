package com.example.bakingapp.retrofitInterfaces;

import com.example.bakingapp.model.RecipeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RecipeListService {
    @GET("topher/2017/May/59121517_baking/baking.json/")
    Call<RecipeResponse> getRecipeResponse();
}
