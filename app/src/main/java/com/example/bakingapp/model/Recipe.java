package com.example.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "recipes")
public class Recipe implements Parcelable {
    @PrimaryKey
    private int id;
    private String name;
    private ArrayList<Ingredients> ingredients;
    private ArrayList<Step> steps;
    private int servings;
    private String image;
    private int favorite;

    @Ignore
    protected Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ingredients = in.createTypedArrayList(Ingredients.CREATOR);
        steps = in.createTypedArrayList(Step.CREATOR);
        servings = in.readInt();
        image = in.readString();
        favorite = in.readInt();
    }
    @Ignore
    public Recipe(){

    }

    public Recipe(int id, String name, ArrayList<Ingredients> ingredients, ArrayList<Step> steps, int servings, String image, int favorite){
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
        this.favorite = favorite;
    }

    @Ignore
    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Ingredients> getIngredients() {
        return ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public int getFavorite(){return favorite;}

    public void setFavorite(int favorite){
        this.favorite = favorite;
    }

    @Override
    @Ignore
    public int describeContents() {
        return 0;
    }

    @Override
    @Ignore
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(steps);
        parcel.writeInt(servings);
        parcel.writeString(image);
        parcel.writeInt(favorite);
    }
}
