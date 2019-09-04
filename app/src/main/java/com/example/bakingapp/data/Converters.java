package com.example.bakingapp.data;

import androidx.room.TypeConverter;

import com.example.bakingapp.model.Ingredients;
import com.example.bakingapp.model.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Converters {
    @TypeConverter
    public String fromIngredientsListToString(ArrayList<Ingredients> ingredients){
        if(ingredients == null){
            return (null);
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Ingredients>>(){}.getType();
        return gson.toJson(ingredients, listType);
    }
    @TypeConverter
    public ArrayList<Ingredients> toIngredientList(String ingredientListString){
        if(ingredientListString == null){
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Ingredients>>(){}.getType();
        return gson.fromJson(ingredientListString, type);
    }
    @TypeConverter
    public String fromStepListToString(ArrayList<Step> steps){
        if(steps == null){
            return (null);
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Step>>(){}.getType();
        return gson.toJson(steps, listType);
    }
    @TypeConverter
    public ArrayList<Step> toStepList(String stepListString){
        if(stepListString == null){
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Step>>(){}.getType();
        return gson.fromJson(stepListString, type);
    }
}
