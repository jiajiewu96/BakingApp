package com.example.bakingapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.example.bakingapp.R;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;

import static com.example.bakingapp.utils.Consts.RECIPE_KEY;
import static com.example.bakingapp.utils.Consts.RECIPE_STEP_DETAIL_TRANSACTION_NAME;
import static com.example.bakingapp.utils.Consts.RECIPE_STEP_TRANSACTION_NAME;
import static com.example.bakingapp.utils.Consts.STEP_KEY;

public class MainActivity extends AppCompatActivity implements RecipeListFragment.OnRecipeListClickListener, RecipeStepListFragment.OnStepClickedListener {

    private final FragmentManager mFragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            RecipeListFragment recipeListFragment = new RecipeListFragment();
            mFragmentManager.beginTransaction()
                    .add(R.id.recipie_contianer, recipeListFragment)
                    .commit();
        }
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_KEY,recipe);

        RecipeStepListFragment recipeStepListFragment = new RecipeStepListFragment();
        recipeStepListFragment.setArguments(bundle);
        mFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.recipie_contianer, recipeStepListFragment)
                .addToBackStack(RECIPE_STEP_TRANSACTION_NAME)
                .commit();


    }

    @Override
    public void onStepClicked(Step step) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STEP_KEY, step);

        RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
        recipeStepDetailFragment.setArguments(bundle);
        mFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.recipie_contianer, recipeStepDetailFragment)
                .addToBackStack(RECIPE_STEP_DETAIL_TRANSACTION_NAME)
                .commit();
    }
}
