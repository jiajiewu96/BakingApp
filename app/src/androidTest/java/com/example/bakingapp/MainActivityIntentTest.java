package com.example.bakingapp;


import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.bakingapp.ui.MainActivity;
import com.example.bakingapp.ui.RecipeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;


@RunWith(AndroidJUnit4.class)
public class MainActivityIntentTest {

    public static final String RECIPE_NAME = "Nutella Pie";

    @Rule public IntentsTestRule<MainActivity> mMainActivityTestRule
            = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void checkRecipeListClick_checkActivityLaunch(){
        //Clicks on the first item in the adapter
        onView(allOf(withId(R.id.recycler_view_recipe_list), isCompletelyDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasComponent(RecipeActivity.class.getName()));
    }
}
