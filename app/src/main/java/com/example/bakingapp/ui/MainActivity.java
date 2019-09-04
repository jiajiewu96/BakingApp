package com.example.bakingapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.bakingapp.R;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;
import com.example.bakingapp.ui.adapters.TabAdapter;
import com.example.bakingapp.widget.IngredientWidgetService;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import static com.example.bakingapp.utils.Consts.FLAG_NEXT;
import static com.example.bakingapp.utils.Consts.FLAG_PREVIOUS;
import static com.example.bakingapp.utils.Consts.POSITION_KEY;
import static com.example.bakingapp.utils.Consts.RECIPE_KEY;
import static com.example.bakingapp.utils.Consts.RECIPE_STEP_DETAIL_TRANSACTION_NAME;
import static com.example.bakingapp.utils.Consts.RECIPE_STEP_TRANSACTION_NAME;
import static com.example.bakingapp.utils.Consts.STEP_KEY;

public class MainActivity extends AppCompatActivity implements RecipeListFragment.OnRecipeListClickListener,
        RecipeStepListFragment.OnStepClickedListener,
        RecipeStepDetailFragment.OnStepChangeClickListener {

    private final FragmentManager mFragmentManager = getSupportFragmentManager();
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabAdapter tabAdapter = new TabAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(tabAdapter);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        configureAppWidget();
    }

    private void configureAppWidget() {

        Intent serviceIntent = new Intent(this, IngredientWidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.ingredients_widget);
        views.setRemoteAdapter(R.id.ingredient_widget_list, serviceIntent);
        views.setEmptyView(R.id.ingredient_widget_list, R.id.tv_widget_empty);

    }

    @Override
    public void onRecipeSelected(Recipe recipe) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_KEY, recipe);

        RecipeStepListFragment recipeStepListFragment = new RecipeStepListFragment();
        recipeStepListFragment.setArguments(bundle);
        mFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.recipe_container, recipeStepListFragment)
                .addToBackStack(RECIPE_STEP_TRANSACTION_NAME)
                .commit();


    }

    @Override
    public void onStepClicked(ArrayList<Step> steps, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(STEP_KEY, steps);
        bundle.putInt(POSITION_KEY, position);

        RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
        recipeStepDetailFragment.setArguments(bundle);
        mFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.recipe_container, recipeStepDetailFragment)
                .addToBackStack(RECIPE_STEP_DETAIL_TRANSACTION_NAME)
                .commit();
    }

    @Override
    public void onStepChanged(int flag, int position, ArrayList<Step> steps) {
        if (flag == FLAG_NEXT) {
            position++;
        } else if (flag == FLAG_PREVIOUS) {
            position--;
        }
        if (position >= 0 && position < steps.size()) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(STEP_KEY, steps);
            bundle.putInt(POSITION_KEY, position);
            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setArguments(bundle);
            mFragmentManager.popBackStack();
            mFragmentManager.beginTransaction()
                    .replace(R.id.recipe_container, recipeStepDetailFragment)
                    .addToBackStack(RECIPE_STEP_DETAIL_TRANSACTION_NAME)
                    .commit();
        }
    }
}
