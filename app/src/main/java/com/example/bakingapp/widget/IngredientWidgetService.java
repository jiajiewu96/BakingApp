package com.example.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingapp.R;
import com.example.bakingapp.data.FavoritesDatabase;
import com.example.bakingapp.data.RecipeRepository;
import com.example.bakingapp.model.Ingredients;
import com.example.bakingapp.model.Recipe;

import java.util.ArrayList;

public class IngredientWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsWidgetItemFactory(getApplicationContext(), intent);
    }
    class IngredientsWidgetItemFactory implements RemoteViewsFactory{
        private Context mContext;
        private int mAppWidgetId;
        private ArrayList<Ingredients> mIngredients = new ArrayList<>();
        private FavoritesDatabase mFavoritesDatabase;

        IngredientsWidgetItemFactory(Context context, Intent intent){
            mContext = context;
            this.mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        @Override
        public void onCreate() {
            mFavoritesDatabase = FavoritesDatabase.getInstance(mContext);
            mIngredients = mFavoritesDatabase.favoritesDao().getIngredients();
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {
            mFavoritesDatabase.close();
        }

        @Override
        public int getCount() {
            return mIngredients.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_ingredients_widget);
            views.setTextViewText(R.id.tv_widget_ingredient, mIngredients.get(i).getIngredient());
            views.setTextViewText(R.id.tv_widget_quantity, Float.toString(mIngredients.get(i).getQuantity()));
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
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
