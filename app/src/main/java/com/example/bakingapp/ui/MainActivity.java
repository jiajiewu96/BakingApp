package com.example.bakingapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.bakingapp.R;
import com.example.bakingapp.model.Recipe;

public class MainActivity extends AppCompatActivity implements RecipeListFragment.OnRecipeListClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecipeListFragment recipeListFragment = new RecipeListFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.recipie_contianer, recipeListFragment)
                .commit();
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        Toast.makeText(this, recipe.getName(), Toast.LENGTH_SHORT).show();
    }
}
