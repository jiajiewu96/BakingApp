package com.example.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingapp.BaseApp;
import com.example.bakingapp.R;
import com.example.bakingapp.data.RecipeRepository;
import com.example.bakingapp.model.Ingredients;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.utils.Consts;

import java.util.ArrayList;

public class IngredientWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsWidgetItemFactory(this.getApplicationContext(), intent);
    }

    class IngredientsWidgetItemFactory implements RemoteViewsFactory {
        private final String TAG = "IngredientWidgetService";
        private Context mContext;
        private int mAppWidgetId;
        private Recipe mRecipe;
        private ArrayList<Ingredients> mIngredients = new ArrayList<>();

        IngredientsWidgetItemFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            mRecipe = intent.getParcelableExtra(Consts.WIDGET_RECIPE_KEY);
            if (mRecipe != null) {
                Log.d(TAG, "Widget IngredientsWidgetItemFactory got recipe with id: " + mRecipe.getId());
            }else{
                Log.d(TAG, "Widget IngredientsWidgetItemFactory recipe is null ");
            }
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            if (mRecipe == null) {
                return;
            }
            mIngredients = mRecipe.getIngredients();
        }

        @Override
        public void onDestroy() {
            mIngredients.clear();
        }

        @Override
        public int getCount() {
            if (mIngredients != null) {
                return mIngredients.size();
            } else {
                return 0;
            }
        }

        @Override
        public RemoteViews getViewAt(int i) {
            if (mIngredients == null || mIngredients.size() == 0) return null;

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_ingredients_widget);
            String ingredientString = mIngredients.get(i).getIngredient();
            String quantityString = Float.toString(mIngredients.get(i).getQuantity()) + mIngredients.get(i).getMeasure();
            views.setTextViewText(R.id.tv_widget_ingredient, ingredientString);
            views.setTextViewText(R.id.tv_widget_quantity, quantityString);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
