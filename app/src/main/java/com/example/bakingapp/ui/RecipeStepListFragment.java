package com.example.bakingapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.ui.adapters.IngredientsListAdapter;
import com.example.bakingapp.ui.adapters.StepAdapter;
import com.example.bakingapp.utils.Consts;

public class RecipeStepListFragment extends Fragment {

    private IngredientsListAdapter mIngredientsListAdapter;
    private StepAdapter mStepAdapter;

    public RecipeStepListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);

        setupRecyclerViews(rootView);

        Bundle bundle = getArguments();
        if(bundle!=null){
            Recipe recipe = (Recipe) bundle.getParcelable(Consts.RECIPE_KEY);
            mStepAdapter.setSteps(recipe.getSteps());
            mIngredientsListAdapter.setIngredients(recipe.getIngredients());
        }

        return rootView;
    }

    private void setupRecyclerViews(View rootView) {
        RecyclerView ingredientsRecycler = (RecyclerView) rootView.findViewById(R.id.recycler_ingredients);
        RecyclerView stepsRecycler = (RecyclerView) rootView.findViewById(R.id.recycler_steps);

        mIngredientsListAdapter = new IngredientsListAdapter();
        ingredientsRecycler.setAdapter(mIngredientsListAdapter);

        mStepAdapter = new StepAdapter();
        stepsRecycler.setAdapter(mStepAdapter);

        LinearLayoutManager ingredientsLayoutManager = new LinearLayoutManager(getActivity()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        LinearLayoutManager stepsLayoutManager = new LinearLayoutManager(getActivity());
        ingredientsRecycler.setLayoutManager(ingredientsLayoutManager);
        stepsRecycler.setLayoutManager(stepsLayoutManager);

        ingredientsRecycler.setNestedScrollingEnabled(true);
        stepsRecycler.setNestedScrollingEnabled(true);
    }
}
