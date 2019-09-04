package com.example.bakingapp.ui.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.bakingapp.R;
import com.example.bakingapp.ui.RecipeListFragment;

import static com.example.bakingapp.utils.Consts.FLAG_FAVORITES;
import static com.example.bakingapp.utils.Consts.FLAG_RECIPES;

public class TabAdapter extends FragmentStatePagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.recipe_list_title, R.string.favorites_list_title};
    private final Context mContext;

    public TabAdapter (Context context, FragmentManager fm){
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = RecipeListFragment.newInstance(FLAG_RECIPES);
                break;
            case 1:
                fragment = RecipeListFragment.newInstance(FLAG_FAVORITES);
                break;
            default:
                fragment = RecipeListFragment.newInstance(FLAG_RECIPES);
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
