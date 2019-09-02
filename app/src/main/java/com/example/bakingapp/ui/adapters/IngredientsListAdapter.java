package com.example.bakingapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.model.Ingredients;

import java.util.ArrayList;

public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.IngredientsListViewHolder> {

    private ArrayList<Ingredients> mIngredients;

    public void setIngredients(ArrayList<Ingredients> ingredients){
        mIngredients = ingredients;
    }

    public IngredientsListAdapter() {
        mIngredients = new ArrayList<>();
    }

    @NonNull
    @Override
    public IngredientsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredients, parent, false);
        return new IngredientsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsListViewHolder holder, int position) {
        String ingredient = convertFirstLetterToUpper(mIngredients.get(position).getIngredient());
        String quantity = makeQuantity(position);
        holder.ingredient.setText(ingredient);
        holder.quantity.setText(quantity);
    }

    private String makeQuantity(int position) {
        return mIngredients.get(position).getQuantity() + mIngredients.get(position).getMeasure();
    }

    private String convertFirstLetterToUpper(String ingredient) {
        return ingredient.substring(0, 1).toUpperCase() + ingredient.substring(1);
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    class IngredientsListViewHolder extends RecyclerView.ViewHolder {
        TextView ingredient;
        TextView quantity;

        IngredientsListViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredient = (TextView) itemView.findViewById(R.id.tv_ingredient);
            quantity = (TextView) itemView.findViewById(R.id.tv_quantity);
        }
    }
}
