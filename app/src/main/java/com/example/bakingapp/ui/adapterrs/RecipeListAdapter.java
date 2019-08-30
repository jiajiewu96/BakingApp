package com.example.bakingapp.ui.adapterrs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.model.Recipe;

import java.util.ArrayList;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {

    private ArrayList<Recipe> recipes;

    public void setRecipes(ArrayList<Recipe> recipes){
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public RecipeListAdapter(){

    }


    @NonNull
    @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_list, parent, false);
        return new RecipeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class RecipeListViewHolder extends RecyclerView.ViewHolder{
        TextView recipeTitle;
        RecipeListViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeTitle = (TextView) itemView.findViewById(R.id.tv_recipe_title);
        }
    }
}
