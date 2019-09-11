package com.example.bakingapp.widget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.bakingapp.BaseApp;
import com.example.bakingapp.R;
import com.example.bakingapp.data.RecipeRepository;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.ui.MainActivity;
import com.example.bakingapp.ui.adapters.RecipeListAdapter;
import com.example.bakingapp.utils.Consts;
import com.example.bakingapp.widget.IngredientWidgetService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientsWidgetConfigActivity extends AppCompatActivity implements  RecipeListAdapter.RecipeClickHandler {
    private int mSw;
    private RecipeListAdapter mRecipeListAdapter;
    private TextView mErrorTextView;
    private int mAppwidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSw = ((BaseApp) getApplication()).screenWidthInDp();
        setContentView(R.layout.activity_ingredients_widget_config);

        Intent configIntent = getIntent();
        Bundle extras = configIntent.getExtras();
        if(extras != null){
            mAppwidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppwidgetId);
        setResult(RESULT_CANCELED, resultValue);

        if(mAppwidgetId == AppWidgetManager.INVALID_APPWIDGET_ID){
            finish();
        }

        mErrorTextView = (TextView) findViewById(R.id.widget_config_error);
        RecyclerView recipeListRecycler = (RecyclerView) findViewById(R.id.widget_config_recycler);
        if(mSw >= 600){
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            recipeListRecycler.setLayoutManager(layoutManager);
        }else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recipeListRecycler.setLayoutManager(layoutManager);
        }
        mRecipeListAdapter = new RecipeListAdapter(this);
        recipeListRecycler.setAdapter(mRecipeListAdapter);
        loadRecipesFromJson();
    }

    private void loadRecipesFromJson() {
        Application application = this.getApplication();
        RecipeRepository repository = ((BaseApp) application).getRepository();
        Call<List<Recipe>> responseCall = repository.getRecipiesFromJSON();

        responseCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(!response.isSuccessful()){
                    showError();
                    return;
                }
                ArrayList<Recipe> recipes;
                if(response.body() != null){
                    recipes = (ArrayList<Recipe>) response.body();
                    mRecipeListAdapter.setRecipes(recipes);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                showError();
            }
        });
    }

    private void showError() {
        mErrorTextView.setVisibility(View.VISIBLE);
        mErrorTextView.setText(getString(R.string.error_message));
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent serviceIntent = new Intent(this, IngredientWidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppwidgetId);
        serviceIntent.putExtra(Consts.WIDGET_RECIPE_KEY, recipe);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.ingredients_widget);
        views.setOnClickPendingIntent(R.id.ingredients_widget_layout, pendingIntent);
//        views.setCharSequence(R.id.tv_widget_recipe_title, "setText", recipe.getName());
        views.setRemoteAdapter(R.id.ingredient_widget_list, serviceIntent);
        views.setEmptyView(R.id.ingredient_widget_list, R.id.tv_widget_empty);
        appWidgetManager.updateAppWidget(mAppwidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(mAppwidgetId ,R.id.ingredient_widget_list);

//        SharedPreferences prefs = getSharedPreferences(Consts.WIDGET_SHARED_PREFS, MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString(Consts.WIDGET_PREFS_KEY + mAppwidgetId ,recipe.getName());
//        editor.apply();

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppwidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

}
