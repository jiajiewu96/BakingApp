package com.example.bakingapp.ui;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.BaseApp;
import com.example.bakingapp.R;
import com.example.bakingapp.data.FavoriteListViewModel;
import com.example.bakingapp.data.FavoritesViewModel;
import com.example.bakingapp.data.RecipeRepository;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.ui.adapters.RecipeListAdapter;
import com.example.bakingapp.ui.fragmentInterfaces.CommonFragmentInterfaces;
import com.example.bakingapp.utils.Consts;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bakingapp.utils.Consts.FLAG_RECIPES;
import static com.example.bakingapp.utils.Consts.RECIPE_LIST_FRAGMENT_KEY;

public class RecipeListFragment extends Fragment implements RecipeListAdapter.RecipeClickHandler {

    private CommonFragmentInterfaces mTitleInterface;

    private RecipeListAdapter mRecipeListAdapter;
    private TextView mErrorTextView;
    private int flag;

    private FragmentActivity mFragmentActivity;

    OnRecipeListClickListener mRecipeListCallback;
    private TextView mEmptyTextView;
    private ActionBar mActionBar;
    private Context mContext;

    @Override
    public void onRecipeClick(Recipe recipe) {
        mRecipeListCallback.onRecipeSelected(recipe);
    }

    public interface OnRecipeListClickListener {
        void onRecipeSelected(Recipe recipe);
    }

    public RecipeListFragment(){

    }

    public static RecipeListFragment newInstance(int flag){
        RecipeListFragment fragment = new RecipeListFragment();
        Bundle args = new Bundle();
        args.putInt(RECIPE_LIST_FRAGMENT_KEY, flag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mRecipeListCallback = (OnRecipeListClickListener) context;
            mTitleInterface = (CommonFragmentInterfaces) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                     " Must implement OnRecipeClickListener");
        }
        mContext = getContext();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            flag = getArguments().getInt(RECIPE_LIST_FRAGMENT_KEY);
        } else {
            flag = FLAG_RECIPES;
        }
        if(mFragmentActivity == null) {
            mFragmentActivity = getActivity();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRecipeListCallback = null;
        mTitleInterface = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        mActionBar = ((AppCompatActivity) mFragmentActivity).getSupportActionBar();

        mTitleInterface.onFragmentChangedListener(mContext.getString(R.string.app_name));

        RecyclerView recipeListRecycler = (RecyclerView) rootView.findViewById(R.id.recycler_view_recipe_list);
        mErrorTextView = rootView.findViewById(R.id.tv_error);
        mEmptyTextView = rootView.findViewById(R.id.tv_empty);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recipeListRecycler.setLayoutManager(layoutManager);

        mRecipeListAdapter = new RecipeListAdapter(this);
        recipeListRecycler.setAdapter(mRecipeListAdapter);

        if(flag == Consts.FLAG_RECIPES) {
            loadRecipesFromJSON();

        } else if (flag == Consts.FLAG_FAVORITES){
            loadFavoritesFromDB();

        }

        return rootView;
    }

    private void loadFavoritesFromDB() {
        FavoriteListViewModel viewModel = ViewModelProviders.of(mFragmentActivity).get(FavoriteListViewModel.class);
        viewModel.getfavorites().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                ArrayList<Recipe> favorites = (ArrayList<Recipe>) recipes;
                mRecipeListAdapter.setRecipes(favorites);
            }
        });
    }

    private void loadRecipesFromJSON() {
        Application application = mFragmentActivity.getApplication();
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
        mErrorTextView.setText(getString(R.string.error_message));
    }
}
