package com.example.bakingapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.data.RecipeRepository;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.RecipeResponse;
import com.example.bakingapp.ui.adapterrs.RecipeListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListFragment extends Fragment {

    private RecipeListAdapter mRecipeListAdapter;
    private TextView errorTextView;

    public RecipeListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        RecyclerView recipeListRecycler = (RecyclerView) rootView.findViewById(R.id.recycler_view_recipe_list);
        errorTextView = rootView.findViewById(R.id.tv_error);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recipeListRecycler.setLayoutManager(layoutManager);

        mRecipeListAdapter = new RecipeListAdapter();
        recipeListRecycler.setAdapter(mRecipeListAdapter);

        loadRecipesFromJSON();


        return rootView;
    }

    private void loadRecipesFromJSON() {
        RecipeRepository repository = RecipeRepository.getInstance();
        Call<RecipeResponse> responseCall = repository.getRecipiesFromJSON();

        responseCall.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if(!response.isSuccessful()){
                    showError();
                    return;
                }
                ArrayList<Recipe> recipes;
                if(response.body() != null){
                    recipes = (ArrayList<Recipe>) response.body().getRecipes();
                    mRecipeListAdapter.setRecipes(recipes);
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                showError();
            }
        });
    }

    private void showError() {
        errorTextView.setText(getString(R.string.error_message));
    }
}
