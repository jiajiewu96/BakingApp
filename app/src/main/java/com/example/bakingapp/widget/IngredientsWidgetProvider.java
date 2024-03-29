package com.example.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.bakingapp.R;
import com.example.bakingapp.ui.RecipeActivity;
import com.example.bakingapp.utils.Consts;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {
    private static final String TAG = IngredientsWidgetProvider.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        //get sharedprefs for recipe id
        SharedPreferences preferences = context.getSharedPreferences(Consts.WIDGET_SHARED_PREFS, Context.MODE_PRIVATE);
        int id = preferences.getInt(Consts.WIDGET_RECIPE_ID_KEY + appWidgetId, -1);
        String recipeName = preferences.getString(Consts.WIDGET_RECIPE_NAME_KEY + appWidgetId, "");
        Log.d(TAG, "updateAppWidget recipeID: " +id);
        //Start service for remoteAdapter
        Intent serviceIntent = new Intent(context, IngredientWidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.putExtra(Consts.WIDGET_RECIPE_ID_KEY + appWidgetId, id);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

        //Create pending intent template
        Intent clickIntent = new Intent(context, RecipeActivity.class);
        PendingIntent clickPendingIntent = PendingIntent.getActivity(context, 0, clickIntent, 0);

        //set remoteAdapterViews
        views.setCharSequence(R.id.tv_widget_recipe_title, "setText", recipeName);
        views.setRemoteAdapter(R.id.ingredient_widget_list, serviceIntent);
        views.setEmptyView(R.id.ingredient_widget_list, R.id.tv_widget_empty);
        views.setPendingIntentTemplate(R.id.ingredient_widget_list, clickPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.ingredient_widget_list);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate: called");
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

