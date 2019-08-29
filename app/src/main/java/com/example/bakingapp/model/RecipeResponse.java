package com.example.bakingapp.model;

import java.util.ArrayList;
import java.util.List;

public class RecipeResponse {

    private List<Recipe> recipes;

    public RecipeResponse(){
        recipes = new ArrayList<>();
    }
    public List<Recipe> getRecipes() {
        return recipes;
    }
}
