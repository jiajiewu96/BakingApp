package com.example.bakingapp.widget;

import android.app.Application;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.AppExecutors;
import com.example.bakingapp.BaseApp;
import com.example.bakingapp.R;
import com.example.bakingapp.data.RecipeRepository;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.ui.RecipeActivity;
import com.example.bakingapp.ui.adapters.RecipeListAdapter;
import com.example.bakingapp.utils.Consts;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientsWidgetConfigActivity extends AppCompatActivity implements  RecipeListAdapter.RecipeClickHandler {
    private static final String TAG = IngredientsWidgetConfigActivity.class.getSimpleName();
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
        recipe.setWidget(Consts.FLAG_IS_WIDGET);
        addRecipeToDb(recipe);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        Intent serviceIntent = new Intent(this, IngredientWidgetService.class);
        setUpServiceIntent(recipe, serviceIntent);

        setUpRemoteViews(recipe, appWidgetManager, serviceIntent);

        setSharedPrefs(recipe);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppwidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    private void addRecipeToDb(final Recipe recipe) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ((BaseApp) getApplication()).getRepository().addRecipeToDB(recipe);
            }
        });
    }

    private void setUpServiceIntent(Recipe recipe, Intent serviceIntent) {
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppwidgetId);
        serviceIntent.putExtra(Consts.WIDGET_RECIPE_ID_KEY + mAppwidgetId, recipe.getId());
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        Log.d(TAG, "onRecipeClick Recipeid: " + recipe.getId());
    }

    private void setUpRemoteViews(Recipe recipe, AppWidgetManager appWidgetManager, Intent serviceIntent) {
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.ingredients_widget);
        views.setCharSequence(R.id.tv_widget_recipe_title, "setText", recipe.getName());
        views.setRemoteAdapter(R.id.ingredient_widget_list, serviceIntent);
        views.setEmptyView(R.id.ingredient_widget_list, R.id.tv_widget_empty);

        Intent clickIntent = new Intent(this, RecipeActivity.class);
        PendingIntent clickPendingIntent = PendingIntent.getActivity(this, 0, clickIntent, 0);
        views.setPendingIntentTemplate(R.id.ingredient_widget_list, clickPendingIntent);

        appWidgetManager.updateAppWidget(mAppwidgetId, views);
    }

    private void setSharedPrefs(Recipe recipe) {
        SharedPreferences prefs = getSharedPreferences(Consts.WIDGET_SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Consts.WIDGET_RECIPE_ID_KEY + mAppwidgetId ,recipe.getId());
        editor.putString(Consts.WIDGET_RECIPE_NAME_KEY + mAppwidgetId, recipe.getName());
        editor.apply();
    }

}
