package com.example.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredients implements Parcelable {
    private float quantity;
    private String measure;
    private String ingredient;

    protected Ingredients(Parcel in) {
        quantity = in.readFloat();
        measure = in.readString();
        ingredient = in.readString();
    }

    public Ingredients(int quantity, String measure, String ingredient){
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }

    public float getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return makeQuantity(measure);
    }

    public String getIngredient() {
        return ingredient;
    }

    private String makeQuantity(String measure) {
        String measureConversion;
        switch (measure){
            case("G"):
                measureConversion = " Grams";
                break;
            case("TBLSP"):
                measureConversion = " Table Spoons";
                break;
            case("TSP"):
                measureConversion =  " Teaspoons";
                break;
            case("K"):
                measureConversion = " Kilogram";
                break;
            case ("CUP"):
                measureConversion = " Cups";
                break;
            default:
                measureConversion = "";
                break;
        }
        return measureConversion;
    }

}
