package com.example.bakingapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.example.bakingapp.BaseApp;
import com.example.bakingapp.R;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;
import com.example.bakingapp.ui.fragmentInterfaces.CommonFragmentInterfaces;
import com.example.bakingapp.utils.Consts;

import java.util.ArrayList;

import static com.example.bakingapp.utils.Consts.FLAG_NEXT;
import static com.example.bakingapp.utils.Consts.FLAG_PREVIOUS;
import static com.example.bakingapp.utils.Consts.POSITION_KEY;
import static com.example.bakingapp.utils.Consts.RECIPE_KEY;
import static com.example.bakingapp.utils.Consts.RECIPE_STEP_DETAIL_TRANSACTION_NAME;
import static com.example.bakingapp.utils.Consts.RECIPE_STEP_TRANSACTION_NAME;
import static com.example.bakingapp.utils.Consts.STEP_KEY;

public class RecipeActivity extends AppCompatActivity implements RecipeStepListFragment.OnStepClickedListener,
        RecipeStepDetailFragment.OnStepChangeClickListener, CommonFragmentInterfaces {

    private final FragmentManager mFragmentManager = getSupportFragmentManager();
    private int mSw;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        mSw = ((BaseApp) this.getApplication()).screenWidthInDp();
        mRecipe = null;
        Bundle bundle = new Bundle();
        if (savedInstanceState != null)
            mRecipe = savedInstanceState.getParcelable(RECIPE_KEY);
        else if (getIntent().getExtras() != null) {
            mRecipe = getIntent().getExtras().getParcelable(RECIPE_KEY);
        }
        bundle.putParcelable(RECIPE_KEY, mRecipe);
        RecipeStepListFragment recipeStepListFragment = new RecipeStepListFragment();
        recipeStepListFragment.setArguments(bundle);
        if (isTabletScreenWidth() && mFragmentManager.getBackStackEntryCount() == 0) {
            ArrayList<Step> steps = mRecipe.getSteps();
            bundle.putParcelableArrayList(STEP_KEY, steps);
            bundle.putInt(POSITION_KEY, 0);

            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.step_list_container, recipeStepListFragment)
                    .add(R.id.step_detail_container, recipeStepDetailFragment)
                    .commit();
        } else if(mFragmentManager.getBackStackEntryCount() == 0) {
            mFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.recipe_container, recipeStepListFragment)
                    .addToBackStack(RECIPE_STEP_TRANSACTION_NAME)
                    .commit();
        }
        Log.d("RecipeActivity", "RecipeActivity" + String.valueOf(mFragmentManager.getBackStackEntryCount()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_KEY, mRecipe);
    }

    @Override
    public void onStepClicked(ArrayList<Step> steps, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(STEP_KEY, steps);
        bundle.putInt(POSITION_KEY, position);
        int containerID = 0;
        if (isTabletScreenWidth()) {
            containerID = R.id.step_detail_container;
        } else {
            containerID = R.id.recipe_container;
        }
        RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
        recipeStepDetailFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(containerID, recipeStepDetailFragment);
        if (!isTabletScreenWidth()) {
            fragmentTransaction.addToBackStack(RECIPE_STEP_DETAIL_TRANSACTION_NAME);
        }

        fragmentTransaction.commit();
    }

    private boolean isTabletScreenWidth() {
        return mSw >= 600;
    }

    @Override
    public void onStepChanged(int flag, int position, ArrayList<Step> steps) {
        if (flag == FLAG_NEXT) {
            position++;
        } else if (flag == FLAG_PREVIOUS) {
            position--;
        }
        int containerID = 0;
        if (isTabletScreenWidth()) {
            containerID = R.id.step_detail_container;
        } else {
            containerID = R.id.recipe_container;
        }
        if (position >= 0 && position < steps.size()) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(STEP_KEY, steps);
            bundle.putInt(POSITION_KEY, position);
            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setArguments(bundle);
            if (!isTabletScreenWidth() && mFragmentManager.getBackStackEntryCount() >0) {
                mFragmentManager.popBackStack();
            }
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(containerID, recipeStepDetailFragment);
            if (!isTabletScreenWidth()) {
                fragmentTransaction.addToBackStack(RECIPE_STEP_DETAIL_TRANSACTION_NAME);
            }
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onFragmentChangedListener(String title) {
        getSupportActionBar().setTitle(title);
    }
}
