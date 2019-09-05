package com.example.bakingapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.bakingapp.BaseApp;
import com.example.bakingapp.R;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;
import com.example.bakingapp.ui.adapters.TabAdapter;
import com.example.bakingapp.ui.fragmentInterfaces.CommonFragmentInterfaces;
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
        CommonFragmentInterfaces {

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
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RECIPE_KEY, recipe);
        startActivity(intent);
    }

    @Override
    public void onFragmentChangedListener(String title) {
        getSupportActionBar().setTitle(title);
    }
}
