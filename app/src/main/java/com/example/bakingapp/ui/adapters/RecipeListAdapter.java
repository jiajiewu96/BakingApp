package com.example.bakingapp.ui.adapters;

import android.util.Log;
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

    private final RecipeClickHandler mClickHandler;

    public interface RecipeClickHandler{
        void onRecipeClick(Recipe recipe);
    }

    public void setRecipes(ArrayList<Recipe> recipes){
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public RecipeListAdapter(RecipeClickHandler clickHandler){
        recipes = new ArrayList<>();
        mClickHandler = clickHandler;
    }


    @NonNull
    @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_list, parent, false);
        return new RecipeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListViewHolder holder, int position) {
        String recipeName = recipes.get(position).getName();
        holder.recipeTitle.setText(recipeName);
        Log.d("Tag", "onBindViewHolder:  set title");
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class RecipeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView recipeTitle;
        RecipeListViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeTitle = (TextView) itemView.findViewById(R.id.tv_recipe_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onRecipeClick(recipes.get(adapterPosition));
        }
    }
}
