package com.example.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.bakingapp.R;
import com.example.bakingapp.ui.MainActivity;
import com.example.bakingapp.utils.Consts;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {
    private static final String TAG = IngredientsWidgetProvider.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        //Start service for remoteAdapter
        Intent serviceIntent = new Intent(context, IngredientWidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        context.startService(serviceIntent);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        //setIntent for clicking the view
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        //set remoteAdapterViews
        views.setRemoteAdapter(R.id.ingredient_widget_list, serviceIntent);
        views.setOnClickPendingIntent(R.id.ingredients_widget_layout,pendingIntent);
        views.setEmptyView(R.id.ingredient_widget_list, R.id.tv_widget_empty);

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
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Widget onReceive: called");

        if(AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            int recipeId = intent.getIntExtra(Consts.WIDGET_RECIPE_ID_KEY, Consts.WIDGET_RECIPE_DEFAULT_ID);
            Log.d(TAG, "Widget onReceive: action update with recipe id: " + recipeId);
            Bundle bundle = new Bundle();
            bundle.putInt(Consts.WIDGET_RECIPE_ID_KEY, recipeId);
            appWidgetManager.updateAppWidgetOptions(appWidgetId, bundle);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.ingredient_widget_list);
        }
        super.onReceive(context, intent);
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

